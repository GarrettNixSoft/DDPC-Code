package assets;

import java.awt.Font;

import org.newdawn.slick.UnicodeFont;

import main.Loader;

public class Fonts {
	
	// menu fonts
	public static UnicodeFont MENU;
	public static UnicodeFont MENU_SMALL;
	public static Font AWT_MENU_SMALL;
	public static UnicodeFont MENU_MED;
	public static UnicodeFont MENU_SMALLER;
	
	// dialogue fonts
	public static UnicodeFont DIALOGUE_NAME;
	public static UnicodeFont DIALOGUE_TEXT;
	
	// credits fonts
	public static UnicodeFont FULLSCREEN;
	public static Font AWT_FULLSCREEN;
	public static UnicodeFont CENTERED;
	public static Font AWT_CENTERED;
	public static UnicodeFont CENTERED_MED;
	public static Font AWT_CENTERED_MED;
	public static UnicodeFont SCROLL;
	public static Font AWT_SCROLL;
	
	// other fonts
	public static UnicodeFont SCRIPT_LARGE;
	public static UnicodeFont SCRIPT_SMALL;
	public static UnicodeFont SCORE;
	public static Font AWT_SCORE;
	
	// challenges
	public static Font AWT_CHALLENGE;
	public static UnicodeFont CHALLENGE;
	
	// dialogue utility
	public static Font AWT_NAME;
	public static Font AWT_TEXT;
	
	public static void load() {
		// load menu fonts
		MENU = Loader.loadFont("menu", 48);
		MENU_MED = Loader.loadFont("menu", 30);
		MENU_SMALL = Loader.loadFont("menu", 24);
		AWT_MENU_SMALL = Loader.loadDefaultAwtFont("Arial", Font.PLAIN, 24);
		MENU_SMALLER = Loader.loadFont("menu", 20);
		// load dialogue fonts
		DIALOGUE_NAME = Loader.loadFont("menu", 24);
		DIALOGUE_TEXT = Loader.loadFont("menu", 28);
		// load credits fonts
		FULLSCREEN = Loader.loadDefaultFont("Segoe Print", Font.PLAIN, 32);
		AWT_FULLSCREEN = Loader.loadDefaultAwtFont("Segoe Print", Font.PLAIN, 32);
		CENTERED = Loader.loadDefaultFont("Segoe Print", Font.PLAIN, 24);
		AWT_CENTERED = Loader.loadDefaultAwtFont("Segoe Print", Font.PLAIN, 24);
		CENTERED_MED = Loader.loadDefaultFont("Segoe Print", Font.PLAIN, 20);
		AWT_CENTERED_MED = Loader.loadDefaultAwtFont("Segoe Print", Font.PLAIN, 20);
		SCROLL = Loader.loadDefaultFont("Segoe Print", Font.PLAIN, 18);
		AWT_SCROLL = Loader.loadDefaultAwtFont("Segoe Print", Font.PLAIN, 18);
		// load other fonts
		SCRIPT_LARGE = Loader.loadFont("journal", 48);
		SCRIPT_SMALL = Loader.loadFont("journal", 24);
		SCORE = Loader.loadDefaultFont("Arial", Font.PLAIN, 14);
		AWT_SCORE = Loader.loadDefaultAwtFont("Arial", Font.PLAIN, 14);
		// load challenge fonts
		AWT_CHALLENGE = Loader.loadDefaultAwtFont("Arial", Font.PLAIN, 22);
		CHALLENGE = Loader.loadFont("menu", 22);
		// load utility fonts
		AWT_NAME = new Font("Arial", Font.PLAIN, 24);
		AWT_TEXT = new Font("Arial", Font.PLAIN, 28);
	}
	
}