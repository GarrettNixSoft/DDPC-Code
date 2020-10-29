package entity.enemy;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.util.Animation;
import tile.TileMap;

public class Spider extends Enemy {
	
	// sprites
	private Texture[] sprites;
	private Texture[] deathSprites;
	private int type;
	
	// movement
	private int xmin;
	private int xmax;
	
	// flinching
	private long flinchTimer;
	
	public Spider(TileMap tm, int x, int y, int type) {
		super(tm);
		this.x = x;
		this.y = y;
		this.type = type;
		xmin = x - 120;
		xmax = x + 120;
		health = 5;
		moveSpeed = 2;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		right = false;
		dx = -moveSpeed;
		width = 60;
		height = 45;
		cwidth = 50;
		cheight = 40;
		value = 200;
		// set up animation
		animation = new Animation();
		switch (type) {
			case 0:
				sprites = Textures.spider_1;
				deathSprites = Textures.spider_1_death;
				break;
			case 1:
				sprites = Textures.spider_1_glitch;
				deathSprites = null;
				break;
		}
		animation.setFrames(sprites);
		animation.setDelay(150);
	}
	
	// used to give spiders remaining health of climbers on spawn
	public Spider(TileMap tm, int x, int y, int health, int type) {
		super(tm);
		this.x = x;
		this.y = y;
		this.type = type;
		xmin = x - 120;
		xmax = x + 120;
		this.health = health;
		moveSpeed = 2;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		right = false;
		dx = -moveSpeed;
		width = 60;
		height = 45;
		cwidth = 50;
		cheight = 40;
		value = 500;
		// set up animation
		animation = new Animation();
		switch (type) {
			case 0:
				sprites = Textures.spider_1;
				deathSprites = Textures.spider_1_death;
				break;
			case 1:
				sprites = Textures.spider_1_glitch;
				deathSprites = null;
				break;
		}
		animation.setFrames(sprites);
		animation.setDelay(150);
	}
	
	private void getNextPosition() {
		if (right) {
			if (x > xmax) {
				x = xmax;
				dx = -moveSpeed;
				right = false;
			}
		}
		else {
			if (x < xmin) {
				x = xmin;
				dx = moveSpeed;
				right = true;
			}
		}
		if (falling) {
			dy += fallSpeed;
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	private void checkTurn() {
		checkTileCollision(x, y + 1);
		if (dx > 0) {
			if ((bottomLeft && !bottomRight) || (x >= tm.getWidth() - width / 2)) {
				right = false;
				dx = -moveSpeed;
			}
		}
		else {
			if (bottomRight && !bottomLeft) {
				right = true;
				dx = moveSpeed;
			}
		}
	}
	
	public void update() {
		animation.update();
		if (health <= 0) return;
		getNextPosition();
		checkCollisions();
		setPosition(xtemp, ytemp);
		checkTurn();
	}
	
	public void render() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) flinching = false;
			if (elapsed / 100 % 2 == 0) return;
		}
		setMapPosition();
		if (notOnScreen()) return;
		if (right) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
		else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height);
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
		return health <= 0 && animation.hasPlayedOnce();
	}
}