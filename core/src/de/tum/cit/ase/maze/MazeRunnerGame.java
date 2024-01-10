package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.Disposable;
import de.tum.cit.ase.maze.screen.ChooseLevelScreen;
import de.tum.cit.ase.maze.screen.GameScreen;
import de.tum.cit.ase.maze.screen.MenuScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.io.IOException;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {

    // World cell width size
    private static final int CELL_WIDTH = 16;

    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private static final String LEVEL_MAP_FORMAT = "maps/level-%d.properties";

    private static final int DEFAULT_LEVEL_INDEX = 1;
    private static final int MAX_LEVEL_INDEX = 5;
    private int levelIndex = DEFAULT_LEVEL_INDEX;


    //Native file chooser
    private final NativeFileChooser fileChooser;

    // Screens
    private MenuScreen menuScreen;
    private ChooseLevelScreen chooseLevelScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    // UI Skin
    private Skin skin;

    // Font styles
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle textButtonStyle;

    //Textures
    Texture basictilesTexture;
    Texture characterTexture;
    Texture objectsTexture;
    Texture mobsTexture;
    Texture thingsTexture;
    Texture keyTexture;
    Texture mazeBackground;

    //TextureRegions
    TextureRegion floorTextureRegion;

    Array<TextureRegion> wallTextureRegionArray;
    Array<TextureRegion> healthTextureRegionArray;

    //Animations
    Animation<TextureRegion> characterDownAnimation;
    Animation<TextureRegion> characterRightAnimation;
    Animation<TextureRegion> characterUpAnimation;
    Animation<TextureRegion> characterLeftAnimation;
    Animation<TextureRegion> enemyDownAnimation;
    Animation<TextureRegion> enemyLeftAnimation;
    Animation<TextureRegion> enemyRightAnimation;
    Animation<TextureRegion> enemyUpAnimation;
    Animation<TextureRegion> flameAnimation;
    Animation<TextureRegion> keyAnimation;
    Animation<TextureRegion> doorAnimation;

    Array<Sound> hurtSoundArray;

    Sound keySound;
    LevelMap levelMap;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        // Load the TTF file for the new font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Magical Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120; // Set the font size as needed

        // Create a BitmapFont from the TTF file for the title
        BitmapFont magicalFontTitle = generator.generateFont(parameter);

        // Create a BitmapFont for the TextButton with the new size parameter
        parameter.size = 40; // Set the font size as needed
        BitmapFont magicalFontButton = generator.generateFont(parameter);

        generator.dispose(); // Dispose of the generator when done

        // Create a TextButtonStyle with the magical font for the TextButton
        labelStyle = new Label.LabelStyle();
        labelStyle.font = magicalFontTitle;
        labelStyle.fontColor = Color.GOLD;

        // Create a TextButtonStyle with the magical font for the TextButton
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = magicalFontButton;
        textButtonStyle.fontColor = Color.GOLD;

        // Load textures
        basictilesTexture = new Texture(Gdx.files.internal("basictiles.png"));
        characterTexture = new Texture(Gdx.files.internal("character.png"));
        objectsTexture = new Texture(Gdx.files.internal("objects.png"));
        mobsTexture = new Texture(Gdx.files.internal("mobs.png"));
        thingsTexture = new Texture(Gdx.files.internal("things.png"));
        keyTexture = new Texture(Gdx.files.internal("key.png"));
        mazeBackground = new Texture(Gdx.files.internal("Maze Background.png"));

        // Load texture regions
        floorTextureRegion = new TextureRegion(basictilesTexture, 16, 16 * 9, 16, 16);

        wallTextureRegionArray = loadTextureRegionArray(basictilesTexture,
                16, 16, 4, 0, 0);
        healthTextureRegionArray = loadTextureRegionArray(objectsTexture,
                16, 16, 5, 16 * 4, 0);

        // Load character animations
        characterDownAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 0);
        characterRightAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 32);
        characterUpAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 64);
        characterLeftAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 96);

        // Load the enemy animation
        enemyDownAnimation = loadAnimation(mobsTexture,
                16, 16, 3, 0.2f, 6 * CELL_WIDTH, 4 * CELL_HEIGHT);
        enemyLeftAnimation = loadAnimation(mobsTexture,
                16, 16, 3, 0.2f, 6 * CELL_WIDTH, 5 * CELL_HEIGHT);
        enemyRightAnimation = loadAnimation(mobsTexture,
                16, 16, 3, 0.2f, 6 * CELL_WIDTH, 6 * CELL_HEIGHT);
        enemyUpAnimation = loadAnimation(mobsTexture,
                16, 16, 3, 0.2f, 6 * CELL_WIDTH, 7 * CELL_HEIGHT);

        // Load the flame animation
        flameAnimation = loadAnimation(objectsTexture,
                16, 16, 7, 0.1f, 4 * 16, 3 * 16);

        // Load the key animation
        keyAnimation = loadAnimation(keyTexture,
                16, 16, 24, 0.05f, 0, 0);

        // Load the door animation
        doorAnimation = loadAnimation(thingsTexture,
                16, 16, 1, 4, 0.1f, 16 * 3, 0);

        // Play some background music
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/Harry Potter - Main Theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        // Hurt sound
        hurtSoundArray = new Array<>();
        for (int i = 1; i <= 3; i++) {
            hurtSoundArray.add(Gdx.audio.newSound(Gdx.files.internal(String.format("sound/hurt/hurt_%d.mp3", i))));
        }

        // Key sound
        keySound = Gdx.audio.newSound(Gdx.files.internal("sound/ring_inventory.wav"));

        levelMap = new LevelMap(this);

        menuScreen = new MenuScreen(this);
        chooseLevelScreen = new ChooseLevelScreen(this);
        gameScreen = new GameScreen(this);

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        setScreen(menuScreen);
    }

    /**
     * Switches to choose level screen.
     */
    public void goToChooseLevel() {
        setScreen(chooseLevelScreen);
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        setScreen(gameScreen);
    }

    /**
     * Switches to the game screen with current level index
     */
    public void goToCurrentLevelIndexGame() {
        try {
            levelMap.load(String.format(LEVEL_MAP_FORMAT, levelIndex));
            gameScreen.initLevel();
            setScreen(gameScreen); // Set the current screen to GameScreen
        }
        catch (IOException e) {
            Gdx.app.log("ERROR", "Failed to load level index: " + levelIndex, e);
        }
    }

    /**
     * Load texture region array of images from texture that stands in one row
     * @param texture the texture
     * @param frameWidth the frame width
     * @param frameHeight the frame height
     * @param count the count of frames
     * @param x the start x position
     * @param y the start y position
     * @return array of texture regions
     */
    private Array<TextureRegion> loadTextureRegionArray(Texture texture, int frameWidth, int frameHeight,
                                                        int count, int x, int y) {
        return loadTextureRegionArray(texture, frameWidth, frameHeight, count, 1, x, y);
    }

    /**
     * Load texture region array from texture
     * @param texture the texture
     * @param frameWidth the frame width
     * @param frameHeight the frame height
     * @param cols the number of columns
     * @param rows the number of rows
     * @param x the start x position
     * @param y the start y position
     * @return array of texture regions
     */
    private Array<TextureRegion> loadTextureRegionArray(Texture texture, int frameWidth, int frameHeight,
                                                        int cols, int rows, int x, int y) {
        Array<TextureRegion> array = new Array<>(TextureRegion.class);
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                array.add(new TextureRegion(texture, col * frameWidth + x, row * frameHeight + y, frameWidth, frameHeight));
            }
        }
        return array;
    }

    /**
     * Load animation from texture
     * @param texture the texture
     * @param frameWidth the frame width
     * @param frameHeight the frame height
     * @param cols the number of columns
     * @param rows the number of rows
     * @param frameDuration the animation frame duration in milliseconds
     * @param x the start x position
     * @param y the start y position
     * @return animation of texture region
     */
    private Animation<TextureRegion> loadAnimation(Texture texture, int frameWidth, int frameHeight,
                                                   int cols, int rows, float frameDuration, int x, int y) {
        Array<TextureRegion> frames = loadTextureRegionArray(texture, frameWidth, frameHeight, cols, rows, x, y);
        return new Animation<>(frameDuration, frames);
    }

    /**
     * Load animation from texture that stands in on row
     * @param texture the texture
     * @param frameWidth the frame width
     * @param frameHeight the frame height
     * @param animationFrames the count of animation frames
     * @param frameDuration the frame duration in milliseconds
     * @param x the start x position
     * @param y the start y position
     * @return the animation of texture regions
     */
    private Animation<TextureRegion> loadAnimation(Texture texture, int frameWidth, int frameHeight,
                                                   int animationFrames, float frameDuration, int x, int y) {
        Array<TextureRegion> frames = loadTextureRegionArray(texture, frameWidth, frameHeight, animationFrames, x, y);
        return new Animation<>(frameDuration, frames);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen

        // Dispose the current screens
        if (menuScreen != null) menuScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();

        spriteBatch.dispose(); // Dispose the spriteBatch
        shapeRenderer.dispose(); // Dispose the shapeRenderer
        skin.dispose(); // Dispose the skin

        //Dispose textures
        basictilesTexture.dispose();
        characterTexture.dispose();
        objectsTexture.dispose();
        mobsTexture.dispose();
        thingsTexture.dispose();
        keyTexture.dispose();
        mazeBackground.dispose();

        disposeArray(hurtSoundArray);

        keySound.dispose();
    }

    /**
     * Dispose array
     * @param array the array of disposable
     */
    private void disposeArray(Array<? extends Disposable> array) {
        for (Disposable d: array) {
            d.dispose();
        }
    }

    /**
     * Increment level index
     */
    public void incrementLevel() {
        if (++levelIndex > MAX_LEVEL_INDEX) levelIndex = DEFAULT_LEVEL_INDEX;
    }

    /**
     * Set current level index
     * @param index the current level index
     */
    public void setLevelIndex(int index) {
        levelIndex = index;
    }

    // Getter methods
    /**
     * Get ui skin
     * @return the ui skin
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Get maze background texture
     * @return the maze background texture
     */
    public Texture getMazeBackground() {
        return mazeBackground;
    }

    /**
     * Get floor texture region
     * @return the floor texture region
     */
    public TextureRegion getFloorTextureRegion() {
        return floorTextureRegion;
    }

    /**
     * Gew wall texture region array
     * @return the array of wall texture region
     */
    public Array<TextureRegion> getWallTextureRegionArray() {
        return wallTextureRegionArray;
    }

    /**
     * Get health texture region array
     * @return the array of health texture region
     */
    public Array<TextureRegion> getHealthTextureRegionArray() {
        return healthTextureRegionArray;
    }

    /**
     * Get character down animation
     * @return the animation of character down movement
     */
    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    /**
     * Get character right animation
     * @return the animation of character right movement
     */
    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    /**
     * Get character up animation
     * @return the animation of character up movement
     */
    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    /**
     * Get character left animation
     * @return the animation of character left movement
     */
    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    /**
     * Get flame animation
     * @return the animation of flame
     */
    public Animation<TextureRegion> getFlameAnimation() {
        return flameAnimation;
    }

    /**
     * Get enemy down animation
     * @return the animation of enemy down movement
     */
    public Animation<TextureRegion> getEnemyDownAnimation() {
        return enemyDownAnimation;
    }

    /**
     * Get enemy left animation
     * @return the animation of enemy left movement
     */
    public Animation<TextureRegion> getEnemyLeftAnimation() {
        return enemyLeftAnimation;
    }

    /**
     * Get enemy right animation
     * @return the animation of enemy right movement
     */
    public Animation<TextureRegion> getEnemyRightAnimation() {
        return enemyRightAnimation;
    }

    /**
     * Get enemy up animation
     * @return the animation of enemy up movement
     */
    public Animation<TextureRegion> getEnemyUpAnimation() {
        return enemyUpAnimation;
    }

    /**
     * Get key animation
     * @return the animation of key rotating
     */
    public Animation<TextureRegion> getKeyAnimation() {
        return keyAnimation;
    }

    /**
     * Get door animation
     * @return the animation of door opening
     */
    public Animation<TextureRegion> getDoorAnimation() {
        return doorAnimation;
    }

    /**
     * Get hurt sound array
     * @return the array of hurt sounds
     */
    public Array<Sound> getHurtSoundArray() {
        return hurtSoundArray;
    }

    /**
     * Get key sound
     * @return the sound of picking key
     */
    public Sound getKeySound() {
        return keySound;
    }

    /**
     * Get sprite batch
     * @return the sprite batch
     */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /**
     * Get level map
     * @return the level map
     */
    public LevelMap getLevelMap() {
        return levelMap;
    }

    /**
     * Get file chooser
     * @return the native file chooser
     */
    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Get game screen
     * @return the game screen
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Get label style
     * @return the label style
     */
    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    /**
     * Get text button style
     * @return the text button style
     */
    public TextButton.TextButtonStyle getTextButtonStyle() {
        return textButtonStyle;
    }
}