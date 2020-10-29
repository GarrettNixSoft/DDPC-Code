package gameState;

import java.util.ArrayList;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import background.AnimatedBackground;
import cutscene.Trigger;
import effects.SnowEffect;
import effects.TextEffect;
import entity.Portal;
import entity.util.Collectible;
import entity.util.EntityHandler;
import event.GameEvent;
import gui.DeadMenu;
import gui.HUD2;
import gui.PauseMenu;
import minigame.GhostYuri;
import tile.MapLoader;
import util.FadeManager;
import util.InputData;
import util.MathUtil;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;

public class ChaseChallengeState extends PlayStateEP {
	
	// input
	private static final int DATA_COUNT = 75;
	private InputData[] inputData;
	private int dataIndex;
	
	// chase
	private GhostYuri ghost;
	
	// portal
	private Portal portal;
	
	// hud
	private HUD2 hud;
	
	// text effects
	private ArrayList<TextEffect> textEffects;
	
	// winter theme
	private SnowEffect snowEffect;
	
	public ChaseChallengeState(GameStateManager gsm) {
		super();
		this.gsm = gsm;
		textEffects = new ArrayList<TextEffect>();
		// music
		Music.resetVolume();
		Music.play(Music.Y_LEVEL);
		// fade
		FadeManager.fadeIn();
		// override tilemap
		mapLoader = new MapLoader();
		mapLoader.setWinterTheme(Settings.winterTheme);
		levels = mapLoader.loadLevel("challenge", this);
		entityLoaders = mapLoader.getEntities();
		entityHandler = new EntityHandler(textEffects);
		refreshEntities();
		refreshEntityHandler();
		events = new GameEvent[0][0];
		triggers = new Trigger[0][0];
		// background
		background = new AnimatedBackground(Textures.backgrounds[3]);
		// can't take damage
		player.setMaxHealth(1);
		// ghost
		ghost = new GhostYuri(levels[currentLevel].tilemap);
		ghost.setPosition(player.getX(), player.getY());
		// portal
		float[] pos = MathUtil.center(levels[currentLevel].tilemap.getExit());
		portal = new Portal(levels[currentLevel].tilemap, pos[0], pos[1]);
		System.out.println("[ChaseChallengeState] Portal position set to (" + (int) MathUtil.center(levels[currentLevel].tilemap.getExit())[0] + "," + (int) MathUtil.center(levels[currentLevel].tilemap.getExit())[1] + ")");
		// input data
		inputData = new InputData[DATA_COUNT];
		// hud
		hud = new HUD2(player);
		// menus
		pauseMenu = new PauseMenu(gsm, this);
		deadMenu = new DeadMenu(gsm);
		// make sure camera is correct
		refreshCamera();
		camera.snapToLock();
		// winter theme
		if (Settings.winterTheme) snowEffect = new SnowEffect(levels[currentLevel].tilemap, player);
		// quick game patches
		DataCache.character = 2;
		DataCache.levelSelected = "chase_challenge";
		DataCache.score = "0";
		entityHandler.setCollectibles(new ArrayList<Collectible>());
		player.invincible((int) (1000 / 60.0 * DATA_COUNT));
	}
	
	// NEW METHODS
	private void checkGhostCollision() {
		if (player.intersects(ghost)) {
			if (player.damage())
				deadMenu.start();
		}
	}
	
	// OVERRIDE METHODS
	@Override
	protected void checkNextArea() {
		java.awt.Rectangle r = levels[currentLevel].tilemap.getExit();
		if (r.contains(player.getRectangle())) {
			if (transitioning) return;
			if (levels[currentLevel].tilemap.isLast()) {
				CURRENT_STATE = FADE;
				FadeManager.fadeOut(2.0f, GameStateManager.WIN_STATE, true);
				Music.fade(0, 2000);
				DataCache.chase_complete = true;
				return;
			}
			transitionTimer = System.nanoTime();
			transitioning = true;
			CURRENT_STATE = TRANSITION;
			FadeManager.fadeOut(1.1f, false);
		}
	}
	
