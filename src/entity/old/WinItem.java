package entity.old;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.Entity;
import tile.TileMap;

import static main.Render.drawImage;

public class WinItem extends Entity {
	
	private Texture sprite;
	private long timer;
	private int initialY;
	
	public WinItem(TileMap tm, int character, int x, int y) {
		super(tm);
		this.x = x;
		this.y = initialY = y;
		setMapPosition();
		// select texture based on character
		switch (character) {
			case 0: // sayori
				sprite = Textures.cookie;
				break;
			case 1: // natsuki
				sprite = Textures.cookie;
				break;
			case 2: // yuri
				sprite = Textures.markov;
				break;
			case 3: // monika
				sprite = Textures.pen;
				break;
			case 4: // sayori
				sprite = Textures.cookie;
				break;
		}
		// set timer
		timer = System.nanoTime();
		// set dimensions
		width = cwidth = sprite.getImageWidth();
		height = cheight = sprite.getImageHeight();
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		int hundredths = (int) elapsed / 10;
		y = (float) (10 * Math.sin(hundredths / 35.0)) + initialY;
	}
	
	public void render() {
		setMapPosition();
		if (notOnScreen()) return;
		drawImage(sprite, x + xmap - width / 2, y + ymap - height / 2, width, height);
	}
}