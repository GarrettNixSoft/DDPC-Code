package gameState;

import static main.Render.drawImage;
import static main.Render.drawStringShadowed;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Sfx;
import assets.Textures;
import effects.MenuSnowEffect;
import gui.Button;
import gui.CheckButton;
import gui.Notif;
import gui.Slider;
import main.Render;
import util.data.DataCache;
import util.data.SaveData;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class SettingsState extends GameState {
	
	// background
	private Texture background;
	private float bgX;
	private float bgY;
	
	// layout
	private Texture title;
	private Texture musicLabel;
	private Texture sfxLabel;
	private Slider musicVol;
	private Slider sfxVol;
	private CheckButton showFPS;
	private CheckButton allowMeta;
	private CheckButton winterTheme;
	private Button resetButton;
	private Button backButton;
	
	// notifs
	private Notif winterThemeNotif;
	
	// winter theme
	private MenuSnowEffect snowEffect;
	
	public SettingsState(GameStateManager gsm) {
		this.gsm = gsm;
		background = Textures.backgrounds[Textures.MENU];
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
		snowEffect = DataCache.snowEffect;
		title = Textures.menu[Textures.SETTINGS_TITLE];
		musicLabel = Textures.menu[Textures.MUSIC_VOL];
		sfxLabel = Textures.menu[Textures.SFX_VOL];
		musicVol = new Slider(25, 200, 240, 6);
		musicVol.setValue(Settings.musicVolume);
		sfxVol = new Slider(25, 350, 240, 6);
		sfxVol.setValue(Settings.sfxVolume);
		showFPS = new CheckButton(400, 180);
		showFPS.setChecked(Settings.showFPS);
		allowMeta = new CheckButton(400, 250);
		allowMeta.setChecked(Settings.allowMETA);
		if (DataCache.lastState == GameStateManager.MENU_STATE) {
			winterTheme = new CheckButton(400, 320);
			winterTheme.setChecked(Settings.winterTheme);
		}
		resetButton = new Button(Textures.menubuttons[7][0], Textures.menubuttons[7][1], 272, 430);
		backButton = new Button(Textures.menubuttons[5][0], Textures.menubuttons[5][1], 361, 530);
		// notifs
		if (!DataCache.winter_theme_enabled) {
			winterThemeNotif = new Notif(385, 340);
			winterThemeNotif.setVisible(true);
		}
	}
	
	public void update() {
		// move background
		bgX -= 1f;
		bgY -= 1f;
		// reset once moved all the way
		if (bgX <= -800) bgX = 0;
		if (bgY <= -600) bgY = 0;
		// update buttons
		musicVol.update();
		sfxVol.update();
		Settings.setMusicVolume(musicVol.getPosition());
		Settings.setSfxVolume(sfxVol.getPosition());
		showFPS.update();
		allowMeta.update();
		if (winterTheme != null) winterTheme.update();
		resetButton.update();
		backButton.update();
		// notifs
		if (winterThemeNotif != null) winterThemeNotif.update();
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}
	
	public void render() {
		drawImage(background, bgX, bgY, Render.WIDTH * 2, Render.HEIGHT * 2);
		drawImage(title, 200, 10);
		drawImage(musicLabel, 25, 120);
		drawImage(sfxLabel, 25, 270);
		musicVol.render();
		sfxVol.render();
		showFPS.render();
		drawStringShadowed("Show FPS", Fonts.MENU_SMALL, Color.white, 450, 185, 2, 2);
		allowMeta.render();
		drawStringShadowed("Allow Meta Content", Fonts.MENU_SMALL, Color.white, 450, 255, 2, 2);
		drawStringShadowed("(Windows 10 Only)", Fonts.MENU_SMALL, Color.white, 450, 280, 2, 2);
		if (winterTheme != null) {
			winterTheme.render();
			drawStringShadowed("Winter Theme", Fonts.MENU_SMALL, Color.white, 450, 325, 2, 2);
		}
		resetButton.render();
		backButton.render();
		// notifs
		if (winterThemeNotif != null) winterThemeNotif.render();
		// winter theme
		if (snowEffect != null) snowEffect.render();
	}
	
	protected void handleInput() {
		if (KeyInput.isPressed(KeyInput.ESC)) {
			DataCache.bgX = bgX;
			DataCache.bgY = bgY;
			DataCache.snowEffect = snowEffect;
			Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
			gsm.returnToPrev();
		}
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (resetButton.isHover()) {
				SaveData.resetProgress();
			}
			if (backButton.isHover()) {
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.snowEffect = snowEffect;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				gsm.returnToPrev();
			}
			if (showFPS.isHover()) {
				showFPS.toggleChecked();
				Settings.showFPS = showFPS.isChecked();
			}
			if (allowMeta.isHover()) {
				allowMeta.toggleChecked();
				Settings.allowMETA = allowMeta.isChecked();
			}
			if (winterTheme != null && winterTheme.isHover()) {
				winterTheme.toggleChecked();
				Settings.winterTheme = winterTheme.isChecked();
				DataCache.winter_theme_enabled = true;
				if (winterThemeNotif != null) winterThemeNotif.setVisible(false);
				// handle fade or new snow
				if (winterTheme.isChecked()) {
					snowEffect = new MenuSnowEffect();
				}
				else {
					if (snowEffect != null) snowEffect.fade();
				}
			}
		}
	}
}