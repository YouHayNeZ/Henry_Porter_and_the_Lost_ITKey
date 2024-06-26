package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * EntryPoint class represents Player's starting point.
 */
public class EntryPoint extends Entity {

    TextureRegion textureRegion;

    /**
     * Creates new EntryPoint.
     * @param game the main game
     */
    public EntryPoint(MazeRunnerGame game) {
        super(game);
        textureRegion = game.getLadderTextureRegion();
        setTextureRegion(textureRegion);
    }
}
