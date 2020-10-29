package effects;

import static main.Render.drawImageC;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import main.Render;
import render.AlphaEffect;
import render.AlphaFadeEffect;
import util.MathUtil;

public class MenuSnowEffect extends VisualEffect {
	
	// snowflakes
	private ArrayList<Snowflake> snowflakes;
	private int target;
	
	// alpha
	private AlphaFadeEffect alphaEffect;
	private boolean fade;
	
	public MenuSnowEffect() {
		snowflakes = new ArrayList<Snowflake>();
		alphaEffect = new AlphaFadeEffect(1.5f);
		fade = false;
		target = 120;
		initSnowflakes();
	}
	
	private void initSnowflakes() {
		while (snowflakes.size() < target) {
			int x = MathUtil.randInt(0, Render.WIDTH);
			int y = MathUtil.randInt(-Render.HEIGHT, -1);
			snowflakes.add(new Snowflake(x, y));
		}
	}
	
	private void spawnSnowflakes() {
		int count = 0;
		while (snowflakes.size() < target && count < 7) {
			int x = MathUtil.randInt(0, Render.WIDTH);
			int y = MathUtil.randInt(-20, -1);
			snowflakes.add(new Snowflake(x, y));
		}
	}
	
	public void fade() {
		fade = true;
		alphaEffect.reset();
	}
	
	public void setAlpha(float alpha) {
		alphaEffect.setAlpha(alpha);
	}
	
	@Override
	public void update() {
		for (int i = 0; i < snowflakes.size(); i++) {
			Snowflake s = snowflakes.get(i);
			s.update();
			if (s.remove()) {
				snowflakes.remove(i);
				i--;
			}
		}
		if (snowflakes.size() < target) spawnSnowflakes();
		if (fade) alphaEffect.update();
	}
	
	@Override
	public void render() {
		for (Snowflake s : snowflakes) s.render(alphaEffect);
	}
	
	private class Snowflake {
		
		// texture
		private Texture tex;
		
		// location and motion
		private float x, y;
		private float dx, dy;
		private float ax;
		
		public Snowflake(int x, int y) {
			tex = Textures.snowflakes_2[MathUtil.randInt(Textures.snowflakes_2.length)];
			this.x = x;
			this.y = y;
			dx = MathUtil.randFloat(-1, 1);
			if (MathUtil.randBool()) ax = -0.04f;
			else ax = 0.04f;
			dy = 1.5f;
		}
		
		public void update() {
			// handle acceleration
			 if (ax < 0) {
				 if (dx < -1) {
					 dx = -1;
					 ax = -ax;
				 }
			 }
			 else {
				 if (dx > 1) {
					 dx = 1;
					 ax = -ax;
				 }
			 }
			 dx += ax;
			 // handle velocity
			 x += dx;
			 y += dy;
		}
		
		public void render(AlphaEffect a) {
			drawImageC(tex, x, y, a.getAlpha());
		}
		
		public boolean remove() {
			return y > Render.HEIGHT + 10;
		}
	}
}