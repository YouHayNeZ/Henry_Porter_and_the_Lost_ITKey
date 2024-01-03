package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public class MazeLoader {

    public static final int WALL = 0;
    public static final int ENTRY_POINT = 1;
    public static final int EXIT = 2;
    public static final int TRAP = 3;
    public static final int ENEMY = 4;
    public static final int KEY = 5;

    public static ObjectMap<String, Integer> loadMaze(String filePath) {
        ObjectMap<String, Integer> mazeMap = new ObjectMap<>();

        try {
            FileHandle fileHandle = Gdx.files.internal(filePath);
            String[] lines = fileHandle.readString().split("\n");

            for (String line : lines) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                int objectType = Integer.parseInt(parts[2].trim());

                mazeMap.put(x + "," + y, objectType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mazeMap;
    }
}