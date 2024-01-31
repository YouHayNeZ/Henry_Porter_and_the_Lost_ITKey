package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Enemy class represents enemy entity, which is movable and updatable.
 * It moves in random directions.
 */
public class Enemy extends MovableEntity {

    // World set up
    private static final int CELL_WIDTH = 16;
    private static final int DEST_ACCURACY = CELL_WIDTH / 8;
    private static final int DEFAULT_MOVE_LENGTH = CELL_WIDTH * 5;
    private static final float DEFAULT_SPEED = 25f;

    // Animation
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> upAnimation;
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> rightAnimation;
    private final Random random;

    // Destination position
    private float destX;
    private float destY;

    /**
     * Creates one new enemy.
     * @param game the main game
     */
    public Enemy(MazeRunnerGame game) {
        super(game);
        downAnimation = game.getEnemyDownAnimation();
        upAnimation = game.getEnemyUpAnimation();
        leftAnimation = game.getEnemyLeftAnimation();
        rightAnimation = game.getEnemyRightAnimation();

        random = new Random(); // random generator

        resetDestPosition(); // set destination position to actual position

        setTextureRegion(downAnimation.getKeyFrames()[0]); // set default texture region
        setSpeed(DEFAULT_SPEED); // set the default speed (same speed as player)
    }

    /**
     * Updates the enemy animation and moves it in random directions.
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(downAnimation.getKeyFrame(getTime(), true));

        // check if destination position equals actual position
        if (Math.abs(destX - getX()) < DEST_ACCURACY && Math.abs(destY - getY()) < DEST_ACCURACY) {
            int sign = random.nextBoolean() ? 1 : -1; // random sign
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
        // move enemy in the direction of destination position
        if (destX < getX()) {
            moveResult = moveLeft(delta);
        } else if (destX > getX()) {
            moveResult = moveRight(delta);
        } else if (destY < getY()) {
            moveResult = moveDown(delta);
        } else if (destY > getY()) {
            moveResult = moveUp(delta);
        }

        // If it cannot move than reset destination position
        if (!moveResult) resetDestPosition();
    }

    /**
     * Reset enemy destination position.
     */
    private void resetDestPosition() {
        destX = getX();
        destY = getY();
    }

    /**
     * Returns the enemy's up animation.
     * @return the enemy's up animation
     */
    @Override
    public Animation<TextureRegion> getUpAnimation() {
        return upAnimation;
    }

    /**
     * Returns the enemy's down animation.
     * @return the enemy's down animation
     */
    @Override
    public Animation<TextureRegion> getDownAnimation() {
        return downAnimation;
    }

    /**
     * Returns the enemy's left animation.
     * @return the enemy's left animation
     */
    @Override
    public Animation<TextureRegion> getLeftAnimation() {
        return leftAnimation;
    }

    /**
     * Returns the enemy's right animation.
     * @return the enemy's right animation
     */
    @Override
    public Animation<TextureRegion> getRightAnimation() {
        return rightAnimation;
    }

    /**
     * Sets the enemy's x position.
     * @param x the enemy's x position
     */
    @Override
    public void setX(float x) {
        super.setX(x);
        destX = x;
    }

    /**
     * Sets the enemy's y position.
     * @param y the enemy's y position
     */
    @Override
    public void setY(float y) {
        super.setY(y);
        destY = y;
    }
}