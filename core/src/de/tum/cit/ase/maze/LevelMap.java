package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;

import de.tum.cit.ase.maze.entity.Enemy;
import de.tum.cit.ase.maze.entity.Entity;
import de.tum.cit.ase.maze.entity.EntryPoint;
import de.tum.cit.ase.maze.entity.Exit;
import de.tum.cit.ase.maze.entity.Key;
import de.tum.cit.ase.maze.entity.Trap;
import de.tum.cit.ase.maze.entity.Wall;

/**
 * LevelMap class provides the ability to work with maps, namely:
 * - load maps
 * - search for certain game objects on the map
 * - calculate the size of the map
 * - and also gain access to game objects
 */
public class LevelMap {

    /**
     * Type enum provides a list of all possible feature classes on a map
     */
    public enum Type {

        WALL(0, Wall.class),
        ENTRY_POINT(1, EntryPoint.class),
        EXIT(2, Exit.class),
        TRAP(3, Trap.class),
        ENEMY(4, Enemy.class),
        KEY(5, Key.class);

        final int value;
        final Class aClass;

        Type(int value, Class aClass) {
            this.value = value;
            this.aClass = aClass;
        }

        public static Type valueOf(int value) {
            for (Type type: values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public Class getaClass() {
            return aClass;
        }
    }

    // World cell width size
    private static final int CELL_WIDTH = 16;
    // World cell height size
    private static final int CELL_HEIGHT = 16;

    private Array<Entity> entities;

    private final MazeRunnerGame game;

    /**
     * Creates level map.
     * @param game the game instance
     */
    public LevelMap(MazeRunnerGame game) {
        this.game = game;
        this.entities = new Array<>();
    }

    /**
     * Load map from file that is in the LOCAL path.
     * @param path local path
     * @throws IOException when we can't load map from this file
     */
    public void load(String path) throws IOException {
        load(Gdx.files.local(path));
    }

    /**
     * Load map from FileHandle.
     * @param fileHandle fileHandle where we are going to load map
     * @throws IOException when we can't load map from this fileHandle
     */
    public void load(FileHandle fileHandle) throws IOException {
        entities = new Array<>();

        //Read file content
        ObjectMap<String, String> map = new ObjectMap<>();
        PropertiesUtils.load(map, fileHandle.reader());
        map.forEach(entry -> {
            try {
                String[] coords = entry.key.split(",");
                //check if coordinates is correct
                if (coords.length == 2) {
                    int typeValue = Integer.parseInt(entry.value);
                    Type type = Type.valueOf(typeValue);
                    Entity entity = (Entity) type.getaClass().getConstructor(MazeRunnerGame.class)
                            .newInstance(game);
                    int col = Integer.parseInt(coords[0]);
                    int row = Integer.parseInt(coords[1]);
                    entity.setX(col * CELL_WIDTH);
                    entity.setY(row * CELL_HEIGHT);

                    //Additional options if it's a wall
                    if (entity instanceof Wall wall) {
                        boolean hasLowerWall = checkIfCellExistsAndItIsWall(map, col, row - 1);
                        boolean hasUpperWall = checkIfCellExistsAndItIsWall(map, col, row + 1);

                        wall.setRepresentationType(Wall.RepresentationType.LOWER_WITHOUT_UPPER);
                        if (hasLowerWall && hasUpperWall) {
                            wall.setRepresentationType(Wall.RepresentationType.CENTER_WITH_UPPER_AND_LOWER);
                        } else if (hasLowerWall) {
                            wall.setRepresentationType(Wall.RepresentationType.UPPER);
                        } else if (hasUpperWall) {
                            wall.setRepresentationType(Wall.RepresentationType.LOWER_WITH_UPPER);
                        }
                    }

                    entities.add(entity);
                }
            }
            catch (Exception exception) {
                // ignore wrong lines
                exception.printStackTrace();
            }
        });
    }

    /**
     * Find first entry point in a map.
     * @return first entry point
     */
    public EntryPoint findEntryPoint() {
        for (Entity entity: entities) {
            if (entity instanceof EntryPoint entryPoint) {
                return entryPoint;
            }
        }
        return null;
    }

    /**
     * Calculates map width.
     * @return map width in pixels
     */
    public float getMapWidth() {
        float maxX = 0;
        for (Entity entity: entities) {
            if (entity.getX() > maxX) {
                maxX = entity.getX();
            }
        }
        return maxX + CELL_WIDTH;
    }

    /**
     * Calculates map height.
     * @return map height in pixels
     */
    public float getMapHeight() {
        float maxY = 0;
        for (Entity entity: entities) {
            if (entity.getY() > maxY) {
                maxY = entity.getY();
            }
        }
        return maxY + CELL_HEIGHT;
    }

    /**
     * Check if cell exist in current col and row position and it is a wall.
     * @param map level map
     * @param col current column
     * @param row current row
     * @return true if cell exists and it is a wall
     */
    private boolean checkIfCellExistsAndItIsWall(ObjectMap<String, String> map, int col, int row) {
        try {
            String value = map.get(String.format("%d,%d", col, row));
            return value != null && Integer.parseInt(value) == Type.WALL.getValue();
        }
        catch (NumberFormatException e) {
            // Ignore wrong file format
        }
        return false;
    }

    /**
     * Get all entities from this map.
     * @return array of all entities
     */
    public Array<Entity> getEntities() {
        return entities;
    }
}
