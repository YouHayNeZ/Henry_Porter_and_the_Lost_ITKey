package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import de.tum.cit.ase.maze.LevelMap;
import de.tum.cit.ase.maze.MazeRunnerGame;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * MovableEntity allow your entities to move in 4 directions, but need to implement animation for each direction
 */
public abstract class MovableEntity extends UpdatableEntity {

    // World cell width size
    private static final int CELL_WIDTH = 16;

    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private static final float DEFAULT_SPEED = 50f;

    private float speed;

    protected MovableEntity(MazeRunnerGame game) {
        super(game);
        this.speed = DEFAULT_SPEED;
    }

    /**
     * Move entity to up direction
     * @param delta the delta time
     * @return true if the object can move without obstacles in the given direction
     */
    public boolean moveUp(float delta) {
        setTextureRegion(getUpAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("y", getY() + speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity to down direction
     * @param delta the delta time
     * @return true if the object can move without obstacles in the given direction
     */
    public boolean moveDown(float delta) {
        setTextureRegion(getDownAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("y", getY() - speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity to left direction
     * @param delta the delta time
     * @return true if the object can move without obstacles in the given direction
     */
    public boolean moveLeft(float delta) {
        setTextureRegion(getLeftAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("x", getX() - speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity to right direction
     * @param delta the delta time
     * @return true if the object can move without obstacles in the given direction
     */
    public boolean moveRight(float delta) {
        setTextureRegion(getRightAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("x", getX() + speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Get animation of move to up direction
     * @return Animation<TextureRegion> class that contains animation
     */
    public abstract Animation<TextureRegion> getUpAnimation();

    /**
     * Get animation of move to down direction
     * @return Animation<TextureRegion> class that contains animation
     */
    public abstract Animation<TextureRegion> getDownAnimation();

    /**
     * Get animation of move to left direction
     * @return Animation<TextureRegion> class that contains animation
     */
    public abstract Animation<TextureRegion> getLeftAnimation();

    /**
     * Get animation of move to right direction
     * @return Animation<TextureRegion> class that contains animation
     */
    public abstract Animation<TextureRegion> getRightAnimation();

    /**
     * Check current entity with borders, wall and exit collision
     * @return true if collision occurred
     */
    private boolean checkBordersWallAndExitCollision() {
        Rectangle rectangle = getEntityRectangle();

        LevelMap levelMap = getGame().getLevelMap();
        if (rectangle.x < 0 || rectangle.x + rectangle.width > levelMap.getMapWidth() ||
                rectangle.y < 0 || rectangle.y + rectangle.height > levelMap.getMapHeight()) return true;
//
//        Rectangle entityRectangle;
//        int size = getGame().getLevelMap().getEntities().size;
//        for (int i = 0; i < size; i++) {
//            Entity e = getGame().getLevelMap().getEntities().get(i);
//            if (e instanceof Wall wall) {
//                entityRectangle = new Rectangle(wall.getX(), wall.getY(), CELL_WIDTH, CELL_HEIGHT);
//                if (Intersector.overlaps(rectangle, entityRectangle)) return true;
//            }
//            if (e instanceof Exit exit) {
//                entityRectangle = exit.getEntityRectangle();
//                if (!exit.isOpen() && Intersector.overlaps(rectangle, entityRectangle)) return true;
//            }
//        }

        return false;
    }

    /**
     * Set float value if condition returns true
     * @param propertyName the property name that will change
     * @param newValue the new value
     * @param condition the condition
     * @return true if value changed
     */
    private boolean setFloatValueIfNot(String propertyName, float newValue, Function<MovableEntity, Boolean> condition) {
        try {
            Field field = Entity.class.getDeclaredField(propertyName);
            float oldValue = field.getFloat(this);
            field.setFloat(this, newValue);
            if (condition.apply(this)) {
                field.setFloat(this, oldValue);
                return false;
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cant set float value in movable entity: " + this, e);
        }
        return true;
    }

    /**
     * Get speed property
     * @return the speed property
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Set speed property
     * @param speed the speed property
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
