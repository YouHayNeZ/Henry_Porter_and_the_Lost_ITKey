package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Trap class represents trap entity. Nothing unusual.
 */
public class Trap extends UpdatableEntity {

    private Animation<TextureRegion> flameAnimation;
    private final Animation<TextureRegion> blueFlameAnimation;


    /**
     * Creates one new trap.
     * @param game the main game
     */
    public Trap(MazeRunnerGame game) {
        super(game);
        flameAnimation = game.getFlameAnimation();
        blueFlameAnimation = game.getBlueFlameAnimation();

        setTextureRegion(blueFlameAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(blueFlameAnimation.getKeyFrame(getTime(), true));
    }
}
