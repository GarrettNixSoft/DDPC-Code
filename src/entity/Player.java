package entity;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import entity.util.Animation;
import tile.TileMap;
import util.LocationData;
import util.data.DataCache;
import util.input.KeyInput;

public class Player extends Entity {
	
	// character
	protected int character;
	
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
	
	// action IDs
	//protected final int[] numFrames = {1, 4, 1, 1, 1};
	protected static final int IDLE = 0;
	protected static final int WALKING = 1;
	protected static final int JUMPING = 2;
	protected static final int FALLING = 3;
	protected static final int ATTACKING = 4;
	protected static final int GLITCH = 5;
	
	// sprites
	protected Texture[] idle;
	protected Texture[] walk;
	protected Texture[] jump;
	protected Texture[] fall;
	protected Texture[] attack;
	
	// misc
	protected int score;
	
	// expansion
	protected boolean glitch;
	
	// debug
	protected boolean fly;
	
	public Player(TileMap tm, int character) {
		super(tm);
		this.character = character;
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
		switch (character) {
			case 0: // sayori
				idle = Textures.s_idle;
				walk = Textures.s_walk;
				jump = Textures.s_jump;
				fall = Textures.s_fall;
				attack = Textures.s_attack;
				break;
			case 1: // natsuki
				idle = Textures.n_idle;
				walk = Textures.n_walk;
				jump = Textures.n_jump;
				fall = Textures.n_fall;
				attack = Textures.n_attack;
				break;
			case 2: // yuri
				idle = Textures.y_idle;
				walk = Textures.y_walk;
				jump = Textures.y_jump;
				fall = Textures.y_fall;
				attack = Textures.y_attack;
				break;
			case 3: // monika
				idle = Textures.m_idle;
				walk = Textures.m_walk;
				jump = Textures.m_jump;
				fall = Textures.m_fall;
				attack = Textures.m_attack;
				break;
			case 4: // sayori
				idle = Textures.s_idle;
				walk = Textures.s_walk;
				jump = Textures.s_jump;
				fall = Textures.s_fall;
				attack = Textures.s_attack;
				break;
		}
		animation = new Animation();
		animation.setFrames(idle);
		animation.setDelay(-1);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int) x - cwidth / 2, (int) y - 24, cwidth, 64);
	}
	
	public Rectangle getScreenRectangle() {
		return new Rectangle((int) (x + xmap) - cwidth / 2, (int) (y + ymap) - 24, cwidth, 64);
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
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getCharacter() { return character; }
	public int getScore() { return score; }
	public int getAttackDamage() { return attackDamage; }
	public boolean isAttacking() { return attacking; }
	public boolean isFlinching() { return flinching; }
	public boolean isDead() { return dead; }
	public int getScreenX() { return (int) (x + xmap); }
	public int getScreenY() { return (int) (y + ymap); }
	
	// get velocity
	public float getDX() { return dx; }
	public float getDY() { return dy; }
	
	public Rectangle getAttackRect() {
		if (facingRight) {
			return new Rectangle((int) (x + 20), (int) (y - cheight / 2), cwidth / 2 + attackRange, cheight);
		}
		else {
			return new Rectangle((int) (x - cwidth / 2 - attackRange), (int) (y - cheight / 2), cwidth / 2 - 20 + attackRange, cheight);
		}
	}
	
	public Rectangle getAttackRectScreen() {
		if (facingRight) {
			return new Rectangle((int) (x + xmap + 20), (int) (y + ymap - cheight / 2), cwidth / 2 - 20 + attackRange, cheight);
		}
		else {
			return new Rectangle((int) (x + xmap - cwidth / 2 - attackRange), (int) (y + ymap - cheight / 2), cwidth / 2 - 20 + attackRange, cheight);
		}
	}
	
	public void setAttacking() {
		long elapsed = (System.nanoTime() - attackStart) / 1000000;
		if (elapsed < 600) return;
		attacking = true;
		attackStart = System.nanoTime();
	}
	
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
		if (currentAction == ATTACKING && !(jumping || falling)) dx = 0;
		// jump
		if (jumping && !falling) {
			dy = jumpStart;
			Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.JUMP);
			falling = true;
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
	
	public void update() {
		if (health <= 0) return;
		if (fly) {
			if (left) x -= 10;
			if (right) x += 10;
			if (jumping) y -= 10;
			if (KeyInput.isDown(KeyInput.S)) y += 10;
		}
		else {
			// move
			getNextPosition();
			checkCollisions();
			setPosition(xtemp, ytemp);
			if (x < width / 2) x = width / 2;
			if (x > tm.getWidth() - width / 2) x = tm.getWidth() - width / 2;
		}
		// check flinch
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 2000) flinching = false;
		}
		// set animation
		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attack);
				animation.setDelay(150);
				Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.ATTACK);
			}
			else {
				if (animation.hasPlayedOnce()) attacking = false;
			}
		}
		else if (jumping) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jump);
				animation.setDelay(-1);
			}
		}
		else if (falling) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(fall);
				animation.setDelay(-1);
			}
		}
		else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(walk);
				animation.setDelay(200);
			}
		}
		else {
			if (currentAction != IDLE && currentAction != GLITCH) {
				currentAction = IDLE;
				animation.setFrames(idle);
				animation.setDelay(-1);
			}
		}
		// update animation
		animation.update();
		// effects
		if (glitch) {
			long elapsed = (System.nanoTime() - glitchTimer) / 1000000;
			if (elapsed > glitchDuration) {
				glitch = false;
				currentAction = IDLE;
				animation.setFrames(idle);
				animation.setDelay(-1);
				Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
			}
		}
		// update direction
		if (currentAction != ATTACKING) {
			if (right) facingRight = true;
			if (left) facingRight = false;
		}
		// update invincibility
		if (invincible) {
			long elapsed = (System.nanoTime() - invincibilityTimer) / 1000000;
			if (elapsed > invincibility) invincible = false;
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
			if (!(bottomLeft || bottomRight)) falling = true;
		}
	}
	
	public void render() {
		setMapPosition();
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) return;
		}
		if (currentAction == ATTACKING) {
			if (facingRight) drawImage(animation.getCurrentFrame(), x + xmap - 30, y + ymap - height / 2, animation.getCurrentFrame().getImageWidth(), height);
			else drawImage(animation.getCurrentFrame(), x + xmap + (width - 30), y + ymap - height / 2, -animation.getCurrentFrame().getImageWidth(), height);
		}
		else {
			if (facingRight) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
			else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height);
		}
	}
	
	public void score(int points) {
		System.out.println("[Player] Scored " + points + " points!");
		score += points;
		DataCache.score = "" + score;
	}
	
	public void idle() {
		left = right = jumping = falling = attacking = false;
		currentAction = IDLE;
		animation.setFrames(idle);
		animation.setDelay(-1);
	}
	
	public void stop() {
		dx = dy = 0;
	}
	
	// debug
	public void fly(boolean fly) {
		this.fly = fly;
	}
	
	public boolean isFly() {
		return fly;
	}
	
	// HEALTH
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = health = maxHealth;
	}
	
	public void heal() {
		health++;
		if (health > maxHealth) health = maxHealth;
		Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.HEAL);
	}
	
	public void healMax() {
		health = maxHealth;
		//Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.HEAL);
	}
	
	public void invincible(int time) {
		invincible = true;
		invincibility = time;
		invincibilityTimer = System.nanoTime();
	}
	
	public boolean damage() {
		if (flinching || invincible) return false;
		flinching = true;
		flinchTimer = System.nanoTime();
		health--;
		if (health <= 0) { // dead
			dead = true;
			Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.LOSE);
		}
		else { // damage
			Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.HURT);
		}
		return true;
	}
	
	public void kill() {
		health = 0;
		dead = true;
		Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.LOSE);
	}

	public LocationData getLocationData() {
		LocationData data = new LocationData();
		data.x = x;
		data.y = y;
		data.dx = dx;
		data.dy = dy;
		return data;
	}
}