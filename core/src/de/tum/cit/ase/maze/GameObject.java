package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private Vector2 position;
    private int type;
    private Texture texture;

    public GameObject(Vector2 position, int type, Texture texture) {
        this.position = position;
        this.type = type;
        this.texture = texture;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void update(float deltaTime) {
        // Implement any necessary update logic for the object
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getType() {
        return type;
    }

    // You can add more methods as needed for specific game object behaviors

    // For example, a method to check for collision with the player
    public boolean collidesWithPlayer(Vector2 playerPosition) {
        // Implement collision logic here
        return false;
    }
}
