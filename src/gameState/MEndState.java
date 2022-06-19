package gameState;

import static main.Render.drawImage;

import java.util.ArrayList;

import effects.JumpProgressMeter;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import cutscene.Camera;
import effects.SnowEffect;
import effects.TextEffect;
import entity.GlitchZone;
import entity.Player;
import entity.SpawnPoint;
import entity.WarpZone;
import entity.enemy.Enemy;
import entity.enemy.Glitch;
import entity.old.HealItem;
import gui.Dialogue;
import gui.DialogueLine;
import gui.HUD;
import main.Render;
import tile.TileMap;
import tile.VerticalTileMap;
import util.FadeManager;
import util.MathUtil;
import util.data.DataCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class MEndState extends PlayState {
	
	// map and camera
	private Camera camera;
	protected VerticalTileMap tilemap;
	
	// render
	private Texture background;
	private float bgY;
	
	// dialogue
	private Dialogue d1;
	private boolean d1Shown;
	private long d1Timer;
	
	// score update
	private long scoreTimer;
	private int scoreDelay;
	
	// glitch zone
	private GlitchZone glitchZone;

	// progress bar
	private JumpProgressMeter progressMeter;
	
	// CHALLENGE MODE
	private boolean challenge;
	
	public MEndState(GameStateManager gsm, boolean challenge) {
		super(gsm, "m_end");
		this.challenge = challenge;
		Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1); // just in case
		// override
		DataCache.level = 3;
		tilemap = new VerticalTileMap("m_end");
		tilemap.setMargins(120, 0);
		tilemap.setAllowBoundaryViolation(true);
		player = new Player(tilemap, 3);
		camera = new Camera(tilemap);
		camera.lockOnEntity(player);
		camera.snapToLock();
		camera.shake(Camera.SMOOTH, 20, 10000000.0f);
		hud = new HUD(player);
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		Music.resetVolume();
		Music.play(Music.M_END);
		FadeManager.fadeIn(1.0f, false);
		background = Textures.backgrounds[Textures.M_END];
		glitchZone = new GlitchZone(tilemap);
		initDialogue();
		initEntities();
		d1Timer = System.nanoTime();
		if (DataCache.m_end_dialogue || challenge) {
			d1Shown = true;
			glitchZone.start();
		}
		if (challenge) {
			DataCache.levelSelected = "jump_challenge";
		}
		zone = new WarpZone(720, 0, 180, 120);
		// fix snow
		if (effects[1] != null) {
			effects[1] = new SnowEffect(tilemap, player);
		}
		// progress bar
		progressMeter = new JumpProgressMeter(tilemap, player);
	}
	
	private void initDialogue() {
		DialogueLine[] d1Lines = new DialogueLine[3];
		d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The safe zone should be just at the top of this area!");
		d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Hurry, the collapse is starting!");
		d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Go as fast as you can, and be careful with the jumps!");
		d1 = new Dialogue(d1Lines);
	}
	
	private void initEntities() {
		healItems = new ArrayList<HealItem>();
		ArrayList<SpawnPoint> healSpawns = tilemap.getHealSpawns();
		if (!challenge) for (SpawnPoint p : healSpawns) { // no heals in challenge mode!
			healItems.add(new HealItem(tilemap, character, (int) p.getX(), (int) p.getY()));
		}
		else {
			player.setMaxHealth(1);
			DataCache.levelSelected = "jump_challenge";
		}
		enemies = new ArrayList<Enemy>();
		ArrayList<SpawnPoint> enemySpawns = tilemap.getEnemySpawns();
		for (SpawnPoint p : enemySpawns) enemies.add(new Glitch(tilemap, (int) p.getX(), (int) p.getY()));
	}
	
	private void scoreUpdate() {
		DataCache.score = "";
		int len = (int) (Math.random() * 7) + 3;
		String bank = "abcdefghijklmnopqrstuvwxyz0123456789`~@#$%^&*()-_=+/.,<>?;:'\"[{]}|\\";
		for (int i = 0; i < len; i++) {
			int index = MathUtil.randInt(bank.length());
			DataCache.score += bank.charAt(index);
		}
		scoreDelay = (int) (Math.random() * 4000) + 1000;
		scoreTimer = System.nanoTime();
	}
	
	public void update() {
		// update the progress bar
		progressMeter.update();
		// the rest of the update
		if (effects[1] != null) effects[1].update();
		if (!d1Shown) {
			long elapsed = (System.nanoTime() - d1Timer) / 1000000;
			if (elapsed > 1000) {
				d1.open();
				player.stop();
				d1Shown = true;
				DataCache.m_end_dialogue = true;
			}
		}
		if (d1.isOpen() || d1.isOpening()) {
			d1.update();
			player.idle();
		}
		else if (paused) {
			pauseMenu.update();
			return;
		}
		else if (player.isDead() && !DataCache.levelSelected.equals("s4")) {
			deadMenu.update();
			return;
		}
		else {
			player.update();
			glitchZone.update();
			long elapsed = (System.nanoTime() - scoreTimer) / 1000000;
			if (elapsed > scoreDelay) scoreUpdate();
			for (int i = 0; i < healItems.size(); i++) {
				HealItem h = healItems.get(i);
				h.update();
				if (player.intersects(h)) {
					player.heal();
					healItems.remove(i);
					i--;
					// generate random text effect
					String bank = "abcdefghijklmnopqrstuvwxyz0123456789`~@#$%^&*()-_=+/.,<>?;:'\"[{]}|\\";
					String text = "";
					for (int j = 0; j < 4; j++) {
						int index = (int) (Math.random() * bank.length());
						text += bank.charAt(index);
					}
					TextEffect t = new TextEffect(text, (TileMap) tilemap, (int) h.getX(), (int) h.getY());
					textEffects.add(t);
				}
			}
			for (Enemy e : enemies) {
				e.update();
				if (player.intersects(e)) {
					player.damage();
					if (player.isDead()) deadMenu.start();
				}
			}
			for (int i = 0; i < textEffects.size(); i++) {
				TextEffect t = textEffects.get(i);
				t.update();
				if (t.remove()) {
					textEffects.remove(i);
					i--;
				}
			}
			bgY = tilemap.getY() / 8;
			bgY %= Render.HEIGHT;
			if (zone != null) {
				if (zone.contains(player.getRectangle())) {
					zone = null;
					Music.fade(0, 2000);
					if (challenge) {
						FadeManager.fadeOut(2.0f, GameStateManager.WIN_STATE, true);
						DataCache.jump_complete = true;
					}
					else {
						FadeManager.fadeOut(2.0f, GameStateManager.M_EPILOGUE, true);
						DataCache.m_complete = true;
					}
				}
			}
		}
		camera.update();
		if (zone == null) return; // player cannot die if the level is completed
		if (player.getY() > tilemap.getMaxY() && !player.isDead()) {
			player.kill();
			deadMenu.start();
		}
		if (glitchZone.contains(player.getRectangle())) {
			player.kill();
			deadMenu.start();
		}
	}
	
	public void render() {
		drawImage(background, 0, bgY);
		drawImage(background, 0, bgY + Render.HEIGHT);
		tilemap.render();
		for (Enemy e : enemies) e.render();
		for (HealItem h : healItems) h.render();
		player.render();
		glitchZone.render();
		for (TextEffect t : textEffects) t.render();
		if (effects[0] != null) effects[0].render();
		if (effects[1] != null) effects[1].render();
		hud.render();
		if (d1.isOpen() || d1.isOpening()) d1.render();
		if (player.isDead() && !DataCache.levelSelected.equals("s4")) {
			deadMenu.render();
		}
		else if (paused) {
			pauseMenu.render();
		}
		// draw the progress bar
		progressMeter.render();
	}
	
	public void handleInput() {
		// TEMP DEV CHEAT
		//if (KeyInput.isPressed(KeyInput.F)) player.setPosition(1000, 0);
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d1.next();
				if (d1.isClosing()) glitchZone.start();
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
				//checkAttack();
			}
			if (KeyInput.isPressed(KeyInput.ESC) && !win && !FadeManager.inProgress()) {
				paused = !paused;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
			}
		}
	}
}