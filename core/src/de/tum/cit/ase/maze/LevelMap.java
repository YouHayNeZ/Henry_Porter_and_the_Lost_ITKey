package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;

import de.tum.cit.ase.maze.entity.*;

/**
 * LevelMap class provides the ability to work with maps, namely:
 * - load maps
 * - search for certain game objects on the map
 * - calculate the size of the map
 * - and also gain access to game objects
 */
public class LevelMap {

    /**
     * Type enum provides a list of all possible feature classes (entities) on a map
     */
    public enum Type {

        WALL(0, Wall.class),
        ENTRY_POINT(1, EntryPoint.class),
        EXIT(2, Exit.class),
        TRAP(3, Trap.class),
        ENEMY(4, Enemy.class),
        KEY(5, Key.class),
        HEART(6, Heart.class),
        COIN(7, Coin.class),
        CLOCK(8, Clock.class),
        POTION(9, Potion.class);

        final int value;
        final Class aClass;

        /**
         * Creates a new type.
         * @param value type value
         * @param aClass type class
         */
        Type(int value, Class aClass) {
            this.value = value;
            this.aClass = aClass;
        }

        /**
         * Get the type by value.
         * @param value type value
         * @return type
         */
        public static Type valueOf(int value) {
            for (Type type: values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }

        /**
         * Get type value.
         * @return type value
         */
        public int getValue() {
            return value;
        }

        /**
         * Get type class.
         * @return type class
         */
        public Class getaClass() {
            return aClass;
        }
    }

    // Set up world
    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;

    // Storage of all entities from map
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
     * Load the map from the file that is in the LOCAL path.
     * @param path local path
     * @throws IOException when we can't load the map from this file
     */
    public void load(String path) throws IOException {
        load(Gdx.files.local(path));
    }

    /**
     * Load map from FileHandle.
     * @param fileHandle fileHandle where we are going to load the map
     * @throws IOException when we can't load the map from this fileHandle
     */
    public void load(FileHandle fileHandle) throws IOException {
        entities = new Array<>();

        // Read file content
        ObjectMap<String, String> map = new ObjectMap<>(); // first String is coordinates, second is the type
        PropertiesUtils.load(map, fileHandle.reader());
        map.forEach(entry -> {
            try {
                String[] coords = entry.key.split(","); // split coordinates
                // check if coordinates are correct (introduced because of level map 2)
                if (coords.length == 2) {
                    int typeValue = Integer.parseInt(entry.value); // get type value
                    Type type = Type.valueOf(typeValue); // get type
                    assert type != null;
                    Entity entity = (Entity) type.getaClass().getConstructor(MazeRunnerGame.class)
                            .newInstance(game); // create new entity
                    int col = Integer.parseInt(coords[0]); // get column
                    int row = Integer.parseInt(coords[1]); // get row
                    entity.setX(col * CELL_WIDTH); // set x coordinate
                    entity.setY(row * CELL_HEIGHT); // set y coordinate

                    // Additional options if it is a wall or inner water
                    if (entity instanceof Wall wall) {
                        boolean hasLowerWall = checkIfCellExistsAndItIsWall(map, col, row - 1);
                        boolean hasUpperWall = checkIfCellExistsAndItIsWall(map, col, row + 1);
                        boolean isWater = checkIfCellExistsAndItIsWater(map, col, row);
                        boolean aboveWater = checkIfCellExistsAndItIsWater(map, col, row - 1);

                        if (isWater) {
                            wall.setRepresentationType(Wall.RepresentationType.WATER);
                        } else if (aboveWater) {
                            wall.setRepresentationType(Wall.RepresentationType.LOWER_WITHOUT_UPPER);
                        } else if (hasLowerWall && hasUpperWall) {
                            wall.setRepresentationType(Wall.RepresentationType.CENTER_WITH_UPPER_AND_LOWER);
                        } else if (hasLowerWall) {
                            wall.setRepresentationType(Wall.RepresentationType.UPPER);
                        } else if (hasUpperWall) {
                            wall.setRepresentationType(Wall.RepresentationType.LOWER_WITH_UPPER);
                        }
                        else {
                            wall.setRepresentationType(Wall.RepresentationType.LOWER_WITHOUT_UPPER);
                        }
                    }

                    entities.add(entity); // add entity to the list to store them
                }
            }
            catch (Exception exception) {
                // ignore wrong lines
                exception.printStackTrace();
            }
        });
    }

    /**
     * Find (first) entry point in a map.
     * @return (first) entry point
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
     * Check if cell exists in current col and row position and it is a wall.
     * @param map level map
     * @param col current column
     * @param row current row
     * @return true if cell exists and it is a wall
     */
    private boolean checkIfCellExistsAndItIsWall(ObjectMap<String, String> map, int col, int row) {
        try {
            // Get value from the map with format "col,row"
            String value = map.get(String.format("%d,%d", col, row));
            // Check if the value isn't null and it's a wall
            return value != null && Integer.parseInt(value) == Type.WALL.getValue();
        }
        catch (NumberFormatException e) {
            // Ignore the wrong file format
        }
        return false;
    }

    private boolean checkIfCellExistsAndItIsWater(ObjectMap<String, String> map, int col, int row) {
        try {
            String value = map.get(String.format("%d,%d", col, row)); // get value from the map with format "col,row"
            boolean hasOuterWalls = checkIfCellExistsAndItIsWall(map, col, row - 1) &&
                    checkIfCellExistsAndItIsWall(map, col, row + 1) &&
                    checkIfCellExistsAndItIsWall(map, col - 1, row) &&
                    checkIfCellExistsAndItIsWall(map, col + 1, row) &&
                    checkIfCellExistsAndItIsWall(map, col - 1, row - 1) &&
                    checkIfCellExistsAndItIsWall(map, col + 1, row - 1) &&
                    checkIfCellExistsAndItIsWall(map, col - 1, row + 1) &&
                    checkIfCellExistsAndItIsWall(map, col + 1, row + 1);
            // Check if the value isn't null and it's a wall
            return value != null && Integer.parseInt(value) == Type.WALL.getValue() && hasOuterWalls;
        }
        catch (NumberFormatException e) {
            // Ignore the wrong file format
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

    /**
     * Find the number of keys in a map.
     * @return number of keys.
     */
    public int findNumberOfKeys() {
        int numberOfKeys = 0;
        for (Entity entity: entities){
            if(entity instanceof Key){
                numberOfKeys++;
            }
        }
        return numberOfKeys;
    }
}
