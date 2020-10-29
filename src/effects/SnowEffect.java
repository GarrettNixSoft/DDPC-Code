package effects;

import static main.Render.drawImageC;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.Entity;
import entity.Player;
import entity.PlayerEnhanced;
import main.Render;
import tile.TileMap;
import tile.VerticalTileMap;
import util.MathUtil;

public class SnowEffect extends VisualEffect {
	
	// tilemap
	private TileMap tm;
	private Player player;
	private PlayerEnhanced p2;
	
	// snowflakes
	private ArrayList<Snowflake> snowflakes;
	private int target;
	private int extra;
	
	// constructor
	public SnowEffect(TileMap tm, Player player) {
		this.tm = tm;
		this.player = player;
		snowflakes = new ArrayList<Snowflake>();
		target = 250;
		initSnowflakes();
	}
	
	public SnowEffect(TileMap tm, PlayerEnhanced player) {
		this.tm = tm;
		this.p2 = player;
		snowflakes = new ArrayList<Snowflake>();
		target = 250;
		initSnowflakes();
	}
	
	// raw
	public SnowEffect() {
		this.tm = null;
		this.player = null;
		snowflakes = new ArrayList<Snowflake>();
		target = 250;
		initSnowflakes();
	}
	
	private void initSnowflakes() {
		while (snowflakes.size() < target - 50) {
			int x = MathUtil.randInt(-300, Render.WIDTH + 300);
			int y = MathUtil.randInt(-300, (int) (Render.HEIGHT * 0.8));
			//System.out.println("[SnowEffect] Generated coordinates: (" + x + "," + y + ")");
			if (tm != null) x -= tm.getX();
			if (tm != null) y -= tm.getY();
			//System.out.println("[SnowEffect] Position mapped to (" + x + "," + y + ")");
			Snowflake s = new Snowflake(tm, x, y);
			snowflakes.add(s);
		}
	}
	
	// refill snowflake list
	private void spawnSnowflakes() {
		int count = 0;
		// fill sides
		if (player != null) {
			if (player.getDX() < -2) { // moving left
				spawnLeft(5);
			}
			else if (player.getDX() > 2) { // moving right
				spawnRight(5);
			}
			// fill below
			if (player.getDY() > 2) { // player falling
				spawnBelow(9);
			}
		}
		else if (p2 != null) {
			if (p2.getDX() < -2) { // moving left
				spawnLeft(5);
			}
			else if (p2.getDX() > 2) { // moving right
				spawnRight(5);
			}
			// fill below
			if (p2.getDY() > 2) { // player falling
				spawnBelow(9);
			}
		}
		while (snowflakes.size() < (target + extra) && count < 7) {
			//System.out.println("[SnowEffect] Target + extra = " + (target + extra) + ", size = " + snowflakes.size() + " spawn!");
			// set x
			int minX = -300;
			int maxX = Render.WIDTH + 300;
			int x = MathUtil.randInt(minX, maxX);
			// set y
			int minY = -100;
			int maxY = 0;
			int y = 0;
			if (x > Render.WIDTH) y = MathUtil.randInt(-20, Render.HEIGHT + 20);
			else if (x < 0) y = MathUtil.randInt(-20, Render.HEIGHT + 20);
			else y = MathUtil.randInt(minY, maxY);
			// adjust to screen
			if (tm != null) x -= tm.getX();
			if (tm != null) y -= tm.getY();
			// create and add snowflake
			Snowflake s = new Snowflake(tm, x, y);
			snowflakes.add(s);
			count++;
		}
	}
	
	private void spawnLeft(int count) {
		extra += count;
		//System.out.println("[SnowEffect] Spawning left, extra total is now: " + extra);
		for (int i = 0; i < count; i++) {
			int x = MathUtil.randInt(-300, 0);
			int y = MathUtil.randInt(-20, Render.HEIGHT + 20);
			if (tm != null) x -= tm.getX();
			if (tm != null) y -= tm.getY();
			Snowflake s = new Snowflake(tm, x, y);
			s.extra = true;
			snowflakes.add(s);
		}
	}
	
	private void spawnRight(int count) {
		extra += count;
		//System.out.println("[SnowEffect] Spawning right, extra total is now: " + extra);
		for (int i = 0; i < count; i++) {
			int x = MathUtil.randInt(Render.WIDTH, Render.WIDTH + 300);
			int y = MathUtil.randInt(-20, Render.HEIGHT + 20);
			if (tm != null) x -= tm.getX();
			if (tm != null) y -= tm.getY();
			Snowflake s = new Snowflake(tm, x, y);
			s.extra = true;
			snowflakes.add(s);
		}
	}
	
	private void spawnBelow(int count) {
		extra += count;
		//System.out.println("[SnowEffect] Spawning below, extra total is now: " + extra);
		for (int i = 0; i < count; i++) {
			int x = MathUtil.randInt(-50, Render.WIDTH + 50);
			int y = MathUtil.randInt(Render.HEIGHT, Render.HEIGHT + 30);
			if (tm != null) x -= tm.getX();
			if (tm != null) y -= tm.getY();
			Snowflake s = new Snowflake(tm, x, y);
			s.extra = true;
			snowflakes.add(s);
		}
	}
	
	@Override
	public void update() {
		// update snowflakes
		//int rem = 0;
		for (int i = 0; i < snowflakes.size(); i++) {
			Snowflake s = snowflakes.get(i);
			s.update();
			if (s.remove()) {
				if (s.extra) extra--;
				snowflakes.remove(i);
				i--;
				//rem++;
			}
		}
		//if (rem > 10) System.out.println("[SnowEffect] Large removal! Removed " + rem + " snowflake entities");
		if (snowflakes.size() < target) spawnSnowflakes();
	}
	
	@Override
	public void render() {
		// render snowflakes
		for (Snowflake s : snowflakes) s.render();
	}
	
	private class Snowflake extends Entity {
		
		private float dy;
		private float dx;
		private float ax;
		private Texture tex;
		
		public boolean extra;
		
		public Snowflake(TileMap tm, int x, int y) {
			super(tm);
			this.x = x;
			this.y = y;
			int spriteIndex = MathUtil.randInt(Textures.snowflakes_2.length);
			tex = Textures.snowflakes_2[spriteIndex];
			dx = MathUtil.randFloat(-1, 1);
			if (MathUtil.randBool()) ax = -0.04f;
			else ax = 0.04f;
			dy = 1.5f;
			if (tm != null) setMapPosition();
		}
		
		public void setMapPosition() {
			//xmap = tm.getX() * 0.1f;
			//ymap = tm.getY() * 0.1f;
			if (tm != null) xmap = tm.getX();
			if (tm != null) ymap = tm.getY();
			if (tm instanceof VerticalTileMap) {
				VerticalTileMap vtm = (VerticalTileMap) tm;
				ymap = vtm.getY();
			}
		}
		
		@Override
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
		
		@Override
		public void render() {
			if (tm != null) setMapPosition();
			drawImageC(tex, x + xmap, y + ymap);
		}
		
		public boolean remove() {
			return y + ymap > Render.HEIGHT + 200 || y + ymap < -220 || x + xmap < -400 || x + xmap > Render.WIDTH + 400;
		}
		
	}
	
}