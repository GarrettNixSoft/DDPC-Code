package util.data;

import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import assets.Music;

/*
 * This file SHOULD be in util.data, but I can't move it because
 * it would mess up save data from previous builds of the game.
 */
public class SaveData {
	
	// level progress keys
	public static final String S_PROGRESS = "s_progress";
	public static final String N_PROGRESS = "n_progress";
	public static final String Y_PROGRESS = "y_progress";
	public static final String M_PROGRESS = "m_progress";
	public static final String S_PROGRESS_2 = "s_progress_2";
	public static final String N_PROGRESS_2 = "n_progress_2";
	public static final String Y_PROGRESS_2 = "y_progress_2";
	public static final String M_PROGRESS_2 = "m_progress_2";
	
	// specific event keys
	public static final String S_END = "s_end";
	public static final String S_COMPLETE = "s_complete";
	public static final String M_INTRO = "m_intro";
	public static final String M_END_DIALOGUE = "m_dialogue";
	public static final String M_COMPLETE = "m_complete";
	
	// expansion events
	public static final String CH2_INTRO = "ch2_intro";
	public static final String MIRROR_INTRO = "mirror_intro";
	
	// settings keys
	public static final String MUSIC_VOL = "music_vol";
	public static final String SFX_VOL = "sfx_vol";
	public static final String SHOW_FPS = "show_fps";
	public static final String ALLOW_META = "allow_meta";
	public static final String WINTER_THEME = "winter_theme";
	
	// challenge keys
	public static final String CHASE_CHALLENGE_COMPLETED = "chase_challenge_completed";
	public static final String JUMP_CHALLENGE_COMPLETED = "jump_challenge_completed";
	public static final String TORTURE_CHALLENGE_COMPLETED = "torture_challenge_completed";
	
	public static final String TORTURE_INTRO_SHOWN = "torture_intro_shown";
	public static final String ATTEMPTS_TOTAL = "attempts_total";
	
	// winter theme
	public static final String SETTINGS_OPENED_1 = "settings_opened_1";
	public static final String WINTER_THEME_ENABLED = "winter_theme_enabled";
	
	// explorer help
	public static final String SHOW_EXPLORER_HELP = "show_explorer_help";
	
	// prefs
	private static Preferences prefs;
	
	/*
	// check if the game has been run before
	private static boolean checkFirstRun() {
		String path = System.getProperty("user.dir") + "\\firstrun";
		File firstrun = new File(path);
		if (!firstrun.exists()) {
			try {
				firstrun.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to create preferences file.\nIf this occurs, please contact\nthe developer and send a\nscreenshot of the following:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1);
			}
			return true;
		}
		return false;
	}
	*/
	
