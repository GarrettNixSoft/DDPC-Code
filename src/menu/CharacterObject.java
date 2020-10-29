package menu;

import org.newdawn.slick.opengl.Texture;

import main.Render;

import static main.Render.drawImage;

public class CharacterObject extends MenuObject {
	
	// movement/direction
	private float dx, dy;
	private float ay;
	private boolean facingRight;
	
	// activation
	private long timer;
	
	public CharacterObject(Texture tex, float x, float y) {
		super(tex, x, y);
		dx = 8;
		dy = -24;
		ay = 1.2f;
		facingRight = true;
		if (x < Render.WIDTH / 2) {
			dx = -dx;
			facingRight = false;
		}
		timer = -1;
	}
	
	public void start() {
		timer = System.nanoTime();
		System.out.println("[CharacterObject] Start!");
	}
	
	@Override
	public void update() {
		if (timer == -1) return;
		x += dx;
		y += dy;
		dy += ay;
	}
	
	@Override
	public void render() {
		if (timer == -1) return;
		if (facingRight) drawImage(tex, x - tex.getImageWidth() / 2, y - tex.getImageHeight() / 2);
		else drawImage(tex, x + tex.getImageWidth() / 2, y - tex.getImageHeight() / 2, -tex.getImageWidth(), tex.getImageHeight());
	}
}