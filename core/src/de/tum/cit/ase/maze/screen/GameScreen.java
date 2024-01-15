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
import de.tum.cit.ase.maze.LevelMap;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.entity.*;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    // World set up
    private static final float WIDTH = 1100f;
    private static final float HEIGHT = 600f;
    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;
    private float mapWidth;
    private float mapHeight;

    // Camera set up
    private static final float CAMERA_SPEED = 50f;
    private static final float PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT = 0.8f;
    private static final float PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT = 0.7f;
    private final OrthographicCamera camera;
    private float cameraDestX;
    private float cameraDestY;

    private final MazeRunnerGame game;

    private final BitmapFont magicalFont;

    private LevelMap levelMap;

    private Array<Entity> floor;
    private Player player;

    private float timeLeft;


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
        magicalFont = game.getSkin().getFont("magical_font");

        initializeLevel();
    }

    /**
     * Initializes the level by generating the floor and player.
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

        timeLeft = 300;
    }

    // Screen interface methods with necessary functionality

    /**
     * Renders the game screen. This method is called every frame.
     * Does the most important things.
     */
    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 60f);

        // Check for escape key press or game over to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if (game.isPlaying() && !game.isPaused()) {
            // Update camera destination position (only map bigger than viewport)
            if (timeLeft > 0) {
                timeLeft -= delta; // Update timer
            } else {
                game.goToEndGame(false); // Time is up and player loses
            }

            // Update camera destination position (only map bigger than viewport)
            if ((mapWidth > camera.viewportWidth * camera.zoom || mapHeight > camera.viewportHeight * camera.zoom) &&
                    (player.getX() + PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT * camera.viewportWidth * camera.zoom / 2 < cameraDestX ||
                            player.getX() - PLAYER_AND_CAMERA_MAX_DIFF_X_PERCENT * camera.viewportWidth * camera.zoom / 2 > cameraDestX ||
                            player.getY() + PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT * camera.viewportHeight * camera.zoom / 2 < cameraDestY ||
                            player.getY() - PLAYER_AND_CAMERA_MAX_DIFF_Y_PERCENT * camera.viewportHeight * camera.zoom / 2 > cameraDestY)) {

                cameraDestX = player.getX();
                cameraDestY = player.getY();
            }

            clampCameraDestPosition(); // Clamp camera position (only need if map bigger than camera viewport)

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
            for (int i = 0; i < size; i++) {
                Entity entity = levelMap.getEntities().get(i);
                if (entity instanceof UpdatableEntity updatableEntity) updatableEntity.update(delta);
            }
            player.update(delta);

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

        // Draw coins if player has them
        int coins = player.getCollectedCoins();
        if (coins > 0) {
            // Save the original scale
            float originalScaleX = magicalFont.getData().scaleX;
            float originalScaleY = magicalFont.getData().scaleY;

            // Set the desired scale (adjust these values accordingly)
            magicalFont.getData().setScale(0.3f, 0.3f);

            game.getSpriteBatch().draw(game.getCoinAnimation().getKeyFrames()[0],
                    camera.position.x - camera.viewportWidth * camera.zoom / 2 + keys * CELL_WIDTH + 4,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_WIDTH - 4,
                    CELL_WIDTH, CELL_HEIGHT);

            magicalFont.draw(game.getSpriteBatch(), "x" + coins,
                    camera.position.x - camera.viewportWidth * camera.zoom / 2 + keys * CELL_WIDTH + 4 + CELL_WIDTH,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_WIDTH - 4 + CELL_HEIGHT);

            // Revert to the original scale
            magicalFont.getData().setScale(originalScaleX, originalScaleY);
        }

        // Draw timer
        drawTimer();

        // Draw debug
        game.getSpriteBatch().end();

        //drawDebugActionRectangles();
    }

    /**
     * Draws the debug action rectangles for the player and exits.
     */
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

    /**
     * Draws a rectangle using the shape renderer.
     * @param rectangle The rectangle to draw.
     */
    private void drawRectangle(Rectangle rectangle) {
        game.getShapeRenderer().rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    /**
     * Draws the debug collision rectangles for the player and exits (only needed if map bigger than camera viewport).
     */
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

    /**
     * Gets the index of the health image to draw.
     * @param healthIndex The index of the health image.
     * @param health The current health of the player.
     * @param maxHealth The maximum health of the player.
     * @return The index of the health image to draw.
     */
    private int getImageIndex(int healthIndex, float health, float maxHealth) {
        return health - healthIndex >= 1 ? 0 : (health - healthIndex) < 0 ? 4 : (int) maxHealth - 1 -
                Math.floorDiv((int) ((health - healthIndex) * 100), Math.floorDiv(100, (int) maxHealth));
    }

    /**
     * Draws the timer in the top right corner.
     */
    private void drawTimer() {
        int minutes = (int) (timeLeft / 60); // Get the minutes
        int seconds = (int) (timeLeft % 60); // Get the seconds

        String timerText = String.format("%02d:%02d", minutes, seconds); // Format the timer text

        GlyphLayout glyphLayout = new GlyphLayout(); // Create a glyph layout to get the width of the text
        glyphLayout.setText(magicalFont, timerText); // Set the text to the glyph layout

        // Draw the timer in the top right corner
        magicalFont.draw(
                game.getSpriteBatch(), timerText,
                camera.position.x + camera.viewportWidth * camera.zoom / 2 - glyphLayout.width - 4,
                camera.position.y + camera.viewportHeight * camera.zoom / 2 + CELL_HEIGHT - 8
        );
    }

    /**
     * Gets the time left of the timer.
     * @return the time left of the timer.
     */
    public float getTimeLeft() {
        return timeLeft;
    }

    /**
     * Sets the time left.
     * @param timeLeft The time left.
     */
    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * Resize method is called when the window is resized.
     * @param width to resize to.
     * @param height to resize to.
     */
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
