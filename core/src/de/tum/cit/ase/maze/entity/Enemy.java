package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

public class Enemy extends UpdatableEntity {

    private Animation<TextureRegion> enemyDownAnimation;

    public Enemy(MazeRunnerGame game) {
        super(game);
        enemyDownAnimation = game.getEnemyDownAnimation();
        setTextureRegion(enemyDownAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setTextureRegion(enemyDownAnimation.getKeyFrame(getTime(), true));
    }
}
