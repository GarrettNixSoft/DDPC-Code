package gui;

import static main.Render.drawImageC;
import static main.Render.drawStringShadowed;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import assets.Textures;
import gameState.GameStateManager;
import main.Render;
import util.FadeManager;
import util.StringBank;
import util.StringUtil;
import util.data.DataCache;
import util.input.MouseInput;

/*
 * The ChallengePopup is used as the pop-up window that appears
 * when a player clicks on a ChallengeButton to display preview
 * information about the challenge, and offers the "Play" button
 * if they want to begin the challenge.
 */
public class ChallengePopup {
	
	// elements
	private Texture window;
	private Texture title;
	private Texture preview;
	private Button playButton;
	private Button backButton;
	private String[] description;
	private int[][] offset;
	
	// open or not?
	private boolean open;
	
	// challenge
	private int challenge;
	
	public ChallengePopup(int challenge) {
		this.challenge = challenge;
		// set up info
		title = Textures.challenge_popup_titles[challenge];
		preview = Textures.challenge_popup_previews[challenge];
		switch(challenge) {
		case 0: // chase
			description = StringBank.CHASE_CHALLENGE_DESC;
			break;
		case 1: // jump
			description = StringBank.JUMP_CHALLENGE_DESC;
			break;
		case 2: // torture
			description = StringBank.TORTURE_CHALLENGE_DESC;
			int last = description.length-1;
			description[last] = description[last].substring(0, description[last].lastIndexOf(' ') + 1) + DataCache.attempts_total;
			break;
		}
		// get description offset
		offset = StringUtil.getOffset(description, Fonts.AWT_CHALLENGE);
		// set up structure
		window = Textures.challenge_popup_base;
		playButton = new Button(Textures.menubuttons[Textures.PLAY][0], Textures.menubuttons[Textures.PLAY][1], 350, 400);
		backButton = new Button(Textures.menubuttons[5][0], Textures.menubuttons[5][1], 361, 445);
	}
	
	// open (display) this window
	public void open() {
		open = true;
	}
	
	// close (hide) this window
	public void close() {
		open = false;
	}
	
	// check if this window is open
	public boolean isOpen() {
		return open;
	}
	
	public boolean backButtonHover() {
		return backButton.isHover();
	}
	
	public void update() {
		if (!open) return;
		
		backButton.update();
		playButton.update();
		handleInput();
	}
	
	public void render() {
		if (!open) return;
		// base
		drawImageC(window, Render.WIDTH / 2, Render.HEIGHT / 2);
		// title
		drawImageC(title, Render.WIDTH / 2, 150);
		// description
		for (int i = 0; i < description.length; i++) {
			drawStringShadowed(description[i], Fonts.CHALLENGE, Color.white, 255 + offset[i][0], 285 + offset[i][1], 1, 1);
		}
		// preview
		drawImageC(preview, Render.WIDTH / 2 + 155, Render.HEIGHT / 2);
		// buttons
		playButton.render();
		backButton.render();
	}
	
	private void handleInput() {
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (playButton.isHover()) { // play challenge
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				switch (challenge) {
				case 0:
					FadeManager.fadeOut(2.0f, GameStateManager.CHASE_CHALLENGE_STATE, false);
					Music.fade(0, 2000);
					break;
				case 1:
					FadeManager.fadeOut(2.0f, GameStateManager.JUMP_CHALLENGE_STATE, false);
					Music.fade(0, 2000);
					break;
				case 2:
					FadeManager.fadeOut(2.0f, GameStateManager.TORTURE_CHALLENGE_STATE, false);
					Music.fade(0, 2000);
					break;
				}
			}
			if (backButton.isHover()) { // cancel/close
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				close();
			}
		}
	}
	
}