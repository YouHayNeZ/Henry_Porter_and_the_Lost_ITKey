package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

import java.util.Random;

/**
 * Enemy class represents enemy entity, which is movable and updatable.
 * It moves in random directions
 */
public class Enemy extends MovableEntity {

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private static final int DEST_ACCURACY = CELL_WIDTH / 8;
    private static final int DEFAULT_MOVE_LENGTH = CELL_WIDTH * 5;
    private static final float DEFAULT_SPEED = 25f;

    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> upAnimation;
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> rightAnimation;
    private final Random random;

    private float destX;
    private float destY;

    /**
     * Create new one enemy
     * @param game the main game
     */
    public Enemy(MazeRunnerGame game) {
        super(game);
        downAnimation = game.getEnemyDownAnimation();
        upAnimation = game.getEnemyUpAnimation();
        leftAnimation = game.getEnemyLeftAnimation();
        rightAnimation = game.getEnemyRightAnimation();

        random = new Random();

        resetDestPosition();

        setTextureRegion(downAnimation.getKeyFrames()[0]);
        setSpeed(DEFAULT_SPEED);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(downAnimation.getKeyFrame(getTime(), true));

        //check dest position equals actual position
        if (Math.abs(destX - getX()) < DEST_ACCURACY && Math.abs(destY - getY()) < DEST_ACCURACY) {
            int sign = random.nextBoolean() ? 1 : -1;
            if (random.nextBoolean()) {
                destX = getX() + sign * DEFAULT_MOVE_LENGTH;
                destY = getY();
            }
            else {
                destY = getY() + sign * DEFAULT_MOVE_LENGTH;
                destX = getX();
            }
        }

        boolean moveResult = true;
        if (destX < getX()) moveResult = moveLeft(delta);
        else if (destX > getX()) moveResult = moveRight(delta);
        else if (destY < getY()) moveResult = moveDown(delta);
        else if (destY > getY()) moveResult = moveUp(delta);

        //If cant move than reset dest position
        if (!moveResult) resetDestPosition();
    }

    /**
     * Reset enemy destination position
     */
    private void resetDestPosition() {
        destX = getX();
        destY = getY();
    }

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
    public void setX(float x) {
        super.setX(x);
        destX = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        destY = y;
    }
}