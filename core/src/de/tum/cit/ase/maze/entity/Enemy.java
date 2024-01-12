package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Enemy class represents enemy entity, which is moveable and updatable.
 * It moves in random directions
 */
public class Enemy extends MovableEntity {

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> upAnimation;
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> rightAnimation;

    public Enemy(MazeRunnerGame game) {
        super(game);
        downAnimation = game.getEnemyDownAnimation();
        upAnimation = game.getEnemyUpAnimation();
        leftAnimation = game.getEnemyLeftAnimation();
        rightAnimation = game.getEnemyRightAnimation();
        setTextureRegion(downAnimation.getKeyFrames()[0]);
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
        setTextureRegion(downAnimation.getKeyFrame(getTime(), true));
    }
}
