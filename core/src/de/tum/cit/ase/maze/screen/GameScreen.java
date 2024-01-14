package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import de.tum.cit.ase.maze.LevelMap;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.entity.*;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    private static final float WIDTH = 1100f;
    private static final float HEIGHT = 600f;
    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;

    private static final float CAMERA_SPEED = 50f;
    private static final float PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT = 0.8f;
    private static final float PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT = 0.7f;

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;

    private BitmapFont defaultFont;
    private BitmapFont boldFont;

    private LevelMap levelMap;

    private Array<Entity> floor;
    private Player player;

    private float mapWidth;
    private float mapHeight;

    private float cameraDestX;
    private float cameraDestY;

    private boolean gameOver = false;

    private float timeLeft = 20;
    private float accumulator = 0.0f;
    private Timer timer;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        camera.zoom = 0.5f;

        // Get the font from the game's skin
        defaultFont = game.getSkin().getFont("font");

        boldFont = game.getSkin().getFont("bold");
        boldFont.getData().setScale(0.5f);

        initializeLevel();
    }

    /**
     * Initialize level using map.
     */
    public void initializeLevel() {
        levelMap = game.getLevelMap();

        mapWidth = (int) levelMap.getMapWidth();
        mapHeight = (int) levelMap.getMapHeight();

        // Generate floor
        int columns = Math.floorDiv((int) mapWidth, CELL_WIDTH);
        int rows = Math.floorDiv((int) mapHeight, CELL_HEIGHT);

        floor = new Array<>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Entity entity = new Entity(game);
                entity.setTextureRegion(game.getFloorTextureRegion());
                entity.setX(i * CELL_WIDTH);
                entity.setY(j * CELL_HEIGHT);
                floor.add(entity);
            }
        }

        // Generate player
        EntryPoint entryPoint = levelMap.findEntryPoint();

        float mapCenterX = mapWidth / 2;
        float mapCenterY = mapHeight / 2;

        player = new Player(game);
        player.setX(entryPoint != null ? entryPoint.getX() + CELL_WIDTH / 2f : mapCenterX);
        player.setY(entryPoint != null ? entryPoint.getY() + CELL_HEIGHT / 2f : mapCenterY);


        //initialize timer
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!gameOver) {
                    timeLeft -= 1; // Decrease time by 1 second
                    if (timeLeft <= 0) {
                        // Time's up, end the game
                        gameOver = true;
                        game.goToEndGame(false);
                        timer.stop();  // Stop the timer to prevent further decrements
                    }
                }
            }
        }, 1, 1); // Schedule the task to run every 1 second
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 60f);

        // Check for escape key press or game over to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if (game.isPlaying() && !game.isPaused()) {
            // Update camera destination position (only map bigger than viewport)
            if ((mapWidth > camera.viewportWidth * camera.zoom || mapHeight > camera.viewportHeight * camera.zoom) &&
                    (player.getX() + PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT * camera.viewportWidth * camera.zoom / 2 < cameraDestX ||
                            player.getX() - PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT * camera.viewportWidth * camera.zoom / 2 > cameraDestX ||
                            player.getY() + PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT * camera.viewportHeight * camera.zoom / 2 < cameraDestY ||
                            player.getY() - PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT * camera.viewportHeight * camera.zoom / 2 > cameraDestY)) {

                cameraDestX = player.getX();
                cameraDestY = player.getY();
            }

            clampCameraDestPosition();

            // Update camera position
            float xDiff = cameraDestX - camera.position.x;
            float yDiff = cameraDestY - camera.position.y;
            if (xDiff < 0) camera.position.x -= Math.max(delta * CAMERA_SPEED, xDiff);
            else if (xDiff > 0) camera.position.x += Math.min(delta * CAMERA_SPEED, xDiff);
            if (yDiff < 0) camera.position.y -= Math.max(delta * CAMERA_SPEED, yDiff);
            else if (yDiff > 0) camera.position.y += Math.min(delta * CAMERA_SPEED, yDiff);
            camera.update();

            // Update all updatable entities
            int size = levelMap.getEntities().size;
            if (!gameOver) {
                for (int i = 0; i < size; i++) {
                    Entity entity = levelMap.getEntities().get(i);
                    if (entity instanceof UpdatableEntity updatableEntity) updatableEntity.update(delta);
                }
                player.update(delta);
            }

            // Check player collision with exit
            Rectangle playerRectangle = player.getEntityRectangle();
            size = levelMap.getEntities().size;
            for (int i = 0; i < size; i++) {
                Entity entity = levelMap.getEntities().get(i);
                if (entity instanceof Exit exit) {
                    if (exit.isOpen() && Intersector.overlaps(playerRectangle, exit.getExitRectangle())) {
                        game.goToEndGame(true);
                    } else if (player.isHasAtLeastOneKey() && Intersector.overlaps(playerRectangle, exit.getActionRectangle())) {
                        exit.open();
                    }
                }
            }
        }

        // Check player health
        if (player.getHealth() <= 0) {
            game.goToEndGame(false);
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);

        // Draw floor
        game.getSpriteBatch().begin();
        game.getSpriteBatch().setColor(1, 1, 1, 0.5f);
        for (Entity entity: floor) {
            entity.draw(game.getSpriteBatch());
        }

        // Draw entities that upper or on same level as player
        game.getSpriteBatch().setColor(1, 1, 1, 1);
        float playerLowerYPosition = player.getY() - CELL_HEIGHT;
        levelMap.getEntities().forEach(entity -> {
            if (entity.getY() >= playerLowerYPosition) {
                entity.draw(game.getSpriteBatch());
            }
        });

        // Draw the player
        player.draw(game.getSpriteBatch());

        // Draw entities that lower than player
        levelMap.getEntities().forEach(entity -> {
            if (entity.getY() < playerLowerYPosition) {
                entity.draw(game.getSpriteBatch());
            }
        });

        // Draw health
        float maxHealth = Player.DEFAULT_HEALTH;
        float health = player.getHealth();
        for (int i = 0; i < maxHealth; i++) {
            int imageIndex = getImageIndex(i, health, maxHealth);
            game.getSpriteBatch().draw(game.getHealthTextureRegionArray().get(imageIndex),
                    camera.position.x - maxHealth * CELL_WIDTH + i * CELL_WIDTH * 2,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_WIDTH * 2,
                    CELL_WIDTH * 2, CELL_WIDTH * 2);
        }

        // Draw keys if player has them
        int keys = player.getCollectedKeys();
        for (int i = 0; i < keys; i++) {
            game.getSpriteBatch().draw(game.getKeyAnimation().getKeyFrames()[0],
                    camera.position.x - camera.viewportWidth * camera.zoom / 2 + i * CELL_WIDTH + 4,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_WIDTH - 4,
                    CELL_WIDTH, CELL_HEIGHT);
        }
