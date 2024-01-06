package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Entity {

    MazeRunnerGame game;
    TextureRegion textureRegion;
    float x, y;
    float xDrawOffset, yDrawOffset;

    public Entity(MazeRunnerGame game) {
        this.game = game;
    }

    public void draw(SpriteBatch batch) {
        if (textureRegion != null) {
            batch.draw(textureRegion, x + xDrawOffset, y + yDrawOffset,
                    textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        }
    };

    void centerDrawOffset() {
        xDrawOffset = -textureRegion.getRegionWidth() / 2;
        yDrawOffset = -textureRegion.getRegionHeight() / 2;
    }

    void resetDrawOffset() {
        xDrawOffset = 0;
        yDrawOffset = 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public float getXDrawOffset() {
        return xDrawOffset;
    }

    public void setXDrawOffset(float xDrawOffset) {
        this.xDrawOffset = xDrawOffset;
    }

    public float getYDrawOffset() {
        return yDrawOffset;
    }

    public void setYDrawOffset(float yDrawOffset) {
        this.yDrawOffset = yDrawOffset;
    }

    protected MazeRunnerGame getGame() {
        return game;
    }
}
