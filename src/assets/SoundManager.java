package assets;

import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.StreamSound;

import main.Loader;
import util.data.Settings;

public class SoundManager {
	
	public static StreamSound[] music;
	
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
	
	// fade up/down
	private static boolean[] fading;
	private static float[] volume;
	private static float[] volumeStart;
	private static float[] volumeTarget;
	private static float[] diff;
	private static long[] millisStart;
	private static long[] millisTarget;
	
	// MULTI-CHANNEL SETUP
	public static StreamSound[] channels;
	
	public static void load() {
		System.out.println("[SoundManager] TESTING SOUND STREAM LOAD TIME");
		long startTime = System.nanoTime();
		music = new StreamSound[15];
		music[0] = Loader.loadMusicStream("menu");
		music[1] = Loader.loadMusicStream("s_level");
		music[2] = Loader.loadMusicStream("n_level");
		music[3] = Loader.loadMusicStream("y_level");
		music[4] = Loader.loadMusicStream("m_glitch");
		music[5] = Loader.loadMusicStream("rain_thunder_loop");
		music[6] = Loader.loadMusicStream("level_win");
		music[7] = Loader.loadMusicStream("s_save");
		music[8] = Loader.loadMusicStream("m_end");
		music[9] = Loader.loadMusicStream("m_epilogue");
		music[10] = Loader.loadMusicStream("s_level_glitch");
		music[11] = Loader.loadMusicStream("n_level_glitch");
		music[12] = Loader.loadMusicStream("y_level_glitch");
		music[13] = Loader.loadMusicStream("credits");
		music[14] = Loader.loadMusicStream("minigame/forest");
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		System.out.println("[SoundManager] Stream loading complete, took " + elapsed + "ms");
		// set up channels
		channels = new StreamSound[4];
		fading = new boolean[4];
		volume = new float[4];
		volumeStart = new float[4];
		volumeTarget = new float[4];
		diff = new float[4];
		millisStart = new long[4];
		millisTarget = new long[4];
		for (int i = 0; i < channels.length; i++) volume[i] = Settings.musicVolume;
	}
	
	public static void playMusicAvail(int index) {
		int channel = getNextAvailableChannel();
		playMusic(index, channel);
	}
	
	public static void playOnceAvail(int index) {
		int channel = getNextAvailableChannel();
		playOnce(index, channel);
	}
	
	public static void playMusic(int index, int channel) {
		System.out.println("[SoundManager] Playing track #" + index + " on channel " + channel);
		if (channel == -1) return;
		channels[channel] = music[index];
		channels[channel].playAsSoundEffect(1.0f, volume[channel], true);
	}
	
	public static void playOnce(int index, int channel) {
		if (channel == -1) return;
		channels[channel] = music[index];
		channels[channel].playAsSoundEffect(1.0f, volume[channel], false);
	}
	
	public static void stopChannel(int channel) {
		if (channel == -1) return;
		channels[channel].stop();
		channels[channel] = null;
	}
	
	public static void stopMusic(int index) {
		for (int channel = 0; channel < channels.length; channel++) {
			if (channels[channel] == music[index]) stopChannel(channel);
		}
	}
	
	public static void resetAll() {
		for (int channel = 0; channel < channels.length; channel++) {
			resetVolume(channel);
		}
	}
	
	public static void setChannelVolume(int channel, float vol) {
		if (channel == -1) return;
		volume[channel] = vol;
		
	}
	
	public static void resetVolume(int channel) {
		if (channel == -1) return;
		volume[channel] = Settings.musicVolume;
		fading[channel] = false;
		if (channels[channel] != null) {
			AL10.alSourcef(channels[channel].getBufferID(), AL10.AL_GAIN, Settings.musicVolume);
		}
	}
	
	public static void setPosition(float position, int channel) {
		if (channel == -1) return;
		if (channels[channel] != null) channels[channel].setPosition(position);
	}
	
	public static void updateVolume(float vol, int channel) {
		if (channel == -1) return;
		if (channels[channel] != null) AL10.alSourcef(channels[channel].getBufferID(), AL10.AL_GAIN, vol);
		volume[channel] = vol;
	}
	
