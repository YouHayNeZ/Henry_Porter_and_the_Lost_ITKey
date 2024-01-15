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
import de.tum.cit.ase.maze.screen.GameScreen;

/**
 * Player class represents player entity, which is movable and updatable.
 * It has health and can be damaged by traps and enemies.
 * Can also attack enemies. Its movement controlled by the user.
 */
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
    private Animation<TextureRegion> attackAnimation;

    private final Array<Sound> hurtSoundArray;
    private final Sound keySound;
    private final Sound healSound;
    private final Sound coinSound;
    private final Sound spellSound;
    private final Sound clockSound;
    private final Sound potionSound;

    private float health;
    private float immutableTime;

    private int collectedCoins;
    private int collectedKeys;
    private boolean hasAtLeastOneKey;
    private GameScreen screen;

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
        healSound = game.getHealSound();
        spellSound = game.getSpellSound();
        coinSound = game.getCoinSound();
        clockSound = game.getClockSound();
        potionSound = game.getPotionSound();

        setTextureRegion(downAnimation.getKeyFrames()[0]);
        centerDrawOffset();

        health = DEFAULT_HEALTH;
        collectedCoins = 0;
        collectedKeys = 0;
        hasAtLeastOneKey = false;
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
        attackAnimation = null;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

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
                setTextureRegion(attackAnimation.getKeyFrame(getTime(), true));
                // Check player attack with enemy collision
                Enemy enemy = checkEnemyCollision();
                if (enemy != null) {
                    getGame().getLevelMap().getEntities().removeValue(enemy, true);
                    // Play spell sound
                    spellSound.play();
                }
                return; // Skip normal movement rendering when attacking
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp((float) (delta + 0.2 * delta * collectedCoins));
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown((float) (delta + 0.2 * delta * collectedCoins));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft((float) (delta + 0.2 * delta * collectedCoins));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight((float) (delta + 0.2 * delta * collectedCoins));
        }

        // Check key collision
        Key key = checkKeyCollision();
        if (key != null) {
            collectedKeys++;
            if (collectedKeys > 0) {
                hasAtLeastOneKey = true;
            }
            getGame().getLevelMap().getEntities().removeValue(key, true);

            // Play key sound
            keySound.play();
        }

        // Check heart collision
        Heart heart = checkHeartCollision();
        if (heart != null) {
            health = Math.min(DEFAULT_HEALTH, health + (1f/5f) * DEFAULT_HEALTH);
            getGame().getLevelMap().getEntities().removeValue(heart, true);

            // Play heal sound
            healSound.play();
        }

        // Check coin collision
        Coin coin = checkCoinCollision();
        if (coin != null) {
            collectedCoins++;
            getGame().getLevelMap().getEntities().removeValue(coin, true);

            // Play coin sound
            coinSound.play();
        }

        // Check clock collision
        Clock clock = checkClockCollision();
        if (clock != null) {
            //add 30 seconds to the timer
            screen = (GameScreen) getGame().getScreen();
            screen.setTimeLeft(screen.getTimeLeft() + 30);

            getGame().getLevelMap().getEntities().removeValue(clock, true);

            // Play clock sound
            clockSound.play();
        }

        // Check potion collision
        Potion potion = checkPotionCollision();
        if (potion != null) {
            immutableTime = DEFAULT_IMMUTABLE_TIME;
            getGame().getLevelMap().getEntities().removeValue(potion, true);

            // Play potion sound
            potionSound.play();
        }

        // Check trap or enemy collision
        if (checkTrapOrEnemyCollision() && immutableTime <= 0) {
            collectedCoins = 0;
            health -= DEFAULT_DAMAGE;
            immutableTime = DEFAULT_IMMUTABLE_TIME;

            // Play random hurt sound
            hurtSoundArray.random().play();
        }
    }

    @Override
    public Rectangle getEntityRectangle() {
        if (attackAnimation != null) {
            return new Rectangle(getX() - (float) CELL_WIDTH / 2, getY() - (float) CELL_HEIGHT / 2,
                    (float) CELL_WIDTH, (float) CELL_HEIGHT);
        } else {
            return new Rectangle(getX() - (float) CELL_WIDTH / 4, getY() - (float) CELL_HEIGHT / 2,
                    (float) CELL_WIDTH / 2, (float) CELL_HEIGHT / 2);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (immutableTime <= 0 || immutableTime % 0.1 < 0.05) {
            super.draw(batch);
        }
    }

    private boolean checkTrapOrEnemyCollision() {
        return checkCollision(Trap.class, Enemy.class) != null;
    }

    private Heart checkHeartCollision() {
        return (Heart) checkCollision(Heart.class);
    }

    private Coin checkCoinCollision() {
        return (Coin) checkCollision(Coin.class);
    }

    private Clock checkClockCollision() {
        return (Clock) checkCollision(Clock.class);
    }

    private Potion checkPotionCollision() {
        return (Potion) checkCollision(Potion.class);
    }

    private Key checkKeyCollision() {
        return (Key) checkCollision(Key.class);
    }

    private Enemy checkEnemyCollision() {
        return (Enemy) checkCollision(Enemy.class);
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

    // Getter methods
    /**
     * Get player health.
     * @return the player health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Set player health.
     * @param health the player health
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * Get immutable time left.
     * @return the immutable time
     */
    public float getImmutableTime() {
        return immutableTime;
    }

    /**
     * Get collected coins.
     * @return the collected coins
     */
    public int getCollectedCoins() {
        return collectedCoins;
    }

    /**
     * Get collected keys.
     * @return the collected keys
     */
    public int getCollectedKeys() {
        return collectedKeys;
    }

    /**
     * Return if player has key.
     * @return true if player has the key
     */
    public boolean isHasAtLeastOneKey() {
        return hasAtLeastOneKey;
    }

    /**
     * Set player has key.
     * @param hasAtLeastOneKey the has key
     */
    public void setHasAtLeastOneKey(boolean hasAtLeastOneKey) {
        this.hasAtLeastOneKey = hasAtLeastOneKey;
    }
}
