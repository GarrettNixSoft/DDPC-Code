package cutscene;

import java.awt.Rectangle;

import entity.Entity;
import entity.PlayerEnhanced;
import util.data.DataCache;

public class Trigger {
	
	// trigger
	private boolean active;
	private int type;
	
	// types
	public static final int TIME = 0;
	public static final int DELAY = 1;
	public static final int AREA = 2;
	public static final int KILL = 3;
	
	// time and delay
	private long timer;
	private int delay;
	private int dependency;
	
	// area
	private Rectangle area;
	private PlayerEnhanced player;
	
	// kill
	private String entityID;
	private Entity entity;
	
	// CONSTRUCTORS
	// time
	public Trigger(int delay) {
		this.delay = delay;
		this.timer = System.nanoTime();
		this.dependency = -1;
		this.area = null;
		this.player = null;
		this.entityID = null;
		type = TIME;
	}
	// delay
	public Trigger(int delay, int dependency) {
		this.delay = delay;
		this.timer = -1;
		this.dependency = dependency;
		this.area = null;
		this.player = null;
		this.entityID = null;
		type = DELAY;
	}
	// area
	public Trigger(Rectangle area, PlayerEnhanced player) {
		this.delay = -1;
		this.timer = -1;
		this.dependency = -1;
		this.area = area;
		this.player = player;
		this.entityID = null;
		type = AREA;
	}
	// kill
	public Trigger(String entityID) {
		this.delay = -1;
		this.timer = -1;
		this.dependency = -1;
		this.area = null;
		this.player = null;
		this.entityID = entityID;
		type = KILL;
	}
	
	public int getType() { return type; }
	public int getDependency() { return dependency; }
	public String getEntityID() { return entityID; }
	public boolean isActive() { return active; }
	
	// start if it's a delay type
	public void start() {
		if (type == DELAY && timer == -1) timer = System.nanoTime();
	}
	
	// set entity if it's a kill type
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public void update() {
		switch(type) {
			case TIME:
				long elapsed = (System.nanoTime() - timer) / 1000000;
				if (elapsed > delay) active = true;
				break;
			case DELAY:
				if (timer != -1) {
					elapsed = (System.nanoTime() - timer) / 1000000;
					if (elapsed > delay) active = true;
				}
				break;
			case AREA:
				this.player = DataCache.player;
				if (area.contains(player.getRectangle())) active = true;
				break;
			case KILL:
				if (entity.isDead()) active = true;
				break;
		}
	}
	
}