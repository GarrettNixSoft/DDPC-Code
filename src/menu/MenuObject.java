package menu;

import org.newdawn.slick.opengl.Texture;

public abstract class MenuObject {
	
	protected Texture tex;
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	
	public MenuObject(Texture tex, float x, float y) {
		this.tex = tex;
		this.x = x;
		this.y = y;
		this.width = tex.getImageWidth();
		this.height = tex.getImageHeight();
	}
	
	public abstract void update();
	public abstract void render();
	
}