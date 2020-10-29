package entity.enemy;

import static main.Render.drawRect;
import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.util.Animation;
import tile.TileMap;

public class Climber extends Enemy {
	
	// sprites
	private Texture[] sprites;
	private Texture[] deathSprites;
	private int type;
	
	// movement
	private int ymin;
	private int ymax;
	
	// states
	private boolean fall;
	private boolean landed;
	private boolean down;
	
	// flinching
	private long flinchTimer;
	
	public Climber(TileMap tm, int x, int y, int type) {
		super(tm);
		this.x = x;
		this.y = y;
		this.type = type;
		ymin = y;
		ymax = y + 185;
		int offset = (int) (Math.random() * 180);
		this.y += offset;
		health = 5;
		moveSpeed = 1;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		down = true;
		dy = moveSpeed;
		width = height = 64;
		cwidth = cheight = 50;
		value = 500;
		// set up animation
		animation = new Animation();
		switch (type) {
			case 0:
				sprites = Textures.spider_2;
				deathSprites = Textures.spider_2_death;
				break;
			case 1:
				sprites = Textures.spider_2_glitch;
				deathSprites = null;
				break;
		}
		animation.setFrames(sprites);
		animation.setDelay(200);
	}
	
	public boolean isFalling() { return fall & !landed; }
	public boolean landed() { return landed; }
	public int getHealth() { return health; }
	
	public Rectangle getWebRect() {
		return new Rectangle((int) x - 2, ymin, 4, (int) (y - ymin));
	}
	
	public void fall() {
		fall = falling = true;
	}
	
	private void checkFloor() {
		checkTileCollision(x, y + 1);
		if (down && (bottomLeft || bottomRight)) down = false;
	}
	
	public void update() {
		animation.update();
		if (fall && !landed) {
			dy += fallSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
			System.out.println("dy is now: " + dy);
			ydest = y + dy;
			checkTileCollision(x, ydest);
			if (bottomLeft || bottomRight) landed = true;
			else y += dy;
		}
		else {
			checkFloor();
			if (health <= 0) return;
			if (down) {
				y += dy;
				if (y > ymax) {
					y = ymax;
					down = false;
				}
			}
			else {
				y -= dy;
				if (y < ymin) {
					y = ymin;
					down = true;
				}
			}
		}
	}
	
	public void render() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) flinching = false;
			if (elapsed / 100 % 2 == 0) return;
		}
		setMapPosition();
		if (fall) {
			drawImage(sprites[0], x + xmap - width / 2, y + ymap - height / 2, width, height);
		}
		else {
			if (health <= 0) drawRect(x + xmap - 2, ymin + ymap - 40, 4, (y - ymin + 50), Color.white);
			else drawRect(x + xmap - 2, ymin + ymap - 40, 4, (y - ymin + 40), Color.white);
			//System.out.println("web is " + (y - ymin) + "px long");
			drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
		}
	}
	
	public void damage(int damage) {
		if (flinching || health <= 0) return;
		health -= damage;
		if (health <= 0) {
			if (type == 1) return;
			animation.setFrames(deathSprites);
			animation.setDelay(1000);
		}
		else {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	public boolean remove() {
		if (type == 1) return health <= 0;
		if (health <= 0) return animation.hasPlayedOnce();
		if (fall) return y > tm.getHeight() + 100;
		else return fall && landed;
	}
}