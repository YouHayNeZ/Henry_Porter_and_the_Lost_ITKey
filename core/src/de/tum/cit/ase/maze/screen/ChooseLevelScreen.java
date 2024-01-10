package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.IOException;

/**
 * The ChooseLevelScreen class is responsible for displaying the all levels that contains in LOCAL_DIRECTORY/maps.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class ChooseLevelScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for ChooseLevelScreen. Sets up the camera, viewport, stage, and UI elements.
     * @param game The main game class, used to access global resources and methods.
     */
    public ChooseLevelScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        // Create a drawable from the texture
        Table table = getTable(game);
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Choose Level", game.getLabelStyle())).padBottom(40).row();

        FileHandle[] propertiesHandles = Gdx.files.local("maps").list(".properties");
        int size = propertiesHandles.length;
        for (int i = 0; i < size; i++) {
            final int index = i + 1;
            TextButton levelButton = new TextButton(propertiesHandles[i].nameWithoutExtension(), game.getTextButtonStyle());
            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setLevelIndex(index);
                    game.goToCurrentLevelIndexGame();
                }
            });
            table.add(levelButton).width(400).row();
        }

        TextButton selectFileButton = getTextButton(game);
        table.add(selectFileButton).width(400).row();
    }

    private Table getTable(MazeRunnerGame game) {
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(game.getMazeBackground()));

        Table table = new Table(); // Create a table for layout

        // Set the background
        backgroundDrawable.setMinWidth(stage.getWidth());
        backgroundDrawable.setMinHeight(stage.getHeight());
        table.setBackground(backgroundDrawable);

        table.setFillParent(true); // Make the table fill the stage
        return table;
    }

    private static TextButton getTextButton(MazeRunnerGame game) {
        TextButton selectFileButton = new TextButton("Select file", game.getSkin());
        selectFileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NativeFileChooserConfiguration configuration = new NativeFileChooserConfiguration();
                configuration.title = "Select File";
                configuration.nameFilter = (dir, name) -> name.endsWith(".properties");
                configuration.directory = Gdx.files.local("maps");
                game.getFileChooser().chooseFile(configuration, new NativeFileChooserCallback() {

                    @Override
                    public void onFileChosen(FileHandle file) {
                        try {
                            game.getLevelMap().load(file);
                            game.getGameScreen().initLevel();
                            game.goToGame();
                        }
                        catch (IOException e) {
                            Gdx.app.log("ERROR", "Failed to load level map at: " + file.path());
                        }
                    }

                    @Override
                    public void onCancellation() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Gdx.app.log("ERROR", "Error while choosing file", e);
                    }
                });
            }
        });
        return selectFileButton;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

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

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
