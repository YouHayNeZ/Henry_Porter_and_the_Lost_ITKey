package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Entity class is a parent for all game objects.
 * This contains common object elements that are necessary for simple texture drawing on the screen
 */
public class Entity {

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private static final float DEFAULT_PADDING = -1;

    MazeRunnerGame game;
    TextureRegion textureRegion;
    float x, y;
    float xDrawOffset, yDrawOffset;

    /**
     * Creates one entity.
     * If you want to create entity by this way - you need to declare textureRegion and position for this entity
     * @param game the main game
     */
    public Entity(MazeRunnerGame game) {
        this.game = game;
    }

    /**
     * Draw entity on sprite batch.
     * Declare textureRegion before calling this method to draw something
     * @param batch the sprite batch, that used to draw
     */
    public void draw(SpriteBatch batch) {
        if (textureRegion != null) {
            batch.draw(textureRegion, x + xDrawOffset, y + yDrawOffset,
                    textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        }
    }

    /**
     * Set draw offset to draw along the middle coordinates
     */
    public void centerDrawOffset() {
        xDrawOffset = (float) -textureRegion.getRegionWidth() / 2;
        yDrawOffset = (float) -textureRegion.getRegionHeight() / 2;
    }

    //Getter & Setter methods
    /**
     * Get entity rectangle with paddings.
     * Used to calculate collisions or other actions
     * @param padding the padding in pixels
     * @return rectangle with paddings
     */
    public Rectangle getEntityRectangle(float padding) {
        return new Rectangle(getX() - padding, getY() - padding,
                CELL_WIDTH + padding * 2, CELL_HEIGHT + padding * 2);
    }

    /**
     * Get entity default rectangle with DEFAULT PADDINGS which is -1.
     * Used to calculate collisions or other actions
     * @return rectangle with DEFAULT PADDINGS
     */
    public Rectangle getEntityRectangle() {
        return getEntityRectangle(DEFAULT_PADDING);
    }

    /**
     * Get x position
     * @return the x position in float
     */
    public float getX() {
        return x;
    }

    /**
     * Set x position
     * @param x the x position in float
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get y position
     * @return the y position in float
     */
    public float getY() {
        return y;
    }

    /**
     * Set y position
     * @param y the y position in float
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get texture region
     * @return the texture region
     */
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    /**
     * Set texture region
     * @param textureRegion the texture region
     */
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * Get game instance
     * @return the game instance
     */
    protected MazeRunnerGame getGame() {
        return game;
    }
}
