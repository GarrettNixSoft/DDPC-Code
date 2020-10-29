package gameState;

import static main.Render.drawImage;
import static main.Render.drawStringShadowed;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import assets.Textures;
import effects.MenuSnowEffect;
import gui.Button;
import main.Render;
import util.FadeManager;
import util.data.DataCache;
import util.input.MouseInput;

public class WinState extends GameState {
	
	// background
	private Texture bg;
	private float bgX;
	private float bgY;
	
	// render
	private Texture title;
	private Texture sprite;
	private Color textColor;
	
	// buttons
	private Button nextLevelButton;
	private Button menuButton;
	
	// update
	private long exitTimer;
	private boolean menu;
	
	// winter theme
	private MenuSnowEffect snowEffect;
	
	public WinState(GameStateManager gsm) {
		this.gsm = gsm;
		bg = Textures.backgrounds[Textures.MENU];
		title = Textures.menu[Textures.LEVEL_COMPLETE];
		sprite = Textures.end_sprites[DataCache.character];
		if (DataCache.character == 0) {
			if (DataCache.level < 2) nextLevelButton = new Button(Textures.menubuttons[12][0], Textures.menubuttons[12][1], 315, 485);
		}
		else if (DataCache.level < 3) nextLevelButton = new Button(Textures.menubuttons[12][0], Textures.menubuttons[12][1], 315, 485);
		if (DataCache.custom || DataCache.challenge) nextLevelButton = null;
		menuButton = new Button(Textures.menubuttons[4][0], Textures.menubuttons[4][1], 296, 530);
		FadeManager.fadeIn(1.5f, true);
		Music.resetVolume();
		Music.play(Music.WIN);
		exitTimer = -1;
		textColor = new Color(187, 85, 153);
		// winter theme
		snowEffect = DataCache.snowEffect;
	}
	
	public void update() {
		// update background
		bgX -= 1f;
		bgY -= 1f;
		// reset loop
		if (bgX <= -800) bgX = 0;
		if (bgY <= -600) bgY = 0;
		// check return to menu
		if (exitTimer != -1) {
			long elapsed = (System.nanoTime() - exitTimer) / 1000000;
			if (elapsed > 900) {
				if (menu) {
					DataCache.bgX = bgX;
					DataCache.bgY = bgY;
					gsm.setState(GameStateManager.MENU_STATE);
				}
			}
		}
		else {
			if (nextLevelButton != null) nextLevelButton.update();
			menuButton.update();
		}
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}
	
	public void render() {
		// draw background
		drawImage(bg, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
		// draw title
		drawImage(title, 140, 20);
		// draw sprite
		drawImage(sprite, 100, 200);
		// draw score
		if (DataCache.challenge) {
			if (DataCache.levelSelected.equals("chase_challenge") || DataCache.levelSelected.equals("jump_challenge")) drawStringShadowed("Challenge complete!", Fonts.MENU, textColor, 320, 290, 3, 3);
			else if (DataCache.levelSelected.equals("torture_challenge")) drawStringShadowed("Well done!", Fonts.MENU, textColor, 350, 290, 3, 3);
		}
		else drawStringShadowed("Score: " + DataCache.score, Fonts.MENU, textColor, 350, 290, 3, 3);
		// draw interface
		if (nextLevelButton != null) nextLevelButton.render();
		menuButton.render();
		// winter theme
		if (snowEffect != null) snowEffect.render();
	}
	
	protected void handleInput() {
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (menuButton.isHover()) {
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				DataCache.snowEffect = snowEffect;
				menu = true;
				exitTimer = System.nanoTime();
				FadeManager.fadeOut();
				Music.fade(0, 900);
			}
			if (nextLevelButton != null) {
				if (nextLevelButton.isHover()) {
					Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					String level = "";
					switch (DataCache.character) {
						case 0: // sayori
							level += "s";
							break;
						case 1: // natsuki
							level += "n";
							break;
						case 2: // yuri
							level += "y";
							break;
						case 3: // monika
							level += "m";
							break;
					}
					level += (DataCache.level + 2);
					DataCache.levelSelected = level;
					DataCache.level++;
					if (level.equals("m1") && !DataCache.m_intro_shown) {
						FadeManager.fadeOut(2.0f, GameStateManager.M_STATE, false);
						Music.fade(0, 1900);
					}
					else if (level.equals("m4")) {
						DataCache.m_intro_shown = true;
						FadeManager.fadeOut(2.0f, GameStateManager.M_STATE, false);
						Music.fade(0, 1900);
					}
					else if (level.equals("s4") && DataCache.s_complete) {
						level = "s_happy";
						DataCache.levelSelected = level;
						FadeManager.fadeOut(1.0f, GameStateManager.PLAY_STATE, true);
						Music.fade(0, 900);
					}
					else {
						FadeManager.fadeOut(1.0f, GameStateManager.PLAY_STATE, true);
						Music.fade(0, 900);
					}
				}
			}
		}
	}
}