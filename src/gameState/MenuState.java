package gameState;

import static main.Render.drawImage;
import static main.Render.drawString;
import static main.Render.fillScreen;

import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import assets.Textures;
import effects.MenuSnowEffect;
import gui.Button;
import gui.Notif;
import main.Render;
import menu.BounceObject;
import menu.CharacterObject;
import menu.FlyInObject;
import menu.StarsEffect;
import util.FadeManager;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class MenuState extends GameState {
	
	// design
	private BounceObject logo;
	private BounceObject title;
	private FlyInObject[] flyIns;
	private CharacterObject[] characters;
	private StarsEffect stars;
	private Texture ts;
	private Texture background;
	private float bgX;
	private float bgY;
	
	// menu items
	private Button[] buttons;
	private Rectangle credits;
	private Rectangle about;
	private Rectangle help;
	private Color creditsColor;
	
	// timing
	private int currentEvent;
	private long timer;
	private float[] delays = {750, 1500, 750, 250, 750, 1500, 750, 250, 750, 1500, 750, 1350, 500, 180, 300, 500};
	
	// music timing
	public static final float SKIP_POSITION = 12.1f;
	
	// notifs
	private ArrayList<Notif> notifs;
	private Notif settingsNotif;
	
	// winter theme
	private MenuSnowEffect snowEffect;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		// init textures/effects
		background = Textures.backgrounds[Textures.MENU];
		logo = new BounceObject(Textures.menu[Textures.LOGO], 400, 125);
		ts = Textures.menu[Textures.TS];
		title = new BounceObject(Textures.menu[Textures.TITLE], 400, 168);
		flyIns = new FlyInObject[4];
		flyIns[0] = new FlyInObject(Textures.menubuttons[Textures.PLAY][0], -75, 300, 400, 300, 200);
		flyIns[1] = new FlyInObject(Textures.menubuttons[Textures.SETTINGS][0], Render.WIDTH + 75, 360, 400, 360, 200);
		flyIns[2] = new FlyInObject(Textures.menubuttons[Textures.CHALLENGES][0], -75, 420, 400, 420, 200);
		flyIns[3] = new FlyInObject(Textures.menubuttons[Textures.QUIT][0], Render.WIDTH + 75, 480, 400, 480, 200);
		characters = new CharacterObject[2];
		getCharacters();
		stars = new StarsEffect();
		// init buttons
		buttons = new Button[4];
		buttons[0] = new Button(Textures.menubuttons[Textures.PLAY][0], Textures.menubuttons[Textures.PLAY][1], 350, 280);
		buttons[1] = new Button(Textures.menubuttons[Textures.SETTINGS][0], Textures.menubuttons[Textures.SETTINGS][1], 339, 340);
		buttons[2] = new Button(Textures.menubuttons[Textures.CHALLENGES][0], Textures.menubuttons[Textures.CHALLENGES][1], 311, 400);
		buttons[3] = new Button(Textures.menubuttons[Textures.QUIT][0], Textures.menubuttons[Textures.QUIT][1], 350, 460);
		// notifs
		notifs = new ArrayList<Notif>();
		settingsNotif = new Notif(320, 356);
		settingsNotif.setVisible(false);
		// play music
		// if intro shown, skip music intro
		if (DataCache.intro_shown) {
			currentEvent = delays.length;
			if (!Music.isPlaying(Music.MENU)) {
				Music.resetVolume();
				Music.play(Music.MENU);
				Music.setPosition(SKIP_POSITION);
				FadeManager.fadeIn(1.0f, true);
			}
			logo.skip();
			title.skip();
			for (FlyInObject f : flyIns) f.skip();
			if (!DataCache.settings_opened_1 || !DataCache.winter_theme_enabled) settingsNotif.setVisible(true);
		}
		else {
			Music.play(Music.MENU);
			// timing
			currentEvent = 0;
		}
		// set bg location
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
		// credits button
		credits = new Rectangle(3, Render.HEIGHT - 28, 70, 23);
		about = new Rectangle(78, Render.HEIGHT - 28, 60, 23);
		help = new Rectangle(145, Render.HEIGHT - 28, 50, 23); // TODO get this precise
		creditsColor = new Color(187, 85, 153);
		DataCache.custom = false;
		timer = System.nanoTime();
		if (!DataCache.settings_opened_1 || !DataCache.winter_theme_enabled) notifs.add(settingsNotif);
		// winter theme
		if (DataCache.snowEffect != null) {
			snowEffect = DataCache.snowEffect;
			System.out.println("[MenuState] Setting snow effect from DataCache");
		}
		//TEMP: TEST
		//Converter.textureToBufferedImage(Textures.escape_key);
		//util.misc.GlitchMaker.glitch2("sprites/minigame/y_ghost_attack", 16, 20);
	}
	
	private void getCharacters() {
		ArrayList<Integer> characterValues = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) characterValues.add(i);
		if (DataCache.mirror_intro) characterValues.add(4);
		// get first character
		int index = (int) (Math.random() * characterValues.size());
		characters[0] = new CharacterObject(Textures.menu_sprites[characterValues.get(index)], 220, 630);
		characterValues.remove(index);
		index = (int) (Math.random() * characterValues.size());
		characters[1] = new CharacterObject(Textures.menu_sprites[characterValues.get(index)], 580, 630);
	}
	
	public void refresh() {
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
	}
	
	public void update() {
		// if the DataCache contains a snow effect, grab it then clear it
		if (DataCache.snowEffect != null) {
			snowEffect = DataCache.snowEffect;
			DataCache.snowEffect = null;
		}
		if (currentEvent == delays.length) {
			for (Button b : buttons) b.update();
			stars.update();
			// winter theme
			if (snowEffect == null) {
				snowEffect = new MenuSnowEffect();
				if (!Settings.winterTheme) snowEffect.setAlpha(0);
				else snowEffect.setAlpha(1);
			}
		}
		else {
			logo.update();
			title.update();
			for (CharacterObject c : characters) c.update();
			for (FlyInObject f : flyIns) f.update();
			stars.update();
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > delays[currentEvent]) {
				currentEvent++;
				if (currentEvent == 12) {
					logo.start();
					characters[0].start();
				}
				if (currentEvent == 13) {
					title.start();
					characters[1].start();
				}
				if (currentEvent == 14) for (FlyInObject f : flyIns) f.start();
				if (currentEvent == 15) {
					stars.start();
					if (!DataCache.settings_opened_1 || !DataCache.winter_theme_enabled) settingsNotif.setVisible(true);
					// winter theme
					if (snowEffect == null) {
						snowEffect = new MenuSnowEffect();
						if (!Settings.winterTheme) snowEffect.setAlpha(0);
						else snowEffect.setAlpha(1);
					}
				}
				timer = System.nanoTime();
			}
		}
		// move background
		bgX -= 1f;
		bgY -= 1f;
		if (bgX <= -Render.WIDTH) bgX = 0;
		if (bgY <= -Render.HEIGHT) bgY = 0;
		// notifs
		if (!DataCache.settings_opened_1 || !DataCache.winter_theme_enabled) settingsNotif.update();
		else if (settingsNotif.visible()) settingsNotif.setVisible(false);
		if (snowEffect != null) snowEffect.update();
	}
	
	public void render() {
		// check if done with intro
		if (currentEvent == 16) {
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			// render logo
			logo.render();
			title.render();
			characters[0].render();
			characters[1].render();
			for (Button b : buttons) b.render();
			stars.render();
			drawString("Credits", Fonts.MENU_SMALLER, creditsColor, 5, Render.HEIGHT - 28);
			drawString("About", Fonts.MENU_SMALLER, creditsColor, 80, Render.HEIGHT - 28);
			if (DataCache.show_explorer_help) drawString("Help!", Fonts.MENU_SMALLER, creditsColor, 145, Render.HEIGHT - 28);
			// notifs
			for (Notif n : notifs) n.render();
			// winter theme
			if (snowEffect != null) snowEffect.render();
			return;
		}
		// render current event
		long elapsed = (System.nanoTime() - timer) / 1000000;
		String top, middle, bottom;
		if (currentEvent == 11) {
			int alpha = (int) ((delays[currentEvent] - elapsed) / delays[currentEvent] * 255);
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			fillScreen(new Color(255, 255, 255, alpha));
		}
		else if (currentEvent > 11) {
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
		}
		else fillScreen(Color.white);
		int alpha = 0;
		int topX = 170, topY = 250;
		int midX = 180, midY = 280;
		int botX = 150, botY = 310;
		switch (currentEvent) {
		case 0:
			top = "Based on the hit game by";
			alpha = (int) (elapsed / delays[currentEvent] * 255);
			drawImage(ts, 0, 20, (float) (alpha / 255.0));
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), 260, 140);
			break;
		case 1:
			top = "Based on the hit game by";
			drawImage(ts, 0, 20);
			drawString(top, Fonts.MENU_SMALL, Color.black, 260, 140);
			break;
		case 2:
			top = "Based on the hit game by";
			alpha = (int) ((delays[currentEvent] - elapsed) / (float) delays[currentEvent] * 255);
			drawImage(ts, 0, 20, (float) (alpha / 255.0));
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), 260, 140);
			break;
		case 3:
			// nothing
			break;
		case 4:
			top = "A fan game by";
			bottom = "Floober101";
			alpha = (int) (elapsed / delays[currentEvent] * 255);
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), 320, 250);
			drawString(bottom, Fonts.MENU, new Color(0, 0, 0, alpha), 270, 280);
			break;
		case 5:
			top = "A fan game by";
			bottom = "Floober101";
			drawString(top, Fonts.MENU_SMALL, Color.black, 320, 250);
			drawString(bottom, Fonts.MENU, Color.black, 270, 280);
			break;
		case 6:
			top = "A fan game by";
			bottom = "Floober101";
			alpha = (int) ((delays[currentEvent] - elapsed) / (float) delays[currentEvent] * 255);
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), 320, 250);
			drawString(bottom, Fonts.MENU, new Color(0, 0, 0, alpha), 270, 280);
			break;
		case 7:
			// nothing
			break;
		case 8:
			top = "This is an unofficial fan work, and contains";
			middle = "spoilers for DDLC. Please play DDLC first";
			bottom = "before continuing. See the README for details.";
			alpha = (int) (elapsed / delays[currentEvent] * 255);
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), topX, topY);
			drawString(middle, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), midX, midY);
			drawString(bottom, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), botX, botY);
			break;
		case 9:
			top = "This is an unofficial fan work, and contains";
			middle = "spoilers for DDLC. Please play DDLC first";
			bottom = "before continuing. See the README for details.";
			drawString(top, Fonts.MENU_SMALL, Color.black, topX, topY);
			drawString(middle, Fonts.MENU_SMALL, Color.black, midX, midY);
			drawString(bottom, Fonts.MENU_SMALL, Color.black, botX, botY);
			break;
		case 10:
			top = "This is an unofficial fan work, and contains";
			middle = "spoilers for DDLC. Please play DDLC first";
			bottom = "before continuing. See the README for details.";
			alpha = (int) ((delays[currentEvent] - elapsed) / (float) delays[currentEvent] * 255);
			drawString(top, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), topX, topY);
			drawString(middle, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), midX, midY);
			drawString(bottom, Fonts.MENU_SMALL, new Color(0, 0, 0, alpha), botX, botY);
			break;
		case 11:
			// fade out
			break;
		case 12:
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			logo.render();
			characters[0].render();
			break;
		case 13:
			if (!DataCache.intro_shown) DataCache.intro_shown = true;
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			logo.render();
			title.render();
			characters[0].render();
			characters[1].render();
			break;
		case 14:
			if (!DataCache.intro_shown) DataCache.intro_shown = true;
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			logo.render();
			title.render();
			characters[0].render();
			characters[1].render();
			for (FlyInObject f : flyIns) f.render();
			break;
		case 15:
			if (!DataCache.intro_shown) DataCache.intro_shown = true;
			// draw background
			drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
			logo.render();
			title.render();
			characters[0].render();
			characters[1].render();
			for (FlyInObject f : flyIns) f.render();
			stars.render();// winter theme
			if (Settings.winterTheme && snowEffect != null) snowEffect.render();
			break;
		}
	}
	
	protected void handleInput() {
		if (KeyInput.isPressed(KeyInput.F)) {
			//util.SaveData.unlockAll();
			//FadeManager.fadeOut(60, GameStateManager.CREDITS_STATE, false);
			gsm.setState(GameStateManager.CHASE_CHALLENGE_STATE);
			//gsm.overrideState(new ScreencapState());
		}
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (buttons[0].isHover()) { // PLAY
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.snowEffect = snowEffect;
				Sfx.mute(50);
				gsm.setState(GameStateManager.LEVEL_SELECT_STATE);
			}
			if (buttons[1].isHover()) { // SETTINGS
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.snowEffect = snowEffect;
				if (!DataCache.settings_opened_1) {
					DataCache.settings_opened_1 = true;
				}
				gsm.openState(GameStateManager.SETTINGS_STATE);
			}
			if (buttons[2].isHover()) { // CHALLENGES (new)
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.snowEffect = snowEffect;
				// set state
				Sfx.mute(50);
				gsm.setState(GameStateManager.CHALLENGE_STATE);
			}
			if (buttons[3].isHover()) { // QUIT
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}
		if (currentEvent >= 14) {
			if (MouseInput.isClicked(MouseInput.LEFT)) {
				if (credits.contains(MouseInput.getMousePos())) {
					System.out.println("[MenuState] Program is running from: " + System.getProperty("user.dir"));
					String path = System.getProperty("user.dir");
					path += "\\CREDITS.txt";
					try {
						Desktop.getDesktop().open(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Failed to open credits.", "Error", JOptionPane.ERROR_MESSAGE);
						JOptionPane.showMessageDialog(null, e.getStackTrace());
					}
				}
				if (about.contains(MouseInput.getMousePos())) {
					System.out.println("[MenuState] Program is running from: " + System.getProperty("user.dir"));
					String path = System.getProperty("user.dir");
					path += "\\About.txt";
					try {
						Desktop.getDesktop().open(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Failed to open about file.", "Error", JOptionPane.ERROR_MESSAGE);
						JOptionPane.showMessageDialog(null, e.getStackTrace());
					}
				}
				if (DataCache.show_explorer_help && help.contains(MouseInput.getMousePos())) {
					System.out.println("[MenuState] Program is running from: " + System.getProperty("user.dir"));
					String path = System.getProperty("user.dir");
					path += "\\Explorer_Help.txt";
					try {
						Desktop.getDesktop().open(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Failed to open about file.", "Error", JOptionPane.ERROR_MESSAGE);
						JOptionPane.showMessageDialog(null, e.getStackTrace());
					}
				}
			}
		}
	}
}