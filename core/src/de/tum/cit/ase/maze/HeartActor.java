package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HeartActor extends Actor {
    private Animation<TextureRegion> animation;
    private float stateTime;

    public HeartActor(Animation<TextureRegion> animation) {
        this.animation = animation;
        setWidth(50);
        setHeight(50);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(animation.getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());
    }
}
