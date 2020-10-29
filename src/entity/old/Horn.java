package entity.old;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.enemy.Enemy;
import tile.TileMap;

public class Horn extends Enemy {
	
	private Texture tex;
	
	public Horn(TileMap tm, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		width = height = 60;
		tex = Textures.horn;
	}
	
	public void update() {
		return;
	}
	
	public void render() {
		setMapPosition();
		drawImage(tex, x + xmap - width / 2, y + ymap - height / 2);
	}
	
	public void damage(int damage) {
		return;
	}
	
	public boolean remove() {
		return false;
	}
}