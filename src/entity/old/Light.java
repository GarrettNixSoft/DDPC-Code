package entity.old;

import static main.Render.drawImage;
import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.Entity;
import entity.util.Animation;
import tile.TileMap;

public class Light extends Entity {
	
	private long timer;
	private float yPos;
	
	public Light(TileMap tm, float x, float y) {
		super(tm);
		this.x = x;
		this.y = y;
		width = height = 60;
		setMapPosition();
		Texture[] tex = new Texture[1];
		tex[0] = Textures.light;
		animation = new Animation();
		animation.setFrames(tex);
		animation.setDelay(-1);
		timer = System.nanoTime();
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		float time = elapsed / 30.0f;
		yPos = (float) (20 * Math.sin(time / 35.0)) + y;
	}
	
	public void render() {
		setMapPosition();
		if (notOnScreen()) return;
		// LIGHTING
		/*
		FloatBuffer lightBuffer = BufferUtils.createFloatBuffer(4);
		lightBuffer.put(new float[] {0.5f, 0.5f, 0.5f, 1f});
		lightBuffer.flip();
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(new float[] {0f, 0f, 0f, 1f});
		position.flip();
		renderLight(lightBuffer, position);
		*/
		// RENDER
		drawImage(animation.getCurrentFrame(), x + xmap - width / 2, yPos + ymap - height / 2, width, height);
	}
}