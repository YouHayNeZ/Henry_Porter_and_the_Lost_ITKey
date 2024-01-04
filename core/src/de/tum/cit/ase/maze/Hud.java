package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    private Stage stage;
    private long worldTimer;
    private long timeCount;

    private Viewport viewport;
    private Label countdownLabel;
    private final Animation<TextureRegion> heartAnimation;
    private float stateTime;


    public Hud(SpriteBatch batch) {
        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        heartAnimation = loadHeartAnimation();

        // Load the new font from the TTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
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
        countdownLabel.setAlignment(Align.center); // Align the label in the center

        table.add(countdownLabel).expandX().padTop(10); // Add the countdown label to the table
        stage.addActor(table); // Add the table to the stage
    }

    private Animation<TextureRegion> loadHeartAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("objects.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> heartSpin = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col ++) {
            heartSpin.add(new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }

        return new Animation<>(0.1f, heartSpin);
    }

    public void update() {
        timeCount += TimeUtils.millis();
        worldTimer = (long) (timeCount / 100_000_000_000_000f);
        countdownLabel.setText(String.format("%02d:%02d", worldTimer / 60, worldTimer % 60));
    }

    public void render(SpriteBatch batch) {
        update();
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        batch.draw(heartAnimation.getKeyFrame(stateTime, true), 300, 550, 50, 50);
        batch.draw(heartAnimation.getKeyFrame(stateTime, true), 360, 550, 50, 50);
        batch.draw(heartAnimation.getKeyFrame(stateTime, true), 420, 550, 50, 50);
    }

    public Stage getStage() {
        return stage;
    }
}
