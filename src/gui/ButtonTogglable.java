package gui;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import util.input.MouseInput;

import static main.Render.drawImage;

public class ButtonTogglable extends Button {
	
	// toggle button active
	private boolean active;
	
	// use texture width/height
	public ButtonTogglable(Texture defaultTex, Texture hoverTex, int x, int y, boolean active) {
		super(defaultTex, hoverTex, x, y);
		this.active = active;
		prevHover = true;
	}
	
	// use scaled width/height
	public ButtonTogglable(Texture defaultTex, Texture hoverTex, int x, int y, int width, int height, boolean active) {
		super(defaultTex, hoverTex, x, y);
		this.active = active;
		prevHover = true;
	}
	
	public void disable() { active = false; }
	public void enable() { active = true; }
	public boolean isActive() { return active; }
	
	public boolean isHover() {
		if (!active) return false;
		else return hover;
	}
	
	public void update() {
		if (!active) return;
		prevHover = hover;
		if (new Rectangle(x, y, width, height).contains(MouseInput.getMousePos())) {
			hover = true;
			if (!prevHover) Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
		}
		else hover = false;
	}
	
	public void render() {
		if (active) renderStandard();
		else renderFaded();
	}
	
	public void renderStandard() {
		if (hover) drawImage(hoverTex, x, y, width, height);
		else drawImage(defaultTex, x, y, width, height);
	}
	
	public void renderFaded() {
		if (hover) drawImage(hoverTex, x, y, width, height, 0.4f);
		else drawImage(defaultTex, x, y, width, height, 0.4f);
	}
}