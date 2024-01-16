package de.tum.cit.ase.maze.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ChooseLevelScreen class is responsible for displaying the all levels that are contained in LOCAL_DIRECTORY/maps.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class ChooseLevelScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for ChooseLevelScreen. Sets up the camera, viewport, stage, and UI elements.
     * @param game The main game class, used to access global resources and methods.
     */
    public ChooseLevelScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera(); // Create a camera
        camera.zoom = 1f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = getTable();
        stage.addActor(table); // Add the table to the stage

        table.add(new Label("Choose Level", game.getSkin(), "title")).padBottom(40).row(); // Add a title label

        FileHandle[] propertiesHandles = Gdx.files.local("maps").list(".properties"); // Get all files in LOCAL_DIRECTORY/maps that end with .properties

        // Filter out files that are not level maps
        List<FileHandle> levelMaps = Arrays.stream(propertiesHandles)
                .filter(file -> file.name().startsWith("level-") && file.name().endsWith(".properties"))
                .collect(Collectors.toList());

        // Ensure that only the first 5 level maps are considered (other maps can be loaded via button, but are not displayed as buttons)
        int size = Math.min(levelMaps.size(), 5);

        // Sort the level maps based on their names
        levelMaps.sort(Comparator.comparing(FileHandle::name));

        for (int i = 0; i < size; i++) {
            final int index = i + 1;
            TextButton levelButton = new TextButton(levelMaps.get(i).nameWithoutExtension(), game.getSkin(), "button"); // Create a button for each level map
            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setLevelIndex(index); // Set the level index in the game class
                    game.goToCurrentLevelIndexGame(); // Go to the game screen
                }
            });
            table.add(levelButton).width(400).height(80).row(); // Add the button to the table
        }

        TextButton selectFileButton = getTextButton(game); // Create a button to select a file
        table.add(selectFileButton).width(400).height(80).row(); // Add the button to the table
    }

    /**
     * Creates a table with a background image.
     * @return The table with the background image.
     */
    private Table getTable() {
        // Create a drawable from the texture
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("backgrounds/choose level background.png")));

        Table table = new Table();

        // Set the background
        backgroundDrawable.setMinWidth(stage.getWidth());
        backgroundDrawable.setMinHeight(stage.getHeight());
        table.setBackground(backgroundDrawable);

        table.setFillParent(true); // Make the table fill the stage
        table.padBottom(40);
        return table;
    }

    /**
     * Creates a button that allows the user to select a file.
     * @param game The main game class, used to access global resources and methods.
     * @return The button that allows the user to select a file.
     */
    private static TextButton getTextButton(MazeRunnerGame game) {
        TextButton selectFileButton = new TextButton("Select file", game.getSkin(), "button"); // Create a button to select a map file
        selectFileButton.addListener(new ChangeListener() {

            /**
             * Called when the button is clicked.
             * @param event The change event.
             * @param actor The actor that was clicked.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var fileChooserConfig = new NativeFileChooserConfiguration(); // Create a file chooser configuration
                fileChooserConfig.title = "Pick a map file"; // Title of the window that will be opened
                fileChooserConfig.intent = NativeFileChooserIntent.OPEN; // We want to open a file
                fileChooserConfig.nameFilter = (file, name) -> name.endsWith("properties"); // Only accept .properties files
                fileChooserConfig.directory = Gdx.files.local("maps"); // Set the directory to LOCAL_DIRECTORY/maps

                game.getFileChooser().chooseFile(fileChooserConfig, new NativeFileChooserCallback() {

                    /**
                     * Called when the user has selected a file.
                     * @param fileHandle The file that was selected.
                     */
                    @Override
                    public void onFileChosen(FileHandle fileHandle) {
                        // Do something with fileHandle
                        try {
                            game.getLevelMap().load(fileHandle); // Load the level map
                            game.getGameScreen().initializeLevel(); // Initialize the level
                            game.goToGame(); // Go to the game screen
                        }
                        catch (IOException e) {
                            Gdx.app.log("ERROR", "Failed to load level map at: " + fileHandle.path());
                        }
                    }

                    /**
                     * Called when the user has cancelled the file selection.
                     */
                    @Override
                    public void onCancellation() {
                        // User closed the window, don't need to do anything
                    }

                    /**
                     * Called when an error occurred while selecting a file.
                     * @param exception The exception that occurred.
                     */
                    @Override
                    public void onError(Exception exception) {
                        System.err.println("Error picking maze file: " + exception.getMessage());
                    }
                });
            }
        });
        return selectFileButton;
    }

    /**
     * Called when the screen should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Called when the screen size was changed.
     * @param width The new width in pixels.
     * @param height The new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    /**
     * Dispose of the stage when the screen is disposed.
     */
    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
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
