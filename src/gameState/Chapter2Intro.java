package gameState;

import static main.Render.drawImage;
import static main.Render.fillScreen;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Textures;
import entity.Monika;
import gui.Dialogue;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.input.KeyInput;
import util.input.MouseInput;

public class Chapter2Intro extends GameState {
	
	// map
	private TileMap tilemap;
	
	// monika
	private Monika monika;
	
	// render
	private Texture vignette_white;
	
	// dialogue
	private Dialogue d1;
	private long d1Timer;
	private boolean d1Shown;
	
	public Chapter2Intro(GameStateManager gsm) {
		this.gsm = gsm;
		tilemap = new TileMap("m_room");
		monika = new Monika(tilemap, tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - tilemap.getSpawnX(), Render.HEIGHT / 2 - tilemap.getSpawnY());
		vignette_white = Textures.vignette_white;
		d1 = new Dialogue("ch2_intro");
		FadeManager.fadeIn(2.0f, true);
		Music.resetVolume();
		Music.play(Music.GLITCH);
		d1Timer = System.nanoTime();
	}
	
	public void update() {
		if (!d1Shown) {
			long elapsed = (System.nanoTime() - d1Timer) / 1000000;
			if (elapsed > 1500) {
				d1.open();
				d1Shown = true;
			}
		}
		if (d1.isOpen() || d1.isOpening()) d1.update();
	}
	
	public void render() {
		fillScreen(Color.white);
		tilemap.render();
		monika.render();
		drawImage(vignette_white, 0, 0);
		if (d1.isOpen() || d1.isOpening()) d1.render();
	}
	
	protected void handleInput() {
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) d1.skipAll();
				else d1.next();
				if (d1.isClosing()) {
					FadeManager.fadeOut(3.0f, GameStateManager.PLAY_STATE_CH2, false);
					Music.fade(0, 2500);
				}
			}
		}
	}
}