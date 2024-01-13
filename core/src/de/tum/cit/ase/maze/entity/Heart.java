package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Heart class represents heart entity.
 * It is a collectible game object, which refills player's health.
 */
public class Heart extends UpdatableEntity {
    private final Animation<TextureRegion> heartAnimation;

    /**
     * Creates one collectible heart.
     * @param game the main game
     */
    public Heart(MazeRunnerGame game) {
        super(game);
        heartAnimation = game.getHeartAnimation();

        setTextureRegion(heartAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(heartAnimation.getKeyFrame(getTime(), true));
    }
}