	public static boolean isPlaying(int musicID) {
		return music[musicID].isPlaying();
	}
	
	public static int getNextAvailableChannel() {
		for (int i = 0; i < channels.length; i++) {
			if (channels[i] == null) return i;
		}
		return -1;
	}
	
	public static void fade(float target, int millis, int channel) {
		millisStart[channel] = System.nanoTime() / 1000000;
		millisTarget[channel] = millisStart[channel] + millis;
		if (volume[channel] == 0) System.out.println("[MusicManager] Started at 0 volume!");
		volumeStart[channel] = volume[channel];
		volumeTarget[channel] = target;
		if (volumeTarget[channel] > Settings.musicVolume) volumeTarget[channel] = Settings.musicVolume; // treat the setting as the maximum
		diff[channel] = volumeTarget[channel] - volumeStart[channel];
		fading[channel] = true;
		System.out.println("[MusicManager] Fade Set on channel " + channel + ": from " + volumeStart[channel] + " to " + volumeTarget[channel] + " over " + millis + "ms");
	}
	
	public static void fadeTrack(float target, int millis, int track) {
		for (int channel = 0; channel < channels.length; channel++) {
			if (channels[channel] == music[track]) {
				millisStart[channel] = System.nanoTime() / 1000000;
				millisTarget[channel] = millisStart[channel] + millis;
				if (volume[channel] == 0) System.out.println("[MusicManager] Started at 0 volume!");
				volumeStart[channel] = volume[channel];
				volumeTarget[channel] = target;
				if (volumeTarget[channel] > Settings.musicVolume) volumeTarget[channel] = Settings.musicVolume; // treat the setting as the maximum
				diff[channel] = volumeTarget[channel] - volumeStart[channel];
				fading[channel] = true;
				System.out.println("[MusicManager] Fade Set on channel " + channel + ": from " + volumeStart[channel] + " to " + volumeTarget[channel] + " over " + millis + "ms");
			}
		}
	}
	
	public static void fadeAll(float target, int millis) {
		for (int channel = 0; channel < channels.length; channel++) {
			millisStart[channel] = System.nanoTime() / 1000000;
			millisTarget[channel] = millisStart[channel] + millis;
			if (volume[channel] == 0) System.out.println("[Music] Started at 0 volume!");
			volumeStart[channel] = volume[channel];
			volumeTarget[channel] = target;
			if (volumeTarget[channel] > Settings.musicVolume) volumeTarget[channel] = Settings.musicVolume; // treat the setting as the maximum
			diff[channel] = volumeTarget[channel] - volumeStart[channel];
			fading[channel] = true;
			System.out.println("[Music] Fade Set on channel " + channel + ": from " + volumeStart[channel] + " to " + volumeTarget[channel] + " over " + millis + "ms");
		}
	}
	
	public static void update() {
		// update music stuff
		for (int i = 0; i < channels.length; i++) {
			if (fading[i]) {
				long millis = System.nanoTime() / 1000000;
				if (millis < millisTarget[i]) {
					float percent = (millis - millisStart[i]) / (float) (millisTarget[i] - millisStart[i]);
					volume[i] = volumeStart[i] + (diff[i] * percent);
					System.out.println("[MusicManager] Diff = " + diff[i] + ", volStart = " + volumeStart[i]);
					System.out.println("[MusicManager] Fading, percent calculated to " + (percent * 100) + "%, volume set to: " + volume[i]);
					if (channels[i] != null) AL10.alSourcef(channels[i].getBufferID(), AL10.AL_GAIN, volume[i]);
				}
				else {
					volume[i] = volumeTarget[i];
					if (channels[i] != null) AL10.alSourcef(channels[i].getBufferID(), AL10.AL_GAIN, volume[i]);
					// screw it we're doing something else
					fading[i] = false;
					System.out.println("[Music] Fade on channel " + i + " complete.");
				}
			}
			else {
				// check if music on this channel is inactive; if it is, free the channel
				if (channels[i] != null && !channels[i].isPlaying()) {
					channels[i] = null;
				}
			}
		}
	}
}