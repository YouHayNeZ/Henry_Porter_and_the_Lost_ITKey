package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Trap class represents trap entity. Nothing unusual
 */
public class Trap extends UpdatableEntity {

    private final Animation<TextureRegion> flameAnimation;

    /**
     * Create one trap
     * @param game the main game
     */
    public Trap(MazeRunnerGame game) {
        super(game);
        flameAnimation = game.getFlameAnimation();
        setTextureRegion(flameAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(flameAnimation.getKeyFrame(getTime(), true));
    }
}
