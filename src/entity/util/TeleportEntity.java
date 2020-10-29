package entity.util;

import java.awt.Rectangle;

public interface TeleportEntity {
	
	public void teleport(float x, float y);
	public Rectangle getNullZone();
	
}