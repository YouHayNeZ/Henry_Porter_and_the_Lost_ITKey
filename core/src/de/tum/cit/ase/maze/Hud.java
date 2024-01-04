package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, batch);
        heartAnimation = loadHeartAnimation();

        // Load the new font from the TTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40; // Set the desired font size
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Dispose the generator

        Table table = new Table();
        table.top(); // Put the table at the top of the stage
        table.setFillParent(true); // Make the table fill the entire stage

        // Create label with the loaded font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        countdownLabel = new Label(String.valueOf(worldTimer), labelStyle);
        countdownLabel.setAlignment(Align.center); // Align the label in the center

        // Add the countdown label to the table
        table.add(countdownLabel).expandX().padTop(10);

        for (int i = 0; i < 5; i++) {
            HeartActor heartActor = new HeartActor(heartAnimation);
            table.add(heartActor).width(50).height(50).pad(5);
        }

        //Animate the hearts
        for (Actor actor : table.getChildren()) {
            actor.addAction(Actions.forever(Actions.sequence(Actions.scaleTo(1.2f, 1.2f, 0.5f), Actions.scaleTo(1f, 1f, 0.5f))));
        }

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
        for (int col = 0; col < animationFrames; col++) {
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
    }

    public Stage getStage() {
        return stage;
    }
}
