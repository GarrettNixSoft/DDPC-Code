package gameState;

import java.util.ArrayList;

import assets.Music;
import assets.Sfx;
import background.DynamicBackground;
import effects.SnowEffect;
import entity.Player;
import entity.SpawnPoint;
import entity.enemy.Enemy;
import entity.enemy.Glitch;
import entity.enemy.RainCloud;
import entity.old.WinItem;
import gui.Dialogue;
import gui.HUD;
import main.Loader;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.MathUtil;
import util.data.DataCache;
import util.data.SaveData;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class TortureChallengeState extends PlayState {
	
	// background
	private DynamicBackground background;
	
	// intro dialogue
	private Dialogue introDialogue;
	
	// attempt counter
	private int attempts;
	
	// win (like that'll ever happen)
	private boolean win;
	
	// winter theme
	private SnowEffect snowEffect;
	
	public TortureChallengeState(GameStateManager gsm) {
		super(gsm);
		// music
		Music.resetVolume();
		Music.play(Music.S_LEVEL);
		// fade in
		FadeManager.fadeIn(0.75f, false);
		// override tilemap
		tilemap = new TileMap("torture_challenge");
		// background
		DataCache.character = 0;
		background = new DynamicBackground(tilemap);
		// player
		player = new Player(tilemap, 0);
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		player.setMaxHealth(1);
		// enemies
		enemies = new ArrayList<Enemy>();
		for (SpawnPoint e : tilemap.getEnemySpawns()) {
			enemies.add(new RainCloud(tilemap, 0, e.getX(), e.getY()));
		}
		// win
		winItem = new WinItem(tilemap, character, tilemap.getWinX(), tilemap.getWinY());
		// add glitch barriers
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				enemies.add(new Glitch(tilemap, 510 + (i * 600) + (j * 60), 630 + (i * 120)));
			}
		}
		// HUD
		hud = new HUD(player);
		hud.setTortureChallenge(true);
		// load dialogue
		introDialogue = new Dialogue(Loader.loadDialogue("torture_challenge_intro"));
		// check if dialogue shown; if not, open it
		if (!DataCache.torture_intro_shown) introDialogue.open();
		// set DataCache
		DataCache.levelSelected = "torture_challenge";
		// winter theme
		if (Settings.winterTheme) snowEffect = new SnowEffect(tilemap, player);
	}
	
	private void updateEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (MathUtil.distance(player.getX(), player.getY(), e.getX(), e.getY()) < 800) {
				e.update();
				if (e.remove()) {
					enemies.remove(i);
					i--;
				}
			}
		}
	}
	
	private void checkCollisions() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (!player.isFlinching() && !e.isFlinching() && !e.isDead() && player.intersects(e)) {
				resetPlayer();
			}
		}
	}
	
	private void resetPlayer() {
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		player.healMax();
		Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.LOSE);
		attempts++;
		DataCache.attempts_total++;
		DataCache.attempts_current = attempts;
		SaveData.save();
	}
	
	@Override
	public void update() {
		if (paused) {
			pauseMenu.update();
			return;
		}
		// update text effects
		updateTextEffects();
		// update background
		background.update();
		// update tilemap
		tilemap.setPosition(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		// update player
		player.update();
		// check falling out
		if (player.getY() > tilemap.getHeight() + 100 && !player.isDead()) resetPlayer();
		// update enemies
		updateEnemies();
		// update win item
		if (winItem != null && !winItem.notOnScreen()) winItem.update();
		if (winItem != null && player.intersects(winItem)) {
			winItem = null;
			win = true;
			player.idle();
			FadeManager.fadeOut(1.5f, GameStateManager.WIN_STATE, true);
			Music.fade(0f, 1500);
			DataCache.score = "" + player.getScore();
			DataCache.torture_complete = true;
		}
		// check collisions
		checkCollisions();
		// update dialogue
		introDialogue.update();
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}
	
	@Override
	public void render() {
		// draw background
		background.render();
		// render tilemap
		tilemap.render();
		// render win item
		if (winItem != null) winItem.render();
		// render enemies
		for (Enemy e : enemies) e.render();
		// render player
		player.render();
		// winter theme
		if (snowEffect != null) snowEffect.render();
		// render dialogue
		introDialogue.render();
		if (paused) pauseMenu.render();
		// render HUD
		hud.render();
	}
	
	@Override
	public void handleInput() {
		if (introDialogue.isOpen()) {
			if (KeyInput.isPressed(KeyInput.SPACE) || KeyInput.isPressed(KeyInput.ENTER) || MouseInput.isClicked(MouseInput.LEFT)) {
				introDialogue.next();
				if (introDialogue.isClosing()) DataCache.torture_intro_shown = true;
			}
			//if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) introDialogue.skipAll();
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
			if (KeyInput.isPressed(KeyInput.ESC)) {
				paused = true;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
			}
			player.setJumping(KeyInput.isDown(KeyInput.W) || KeyInput.isDown(KeyInput.UP));
			player.setLeft(KeyInput.isDown(KeyInput.A) || KeyInput.isDown(KeyInput.LEFT));
			player.setRight(KeyInput.isDown(KeyInput.D) || KeyInput.isDown(KeyInput.RIGHT));
			if (KeyInput.isPressed(KeyInput.SPACE) || MouseInput.isClicked(MouseInput.LEFT) && !player.isAttacking()) {
				player.setAttacking();
				checkAttack();
			}
			// TEST
//			if (KeyInput.isPressed(KeyInput.R)) {
//				player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
//			}
//			if (KeyInput.isPressed(KeyInput.F)) {
//				if (player.isFly()) player.fly(false);
//				else player.fly(true);
//			}
		}
	}
	
}