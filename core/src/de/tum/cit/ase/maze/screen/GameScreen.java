package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.Hud;
import de.tum.cit.ase.maze.LevelMap;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.entity.Entity;
import de.tum.cit.ase.maze.entity.EntryPoint;
import de.tum.cit.ase.maze.entity.Player;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    private static final float WIDTH = 1100f;
    private static final float HEIGHT = 600f;
    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;

    private final BitmapFont font;

    private LevelMap levelMap;

    private Array<Entity> floor;
    private Player player;

    private float mapWidth;
    private float mapHeight;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.5f;

        // Load the new font from the TTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40; // Set the desired font size
        font = generator.generateFont(parameter);
        generator.dispose(); // Important to dispose of the generator after creating the font

        initLevel();
    }

    /**
     * Init level using map
     */
    public void initLevel() {
        levelMap = game.getLevelMap();

        mapWidth = (int) levelMap.getMapWidth();
        mapHeight = (int) levelMap.getMapHeight();

        //Generate floor
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

        //Generate player
        EntryPoint entryPoint = levelMap.findEntryPoint();

        float mapCenterX = mapWidth / 2;
        float mapCenterY = mapHeight / 2;

        player = new Player(game);
        player.setX(entryPoint != null ? entryPoint.getX() + CELL_WIDTH / 2f : mapCenterX);
        player.setY(entryPoint != null ? entryPoint.getY() + CELL_HEIGHT / 2f : mapCenterY);
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 60f);

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        //Draw floor
        game.getSpriteBatch().begin();
        game.getSpriteBatch().setColor(1, 1, 1, 0.5f);
        for (Entity entity: floor) entity.draw(game.getSpriteBatch());

        //Draw entities that upper or on same level than player
        game.getSpriteBatch().setColor(1, 1, 1, 1);
        float playerLowerYPosition = player.getY() - CELL_HEIGHT;
        levelMap.getEntities().forEach(e -> {
            if (e.getY() >= playerLowerYPosition) e.draw(game.getSpriteBatch());
        });

        //Draw the player
        player.draw(game.getSpriteBatch());

        //Draw entities that lower than player
        levelMap.getEntities().forEach(e -> {
            if (e.getY() < playerLowerYPosition) e.draw(game.getSpriteBatch());
        });

        //Draw health
        float maxHealth = Player.DEFAULT_HEALTH;
        float health = player.getHealth();
        for (int i = 0; i < maxHealth; i++) {
            int imageIndex = getImageIndex(i, health, maxHealth);
            game.getSpriteBatch().draw(game.getHealthTextureRegionArray().get(imageIndex),
                    camera.position.x - maxHealth * CELL_WIDTH + i * CELL_WIDTH * 2,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_WIDTH * 2,
                    CELL_WIDTH * 2, CELL_WIDTH * 2);
        }

        //Draw key if player have it
        if (player.isHasKey()) {
            game.getSpriteBatch().draw(game.getKeyAnimation().getKeyFrames()[0],
                    camera.position.x - camera.viewportWidth * camera.zoom / 2,
                    camera.position.y + camera.viewportHeight * camera.zoom / 2 - CELL_HEIGHT,
                    CELL_WIDTH, CELL_HEIGHT);
        }

        //Draw debug
        //defaultFont.draw(game.getSpriteBatch(), "Game over: " + gameOver, 0, 0);
        game.getSpriteBatch().end();

        //drawDebugActionRectangles();
    }

    private int getImageIndex(int healthIndex, float health, float maxHealth) {
        return health - healthIndex >= 1 ? 0 : (health - healthIndex) < 0 ? 4 : (int) maxHealth - 1 -
                Math.floorDiv((int) ((health - healthIndex) * 100), Math.floorDiv(100, (int) maxHealth));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
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
