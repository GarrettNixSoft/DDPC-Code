package gameState;

import static main.Render.drawImage;
import static main.Render.drawString;
import static main.Render.fillScreen;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Music;
import assets.Textures;
import effects.BlizzardEffect;
import effects.RainEffect;
import effects.VisualEffect;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;

public class SDeathState extends GameState {
	
	// render
	private Texture[] background;
	private VisualEffect weather;
	private Color shade;
	private String line1 = "It doesn't have to end this way.";
	private String line2 = "Follow the hole, the wall is a lie.";
	
	// timing
	private long eventTimer;
	private int stage;
	private boolean exiting;
	
	public SDeathState(GameStateManager gsm) {
		this.gsm = gsm;
		background = Textures.s_death_layers;
		TileMap tm = new TileMap();
		if (Settings.winterTheme) weather = new BlizzardEffect();
		else weather = new RainEffect(tm, null, false);
		shade = new Color(0, 0, 0, 0.8f);
		eventTimer = System.nanoTime();
		FadeManager.fadeIn(3.0f, false);
		DataCache.s_end = true;
	}
	
	private void advance() {
		stage++;
		eventTimer = System.nanoTime();
	}
	
	public void update() {
		if (stage < 5) {
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			switch (stage) {
				case 0:
					if (elapsed > 5000) advance();
					break;
				case 1:
					if (elapsed > 1000) advance();
					break;
				case 2:
					if (elapsed > 3000) advance();
					break;
				case 3:
					if (elapsed > 1000) advance();
					break;
				case 4:
					if (elapsed > 3000) advance();
					break;
			}
		}
		weather.update();
	}
	
	public void render() {
		drawImage(background[0], 0, 0);
		weather.render();
		drawImage(background[1], 0, 0);
		drawImage(background[2], 0, 0);
		fillScreen(shade);
		int l1x = 420, l2x = 600;
		int l1y = 480, l2y = 540;
		if (stage == 1) {
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			float percent = elapsed / 1000.0f;
			drawString(line1, Fonts.SCRIPT_LARGE, new Color(1, 1, 1, percent), l1x, l1y);
		}
		else if (stage == 2) {
			drawString(line1, Fonts.SCRIPT_LARGE, Color.white, l1x, l1y);
		}
		else if (stage == 3) {
			drawString(line1, Fonts.SCRIPT_LARGE, Color.white, l1x, l1y);
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			float percent = elapsed / 1000.0f;
			drawString(line2, Fonts.SCRIPT_SMALL, new Color(1, 1, 1, percent), l2x, l2y);
		}
		else if (stage == 4) {
			drawString(line1, Fonts.SCRIPT_LARGE, Color.white, l1x, l1y);
			drawString(line2, Fonts.SCRIPT_SMALL, Color.white, l2x, l2y);
		}
		else if (stage == 5) {
			drawString(line1, Fonts.SCRIPT_LARGE, Color.white, l1x, l1y);
			drawString(line2, Fonts.SCRIPT_SMALL, Color.white, l2x, l2y);
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			if (elapsed / 800 % 2 == 0) return;
			drawImage(Textures.escape_key, 20, 540);
			drawString("to return to menu", Fonts.MENU_SMALLER, Color.white, 70, 550);
		}
	}
	
	protected void handleInput() {
		if (KeyInput.isPressed(KeyInput.ESC) && !exiting && !FadeManager.inProgress()) {
			FadeManager.fadeOut(3.0f, GameStateManager.MENU_STATE, true);
			Music.fade(0, 3000);
			exiting = true;
		}
	}
}