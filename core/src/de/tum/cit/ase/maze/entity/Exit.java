package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Exit class represents exit from the game.
 * Only opens if Player has a key.
 */
public class Exit extends UpdatableEntity {

    private static final float ACTION_PADDING = 4f;
    private static final float EXIT_PADDING = -7f;

    private final Animation<TextureRegion> animation;
    private boolean isOpening;

    /**
     * Create one new Exit.
     * @param game the main game
     */
    public Exit(MazeRunnerGame game) {
        super(game);
        animation = game.getDoorAnimation();
        isOpening = false;
        setTextureRegion(animation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        if (isOpening) {
            super.update(delta);
            setTextureRegion(animation.getKeyFrame(getTime(), false));
        }
    }

    /**
     * Get rectangle for action.
     * Used when Player has a key and it near.
     * Bigger than default entity rectangle.
     * @return Rectangle that represents action rectangle
     */
    public Rectangle getActionRectangle() {
        return getEntityRectangle(ACTION_PADDING);
    }

    /**
     * Get rectangle for exit.
     * Used when Player is near and exit is open.
     * Smaller than default entity rectangle.
     * @return Rectangle that represents exit rectangle
     */
    public Rectangle getExitRectangle() {
        return getEntityRectangle(EXIT_PADDING);
    }

    /**
     * Check if the door is open for exit.
     * @return true if exit is open
     */
    public boolean isOpen() {
        int lastIndex = animation.getKeyFrames().length - 1;
        return getTextureRegion() == animation.getKeyFrames()[lastIndex];
    }

    /**
     * Open the door.
     * Start an animation timer that's opening the door.
     */
    public void open() {
        isOpening = true;
    }
}
