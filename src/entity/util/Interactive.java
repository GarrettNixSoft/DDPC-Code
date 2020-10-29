package entity.util;

import java.awt.Rectangle;

public interface Interactive {
	
	public Rectangle getRectangle();
	public void update();
	public void render();
	public void interact(Interactions e);
	public boolean remove();
	
}