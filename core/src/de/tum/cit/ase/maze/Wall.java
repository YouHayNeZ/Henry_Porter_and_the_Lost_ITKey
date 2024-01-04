package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Wall extends GameObject {
    public Wall(Vector2 position, int type) {
        super(position, 0); // Type 0 for Wall
    }
}

