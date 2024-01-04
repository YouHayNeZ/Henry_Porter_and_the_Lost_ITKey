package de.tum.cit.ase.maze;

public class Maze {
    private final int width;
    private final int height;
    private final int[][] cells;

    /**
     * Constructor for Maze.
     *
     * @param width  The width of the maze.
     * @param height The height of the maze.
     */
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new int[width][height];
    }

    /**
     * Get the width of the maze.
     *
     * @return The width of the maze.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the maze.
     *
     * @return The height of the maze.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the type of a cell at a specific position in the maze.
     *
     * @param x    The x-coordinate of the cell.
     * @param y    The y-coordinate of the cell.
     * @param type The type to set for the cell.
     */
    public void setCell(int x, int y, int type) {
        if (isValidPosition(x, y)) {
            cells[x][y] = type;
        }
    }

    /**
     * Get the type of a cell at a specific position in the maze.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return The type of the cell.
     */
    public int getCell(int x, int y) {
        if (isValidPosition(x, y)) {
            return cells[x][y];
        }
        return -1; // Return -1 for invalid positions
    }

    /**
     * Check if a position is valid within the maze.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the position is valid, false otherwise.
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
