package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.screen.GameScreen;
import de.tum.cit.ase.maze.screen.MenuScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;

import java.io.IOException;
import java.util.Properties;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    //Native file chooser
    private final NativeFileChooser fileChooser;

    //Textures
    Texture basictilesTexture;
    Texture characterTexture;
    Texture objectsTexture;
    Texture mobsTexture;
    Texture thingsTexture;

    //TextureRegions
    TextureRegion floorTextureRegion;
    TextureRegion doorTextureRegion;

    Array<TextureRegion> wallTextureRegionArray;
    Array<TextureRegion> switchTextureRegionArray;
    Array<TextureRegion> healthTextureRegionArray;

    //Animations
    Animation<TextureRegion> characterDownAnimation;
    Animation<TextureRegion> characterRightAnimation;
    Animation<TextureRegion> characterUpAnimation;
    Animation<TextureRegion> characterLeftAnimation;
    Animation<TextureRegion> flameAnimation;
    Animation<TextureRegion> enemyDownAnimation;

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
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        // Load textures
        basictilesTexture = new Texture(Gdx.files.internal("basictiles.png"));
        characterTexture = new Texture(Gdx.files.internal("character.png"));
        objectsTexture = new Texture(Gdx.files.internal("objects.png"));
        mobsTexture = new Texture(Gdx.files.internal("mobs.png"));
        thingsTexture = new Texture(Gdx.files.internal("things.png"));

        // Load texture regions
        floorTextureRegion = new TextureRegion(basictilesTexture, 16, 16 * 9, 16, 16);
        doorTextureRegion = new TextureRegion(basictilesTexture, 16, 16 * 6, 16, 16);

        // Load texture region arrays
        wallTextureRegionArray = loadTextureRegionArray(basictilesTexture, 16, 16,
                4, 0, 0);
        switchTextureRegionArray = loadTextureRegionArray(thingsTexture, 16, 16,
                3, 16 * 3, 16 * 4);
        healthTextureRegionArray = loadTextureRegionArray(objectsTexture, 16, 16,
                5, 16 * 4, 0);

        // Load character animations
        characterDownAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 0);
        characterRightAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 32);
        characterUpAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 64);
        characterLeftAnimation = loadAnimation(characterTexture,
                16, 32, 4, 0.1f, 0, 96);

        // Load the flame animation
        flameAnimation = loadAnimation(objectsTexture,
                16, 16, 7, 0.1f,
                4 * 16, 3 * 16);

        // Load the enemy animation
        enemyDownAnimation = loadAnimation(mobsTexture,
                16, 16, 3, 0.1f,
                6 * 16, 4 * 16);

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Harry Potter - Main Theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Load an array of texture regions from a texture.
     * @param texture
     * @param frameWidth
     * @param frameHeight
     * @param frameCount
     * @param x
     * @param y
     */
    private Array<TextureRegion> loadTextureRegionArray(Texture texture, int frameWidth, int frameHeight,
                                                        int frameCount, int x, int y) {

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> array = new Array<>(TextureRegion.class);

        // Add all frames to the array
        for (int col = 0; col < frameCount; col++) {
            array.add(new TextureRegion(texture, col * frameWidth + x, y, frameWidth, frameHeight));
        }

        return array;
    }

    /**
     * Load an animation from a texture.
     * @param texture
     * @param frameWidth
     * @param frameHeight
     * @param animationFrames
     * @param frameDuration
     * @param x
     * @param y
     */
    private Animation<TextureRegion> loadAnimation(Texture texture, int frameWidth, int frameHeight, int animationFrames, float frameDuration, int x, int y) {

        // Load the texture regions from the loadTextureRegionArray method
        Array<TextureRegion> frames = loadTextureRegionArray(texture, frameWidth, frameHeight,
                animationFrames, x, y);

        return new Animation<>(frameDuration, frames);
    }

    /**
     * Open the file chooser to allow the user to pick a maze file.
     */
    public void chooseMazeFile() {
        var fileChooserConfig = new NativeFileChooserConfiguration();
        fileChooserConfig.title = "Pick a maze file"; // Title of the window that will be opened
        fileChooserConfig.intent = NativeFileChooserIntent.OPEN; // Open a file
        fileChooserConfig.nameFilter = (file, name) -> name.endsWith("properties"); // Only accept .properties files
        fileChooserConfig.directory = Gdx.files.internal("maps"); // Open at the user's home directory

        fileChooser.chooseFile(fileChooserConfig, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle fileHandle) {
                // Pass the game instance and the selected file handle to the loadMaze method
                loadMaze(MazeRunnerGame.this, fileHandle);
            }

            @Override
            public void onCancellation() {
                // User closed the window, don't need to do anything
            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error picking maze file: " + exception.getMessage());
            }
        });
    }

    /**
     * Load a maze from a properties file.
     *
     * @param game       The MazeRunnerGame instance.
     * @param fileHandle The handle to the maze file.
     */
    public void loadMaze(MazeRunnerGame game, FileHandle fileHandle) {
        Properties properties = new Properties();
        try {
            properties.load(fileHandle.reader());
        } catch (IOException e) {
            System.err.println("Error loading maze file: " + e.getMessage());
            return;
        }

        // Example method to print the loaded data (other processing in TODO)

        String fileContent = fileHandle.readString();
        String[] lines = fileContent.split("\\r?\\n");

        int width = Integer.parseInt(lines[0]);
        int height = Integer.parseInt(lines[1]);

//        Maze maze = new Maze(width, height);

        properties.forEach((key, value) -> {
            String[] coordinates = key.toString().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int type = Integer.parseInt(value.toString());

            System.out.println("Coordinate: (" + x + ", " + y + "), Type: " + type);
        });

        // TODO: Implement logic to create the actual maze based on the loaded data.
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public TextureRegion getFloorTextureRegion() {
        return floorTextureRegion;
    }

    public TextureRegion getDoorTextureRegion() {
        return doorTextureRegion;
    }

    public Array<TextureRegion> getWallTextureRegionArray() {
        return wallTextureRegionArray;
    }

    public Array<TextureRegion> getSwitchTextureRegionArray() {
        return switchTextureRegionArray;
    }

    public Array<TextureRegion> getHealthTextureRegionArray() {
        return healthTextureRegionArray;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getFlameAnimation() {
        return flameAnimation;
    }

    public Animation<TextureRegion> getEnemyDownAnimation() {
        return enemyDownAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

}