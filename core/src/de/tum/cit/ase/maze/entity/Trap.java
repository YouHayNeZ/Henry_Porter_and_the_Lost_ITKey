package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Trap extends UpdatableEntity {

    private Animation<TextureRegion> flameAnimation;
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
