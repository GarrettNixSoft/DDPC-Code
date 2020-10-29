package entity;

import entity.old.HealItem;
import entity.util.Collectible;
import tile.DynamicTilemap;
import static main.Render.drawImageNoBlend;

public class Heal extends HealItem implements Collectible {
	
	// movement
	private long timer;
	
	// removal
	private boolean remove;
	
	public Heal(DynamicTilemap tm, int character, int x, int y) {
		super(tm, character, x, y);
		timer = System.nanoTime();
	}
	
	public void render() {
		setMapPosition();
		if (notOnScreen()) return;
		long elapsed = (System.nanoTime() - timer) / 1000000;
		float yDelta = (float) (10 * Math.sin(elapsed * Math.PI / 1500));
		drawImageNoBlend(animation.getCurrentFrame(), x + xmap - width / 2, y + yDelta + ymap - height / 2);
	}
	
	public void setRemove() {
		remove = true;
	}
	
	public boolean remove() {
		return remove;
	}
}