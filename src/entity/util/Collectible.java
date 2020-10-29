package entity.util;

import java.awt.Rectangle;

public interface Collectible {
	
	public static final int HEAL = 0;
	public static final int SHARD = 1;
	public static final int TOKEN = 2;
	
	public void update();
	public void render();
	public Rectangle getRectangle();
	public void setRemove();
	public boolean remove();
	
}