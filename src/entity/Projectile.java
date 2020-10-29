package entity;

import org.newdawn.slick.opengl.Texture;

import tile.DynamicTilemap;

public abstract class Projectile extends Entity {
	
	// directions
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	// souce IDs
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	public static final int OTHER = 2;
	public static final int OTHER_PASSIVE = 3;
	// (OTHER = damages either, OTHER_PASSIVE = damages none)
	
	// origin
	protected int sourceID;
	
	// damage
	protected int damage;
	
	// tile behavior
	protected boolean tileCollisions;
	private boolean hit;
	
	// sprites
	protected Texture[] sprites;
	protected Texture[] hitSprites;
	protected int spriteSpeed;
	protected int hitSpriteSpeed;
	
	// construct
	public Projectile(DynamicTilemap tm, int sourceID) {
		super(tm);
		this.sourceID = sourceID;
	}
	
	// set parameters
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setTileCollisions(boolean tileCollisions) {
		this.tileCollisions = tileCollisions;
	}
	
	public void setDirection(int direction) {
		switch(direction) {
		case UP:
			up = true;
			break;
		case DOWN:
			down = true;
			break;
		case LEFT:
			left = true;
			break;
		case RIGHT:
			right = true;
			break;
		}
	}
	
	// get parameters
	public int getSourceID() { return sourceID; }
	public int getDamage() { return damage; }
	public boolean tileCollisions() { return tileCollisions; }
	public boolean hit() { return hit; }
	
	// receiving actions
	public void setHit() {
		hit = true;
		dx = dy = 0;
		animation.setFrames(hitSprites);
		animation.setDelay(hitSpriteSpeed);
	}
	
	// update
	public void checkTileCollision() {
		if (!tileCollisions) return;
		float xdest = x + dx;
		float ydest = y + dy;
		checkTileCollision(xdest, ydest);
		if (bottomLeft || bottomRight || topLeft || topRight) {
			setHit();
		}
		else {
			x = xdest;
			y = ydest;
		}
	}
	
	public abstract void update();
	
	// render
	public abstract void render();
	
	// removal
	public abstract boolean remove();
	
}