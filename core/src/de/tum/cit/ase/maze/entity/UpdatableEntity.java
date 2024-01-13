package de.tum.cit.ase.maze.entity;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * UpdatableEntity class is the parent class of all game objects that can change over time.
 * Mainly used to display animations, but can also simply change internal parameters over time.
 */
public class UpdatableEntity extends Entity {

    private float time;

    /**
     * Creates one updatableEntity.
     * As with regular game objects, if we want to create game objects in this way, we need to define a texture.
     * @param game the main game
     */
    public UpdatableEntity(MazeRunnerGame game) {
        super(game);
    }

    /**
     * Here the time variable is updated over time.
     * We can use it any time by calling <bold>getTime()</bold> for the animations in way:
     *      {@code @Override}
     *      public void update(float delta) {
     *          super.update(delta);
     *          setTextureRegion(entityAnimation.getKeyFrame(getTime(), true));
     *      }
     * Where <bold>entityAnimation</bold> - is the animation class
     * We can override this method to change the object as we wish, but it is advisable to call the parent method first.
     * @param delta the delta time
     */
    public void update(float delta) {
        time += delta;
    }

    /**
     * Get time variable.
     * @return time in milliseconds
     */
    public float getTime() {
        return time;
    }
}
