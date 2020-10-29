package entity;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import render.AlphaFadeInEffect;
import tile.TileMap;

import static main.Render.drawImageC;

public class Portal extends Entity {
	
	// textures
	private Texture[] tex;
	private int currentTex;
	private AlphaFadeInEffect alphaEffect;
	
	// timing
	private long timer;
	private boolean delay;
	
	public Portal(TileMap tm, float x, float y) {
		super(tm);
		this.x = x;
		this.y = y;
		tex = Textures.portal;
		alphaEffect = new AlphaFadeInEffect(1.5f);
		timer = System.nanoTime();
	}

	@Override
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (delay) {
			if (elapsed > 2000) {
				delay = false;
				timer = System.nanoTime();
				alphaEffect.reset();
			}
		}
		else {
			if (elapsed > 1500) {
				delay = true;
				timer = System.nanoTime();
				currentTex = (currentTex + 1) % tex.length;
			}
			else alphaEffect.update();
		}
	}

	@Override
	public void render() {
		setMapPosition();
		drawImageC(tex[currentTex], x + xmap, y + ymap);
		if (!delay) {
			int topIndex = (currentTex + 1) % tex.length;
			//System.out.println("[Portal] currentTex = " + currentTex + ", topIndex = " + topIndex);
			drawImageC(tex[topIndex], x + xmap, y + ymap, alphaEffect.getAlpha());
		}
		drawImageC(Textures.portal_outline, x + xmap, y + ymap);
	}
}