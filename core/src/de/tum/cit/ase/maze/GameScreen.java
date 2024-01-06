package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;

    private final Hud hud;

    private final Character character;

    //Box2D stuff
    private World world;
    private Box2DDebugRenderer b2dr;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

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

        //Box2D stuff
        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0,0);
        Body groundBody = world.createBody(bdef);

        PolygonShape ground = new PolygonShape();
        ground.setAsBox(camera.viewportWidth, camera.viewportHeight);
        groundBody.createFixture(ground, 0.0f);
        ground.dispose();

        this.character = new Character(world);
        hud = new Hud(game.getSpriteBatch());
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x);
        float textY = (float) (camera.position.y);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text
        font.draw(game.getSpriteBatch(), "Press ESC to go back to the Menu", textX, textY);

        // Render the character
        this.character.render(delta, game.getSpriteBatch());
        this.hud.render(game.getSpriteBatch());

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        // Set up and begin drawing with the HUD stage
        game.getSpriteBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hud.getStage().draw();

        //Box2D stuff
        // Update the Box2D world
        world.step(1/60f, 6, 2);

        // Render the Box2D debug lines
        b2dr.render(world, camera.combined);
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
        font.dispose();
        hud.dispose();
        world.dispose();
        b2dr.dispose();
    }

    // Additional methods and logic can be added as needed for the game screen
}
