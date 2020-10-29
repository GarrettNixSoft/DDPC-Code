package effects;

import static main.Render.drawImageC;
import static main.Render.drawRect;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Textures;
import main.Render;
import render.AlphaBounceEffect;
import util.MathUtil;
import util.data.Settings;

public class BlizzardEffect extends VisualEffect {
	
	// snow
	private ArrayList<Snowflake> snowflakes;
	private int target;
	
	// haze
	private AlphaBounceEffect alphaEffect;
	
	public BlizzardEffect() {
		snowflakes = new ArrayList<Snowflake>();
		target = 150;
		initSnowflakes();
		alphaEffect = new AlphaBounceEffect(0.65f, 0.8f, 0.003f, 0.03f, 0.1f);
	}
	
	public void soundOn() {
		Music.stop();
		Music.setVolume(0);
		Music.fade(Settings.musicVolume, 1000);
		Music.play(Music.WIND);
	}
	
	private void initSnowflakes() {
		while (snowflakes.size() < target) {
			int x = MathUtil.randInt(Render.WIDTH);
			int y = MathUtil.randInt(Render.HEIGHT);
			Snowflake s = new Snowflake(x, y);
			snowflakes.add(s);
		}
	}
	
	private void spawnSnowflakes() {
		int count = 0;
		while (snowflakes.size() < target && count < 7) {
			int x = MathUtil.randInt(Render.WIDTH, Render.WIDTH + 50);
			int y = MathUtil.randInt(-20, Render.HEIGHT - 10);
			Snowflake s = new Snowflake(x, y);
			snowflakes.add(s);
		}
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
		alphaEffect.update();
	}
	
	@Override
	public void render() {
		for (Snowflake s : snowflakes) s.render();
		drawRect(0, 0, Render.WIDTH, Render.HEIGHT, new Color(1.0f, 1.0f, 1.0f, alphaEffect.getAlpha()));
		drawRect(0, 0, Render.WIDTH, Render.HEIGHT, new Color(0f, 0f, 0f, 0.4f));
	}
	
	private class Snowflake {
		
		private float x;
		private float y;
		private float dx;
		private float dy;
		private Texture tex;
		
		public Snowflake(int x, int y) {
			this.x = x;
			this.y = y;
			int index = MathUtil.randInt(Textures.snowflakes.length);
			tex = Textures.snowflakes[index];
			dx = -30;
			dy = 2;
		}
		
		public void update() {
			x += dx;
			y += dy;
		}
		
		public void render() {
			drawImageC(tex, x, y);
		}
		
		public boolean remove() {
			return x < -10;
		}
		
	}
}