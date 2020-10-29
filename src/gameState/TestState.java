package gameState;

import static main.Render.drawStringShadowed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import assets.Music;
import background.AnimatedBackground;
import background.ElementIDs;
import cutscene.Camera;
import effects.VisualEffect;
import entity.Character;
import entity.PlayerV2;
import entity.Projectile;
import entity.util.EntityHandler;
import entity.util.EntityLoader;
import gui.Dialogue;
import minigame.BloodSpatterEffect;
import minigame.GhostYuri;
import minigame.HorrorCombinedEffect;
import tile.DynamicTilemap;
import util.FadeManager;
import util.data.DataCache;
import util.data.LevelCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class TestState extends GameState {
	
	// bg
	private AnimatedBackground bg;
	
	// level effects
	private List<VisualEffect> levelEffects;
	
	// level
	private LevelCache lc;
	
	// CONTAINERS
	// cutscenes
	//private ArrayList<Cutscene> cutscenes = new ArrayList<Cutscene>();
	// events
	//private ArrayList<GameEvent> events = new ArrayList<GameEvent>();
	// entities
	private EntityLoader entityLoader;
	private EntityHandler entityHandler;
	
	// custom settings
	private boolean doCollisions;
	//private boolean doDamage;
	//private boolean doPickups;
	
	// custom render settings
	private boolean debugInfo;
	private boolean renderPlayer;
	private boolean renderCollectibles;
	//private boolean renderInteractives;
	private boolean renderCharacters;
	private boolean doEffects;
	
	// test dialogue
	private Dialogue dialogue;
	
	// test effects
	private BloodSpatterEffect blood;
	
	public TestState(GameStateManager gsm) {
		// gsm
		this.gsm = gsm;
		// level setup
		DataCache.character = 1;
		lc = new LevelCache();
		lc.tilemap = new DynamicTilemap("/maps/expansion/test/test/test.dmap");
		lc.camera = new Camera(lc.tilemap);
		lc.player = new PlayerV2(lc);
		lc.player.setPosition(lc.tilemap.getSpawnX(), lc.tilemap.getSpawnY());
		lc.characters = new Character[5];
		// entity setup
		entityLoader = new EntityLoader(lc.tilemap);
		entityLoader.load("test", "test");
		entityHandler = new EntityHandler();
		refreshEntityHandler();
		// background setup
		String bgID = ElementIDs.getBackgroundID("test");
		bg = new AnimatedBackground(bgID, lc.tilemap);
		// finish setup
		lc.camera.lockOnEntity(lc.player);
		lc.camera.snapToLock();
		//Music.fade(0, 2000);
		Music.stop();
		//MusicManager.resetAll();
		Music.play(Music.MINIGAME_BG);
		// custom configuration
		customSettings();
		customSetup();
		customEffects();
	}
	
	private void refreshEntityHandler() {
		// set to entities from current room
		// add player
		entityHandler.setPlayer(lc.player);
		// add enemies
		entityHandler.setEnemies(entityLoader.getEnemies());
		// add characters
		entityHandler.setCharacters(entityLoader.getCharacters());
		// add interactives
		entityHandler.setInteractives(entityLoader.getInteractives());
		// add collectibles
		entityHandler.setCollectibles(entityLoader.getCollectibles());
		// add projectiles
		entityHandler.setProjectiles(new ArrayList<Projectile>());
	}
	
	/*
	 * Use this method to set up the custom settings.
	 */
	private void customSettings() {
		// game settings
		doCollisions = true;
		//doDamage = true;
		//doPickups = true;
		// render settings
		debugInfo = false;
		renderPlayer = true;
		renderCollectibles = true;
		//renderInteractives = true;
		renderCharacters = true;
		doEffects = true;
	}
	
	/*
	 * Use this method to add any sort of configurations that need to be tested.
	 * I.e. spawning entities, adding level events/effects, etc. to test.
	 */
	private void customSetup() {
		//dialogue = new Dialogue("for_fwort");
		GhostYuri ghost = new GhostYuri(lc.tilemap);
		ghost.setPosition(lc.tilemap.getSpawnX() + 100, lc.tilemap.getSpawnY());
		ghost.setFacingRight(false);
		entityHandler.addEntity(ghost);
	}
	
	private void customEffects() {
		levelEffects = new ArrayList<VisualEffect>();
		HorrorCombinedEffect hce = new HorrorCombinedEffect(lc);
		hce.setPriority(1);
		levelEffects.add(hce);
		blood = new BloodSpatterEffect(BloodSpatterEffect.LEFT);
		blood.setPriority(0);
		levelEffects.add(blood);
		
		Collections.sort(levelEffects);
	}
	
	public void update() {
		bg.update();
		lc.camera.update();
		if (doEffects) for (VisualEffect ve : levelEffects) ve.update();
		if (dialogue != null && (dialogue.isOpen() || dialogue.isOpening())) {
			dialogue.update();
			return;
		}
		entityHandler.update();
		if (doCollisions) doCollisions();
	}
	
	public void render() {
		bg.render();
		lc.tilemap.render();
		if (renderCollectibles) entityHandler.renderCollectibles();
		if (renderCharacters) entityHandler.renderCharacters();
		if (renderPlayer) entityHandler.renderPlayer();
		entityHandler.renderEntities();
		if (doEffects) for (VisualEffect ve : levelEffects) ve.render();
		if (dialogue != null && (dialogue.isOpen() || dialogue.isOpening())) dialogue.render();
		if (debugInfo) renderDebug();
	}
	
	private void renderDebug() {
		drawStringShadowed("Pos: (" + (int) lc.player.getX() + "," + (int) lc.player.getY() + ")", 10, 5);
		drawStringShadowed("Tile: (" + (int) (lc.player.getX() / 60) + "," + (int) (lc.player.getY() / 60) + ")", 10, 20);
		drawStringShadowed("Attacking: " + lc.player.isAttacking(), 10, 35);
		drawStringShadowed("Cooldown: " + lc.player.attackCooldown(), 10, 50);
		drawStringShadowed("HP: " + lc.player.getHealth(), 10, 65);
		drawStringShadowed("Camera: (" + lc.camera.getX() + "," + lc.camera.getY() + ")", 10, 80);
	}
	
	protected void handleInput() {
		lc.player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
		lc.player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
		lc.player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP));
		if (MouseInput.isClicked(MouseInput.LEFT) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			// check interface
			// otherwise player attack
			if (!lc.player.attackCooldown()) {
				lc.player.setAttacking();
				checkAttack();
			}
		}
		// check return to menu
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && KeyInput.isPressed(KeyInput.ESC)) {
			gsm.setState(GameStateManager.MENU_STATE);
			Music.resetVolume();
			Music.play(Music.MENU);
			Music.setPosition(MenuState.SKIP_POSITION);
			FadeManager.fadeIn(1.0f, true);
		}
		if (dialogue != null && KeyInput.isPressed(KeyInput.L)) dialogue.open();
		if (dialogue != null && dialogue.isOpen()) {
			if (KeyInput.isPressed(KeyInput.SPACE) || KeyInput.isPressed(KeyInput.ENTER) || MouseInput.isClicked(0)) dialogue.advance();
		}
		// TEST
		if (KeyInput.isPressed(KeyInput.B)) {
			blood.reset();
			blood.play();
		}
		if (KeyInput.isPressed(KeyInput.P)) lc.camera.shake(Camera.SMOOTH, 5, 3.0f);
		if (KeyInput.isPressed(KeyInput.KEY_9)) renderPlayer = !renderPlayer;
		if (KeyInput.isPressed(KeyInput.KEY_0)) debugInfo = !debugInfo;
	}
	
	// custom update methods
	private void doCollisions() {
		
	}
	
	private void checkAttack() {
		//System.out.println("[TestState] Check attack");
	}
	
}