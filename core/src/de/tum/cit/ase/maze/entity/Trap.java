package de.tum.cit.ase.maze.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.ase.maze.MazeRunnerGame;

import java.util.Random;

/**
 * Trap class represents trap entity. Nothing unusual except that it can be blue or red.
 */
public class Trap extends UpdatableEntity {

    private Animation<TextureRegion> flameAnimation;
    private final Animation<TextureRegion> yellowFlameAnimation;
    private final Animation<TextureRegion> blueFlameAnimation;
    private final Animation<TextureRegion> redFlameAnimation;
    private final Animation<TextureRegion> greenFlameAnimation;

    private final Random random;

    private boolean yellow = false;
    private boolean blue = false;
    private boolean red = false;
    private boolean green = false;

    /**
     * Creates one new trap and randomly chooses its color.
     * @param game the main game
     */
    public Trap(MazeRunnerGame game) {
        super(game);
        flameAnimation = game.getFlameAnimation();
        yellowFlameAnimation = game.getYellowFlameAnimation();
        blueFlameAnimation = game.getBlueFlameAnimation();
        redFlameAnimation = game.getRedFlameAnimation();
        greenFlameAnimation = game.getGreenFlameAnimation();

        random = new Random();
//        if (new Random(. {
//            setTextureRegion(flameAnimation.getKeyFrames()[0]);
//            blue = true;
//        } else {
//            setTextureRegion(blueFlameAnimation.getKeyFrames()[0]);
//        }

        switch (random.nextInt(4)) {
            case 0 -> { yellow = true; setTextureRegion(yellowFlameAnimation.getKeyFrames()[0]);
            } // Yellow Flame
            case 1 -> { blue = true; setTextureRegion(blueFlameAnimation.getKeyFrames()[0]);
            } // Blue Flame
            case 2 -> { red = true; setTextureRegion(redFlameAnimation.getKeyFrames()[0]);
            } // Red Flame
            case 3 -> { green = true; setTextureRegion(greenFlameAnimation.getKeyFrames()[0]);
            } // Green Flame
            default -> setTextureRegion(flameAnimation.getKeyFrames()[0]); // Fallback to the default animation
        };

    }

    /**
     * Updates the trap animation.
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        if (yellow) {
            setTextureRegion(yellowFlameAnimation.getKeyFrame(getTime(), true));
        } else if (red) {
            setTextureRegion(redFlameAnimation.getKeyFrame(getTime(), true));
        } else if (green) {
            setTextureRegion(greenFlameAnimation.getKeyFrame(getTime(), true));
        } else if (blue) {
            setTextureRegion(blueFlameAnimation.getKeyFrame(getTime(), true));
        } else {
            setTextureRegion(flameAnimation.getKeyFrame(getTime(), true));
        }
    }
}
