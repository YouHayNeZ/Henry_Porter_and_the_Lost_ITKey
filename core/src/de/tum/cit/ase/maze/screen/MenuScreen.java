package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {
    private final MazeRunnerGame game;
    private final Stage stage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;

        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = getTable();
        stage.addActor(table); // Add the table to the stage

        // Create and add a label as a title with the title font
        Label titleLabel = new Label("Henry Porter and the\n Lost ITKey", game.getSkin(), "title");
        titleLabel.setAlignment(Align.center); // Align the label to the center
        table.add(titleLabel).padBottom(40).row(); // Add the label to the table

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Start Game", game.getSkin(), "button");
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToCurrentLevelIndexGame(); // Change to the game screen when the button is pressed
            }
        });
        table.add(goToGameButton).width(400).height(80).row(); // Add the button to the table

        // Create and add a button to continue the game
        TextButton Continue = new TextButton("Continue", game.getSkin(), "button");
        Continue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isPlaying()) {
                    game.goToGame(); // Continue to the game screen when the button is pressed
                }
            }
        });
        table.add(Continue).width(400).height(80).row(); // Add the button to the table

        // Create and add a button to choose a maze file
        TextButton goToChooseLevelButton = new TextButton("Choose Level", game.getSkin(), "button");
        goToChooseLevelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToChooseLevel(); // Change to choose level screen when button is pressed
            }
        });
        table.add(goToChooseLevelButton).width(400).height(80).row(); // Add the button to the table

        // Create and add a button to exit the game
        TextButton exitButton = new TextButton("Exit Game", game.getSkin(), "button");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Close the application when the button is pressed
            }
        });
        table.add(exitButton).width(400).height(80).row(); // Add the button to the table
    }

    /**
     * Creates a table with a background image.
     * @return The table with the background image.
     */
    private Table getTable() {
        // Create a drawable from the texture
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("backgrounds/menu background.png")));

        Table table = new Table(); // Create a table for layout

        // Set the background
        backgroundDrawable.setMinWidth(stage.getWidth());
        backgroundDrawable.setMinHeight(stage.getHeight());
        table.setBackground(backgroundDrawable);

        table.setFillParent(true); // Make the table fill the stage
        table.padBottom(100); // Add padding to the bottom of the table
        return table;
    }

    /**
     * Renders the stage and clears the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (game.isPlaying() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.goToGame(); // Continue to the game screen when enter is pressed
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Updates the stage viewport on resize.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    /**
     * Disposes of the stage when the screen is disposed.
     */
    @Override
    public void dispose() {
        // Dispose of the stage when the screen is disposed
        stage.dispose();
    }

    /**
     * Sets the input processor to the stage when the screen is shown.
     */
    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    /**
     * Sets the input processor to null when the screen is hidden.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
