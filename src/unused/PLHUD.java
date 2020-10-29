package unused;

import static main.Render.drawImage;
import static main.Render.drawRect;
import static main.Render.drawString;
import static main.Render.drawOutline;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Textures;
import effects.HUDEffect;

public class PLHUD {
	
	private Texture box;
	private Texture heart;
	//private Texture character;
	private PLPlayer player;
	private HUDEffect effect;
	private Color redShade;
	private Color greenShade;
	
	public PLHUD(PLPlayer player) {
		box = Textures.box;
		heart = Textures.heart[1];
		this.player = player;
		redShade = new Color(1.0f, 0f, 0f, 0.4f);
		greenShade = new Color(0f, 1.0f, 0f, 0.4f);
	}
	
	public void render() {
		drawImage(box, 0, 0, 80, 80);
		if (player.eyeActive()) ;
		else drawImage(Textures.faces[2], 40 - Textures.faces[2].getImageWidth() / 2, 40 - Textures.faces[2].getImageHeight() / 2);
		if (effect != null) effect.render();
		drawImage(heart, 85, 10);
		// render abilities
		for (int i = 0; i < 4; i++) {
			//drawImage(Textures.plHUD[0], 560 + i * 60, 0);
			drawString("" + (i + 1), Fonts.SCORE, Color.white, 563 + i * 60, 3);
			float cooldown;
			switch (i) {
				case PLPlayer.ATTACK:
					break;
				case PLPlayer.BLAST:
					break;
				case PLPlayer.TELEPORT:
					cooldown = player.getTeleportCooldown();
					if (cooldown != 0) drawRect(561 + i * 60, (60 - (60 * cooldown)), 58, (59 * cooldown), redShade);
					break;
				case PLPlayer.VANISH:
					cooldown = player.getVanishCooldown();
					if (cooldown != 0) {
						if (player.invisible()) drawRect(561 + i * 60, (60 - (60 * cooldown)), 58, (59 * cooldown), greenShade);
						else drawRect(561 + i * 60, (60 - (60 * cooldown)), 58, (59 * cooldown), redShade);
					}
					break;
			}
			if (i == player.getCurrentAbility()) drawOutline(560 + i * 60, 0, 60, 60, Color.green);
		}
	}
	
}