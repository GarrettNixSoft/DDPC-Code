package entity.util;

import java.awt.Rectangle;

public interface Interactions {
	
	// entities that can interact with an
	// Interactive must be able to react to
	// those interactions
	public abstract void react(Interactive i);
	
	// entity-based methods
	public Rectangle getRectangle();
	
}