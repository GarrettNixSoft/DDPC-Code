package assets;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import main.Loader;
import main.Render;

public class Textures {
	
	// backgrounds
	public static Texture[] backgrounds;
	public static final int MENU = 0;
	public static final int S_LEVEL = 1;
	public static final int N_LEVEL = 2;
	public static final int Y_LEVEL = 3;
	public static final int M_LEVEL_1 = 4;
	public static final int M_LEVEL_2 = 5;
	public static final int M_LEVEL_3 = 6;
	public static final int S_LEVEL_RAIN = 7;
	public static final int S_DEATH = 8;
	public static final int S_END = 9;
	public static final int M_END = 10;
	public static final int M_FINAL = 11;
	
	// ***ANIMATED BACKGROUNDS***
	// BASES
	// noise
	public static Texture[] bg_noise;
	
	// ELEMENTS
	// grid
	public static Texture grid_fade;
	
	// error
	public static Texture error_message;
	public static Texture[] error_glitch;
	
	// ORIGINAL CUSTOM BGs
	// s_death background
	public static Texture[] s_death_layers;
	
	// dynamic background elements
	public static Texture cloud_large;
	public static Texture[] cloud_medium;
	public static Texture cloud_small;
	public static Texture sky_clouds;
	
	// level effects
	public static Texture[] raindrop;
	public static Texture[] raindrop_land;
	public static Texture s_hud_scare;
	public static Texture vignette_1;
	public static Texture vignette_2;
	public static Texture vignette_white;
	public static Texture vignette_red;
	public static Texture[] grid;
	public static Texture[][] glitch_pixels;
	public static Texture glitch_grid;
	public static Texture scare_glitch;
	
	// menu textures
	public static Texture[] menu;
	public static final int LOGO = 0;
	public static final int TS = 1;
	public static final int TITLE = 2;
	public static final int LEVEL_SELECT = 3;
	public static final int SETTINGS_TITLE = 4;
	public static final int MUSIC_VOL = 5;
	public static final int SFX_VOL = 6;
	public static final int LEVEL_COMPLETE = 7;
	public static final int CUSTOM_LEVEL = 8;
	
	// in-game menus
	public static Texture menu_bg;
	public static Texture menu_bg_short;
	public static Texture paused_title;
	public static Texture dead_title;
	public static Texture[] resume;
	public static Texture[] settings;
	public static Texture[] restart;
	public static Texture[] exit;
	public static Texture error_popup;
	
	// menu buttons
	public static Texture[][] menubuttons;
	public static final int PLAY = 0;
	public static final int SETTINGS = 1;
	public static final int CHALLENGES = 2; // new
	public static final int QUIT = 3;
	public static final int BACK_1 = 4;
	public static final int BACK_2 = 5;
	public static final int OK = 6;
	public static Texture[] checkbox;
	
	// other menu elements
	public static Texture[] notif;
	
	// misc buttons
	public static Texture[] slider;
	
	// level select screen
	public static Texture[][] levelThumbnails;
	public static Texture[][] levelThumbnails2;
	public static Texture[] characters;
	public static Texture[] lock;
	
	// special thumbnails
	public static Texture s4_dark;
	
	// tilesets
	public static Texture[][] tileset1;
	public static Texture[][] tileset2;
	public static Texture[][] tileset3;
	
	// dynamic tiles
	public static Texture[] bounceTiles;
	public static Texture[][][] glitchTiles1;
	public static Texture[][][] glitchTiles2;
	public static Texture[][][] glitchTiles3;
	
	// HUD
	public static Texture box;
	public static Texture[] faces;
	public static Texture[] heart;
	
	// dialogue
	public static Texture[] dialogue_faces;
	public static Texture arrow;
	
	// dialogue effects
	public static Texture[][] dialogue_faces_glitch;
	
	// misc UI stuff
	public static Texture escape_key;
	public static Texture preview;
	
	// SPRITES
	// sayori
	public static Texture[] s_idle;
	public static Texture[] s_walk;
	public static Texture[] s_jump;
	public static Texture[] s_fall;
	public static Texture[] s_attack;
	public static Texture s_face;
	// natsuki
	public static Texture[] n_idle;
	public static Texture[] n_walk;
	public static Texture[] n_jump;
	public static Texture[] n_fall;
	public static Texture[] n_attack;
	public static Texture n_face;
	// yuri
	public static Texture[] y_idle;
	public static Texture[] y_walk;
	public static Texture[] y_jump;
	public static Texture[] y_fall;
	public static Texture[] y_attack;
	public static Texture y_face;
	// monika
	public static Texture[] m_idle;
	public static Texture[] m_walk;
	public static Texture[] m_jump;
	public static Texture[] m_fall;
	public static Texture[] m_attack;
	public static Texture light;
	public static Texture m_face;
	// mirror
	public static Texture[] mr_idle;
	public static Texture[] mr_walk;
	public static Texture[] mr_jump;
	public static Texture[] mr_fall;
	public static Texture[] mr_attack;
	public static Texture mr_face;
	// end screen
	public static Texture[] end_sprites;
	
