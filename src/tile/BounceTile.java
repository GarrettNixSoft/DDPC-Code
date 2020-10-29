package tile;

import static main.Render.drawRect;
import static main.Render.drawImage;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Textures;

public class BounceTile extends DynamicTile {
	
	// colors
	private Color[] colors = {
			new Color(0, 255, 255),
			new Color(255, 127, 220),
			new Color(120, 0, 255),
			new Color(0, 255, 33)
	};
	
	// render
	private Color color;
	private Color whiteHue;
	private Texture tex;
	private long timer;
	
	// direction
	private int direction;
	
	// direction IDs
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	public BounceTile(int character, int direction) {
		this.color = colors[character];
		this.direction = direction;
		this.whiteHue = new Color(255, 255, 255, 0);
		this.tex = Textures.bounceTiles[direction];
		timer = System.nanoTime();
		this.tileID = BOUNCE;
	}
	
	public int getDirection() { return direction; }
	
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		int alpha = (int) (30.0 * (Math.sin(elapsed / 300)));
		whiteHue = new Color(255, 255, 255, alpha);
	}
	
	public void render(float x, float y) {
		drawRect(x, y, 60, 60, color);
		drawRect(x, y, 60, 60, whiteHue);
		drawImage(tex, x, y, 60, 60);
	}
	
}