//        if (player.isHasAllKeys()) {
//            game.getSpriteBatch().draw(game.getKeyAnimation().getKeyFrames()[0],
//                    camera.position.x - camera.viewportWidth * camera.zoom / 2,
//                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_HEIGHT,
//                    CELL_WIDTH, CELL_HEIGHT);
//        }

        // Draw end game ????
        /*
        if (gameOver) {
            String message = player.getHealth() > 0 ? "You win" : "You die";
            GlyphLayout glyphLayout = new GlyphLayout();
            glyphLayout.setText(defaultFont, message);

            defaultFont.draw(game.getSpriteBatch(), message,
                    camera.position.x - glyphLayout.width / 2,
                    camera.position.y + glyphLayout.height * 2);

            message = "Press ESC button to continue";
            glyphLayout.setText(boldFont, message);
            boldFont.draw(game.getSpriteBatch(), message,
                    camera.position.x - glyphLayout.width / 2,
                    camera.position.y + glyphLayout.height / 2);
        }
         */

        // Draw timer
        drawTimer();

        // Draw debug
        // defaultFont.draw(game.getSpriteBatch(), "Game over: " + gameOver, 0, 0);
        game.getSpriteBatch().end();

         drawDebugActionRectangles();
    }

    private void drawDebugActionRectangles() {
        game.getShapeRenderer().begin();

        // Draw player rectangle
        game.getShapeRenderer().setColor(Color.WHITE);
        drawRectangle(player.getEntityRectangle());

        // Draw exit rectangles
        int size = game.getLevelMap().getEntities().size;
        for (int i = 0; i < size; i++) {
            Entity entity = game.getLevelMap().getEntities().get(i);
            if (entity instanceof Exit exit) {
                // Action rectangle
                game.getShapeRenderer().setColor(Color.RED);
                drawRectangle(exit.getActionRectangle());

                game.getShapeRenderer().setColor(Color.PURPLE);
                drawRectangle(exit.getExitRectangle());
            }
            if (entity instanceof Enemy) {
                game.getShapeRenderer().setColor(Color.RED);
                drawRectangle(entity.getEntityRectangle());
            }
        }

        game.getShapeRenderer().end();
    }

    private void drawRectangle(Rectangle rectangle) {
        game.getShapeRenderer().rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    // Clamp camera destination position (only need if map bigger than camera viewport)
    private void clampCameraDestPosition() {
        if (mapWidth > camera.viewportWidth / 2) {
            if (cameraDestX < camera.viewportWidth / 4) {
                cameraDestX = camera.viewportWidth / 4;
            }
            if (cameraDestX > mapWidth - camera.viewportWidth / 4) {
                cameraDestX = mapWidth - camera.viewportWidth / 4;
            }
        }
        if (mapHeight > camera.viewportHeight / 2) {
            if (cameraDestY < camera.viewportHeight / 4) {
                cameraDestY = camera.viewportHeight / 4;
            }
            if (cameraDestY > mapHeight - camera.viewportHeight / 4 + CELL_WIDTH * 2) {
                cameraDestY = mapHeight - camera.viewportHeight / 4 + CELL_WIDTH * 2;
            }
        }
    }

    private int getImageIndex(int healthIndex, float health, float maxHealth) {
        return health - healthIndex >= 1 ? 0 : (health - healthIndex) < 0 ? 4 : (int) maxHealth - 1 -
                Math.floorDiv((int) ((health - healthIndex) * 100), Math.floorDiv(100, (int) maxHealth));
    }

    private void drawTimer() {
        int minutes = (int) (timeLeft / 60);
        int seconds = (int) (timeLeft % 60);

        String timerText = String.format("%02d:%02d", minutes, seconds);

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(defaultFont, timerText);

        // Draw the timer in the top right corner
        defaultFont.draw(
                game.getSpriteBatch(),
                timerText,
                camera.position.x + camera.viewportWidth * camera.zoom / 2 - glyphLayout.width - 4,
                camera.position.y + camera.viewportHeight * camera.zoom / 2 - 4
        );
    }



    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        //Set camera on center map
        cameraDestX = mapWidth < camera.viewportWidth * camera.zoom ? mapWidth / 2 : player.getX();
        cameraDestY = mapHeight < camera.viewportHeight * camera.zoom ? mapHeight / 2 : player.getY();

        //Clamp camera position (only need if map bigger than camera viewport)
        clampCameraDestPosition();

        camera.position.set(cameraDestX, cameraDestY, 0);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
