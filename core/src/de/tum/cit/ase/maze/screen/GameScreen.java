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
    private final Hud hud;

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
        hud = new Hud(game.getSpriteBatch());

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Load the new font from the TTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40; // Set the desired font size
        font = generator.generateFont(parameter);
        generator.dispose(); // Important to dispose of the generator after creating the font

        //Generate player
        player = new Player(game);
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

        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        float textX = (camera.position.x);
        float textY = (camera.position.y);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text
        font.draw(game.getSpriteBatch(), "Press ESC to go back to the Menu", textX, textY);

        // Render the character
        this.player.render(delta, game.getSpriteBatch());
        this.hud.render(game.getSpriteBatch());

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        // Set up and begin drawing with the HUD stage
        game.getSpriteBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
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
