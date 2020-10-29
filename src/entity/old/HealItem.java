package entity.old;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.Entity;
import entity.util.Animation;
import tile.TileMap;

import static main.Render.drawImage;

public class HealItem extends Entity {
	
	private Texture[] sprites;
	
	public HealItem(TileMap tm, int character, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		setMapPosition();
		// set texture based on character
		switch (character) {
			case 0: // sayori
				sprites = Textures.bottle;
				break;
			case 1: // natsuki
				sprites = Textures.cupcake;
				break;
			case 2: // yuri
				sprites = Textures.tea;
				break;
			case 3: // monika
				sprites = Textures.coffee;
				break;
		}
		width = cwidth = sprites[0].getImageWidth();
		height = cheight = sprites[0].getImageHeight();
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(500);
	}
	
	public void update() {
		animation.update();
	}
	
	public void render() {
		setMapPosition();
		drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
	}
}