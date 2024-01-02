package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private final Texture mazeBackground;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        // Load the maze background texture
        mazeBackground = new Texture(Gdx.files.internal("assets/Maze Background.png"));

        // Load the TTF file for the new font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120; // Set the font size as needed

        // Create a BitmapFont from the TTF file for the title
        BitmapFont magicalFontTitle = generator.generateFont(parameter);
        generator.dispose(); // Dispose of the generator when done

        // Create a drawable from the texture
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(mazeBackground));

        Table table = new Table(); // Create a table for layout

        // Set the background
        backgroundDrawable.setMinWidth(stage.getWidth());
        backgroundDrawable.setMinHeight(stage.getHeight());
        table.setBackground(backgroundDrawable);

        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title with the title font
        Label titleLabel = new Label("Harry Potter and the\nFinal ITP Exam", new Label.LabelStyle(magicalFontTitle, Color.GOLD));
        titleLabel.setAlignment(Align.center);
        table.add(titleLabel).padBottom(80).row();

        // Create a new FreeTypeFontGenerator for the button font
        FreeTypeFontGenerator buttonFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter buttonParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        buttonParameter.size = 40; // Set the font size for the TextButton

        // Create a BitmapFont for the TextButton with the new parameter
        BitmapFont magicalFontButton = buttonFontGenerator.generateFont(buttonParameter);

        // Create a TextButtonStyle with the magical font for the TextButton
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = magicalFontButton;
        textButtonStyle.fontColor = Color.GOLD;

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Go To Game", textButtonStyle);
        table.add(goToGameButton).width(300).height(80).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });

    // Dispose of the button font generator
        buttonFontGenerator.dispose();
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
        // Dispose of the maze background texture
        mazeBackground.dispose();
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
    }
}
