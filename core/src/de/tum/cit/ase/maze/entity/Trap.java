package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

import java.util.Random;

/**
 * Trap class represents trap entity. Nothing unusual except that it can be blue or red.
 */
public class Trap extends UpdatableEntity {

    private Animation<TextureRegion> flameAnimation;
    private final Animation<TextureRegion> blueFlameAnimation;
    private final Random random;
    private boolean blue = false;


    /**
     * Creates one new trap and randomly chooses its color.
     * @param game the main game
     */
    public Trap(MazeRunnerGame game) {
        super(game);
        flameAnimation = game.getFlameAnimation();
        blueFlameAnimation = game.getBlueFlameAnimation();
        random = new Random();
        if (random.nextBoolean()) {
            setTextureRegion(flameAnimation.getKeyFrames()[0]);
            blue = true;
        } else {
            setTextureRegion(blueFlameAnimation.getKeyFrames()[0]);
        }
    }

    /**
     * Updates the trap animation.
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        if (blue) {
            setTextureRegion(blueFlameAnimation.getKeyFrame(getTime(), true));
        } else {
            setTextureRegion(flameAnimation.getKeyFrame(getTime(), true));
        }
    }
}
