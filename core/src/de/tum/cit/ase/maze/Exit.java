package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Exit extends GameObject{
    public Exit(Vector2 position, int type) {
        super(position, 2); // Type 2 for Exit
    }
}
