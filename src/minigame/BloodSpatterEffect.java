package minigame;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import effects.VisualEffect;
import entity.util.Animation;
import main.Render;

import static main.Render.drawImageC;

public class BloodSpatterEffect extends VisualEffect {
	
	// directions
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	// frames
	private Animation animation;
	private Texture[] frames;
	private boolean playing;
	
	public BloodSpatterEffect(int direction) {
		animation = new Animation();
		switch(direction) {
		case LEFT:
			frames = Textures.blood_spatter_left;
			break;
		case RIGHT:
			frames = Textures.blood_spatter_right;
			break;
		}
		animation.setFrames(frames);
		animation.setDelay(40);
		animation.setSingle(true);
	}
	
	public void play() {
		playing = true;
	}
	
	public void reset() {
		playing = false;
		animation.reset();
	}
	
	@Override
	public void update() {
		if (!playing) return;
		if (!animation.hasPlayedOnce()) animation.update();
	}

	@Override
	public void render() {
		if (!playing) return;
		drawImageC(animation.getCurrentFrame(), Render.WIDTH / 2, Render.HEIGHT / 2);
	}
	
}