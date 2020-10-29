package entity;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import entity.util.Animation;
import tile.DynamicTilemap;

public class Character extends Player {
	
	// characters
	public static final int CHR_SAYORI = 0;
	public static final int CHR_NATSUKI = 1;
	public static final int CHR_YURI = 2;
	public static final int CHR_MIRROR = 3;
	
	// glitch effect
	private Texture[] glitch_tex;
	
	// fly mode
	private boolean fly;
	
	// removal
	private boolean remove;
	
	// face camera
	private Texture faceTex;
	private boolean face;
	
	public Character(DynamicTilemap tm, int character) {
		super(tm, character);
		System.out.println("[Character] id=" + character);
		switch (character) {
		case 0:
			glitch_tex = Textures.s_glitch;
			faceTex = Textures.s_face;
			break;
		case 1:
			glitch_tex = Textures.n_glitch;
			faceTex = Textures.n_face;
			break;
		case 2:
			glitch_tex = Textures.y_glitch;
			faceTex = Textures.y_face;
			break;
		case 3:
			glitch_tex = Textures.m_glitch;
			faceTex = Textures.m_face;
			break;
		case 4:
			// override with Mirror's sprites
			System.out.println("[Character] Overriding!");
			idle = Textures.mr_idle;
			walk = Textures.mr_walk;
			jump = Textures.mr_jump;
			fall = Textures.mr_fall;
			attack = Textures.mr_attack;
			glitch_tex = Textures.mr_glitch;
			break;
		}
		animation = new Animation();
		animation.setFrames(idle);
		animation.setDelay(-1);
	}
	
	public void facingRight(boolean b) { this.facingRight = b; }
	public void setFly(boolean fly) { this.fly = fly; }
	public boolean flyMode() { return fly; }
	
	public void face() { face = true; }
	public void unface() { face = false; }
	
	// actions
	@Override
	public void glitch(float duration) {
		glitch = true;
		glitchTimer = System.nanoTime();
		glitchDuration = (int) (duration * 1000);
		left = right = jumping = falling = attacking = false;
		currentAction = GLITCH;
		animation.setFrames(glitch_tex);
		animation.setDelay(75);
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
		System.out.println("[Character] Character glitch! [duration=" + duration + "s]");
	}
	
	// receiving actions
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRemove() {
		remove = true;
	}
	
	// OVERRIDE for custom collision checking
	protected void checkCollisions() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		if (!fly) {
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
				if (falling) System.out.println("fall detected");
			}
		}
		else {
			xtemp = xdest;
			ytemp = ydest;
		}
	}
	
	// removal
	public boolean remove() {
		return remove;
	}
	
	// render
	@Override
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
			if (face) drawImage(faceTex, x + xmap - width / 2, y + ymap - height / 2, width, height);
			else if (facingRight) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
			else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height);
		}
	}
	
}