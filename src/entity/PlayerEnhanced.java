package entity;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import entity.old.HealItem;
import entity.util.Collectible;
import entity.util.Interactions;
import entity.util.Interactive;
import util.LocationData;
import util.data.DataCache;
import util.data.LevelCache;

public abstract class PlayerEnhanced extends Entity implements Interactions {
	
	// state
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected boolean flinching;
	protected long flinchTimer;
	protected long invincibilityTimer;
	protected int invincibility;
	protected boolean invincible;
	
	// attack
	protected boolean attacking;
	protected int attackRange;
	protected int attackDamage;
	protected long attackStart;
	protected boolean attackCooldown;
	
	// blast
	protected boolean blasting;
	protected int blastRange;
	protected int blastDamage;
	protected long blastStart;
	
	// action IDs
	protected static final int IDLE = 0;
	protected static final int WALKING = 1;
	protected static final int JUMPING = 2;
	protected static final int FALLING = 3;
	protected static final int ATTACKING = 4;
	protected static final int LUNGING = 5;
	protected static final int RANGED = 6;
	// sprites
	protected Texture[] idle;
	protected Texture[] walk;
	protected Texture[] jump;
	protected Texture[] fall;
	protected Texture[] attack;
	protected Texture[] blast;
	
	// glitch effect
	protected Texture[] glitch_tex;
	protected int glitchDuration;
	protected long glitchTimer;
	protected boolean glitch;
	
	// misc
	protected int score;
	
	// flyMode
	protected boolean flyMode;
	
	// parent access
	protected LevelCache lc;
	
	public PlayerEnhanced(LevelCache lc) {
		super(lc.tilemap);
		this.lc = lc;
		// stats
		width = 60;
		height = 80;
		cwidth = 52;
		cheight = 80;
		health = maxHealth = 3;
		moveSpeed = 0.8f;
		maxSpeed = 4f;
		stopSpeed = 0.6f;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		jumpStart = -15f;
		stopJumpSpeed = 0.5f;
		facingRight = true;
		attackRange = 28;
		attackDamage = 5;
	}
	
	public void flyMode() {
		flyMode = !flyMode;
	}
	
	// OVERRIDE for custom collision checking
	protected void checkTileCollision(float x, float y) {
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - 24) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		if (topTile < 0 || bottomTile >= tm.getNumRows() || leftTile < 0 || rightTile >= tm.getNumCols()) {
			if (leftTile >= 0 && rightTile >= tm.getNumCols()) {
				if (bottomTile >= tm.getNumRows()) {
					topLeft = topRight = bottomLeft = bottomRight = false;
					return;
				}
				bottomLeft = tm.isBlocked(bottomTile, leftTile);
				bottomRight = bottomLeft;
				topLeft = tm.isBlocked(topTile, leftTile);
				topRight = topLeft;
			}
			else topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		topLeft = tm.isBlocked(topTile, leftTile);
		topRight = tm.isBlocked(topTile, rightTile);
		bottomLeft = tm.isBlocked(bottomTile, leftTile);
		bottomRight = tm.isBlocked(bottomTile, rightTile);
	}
	
	// attacking
	public Rectangle getAttackRect() {
		if (facingRight) {
			return new Rectangle((int) (x - cwidth / 2), (int) (y - cheight / 2), cwidth + attackRange, cheight);
		}
		else {
			return new Rectangle((int) (x - cwidth / 2 - attackRange), (int) (y - cheight / 2), cwidth + attackRange, cheight);
		}
	}
	
	// attack actions
	public void setAttacking() { // TODO: why isn't attackCooldown true here even though it FREAKING IS
		if (attackCooldown) {
			//System.out.println("[PlayerEnhanced] Attack on cooldown");
			return;
		}
		//System.out.println("[PlayerEnhanced] Set attacking");
		long elapsed = (System.nanoTime() - attackStart) / 1000000;
		if (elapsed < 600) return;
		attacking = true;
		attackStart = System.nanoTime();
		attackCooldown = true;
	}
	
	public abstract void lungeAttack();
	public abstract void rangeAttack();
	
	protected void getNextPosition() {
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) dx = -maxSpeed;
		}
		else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) dx = maxSpeed;
		}
		else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) dx = 0;
			}
			else {
				dx += stopSpeed;
				if (dx > 0) dx = 0;
			}
		}
		// can't move while attacking
		if ((currentAction == ATTACKING || currentAction == RANGED) && !(jumping || falling)) dx = 0;
		// jump
		if (jumping && !falling) {
			dy = jumpStart;
			Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.JUMP);
			if (!flyMode) falling = true;
		}
		else if (jumping && flyMode) {
			falling = false;
		}
		// fall
		if (falling) {
			dy += fallSpeed;
			if (dy > 0) {
				jumping = false;
			}
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	// OVERRIDE for custom collision checking
	protected void checkCollisions() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		//check top or bottom tile collision
		checkTileCollision(x, ydest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + 24;
			}
			else ytemp += dy;
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else ytemp += dy;
		}
		
		// check left or right tile collision
		checkTileCollision(xdest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else xtemp += dx;
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else xtemp += dx;
		}
		
		// check falling
		if (!falling) {
			checkTileCollision(x, ydest + 1);
			if (!(bottomLeft || bottomRight) && !jumping) falling = true;
			//if (falling) System.out.println("fall detected");
		}
	}
	
	public abstract void update();
	public abstract void render();
	
	// getters
	public float getDX() { return dx; }
	public float getDY() { return dy; }
	
	// receiving actions
	public void score(int points) {
		System.out.println("[PlayerEnhanced] Scored " + points + " points!");
		score += points;
		DataCache.score = "" + score;
	}
	
	public void idle() {
		currentAction = IDLE;
		animation.setFrames(idle);
		animation.setDelay(-1);
	}
	
	public void glitch(float duration) {
		glitch = true;
		currentAction = IDLE;
		animation.setFrames(glitch_tex);
		animation.setDelay(75);
		glitchDuration = (int) (duration * 1000);
		glitchTimer = System.nanoTime();
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
		System.out.println("[PlayerEnhanced] Player glitch! [duration=" + duration + "s]");
	}
	
	public void collect(Collectible c) {
		if (c instanceof HealItem) {
			heal();
		}
	}
	
	public void react(Interactive i) {
		// TODO: react to teleporters
	}
	
	// HEALTH
	public abstract boolean damage();
	
	public void kill() {
		health = 0;
		dead = true;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = health = maxHealth;
	}
	
	public void heal() { 
		health++;
		if (health > maxHealth) health = maxHealth;
	}
	
	public void invincible(int time) {
		invincible = true;
		invincibility = time;
		invincibilityTimer = System.nanoTime();
	}
	
	// GETTERS
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getScore() { return score; }
	public int getAttackDamage() { return attackDamage; }
	public boolean isAttacking() { return attacking; }
	public boolean isFlinching() { return flinching; }
	public boolean isDead() { return dead; }
	public boolean attackCooldown() { return attackCooldown; }
	
	public LocationData getLocationData() {
		LocationData data = new LocationData();
		data.x = x;
		data.y = y;
		data.dx = dx;
		data.dy = dy;
		return data;
	}
	
}