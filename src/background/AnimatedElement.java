package background;

import org.newdawn.slick.opengl.Texture;

import entity.util.Animation;
import tile.DynamicTilemap;

import static main.Render.drawImage;

public class AnimatedElement {
	
	// animation
	protected Animation animation;
	
	// position and motion
	protected float x;
	protected float y;
	protected float startX;
	protected float startY;
	protected int width;
	protected int height;
	protected float xms;
	protected float yms;
	
	// map for position reference
	private DynamicTilemap tm;
	
	// regular
	public AnimatedElement(DynamicTilemap tm, Texture[] t, int x, int y) {
		this.tm = tm;
		animation = new Animation();
		animation.setFrames(t);
		this.x = startX = x;
		this.y = startY = y;
		this.width = t[0].getImageWidth();
		this.height = t[0].getImageHeight();
		this.xms = 0;
		this.yms = 0;
	}
	
	// partly overloaded
	public AnimatedElement(DynamicTilemap tm, Texture[] t, int x, int y, float xms, float yms) {
		this.tm = tm;
		animation = new Animation();
		animation.setFrames(t);
		this.x = startX = x;
		this.y = startY = y;
		this.width = t[0].getImageWidth();
		this.height = t[0].getImageHeight();
		this.xms = xms;
		this.yms = yms;
	}
	
	// fully overloaded
	public AnimatedElement(DynamicTilemap tm, Texture[] t, int x, int y, int width, int height, float xms, float yms) {
		this.tm = tm;
		animation = new Animation();
		animation.setFrames(t);
		this.x = startX = x;
		this.y = startY = y;
		this.width = width;
		this.height = height;
		this.xms = xms;
		this.yms = yms;
	}
	
	// setters
	public void setAnimationDelay(int delay) {
		animation.setDelay(delay);
	}
	
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public void setXms(float xms) { this.xms = xms; }
	public void setYms(float yms) { this.yms = yms; }
	
	public void update() {
		// update animation
		animation.update();
		// update position
		x = startX + (tm.getX() * xms);
		y = startY + (tm.getY() * yms);
	}

	public void render() {
		drawImage(animation.getCurrentFrame(), x - width / 2, y - height / 2, width, height);
	}
	
}