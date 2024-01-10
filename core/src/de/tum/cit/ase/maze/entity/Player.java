package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.MazeRunnerGame;

public class Player extends MovableEntity {

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    //Player default health count
    public static final float DEFAULT_HEALTH = 5f;

    private static final float DEFAULT_DAMAGE = 1f;
    private static final float DEFAULT_IMMUTABLE_TIME = 5f;

    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightAnimation;
    private final Animation<TextureRegion> upAnimation;
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> attackDownAnimation;
    private final Animation<TextureRegion> attackUpAnimation;
    private final Animation<TextureRegion> attackRightAnimation;
    private final Animation<TextureRegion> attackLeftAnimation;

    private final Array<Sound> hurtSoundArray;
    private final Sound keySound;

    private float health;
    private float immutableTime;

    private boolean hasKey;
    private final Rectangle character;

    public Player(MazeRunnerGame game) {
        super(game);
        downAnimation = game.getCharacterDownAnimation();
        rightAnimation = game.getCharacterRightAnimation();
        upAnimation = game.getCharacterUpAnimation();
        leftAnimation = game.getCharacterLeftAnimation();
        attackDownAnimation = game.getCharacterAttackDownAnimation();
        attackUpAnimation = game.getCharacterAttackUpAnimation();
        attackRightAnimation = game.getCharacterAttackRightAnimation();
        attackLeftAnimation = game.getCharacterAttackLeftAnimation();

        hurtSoundArray = game.getHurtSoundArray();
        keySound = game.getKeySound();

        setTextureRegion(downAnimation.getKeyFrames()[0]);
        centerDrawOffset();

        health = DEFAULT_HEALTH;
        hasKey = false;

        // Create a Rectangle to logically represent the character
        character = new Rectangle(600, 300, 32, 64);
    }

    // Override getter methods for animations
    @Override
    public Animation<TextureRegion> getUpAnimation() {
        return upAnimation;
    }

    @Override
    public Animation<TextureRegion> getDownAnimation() {
        return downAnimation;
    }

    @Override
    public Animation<TextureRegion> getLeftAnimation() {
        return leftAnimation;
    }

    @Override
    public Animation<TextureRegion> getRightAnimation() {
        return rightAnimation;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        immutableTime -= delta;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveUp(delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) moveDown(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveLeft(delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) moveRight(delta);

        //Check key collision
        Key key = checkKeyCollision();
        if (key != null) {
            hasKey = true;
            getGame().getLevelMap().getEntities().removeValue(key, true);

            keySound.play();
        }

        //Check trap or enemy collision
        if (checkTrapOrEnemyCollision() && immutableTime <= 0) {
            health -= DEFAULT_DAMAGE;
            immutableTime = DEFAULT_IMMUTABLE_TIME;

            //Play random hurt sound
            hurtSoundArray.random().play();
        }
    }

    @Override
    public Rectangle getEntityRectangle() {
        return new Rectangle(getX() - (float) CELL_WIDTH / 4, y - (float) CELL_HEIGHT / 2,
                (float) CELL_WIDTH / 2, (float) CELL_HEIGHT / 2);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (immutableTime <= 0 || immutableTime % 0.1 < 0.05) super.draw(batch);
    }

    private Key checkKeyCollision() {
        return (Key) checkCollision(Key.class);
    }

    private boolean checkTrapOrEnemyCollision() {
        return checkCollision(Trap.class, Enemy.class) != null;
    }

    private Object checkCollision(Class... classes) {
        Rectangle playerRectangle = getEntityRectangle();
        Entity entity;
        Rectangle entityRectangle;
        int size = getGame().getLevelMap().getEntities().size;
        for (int i = 0; i < size; i++) {
            entity = getGame().getLevelMap().getEntities().get(i);
            entityRectangle = entity.getEntityRectangle();
            for (Class aClass : classes) {
                if (entity.getClass().equals(aClass) && Intersector.overlaps(playerRectangle, entityRectangle)) {
                    return entity;
                }
            }
        }
        return null;
    }

    private void renderAnimation(SpriteBatch batch, Animation<TextureRegion> animation) {
        batch.draw(animation.getKeyFrame(animationTime, true), character.x, character.y, character.width, character.height);
    }

    float animationTime;
    /**
     * Renders the character animation.
     */
    public void render(float delta, SpriteBatch batch) {
        super.update(delta);
        animationTime += delta;

        float speed = 64 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Animation<TextureRegion> attackAnimation = null;

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                attackAnimation = attackDownAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                attackAnimation = attackRightAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                attackAnimation = attackUpAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                attackAnimation = attackLeftAnimation;
            }

            if (attackAnimation != null) {
                // render attack animation
                batch.draw(attackAnimation.getKeyFrame(animationTime, true), character.x, character.y, character.width, character.height);
                return; // Skip normal movement rendering when attacking
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            character.y += speed;
            // render move up animation
            renderAnimation(batch, upAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            character.y -= speed;
            // render move down animation
            renderAnimation(batch, downAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            character.x -= speed;
            // render move left animation
            renderAnimation(batch, leftAnimation);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            character.x += speed;
            // render move right animation
            renderAnimation(batch, rightAnimation);
        }

        else {
            // render standing animation
            batch.draw(downAnimation.getKeyFrame(1, true), character.x, character.y, character.width, character.height);
        }
    }

    //Getter methods
    /**
     * Get player health
     * @return the player health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Set player health
     * @param health the player health
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * Get immutable time left
     * @return the immutable time
     */
    public float getImmutableTime() {
        return immutableTime;
    }

    /**
     * Return if player has key
     * @return true if player has the key
     */
    public boolean isHasKey() {
        return hasKey;
    }

    /**
     * Set player has key
     * @param hasKey the has key
     */
    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }
}
