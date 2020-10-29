package effects;

import static main.Render.drawImage;

import assets.Textures;

public class HUDEffect extends VisualEffect {
	
	// timing
	private long timer;
	
	public HUDEffect() {
		timer = System.nanoTime();
	}
	
	public void update() {
		// nothing
	}
	
	public void render() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > 40000) drawImage(Textures.s_hud_scare, 0, 0);
		else {
			float percent = elapsed / 40000.0f;
			drawImage(Textures.s_hud_scare, 0, 0, percent);
		}
	}
}