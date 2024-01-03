package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends GameObject{
    public Enemy(Vector2 position, int type, Texture texture) {
        super(position, 4, texture); // Type 4 for Enemy
    }
}
