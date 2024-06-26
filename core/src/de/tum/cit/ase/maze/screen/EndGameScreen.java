package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * The EndGameScreen class is responsible for displaying the end game menu when the game is over.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class EndGameScreen implements Screen {

    private final MazeRunnerGame game;
    private final Stage stage;
    private final Image background;

    // UI elements
    private final Label titleLabel;
    private final TextButton nextLevelButton;

    /**
     * Constructor for EndGameScreen. Sets up the camera, viewport, stage, and UI elements.
     * @param game The main game class, used to access global resources and methods.
     */
    public EndGameScreen(MazeRunnerGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        camera.zoom = 1f;

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport); // Create a stage for UI elements

        background = new Image();
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        titleLabel = new Label("", game.getSkin(), "title"); // Use the "title" style from the skin
        table.add(titleLabel).padBottom(40).row(); // Add the label to the table

        nextLevelButton = new TextButton("", game.getSkin(), "button"); // Use the "button" style from the skin
        table.add(nextLevelButton).width(400).height(80).row(); // Add the button to the table

        TextButton goToMenuButton = new TextButton("Go to menu", game.getSkin(), "button"); // Use the "button" style from the skin
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        }); // Add a listener to the button
        table.add(goToMenuButton).width(400).height(80).row(); // Add the button to the table
    }

    /**
     * Sets the screen to display the winning or losing screen.
     * @param isWinner True if the player won the game, false if the player lost the game.
     */
    public void setIsWinner(boolean isWinner) {
        titleLabel.setText(isWinner ? "You win!" : "You lose!"); // Set the title based on winning or losing
        nextLevelButton.setText(isWinner ? "Next level" : "Restart level"); // Set the button text based on winning or losing

        // Remove all change listeners before add new one
        for (int i = 0; i < nextLevelButton.getListeners().size; i++) {
            EventListener listener = nextLevelButton.getListeners().get(i); // Get the listener
            if (listener instanceof ChangeListener) {
                nextLevelButton.getListeners().removeIndex(i); // Remove the listener
                i--; // Decrement the index
            }
        }

        nextLevelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isWinner) {
                    game.incrementLevel(); // Increment the level
                }
                game.goToCurrentLevelIndexGame(); // Go to the current level
            }
        });

        // Set background based on winning or losing
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(
                new TextureRegion(isWinner ? new Texture("backgrounds/winning background.png") : new Texture("backgrounds/losing background.png")));

        // Set the background
        background.setDrawable(backgroundDrawable);
        background.setSize(stage.getWidth(), stage.getHeight()); // Set the size of the background
    }

    /**
     * Sets the screen to display the game over screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor
    }

    /**
     * Renders the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Resizes the screen.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
        background.setSize(stage.getWidth(), stage.getHeight()); // Update the background size on resize
    }

    /**
     * Pauses the screen.
     */
    @Override
    public void pause() {

    }

    /**
     * Resumes the screen.
     */
    @Override
    public void resume() {

    }

    /**
     * Hides the screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); // Remove the stage as the input processor
    }

    /**
     * Disposes the screen.
     */
    @Override
    public void dispose() {
        stage.dispose(); // Dispose the stage
    }
}
