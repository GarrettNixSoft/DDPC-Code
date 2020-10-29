package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import static main.Render.drawImageC;
import static main.Render.drawOutline;

import java.awt.Rectangle;

public class ChallengeButton extends Button {
	
	public ChallengeButton(Texture defaultTex, int x, int y) {
		super(defaultTex, null, x, y);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(x - width / 2, y - height / 2, width, height);
	}
	
	@Override
	public void render() {
		drawImageC(defaultTex, x, y);
		if (isHover()) drawOutline(getRectangle(), Color.yellow);
	}
	
}