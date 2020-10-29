package gameState;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import main.Render;
import util.FadeManager;
import util.data.DataCache;
import util.data.SaveData;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class GameStateManager {
	
	// number of possible game states
	public static final int NUM_STATES = 20;
	// standard game states
	public static final int MENU_STATE = 0;
	public static final int LEVEL_SELECT_STATE = 1;
	public static final int PLAY_STATE = 2;
	public static final int SETTINGS_STATE = 3;
	public static final int WIN_STATE = 4;
	// special game states
	public static final int S_DEATH_STATE = 5;
	public static final int S_SAVE_STATE = 6;
	public static final int M_STATE = 7;
	public static final int M_FINAL_STATE = 8;
	public static final int M_END_STATE = 9;
	public static final int M_EPILOGUE = 10;
	public static final int CREDITS_STATE = 11;
	// custom levels
	public static final int CUSTOM_STATE = 12;
	// unused (originally planned for expansion)
	public static final int PLAY_STATE_CH2 = 13;
	public static final int CH2_INTRO_STATE = 14;
	// challenges
	public static final int CHASE_CHALLENGE_STATE = 15;
	public static final int JUMP_CHALLENGE_STATE = 16;
	public static final int TORTURE_CHALLENGE_STATE = 17;
	// challenge menu
	public static final int CHALLENGE_STATE = 18;
	// testing
	public static final int TEST_STATE = 19;
	
	// game states
	private GameState[] gameStates;
	private int currentState;
	private int prevState;
	
	// FPS counter
	private int frames;
	
	public GameStateManager() {
		gameStates = new GameState[NUM_STATES];
		setState(MENU_STATE);
		FadeManager.initGSM(this);
	}
	
	public void setState(int state) {
		DataCache.lastState = currentState;
		Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		SaveData.save();
	}
	
	private void loadState(int state) {
		// default to false
		boolean temp = DataCache.challenge;
		DataCache.challenge = false;
		switch (state) {
			case MENU_STATE:
				gameStates[state] = new MenuState(this);
				break;
			case LEVEL_SELECT_STATE:
				gameStates[state] = new LevelSelectState(this);
				break;
			case PLAY_STATE:
				System.out.println("LOADING LEVEL: " + DataCache.levelSelected);
				gameStates[state] = new PlayState(this, DataCache.levelSelected);
				break;
			case SETTINGS_STATE:
				gameStates[state] = new SettingsState(this);
				break;
			case WIN_STATE:
				DataCache.challenge = temp;
				gameStates[state] = new WinState(this);
				break;
			case S_DEATH_STATE:
				gameStates[state] = new SDeathState(this);
				break;
			case S_SAVE_STATE:
				gameStates[state] = new SEndState(this);
				break;
			case M_STATE:
				gameStates[state] = new MState(this);
				break;
			case M_FINAL_STATE:
				DataCache.levelSelected = "m_final";
				gameStates[state] = new MFinalLevelState(this);
				break;
			case M_END_STATE:
				DataCache.level = 2;
				DataCache.levelSelected = "m_end";
				gameStates[state] = new MEndState(this, false);
				break;
			case M_EPILOGUE:
				gameStates[state] = new MEpilogueState(this);
				break;
			case CREDITS_STATE:
				gameStates[state] = new CreditsState(this);
				break;
			case CUSTOM_STATE:
				gameStates[state] = new CustomLevelState(this);
				break;
			case PLAY_STATE_CH2:
				gameStates[state] = new PlayStateEP(this);
				break;
			case CH2_INTRO_STATE:
				gameStates[state] = new Chapter2Intro(this);
				break;
			case CHASE_CHALLENGE_STATE:
				// chase challenge activate
				gameStates[state] = new ChaseChallengeState(this);
				DataCache.challenge = true;
				break;
			case JUMP_CHALLENGE_STATE:
				// jump challenge activate
				gameStates[state] = new MEndState(this, true);
				DataCache.challenge = true;
				break;
			case TORTURE_CHALLENGE_STATE:
				// torture challenge activate
				gameStates[state] = new TortureChallengeState(this);
				DataCache.challenge = true;
				break;
			case CHALLENGE_STATE:
				gameStates[state] = new ChallengesState(this);
				break;
			case TEST_STATE:
				gameStates[state] = new TestState(this);
				break;
		}
	}
	
	public void overrideState(GameState state) {
		gameStates[currentState] = state;
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	// similar to setState, but leaves previous state in memory to come back to it
	public void openState(int state) {
		prevState = currentState;
		DataCache.lastState = currentState;
		currentState = state;
		loadState(currentState);
	}
	
	// return to the previous state
	public void returnToPrev() {
		DataCache.lastState = currentState;
		currentState = prevState;
		gameStates[currentState].refresh();
	}
	
	public void frameRate(int frames) {
		this.frames = frames;
	}
	
	public void update() {
		KeyInput.update();
		MouseInput.update();
		FadeManager.update();
		Music.update();
		Sfx.update();
		gameStates[currentState].handleInput();
		gameStates[currentState].update();
	}
	
	public void render() {
		// clear frame
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        // draw new frame
		gameStates[currentState].render();
		// draw fade
		FadeManager.render();
		// draw FPS
		if (Settings.showFPS) Render.drawStringShadowed(frames + "fps", Fonts.MENU_SMALLER, Color.white, Render.WIDTH - 80, Render.HEIGHT - 25, 2, 2);
	}
	
}