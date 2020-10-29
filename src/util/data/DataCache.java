package util.data;

import cutscene.Camera;
import effects.MenuSnowEffect;
import entity.Character;
import entity.PlayerEnhanced;

/*
 * This class stores data used when transitioning between states.
 * For example, it stores the location of the menu background when
 * transitioning from the menu to the level select screen.
 */
public class DataCache {
	
	// PLAYER DATA
	
	// base game progress
	public static int s_progress;
	public static int n_progress;
	public static int y_progress;
	public static int m_progress;
	
	// expansion progress
	public static int s_progress_2;
	public static int n_progress_2;
	public static int y_progress_2;
	public static int m_progress_2;
	
	// game events completed
	public static boolean s_end;
	public static boolean s_complete;
	public static boolean m_end_dialogue;
	public static boolean m_intro_shown;
	// m_complete = full game completed
	public static boolean m_complete;
	
	// expansion events completed
	public static boolean ch2_intro;
	public static boolean mirror_intro;
	
	// check for player progression
	public static void checkProgression(int character) {
		switch (character) {
			case 0: // sayori
				if (level == s_progress) s_progress++;
				break;
			case 1: // natsuki
				if (level == n_progress) n_progress++;
				break;
			case 2: // yuri
				if (level == y_progress) y_progress++;
				break;
			case 3: // monika
				if (level == m_progress) m_progress++;
				break;
			case 4: // sayori
				if (level == s_progress) s_progress = 3;
				break;
		}
		if (s_progress >= 4 && n_progress >= 4 && y_progress >= 4 && s_complete && m_progress == -1) m_progress = 0;
		System.out.println("[DataCache] PROGRESS:");
		System.out.println("[DataCache] s: " + s_progress);
		System.out.println("[DataCache] n: " + n_progress);
		System.out.println("[DataCache] y: " + y_progress);
		System.out.println("[DataCache] m: " + m_progress);
		// TEMP
		//m_progress = 0;
	}
	
	// current player data
	public static int character;
	public static String score = "0";
	
	// last state the game was in
	public static int lastState;

	// page on level select screen
	public static int level_select_page;
	
	// whether or not the intro has been shown
	// used to go straight to menu while in game
	// instead of playing the intro again first
	public static boolean intro_shown = false;
	
	// the location of the background on screen
	// used to make the background flow smoothly
	// while switching between game states
	public static float bgX;
	public static float bgY;
	
	// used for passing the selected level to
	// the GameStateManager so the PlayState loads
	// the correct level and uses the right textures
	public static String levelSelected = "";
	public static int level;
	public static boolean custom;
	
	// misc data
	public static boolean settings_opened_1;
	public static boolean winter_theme_enabled;
	
	// challenge data
	public static boolean chase_complete;
	public static boolean jump_complete;
	public static boolean torture_complete;
	
	public static boolean torture_intro_shown;
	
	public static int attempts_current;
	public static long attempts_total;
	
	// is the current state a challenge
	public static boolean challenge;
	
	// winter theme in menus
	public static MenuSnowEffect snowEffect;
	
	// whether or not to show the explorer restoration help
	public static boolean show_explorer_help = false;
	
	// CHAPTER 2 DATA
	public static PlayerEnhanced player;
	public static Character[] characters;
	public static Camera camera;
	public static int winX;
	public static int winY;
	
}