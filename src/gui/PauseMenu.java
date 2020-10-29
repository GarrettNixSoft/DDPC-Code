package gui;

import static main.Render.drawImage;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import gameState.GameStateManager;
import gameState.PlayState;
import gameState.PlayStateEP;
import util.FadeManager;
import util.data.DataCache;
import util.input.MouseInput;

public class PauseMenu extends Menu {
	
	private PlayState ps;
	private PlayStateEP pse;
	
	public PauseMenu(GameStateManager gsm, PlayState ps) {
		this.gsm = gsm;
		this.ps = ps;
		bg = Textures.menu_bg;
		title = Textures.paused_title;
		buttons = new Button[4];
		buttons[0] = new Button(Textures.resume[0], Textures.resume[1], 330, 220);
		buttons[1] = new Button(Textures.settings[0], Textures.settings[1], 324, 280);
		buttons[2] = new Button(Textures.restart[0], Textures.restart[1], 331, 340);
		if (DataCache.levelSelected.equals("m_end") || DataCache.levelSelected.equals("m_final")) buttons[3] = new Button(Textures.exit[2], Textures.exit[3], 312, 400);
		else buttons[3] = new Button(Textures.exit[0], Textures.exit[1], 312, 400);
	}
	
	public PauseMenu(GameStateManager gsm, PlayStateEP pse) {
		this.gsm = gsm;
		this.pse = pse;
		bg = Textures.menu_bg;
		title = Textures.paused_title;
		buttons = new Button[4];
		buttons[0] = new Button(Textures.resume[0], Textures.resume[1], 330, 220);
		buttons[1] = new Button(Textures.settings[0], Textures.settings[1], 324, 280);
		buttons[2] = new Button(Textures.restart[0], Textures.restart[1], 331, 340);
		if (DataCache.levelSelected.equals("m_end") || DataCache.levelSelected.equals("m_final")) buttons[3] = new Button(Textures.exit[2], Textures.exit[3], 312, 400);
		else buttons[3] = new Button(Textures.exit[0], Textures.exit[1], 312, 400);
	}
	
	public void update() {
		for (Button b : buttons) b.update();
	}
	
	public void render() {
		drawImage(bg, 250, 125);
		drawImage(title, 308, 135);
		for (Button b : buttons) b.render();
	}
	
	public void handleInput() {
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].isHover()) {
					Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					switch (i) {
						case 0: // resume
							if (ps != null) ps.resume();
							else if (pse != null) pse.resume();
							break;
						case 1: // settings
							gsm.openState(GameStateManager.SETTINGS_STATE);
							break;
						case 2:// restart
							System.out.println("[PauseMenu] DataCache.levelSelected = " + DataCache.levelSelected);
							if (DataCache.levelSelected.equals("m_end")) gsm.setState(GameStateManager.M_END_STATE);
							else if (DataCache.levelSelected.equals("m_final")) gsm.setState(GameStateManager.M_FINAL_STATE);
							else if (DataCache.levelSelected.equals("chase_challenge")) gsm.setState(GameStateManager.CHASE_CHALLENGE_STATE);
							else if (DataCache.levelSelected.equals("jump_challenge")) gsm.setState(GameStateManager.JUMP_CHALLENGE_STATE);
							else if (DataCache.levelSelected.equals("torture_challenge")) gsm.setState(GameStateManager.TORTURE_CHALLENGE_STATE);
							else gsm.setState(GameStateManager.PLAY_STATE);
							break;
						case 3: // exit
							if (DataCache.levelSelected.equals("m_end")) gsm.setState(GameStateManager.M_END_STATE);
							else if (DataCache.levelSelected.equals("m_final")) gsm.setState(GameStateManager.M_FINAL_STATE);
							else {
								FadeManager.fadeOut(1.0f, GameStateManager.MENU_STATE, true);
								Music.fade(0, 1000);
							}
							break;
					}
				}
			}
		}
	}
}