package gui;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import util.input.MouseInput;

import static main.Render.drawImage;

public class Button {
	
	protected Texture defaultTex;
	protected Texture hoverTex;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected boolean hover;
	protected boolean prevHover;
	
	// use texture width/height
	public Button(Texture defaultTex, Texture hoverTex, int x, int y) {
		this.defaultTex = defaultTex;
		this.hoverTex = hoverTex;
		this.x = x;
		this.y = y;
		this.width = defaultTex.getImageWidth();
		this.height = defaultTex.getImageHeight();
	}
	
	// use scaled width/height
	public Button(Texture defaultTex, Texture hoverTex, int x, int y, int width, int height) {
		this.defaultTex = defaultTex;
		this.hoverTex = hoverTex;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isHover() {
		return hover;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
	
	public void update() {
		prevHover = hover;
		if (getRectangle().contains(MouseInput.getMousePos())) {
			hover = true;
			if (!prevHover) Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
		}
		else hover = false;
	}
	
	public void render() {
		if (hover) drawImage(hoverTex, x, y, width, height);
		else drawImage(defaultTex, x, y, width, height);
	}
	
	public void render(float alpha) {
		if (hover) drawImage(hoverTex, x, y, width, height, alpha);
		else drawImage(defaultTex, x, y, width, height, alpha);
	}
}