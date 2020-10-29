package assets;

import org.newdawn.slick.openal.Audio;

import main.Loader;
import util.data.Settings;

public class Sfx {
	
	// sfx categories
	public static final int TYPE_MENU = 0;
	public static final int TYPE_PLAYER = 1;
	public static final int TYPE_ENTITY = 2;
	public static final int TYPE_MISC = 3;
	
	// menu sfx
	public static Audio[] menu;
	public static final int HOVER = 0;
	public static final int SELECT = 1;
	
	// player sfx
	public static Audio[] player;
	public static final int JUMP = 0;
	public static final int ATTACK = 1;
	public static final int HURT = 2;
	public static final int LOSE = 3;
	public static final int HEAL = 4;
	
	// entity sfx
	public static Audio[] entity;
	public static final int TELEPORT = 0;
	
	// misc sfx
	public static Audio[] misc;
	public static final int THUNDER = 0;
	public static final int STATIC_1 = 1;
	public static final int STATIC_2 = 2;
	public static final int STATIC_3 = 3;
	public static final int STATIC_4 = 4;
	public static final int WALL_BREAK = 5;
	public static final int SLICE = 6;
	public static final int HORROR = 7;
	
	// util
	private static boolean mute;
	private static long muteStart;
	private static int muteTime;
	
	public static void load() {
		// load menu sfx
		menu = new Audio[2];
		menu[HOVER]  = Loader.loadAudio("menu", "h2");
		menu[SELECT] = Loader.loadAudio("menu", "s2");
		// load player sfx
		player = new Audio[5];
		player[JUMP]   = Loader.loadAudio("player", "jump_1");
		player[ATTACK] = Loader.loadAudio("player", "attack_1");
		player[HURT]   = Loader.loadAudio("player", "hurt_1");
		player[LOSE]   = Loader.loadAudio("player", "lose_1");
		player[HEAL]   = Loader.loadAudio("player", "heal_1");
		// load entity sfx
		entity = new Audio[10];
		entity[TELEPORT] = Loader.loadAudio("entity", "teleport");
		// load misc sfx
		misc = new Audio[12];
		misc[THUNDER]    = Loader.loadAudio("misc", "thunder_1");
		misc[STATIC_1]   = Loader.loadAudio("misc", "static_1");
		misc[STATIC_2]   = Loader.loadAudio("misc", "static_2");
		//misc[STATIC_3]   = Loader.loadAudio("misc", "static_3");
		//misc[STATIC_4]   = Loader.loadAudio("misc", "static_4");
		misc[WALL_BREAK] = Loader.loadAudio("misc", "wall_break");
		misc[SLICE]      = Loader.loadAudio("misc", "slice");
		misc[HORROR]     = Loader.loadAudio("misc", "horror");
	}
	
	public static void mute(int ms) {
		muteStart = System.nanoTime();
		mute = true;
		muteTime = ms;
	}
	
	public static void update() {
		if (mute) {
			long elapsed = (System.nanoTime() - muteStart) / 1000000;
			if (elapsed > muteTime) mute = false;
		}
	}
	
	public static boolean isPlaying(int type, int index) {
		switch (type) {
			case TYPE_MENU:
				return menu[index].isPlaying();
			case TYPE_PLAYER:
				return player[index].isPlaying();
			case TYPE_ENTITY:
				return entity[index].isPlaying();
			case TYPE_MISC:
				return misc[index].isPlaying();
		}
		return false;
	}
	
	// play a sound effect
	public static void playSound(int type, int index) {
		if (mute) return;
		switch (type) {
			case TYPE_MENU:
				menu[index].playAsSoundEffect(1.0f, Settings.sfxVolume, false);
				break;
			case TYPE_PLAYER:
				player[index].playAsSoundEffect(1.0f, Settings.sfxVolume, false);
				break;
			case TYPE_ENTITY:
				entity[index].playAsSoundEffect(1.0f, Settings.sfxVolume, false);
				break;
			case TYPE_MISC:
				misc[index].playAsSoundEffect(1.0f, Settings.sfxVolume, false);
				break;
		}
	}
	
	// play a sound effect at a certain volume
	public static void playSound(int type, int index, float vol) {
		if (mute) return;
		switch (type) {
			case TYPE_MENU:
				menu[index].playAsSoundEffect(1.0f, Settings.sfxVolume * vol, false);
				break;
			case TYPE_PLAYER:
				player[index].playAsSoundEffect(1.0f, Settings.sfxVolume * vol, false);
				break;
			case TYPE_ENTITY:
				entity[index].playAsSoundEffect(1.0f, Settings.sfxVolume * vol, false);
				break;
			case TYPE_MISC:
				misc[index].playAsSoundEffect(1.0f, Settings.sfxVolume * vol, false);
				break;
		}
	}
	
	// stop playing a sound effect
	public static void stopSound(int type, int index) {
		switch (type) {
			case TYPE_MENU:
				if (menu[index].isPlaying()) menu[index].stop();
				break;
			case TYPE_PLAYER:
				if (player[index].isPlaying()) player[index].stop();
				break;
			case TYPE_ENTITY:
				if (entity[index].isPlaying()) entity[index].stop();
				break;
			case TYPE_MISC:
				if (misc[index].isPlaying()) misc[index].stop();
				break;
		}
	}
}