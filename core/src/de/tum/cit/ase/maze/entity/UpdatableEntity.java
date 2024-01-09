package de.tum.cit.ase.maze.entity;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * UpdatableEntity the parent of all objects that can change over time.
 * Mainly used to display animations, but can also simply change internal parameters over time
 */
public class UpdatableEntity extends Entity {

    private float time;

    /**
     * Creates one updatableEntity.
     * As with regular objects, if you want to create objects in this way, you need to define a texture
     * @param game the main game
     */
    public UpdatableEntity(MazeRunnerGame game) {
        super(game);
    }

    /**
     * Here the time variable is updated over time.
     * You can use it any time by calling <bold>getTime()</bold> for you animations in way:
     * <pre>
     *      {@code @Override}
     *      public void update(float delta) {
     *          super.update(delta);
     *          setTextureRegion(yourAnimation.getKeyFrame(getTime(), true));
     *      }
     * </pre>
     * Where <bold>yourAnimation</bold> - is your animation class
     * You can override this method to change the object as you wish, but it is advisable to call the parent method first
     * @param delta the delta time
     */
    public void update(float delta) {
        time += delta;
    }

    /**
     * Get time variable
     * @return time in milliseconds
     */
    public float getTime() {
        return time;
    }
}
