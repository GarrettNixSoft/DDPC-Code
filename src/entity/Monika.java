package entity;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import tile.TileMap;

public class Monika extends Entity {
	
	private Texture sprite;
	
	public Monika(TileMap tm, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		sprite = Textures.m_face;
	}
	
	public void render() {
		setMapPosition();
		drawImage(sprite, x + xmap - sprite.getImageWidth() / 2, y + ymap - sprite.getImageHeight() / 2);
	}
	@Override
	public void update() {
		// doesn't move...
	}
	
}