package gui;

import static main.Render.drawImage;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import gameState.GameStateManager;
import util.FadeManager;
import util.data.DataCache;
import util.input.MouseInput;

public class DeadMenu extends Menu {
	
	private long timer;
	private boolean fadeComplete;
	
	public DeadMenu(GameStateManager gsm) {
		this.gsm = gsm;
		this.bg = Textures.menu_bg_short;
		this.title = Textures.dead_title;
		buttons = new Button[2];
		buttons[0] = new Button(Textures.restart[0], Textures.restart[1], 331, 280);
		if (DataCache.levelSelected.equals("m_end") || DataCache.levelSelected.equals("m_final")) buttons[1] = new Button(Textures.exit[2], Textures.exit[3], 312, 330);
		else buttons[1] = new Button(Textures.exit[0], Textures.exit[1], 312, 330);
		timer = -1;
	}
	
	public void start() {
		timer = System.nanoTime();
	}
	
	public void update() {
		if (!fadeComplete) {
			if (timer != -1) {
				long elapsed = (System.nanoTime() - timer) / 1000000;
				if (elapsed > 1000) fadeComplete = true;
			}
		}
		else for (Button b : buttons) b.update();
	}
	
	public void render() {
		if (!fadeComplete) {
			if (timer == -1) {
				return;
			}
			long elapsed = (System.nanoTime() - timer) / 1000000;
			float percent = elapsed / 1000.0f;
			drawImage(bg, 250, 175, percent);
			drawImage(title, 281, 195, percent);
			for (Button b : buttons) b.render(percent);
		}
		else {
			drawImage(bg, 250, 175);
			drawImage(title, 281, 195);
			for (Button b : buttons) b.render();
		}
	}
	
	public void handleInput() {
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].isHover()) {
					Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
					switch (i) {
						case 0:// restart
							if (DataCache.levelSelected.equals("m_end")) gsm.setState(GameStateManager.M_END_STATE);
							else if (DataCache.levelSelected.equals("m_final")) gsm.setState(GameStateManager.M_FINAL_STATE);
							else if (DataCache.levelSelected.equals("chase_challenge")) gsm.setState(GameStateManager.CHASE_CHALLENGE_STATE);
							else if (DataCache.levelSelected.equals("jump_challenge")) gsm.setState(GameStateManager.JUMP_CHALLENGE_STATE);
							else if (DataCache.levelSelected.equals("torture_challenge")) gsm.setState(GameStateManager.TORTURE_CHALLENGE_STATE);
							else gsm.setState(GameStateManager.PLAY_STATE);
							break;
						case 1: // exit
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