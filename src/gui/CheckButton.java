package gui;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import util.input.MouseInput;

public class CheckButton {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private Texture box;
	private Texture check;
	private boolean checked;
	private boolean hover;
	
	public CheckButton(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = 40;
		this.height = 40;
		box = Textures.checkbox[0];
		check = Textures.checkbox[1];
	}
	
	public void toggleChecked() {
		checked = !checked;
		Sfx.playSound(Sfx.TYPE_MENU, Sfx.HOVER);
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public boolean isHover() {
		return hover;
	}
	
	public void update() {
		Rectangle r = new Rectangle(x, y, width, height);
		hover = r.contains(MouseInput.getMousePos());
	}
	
	public void render() {
		drawImage(box, x, y, width, height);
		if (checked) drawImage(check, x - 5, y - 5, width + 10, height + 10);
	}
}