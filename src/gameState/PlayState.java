package gameState;

import static main.Render.drawStringShadowed;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import background.DynamicBackground;
import effects.BlizzardEffect;
import effects.BloodEffect;
import effects.PlayableEffect;
import effects.RainEffect;
import effects.SnowEffect;
import effects.TextEffect;
import effects.VisualEffect;
import entity.Player;
import entity.SpawnPoint;
import entity.WarpZone;
import entity.enemy.Climber;
import entity.enemy.Enemy;
import entity.enemy.Ghost;
import entity.enemy.RainCloud;
import entity.enemy.Spider;
import entity.old.HealItem;
import entity.old.Horn;
import entity.old.WinItem;
import gui.DeadMenu;
import gui.HUD;
import gui.PauseMenu;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.MathUtil;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class PlayState extends GameState {
	
	// background
	protected DynamicBackground background;
	
	// effect
	protected VisualEffect[] effects;
	protected ArrayList<TextEffect> textEffects;
	
	// map
	protected TileMap tilemap;
	
	// entities
	protected Player player;
	protected HUD hud;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<HealItem> healItems;
	protected WinItem winItem;
	
	// character/setup
	protected int character;
	
	// update
	protected PauseMenu pauseMenu;
	protected DeadMenu deadMenu;
	protected boolean paused;
	protected boolean win;
	
	// zones
	protected WarpZone zone;
	
	// exit
	protected long exitTimer;
	
	// debug
	protected boolean location;
	
	public PlayState(GameStateManager gsm, String level) {
		this.gsm = gsm;
		if (level.endsWith(".map")) tilemap = new TileMap(level, false);
		else tilemap = new TileMap(level);
		levelSetup(level);
		if (player == null) System.out.println("[PlayState] Player not initialized...");
		else hud = new HUD(player);
		background = new DynamicBackground(tilemap);
		initEntities(character);
		DataCache.score = "0";
		FadeManager.fadeIn(1.0f, true);
		Music.resetVolume();
		win = false;
		exitTimer = -1;
		// menus
		pauseMenu = new PauseMenu(gsm, this);
		deadMenu = new DeadMenu(gsm);
		// text effects
		textEffects = new ArrayList<TextEffect>();
		//OS.processCheck();
	}
	
	// dummy constructor that performs any non-critical setup steps
	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		// menus
		pauseMenu = new PauseMenu(gsm, this);
		deadMenu = new DeadMenu(gsm);
		// text effects
		textEffects = new ArrayList<TextEffect>();
	}
	
	private void levelSetup(String level) {
		if (level.equals("dummy")) return;
		System.out.println("[PlayState] Level setup: " + level);
		effects = new VisualEffect[2]; // 2 effect slots
		if (level.startsWith("s")) {
			DataCache.character = character = 0;
			if (level.equals("s4")) {
				character = 4;
				player = new Player(tilemap, character);
				if (Settings.winterTheme) {
					BlizzardEffect be = new BlizzardEffect();
					be.soundOn();
					effects[0] = be;
				}
				else {
					RainEffect re = new RainEffect(tilemap, player, true);
					re.soundOn();
					effects[0] = re;
				}
				zone = new WarpZone(11760, 5760, 180, 180);
			}
			else {
				Music.play(Music.S_LEVEL);
				player = new Player(tilemap, character);
			}
		}
		else if (level.startsWith("n")) {
			DataCache.character = character = 1;
			player = new Player(tilemap, character);
			Music.play(Music.N_LEVEL);
		}
		else if (level.startsWith("y")) {
			DataCache.character = character = 2;
			player = new Player(tilemap, character);
			Music.play(Music.Y_LEVEL);
			effects[0] = new BloodEffect();
		}
		else if (level.startsWith("m")) {
			DataCache.character = character = 3;
			player = new Player(tilemap, character);
			switch (DataCache.level) {
				case 0:
					Music.play(Music.S_LEVEL_GLITCH);
					break;
				case 1:
					Music.play(Music.N_LEVEL_GLITCH);
					break;
				case 2:
					Music.play(Music.Y_LEVEL_GLITCH);
					break;
			}
		}
		else {
			switch (tilemap.getBgValue()) {
				case 0:
					DataCache.character = character = 0;
					Music.play(Music.S_LEVEL);
					break;
				case 1:
					DataCache.character = character = 1;
					Music.play(Music.N_LEVEL);
					break;
				case 2:
					DataCache.character = character = 2;
					Music.play(Music.Y_LEVEL);
					effects[0] = new BloodEffect();
					break;
				case 3:
					DataCache.character = character = 0;
					effects[0] = new RainEffect(tilemap, player, true);
					zone = new WarpZone(11760, 5760, 180, 180);
					character = 4;
					Music.play(Music.THUNDER);
					break;
				case 4:
					DataCache.character = character = 3;
					Music.play(Music.S_LEVEL);
					break;
				case 5:
					DataCache.character = character = 3;
					Music.play(Music.N_LEVEL);
					break;
				case 6:
					DataCache.character = character = 3;
					Music.play(Music.Y_LEVEL);
					break;
			}
		}
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		if (effects[1] == null && !(effects[0] instanceof RainEffect || effects[0] instanceof BlizzardEffect) && Settings.winterTheme) {
			SnowEffect se = new SnowEffect(tilemap, player);
			effects[1] = se;
		}
	}
	
	private void initEntities(int character) {
		healItems = new ArrayList<HealItem>();
		ArrayList<SpawnPoint> healSpawns = tilemap.getHealSpawns();
		for (SpawnPoint p : healSpawns) {
			healItems.add(new HealItem(tilemap, character, (int) p.getX(), (int) p.getY()));
		}
		enemies = new ArrayList<Enemy>();
		ArrayList<SpawnPoint> enemySpawns = tilemap.getEnemySpawns();
		for (SpawnPoint p : enemySpawns) {
			switch (character) {
				case 0: // sayori
					enemies.add(new RainCloud(tilemap, p.getType(), (int) p.getX(), (int) p.getY()));
					break;
				case 1: // natsuki
					switch (p.getType()) {
						case 0: // crawler
							enemies.add(new Spider(tilemap, (int) p.getX(), (int) p.getY(), 0));
							break;
						case 1: // climber
							enemies.add(new Climber(tilemap, (int) p.getX(), (int) p.getY(), 0));
							break;
					}
					break;
				case 2: // yuri
					enemies.add(new Ghost(tilemap, (int) p.getType(), (int) p.getX(), (int) p.getY() - 10));
					break;
				case 3: // monika
					switch (DataCache.level) {
						case 0:
							enemies.add(new RainCloud(tilemap, p.getType() + 2, (int) p.getX(), (int) p.getY()));
							break;
						case 1:
							switch (p.getType()) {
								case 0: // crawler
									enemies.add(new Spider(tilemap, (int) p.getX(), (int) p.getY(), 1));
									break;
								case 1: // climber
									enemies.add(new Climber(tilemap, (int) p.getX(), (int) p.getY(), 1));
									break;
							}
							break;
						case 2:
							enemies.add(new Ghost(tilemap, p.getType() + 2, (int) p.getX(), (int) p.getY() - 10));
							break;
					}
					break;
				case 4: // sayori
					enemies.add(new RainCloud(tilemap, p.getType(), (int) p.getX(), (int) p.getY()));
					break;
			}
		}
		// horn
		if (character == 2 && DataCache.level == 3) {
			enemies.add(new Horn(tilemap, 90, 150));
		}
		// init winItem
		winItem = new WinItem(tilemap, character, tilemap.getWinX(), tilemap.getWinY());
	}
	
	protected void checkAttack() {
		Rectangle area = player.getAttackRect();
		for (Enemy e : enemies) {
			if (e.getRectangle().intersects(area) && !e.isDead()) {
				e.damage(player.getAttackDamage());
				if (e.isDead()) {
					TextEffect t = null;
					if (character == 3) {
						switch (DataCache.level) {
							case 0:
								t = new TextEffect("cloud.chr deleted", tilemap, (int) e.getX(), (int) e.getY());
								break;
							case 1:
								t = new TextEffect("spider.chr deleted", tilemap, (int) e.getX(), (int) e.getY());
								break;
							case 2:
								t = new TextEffect("ghost.chr deleted", tilemap, (int) e.getX(), (int) e.getY());
								break;
						}
					}
					else if (character == 4 && DataCache.level == 3 && !DataCache.s_complete) t = new TextEffect("hxppy thxughts", tilemap, (int) e.getX(), (int) e.getY());
					else t = new TextEffect("" + e.getValue(), tilemap, (int) e.getX(), (int) e.getY());
					textEffects.add(t);
					player.score(e.getValue());
				}
			}
		}
	}
	
	public void resume() {
		if (paused) paused = false;
	}
	
	protected void updateTextEffects() {
		for (int i = 0; i < textEffects.size(); i++) {
			TextEffect t = textEffects.get(i);
			t.update();
			if (t.remove()) {
				textEffects.remove(i);
				i--;
			}
		}
	}
	
	public void update() {
		if (exitTimer != -1) {
			long elapsed = (System.nanoTime() - exitTimer) / 1000000;
			if (elapsed > 2900) {
				// go to next state
				if (DataCache.levelSelected.equals("s4")) {
					gsm.setState(GameStateManager.S_DEATH_STATE);
				}
			}
		}
		if (paused) {
			pauseMenu.update();
			return;
		}
		else if (player.isDead() && !DataCache.levelSelected.equals("s4")) {
			deadMenu.update();
			return;
		}
		player.update();
		if (!win) {
			if (!winItem.notOnScreen()) winItem.update();
			if (player.intersects(winItem)) {
				winItem = null;
				win = true;
				player.idle();
				FadeManager.fadeOut(1.5f, GameStateManager.WIN_STATE, true);
				Music.fade(0f, 1500);
				DataCache.score = "" + player.getScore();
				// check if player progresses
				DataCache.checkProgression(character);
			}
		}
		tilemap.setPosition(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		background.update();
		// check if player falls out of the map
		if (player.getY() > tilemap.getHeight() + 100 && !player.isDead()) {
			player.kill();
			if (exitTimer == -1 && DataCache.levelSelected.equals("s4")) {
				FadeManager.fadeOut(3.0f, false);
				exitTimer = System.nanoTime();
			}
			else {
				deadMenu.start();
			}
		}
		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			// update enemy if it's close to player
			if (MathUtil.distance(player.getX(), player.getY(), e.getX(), e.getY()) < 800) {
				e.update();
				if (e.remove()) {
					enemies.remove(i);
					i--;
				}
				// if it's a climber, check for web hit and falling
				if (e instanceof Climber) {
					Climber c = (Climber) e;
					if (player.isAttacking()) {
						if (!player.getAttackRect().intersects(e.getRectangle())) {
							// player is attacking, but did not hit the spider
							if (player.getAttackRect().intersects(c.getWebRect()) && !c.isFalling()) c.fall();
						}
					}
					else if (!player.isAttacking() && e.intersects(player) && !e.isFlinching() && !e.isDead()) {
						player.damage();
						if (player.isDead() && exitTimer == -1 && DataCache.levelSelected.equals("s4")) {
							FadeManager.fadeOut(3.0f, false);
							exitTimer = System.nanoTime();
						}
						else if (player.isDead()) deadMenu.start();
					}
					if (c.landed()) {
						int xPos = (int) c.getX();
						int yPos = (int) c.getY();
						int hp = c.getHealth();
						enemies.remove(c);
						if (character == 3) enemies.add(new Spider(tilemap, xPos, yPos, hp, 1));
						else enemies.add(new Spider(tilemap, xPos, yPos, hp, 0));
					}
				}
				// if it's a ghost, check for player hit to play effect
				else if (e instanceof Ghost) {
					Ghost g = (Ghost) e;
					if (!player.isAttacking() && g.intersects(player) && !g.isFlinching() && !g.isDead()) {
						boolean b = player.damage();
						if (g.getTier() == 1) {
							PlayableEffect p = (PlayableEffect) effects[0];
							if (!p.isPlaying() && b) p.play();
						}
						if (player.isDead()) deadMenu.start();
					}
				}
				// otherwise just update it
				else {
					if (!player.isAttacking() && e.intersects(player) && !e.isFlinching() && !e.isDead()) {
						player.damage();
						if (player.isDead() && exitTimer == -1 && DataCache.levelSelected.equals("s4")) {
							FadeManager.fadeOut(3.0f, false);
							exitTimer = System.nanoTime();
						}
						else if (player.isDead()) deadMenu.start();
					}
				}
			}
		}
		// update heal items
		for (int i = 0; i < healItems.size(); i++) {
			HealItem h = healItems.get(i);
			if (!h.notOnScreen()) {
				h.update();
				if (player.intersects(h)) {
					player.heal();
					player.score(1000);
					healItems.remove(i);
					i--;
					TextEffect t = new TextEffect("1000", tilemap, (int) h.getX(), (int) h.getY());
					textEffects.add(t);
				}
			}
		}
		// update text effects
		updateTextEffects();
		// update warp zone if it exists
		if (zone != null) {
			if (DataCache.levelSelected.equals("s4")) {
				if (zone.contains(player.getRectangle())) {
					FadeManager.fadeOut(2.0f, GameStateManager.S_SAVE_STATE, false);
					DataCache.score = "" + player.getScore();
				}
			}
		}
		// update effect if it exists
		if (effects[0] != null) effects[0].update();
		if (effects[1] != null) effects[1].update();
	}
	
	public void render() {
		background.render();
		tilemap.render();
		for (Enemy e : enemies) e.render();
		for (HealItem h : healItems) h.render();
		if (!win) winItem.render();
		player.render();
		for (TextEffect t : textEffects) t.render();
		if (effects[0] != null) effects[0].render();
		if (effects[1] != null) effects[1].render();
		hud.render();
		if (player.isDead() && !DataCache.levelSelected.equals("s4")) {
			deadMenu.render();
		}
		else if (paused) {
			pauseMenu.render();
		}
		if (location) {
			drawStringShadowed("x=" + (int) player.getX() + ",y=" + (int) player.getY() + ", dy=" + (int) player.getDY(), Fonts.MENU_SMALLER, Color.white, player.getX() + tilemap.getX(), player.getY() + tilemap.getY() - 50, 2, 2);
			main.Render.drawOutline(player.getAttackRectScreen(), Color.red);
		}
		/*
		if (zone != null) {
			int x = (int) zone.getRect().getX();
			int y = (int) zone.getRect().getY();
			x += tilemap.getX();
			y += tilemap.getY();
			Render.drawRect(x, y, (int) zone.getRect().getWidth(), (int) zone.getRect().getHeight(), Color.yellow);
		}
		*/
	}
	
	protected void handleInput() {
		if (player.isDead()) {
			deadMenu.handleInput();
		}
		else if (paused) {
			pauseMenu.handleInput();
			if (KeyInput.isPressed(KeyInput.ESC)) {
				paused = false;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
			}
		}
		else {
			if (win) return;
			player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
			player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
			player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP));
			if (MouseInput.isClicked(MouseInput.LEFT) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				// check interface
				// otherwise player attack
				player.setAttacking();
				checkAttack();
			}
			if (KeyInput.isPressed(KeyInput.ESC) && !win && !FadeManager.inProgress()) {
				paused = !paused;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
			}
			if (KeyInput.isPressed(KeyInput.L)) {
				location = !location;
			}
			// TEMP
			if (KeyInput.isPressed(KeyInput.F)) {
				//OS.minimizeAll();
				//OS.restoreWindow();
				//OS.killExplorerTest();
				//FadeManager.fadeOut(120, GameStateManager.S_SAVE_STATE, false);
				//player.setPosition(winItem.getX(), winItem.getY());
				//Music.setPosition(71);
				//dialogue.open();
				//player.heal();
				//player.setPosition(tilemap.getWidth() - 100, 100);
				//FadeManager.fadeOut(120, GameStateManager.S_SAVE_STATE, false);
			}
		}
	}
}