package gui;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import util.input.MouseInput;

public class Slider {
	
	private Texture bar;
	private Texture button;
	private int x;
	private int y;
	private int width;
	private int height;
	private int buttonX;
	private int buttonY;
	private int buttonWidth;
	private int buttonHeight;
	private boolean drag;
	
	public Slider(int x, int y, int width, int height) {
		this.bar = Textures.slider[0];
		this.button = Textures.slider[1];
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		buttonX = x;
		buttonY = y + height / 2;
		buttonWidth = 30;
		buttonHeight = 36;
	}
	
	public void setValue(float value) {
		buttonX = x + (int) (width * value);
	}
	
	public float getPosition() {
		return (float) ((float) (buttonX - x) / width);
	}
	
	public void update() {
		if (Mouse.isButtonDown(0)) {
			Rectangle rect = new Rectangle((int) buttonX - buttonWidth / 2, (int) buttonY - buttonHeight / 2, buttonWidth, buttonHeight);
			if (rect.contains(MouseInput.getMousePos())) drag = true;
		}
		else drag = false;
		if (drag) {
			buttonX = Mouse.getX();
			if (buttonX < x) buttonX = x;
			if (buttonX > x + width) buttonX = x + width;
		}
	}
	
	public void render() {
		drawImage(bar, x, y, width, height);
		drawImage(button, buttonX - buttonWidth / 2, buttonY - buttonHeight / 2, buttonWidth, buttonHeight);
	}
}