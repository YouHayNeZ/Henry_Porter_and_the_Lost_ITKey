package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import de.tum.cit.ase.maze.MazeRunnerGame;


public class Character extends UpdatableEntity {

    private final float speed = 64f;
    private final float health = 5f;

    private final Animation<TextureRegion> characterDownAnimation;
    private final Animation<TextureRegion> characterRightAnimation;
    private final Animation<TextureRegion> characterUpAnimation;
    private final Animation<TextureRegion> characterLeftAnimation;

    private final Animation<TextureRegion> characterAttackDownAnimation;
    private final Animation<TextureRegion> characterAttackUpAnimation;
    private final Animation<TextureRegion> characterAttackRightAnimation;
    private final Animation<TextureRegion> characterAttackLeftAnimation;

    private final Rectangle character;

    int frameWidth = 16;
    int frameHeight = 32;
    float animationTime = 0;

    public Character(MazeRunnerGame game) {
        super(game);
        characterDownAnimation = game.getCharacterDownAnimation();
        characterRightAnimation = game.getCharacterRightAnimation();
        characterUpAnimation = game.getCharacterUpAnimation();
        characterLeftAnimation = game.getCharacterLeftAnimation();

        setTextureRegion(characterDownAnimation.getKeyFrames()[0]);
//        centerDrawOffset();

        characterAttackDownAnimation = loadCharacterAnimation(7, 4 * frameHeight, 2);
        characterAttackUpAnimation = loadCharacterAnimation(7, 5 * frameHeight, 2);
        characterAttackRightAnimation = loadCharacterAnimation(7, 6 * frameHeight, 2);
        characterAttackLeftAnimation = loadCharacterAnimation(7, 7 * frameHeight, 2);

        // Create a Rectangle to logically represent the character
        character = new Rectangle(600, 300, 32, 64);
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private Animation<TextureRegion> loadCharacterAnimation(int offsetX, int offsetY, int multiplier) {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int animationFrames = 4 * multiplier;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col += multiplier) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth + offsetX, offsetY, frameWidth, frameHeight));
        }

        return new Animation<>(0.1f, walkFrames);
    }

    private void renderAnimation(SpriteBatch batch, Animation<TextureRegion> animation) {
        batch.draw(animation.getKeyFrame(animationTime, true), character.x, character.y, character.width, character.height);
    }

    /**
     * Renders the character animation.
     */
    public void render(float delta, SpriteBatch batch) {
        super.update(delta);
        animationTime += delta;

        float speed = 64 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            Animation<TextureRegion> attackAnimation = null;

            if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                attackAnimation = characterAttackDownAnimation;
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                attackAnimation = characterAttackRightAnimation;
            } else if (Gdx.input.isKeyPressed(Keys.UP)) {
                attackAnimation = characterAttackUpAnimation;
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                attackAnimation = characterAttackLeftAnimation;
            }

            if (attackAnimation != null) {
                // render attack animation
                batch.draw(attackAnimation.getKeyFrame(animationTime, true), character.x, character.y, character.width, character.height);
                return; // Skip normal movement rendering when attacking
            }
        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            character.y += speed;
            // render move up animation
            batch.draw(characterUpAnimation.getKeyFrame(animationTime, true), character.x, character.y, character.width, character.height);

//            setTextureRegion(characterUpAnimation.getKeyFrame(getTime(), true));
//            renderAnimation(batch, characterUpAnimation);
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            character.y -= speed;
            // render move down animation
            renderAnimation(batch, characterDownAnimation);
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            character.x -= speed;
            // render move left animation
            renderAnimation(batch, characterLeftAnimation);
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            character.x += speed;
            // render move right animation
            renderAnimation(batch, characterRightAnimation);
        }

        else {
            // render standing animation
            batch.draw(characterDownAnimation.getKeyFrame(1, true), character.x, character.y, character.width, character.height);
        }
    }
}
