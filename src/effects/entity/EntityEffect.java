package effects.entity;

public abstract class EntityEffect {
	
	protected long timer;
	protected boolean active;
	
	protected EntityEffect syncEffect;
	protected boolean synchronize;
	
	public void activate() { active = true; }
	public void deactivate() { active = false; }
	public boolean isActive() { return active; }
	
	public void synchronize(EntityEffect e) {
		syncEffect = e;
		synchronize = true;
	}
	
	public void desync() {
		syncEffect = null;
		synchronize = false;
	}
	
	public abstract void update();
	
}