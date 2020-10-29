package entity;

import static main.Render.drawImage;

import assets.Sfx;
import assets.Textures;
import entity.util.Animation;
import util.data.DataCache;
import util.data.LevelCache;

public class PlayerV2 extends PlayerEnhanced {
	
	// ABILITIES
	// ranged
	private int rangedDamage;
	
	public PlayerV2(LevelCache lc) {
		super(lc);
		// sprites
		idle = Textures.y_idle;
		walk = Textures.y_walk;
		jump = Textures.y_jump;
		fall = Textures.y_fall;
		attack = Textures.y_attack;
		blast = Textures.y_attack;
		glitch_tex = Textures.y_glitch;
		// ranged
		rangedDamage = 3;
		// animation
		animation = new Animation();
		animation.setFrames(idle);
		animation.setDelay(-1);
	}
	
	public void lungeAttack() {
		
	}
	
	public void rangeAttack() {
		long elapsed = (System.nanoTime() - blastStart) / 1000000;
		if (elapsed < 1500) return;
		blasting = true;
		blastStart = System.nanoTime();
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
		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attack);
				animation.setDelay(150);
				Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.ATTACK);
			}
			else {
				if (animation.hasPlayedOnce()) {
					attacking = attackCooldown = false;
				}
			}
		}
		else if (blasting) {
			if (currentAction != RANGED) {
				currentAction = RANGED;
				animation.setFrames(blast);
				animation.setDelay(200);
				/// sfx
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
		// temporary effects
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
	
	public void render() {
		setMapPosition();
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) return;
		}
		if (currentAction == ATTACKING || currentAction == LUNGING || currentAction == RANGED) {
			if (facingRight) drawImage(animation.getCurrentFrame(), x + xmap - 30, y + ymap - height / 2, animation.getCurrentFrame().getImageWidth(), height);
			else drawImage(animation.getCurrentFrame(), x + xmap + (width - 30), y + ymap - height / 2, -animation.getCurrentFrame().getImageWidth(), height);
		}
		else {
			if (facingRight) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
			else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height);
		}
	}
	
	public void heal() {
		health++;
		if (health > maxHealth) health = maxHealth;
		Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.HEAL);
	}
	
	public void score(int points) {
		System.out.println("[Player] scored " + points + " points");
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
	
	// GETTERS
	public int getRangedDamage() { return rangedDamage; }
	
}