package effects;

import static main.Render.drawRect;

import java.awt.Rectangle;

import org.newdawn.slick.Color;

import assets.Sfx;

public class GlitchEffect extends VisualEffect {

	// location
	private int xCenter;
	private int yCenter;
	private int xRange;
	private int yRange;
	
	// update
	private int ticks;
	private int duration;
	private long timer;
	private boolean stopped;
	
	// elements
	private Rectangle[] glitchRects;
	
	public GlitchEffect(int xCenter, int yCenter, int xRange, int yRange, int duration) {
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.xRange = xRange;
		this.yRange = yRange;
		this.duration = duration;
		timer = System.nanoTime();
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > duration && !stopped) {
			stopped = true;
			Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
			return;
		}
		ticks++;
		if (ticks % 2 == 0) return;
		int len = 2 + (int) (Math.random() * 10);
		glitchRects = new Rectangle[len];
		for (int i = 0; i < len; i++) {
			int xPos = -xRange + (int) (Math.random() * (xRange * 2));
			int yPos = -yRange + (int) (Math.random() * (yRange * 2));
			int width = 10 + (int) (Math.random() * 40);
			int height = 2 + (int) (Math.random() * 5);
			glitchRects[i] = new Rectangle(xCenter + xPos - width / 2, yCenter + yPos - height / 2, width, height);
		}
	}
	
	public void render() {
		if (stopped) return;
		for (int i = 0; i < glitchRects.length; i++) {
			Rectangle r = glitchRects[i];
			drawRect(r, Color.black);
		}
	}
}