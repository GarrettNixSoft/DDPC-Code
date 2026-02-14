package gameState;

import static main.Render.drawImage;
import static main.Render.drawOutline;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import effects.LevelS4Effect;
import effects.MenuSnowEffect;
import effects.PopupEffect;
import effects.VisualEffect;
import gui.Button;
import gui.ButtonTogglable;
import main.Render;
import util.FadeManager;
import util.data.DataCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class LevelSelectState extends GameState {
	
	private Texture background;
	private Texture title;
	private float bgX;
	private float bgY;
	private boolean exit;
	
	// characters
	private Texture[] characters;
	
	// levels
	private Texture[][] levels;
	private Texture[][] levels_2;
	
	// icons
	private Texture[] lock;
	
	// buttons
	private Button[][] level_buttons_1;
	private Button[][] level_buttons_2;
	private boolean[][] locked_1;
	private boolean[][] locked_2;
	private Button backButton;
	private ButtonTogglable[] arrow;
	
	// pages
	private int currentPage;
	
	// effects
	private VisualEffect s_effect;
	private PopupEffect popup;
	private MenuSnowEffect snowEffect;
	
	public LevelSelectState(GameStateManager gsm) {
		this.gsm = gsm;
		background = Textures.backgrounds[Textures.MENU];
		title = Textures.menu[Textures.LEVEL_SELECT];
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
		characters = Textures.characters;
		levels = Textures.levelThumbnails;
		levels_2 = Textures.levelThumbnails2;
		lock = Textures.lock;
		currentPage = DataCache.level_select_page;
		// init level buttons
		level_buttons_1 = new Button[4][4];
		locked_1 = new boolean[4][4];
		// sayori levels
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.s_progress) {
				level_buttons_1[0][i] = new Button(levels[0][i], levels[0][i], 190 + i * 140, 105, 120, 80);
				locked_1[0][i] = false;
			}
			else {
				level_buttons_1[0][i] = new Button(lock[0], lock[1], 190 + i * 140, 105, 120, 80);
				locked_1[0][i] = true;
			}
		}
		// natsuki levels
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.n_progress) {
				level_buttons_1[1][i] = new Button(levels[1][i], levels[1][i], 190 + i * 140, 215, 120, 80);
				locked_1[1][i] = false;
			}
			else {
				level_buttons_1[1][i] = new Button(lock[0], lock[1], 190 + i * 140, 215, 120, 80);
				locked_1[1][i] = true;
			}
		}
		// yuri levels
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.y_progress) {
				level_buttons_1[2][i] = new Button(levels[2][i], levels[2][i], 190 + i * 140, 325, 120, 80);
				locked_1[1][i] = false;
			}
			else {
				level_buttons_1[2][i] = new Button(lock[0], lock[1], 190 + i * 140, 325, 120, 80);
				locked_1[2][i] = true;
			}
		}
		// monika levels
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.m_progress) {
				level_buttons_1[3][i] = new Button(levels[3][i], levels[3][i], 190 + i * 140, 435, 120, 80);
				locked_1[3][i] = false;
			}
			else {
				level_buttons_1[3][i] = new Button(lock[0], lock[1], 190 + i * 140, 435, 120, 80);
				locked_1[3][i] = true;
			}
		}
		level_buttons_2 = new Button[4][4];
		locked_2 = new boolean[4][4];
		// sayori
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.s_progress_2) {
				level_buttons_2[0][i] = new Button(levels_2[0][i], levels_2[0][i], 190 + i * 140, 105, 120, 80);
				locked_2[0][i] = false;
			}
			else {
				level_buttons_2[0][i] = new Button(lock[0], lock[1], 190 + i * 140, 105, 120, 80);
				locked_2[0][i] = true;
			}
		}
		// natsuki
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.n_progress_2) {
				level_buttons_2[1][i] = new Button(levels_2[1][i], levels_2[1][i], 190 + i * 140, 215, 120, 80);
				locked_2[1][i] = false;
			}
			else {
				level_buttons_2[1][i] = new Button(lock[0], lock[1], 190 + i * 140, 215, 120, 80);
				locked_2[1][i] = true;
			}
		}
		// yuri
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.y_progress_2) {
				level_buttons_2[2][i] = new Button(levels_2[2][i], levels_2[2][i], 190 + i * 140, 325, 120, 80);
				locked_2[1][i] = false;
			}
			else {
				level_buttons_2[2][i] = new Button(lock[0], lock[1], 190 + i * 140, 325, 120, 80);
				locked_2[2][i] = true;
			}
		}
		// all
		for (int i = 0; i < 4; i++) {
			if (i <= DataCache.m_progress_2) {
				level_buttons_2[3][i] = new Button(levels_2[3][i], levels_2[3][i], 190 + i * 140, 435, 120, 80);
				locked_2[3][i] = false;
			}
			else {
				level_buttons_2[3][i] = new Button(lock[0], lock[1], 190 + i * 140, 435, 120, 80);
				locked_2[3][i] = true;
			}
		}
		// other ui elements
		backButton = new Button(Textures.menubuttons[Textures.BACK_1][0], Textures.menubuttons[Textures.BACK_1][1], 296, 540);
		popup = new PopupEffect();
		// effects
		if (DataCache.s_progress == 3 && !DataCache.s_complete) s_effect = new LevelS4Effect();
		if (DataCache.m_complete) {
			arrow = new ButtonTogglable[2];
			if (currentPage == 0) {
				arrow[0] = new ButtonTogglable(Textures.menubuttons[9][0], Textures.menubuttons[9][1], 60, 545, false);
				arrow[1] = new ButtonTogglable(Textures.menubuttons[8][0], Textures.menubuttons[8][1], 700, 545, true);
			}
			else {
				arrow[0] = new ButtonTogglable(Textures.menubuttons[9][0], Textures.menubuttons[9][1], 60, 545, true);
				arrow[1] = new ButtonTogglable(Textures.menubuttons[8][0], Textures.menubuttons[8][1], 700, 545, true);
			}
		}
		// winter theme
		snowEffect = DataCache.snowEffect;
	}
	
	public void update() {
		// move background
		bgX -= 1f;
		bgY -= 1f;
		// reset once moved all the way
		if (bgX <= -800) bgX = 0;
		if (bgY <= -600) bgY = 0;
		if (!exit && !popup.isOpen()) {
			if (currentPage == 0) {
				for (Button[] buttons : level_buttons_1) {
					for (Button b : buttons) b.update();
				}
			}
			else {
				for (Button[] buttons : level_buttons_2) {
					for (Button b : buttons) b.update();
				}
			}
			backButton.update();
			if (arrow != null) {
				arrow[0].update();
				arrow[1].update();
			}
		}
		if (s_effect != null) s_effect.update();
		popup.update();
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}
	
	public void render() {
		drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
		drawImage(title, 200, -5);
		for (int i = 0; i < characters.length; i++) {
			drawImage(characters[i], 100 - (characters[i].getImageWidth() / 2), 145 + i * 110 - (characters[i].getImageHeight() / 2));
		}
		for (int i = 0; i < levels.length; i++) {
			for (int j = 0; j < levels[i].length; j++) {
				if (currentPage == 0) {
					level_buttons_1[i][j].render();
					switch (i) {
						case 0:
							if (j < DataCache.s_progress) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 1:
							if (j < DataCache.n_progress) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 2:
							if (j < DataCache.y_progress) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 3:
							if (j < DataCache.m_progress) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
					}
				}
				else {
					level_buttons_2[i][j].render();
					switch (i) {
						case 0:
							if (j < DataCache.s_progress_2) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 1:
							if (j < DataCache.n_progress_2) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 2:
							if (j < DataCache.y_progress_2) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
						case 3:
							if (j < DataCache.m_progress_2) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.green);
							else drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.black);
							break;
					}
				}
				
				if (level_buttons_1[i][j].isHover()) drawOutline(190 + j * 140, 105 + i * 110, 120, 80, Color.yellow);
			}
		}
		backButton.render();
		if (s_effect != null) s_effect.render();
		if (arrow != null) {
			arrow[0].render();
			arrow[1].render();
		}
		popup.render();
		// winter theme
		if (snowEffect != null) snowEffect.render();
	}
	
	protected void handleInput() {
		if (popup.isOpen()) {
			// nothing
		}
		else if (MouseInput.isClicked(MouseInput.LEFT)) {
			// check level buttons
			if (exit) return;
			for (int i = 0; i < level_buttons_1.length; i++) {
				for (int j = 0; j < level_buttons_1[i].length; j++) {
					if (level_buttons_1[i][j].isHover() || level_buttons_2[i][j].isHover()) {
						Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
						if (currentPage == 0 && locked_1[i][j]) return;
						//else if (currentPage == 1 && locked_2[i][j]) return;
						String level = "";
						switch (i) {
							case 0:
								level += "s";
								break;
							case 1:
								level += "n";
								break;
							case 2:
								level += "y";
								break;
							case 3:
								level += "m";
								break;
						}
						level += (j + 1);
						if (currentPage == 1) level += "_2";
						DataCache.levelSelected = level;
						DataCache.level = j;
						if (currentPage == 1) {
							DataCache.level++;
							DataCache.level += (i + 1) * 10;
						}
						//System.out.println("level: " + DataCache.level);
						if (currentPage == 0 && DataCache.m_complete && !KeyInput.isDown(KeyInput.LSHIFT)) {
							popup.open();
							break;
						}
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
						else if (level.endsWith("_2")) {
							if (level.equals("s1_2") && !DataCache.ch2_intro) {
								FadeManager.fadeOut(2.0f, GameStateManager.CH2_INTRO_STATE, true);
								Music.fade(0, 1900);
							}
							else {
								FadeManager.fadeOut(1.0f, GameStateManager.PLAY_STATE_CH2, false);
								Music.fade(0, 900);
							}
						}
						else {
							FadeManager.fadeOut(1.0f, GameStateManager.PLAY_STATE, true);
							Music.fade(0, 900);
						}
						exit = true;
						DataCache.custom = false;
					}
				}
			}
			if (backButton.isHover()) {
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.level_select_page = currentPage;
				DataCache.snowEffect = snowEffect;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
				gsm.setState(GameStateManager.MENU_STATE);
			}
			if (arrow != null) {
				if (arrow[0].isHover()) {
					System.out.println("[LevelSelectState] left button");
					DataCache.bgX = bgX;
					DataCache.bgY = bgY;
					if (currentPage == 0) {
						// first page, do nothing
						return;
					}
					else if (currentPage == 1) {
						// return to first page
						currentPage = 0;
						arrow[0].disable();
						Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					}
				}
				if (arrow[1].isHover()) {
					System.out.println("[LevelSelectState] right button");
					DataCache.bgX = bgX;
					DataCache.bgY = bgY;
					gsm.setState(GameStateManager.CUSTOM_STATE);
					Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
					// no second page, expansion cancelled for now
					/*
					if (currentPage == 0) {
						// next page
						currentPage = 1;
						arrow[0].enable();
						Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					}
					else if (currentPage == 1) {
						DataCache.bgX = bgX;
						DataCache.bgY = bgY;
						DataCache.level_select_page = currentPage;
						Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
						Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
						gsm.setState(GameStateManager.CUSTOM_STATE);
					}
					*/
				}
			}
		}
	}
}