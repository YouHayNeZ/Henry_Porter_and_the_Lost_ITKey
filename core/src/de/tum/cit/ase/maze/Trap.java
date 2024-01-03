package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Trap extends GameObject{
    public Trap(Vector2 position, int type, Texture texture) {
        super(position, 3, texture); // Type 3 for Trap
    }
}
