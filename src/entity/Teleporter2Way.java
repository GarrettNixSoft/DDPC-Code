package entity;

import java.awt.Rectangle;

import assets.Sfx;
import assets.Textures;
import entity.util.Animation;
import entity.util.Interactive;
import main.Render;
import tile.DynamicTilemap;
import util.FadeManager;

public class Teleporter2Way extends Teleporter implements Interactive {
	
	// prevent loops
	private boolean zone2;
	private boolean zone3;
	private boolean prevZone2;
	private boolean prevZone3;
	
	public Teleporter2Way(DynamicTilemap tm, int x1, int y1, int x2, int y2) {
		super(tm, x1, y1, x2, y2);
		zones = new Rectangle[4];
		zones[0] = new Rectangle(x1 - size / 2, y1 - size / 2, size, size);
		zones[1] = new Rectangle(x2 - size / 2, y2 - size / 2, size, size);
		zones[2] = new Rectangle(x1 - 5, y1 - 5, 10, 10);
		zones[3] = new Rectangle(x2 - 5, y2 - 5, 10, 10);
		sprites = Textures.teleport_2;
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(750);
	}
	
	public void interact(Entity e) {
		zone2 = e.getRectangle().intersects(zones[2]);
		zone3 = e.getRectangle().intersects(zones[3]);
		if (zone2) {
			if (prevZone2 || prevZone3) return;
			e.setPosition(x2, y2 - 10);
			FadeManager.fadeIn(1.0f, true);
			tm.setPositionAbsolute(Render.WIDTH / 2 - e.getX(), Render.HEIGHT / 2 - e.getY());
			Sfx.playSound(Sfx.TYPE_ENTITY, Sfx.TELEPORT);
		}
		else if (zone3) {
			if (prevZone2 || prevZone3) return;
			e.setPosition(x1, y1 - 10);
			FadeManager.fadeIn(1.0f, true);
			tm.setPositionAbsolute(Render.WIDTH / 2 - e.getX(), Render.HEIGHT / 2 - e.getY());
			Sfx.playSound(Sfx.TYPE_ENTITY, Sfx.TELEPORT);
		}
		prevZone2 = zone2;
		prevZone3 = zone3;
	}
	
}