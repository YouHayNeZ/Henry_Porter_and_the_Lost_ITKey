package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Enemy extends MovableEntity {

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private final Animation<TextureRegion> enemyDownAnimation;

    public Enemy(MazeRunnerGame game) {
        super(game);
        enemyDownAnimation = game.getEnemyDownAnimation();
        setTextureRegion(enemyDownAnimation.getKeyFrames()[0]);
    }

    /**
     * Get animation of move to up direction
     *
     * @return Animation<TextureRegion> class that contains animation
     */
    @Override
    public Animation<TextureRegion> getUpAnimation() {
        return null;
    }

    /**
     * Get animation of move to down direction
     *
     * @return Animation<TextureRegion> class that contains animation
     */
    @Override
    public Animation<TextureRegion> getDownAnimation() {
        return null;
    }

    /**
     * Get animation of move to left direction
     *
     * @return Animation<TextureRegion> class that contains animation
     */
    @Override
    public Animation<TextureRegion> getLeftAnimation() {
        return null;
    }

    /**
     * Get animation of move to right direction
     *
     * @return Animation<TextureRegion> class that contains animation
     */
    @Override
    public Animation<TextureRegion> getRightAnimation() {
        return null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(enemyDownAnimation.getKeyFrame(getTime(), true));
    }
}
