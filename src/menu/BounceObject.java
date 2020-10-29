package menu;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

public class BounceObject extends MenuObject {
	
	// timing and size
	private int stage;
	private long timer;
	private int[] delays = {217, 116, 100};
	private float[] sizes = {0, 1.1f, 0.8f, 1.0f};
	private float scale;
	
	public BounceObject(Texture tex, float x, float y) {
		super(tex, x, y);
		timer = -1;
		scale = 0;
	}
	
	public void start() {
		if (timer == -1) timer = System.nanoTime();
	}
	
	public boolean started() {
		return timer != -1;
	}
	
	public void skip() {
		stage = 3;
	}
	
	public void update() {
		if (timer == -1) return;
		long elapsed = (System.nanoTime() - timer) / 1000000;
		switch (stage) {
		case 0:
			if (elapsed > delays[0]) {
				stage++;
				timer = System.nanoTime();
			}
			break;
		case 1:
			if (elapsed > delays[1]) {
				stage++;
				timer = System.nanoTime();
			}
			break;
		case 2:
			if (elapsed > delays[2]) {
				stage++;
				timer = System.nanoTime();
			}
			break;
		case 3:
			break;
		}
	}
	
	public void render() {
		if (stage == 3) scale = 1.0f;
		else {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			float percent = elapsed / (float) delays[stage];
			float diff = sizes[stage + 1] - sizes[stage];
			scale = sizes[stage] + (diff * percent);
			//System.out.println("scale: " + scale);
		}
		drawImage(tex, x - (width / 2 * scale), y - (height / 2 * scale), width * scale, height * scale);
	}
	
}