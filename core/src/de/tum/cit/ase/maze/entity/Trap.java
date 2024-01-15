package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * Trap class represents trap entity. Nothing unusual.
 */
public class Trap extends UpdatableEntity {

    private final Animation<TextureRegion> flameAnimation;
    private final Array<Animation<TextureRegion>> trapAnimations;

    /**
     * Creates one new trap.
     * @param game the main game
     */
    public Trap(MazeRunnerGame game) {
        super(game);
        flameAnimation = game.getFlameAnimation();
        trapAnimations = game.getTrapAnimations();
        int randomValue = MathUtils.random(trapAnimations.size - 1);
        Animation<TextureRegion> selectedAnimation = trapAnimations.random();
        //        setTextureRegion(trapAnimations.get(0).getKeyFrames()[0]);
        setTextureRegion(selectedAnimation.getKeyFrames()[0]);
//        setTextureRegion(flameAnimation.getKeyFrames()[0]);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        int randomValue = MathUtils.random(trapAnimations.size - 1);
        Animation<TextureRegion> selectedAnimation = switch (randomValue) {
            case 0 -> trapAnimations.get(0); // Yellow Flame
            case 1 -> trapAnimations.get(1); // Blue Flame
            case 2 -> trapAnimations.get(2); // Red Flame
            case 3 -> trapAnimations.get(3); // Green Flame
            default -> trapAnimations.get(0); // Fallback to the first animation
        };
        setTextureRegion(selectedAnimation.getKeyFrame(getTime(), true));
//        setTextureRegion(flameAnimation.getKeyFrame(getTime(), true));
    }
}