	public void update() {
		switch (CURRENT_STATE) {
		case PLAY:
			if (Settings.winterTheme) snowEffect.update();
			// menus
			if (paused) {
				pauseMenu.update();
				return;
			}
			else if (player.isDead()) {
				deadMenu.update();
				return;
			}
			// update ghost
			ghost.update();
			// update everything else
			//	super.update();
			// update background
			background.update();
			// update tilemap
			levels[currentLevel].tilemap.update();
			// update entities
			entityHandler.update();
			if (player.getY() > levels[currentLevel].tilemap.getHeight() + 100) player.kill();
			if (player.isDead()) deadMenu.start();
			// move camera
			camera.update();
			checkNextArea();
			// check ghost collision
			checkGhostCollision();
			// update portal
			portal.update();
			// update text effects
			for (TextEffect te : textEffects) te.update();
			break;
		case TRANSITION:
			if (Settings.winterTheme) snowEffect.update();
			// update background
			background.update();
			// move camera
			camera.update();
			// update tilemap
			levels[currentLevel].tilemap.update();
			// update ghost
			ghost.update();
			// update portal
			portal.update();
			// update text effects
			for (TextEffect te : textEffects) te.update();
			// check finished
			long elapsed = (System.nanoTime() - transitionTimer) / 1000000;
			if (elapsed > 1000) {
				currentLevel++;
				CURRENT_STATE = PLAY;
				transitioning = false;
				refreshEntities();
				entityHandler.setCollectibles(new ArrayList<Collectible>());
				refreshDataCache();
				refreshCamera();
				player.setMaxHealth(1);
				player.invincible((int) (1000 / 60.0 * DATA_COUNT));
				ghost = new GhostYuri(levels[currentLevel].tilemap);
				ghost.setPosition(player.getX(), player.getY());
				inputData = new InputData[DATA_COUNT];
				float[] pos = MathUtil.center(levels[currentLevel].tilemap.getExit());
				portal = new Portal(levels[currentLevel].tilemap, pos[0], pos[1]);
				portal.setPosition(MathUtil.center(levels[currentLevel].tilemap.getExit()));
				if (Settings.winterTheme) snowEffect = new SnowEffect(levels[currentLevel].tilemap, player);
				FadeManager.fadeIn(1.0f, false);
				System.out.println("[PlayStateCH2] Transition finished");
			}
			break;
		case FADE:
			if (Settings.winterTheme) snowEffect.update();
			// update ghost
			ghost.update();
			// update background
			background.update();
			// move camera
			camera.update();
			// update tilemap
			levels[currentLevel].tilemap.update();
			// update entities
			entityHandler.updateInteractives();
			// update portal
			portal.update();
			// update text effects
			for (TextEffect te : textEffects) te.update();
			break;
		}
		
	}
	
	public void render() {
		background.render();
		levels[currentLevel].tilemap.render();
		portal.render();
		entityHandler.render();
		ghost.render();
		if (Settings.winterTheme) snowEffect.render();
		for (TextEffect te : textEffects) te.render();
		hud.render();
		if (player.isDead()) {
			deadMenu.render();
		}
		else if (paused) {
			pauseMenu.render();
		}
		//Render.drawOutline(levels[currentLevel].tilemap.getExitScreen(), Color.yellow);
	}
	
	public void handleInput() {
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
			if (KeyInput.isPressed(KeyInput.ESC) && !FadeManager.inProgress()) {
				paused = !paused;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
			}
			player.setJumping(KeyInput.isDown(KeyInput.W) || KeyInput.isDown(KeyInput.UP));
			player.setLeft(KeyInput.isDown(KeyInput.A) || KeyInput.isDown(KeyInput.LEFT));
			player.setRight(KeyInput.isDown(KeyInput.D) || KeyInput.isDown(KeyInput.RIGHT));
			if (KeyInput.isDown(KeyInput.SPACE) || MouseInput.isPressed(MouseInput.LEFT)) player.setAttacking();
			//if (KeyInput.isPressed(KeyInput.R)) gsm.setState(GameStateManager.CHASE_CHALLENGE_STATE);
			//if (KeyInput.isPressed(KeyInput.F)) player.setPosition(2600, 60);
			//if (KeyInput.isPressed(KeyInput.H)) player.heal();
			processInputData();
		}
	}
	
	private void processInputData() {
		// process data
		InputData data = new InputData();
		data.W          = KeyInput.isDown(KeyInput.W);
		data.A          = KeyInput.isDown(KeyInput.A);
		data.S          = KeyInput.isDown(KeyInput.S);
		data.D          = KeyInput.isDown(KeyInput.D);
		data.UP         = KeyInput.isDown(KeyInput.UP);
		data.DOWN       = KeyInput.isDown(KeyInput.DOWN);
		data.LEFT       = KeyInput.isDown(KeyInput.LEFT);
		data.RIGHT      = KeyInput.isDown(KeyInput.RIGHT);
		data.SPACE      = KeyInput.isDown(KeyInput.SPACE);
		data.LEFT_CLICK = MouseInput.isPressed(MouseInput.LEFT);
		data.locationData = player.getLocationData();
		if (dataIndex % 5 == 0) data.locationData.use = true;
		inputData[dataIndex] = data;
		// send data to ghost
		int sendIndex = (dataIndex + 1) % inputData.length;
		ghost.processData(inputData[sendIndex]);
		// DEBUG OUTPUT
		//System.out.printf("[ChaseChallengeState] InputData[%03d] = " + inputData[dataIndex] + "\n", dataIndex);
		// continue
		dataIndex++;
		if (dataIndex >= inputData.length) dataIndex = 0;
	}
	
}