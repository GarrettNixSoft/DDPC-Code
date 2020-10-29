package menu;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

public class FlyInObject extends MenuObject {
	
	// target
	private float xTarget;
	private float yTarget;
	
	 // timing
	private long timer;
	private int duration;
	
	public FlyInObject(Texture tex, float x, float y, float xTarget, float yTarget, int duration) {
		super(tex, x, y);
		this.xTarget = xTarget;
		this.yTarget = yTarget;
		this.duration = duration;
		timer = -1;
	}
	
	public void start() {
		if (timer != -1) return;
		timer = System.nanoTime();
	}
	
	public void skip() {
		x = xTarget;
		y = yTarget;
	}
	
	public void update() {
		if (timer == -1) return;
	}
	
	public void render() {
		if (timer == -1) return;
		long elapsed = (System.nanoTime() - timer) / 1000000;
		float percent = (elapsed / (float) duration);
		//System.out.println("elapsed = " + elapsed + ", percent = " + percent + "%");
		if (percent >= 100) drawImage(tex, xTarget - tex.getImageWidth() / 2, yTarget - tex.getImageHeight() / 2);
		else {
			float diffX = xTarget - x;
			float diffY = yTarget - y;
			//System.out.println("diffX = " + diffX);
			//System.out.println("% * diffX = " + (diffX * percent));
			float xPos = x + (diffX * percent);
			float yPos = y + (diffY * percent);
			if (x < xTarget && xPos > xTarget) xPos = xTarget;
			if (x > xTarget && xPos < xTarget) xPos = xTarget;
			if (y < yTarget && yPos > yTarget) yPos = yTarget;
			if (y > yTarget && yPos < yTarget) yPos = yTarget;
			drawImage(tex, xPos - tex.getImageWidth() / 2, yPos - tex.getImageHeight() / 2);
		}
	}
}