	// ITEMS
	// heal
	public static Texture[] bottle;
	public static Texture[] cupcake;
	public static Texture[] tea;
	public static Texture[] coffee;
	// win
	public static Texture cookie;
	public static Texture manga;
	public static Texture markov;
	public static Texture pen;
	
	// ENEMIES
	// rainclouds
	public static Texture[] raincloud_1;
	public static Texture[] raincloud_2;
	public static Texture[] raincloud_1_death;
	public static Texture[] raincloud_2_death;
	// spiders
	public static Texture[] spider_1;
	public static Texture[] spider_2;
	public static Texture[] spider_1_death;
	public static Texture[] spider_2_death;
	// ghosts
	public static Texture[] ghost_1;
	public static Texture[] ghost_2;
	
	// glitch enemies
	public static Texture[] raincloud_1_glitch;
	public static Texture[] raincloud_2_glitch;
	public static Texture[] spider_1_glitch;
	public static Texture[] spider_2_glitch;
	public static Texture[] ghost_1_glitch;
	public static Texture[] ghost_2_glitch;
	public static Texture[] glitch;
	
	// SPECIAL
	public static Texture wall;
	public static Texture horn;
	
	// EXPANSION ENTITIES
	// splash screen
	private static Texture[] splash;
	
	public static Texture[] teleport_1;
	public static Texture[] teleport_2;
	
	// EXPANSION EFFECTS
	// menu
	public static Texture star;
	public static Texture star_yellow;
	public static Texture[] menu_sprites;
	
	// character glitches
	public static Texture[] s_glitch;
	public static Texture[] n_glitch;
	public static Texture[] y_glitch;
	public static Texture[] m_glitch;
	public static Texture[] mr_glitch;
	
	// in-game
	public static Texture[] lightning;
	
	// WINTER THEME
	public static Texture[][] s_snow_level;
	public static Texture[][] n_snow_level;
	public static Texture[][] y_snow_level;
	public static Texture s_snow_bg;
	public static Texture n_snow_bg;
	public static Texture[] snowflakes;
	public static Texture[] snowflakes_2;
	
	// CHALLENGES
	public static Texture challenge_title;
	public static Texture[] challenge_thumbnails;
	// popup menu
	public static Texture challenge_popup_base;
	public static Texture[] challenge_popup_titles;
	public static Texture[] challenge_popup_previews;
	// challenge-specific
	public static Texture[] portal;
	public static Texture portal_outline;
	
	// <<MINIGAME: YURI'S FRIGHT>>
	
	// EFFECTS
	// persistent
	public static Texture dark_vignette;
	public static Texture darkness;
	
	// recurring
	public static Texture flash;
	
	// dynamic
	public static Texture[] raindrop_2;
	public static Texture[] raindrop_land_2;
	
	// screen
	public static Texture[] blood_spatter_right;
	public static Texture[] blood_spatter_left;
	
	// SPRITES
	// ghost yuri
	public static Texture[] y_ghost_idle;
	public static Texture[] y_ghost_walk;
	public static Texture[] y_ghost_jump;
	public static Texture[] y_ghost_fall;
	public static Texture[] y_ghost_attack;
	
	public static Texture[] y_ghost_idle_glitch;
	public static Texture[][] y_ghost_walk_glitch;
	public static Texture[] y_ghost_jump_glitch;
	public static Texture[] y_ghost_fall_glitch;
	public static Texture[] y_ghost_attack_glitch;

	// ghost monika
	public static Texture[] m_ghost_idle;
	public static Texture[] m_ghost_walk;
	public static Texture[] m_ghost_jump;
	public static Texture[] m_ghost_fall;
	public static Texture[] m_ghost_attack;

	public static Texture[] m_ghost_idle_glitch;
	public static Texture[][] m_ghost_walk_glitch;
	public static Texture[] m_ghost_jump_glitch;
	public static Texture[] m_ghost_fall_glitch;
	public static Texture[] m_ghost_attack_glitch;
	
	/*
	// PL TEXTURES
	public static Texture[][] plYuri;
	public static Texture[][] plGuard;
	public static Texture[][] pl_tiles;
	public static Texture[] plHUD;
	public static Texture[] teleportEffect;
	public static Texture teleportTrail;
	*/
	
