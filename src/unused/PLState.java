package unused;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import assets.Music;
import gameState.GameState;
import gameState.GameStateManager;
import main.Render;
import util.input.KeyInput;

public class PLState extends GameState {
	
	// map
	private PLTileMap tilemap;
	
	// entities
	private PLPlayer player;
	//private ArrayList<PLGuard> guards;
	
	// hud
	private PLHUD hud;
	
	// effects
	private EffectsManager fxm;
	
	public PLState(GameStateManager gsm) {
		Display.setTitle("501 - A DDPC Extension");
		this.gsm = gsm;
		tilemap = new PLTileMap();
		player = new PLPlayer(this, tilemap);
		hud = new PLHUD(player);
		fxm = new EffectsManager(tilemap, player);
		Music.stop();
	}
	
	public void playEffect(int effect) {
		fxm.playEffect(effect);
	}
	
	public void update() {
		player.update();
		tilemap.setPosition(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY() + 100);
		fxm.update();
	}
	
	public void render() {
		tilemap.render();
		player.render();
		fxm.render();
		hud.render();
	}
	
	protected void handleInput() {
		player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
		player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
		player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP));
		if (KeyInput.isPressed(KeyInput.F)) player.transform();
		if (KeyInput.isPressed(KeyInput.SPACE)) player.useAbility();
		if (KeyInput.isPressed(KeyInput.KEY_1)) player.setAbility(0);
		if (KeyInput.isPressed(KeyInput.KEY_2)) player.setAbility(1);
		if (KeyInput.isPressed(KeyInput.KEY_3)) player.setAbility(2);
		if (KeyInput.isPressed(KeyInput.KEY_4)) player.setAbility(3);
	}
}