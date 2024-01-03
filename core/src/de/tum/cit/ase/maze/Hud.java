package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    public Stage stage;
    public Integer worldTimer;
    public float timeCount;

    private Viewport viewport;
    private Label countdownLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        // Load the new font from the TTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40; // Set the desired font size
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Don't forget to dispose the generator

        Table table = new Table();
        table.top(); // Put the table at the top of the stage
        table.setFillParent(true); // Make the table fill the entire stage

        // Create label with the loaded font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        countdownLabel = new Label(String.valueOf(worldTimer), labelStyle);

        table.add(countdownLabel).expandX().padTop(10); // Add the countdown label to the table
        stage.addActor(table); // Add the table to the stage
    }
}
