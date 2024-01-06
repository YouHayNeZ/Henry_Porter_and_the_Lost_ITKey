package de.tum.cit.ase.maze.entity;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class UpdatableEntity extends Entity {

    private float time;
    public UpdatableEntity(MazeRunnerGame game) {
        super(game);
    }

    public void update(float delta) {
        time += delta;
    }

    public void resetTime() {
        time = 0f;
    }

    public float getTime() {
        return time;
    }
}
