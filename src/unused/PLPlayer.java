package unused;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import entity.Player;
import entity.util.Animation;

public class PLPlayer extends Player {
	
	// parent and map
	private PLState parent;
	//private PLTileMap tilemap;
	
	// action IDs
	protected static final int IDLE = 0;
	protected static final int WALKING = 1;
	protected static final int JUMPING = 2;
	protected static final int FALLING = 3;
	protected static final int ATTACKING = 4;
	protected static final int BLASTING = 5;
	protected static final int TELEPORTING = 6;
	protected static final int VANISHING = 7;
	
	// action booleans
	protected boolean teleporting;
	protected boolean blasting;
	protected boolean vanshing;
	
	// ability IDs
	public static final int ATTACK = 0;
	public static final int BLAST = 1;
	public static final int TELEPORT = 2;
	public static final int VANISH = 3;
	
	// ABILITY VARIABLES
	// teleport
	protected int teleportDistance;
	protected int maxTeleportDistance;
	protected int teleportSpeed;
	protected boolean teleportRight;
	protected TeleportTrailElement[] trailElements;
	
	// ability selection
	protected int currentAbility;
	
	// ability cooldowns
	protected long attackCooldown;
	protected long blastCooldown;
	protected long teleportCooldown;
	protected long vanishTimer;
	protected long vanishCooldown;
	
	// ability effects
	private boolean invisible;
	
	// transformation
	protected boolean eye_active;
	
