package effects;

import static main.Render.drawImage;
import static main.Render.fillScreen;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import entity.Entity;
import entity.util.Animation;
import main.Render;
import tile.TileMap;
import util.FadeManager;

public class RainEffect extends VisualEffect {
	
	private TileMap tm;
	private ArrayList<RainDrop> raindrops;
	private int numDrops;
	private boolean firstSpawn;
	private long thunderTimer;
	private int thunderWait;
	private boolean lightning;
	
	// lightning flash
	private Animation lightningFlash;
	private int strikeLocation;
	private boolean striking;
	private Entity player;
	
	public RainEffect(TileMap tm, Entity player, boolean lightning) {
		this.tm = tm;
		this.player = player;
		this.lightning = lightning;
		numDrops = 170;
		if (!lightning) numDrops = 80;
		firstSpawn = true;
		raindrops = new ArrayList<RainDrop>();
		lightningFlash = new Animation();
		lightningFlash.setFrames(Textures.lightning);
		lightningFlash.setDelay(120);
		spawnDrops();
		setNextThunder();
	}
	
	private void setNextThunder() {
		if (!lightning) return;
		thunderWait = 5000 + (int) (Math.random() * 10000);
		strikeLocation = (int) (Math.random() * tm.getWidth());
		System.out.println("ground at location: " + tm.getGroundY(strikeLocation));
		System.out.println("time to next: " + thunderWait + "ms, strike at x=" + strikeLocation);
	}
	
	private void spawnDrops() {
		int count = 0;
		while (raindrops.size() < numDrops) {
			float xPos = -100 + (float) (Math.random() * (Render.WIDTH + 200));
			float yPos = -100 + (float) (Math.random() * 90);
			if (!lightning) {
				xPos = 400 + (float) (Math.random() * 300);
				yPos = 100;
			}
			if (firstSpawn) {
				if (!lightning) {
					yPos = 100 + (float) (Math.random() * 350);
				}
				yPos = (float) (Math.random() * 600);
			}
			xPos -= tm.getX();
			if (xPos > tm.getWidth() - 15) {
				xPos = (tm.getWidth() - Render.WIDTH) + (float) (Math.random() * (Render.WIDTH - 30));
			}
			yPos -= tm.getY();
			RainDrop r = new RainDrop(tm, xPos, yPos);
			raindrops.add(r);
			count++;
			if (!firstSpawn && count > 15) break;
		}
		if (firstSpawn) firstSpawn = false;
	}
	
	public void soundOn() {
		Music.play(Music.THUNDER);
	}
	
	public void soundOff() {
		Music.stop();
	}
	
	public void update() {
		// update rain
		for (int i = 0; i < raindrops.size(); i++) {
			raindrops.get(i).update();
			if (raindrops.get(i).remove()) {
				raindrops.remove(i);
				i--;
			}
		}
		spawnDrops();
		// check thunder
		if (striking) {
			lightningFlash.update();
			if (lightningFlash.hasPlayedOnce()) {
				striking = false;
				lightningFlash.reset();
				thunderTimer = System.nanoTime();
				setNextThunder();
			}
		}
		long elapsed = (System.nanoTime() - thunderTimer) / 1000000;
		if (elapsed > thunderWait && lightning && !striking) {
			if (!FadeManager.isWhite()) return;
			Sfx.playSound(Sfx.TYPE_MISC, Sfx.THUNDER);
			FadeManager.fadeIn(0.5f, true);
			striking = true;
			if (Math.abs(strikeLocation - player.getX()) < 150) player.damage();
		}
	}
	
	public void render() {
		fillScreen(new Color(0, 0, 0, 80));
		if (striking) {
			Texture tex = lightningFlash.getCurrentFrame();
			float x = strikeLocation + tm.getX() - tex.getImageWidth() / 2;
			float y = tm.getY() + tm.getGroundY(strikeLocation) - tex.getImageHeight();
			System.out.println("[RainEffect] rendering lightning at (" + x + "," + y + ")");
			drawImage(tex, x, y);
		}
		for (RainDrop r : raindrops) r.render();
		// DEBUG
		//if (lightning) Render.drawString("strike: " + strikeLocation, 3, 584);
	}
	
	private class RainDrop extends Entity {
		
		private boolean landed;
		
		public RainDrop(TileMap tm, float x, float y) {
			super(tm);
			this.x = x;
			this.y = y;
			width = 4;
			height = 16;
			cwidth = 12;
			cheight = 6;
			moveSpeed = 16f;
			animation = new Animation();
			animation.setFrames(Textures.raindrop_2);
			animation.setDelay(-1);
			setMapPosition();
		}
		
		public void update() {
			if (!landed) {
				dy = moveSpeed;
				currRow = (int) (y / tileSize);
				checkTileCollision(x, y + dy);
				if (bottomLeft || bottomRight) {
					landed = true;
					dy = 0;
					y = (currRow + 1) * tileSize - cheight / 2;
					width = 12;
					animation.setFrames(Textures.raindrop_land_2);
					animation.setDelay(50);
				}
				else y += dy;
			}
			animation.update();
		}
		
		public void render() {
			setMapPosition();
			if (notOnScreen()) return;
			drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
		}
		
		public boolean notOnScreen() {
			return y + ymap < -6 || y + ymap > Render.HEIGHT + 10 || x + xmap < -4 || x + xmap > Render.WIDTH + 4;
		}
		
		public boolean remove() {
			if (!lightning) return y > 500;
			return (landed && animation.hasPlayedOnce()) || y > tm.getHeight() + 10;
		}
	}
}