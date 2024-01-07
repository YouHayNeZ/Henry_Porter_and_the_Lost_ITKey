package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Key extends Entity {

    private Array<TextureRegion> switchTextureRegionArray;
    public Key(MazeRunnerGame game) {
        super(game);
        switchTextureRegionArray = game.getSwitchTextureRegionArray();
        setTextureRegion(switchTextureRegionArray.get(0));
    }
}
