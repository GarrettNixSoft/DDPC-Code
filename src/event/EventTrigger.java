package event;

import java.awt.Rectangle;

public class EventTrigger {
	
	// type
	public static final int SINGLE = 0;
	public static final int RECURRING = 1;
	private int type;
	
	// trigger area
	private Rectangle area;
	private int x;
	private int y;
	
	// construct with a rectangle
	public EventTrigger(Rectangle area, int x, int y) {
		this.area = area;
		this.x = x;
		this.y = y;
	}
	
	// construct with coordinates
	public EventTrigger(int x, int y, int width, int height, int lx, int ly) {
		this.area = new Rectangle(x, y, width, height);
		this.x = lx;
		this.y = ly;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	// get area
	public Rectangle getArea() { return area; }
	public int[] getLocation() { return new int[] {x, y}; }
	
}