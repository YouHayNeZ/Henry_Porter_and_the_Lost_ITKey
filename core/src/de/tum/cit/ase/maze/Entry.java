package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Entry extends GameObject {
    public Entry(Vector2 position, int type) {
        super(position, 1); // Type 1 for Entry
    }
}
