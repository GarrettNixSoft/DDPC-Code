package background;

import org.newdawn.slick.opengl.Texture;

import main.Render;

import static main.Render.drawImage;

public class BackgroundElement {
	
	private Texture texture;
	private float startX;
	private float startY;
	private float x;
	private float y;
	private int width;
	private int height;
	private float xMoveScale;
	private float yMoveScale;
	
	public BackgroundElement(Texture t, int x, int y, int width, int height, float xMoveScale, float yMoveScale) {
		this.texture = t;
		this.x = startX = x;
		this.y = startY = y;
		this.width = width;
		this.height = height;
		this.xMoveScale = xMoveScale;
		this.yMoveScale = yMoveScale;
	}
	
	public void update(float tmX, float tmY) {
		x = startX + (tmX * xMoveScale);
		y = startY + (tmY * yMoveScale);
		if (x + width / 2 < 0) {
			while (x + width / 2 < 0) {
				x = x + Render.WIDTH + width;
				if (x > 0) break;
			}
		}
		else if (x - width / 2 > Render.WIDTH) {
			while (x - width / 2 > Render.WIDTH) {
				x = x - Render.WIDTH - width;
				if (x < Render.WIDTH) break;
			}
		}
	}
	
	public void render() {
		drawImage(texture, x - width / 2, y - height / 2, width, height);
		// loop if full screen width
		if (width == Render.WIDTH) {
			if (x - width / 2 < 0) drawImage(texture, x - width / 2 + Render.WIDTH, y - height / 2, width, height);
			if (x + width / 2 > Render.WIDTH) drawImage(texture, x - width / 2 - Render.WIDTH, y - height / 2, width, height);
		}
	}
}