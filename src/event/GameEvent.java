package event;

import util.data.LevelCache;

public abstract class GameEvent {
	
	// game access
	protected LevelCache level;
	
	// trigger
	protected EventTrigger trigger;
	
	// activate
	protected boolean active;
	protected boolean activated;
	
	public GameEvent(EventTrigger trigger, LevelCache level) {
		this.trigger = trigger;
		this.level = level;
	}
	
	public void activate() {
		this.active = true;
		this.activated = true;
	}
	
	public EventTrigger getTrigger() { return trigger; }
	public boolean active() { return active; }
	public boolean complete() {
		if (trigger.getType() == EventTrigger.RECURRING) return false;
		else return activated && !active;
	}
	
	public abstract void update();
	public abstract void render();
	
}