package gameState;

import static main.Render.drawImage;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Sfx;
import assets.Textures;
import effects.MenuSnowEffect;
import gui.Button;
import gui.ChallengeButton;
import gui.ChallengePopup;
import main.Render;
import util.StringUtil;
import util.data.DataCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class ChallengesState extends GameState {
	
	// background
	private Texture background;
	private Texture title;
	private float bgX;
	private float bgY;
	
	// challenges
	public static final int CHASE = 0;
	public static final int JUMP = 1;
	public static final int TORTURE = 2;
	private ChallengeButton[] buttons;
	private String[] labels;
	private int[] labelPositions;
	// popups
	private boolean[] unlocked;
	private ChallengePopup[] popups;
	private ChallengePopup popupWindow;
	private boolean popupOpen;
	
	// other gui
	private Button menuButton;
	
	// winter theme
	private MenuSnowEffect snowEffect;
	
	public ChallengesState(GameStateManager gsm) {
		// general setup
		this.gsm = gsm;
		background = Textures.backgrounds[Textures.MENU];
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
		// challenges
		title = Textures.challenge_title;
		initChallengeButtons();
		initLabels();
		initPopups();
		checkUnlocks();
		// other gui
		menuButton = new Button(Textures.menubuttons[Textures.BACK_1][0], Textures.menubuttons[Textures.BACK_1][1], 296, 540);
		// winter theme
		snowEffect = DataCache.snowEffect;
	}
	
	/*
	 * Create the buttons used to select a challenge.
	 */
	private void initChallengeButtons() {
		buttons = new ChallengeButton[3];
		Texture[] buttonTex = new Texture[3];
		buttonTex[0] = (DataCache.m_complete) ? Textures.challenge_thumbnails[0] : Textures.challenge_thumbnails[3];
		buttonTex[1] = (DataCache.m_complete) ? Textures.challenge_thumbnails[1] : Textures.challenge_thumbnails[3];
		buttonTex[2] = (DataCache.chase_complete && DataCache.jump_complete) ? Textures.challenge_thumbnails[2] : Textures.challenge_thumbnails[4];
		buttons[0] = new ChallengeButton(buttonTex[0], 150, 250);
		buttons[1] = new ChallengeButton(buttonTex[1], 400, 250);
		buttons[2] = new ChallengeButton(buttonTex[2], 650, 250);
	}
	
	/*
	 * Decide which labels to use and where to position them.
	 */
	private void initLabels() {
		labels = new String[3];
		labels[0] = (DataCache.m_complete) ? "Yuri's Escape" : "???";
		labels[1] = (DataCache.m_complete) ? "Perfect Jump Run" : "???";
		labels[2] = (DataCache.chase_complete && DataCache.jump_complete) ? "I'm (not) Sorry" : "???";
		labelPositions = new int[3];
		labelPositions[0] = StringUtil.getOffset(labels[0], Fonts.AWT_MENU_SMALL);
		labelPositions[1] = StringUtil.getOffset(labels[1], Fonts.AWT_MENU_SMALL);
		labelPositions[2] = StringUtil.getOffset(labels[2], Fonts.AWT_MENU_SMALL);
	}
	
	/*
	 * Create the popup menus to be displayed as previews for the challenges.
	 */
	private void initPopups() {
		popups = new ChallengePopup[3];
		for (int i = 0; i < popups.length; i++) popups[i] = new ChallengePopup(i);
	}
	
	/*
	 * Determine which challenges have been unlocked.
	 */
	private void checkUnlocks() {
		unlocked = new boolean[3];
		unlocked[0] = unlocked[1] = DataCache.m_complete;
		unlocked[2] = DataCache.chase_complete && DataCache.jump_complete;
	}
	
	@Override
	public void update() {
		// move background
		bgX -= 1f;
		bgY -= 1f;
		// reset once moved all the way
		if (bgX <= -Render.WIDTH) bgX = 0;
		if (bgY <= -Render.HEIGHT) bgY = 0;
		// check popup
		boolean popup = false;
		popupWindow = null;
		for (ChallengePopup cp : popups) {
			if (cp.isOpen()) {
				popup = true;
				popupWindow = cp;
			}
		}
		if (popup) {
			popupWindow.update();
		}
		else {
			// challenges
			for (ChallengeButton cb : buttons) cb.update();
			// other gui
			menuButton.update();
		}
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}

	@Override
	public void render() {
		// draw background
		drawImage(background, bgX, bgY);
		// challenges
		Render.drawImageC(title, Render.WIDTH / 2, 50);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].render();
			Render.drawStringShadowed(labels[i], Fonts.MENU_SMALL, Color.white, buttons[i].getX() + labelPositions[i], buttons[i].getY() + 100, 2, 2);
		}
		// completion stars
		if (DataCache.chase_complete) drawImage(Textures.star_yellow, 60, 180);
		if (DataCache.jump_complete) drawImage(Textures.star_yellow, 310, 180);
		if (DataCache.torture_complete) drawImage(Textures.star_yellow, 560, 180);
		// popup
		for (ChallengePopup cp : popups) cp.render();
		// other gui
		menuButton.render();
		// winter theme
		if (snowEffect != null) snowEffect.render();
	}

	@Override
	protected void handleInput() {
		if (popupOpen) {
			if (!popupWindow.isOpen()) {
				popupOpen = false;
			}
			if (KeyInput.isPressed(KeyInput.ESC)) {
				popupWindow.close();
				popupOpen = false;
			}
		}
		else {	
			if (MouseInput.isClicked(MouseInput.LEFT)) {
				// challenges
				for (int i = 0; i < buttons.length; i++) {
					// only proceed if it's unlocked
					if (unlocked[i]) {
						// check if the button is clicked
						if (buttons[i].isHover()) {
							Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
							popups[i].open();
							popupWindow = popups[i];
							popupOpen = true;
						}
					}
				}
				// other gui
				if (menuButton.isHover()) {
					Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					DataCache.bgX = bgX;
					DataCache.bgY = bgY;
					DataCache.snowEffect = snowEffect;
					Sfx.mute(50);
					gsm.setState(GameStateManager.MENU_STATE);
				}
			}
		}
	}

}