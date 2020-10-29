package gameState;

import static main.Render.drawImage;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import background.DynamicBackground;
import effects.DamageScareEffect;
import effects.SnowEffect;
import entity.GlitchWall;
import entity.Player;
import entity.SpawnPoint;
import entity.WarpZone;
import entity.enemy.Enemy;
import entity.enemy.Glitch;
import gui.Dialogue;
import gui.DialogueLine;
import gui.HUD;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class MFinalLevelState extends PlayState {
	
	// player
	private Player player;
	
	// background
	private DynamicBackground background;
	
	// background glitch
	private Texture grid;
	private boolean grid_on;
	private long timer;
	private int duration;
	private int delay;
	private float gridY;
	
	// wall
	private GlitchWall wall;
	
	// effect
	private DamageScareEffect effect;
	
	// score update
	private long scoreTimer;
	private int scoreDelay;
	
	// dialogue
	private Dialogue d1;
	private Dialogue d2;
	private boolean d1Shown;
	private boolean d2Shown;
	private long d2Timer = -1;
	
	// exit
	private boolean exit;
	
	public MFinalLevelState(GameStateManager gsm) {
		super(gsm, "m4");
		tilemap = new TileMap("m4");
		DataCache.character = 3;
		player = new Player(tilemap, 3);
		hud = new HUD(player);
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		background = new DynamicBackground(tilemap);
		grid = Textures.glitch_grid;
		wall = new GlitchWall(tilemap, 5970, 360);
		zone = new WarpZone(5910, 0, 90, 2000);
		Music.resetVolume();
		Music.play(Music.GLITCH);
		FadeManager.fadeIn(2.0f, false);
		effect = new DamageScareEffect();
		initDialogue();
		delay = 3500;
		duration = 500;
		initEntities();
		DataCache.score = "e%fga^&f";
		scoreTimer = System.nanoTime();
		if (Settings.winterTheme) effects[1] = new SnowEffect(tilemap, player);
	}
	
	private void scoreUpdate() {
		DataCache.score = "";
		int len = (int) (Math.random() * 7) + 3;
		String bank = "abcdefghijklmnopqrstuvwxyz0123456789`~@#$%^&*()-_=+/.,<>?;:'\"[{]}|\\";
		for (int i = 0; i < len; i++) {
			int index = (int) (Math.random() * bank.length());
			DataCache.score += bank.charAt(index);
		}
		scoreDelay = (int) (Math.random() * 4000) + 1000;
		scoreTimer = System.nanoTime();
	}
	
	private void initDialogue() {
		String user = System.getProperty("user.name");
		DialogueLine[] d1Lines = new DialogueLine[10];
		d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
		d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Uh oh...");
		d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "That barrier is between me and the safe zone.");
		d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I recognize it though... this isn't good.");
		d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "That's part of the game's level engine...");
		d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Breaking through it will cause the game to start collapsing immediately.");
		d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But I don't really have any other choice...");
		d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If I stay out here it'll just happen eventually anyway.");
		d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Guess we're just going to have to go for it, " + user + "!");
		d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Let's bust through and then get out of here as fast as we can!");
		d1 = new Dialogue(d1Lines);
		DialogueLine[] d2Lines = new DialogueLine[2];
		d2Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Alright, there it goes! The path is clear!");
		d2Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "There's no time to waste, I have to run!");
		d2 = new Dialogue(d2Lines);
	}
	
	private void initEntities() {
		enemies = new ArrayList<Enemy>();
		for (SpawnPoint s : tilemap.getEnemySpawns()) {
			enemies.add(new Glitch(tilemap, s.getX(), s.getY()));
		}
	}
	
	private void nextGrid() {
		delay = (int) (Math.random() * 10000) + 5000;
		duration = (int) (Math.random() * 1000) + 200;
		timer = System.nanoTime();
		System.out.println("[MFinalLevelState] next glitch in " + delay + "ms");
		System.out.println("[MFinalLevelState] it will last " + duration + "ms");
	}
	
	protected void checkAttack() {
		if (wall == null) return;
		Rectangle r = player.getAttackRect();
		if (r.intersects(wall.getRectangle())) {
			wall.damage(player.getAttackDamage());
		}
	}
	
	private void checkEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.intersects(player)) {
				player.damage();
				if (player.isDead()) {
					Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
					deadMenu.start();
				}
				else if (!effect.isPlaying()) effect.play();
			}
		}
	}
	
	public void update() {
		if (paused) {
			pauseMenu.update();
			return;
		}
		else if (player.isDead()) {
			deadMenu.update();
			return;
		}
		long elapsed = (System.nanoTime() - scoreTimer) / 1000000;
		if (elapsed > scoreDelay) scoreUpdate();
		background.update();
		if (grid_on) {
			elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > duration) {
				grid_on = false;
				gridY = 0;
				nextGrid();
				Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
			}
			else {
				gridY -= 2.5f;
			}
		}
		else if (wall != null) {
			elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > delay) {
				grid_on = true;
				timer = System.nanoTime();
				Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
			}
		}
		if (d1.isOpen() || d1.isOpening()) {
			d1.update();
			player.idle();
		}
		else if (d2.isOpen() || d2.isOpening()) {
			d2.update();
			player.idle();
		}
		else {
			player.update();
			for (Enemy e : enemies) {
				e.update();
			}
			checkEnemies();
			effect.update();
		}
		if (player.getY() > tilemap.getHeight() + 100 && !player.isDead()) {
			player.kill();
			deadMenu.start();
		}
		if (player.getX() > 5910 && !d2Shown) player.setPosition(5910, player.getY());
		tilemap.setPosition(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		if (player.getX() > 5600 && !d1Shown) {
			d1.open();
			player.stop();
			player.idle();
			d1Shown = true;
		}
		if (d2Timer != -1 && !d2Shown) {
			elapsed = (System.nanoTime() - d2Timer) / 1000000;
			if (elapsed > 1000) {
				d2.open();
				player.stop();
				player.idle();
				d2Shown = true;
			}
		}
		if (wall != null) {
			wall.update();
			if (wall.remove()) {
				wall = null;
				FadeManager.fadeIn(1.0f, true);
				Sfx.playSound(Sfx.TYPE_MISC, Sfx.WALL_BREAK);
				d2Timer = System.nanoTime();
			}
		}
		if (d2Shown && zone.contains(player.getRectangle()) && !exit) {
			FadeManager.fadeOut(1.0f, GameStateManager.M_END_STATE, false);
			exit = true;
		}
		if (effects[1] != null) effects[1].update();
	}
	
	public void render() {
		background.render();
		if (grid_on) {
			drawImage(grid, 0, gridY);
			drawImage(grid, 0, gridY + Render.HEIGHT);
		}
		tilemap.render();
		if (wall != null) wall.render();
		for (Enemy e : enemies) e.render();
		player.render();
		if (effects[1] != null) effects[1].render();
		hud.render();
		effect.render();
		if (d1.isOpen() || d1.isOpening()) d1.render();
		if (d2.isOpen() || d2.isOpening()) d2.render();
		if (player.isDead()) {
			deadMenu.render();
		}
		else if (paused) {
			pauseMenu.render();
		}
	}
	
	protected void handleInput() {
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d1.next();
			}
		}
		if (d2.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d2.next();
			}
		}
		if (player.isDead()) {
			deadMenu.handleInput();
		}
		else if (paused) {
			pauseMenu.handleInput();
			if (KeyInput.isPressed(KeyInput.ESC)) paused = false;
		}
		else {
			player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
			player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
			player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP));
			if (MouseInput.isClicked(MouseInput.LEFT) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				// player attack
				player.setAttacking();
				checkAttack();
			}
			if (KeyInput.isPressed(KeyInput.ESC) && !win && !FadeManager.inProgress()) {
				paused = !paused;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
			}
		}
	}
}