	public static void load() {
		splash = new Texture[3];
		splash[0] = Loader.loadTexture("menu", "splash_s");
		splash[1] = Loader.loadTexture("menu", "splash_n");
		splash[2] = Loader.loadTexture("menu", "splash_y");
		int index = (int) (Math.random() * splash.length);
		Render.drawImage(splash[index], 0, 0, Render.WIDTH, Render.HEIGHT);
		Display.update();
		// load backgrounds
		backgrounds = new Texture[12];
		backgrounds[0] = Loader.loadTexture("backgrounds", "menu");
		backgrounds[1] = Loader.loadTexture("backgrounds", "s_level");
		backgrounds[2] = Loader.loadTexture("backgrounds", "n_level");
		backgrounds[3] = Loader.loadTexture("backgrounds", "y_level");
		backgrounds[4] = Loader.loadTexture("backgrounds", "s_level_glitch");
		backgrounds[5] = Loader.loadTexture("backgrounds", "n_level_glitch");
		backgrounds[6] = Loader.loadTexture("backgrounds", "y_level_glitch");
		backgrounds[7] = Loader.loadTexture("backgrounds", "s_level_rain");
		backgrounds[8] = Loader.loadTexture("backgrounds", "s_death");
		backgrounds[9] = Loader.loadTexture("backgrounds", "s_end");
		backgrounds[10] = Loader.loadTexture("backgrounds", "m_end");
		backgrounds[11] = Loader.loadTexture("backgrounds", "m_final");
		// ***ANIMATED BACKGROUNDS***
		// BASES
		// noise
		bg_noise = new Texture[4];
		bg_noise[0] = Loader.loadTexture("backgrounds/animated/bases/noise", "noise1");
		bg_noise[1] = Loader.loadTexture("backgrounds/animated/bases/noise", "noise2");
		bg_noise[2] = Loader.loadTexture("backgrounds/animated/bases/noise", "noise3");
		bg_noise[3] = Loader.loadTexture("backgrounds/animated/bases/noise", "noise4");
		// ELEMENTS
		// grid
		grid_fade = Loader.loadTextureTGA("backgrounds/animated/elements/grid_fade", "grid");
		// error
		error_message = Loader.loadTexture("backgrounds/animated/elements/error", "message");
		error_glitch = new Texture[18];
		for (int i = 0; i < error_glitch.length; i++) {
			error_glitch[i] = Loader.loadTexture("backgrounds/animated/elements/error/error_glitch", "" + i);
		}
		// load s_death background
		s_death_layers = new Texture[3];
		s_death_layers[0] = Loader.loadTexture("backgrounds", "s_death_bottom");
		s_death_layers[1] = Loader.loadTexture("backgrounds", "s_death_middle");
		s_death_layers[2] = Loader.loadTexture("backgrounds", "s_death_top");
		// load dynamic background elements
		cloud_large = Loader.loadTexture("backgrounds/clouds", "cloud_large");
		cloud_medium = new Texture[3];
		cloud_medium[0] = Loader.loadTexture("backgrounds/clouds", "cloud_medium_1");
		cloud_medium[1] = Loader.loadTexture("backgrounds/clouds", "cloud_medium_2");
		cloud_medium[2] = Loader.loadTexture("backgrounds/clouds", "cloud_medium_3");
		cloud_small = Loader.loadTexture("backgrounds/clouds", "cloud_small");
		sky_clouds = Loader.loadTexture("backgrounds/clouds", "sky_clouds");
		// load level effects
		//raindrop = new Texture[1];
		//raindrop[0] = Loader.loadTextureTGA("effects", "raindrop");
		//raindrop_land = new Texture[4];
		//raindrop_land[0] = Loader.loadTextureTGA("effects", "raindrop_land_1");
		//raindrop_land[1] = Loader.loadTextureTGA("effects", "raindrop_land_2");
		//raindrop_land[2] = Loader.loadTextureTGA("effects", "raindrop_land_3");
		//raindrop_land[3] = Loader.loadTextureTGA("effects", "raindrop_land_4");
		s_hud_scare = Loader.loadTexture("ui", "char_sayori_scare");
		vignette_1 = Loader.loadTextureTGA("effects", "s_end_vignette");
		vignette_2 = Loader.loadTextureTGA("effects", "vignette");
		vignette_white = Loader.loadTextureTGA("effects", "vignette_white");
		vignette_red = Loader.loadTextureTGA("effects", "vignette_red");
		grid = new Texture[2];
		grid[0] = Loader.loadTexture("effects", "grid");
		grid[1] = Loader.loadTexture("effects", "grid2");
		glitch_pixels = new Texture[4][4];
		for (int i = 0; i < glitch_pixels.length; i++) {
			for (int j = 0; j < glitch_pixels[i].length; j++) {
				glitch_pixels[i][j] = Loader.loadTexture("effects/glitch", "" + ((i * 4) + (j + 1)));
			}
		}
		glitch_grid = Loader.loadTexture("effects", "glitch_grid");
		scare_glitch = Loader.loadTextureTGA("effects", "scare_glitch");
		// load menu textures
		menu = new Texture[9];
		menu[0] = Loader.loadTexture("menu", "logo");
		menu[1] = Loader.loadTextureTGA("menu", "ts transparent");
		menu[2] = Loader.loadTexture("menu", "title_pixel");
		menu[3] = Loader.loadTextureTGA("menu", "level select");
		menu[4] = Loader.loadTextureTGA("menu", "settings");
		menu[5] = Loader.loadTexture("menu", "music_volume");
		menu[6] = Loader.loadTexture("menu", "sfx_volume");
		menu[7] = Loader.loadTextureTGA("menu", "level_complete");
		menu[8] = Loader.loadTextureTGA("menu", "custom_level");
		// load in-game menu textures
		menu_bg = Loader.loadTexture("menu", "menu_bg");
		menu_bg_short = Loader.loadTexture("menu", "menu_bg_short");
		paused_title = Loader.loadTexture("menu", "paused");
		dead_title = Loader.loadTexture("menu", "dead_title");
		resume = new Texture[2];
		resume[0] = Loader.loadTexture("menu", "resume_button");
		resume[1] = Loader.loadTexture("menu", "resume_button2");
		settings = new Texture[2];
		settings[0] = Loader.loadTexture("menu", "settings_button");
		settings[1] = Loader.loadTexture("menu", "settings_button2");
		restart = new Texture[2];
		restart[0] = Loader.loadTexture("menu", "restart_button");
		restart[1] = Loader.loadTexture("menu", "restart_button2");
		exit = new Texture[4];
		exit[0] = Loader.loadTexture("menu", "exit_button");
		exit[1] = Loader.loadTexture("menu", "exit_button2");
		exit[2] = Loader.loadTexture("menu", "exit_glitch");
		exit[3] = Loader.loadTexture("menu", "exit_glitch2");
		error_popup = Loader.loadTexture("menu", "error_bg");
		// load menu button textures
		menubuttons = new Texture[13][2];
		menubuttons[0][0] = Loader.loadTexture("menu", "button_play");
		menubuttons[0][1] = Loader.loadTexture("menu", "button_play2");
		menubuttons[1][0] = Loader.loadTexture("menu", "button_settings");
		menubuttons[1][1] = Loader.loadTexture("menu", "button_settings2");
		menubuttons[2][0] = Loader.loadTexture("menu", "challenges_button");
		menubuttons[2][1] = Loader.loadTexture("menu", "challenges_button2");
		menubuttons[3][0] = Loader.loadTexture("menu", "button_quit");
		menubuttons[3][1] = Loader.loadTexture("menu", "button_quit2");
		menubuttons[4][0] = Loader.loadTexture("menu", "back_button");
		menubuttons[4][1] = Loader.loadTexture("menu", "back_button2");
		menubuttons[5][0] = Loader.loadTexture("menu", "back");
		menubuttons[5][1] = Loader.loadTexture("menu", "back2");
		menubuttons[6][0] = Loader.loadTexture("menu", "button_ok");
		menubuttons[6][1] = Loader.loadTexture("menu", "button_ok2");
		menubuttons[7][0] = Loader.loadTexture("menu", "button_reset");
		menubuttons[7][1] = Loader.loadTexture("menu", "button_reset2");
		menubuttons[8][0] = Loader.loadTexture("menu", "arrow");
		menubuttons[8][1] = Loader.loadTexture("menu", "arrow2");
		menubuttons[9][0] = Loader.loadTexture("menu", "arrow_left");
		menubuttons[9][1] = Loader.loadTexture("menu", "arrow_left2");
		menubuttons[10][0] = Loader.loadTexture("menu",  "button_load");
		menubuttons[10][1] = Loader.loadTexture("menu",  "button_load2");
		menubuttons[11][0] = Loader.loadTexture("menu",  "play");
		menubuttons[11][1] = Loader.loadTexture("menu",  "play2");
		menubuttons[12][0] = Loader.loadTexture("menu", "button_nextlevel");
		menubuttons[12][1] = Loader.loadTexture("menu", "button_nextlevel2");
		checkbox = new Texture[2];
		checkbox[0] = Loader.loadTexture("menu", "checkbox");
		checkbox[1] = Loader.loadTexture("menu", "check");
		// load other menu elements
		notif = new Texture[2];
		notif[0] = Loader.loadTexture("menu", "notif_1");
		notif[1] = Loader.loadTexture("menu", "notif_2");
		// load misc buttons
		slider = new Texture[2];
		slider[0] = Loader.loadTexture("menu", "slider_bar");
		slider[1] = Loader.loadTexture("menu", "slider_button");
		// load level thumbnails
		levelThumbnails = new Texture[4][4];
		levelThumbnails2 = new Texture[4][4];
		String[] a = {"s", "n", "y", "m"};
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				levelThumbnails[i][j] = Loader.loadTexture("menu/thumbnails", a[i] + (j + 1));
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				levelThumbnails2[i][j] = Loader.loadTexture("menu/thumbnails", a[i] + (j + 1) + "_2");
			}
		}
		// load special thumbnails
		s4_dark = Loader.loadTexture("menu/thumbnails", "s4_dark");
		// load characters
		characters = new Texture[4];
		characters[0] = Loader.loadTexture("menu", "sayori");
		characters[1] = Loader.loadTexture("menu", "natsuki");
		characters[2] = Loader.loadTexture("menu", "yuri");
		characters[3] = Loader.loadTexture("menu", "monika");
		// load other level select icons
		lock = new Texture[2];
		lock[0] = Loader.loadTexture("menu", "lock");
		lock[1] = Loader.loadTexture("menu", "lock2");
		// load tilesets
		tileset1 = Loader.loadTileset("s_level", 60);
		tileset2 = Loader.loadTileset("n_level", 60);
		tileset3 = Loader.loadTileset("y_level", 60);
		// dynamic tiles
		//bounceTiles = new Texture[4];
		//bounceTiles[0] = Loader.loadTexture("tilesets", "bounce1");
		//bounceTiles[1] = Loader.loadTexture("tilesets", "bounce2");
		//bounceTiles[2] = Loader.loadTexture("tilesets", "bounce3");
		//bounceTiles[3] = Loader.loadTexture("tilesets", "bounce4");
		//glitchTiles1 = new Texture[4][][];
		//glitchTiles1[0] = Loader.loadTileset("/glitch_tiles/s_glitch1", 60);
		//glitchTiles1[1] = Loader.loadTileset("/glitch_tiles/s_glitch2", 60);
		//glitchTiles1[2] = Loader.loadTileset("/glitch_tiles/s_glitch3", 60);
		//glitchTiles1[3] = Loader.loadTileset("/glitch_tiles/s_glitch4", 60);
		//glitchTiles2 = new Texture[4][][];
		//glitchTiles2[0] = Loader.loadTileset("/glitch_tiles/n_glitch1", 60);
		//glitchTiles2[1] = Loader.loadTileset("/glitch_tiles/n_glitch2", 60);
		//glitchTiles2[2] = Loader.loadTileset("/glitch_tiles/n_glitch3", 60);
		//glitchTiles2[3] = Loader.loadTileset("/glitch_tiles/n_glitch4", 60);
		//glitchTiles3 = new Texture[4][][];
		//glitchTiles3[0] = Loader.loadTileset("/glitch_tiles/y_glitch1", 60);
		//glitchTiles3[1] = Loader.loadTileset("/glitch_tiles/y_glitch2", 60);
		//glitchTiles3[2] = Loader.loadTileset("/glitch_tiles/y_glitch3", 60);
		//glitchTiles3[3] = Loader.loadTileset("/glitch_tiles/y_glitch4", 60);
		// load HUD textures
		box = Loader.loadTexture("ui", "box");
		faces = new Texture[4];
		faces[0] = Loader.loadTexture("ui", "char_sayori");
		faces[1] = Loader.loadTexture("ui", "char_natsuki");
		faces[2] = Loader.loadTexture("ui", "char_yuri");
		faces[3] = Loader.loadTexture("ui", "char_monika");
		heart = new Texture[2];
		heart[0] = Loader.loadTexture("ui", "heart_empty");
		heart[1] = Loader.loadTexture("ui", "heart_full");
		// load dialogue textures
		dialogue_faces = new Texture[5];
		dialogue_faces[0] = Loader.loadTexture("ui", "char_sayori_dialogue");
		dialogue_faces[1] = Loader.loadTexture("ui", "char_natsuki_dialogue");
		dialogue_faces[2] = Loader.loadTexture("ui", "char_yuri_dialogue");
		dialogue_faces[3] = Loader.loadTexture("ui", "char_monika_dialogue");
		dialogue_faces[4] = Loader.loadTextureTGA("ui", "char_light_dialogue");
		arrow = Loader.loadTexture("ui", "arrow");
		// load dialogue effects
		//dialogue_faces_glitch = new Texture[5][];
		//dialogue_faces_glitch[0] = Loader.loadGlitchTextures("ui/glitch", "char_sayori_dialogue");
		//dialogue_faces_glitch[1] = Loader.loadGlitchTextures("ui/glitch", "char_natsuki_dialogue");
		//dialogue_faces_glitch[2] = Loader.loadGlitchTextures("ui/glitch", "char_yuri_dialogue");
		//dialogue_faces_glitch[3] = Loader.loadGlitchTextures("ui/glitch", "char_monika_dialogue");
		// load misc UI stuff
		escape_key = Loader.loadTexture("ui", "escape_key");
		preview = Loader.loadTexture("menu", "preview");
		// LOAD SPRITES
		// sayori
		s_idle = Loader.loadSprites("s_idle");
		s_walk = Loader.loadSprites("s_walk");
		s_jump = Loader.loadSprites("s_jump");
		s_fall = Loader.loadSprites("s_fall");
		s_attack = Loader.loadSprites("s_attack");
		s_face = Loader.loadTexture("sprites", "s_face");
		// natsuki
		n_idle = Loader.loadSprites("n_idle");
		n_walk = Loader.loadSprites("n_walk");
		n_jump = Loader.loadSprites("n_jump");
		n_fall = Loader.loadSprites("n_fall");
		n_attack = Loader.loadSprites("n_attack");
		n_face = Loader.loadTexture("sprites", "n_face");
		// yuri
		y_idle = Loader.loadSprites("y_idle");
		y_walk = Loader.loadSprites("y_walk");
		y_jump = Loader.loadSprites("y_jump");
		y_fall = Loader.loadSprites("y_fall");
		y_attack = Loader.loadSprites("y_attack", 72, 80);
		y_face = Loader.loadTexture("sprites", "y_face");
		// monika
		m_idle = Loader.loadSprites("m_idle");
		m_walk = Loader.loadSprites("m_walk");
		m_jump = Loader.loadSprites("m_jump");
		m_fall = Loader.loadSprites("m_fall");
		m_attack = Loader.loadSprites("m_attack");
		light = Loader.loadTextureTGA("sprites", "light");
		m_face = Loader.loadTexture("sprites", "m_face");
		// end sprites
		end_sprites = new Texture[4];
		end_sprites[0] = Loader.loadTexture("sprites", "s_sprite_end");
		end_sprites[1] = Loader.loadTexture("sprites", "n_sprite_end");
		end_sprites[2] = Loader.loadTexture("sprites", "y_sprite_end");
		end_sprites[3] = Loader.loadTexture("sprites", "m_sprite_end");
		// LOAD ITEM SPRITES
		// load heal items
		bottle = Loader.loadSprites("bottle", 30, 60);
		cupcake = Loader.loadSprites("cupcake", 45, 54);
		tea = Loader.loadSprites("tea", 45, 54);
		coffee = Loader.loadSprites("coffee", 45, 45);
		// load win items
		cookie = Loader.loadTexture("sprites", "cookie");
		// manga = Loader.loadTexture("sprites", "manga");
		markov = Loader.loadTexture("sprites", "markov");
		pen = Loader.loadTexture("sprites", "pen");
		// LOAD ENEMY SPRITES
		// rainclouds
		raincloud_1 = Loader.loadSprites("raincloud1", 60, 60);
		raincloud_2 = Loader.loadSprites("raincloud2", 60, 60);
		raincloud_1_death = Loader.loadSprites("raincloud1_death", 60, 60);
		raincloud_2_death = Loader.loadSprites("raincloud2_death", 60, 60);
		// spiders
		spider_1 = new Texture[6];
		spider_1_glitch = new Texture[6];
		for (int i = 0; i < spider_1.length; i++) {
			spider_1[i] = Loader.loadTextureTGA("sprites/crawler", "c" + i);
			spider_1_glitch[i] = Loader.loadTextureTGA("sprites/crawler", "c" + i + "g");
		}
		spider_1_death = new Texture[1];
		spider_1_death[0] = Loader.loadTextureTGA("sprites/crawler", "dead");
		spider_2 = new Texture[6];
		spider_2_glitch = new Texture[6];
		for (int i = 0; i < spider_2.length; i++) {
			spider_2[i] = Loader.loadTextureTGA("sprites/climber", "c" + i);
			spider_2_glitch[i] = Loader.loadTextureTGA("sprites/climber", "c" + i + "g");
		}
		spider_2_death = new Texture[1];
		spider_2_death[0] = Loader.loadTextureTGA("sprites/climber", "dead");
		// ghosts
		ghost_1 = new Texture[4];
		ghost_1[0] = Loader.loadTextureTGA("sprites", "y_ghost_walk_0");
		ghost_1[1] = Loader.loadTextureTGA("sprites", "y_ghost_walk_1");
		ghost_1[2] = Loader.loadTextureTGA("sprites", "y_ghost_walk_2");
		ghost_1[3] = Loader.loadTextureTGA("sprites", "y_ghost_walk_1");
		ghost_2 = new Texture[4];
		ghost_2[0] = Loader.loadTextureTGA("sprites", "y_ghost_walk2_0");
		ghost_2[1] = Loader.loadTextureTGA("sprites", "y_ghost_walk2_1");
		ghost_2[2] = Loader.loadTextureTGA("sprites", "y_ghost_walk2_2");
		ghost_2[3] = Loader.loadTextureTGA("sprites", "y_ghost_walk2_1");
		// glitch enemies
		raincloud_1_glitch = Loader.loadSprites("raincloud_1_glitch", 60, 60);
		raincloud_2_glitch = Loader.loadSprites("raincloud_2_glitch", 60, 60);
		ghost_1_glitch = new Texture[4];
		ghost_1_glitch[0] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk_0");
		ghost_1_glitch[1] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk_1");
		ghost_1_glitch[2] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk_2");
		ghost_1_glitch[3] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk_1");
		ghost_2_glitch = new Texture[4];
		ghost_2_glitch[0] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk2_0");
		ghost_2_glitch[1] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk2_1");
		ghost_2_glitch[2] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk2_2");
		ghost_2_glitch[3] = Loader.loadTextureTGA("sprites", "y_ghost_glitch_walk2_1");
		glitch = Loader.loadSprites("glitch", 60, 60);
		// SPECIAL
		wall = Loader.loadTextureTGA("sprites", "wall");
		horn = Loader.loadTexture("sprites", "horn");
		// PL TEXTURES
		/*
		plYuri = new Texture[7][];
		plYuri[0] = Loader.loadSprites("pl_idle");
		plYuri[1] = Loader.loadSprites("pl_walk");
		plYuri[2] = Loader.loadSprites("pl_jump");
		plYuri[3] = Loader.loadSprites("pl_fall");
		//plYuri[4] = Loader.loadSprites("pl_attack", 72, 80);
		pl_tiles = Loader.loadTileset("pl_tiles", 64);
		plHUD = new Texture [10];
		plHUD[0] = Loader.loadTexture("pl/hud", "box");
		teleportEffect = Loader.loadSprites("effects", "teleport_effect", 180, 180);
		teleportTrail = Loader.loadTextureTGA("sprites", "pl_teleport");
		*/
		
		// EXPANSION ENTITIES
		// teleporters
		//teleport_1 = Loader.loadSprites("/expansion/teleport_1", 60, 60);
		//teleport_2 = Loader.loadSprites("/expansion/teleport_2", 60, 60);
		// EXPANSION EFFECTS
		// menu
		star = Loader.loadTexture("menu", "star");
		star_yellow = Loader.loadTexture("menu", "star_yellow");
		menu_sprites = new Texture[5];
		menu_sprites[0] = Loader.loadTexture("menu/sprites", "s_jump_menu");
		menu_sprites[1] = Loader.loadTexture("menu/sprites", "n_jump_menu");
		menu_sprites[2] = Loader.loadTexture("menu/sprites", "y_jump_menu");
		menu_sprites[3] = Loader.loadTexture("menu/sprites", "m_jump_menu");
		menu_sprites[4] = Loader.loadTexture("menu/sprites", "mr_jump_menu");
		// character glitch sprites
		//s_glitch = Loader.loadGlitchSprites("s_glitch");
		//n_glitch = Loader.loadGlitchSprites("n_glitch");
		//y_glitch = Loader.loadGlitchSprites("y_glitch");
		//m_glitch = Loader.loadGlitchSprites("m_glitch");
		//mr_glitch = Loader.loadGlitchSprites("mr_glitch");
		// in-game
		lightning = new Texture[4];
		lightning[0] = Loader.loadTexture("effects/expansion/lightning", "lightning_frame_1");
		lightning[1] = Loader.loadTexture("effects/expansion/lightning", "lightning_frame_2");
		lightning[2] = Loader.loadTexture("effects/expansion/lightning", "lightning_frame_3");
		lightning[3] = Loader.loadTexture("effects/expansion/lightning", "lightning_frame_4");
		
		// WINTER THEME
		s_snow_level = Loader.loadTileset("s_snow_level", 60);
		n_snow_level = Loader.loadTileset("n_snow_level", 60);
		y_snow_level = Loader.loadTileset("y_snow_level", 60);
		s_snow_bg = Loader.loadTexture("backgrounds", "s_snow_level");
		n_snow_bg = Loader.loadTexture("backgrounds", "n_snow_level");
		snowflakes = Loader.loadSprites("effects/winter", "snow", 8, 8);
		snowflakes_2 = Loader.loadSprites("effects/winter", "snow_2", 10, 10);
		
		// CHALLENGES
		challenge_title = Loader.loadTexture("menu/challenges", "challenge_title");
		challenge_thumbnails = new Texture[5];
		challenge_thumbnails[0] = Loader.loadTexture("menu/challenges", "chase_challenge");
		challenge_thumbnails[1] = Loader.loadTexture("menu/challenges", "jump_challenge");
		challenge_thumbnails[2] = Loader.loadTexture("menu/challenges", "torture_challenge");
		challenge_thumbnails[3] = Loader.loadTexture("menu/challenges", "locked_challenge");
		challenge_thumbnails[4] = Loader.loadTexture("menu/challenges", "locked_challenge_final");
		// challenge menu
		challenge_popup_base = Loader.loadTexture("menu/challenges", "popup_base");
		challenge_popup_titles = new Texture[3];
		challenge_popup_titles[0] = Loader.loadTexture("menu/challenges", "chase_title");
		challenge_popup_titles[1] = Loader.loadTexture("menu/challenges", "jump_title");
		challenge_popup_titles[2] = Loader.loadTexture("menu/challenges", "torture_title");
		challenge_popup_previews = new Texture[3];
		challenge_popup_previews[0] = Loader.loadTexture("menu/challenges", "chase_challenge_thumbnail");
		challenge_popup_previews[1] = Loader.loadTexture("menu/challenges", "jump_challenge_thumbnail");
		challenge_popup_previews[2] = Loader.loadTexture("menu/challenges", "torture_challenge_thumbnail");
		// challenge-specific
		portal = Loader.loadSprites("portal", 180, 180);
		portal_outline = Loader.loadTexture("sprites", "portal_outline");
		
		// << MINIGAME: YURI'S FRIGHT - CANCELLED >>
		/*
		 * I was going to make this originally, but after some issues with Slick's audio engine,
		 * I decided it wasn't going to work. Plus the pathfinding I was going to use is still a
		 * bit over my head at this point. I'm too lazy to gut all of this, so I'll leave it here,
		 * but I'll comment out the loading of unused sprites when I finish (if I remember) to keep
		 * loading time to a minimum. It'll only help by a few milliseconds for most people though.
		 */
		
		// EFFECTS
		
		// persistent
		//dark_vignette = Loader.loadTexture("effects/minigame", "dark_vignette");
		//darkness = Loader.loadTexture("effects/minigame", "darkness");
		
		// recurring
		//flash = Loader.loadTextureTGA("effects/minigame", "flash_2");
		
		// dynamic
		raindrop_2 = new Texture[1];
		raindrop_2[0] = Loader.loadTextureTGA("effects/minigame", "raindrop_2");
		raindrop_land_2 = new Texture[4];
		raindrop_land_2[0] = Loader.loadTextureTGA("effects/minigame", "raindrop_land_2_1");
		raindrop_land_2[1] = Loader.loadTextureTGA("effects/minigame", "raindrop_land_2_2");
		raindrop_land_2[2] = Loader.loadTextureTGA("effects/minigame", "raindrop_land_2_3");
		raindrop_land_2[3] = Loader.loadTextureTGA("effects/minigame", "raindrop_land_2_4");
		
		// screen
		//blood_spatter_right = Loader.loadSprites("effects/minigame", "blood_right", 512, 512);
		//blood_spatter_left = Loader.loadSprites("effects/minigame", "blood_left", 512, 512);
		
		// SPRITES
		//enemies
		y_ghost_idle = Loader.loadSprites("minigame/y_ghost_idle");
		y_ghost_walk = Loader.loadSprites("minigame/y_ghost_walk");
		y_ghost_jump = Loader.loadSprites("minigame/y_ghost_jump");
		y_ghost_fall = Loader.loadSprites("minigame/y_ghost_fall");
		y_ghost_attack = new Texture[1];
		y_ghost_attack[0] = Loader.loadTextureTGA("sprites/minigame", "y_ghost_attack");
		y_ghost_idle_glitch = Loader.loadGlitchTextures("sprites/minigame/y_ghost_idle_glitch", "y_ghost_idle_glitch");
		y_ghost_jump_glitch = Loader.loadGlitchTextures("sprites/minigame/y_ghost_jump_glitch", "y_ghost_jump_glitch");
		y_ghost_fall_glitch = Loader.loadGlitchTextures("sprites/minigame/y_ghost_fall_glitch", "y_ghost_fall_glitch");
		y_ghost_attack_glitch = Loader.loadGlitchTextures("sprites/minigame/y_ghost_attack_glitch", "y_ghost_attack_glitch");
		y_ghost_walk_glitch = new Texture[4][];
		y_ghost_walk_glitch[0] = y_ghost_jump_glitch;
		y_ghost_walk_glitch[1] = y_ghost_idle_glitch;
		y_ghost_walk_glitch[2] = y_ghost_fall_glitch;
		y_ghost_walk_glitch[3] = y_ghost_idle_glitch;

		// monika ghost
		m_ghost_idle = Loader.loadSprites("minigame/m_ghost_idle");
		m_ghost_walk = Loader.loadSprites("minigame/m_ghost_walk");
		m_ghost_jump = Loader.loadSprites("minigame/m_ghost_jump");
		m_ghost_fall = Loader.loadSprites("minigame/m_ghost_fall");
		m_ghost_attack = new Texture[1];
		m_ghost_attack[0] = Loader.loadTextureTGA("sprites/minigame", "m_ghost_attack");
		m_ghost_idle_glitch = Loader.loadGlitchTextures("sprites/minigame/m_ghost_idle_glitch", "m_ghost_idle_glitch");
		m_ghost_jump_glitch = Loader.loadGlitchTextures("sprites/minigame/m_ghost_jump_glitch", "m_ghost_jump_glitch");
		m_ghost_fall_glitch = Loader.loadGlitchTextures("sprites/minigame/m_ghost_fall_glitch", "m_ghost_fall_glitch");
		m_ghost_attack_glitch = Loader.loadGlitchTextures("sprites/minigame/m_ghost_attack_glitch", "m_ghost_attack_glitch");
		m_ghost_walk_glitch = new Texture[4][];
		m_ghost_walk_glitch[0] = m_ghost_jump_glitch;
		m_ghost_walk_glitch[1] = m_ghost_idle_glitch;
		m_ghost_walk_glitch[2] = m_ghost_fall_glitch;
		m_ghost_walk_glitch[3] = m_ghost_idle_glitch;
	}
}