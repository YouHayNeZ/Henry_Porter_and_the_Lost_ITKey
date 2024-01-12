package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    MazeRunnerGame game;

    private final Stage stage;
    private long worldTimer;
    private long timeCount;

    private final Viewport viewport;
    private final Label countdownLabel;

    public Hud(SpriteBatch batch) {
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top(); // Put the table at the top of the stage

        // Create label with the loaded font
        countdownLabel = new Label(String.valueOf(worldTimer), game.getLabelStyle());
        countdownLabel.setAlignment(Align.left); // Align the label on the left side

        // Add the countdown label to the table
        table.add(countdownLabel).expandX().padTop(10);

        stage.addActor(table); // Add the table to the stage
    }

    public void update() {
        timeCount += (long) (Gdx.graphics.getDeltaTime() * 1000); // Convert elapsed time to milliseconds
        // Initial countdown time in seconds
        long initialWorldTimer = 600;
        worldTimer = initialWorldTimer - timeCount / 1000; // Calculate remaining time

        if (worldTimer < 0) {
            worldTimer = 0; // Ensure the timer doesn't go below 0
        }

        countdownLabel.setText(String.format("%02d:%02d", worldTimer / 60, worldTimer % 60));
    }

    public void render(SpriteBatch batch) {
        update();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
