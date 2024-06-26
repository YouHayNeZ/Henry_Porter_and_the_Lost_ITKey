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
 * MovableEntity class allows the entities to move in four directions,
 * but it's needed to implement animation for each direction.
 */
public abstract class MovableEntity extends UpdatableEntity {

    // World set up
    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;
    private static final float DEFAULT_SPEED = 50f;

    private float speed;

    /**
     * Creates one new movable entity.
     * @param game the main game.
     */
    protected MovableEntity(MazeRunnerGame game) {
        super(game);
        this.speed = DEFAULT_SPEED;
    }

    /**
     * Move entity into the UP direction.
     * @param delta the delta time.
     * @return true if the object can move without obstacles in the given direction.
     */
    public boolean moveUp(float delta) {
        setTextureRegion(getUpAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("y", getY() + speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity into the DOWN direction.
     * @param delta the delta time.
     * @return true if the object can move without obstacles in the given direction.
     */
    public boolean moveDown(float delta) {
        setTextureRegion(getDownAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("y", getY() - speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity into the LEFT direction.
     * @param delta the delta time.
     * @return true if the object can move without obstacles in the given direction.
     */
    public boolean moveLeft(float delta) {
        setTextureRegion(getLeftAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("x", getX() - speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Move entity into the RIGHT direction.
     * @param delta the delta time.
     * @return true if the object can move without obstacles in the given direction.
     */
    public boolean moveRight(float delta) {
        setTextureRegion(getRightAnimation().getKeyFrame(getTime(), true));
        return setFloatValueIfNot("x", getX() + speed * delta,
                MovableEntity::checkBordersWallAndExitCollision);
    }

    /**
     * Get animation of move into the UP direction.
     * @return Animation<TextureRegion> class that contains animation.
     */
    public abstract Animation<TextureRegion> getUpAnimation();

    /**
     * Get animation of move into the DOWN direction.
     * @return Animation<TextureRegion> class that contains animation.
     */
    public abstract Animation<TextureRegion> getDownAnimation();

    /**
     * Get animation of move into the LEFT direction.
     * @return Animation<TextureRegion> class that contains animation.
     */
    public abstract Animation<TextureRegion> getLeftAnimation();

    /**
     * Get animation of move into the RIGHT direction.
     * @return Animation<TextureRegion> class that contains animation.
     */
    public abstract Animation<TextureRegion> getRightAnimation();

    /**
     * Check the current entity with borders, wall and exit collision.
     * @return true if collision occurred.
     */
    private boolean checkBordersWallAndExitCollision() {
        Rectangle rectangle = getEntityRectangle();

        LevelMap levelMap = getGame().getLevelMap(); // get the level map
        if (rectangle.x < 0 || rectangle.x + rectangle.width > levelMap.getMapWidth() ||
                rectangle.y < 0 || rectangle.y + rectangle.height > levelMap.getMapHeight()) { // check borders
            return true;
        }

        Rectangle entityRectangle;
        int size = getGame().getLevelMap().getEntities().size;
        for (int i = 0; i < size; i++) {
            Entity entity = getGame().getLevelMap().getEntities().get(i);
            if (entity instanceof Wall wall) {
                entityRectangle = new Rectangle(wall.getX(), wall.getY(), CELL_WIDTH, CELL_HEIGHT);
                if (Intersector.overlaps(rectangle, entityRectangle)) { // check wall collision
                    return true;
                }
            }
            if (entity instanceof Exit exit) {
                entityRectangle = exit.getEntityRectangle();
                if (!exit.isOpen() && Intersector.overlaps(rectangle, entityRectangle)) { // check exit collision
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Set float value if the condition returns true.
     * @param propertyName the property name that will change.
     * @param newValue the new value.
     * @param condition the condition.
     * @return true if value changed.
     */
    private boolean setFloatValueIfNot(String propertyName, float newValue, Function<MovableEntity, Boolean> condition) {
        try {
            Field field = Entity.class.getDeclaredField(propertyName); // get field
            float oldValue = field.getFloat(this);
            field.setFloat(this, newValue);
            if (condition.apply(this)) {
                field.setFloat(this, oldValue);
                return false;
            }
        }
        catch (Exception exception) {
            throw new RuntimeException("Cant set float value in movable entity: " + this, exception);
        }
        return true;
    }

    /**
     * Set speed property.
     * @param speed the speed property.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
