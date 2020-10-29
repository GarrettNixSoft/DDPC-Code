package util.data;

import assets.Music;

public class Settings {
	
	// volume
	public static float musicVolume;
	public static float sfxVolume;
	
	// other
	public static boolean showFPS;
	public static boolean allowMETA;
	
	// theme
	public static boolean winterTheme;
	
	public static void setMusicVolume(float volume) {
		musicVolume = volume;
		Music.resetVolume();
		SaveData.save();
	}
	
	public static void setSfxVolume(float volume) {
		sfxVolume = volume;
		SaveData.save();
	}
	
	public static float getMusicVolume() {
		return musicVolume;
	}
	
	public static float getSfxVolume() {
		return sfxVolume;
	}
	
}