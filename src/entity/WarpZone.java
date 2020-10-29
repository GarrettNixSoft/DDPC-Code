package entity;

import java.awt.Rectangle;

public class WarpZone {
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	public WarpZone(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean contains(Rectangle r) {
		return new Rectangle(x, y, width, height).contains(r);
	}
	
}