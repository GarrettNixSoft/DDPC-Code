package effects;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;

public class BloodEffect extends PlayableEffect {
	
	// render
	private Texture vignette;
	
	// timing
	private long timer;
	
	public BloodEffect() {
		vignette = Textures.vignette_red;
	}
	
	public void play() {
		playing = true;
		timer = System.nanoTime();
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.SLICE);
		Sfx.stopSound(Sfx.TYPE_PLAYER, Sfx.HURT);
	}
	
	public void update() {
		if (playing) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > 1000) playing = false;
		}
	}
	
	public void render() {
		if (playing) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			float percent = 1.0f - (elapsed / 1000.0f);
			drawImage(vignette, 0, 0, percent);
		}
	}
}