package entity.enemy;

import entity.Entity;
import tile.TileMap;

public abstract class Enemy extends Entity {
	
	protected int health;
	protected int damage;
	protected int value;
	protected boolean flinching;
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public int getHealth() { return health; }
	public int getDamage() { return damage; }
	public int getValue() { return value; }
	
	public abstract void update();
	public abstract void render();
	public abstract void damage(int damage);
	public boolean isFlinching() { return flinching; }
	public boolean isDead() { return health <= 0; }
	public abstract boolean remove();
	
}