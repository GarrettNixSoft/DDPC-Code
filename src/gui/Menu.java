package gui;

import org.newdawn.slick.opengl.Texture;

import gameState.GameStateManager;

/*
 * The Menu class defines a menu overlay used in game. For example, the pause menu
 * is a type of Menu object.
 */
public abstract class Menu {
	
	// game states
	protected GameStateManager gsm;
	
	// texture
	protected Texture bg;
	protected Texture title;
	
	// buttons
	protected Button[] buttons;
	
	public abstract void update();
	public abstract void render();
	public abstract void handleInput();
	
}