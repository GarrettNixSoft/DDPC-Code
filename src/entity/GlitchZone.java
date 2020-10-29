package entity;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import main.Render;
import tile.VerticalTileMap;

public class GlitchZone {
	
	// texture
	private Texture texture;
	private Texture pixels;
	
	// movement and position
	private float moveSpeed;
	private float y, ymap;
	private long timer = -1;
	
	// size and such
	private int width, height;
	
	// tilemap
	private VerticalTileMap tm;
	
	// pixel effect
	//private int[][] pixels;
	private int index;
	private long refreshTimer;
	private boolean grid;
	
	public GlitchZone(VerticalTileMap tm) {
		this.tm = tm;
		width = Render.WIDTH;
		height = 200;
		if (tm != null) y = tm.getMaxY();
		texture = Textures.grid[0];
		moveSpeed = 1.8f;
		newPixels();
	}
	
	public void start() {
		timer = System.nanoTime();
	}
	
	public boolean contains(Rectangle rect) {
		Rectangle r = new Rectangle(0, (int) y + 160, width, height);
		return r.contains(rect);
	}
	
	private void newPixels() {
		/*
		pixels = new int[40][200];
		float[] probabilities = {0.05f, 0.25f, 0.55f, 0.9f};
		float greenProb = 0.1f;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[row].length; col++) {
				float roll = (float) Math.random();
				float chance = probabilities[row / 10];
				if (roll < chance) {
					float roll2 = (float) Math.random();
					if (roll2 < greenProb) pixels[row][col] = 2;
					else pixels[row][col] = 1;
				}
			}
		}
		*/
		index++;
		index %= 4;
		int selection = (int) (Math.random() * 4);
		pixels = Textures.glitch_pixels[index][selection];
		refreshTimer = System.nanoTime();
		grid = !grid;
		if (grid) texture = Textures.grid[0];
		else texture = Textures.grid[1];
	}
	
	public void update() {
		if (timer != -1) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > 2000) y -= moveSpeed;
		}
		if (tm != null && y > tm.getMaxY() - 100) y = tm.getMaxY() - 100;
		long elapsed = (System.nanoTime() - refreshTimer) / 1000000;
		if (elapsed > 250) newPixels();
	}
	
	public void render() {
		if (tm != null) ymap = tm.getY();
		drawImage(texture, 0, y + 159 + ymap);
		drawImage(pixels, 0, y + ymap);
		/*
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[row].length; col++) {
				switch (pixels[row][col]) {
					case 0:
						break;
					case 1:
						drawRect(col * 4, y + ymap + row * 4, 4, 4, Color.black);
						break;
					case 2:
						drawRect(col * 4, y + ymap + row * 4, 4, 4, Color.green.brighter());
						break;
				}
			}
		}
		*/
	}
}