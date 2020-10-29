package gui;

import static main.Render.drawImage;
import static main.Render.drawStringShadowed;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Textures;
import entity.PlayerEnhanced;
import util.data.DataCache;

public class HUD2 {

	private Texture box;
	private Texture[] heart;
	private Texture character;
	private PlayerEnhanced player;
	
	public HUD2(PlayerEnhanced player) {
		box = Textures.box;
		heart = Textures.heart;
		this.player = player;
		character = Textures.faces[2];
	}
	
	public void render() {
		drawImage(box, 0, 0, 80, 80);
		drawImage(character, 40 - character.getImageWidth() / 2, 40 - character.getImageHeight() / 2);
		for (int i = 0; i < player.getMaxHealth(); i++) {
			if (i < player.getHealth()) drawImage(heart[1], 85 + i * 35, 10);
			else drawImage(heart[0], 85 + i * 35, 10);
		}
		drawStringShadowed(DataCache.score, Fonts.MENU_SMALL, Color.white, 90, 45, 2, 2);
	}
	
}