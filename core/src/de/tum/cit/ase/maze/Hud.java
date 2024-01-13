package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

public class Hud implements Disposable {

    private Timer timer;
    private BitmapFont magicalFontButton;
    private long worldTimer = 300;
    private long timeCount;

    public Hud(MazeRunnerGame game) {
        timer = new Timer();

        // Load the TTF file for the new font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Create a BitmapFont for the TextButton with the new size parameter
        parameter.size = 40; // Set the font size as needed
        magicalFontButton = generator.generateFont(parameter);
    }

    public void update() {
        timeCount += (long) (Gdx.graphics.getDeltaTime() * 1000); // Convert elapsed time to milliseconds
        // Initial countdown time in seconds
        long initialWorldTimer = 600;
        worldTimer = initialWorldTimer - timeCount / 1000; // Calculate remaining time

//        (String.format("%02d:%02d", worldTimer / 60, worldTimer % 60));
    }

    @Override
    public void dispose() {
        timer.clear();;
    }
}