	// load progress data
	public static void load() {
		prefs = Preferences.userNodeForPackage(SaveData.class);
		// init progress
		DataCache.s_progress = prefs.getInt(S_PROGRESS, 0);
		DataCache.n_progress = prefs.getInt(N_PROGRESS, 0);
		DataCache.y_progress = prefs.getInt(Y_PROGRESS, 0);
		DataCache.m_progress = prefs.getInt(M_PROGRESS, -1);
		System.out.println("[SaveData] s_progress:     " + DataCache.s_progress);
		System.out.println("[SaveData] n_progress:     " + DataCache.n_progress);
		System.out.println("[SaveData] y_progress:     " + DataCache.y_progress);
		System.out.println("[SaveData] m_progress:     " + DataCache.m_progress);
		DataCache.s_progress_2 = prefs.getInt(S_PROGRESS_2, 0);
		DataCache.n_progress_2 = prefs.getInt(N_PROGRESS_2, 0);
		DataCache.y_progress_2 = prefs.getInt(Y_PROGRESS_2, 0);
		DataCache.m_progress_2 = prefs.getInt(M_PROGRESS_2, -1);
		// init events
		DataCache.s_end = prefs.getBoolean(S_END, false);
		DataCache.s_complete = prefs.getBoolean(S_COMPLETE, false);
		DataCache.m_intro_shown = prefs.getBoolean(M_INTRO, false);
		DataCache.m_end_dialogue = prefs.getBoolean(M_END_DIALOGUE, false);
		DataCache.m_complete = prefs.getBoolean(M_COMPLETE, false);
		System.out.println("[SaveData] s_end:          " + DataCache.s_end);
		System.out.println("[SaveData] s_complete:     " + DataCache.s_complete);
		System.out.println("[SaveData] m_intro_shown:  " + DataCache.m_intro_shown);
		System.out.println("[SaveData] m_end_dialogue: " + DataCache.m_end_dialogue);
		System.out.println("[SaveData] m_complete:     " + DataCache.m_complete);
		// init settings
		Settings.musicVolume = prefs.getFloat(MUSIC_VOL, 0.75f);
		Settings.sfxVolume = prefs.getFloat(SFX_VOL, 0.75f);
		Settings.showFPS = prefs.getBoolean(SHOW_FPS, false);
		Settings.allowMETA = prefs.getBoolean(ALLOW_META, false);
		Settings.winterTheme = prefs.getBoolean(WINTER_THEME, false);
		// init challenges
		DataCache.chase_complete = prefs.getBoolean(CHASE_CHALLENGE_COMPLETED, false);
		DataCache.jump_complete = prefs.getBoolean(JUMP_CHALLENGE_COMPLETED, false);
		DataCache.torture_complete = prefs.getBoolean(TORTURE_CHALLENGE_COMPLETED, false);
		DataCache.torture_intro_shown = prefs.getBoolean(TORTURE_INTRO_SHOWN, false);
		DataCache.attempts_total = prefs.getLong(ATTEMPTS_TOTAL, 0);
		// init winter theme
		DataCache.settings_opened_1 = prefs.getBoolean(SETTINGS_OPENED_1, false);
		DataCache.winter_theme_enabled = prefs.getBoolean(WINTER_THEME_ENABLED, false);
		// init explorer help
		//DataCache.show_explorer_help = prefs.getBoolean(SHOW_EXPLORER_HELP, false);
		Music.resetVolume();
	}
	
	// save progress data
	public static void save() {
		// save level progress
		prefs.putInt(S_PROGRESS, DataCache.s_progress);
		prefs.putInt(N_PROGRESS, DataCache.n_progress);
		prefs.putInt(Y_PROGRESS, DataCache.y_progress);
		prefs.putInt(M_PROGRESS, DataCache.m_progress);
		prefs.putInt(S_PROGRESS_2, DataCache.s_progress_2);
		prefs.putInt(N_PROGRESS_2, DataCache.n_progress_2);
		prefs.putInt(Y_PROGRESS_2, DataCache.y_progress_2);
		prefs.putInt(M_PROGRESS_2, DataCache.m_progress_2);
		// save events
		prefs.putBoolean(S_END, DataCache.s_end);
		prefs.putBoolean(S_COMPLETE, DataCache.s_complete);
		prefs.putBoolean(M_INTRO, DataCache.m_intro_shown);
		prefs.putBoolean(M_END_DIALOGUE, DataCache.m_end_dialogue);
		prefs.putBoolean(M_COMPLETE, DataCache.m_complete);
		// save settings
		prefs.putFloat(MUSIC_VOL, Settings.musicVolume);
		prefs.putFloat(SFX_VOL, Settings.sfxVolume);
		prefs.putBoolean(SHOW_FPS, Settings.showFPS);
		prefs.putBoolean(ALLOW_META, Settings.allowMETA);
		prefs.putBoolean(WINTER_THEME, Settings.winterTheme);
		// save challenges
		prefs.putBoolean(CHASE_CHALLENGE_COMPLETED, DataCache.chase_complete);
		prefs.putBoolean(JUMP_CHALLENGE_COMPLETED, DataCache.jump_complete);
		prefs.putBoolean(TORTURE_CHALLENGE_COMPLETED, DataCache.torture_complete);
		prefs.putBoolean(TORTURE_INTRO_SHOWN, DataCache.torture_intro_shown);
		prefs.putLong(ATTEMPTS_TOTAL, DataCache.attempts_total);
		// save winter theme
		prefs.putBoolean(SETTINGS_OPENED_1, DataCache.settings_opened_1);
		prefs.putBoolean(WINTER_THEME_ENABLED, DataCache.winter_theme_enabled);
		// save explorer help
		prefs.getBoolean(SHOW_EXPLORER_HELP, false);
	}
	
