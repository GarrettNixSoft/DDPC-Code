package menu;

import static main.Render.drawImageAR;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import main.Render;
import util.MathUtil;

public class StarsEffect {
	
	// stars
	private Star[] left;
	private Star[] right;
	
	// timing
	private long timer;
	private boolean finished;
	
	public StarsEffect() {
		timer = -1;
		left = new Star[MathUtil.randInt(2, 5)];
		right = new Star[MathUtil.randInt(2, 5)];
		System.out.println("[StarsEffect] left: " + left.length + ", right: " + right.length);
		for (int i = 0; i < left.length; i++) {
			left[i] = new Star(false);
		}
		for (int i = 0; i < right.length; i++) {
			right[i] = new Star(true);
		}
	}
	
	public void start() {
		timer = System.nanoTime();
		for (Star s : left) s.start();
		for (Star s : right) s.start();
	}
	
	public void update() {
		if (timer == -1) return;
		//System.out.println("stars updated");
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > 1500) {
			//finished = true;
			return;
		}
		for (Star s : left) s.update();
		for (Star s : right) s.update();
	}
	
	public void render() {
		if (timer == -1 || finished) return;
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed < 800) {
			float alpha = (float) ((800 - elapsed) / 800.0);
			alpha *= 0.7f;
			//System.out.println("alpha = " + alpha);
			Render.fillScreen(new Color(255, 255, 255, (int) (255 * alpha)));
		}
		for (Star s : left) s.render();
		for (Star s : right) s.render();
		//System.out.println("********************************");
	}
	
	private class Star {
		// movement
		private float dx;
		private float dy;
		private float da;
		// location
		private float x;
		private float y;
		private float angle;
		// timing
		private long timer;
		// texture
		private Texture tex;
		
		public Star(boolean right) {
			this.tex = Textures.star;
			timer = -1;
			x = MathUtil.randFloat(385, 415);
			y = MathUtil.randFloat(135, 165);
			dx = MathUtil.randFloat(0.3, 6);
			dy = MathUtil.randFloat(-12, -0.5);
			da = MathUtil.randFloat(-8, 8);
			if (!right) dx = -dx;
		}
		
		public void start() {
			timer = System.nanoTime();
		}
		
		public void update() {
			if (timer == -1) return;
			x += dx;
			y += dy;
			dy += 0.4;
			angle += da;
		}
		
		public void render() {
			if (timer == -1) return;
			//System.out.println("render star");
			long elapsed = (System.nanoTime() - timer) / 1000000;
			float alpha = (float) ((1500 - elapsed) / 1500.0);
			alpha *= 0.75f;
			drawImageAR(tex, x - tex.getImageWidth() / 2, y - tex.getImageHeight() / 2, tex.getImageWidth(), tex.getImageHeight(), alpha, angle);
		}
	}
	
}