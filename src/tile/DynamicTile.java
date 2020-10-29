package tile;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import entity.util.Animation;

public abstract class DynamicTile {
	
	// texture(s)
	protected Texture[] tex;
	protected Animation animation;
	
	// attributes
	protected boolean blocked;
	protected int tileID;
	
	// tileIDs
	public static final int STATIC = 0;
	public static final int SWITCH = 1;
	public static final int BOUNCE = 2;
	
	public void setAnimationDelay(int delay) {
		animation.setDelay(delay);
	}
	
	public boolean isBlocked() { return blocked; }
	
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	public abstract void update();
	
	public Texture getTex() {
		return animation.getCurrentFrame();
	}
	
	public void render(float x, float y) {
		drawImage(getTex(), x, y, 60, 60);
	}
	
}