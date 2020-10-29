package entity.enemy;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.util.Animation;
import tile.TileMap;

import static main.Render.drawImage;

public class RainCloud extends Enemy {
	
	// sprites
	private Texture[] sprites;
	private Texture[] deathSprites;
	private boolean glitch;
	
	// movement
	private int xmin;
	private int xmax;
	
	// flinching
	private long flinchTimer;
	
	public RainCloud(TileMap tm, int tier, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		xmin = x - 160;
		xmax = x + 160;
		moveSpeed = 3;
		right = true;
		width = height = 60;
		cwidth = cheight = 50;
		animation = new Animation();
		switch (tier) {
			case 0:
				sprites = Textures.raincloud_1;
				deathSprites = Textures.raincloud_1_death;
				health = 5;
				value = 200;
				break;
			case 1:
				sprites = Textures.raincloud_2;
				deathSprites = Textures.raincloud_2_death;
				health = 8;
				value = 500;
				break;
			case 2:
				glitch = true;
				sprites = Textures.raincloud_1_glitch;
				deathSprites = null;
				health = 5;
				value = 200;
				break;
			case 3:
				glitch = true;
				sprites = Textures.raincloud_2_glitch;
				deathSprites = null;
				health = 8;
				value = 500;
				break;
		}
		animation.setFrames(sprites);
		animation.setDelay(200);
	}
	
	public void update() {
		animation.update();
		if (health <= 0) return;
		if (right) {
			x += moveSpeed;
			if (x > xmax) {
				x = xmax;
				right = false;
			}
		}
		else {
			x -= moveSpeed;
			if (x < xmin) {
				x = xmin;
				right = true;
			}
		}
	}
	
	public void render() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 2000) flinching = false;
			if (elapsed / 100 % 2 == 0) return;
		}
		setMapPosition();
		if (notOnScreen()) return;
		drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
	}
	
	public void damage(int damage) {
		if (flinching || health <= 0) return;
		health -= damage;
		if (health <= 0) {
			if (glitch) return;
			animation.setFrames(deathSprites);
			animation.setDelay(25);
		}
		else {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	public boolean remove() {
		if (glitch) return health <= 0;
		else return health <= 0 && animation.hasPlayedOnce();
	}
}