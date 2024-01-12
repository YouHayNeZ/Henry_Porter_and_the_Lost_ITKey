package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Wall class represents wall entity.
 * Can be displayed in different ways depending on the position of other walls in the map.
 * The LevelMap class is responsible for setting the correct display.
 * Blocks the user or enemy so that they cannot pass.
 */
public class Wall extends Entity {

    /**
     * RepresentationType provides a list of possible displaying
     */
    public enum RepresentationType {

        LOWER_WITHOUT_UPPER(0),
        CENTER_WITH_UPPER_AND_LOWER(1),
        LOWER_WITH_UPPER(2),
        UPPER(3);

        final int imageIndex;

        RepresentationType(int imageIndex) {
            this.imageIndex = imageIndex;
        }
    }

    Array<TextureRegion> textureRegionArray;
    RepresentationType representationType;

    /**
     * Creates one wall.
     * @param game the main game
     */
    public Wall(MazeRunnerGame game) {
        super(game);
        textureRegionArray = game.getWallTextureRegionArray();
        setRepresentationType(RepresentationType.CENTER_WITH_UPPER_AND_LOWER);
    }

    /**
     * Set representation type.
     * @param type the representation type
     */
    public void setRepresentationType(RepresentationType type) {
        this.representationType = type;
        setTextureRegion(textureRegionArray.get(representationType.imageIndex));
    }
}
