package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Coin class represents clock entity.
 * It is a collectible game object, which increases the time left on the timer.
 */
public class Clock extends UpdatableEntity {
    private final Animation<TextureRegion> clockAnimation;

    /**
     * Creates one collectible clock.
     * @param game the main game
     */
    public Clock(MazeRunnerGame game) {
        super(game);
        clockAnimation = game.getClockAnimation();

        setTextureRegion(clockAnimation.getKeyFrames()[0]);
    }

    /**
     * Updates the clock animation.
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(clockAnimation.getKeyFrame(getTime(), true));
    }
}