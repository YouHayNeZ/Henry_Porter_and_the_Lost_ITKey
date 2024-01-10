package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Wall extends Entity {

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

    public Wall(MazeRunnerGame game) {
        super(game);
        textureRegionArray = game.getWallTextureRegionArray();
        setRepresentationType(RepresentationType.CENTER_WITH_UPPER_AND_LOWER);
    }

    public void setRepresentationType(RepresentationType type) {
        this.representationType = type;
        setTextureRegion(textureRegionArray.get(representationType.imageIndex));
    }

    public RepresentationType getRepresentationType() {
        return representationType;
    }
}
