package effects;

import static main.Render.drawRect;
import static main.Render.drawImage;
import static main.Render.drawOutline;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;

public class LevelS4Effect extends VisualEffect {
	
	// timing
	private long glitchTimer;
	private long glitchStart;
	private int glitchDuration;
	private int glitchWait;
	private boolean inProgress;
	private int ticks;
	private boolean first;
	
	// elements
	private Texture thumbnail;
	private Rectangle[] glitchRects;
	
	public LevelS4Effect() {
		// init texture
		thumbnail = Textures.s4_dark;
		glitchRects = new Rectangle[0];
		// set timer
		first = true;
		setNextGlitch();
	}
	
	private void setNextGlitch() {
		glitchTimer = System.nanoTime();
		glitchDuration = 150 + (int) (Math.random() * 300);
		if (first) {
			glitchWait = 500;
			first = false;
		}
		else glitchWait = 2000 + (int) (Math.random() * 6000);
		System.out.println("time to next: " + glitchWait + "ms");
		System.out.println("it will last: " + glitchDuration + "ms");
	}
	
	public void update() {
		if (!inProgress) {
			long elapsed = (System.nanoTime() - glitchTimer) / 1000000;
			if (elapsed > glitchWait) { // start glitch
				glitchStart = System.nanoTime();
				inProgress = true;
				Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
			}
		}
		else {
			long elapsed = (System.nanoTime() - glitchStart) / 1000000;
			if (elapsed > glitchDuration) { // end glitch
				inProgress = false;
				ticks = 0;
				Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
				setNextGlitch();
			}
			else { // update glitch
				ticks++;
				if (ticks % 2 == 0) return;
				int len = 2 + (int) (Math.random() * 10);
				glitchRects = new Rectangle[len];
				int xCenter = 670;
				int yCenter = 145;
				for (int i = 0; i < len; i++) {
					int xPos = -65 + (int) (Math.random() * 130);
					int yPos = -45 + (int) (Math.random() * 90);
					int width = 10 + (int) (Math.random() * 40);
					int height = 2 + (int) (Math.random() * 5);
					glitchRects[i] = new Rectangle(xCenter + xPos - width / 2, yCenter + yPos - height / 2, width, height);
				}
			}
		}
	}
	
	public void render() {
		if (inProgress) {
			// draw texture
			drawImage(thumbnail, 610, 105);
			drawOutline(610, 105, 120, 80, Color.black);
			// draw glitch rectangles
			for (int i = 0; i < glitchRects.length; i++) {
				Rectangle r = glitchRects[i];
				drawRect(r, Color.black);
			}
		}
	}
}