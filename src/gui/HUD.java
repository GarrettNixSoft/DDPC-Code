package gui;

import static main.Render.drawImage;
//import static main.Render.drawString;
import static main.Render.drawStringShadowed;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Textures;
import effects.HUDEffect;
import entity.Player;
import util.data.DataCache;

public class HUD {
	
	private Texture box;
	private Texture[] heart;
	private Texture character;
	private Player player;
	private HUDEffect effect;
	
	private boolean torture_challenge;
	
	public HUD(Player player) {
		box = Textures.box;
		heart = Textures.heart;
		if (player.getCharacter() == 4) {
			character = Textures.faces[0];
			effect = new HUDEffect();
		}
		else character = Textures.faces[player.getCharacter()];
		this.player = player;
	}
	
	public void setTortureChallenge(boolean torture) {
		torture_challenge = torture;
	}
	
	public void render() {
		drawImage(box, 0, 0, 80, 80);
		drawImage(character, 40 - character.getImageWidth() / 2, 40 - character.getImageHeight() / 2);
		if (effect != null) effect.render();
		for (int i = 0; i < player.getMaxHealth(); i++) {
			if (i < player.getHealth()) drawImage(heart[1], 85 + i * 35, 10);
			else drawImage(heart[0], 85 + i * 35, 10);
		}
		if (torture_challenge) drawStringShadowed("Attempts: " + DataCache.attempts_current, Fonts.MENU_SMALL, Color.white, 90, 45, 2, 2);
		else drawStringShadowed(DataCache.score, Fonts.MENU_SMALL, Color.white, 90, 45, 2, 2);
	}
	
}