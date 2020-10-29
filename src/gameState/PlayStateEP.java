package gameState;

import static main.Render.drawString;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import assets.Music;
import background.AnimatedBackground;
import background.ElementIDs;
import cutscene.Camera;
import cutscene.Cutscene;
import cutscene.TeleporterEvent;
import cutscene.Trigger;
import entity.Character;
import entity.Entity;
import entity.PlayerEnhanced;
import entity.PlayerV2;
import entity.Projectile;
import entity.Teleporter;
import entity.Teleporter2Way;
import entity.enemy.Enemy;
import entity.util.EntityHandler;
import entity.util.EntityLoader;
import event.EventTrigger;
import event.GameEvent;
import gui.DeadMenu;
import gui.PauseMenu;
import main.Render;
import tile.MapLoader;
import util.FadeManager;
import util.data.DataCache;
import util.data.LevelCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class PlayStateEP extends GameState {
	
	// camera
	protected Camera camera;
	
	// background
	protected AnimatedBackground background;
	
	// map and entities
	protected LevelCache[] levels;
	protected int currentLevel;
	protected EntityLoader[] entityLoaders;
	protected MapLoader mapLoader;
	
	// player
	protected PlayerEnhanced player;
	
	// ENTITIES
	protected EntityHandler entityHandler;
	
	// update
	protected int CURRENT_STATE;
	public static final int PLAY = 0;
	public static final int FADE = 1;
	public static final int CUTSCENE = 2;
	public static final int TRANSITION = 3;
	
	// area transition
	protected boolean transitioning;
	protected long transitionTimer;
	
	// cutscenes
	protected int currentCutscene;
	protected Cutscene[][] cutscenes;
	protected Trigger[][] triggers;
	
	// events
	protected EventTrigger[][] evtTriggers;
	protected GameEvent[][] events;
	
	// menus
	protected PauseMenu pauseMenu;
	protected DeadMenu deadMenu;
	protected boolean paused;
	
	// debug
	protected boolean debug;
	
	public PlayStateEP(GameStateManager gsm) {
		this.gsm = gsm;
		mapLoader = new MapLoader();
		levels = mapLoader.loadLevel(DataCache.levelSelected, this);
		entityLoaders = mapLoader.getEntities();
		entityHandler = new EntityHandler();
		refreshEntityHandler();
		refreshCamera();
		levelSetup();
		camera.snapToLock();
		// default music
		Music.resetVolume();
		Music.play(Music.GLITCH);
		// set data cache
		refreshDataCache();
		// load cutscenes
		loadCutscenes();
		// load events
		loadEvents();
		// menus
		pauseMenu = new PauseMenu(gsm, this);
		deadMenu = new DeadMenu(gsm);
		// fade in
		FadeManager.fadeIn(2.0f);
		// transition inactive
		transitioning = false;
		transitionTimer = -1;
	}
	
	public PlayStateEP() {
		// just skip all the BS because I need to extend this class
		// in order to use the multi-room system I set up
	}
	
	// SETUP
	protected void levelSetup() {
		// setup background
		String bgID = ElementIDs.getBackgroundID(DataCache.levelSelected);
		background = new AnimatedBackground(bgID, levels[currentLevel].tilemap);
		// setup entities
		refreshEntities();
	}
	
	// LOAD CUTSCENES
	protected void loadCutscenes() {
		cutscenes = mapLoader.getCutscenes();
		triggers = new Trigger[cutscenes.length][];
		for (int i = 0; i < cutscenes.length; i++) {
			triggers[i] = new Trigger[cutscenes[i].length];
			for (int j = 0; j < cutscenes[i].length; j++) {
				if (cutscenes[i][j] == null) System.out.println("[PlayStateCH2] null scene");
				triggers[i][j] = cutscenes[i][j].getTrigger();
				if (triggers[i][j] == null) {
					System.out.println("[PlayStateCH2] TRIGGER [" + i + "," + j + "] is null");
					System.exit(-1);
				}
				cutscenes[i][j].init();
			}
		}
	}
	
	// LOAD EVENTS
	protected void loadEvents() {
		events = mapLoader.getEvents();
		System.out.println("[PlayStateCH2] Received " + events.length + " event arrays from MapLoader");
		evtTriggers = new EventTrigger[events.length][];
		for (int i = 0; i < events.length; i++) {
			System.out.println("[PlayStateCH2] Event array [index=" + i + "] is length " + events[i].length);
			evtTriggers[i] = new EventTrigger[events[i].length];
			if (events[i].length == 0) continue;
			for (int j = 0; j < evtTriggers[i].length; j++) {
				if (events[i][j] == null) {
					System.out.println("[PlayStateCH2] EVENT [index=" + j + "] IS NULL");
					//continue;
				}
				evtTriggers[i][j] = events[i][j].getTrigger();
			}
		}
	}
	
	// GETTERS
	public EntityHandler getEntityHandler() { return entityHandler; }
	
	// ACTIONS
	public void spawn(Entity e) {
		if (e instanceof Character) {
			Character c = (Character) e;
			entityHandler.addCharacter(c, c.getCharacter());
		}
		if (e instanceof Enemy) {
			Enemy en = (Enemy) e;
			entityHandler.addEnemy(en);
		}
		refreshDataCache();
	}
	
	public void spawnOnPlayer(Entity e) {
		System.out.println("[PlayStateCH2] Entity [id=" + e + "] spawned at player");
		if (e instanceof Character) {
			Character c = (Character) e;
			c.setLocation((int) player.getX(), (int) player.getY());
			entityHandler.addCharacter(c, c.getCharacter());
		}
		if (e instanceof Enemy) {
			Enemy en = (Enemy) e;
			e.setPosition(player.getX(), player.getY());
			entityHandler.addEnemy(en);
		}
		refreshDataCache();
	}
	
	public void resume() {
		if (paused) paused = false;
	}
	
	// spawn an entity
	public void spawn(Entity e, int x, int y) {
		System.out.println("[PlayStateCH2] Entity [id=" + e + "] spawned at (" + x + "," + y + ")");
		if (e instanceof Character) {
			Character c = (Character) e;
			c.setLocation(x, y);
			entityHandler.addCharacter(c, c.getCharacter());
		}
		if (e instanceof Enemy) {
			Enemy en = (Enemy) e;
			e.setPosition(x, y);
			entityHandler.addEnemy(en);
		}
		refreshDataCache();
	}
	
	// spawn a teleporter
	public void spawnTeleporter(int type, int x1, int y1, int x2, int y2) {
		if (type == TeleporterEvent.ONE_WAY) entityHandler.addInteractive(new Teleporter(levels[currentLevel].tilemap, x1, y1, x2, y2));
		else if (type == TeleporterEvent.TWO_WAY) entityHandler.addInteractive(new Teleporter2Way(levels[currentLevel].tilemap, x1, y1, x2, y2));
	}
	
	// UPDATES
	protected void refreshEntities() {
		player = new PlayerV2(levels[currentLevel]);
		player.setPosition(levels[currentLevel].tilemap.getSpawnX(), levels[currentLevel].tilemap.getSpawnY());
		System.out.println("[PlayStateCH2] Player moved to " + player.getX() + ", " + player.getY());
		refreshEntityHandler();
	}
	
	protected void refreshEntityHandler() {
		// set to entities from current room
		// add player
		entityHandler.setPlayer(player);
		// add enemies
		entityHandler.setEnemies(entityLoaders[currentLevel].getEnemies());
		// add characters
		entityHandler.setCharacters(entityLoaders[currentLevel].getCharacters());
		// add interactives
		entityHandler.setInteractives(entityLoaders[currentLevel].getInteractives());
		// add collectibles
		entityHandler.setCollectibles(entityLoaders[currentLevel].getCollectibles());
		// add projectiles
		entityHandler.setProjectiles(new ArrayList<Projectile>());
	}
	
	// refresh data cache with entity data
	protected void refreshDataCache() {
		DataCache.player = player;
		DataCache.camera = camera;
		DataCache.characters = entityHandler.getCharacters();
		DataCache.winX = levels[currentLevel].tilemap.getWinX();
		DataCache.winY = levels[currentLevel].tilemap.getWinY();
	}
	
	protected void refreshCamera() {
		camera = levels[currentLevel].camera;
		camera.lockOnEntity(entityHandler.getPlayer());
		camera.snapToLock();
		//DataCache.camera = this.camera;
	}
	
	protected void checkAttack() {
		// check player attack on enemies
	}
	
	protected void checkNextArea() {
		java.awt.Rectangle r = levels[currentLevel].tilemap.getExit();
		if (r.contains(player.getRectangle())) {
			if (transitioning) return;
			if (levels[currentLevel].tilemap.isLast()) {
				CURRENT_STATE = FADE;
				FadeManager.fadeOut(2.0f, GameStateManager.WIN_STATE, true);
				return;
			}
			transitionTimer = System.nanoTime();
			transitioning = true;
			CURRENT_STATE = TRANSITION;
			FadeManager.fadeOut(1.1f, false);
		}
	}
	
	public void update() {
		switch(CURRENT_STATE) {
			case PLAY:
				// update background
				background.update();
				// update tilemap
				levels[currentLevel].tilemap.update();
				// update entities
				entityHandler.update();
				// move camera
				camera.update();
				checkNextArea();
				// check for game event
				if (events.length != 0) for (int i = 0; i < events[currentLevel].length; i++) {
					GameEvent ge = events[currentLevel][i];
					ge.update();
					if (ge.active() || ge.complete()) continue;
					else {
						EventTrigger trigger = ge.getTrigger();
						if (trigger.getArea().contains(player.getRectangle())) {
							ge.activate();
						}
					}
				}
				// check for cutscene start
				if (triggers.length != 0) for (int i = 0; i < triggers[currentLevel].length; i++) {
					Cutscene c = cutscenes[currentLevel][i];
					if (c.complete()) continue;
					else if (c.isDependent()) {
						if (cutscenes[currentLevel][c.getDependency()].complete()) {
							triggers[currentLevel][i].update();
							if (triggers[currentLevel][i].isActive()) {
								currentCutscene = i;
								CURRENT_STATE = CUTSCENE;
							}
						}
					}
					else {
						if (triggers[currentLevel] == null) {
							System.out.println("[PlayStateCH2] No trigger array found!");
							System.exit(-1);
						}
						triggers[currentLevel][i].update();
						if (triggers[currentLevel][i].isActive()) {
							currentCutscene = i;
							CURRENT_STATE = CUTSCENE;
						}
					}
				}
				if (triggers.length != 0) {
					if (triggers[currentLevel][currentCutscene] == null) break;
					triggers[currentLevel][currentCutscene].update();
					if (triggers[currentLevel][currentCutscene].isActive()) {
						CURRENT_STATE = CUTSCENE;
						triggers[currentLevel][currentCutscene] = null;
					}
				}
				break;
			case FADE:
				// update background
				background.update();
				// move camera
				camera.update();
				// update tilemap
				levels[currentLevel].tilemap.update();
				// update entities
				entityHandler.updateInteractives();
				break;
			case CUTSCENE:
				cutscenes[currentLevel][currentCutscene].update();
				levels[currentLevel].tilemap.update();
				background.update();
				// make player fall
				player.update();
				// update entities
				entityHandler.updateCharacters();
				entityHandler.updateEnemies();
				entityHandler.updateInteractives();
				camera.update();
				if (cutscenes[currentLevel][currentCutscene].complete()) {
					CURRENT_STATE = PLAY;
					camera.clearEntityLock();
					camera.lockOnEntity(player);
					System.out.println("[PlayStateCH2] End cutscene");
				}
				break;
			case TRANSITION:
				// update background
				background.update();
				// move camera
				camera.update();
				// update tilemap
				levels[currentLevel].tilemap.update();
				// check finished
				long elapsed = (System.nanoTime() - transitionTimer) / 1000000;
				if (elapsed > 1000) {
					currentLevel++;
					CURRENT_STATE = PLAY;
					transitioning = false;
					refreshEntities();
					refreshDataCache();
					refreshCamera();
					FadeManager.fadeIn(1.0f, false);
					System.out.println("[PlayStateCH2] Transition finished");
				}
				break;
		}
		
	}
	
	public void render() {
		// render background
		background.render();
		// render tilemap
		levels[currentLevel].tilemap.render();
		// render entities
		entityHandler.render();
		// render effects
		
		// render events
		if (events[currentLevel] == null) System.out.println("WHAT. THE. HELL.");
		for (GameEvent ge : events[currentLevel]) if (ge != null) ge.render();
		if (CURRENT_STATE == CUTSCENE) cutscenes[currentLevel][currentCutscene].render();
		if (debug) renderDebug();
	}
	
	protected void renderDebug() {
		drawString("Pos: (" + (int) player.getX() + "," + (int) player.getY() + ")", 10, 5);
		drawString("Attacking: " + player.isAttacking(), 10, 20);
		drawString("Cooldown: " + player.attackCooldown(), 10, 35);
		drawString("HP: " + player.getHealth(), 10, 50);
	}
	
	protected void handleInput() {
		if (KeyInput.isPressed(KeyInput.KEY_9)) debug = !debug;
		switch (CURRENT_STATE) {
		case PLAY:
			player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
			player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
			player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP));
			if (KeyInput.isPressed(KeyInput.F)) player.flyMode();
			if (MouseInput.isClicked(MouseInput.LEFT) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				// check interface
				// otherwise player attack
				player.setAttacking();
				checkAttack();
			}
			if (KeyInput.isPressed(KeyInput.R)) {
				player.setPosition(levels[currentLevel].tilemap.getSpawnX(), levels[currentLevel].tilemap.getSpawnY());
				levels[currentLevel].tilemap.setPositionAbsolute(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
				player.heal();
			}
			if (KeyInput.isPressed(KeyInput.KEY_0)) {
				player.setPosition(levels[currentLevel].tilemap.getExit().x, levels[currentLevel].tilemap.getExit().y);
			}
			break;
		case FADE:
			
			break;
		case CUTSCENE:
			
			break;
		case TRANSITION:
			
			break;
		}
		if (KeyInput.isPressed(KeyInput.P)) camera.shake(Camera.SMOOTH, 10, 3.0f);
	}
}