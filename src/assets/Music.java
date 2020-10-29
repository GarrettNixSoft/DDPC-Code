package assets;

import main.Loader;
import util.data.Settings;

public class Music {
	
	public static org.newdawn.slick.Music[] music;
	public static org.newdawn.slick.Music currentMusic;
	public static final int MENU = 0;
	public static final int S_LEVEL = 1;
	public static final int N_LEVEL = 2;
	public static final int Y_LEVEL = 3;
	public static final int GLITCH = 4;
	public static final int THUNDER = 5;
	public static final int WIN = 6;
	public static final int S_END = 7;
	public static final int M_END = 8;
	public static final int M_EPILOGUE = 9;
	public static final int S_LEVEL_GLITCH = 10;
	public static final int N_LEVEL_GLITCH = 11;
	public static final int Y_LEVEL_GLITCH = 12;
	public static final int CREDITS = 13;
	public static final int MINIGAME_BG = 14;
	public static final int WIND = 15;
	
	// fade up/down
	private static boolean fading;
	private static float volume;
	private static float volumeStart;
	private static float volumeTarget;
	private static float diff;
	private static long millisStart;
	private static long millisTarget;
	
	public static void load() {
		music = new org.newdawn.slick.Music[16];
		music[0] = Loader.loadMusic("menu");
		music[1] = Loader.loadMusic("s_level");
		music[2] = Loader.loadMusic("n_level");
		music[3] = Loader.loadMusic("y_level");
		music[4] = Loader.loadMusic("m_glitch");
		music[5] = Loader.loadMusic("rain_loop");
		music[6] = Loader.loadMusic("level_win");
		music[7] = Loader.loadMusic("s_save");
		music[8] = Loader.loadMusic("m_end");
		music[9] = Loader.loadMusic("m_epilogue");
		music[10] = Loader.loadMusic("s_level_glitch");
		music[11] = Loader.loadMusic("n_level_glitch");
		music[12] = Loader.loadMusic("y_level_glitch");
		music[13] = Loader.loadMusic("credits");
		//music[14] = Loader.loadMusic("minigame/forest");
		music[15] = Loader.loadMusic("wind");
		volume = Settings.musicVolume;
	}
	
	public static void play(int index) {
		System.out.println("[MusicManager] Playing track #" + index + " on Music Channel");
		currentMusic = music[index];
		currentMusic.loop(1.0f, volume);
	}
	
	public static void playOnce(int index) {
		currentMusic = music[index];
		currentMusic.play(1.0f, volume);
	}
	
	public static void stop() {
		currentMusic.stop();
		currentMusic = null;
	}
	
	public static void setVolume(float vol) {
		volume = vol;
		if (currentMusic != null) currentMusic.setVolume(volume);
	}
	
	public static void resetVolume() {
		volume = Settings.musicVolume;
		fading = false;
		if (currentMusic != null) currentMusic.setVolume(volume);
	}
	
	public static void setPosition(float position) {
		if (currentMusic != null) currentMusic.setPosition(position);
	}
	
	public static boolean isPlaying(int musicID) {
		return music[musicID].playing();
	}
	
	public static void fade(float target, int millis) {
		millisStart = System.nanoTime() / 1000000;
		millisTarget = millisStart + millis;
		if (volume == 0) System.out.println("[Music] Started at 0 volume!");
		volumeStart = volume;
		volumeTarget = target;
		if (volumeTarget > Settings.musicVolume) volumeTarget = Settings.musicVolume; // treat the setting as the maximum
		diff = volumeTarget - volumeStart;
		fading = true;
		System.out.println("[Music] Fade Set on channel Music Channel: from " + volumeStart + " to " + volumeTarget + " over " + millis + "ms");
	}
	
	public static void update() {
		if (fading) {
			long millis = System.nanoTime() / 1000000;
			if (millis < millisTarget) {
				float percent = (millis - millisStart) / (float) (millisTarget - millisStart);
				volume = volumeStart + (diff * percent);
				//System.out.println("[Music] Diff = " + diff + ", volStart = " + volumeStart);
				//System.out.println("[Music] Fading, percent calculated to " + (percent * 100) + "%, volume set to: " + volume);
				if (currentMusic != null) currentMusic.setVolume(volume);
			}
			else {
				volume = volumeTarget;
				if (currentMusic != null) currentMusic.setVolume(volume);
				// screw it we're doing something else
				fading = false;
				System.out.println("[Music] Fade on Music Channel complete.");
			}
		}
		else {
			// check if music on this channel is inactive; if it is, free the channel
			if (currentMusic != null && !currentMusic.playing()) {
				currentMusic = null;
			}
		}
	}
}