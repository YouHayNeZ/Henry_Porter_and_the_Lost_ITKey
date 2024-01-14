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
        titleLabel = new Label("", game.getSkin(), "title");  // Set the Magical Font style
        table.add(titleLabel).padBottom(40).row();

        nextLevelButton = new TextButton("", game.getSkin(), "button");
        table.add(nextLevelButton).width(400).height(80).row();

        TextButton goToMenuButton = new TextButton("Go to menu", game.getSkin(), "button");
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        });
        table.add(goToMenuButton).width(400).height(80).row();
    }

    public void setIsWinner(boolean isWinner) {
        titleLabel.setText(isWinner ? "You win!" : "You lose!");
        nextLevelButton.setText(isWinner ? "Next level" : "Restart level");

        // Remove all change listeners before add new one
        for (int i = 0; i < nextLevelButton.getListeners().size; i++) {
            EventListener listener = nextLevelButton.getListeners().get(i);
            if (listener instanceof ChangeListener) {
                nextLevelButton.getListeners().removeIndex(i);
                i--;
            }
        }

        nextLevelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isWinner) {
                    game.incrementLevel();
                }
                game.goToCurrentLevelIndexGame();
            }
        });

        // Set background based on winning or losing
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(
                new TextureRegion(isWinner ? new Texture("backgrounds/winning background.png") : new Texture("backgrounds/losing background.png")));

        // Set the background
        background.setDrawable(backgroundDrawable);
        background.setSize(stage.getWidth(), stage.getHeight());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
