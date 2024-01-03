package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Key extends GameObject{
    public Key(Vector2 position, int type, Texture texture) {
        super(position, 5, texture); // Type 5 for Key
    }
}
