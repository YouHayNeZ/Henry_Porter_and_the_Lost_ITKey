package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Key class represents key entity. Nothing unusual.
 */
public class Key extends UpdatableEntity {

    private Animation<TextureRegion> keyAnimation;

    /**
     * Create one key
     * @param game the main game
     */
    public Key(MazeRunnerGame game) {
        super(game);
        keyAnimation = game.getKeyAnimation();

        setTextureRegion(keyAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(keyAnimation.getKeyFrame(getTime(), true));
    }
}
