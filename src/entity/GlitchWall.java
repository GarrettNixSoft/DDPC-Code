package entity;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.enemy.Enemy;
import tile.TileMap;

public class GlitchWall extends Enemy {
	
	// texture
	private Texture sprite;
	
	// flinching
	private long flinchTimer;
	
	public GlitchWall(TileMap tm, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		width = cwidth = 60;
		height = cheight = 720;
		health = 30;
		sprite = Textures.wall;
	}
	
	public void update() {
		// nothing
	}
	
	public void render() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 500) flinching = false;
			if (elapsed / 20 % 2 == 0) return;
		}
		setMapPosition();
		drawImage(sprite, x + xmap - width / 2, y + ymap - height / 2, width, height);
	}
	
	public void damage(int damage) {
		if (flinching || health <= 0) return;
		health -= damage;
		if (health <= 0) {
			// dead
		}
		else {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	public boolean remove() {
		return health <= 0;
	}
}