	// reset progress
	public static void resetProgress() {
		int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset your\nprogress? This cannot be undone.", "Confirm Reset", JOptionPane.YES_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			// reset level progress
			prefs.putInt(S_PROGRESS, 0);
			prefs.putInt(N_PROGRESS, 0);
			prefs.putInt(Y_PROGRESS, 0);
			prefs.putInt(M_PROGRESS, -1);
			prefs.putInt(S_PROGRESS_2, 0);
			prefs.putInt(N_PROGRESS_2, 0);
			prefs.putInt(Y_PROGRESS_2, 0);
			prefs.putInt(M_PROGRESS_2, -1);
			// reset events
			prefs.putBoolean(S_END, false);
			prefs.putBoolean(S_COMPLETE, false);
			prefs.putBoolean(M_INTRO, false);
			prefs.putBoolean(M_END_DIALOGUE, false);
			prefs.putBoolean(M_COMPLETE, false);
			// reset challenges
			prefs.putBoolean(CHASE_CHALLENGE_COMPLETED, false);
			prefs.putBoolean(JUMP_CHALLENGE_COMPLETED, false);
			prefs.putBoolean(TORTURE_CHALLENGE_COMPLETED, false);
			prefs.putBoolean(TORTURE_INTRO_SHOWN, false);
			// reset explorer help
			prefs.putBoolean(SHOW_EXPLORER_HELP, false);
			// reset current game
			load();
			JOptionPane.showMessageDialog(null, "Progress reset successfully.", "Success", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	// reset without confirmation
	public static void resetProgressNoConfirm() {
		// reset level progress
		prefs.putInt(S_PROGRESS, 0);
		prefs.putInt(N_PROGRESS, 0);
		prefs.putInt(Y_PROGRESS, 0);
		prefs.putInt(M_PROGRESS, -1);
		prefs.putInt(S_PROGRESS_2, 0);
		prefs.putInt(N_PROGRESS_2, 0);
		prefs.putInt(Y_PROGRESS_2, 0);
		prefs.putInt(M_PROGRESS_2, -1);
		// reset events
		prefs.putBoolean(S_END, false);
		prefs.putBoolean(S_COMPLETE, false);
		prefs.putBoolean(M_INTRO, false);
		prefs.putBoolean(M_END_DIALOGUE, false);
		prefs.putBoolean(M_COMPLETE, false);
		// reset challenges
		prefs.putBoolean(CHASE_CHALLENGE_COMPLETED, false);
		prefs.putBoolean(JUMP_CHALLENGE_COMPLETED, false);
		prefs.putBoolean(TORTURE_CHALLENGE_COMPLETED, false);
		prefs.putBoolean(TORTURE_INTRO_SHOWN, false);
		// reset explorer help
		prefs.putBoolean(SHOW_EXPLORER_HELP, false);
		// reset current game
		load();
	}
	
	// unlock everything (for testing purposes)
	public static void unlockAll() {
		// reset level progress
		prefs.putInt(S_PROGRESS, 3);
		prefs.putInt(N_PROGRESS, 3);
		prefs.putInt(Y_PROGRESS, 3);
		prefs.putInt(M_PROGRESS, 3);
		prefs.putInt(S_PROGRESS_2, 3);
		prefs.putInt(N_PROGRESS_2, 3);
		prefs.putInt(Y_PROGRESS_2, 3);
		prefs.putInt(M_PROGRESS_2, 3);
		// reset events
		prefs.putBoolean(S_END, true);
		prefs.putBoolean(S_COMPLETE, true);
		prefs.putBoolean(M_INTRO, true);
		prefs.putBoolean(M_END_DIALOGUE, true);
		prefs.putBoolean(M_COMPLETE, true);
		// reset current game
		load();
		JOptionPane.showMessageDialog(null, "All levels/features unlocked.", "Success", JOptionPane.PLAIN_MESSAGE);
	}
}