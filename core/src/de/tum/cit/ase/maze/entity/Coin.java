package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Coin class represents coin entity.
 * It is a collectible game object, which increases the player's speed.
 */
public class Coin extends UpdatableEntity {
    private final Animation<TextureRegion> coinAnimation;

    /**
     * Creates one collectible coin.
     * @param game the main game
     */
    public Coin(MazeRunnerGame game) {
        super(game);
        coinAnimation = game.getCoinAnimation();

        setTextureRegion(coinAnimation.getKeyFrames()[0]);
    }

    /**
     * Updates the coin animation.
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(coinAnimation.getKeyFrame(getTime(), true));
    }
}