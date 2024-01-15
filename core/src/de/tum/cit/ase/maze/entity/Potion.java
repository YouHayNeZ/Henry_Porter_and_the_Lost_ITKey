package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Coin class represents potion entity.
 * It is a collectible game object, which renders the player invincible for a short period of time.
 */
public class Potion extends UpdatableEntity {
    private final Animation<TextureRegion> potionAnimation;

    /**
     * Creates one collectible potion.
     * @param game the main game
     */
    public Potion(MazeRunnerGame game) {
        super(game);
        potionAnimation = game.getPotionAnimation();

        setTextureRegion(potionAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(potionAnimation.getKeyFrame(getTime(), true));
    }
}