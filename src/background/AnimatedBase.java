package background;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import main.Render;

public class AnimatedBase {
	
	private Texture[] frames;
	private long timer;
	private int delay;
	private int currentFrame;
	
	public AnimatedBase(Texture[] frames, int delay) {
		this.frames = frames;
		this.timer = System.nanoTime();
		this.delay = delay;
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			if (currentFrame >= frames.length) currentFrame = 0;
			timer = System.nanoTime();
		}
	}
	
	public void render() {
		drawImage(frames[currentFrame], 0, 0, Render.WIDTH, Render.HEIGHT);
	}
	
}