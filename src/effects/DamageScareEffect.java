package effects;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;

public class DamageScareEffect extends PlayableEffect {
	
	// effect
	private Texture overlay;
	
	// position
	private int x;
	
	// timing
	private long timer;
	
	public DamageScareEffect() {
		overlay = Textures.scare_glitch;
	}
	
	public void play() {
		timer = System.nanoTime();
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.HORROR);
		playing = true;
	}
	
	public void update() {
		if (playing) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > 1300) playing = false;
			else {
				x = (int) (Math.random() * 80) - 40;
			}
		}
	}
	
	public void render() {
		if (playing) {
			drawImage(overlay, -100 + x, -75, 0.5f);
		}
	}
}