	public PLPlayer(PLState parent, PLTileMap tm) {
		// init
		super(tm, 0);
		this.parent = parent;
		//this.tilemap = tm;
		// set sprites
		idle = Textures.y_idle;
		walk = Textures.y_walk;
		jump = Textures.y_jump;
		fall = Textures.y_fall;
		attack = Textures.y_attack;
		// set dimensions
		width = 60;
		height = 80;
		cwidth = 52;
		cheight = 80;
		// set stats
		health = maxHealth = 100;
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
		// animation setup
		animation = new Animation();
		animation.setFrames(idle);
		animation.setDelay(-1);
		attackCooldown = blastCooldown = teleportCooldown = vanishTimer = -1;
		// ability stats
		maxTeleportDistance = 300;
		teleportSpeed = 45;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int) x - cwidth / 2, (int) y - 24, cwidth, 64);
	}
	
	public Rectangle getScreenRectangle() {
		return new Rectangle((int) (x + xmap) - cwidth / 2, (int) (y + ymap) - 24, cwidth, 64);
	}
	
	public boolean eyeActive() { return eye_active; }
	public int getCurrentAbility() { return currentAbility; }
	public boolean invisible() { return invisible; }
	
	protected void getNextPosition() {
		if (teleporting) {
			
		}
		else {
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
		}
		// can't move while attacking
		if ((currentAction == ATTACKING || currentAction == BLASTING) && !(jumping || falling)) dx = 0;
		if (currentAction == TELEPORTING || currentAction == VANISHING) dx = 0;
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
	
	private void setAnimation() {
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
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idle);
				animation.setDelay(-1);
			}
		}
		// update animation
		animation.update();
	}
	
	public void update() {
		if (health <= 0) return;
		// move
		getNextPosition();
		checkCollisions();
		setPosition(xtemp, ytemp);
		if (x < width / 2) x = width / 2;
		if (x > tm.getWidth() - width / 2) x = tm.getWidth() - width / 2;
		// check flinch
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 2000) flinching = false;
		}
		// set animation
		setAnimation();
		// update direction
		if (currentAction != ATTACKING) {
			if (right) facingRight = true;
			if (left) facingRight = false;
		}
		// update abilities
		updateAbilities();
	}
	
	public void render() {
		setMapPosition();
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) return;
		}
		if (currentAction == ATTACKING) {
			if (facingRight) {
				if (invisible) ;
				else drawImage(animation.getCurrentFrame(), x + xmap - 30, y + ymap - height / 2, animation.getCurrentFrame().getImageWidth(), height);
			}
			else {
				if (invisible) ;
				else drawImage(animation.getCurrentFrame(), x + xmap + (width - 30), y + ymap - height / 2, -animation.getCurrentFrame().getImageWidth(), height);
			}
		}
		else {
			if (teleporting) {
				for (int i = 0; i < trailElements.length; i++) {
					if (trailElements[i] == null) break;
					trailElements[i].render();
				}
			}
			if (facingRight) {
				if (invisible) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height, 0.4f);
				else drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
			}
			else {
				if (invisible) drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height, 0.4f);
				else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height);
			}
		}
	}
	
	// RECIEVING ACTIONS
	// transform to 501
	public void transform() {
		if (eye_active) {
			eye_active = false;
			idle = Textures.y_idle;
			walk = Textures.y_walk;
			jump = Textures.y_jump;
			fall = Textures.y_fall;
			attack = Textures.y_attack;
			currentAction = -1;
			setAnimation();
		}
		else {
			eye_active = true;
			//idle = Textures.plYuri[0];
			//walk = Textures.plYuri[1];
			//jump = Textures.plYuri[2];
			//fall = Textures.plYuri[3];
			currentAction = -1;
			setAnimation();
		}
	}
	
	// ABILITIES
	public void useAbility() {
		switch (currentAbility) {
			case ATTACK:
				
				break;
			case BLAST:
				
				break;
			case TELEPORT:
				teleport();
				break;
			case VANISH:
				vanish();
				break;
		}
	}
	
	public void setAbility(int ability) {
		currentAbility = ability;
	}
	
	public float getBlastCooldown() {
		if (blastCooldown == -1) return 0;
		else {
			long elapsed = (System.nanoTime() - blastCooldown) / 1000000;
			return elapsed / 3000.0f;
		}
	}
	
	public float getTeleportCooldown() {
		if (teleportCooldown == -1) return 0;
		else {
			long elapsed = (System.nanoTime() - teleportCooldown) / 1000000;
			return 1.0f - (elapsed / 10000.0f);
		}
	}
	
	public float getVanishCooldown() {
		if (vanishTimer == -1) {
			long elapsed = (System.nanoTime() - vanishCooldown) / 1000000;
			return 1.0f - (elapsed / 20000.0f);
		}
		else if (invisible) {
			long elapsed = (System.nanoTime() - vanishTimer) / 1000000;
			return 1.0f - (elapsed / 5000.0f);
		}
		else return 0;
	}
	
	private void updateAbilities() {
		// check ability status
		if (teleporting) {
			int prevX = (int) x;
			if (teleportRight) x += teleportSpeed;
			else x -= teleportSpeed;
			currCol = (int) x / tileSize;
			trailElements[teleportDistance / teleportSpeed] = new TeleportTrailElement(prevX, (int) y);
			teleportDistance += teleportSpeed;
			// check collision
			checkTileCollision(x, y);
			if (teleportRight) {
				if (topRight || bottomRight) {
					x = (currCol + 1) * tileSize - cwidth / 2;
					teleporting = false;
					teleportDistance = 0;
				}
			}
			else {
				if (topLeft || bottomLeft) {
					x = currCol * tileSize + cwidth / 2;
					teleporting = false;
					teleportDistance = 0;
				}
			}
			// if no collision, check if max distance reached
			if (teleporting) {
				if (teleportDistance >= maxTeleportDistance) {
					teleporting = false;
					teleportDistance = 0;
				}
			}
		}
		// check if cooldowns are complete
		long elapsed;
		if (attackCooldown != -1) {
			elapsed = (System.nanoTime() - attackCooldown) / 1000000;
			if (elapsed > 500) attackCooldown = -1;
		}
		if (blastCooldown != -1) {
			elapsed = (System.nanoTime() - blastCooldown) / 1000000;
			if (elapsed > 3000) blastCooldown = -1;
		}
		if (teleportCooldown != -1) {
			elapsed = (System.nanoTime() - teleportCooldown) / 1000000;
			if (elapsed > 10000) teleportCooldown = -1;
		}
		if (vanishTimer != -1) {
			elapsed = (System.nanoTime() - vanishTimer) / 1000000;
			if (elapsed > 5000) {
				System.out.println("invisibility expired");
				invisible = false;
				vanishCooldown = System.nanoTime();
				vanishTimer = -1;
			}
		}
		if (vanishCooldown != -1) {
			elapsed = (System.nanoTime() - vanishCooldown) / 1000000;
			if (elapsed > 20000) {
				vanishCooldown = -1;
			}
		}
	}
	
	private void teleport() {
		if (teleportCooldown == -1) {
			System.out.println("teleport");
			// calculate destination
			/* int xDest = 0;
			if (facingRight) xDest = (int) x + 300;
			else xDest = (int) x - 300;
			// check if it's clear
			checkTileCollision(xDest, y);
			if (bottomLeft || bottomRight || topLeft || topRight) return; */
			// if clear, teleport
			teleporting = true;
			teleportRight = facingRight;
			// set up trail
			trailElements = new TeleportTrailElement[maxTeleportDistance / teleportSpeed + 1];
			teleportCooldown = System.nanoTime();
			parent.playEffect(EffectsManager.TELEPORT);
		}
	}
	
	private void vanish() {
		if (vanishTimer == -1) {
			System.out.println("vanished");
			invisible = true;
			vanishTimer = System.nanoTime();
		}
	}
	
	
	private class TeleportTrailElement {
		
		// location
		private int x;
		private int y;
		
		// texture
		private Texture tex;
		
		public TeleportTrailElement(int x, int y) {
			this.x = x;
			this.y = y;
			//tex = Textures.teleportTrail;
		}
		
		public void render() {
			drawImage(tex, x + xmap - tex.getImageWidth() / 2, y + ymap - tex.getImageHeight() / 2);
		}
	}
}