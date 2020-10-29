package minigame;

import static main.Render.drawImage;
import static main.Render.fillScreen;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.SoundManager;
import assets.Textures;
import cutscene.Camera;
import effects.Event;
import effects.EventQueue;
import effects.VisualEffect;
import entity.Entity;
import entity.util.Animation;
import main.Render;
import tile.DynamicTilemap;
import util.FadeManager;

public class ThunderStormEffect extends VisualEffect {
	
	private DynamicTilemap tm;
	private Camera camera;
	private long thunderTimer;
	private int thunderWait;
	
	// raindrops
	private ArrayList<RainDrop> raindrops;
	private int numDrops;
	private boolean firstSpawn;
	private int batchLimit;
	
	// audio control
	private int audioChannel;
	
	// lightning flash
	private Animation lightningFlash;
	private int strikeLocation;
	private boolean striking;
	
	// event queue
	private EventQueue eventQueue;
	
	public ThunderStormEffect(DynamicTilemap tm, Camera camera) {
		this.tm = tm;
		this.camera = camera;
		audioChannel = -1;
		numDrops = 170;
		batchLimit = 4;
		firstSpawn = true;
		raindrops = new ArrayList<RainDrop>();
		lightningFlash = new Animation();
		lightningFlash.setFrames(Textures.lightning);
		lightningFlash.setDelay(120);
		spawnDrops();
		setNextThunder();
	}
	
	// add an event queue
	public void setEventQueue(EventQueue eq) {
		if (eq != null) System.out.println("[ThunderStormEffect] Event Queue added successfully!");
		this.eventQueue = eq;
	}
	
	// adjust the volume of the rain
	public void setRainVolume(float volume) {
		SoundManager.setChannelVolume(audioChannel, volume);
	}
	
	private void setNextThunder() {
		thunderTimer = System.nanoTime();
		thunderWait = 5000 + (int) (Math.random() * 10000);
		strikeLocation = (int) (Math.random() * tm.getWidth());
		//System.out.println("ground at location: " + tm.getGroundY(strikeLocation));
		//System.out.println("time to next: " + thunderWait + "ms, strike at x=" + strikeLocation);
	}
	
	private void spawnDrops() {
		int count = 0;
		while (raindrops.size() < numDrops) {
			float xPos = -100 + (float) (Math.random() * (Render.WIDTH + 200));
			float yPos = -100 + (float) (Math.random() * 90);
			if (firstSpawn) yPos = (float) (Math.random() * 400);
			xPos += camera.getX() - Render.WIDTH / 2;
			if (xPos > tm.getWidth() - 15) {
				xPos = (tm.getWidth() - Render.WIDTH) + (float) (Math.random() * (Render.WIDTH - 30));
			}
			yPos += camera.getY() - Render.HEIGHT / 2;
			//System.out.println("[ThunderstormEffect] Spawning drop at (" + xPos + "," + yPos + ")");
			RainDrop r = new RainDrop(tm, xPos, yPos);
			raindrops.add(r);
			count++;
			// limit batch size
			if (!firstSpawn && count > batchLimit) break;
		}
		if (firstSpawn) firstSpawn = false;
	}
	
	public void soundOn() {
		audioChannel = SoundManager.getNextAvailableChannel();
		System.out.println("[ThunderStormEffect] Audio channel open: " + audioChannel);
		SoundManager.resetVolume(audioChannel);
		SoundManager.playMusic(Music.THUNDER, audioChannel);
	}
	
	public void soundOff() {
		SoundManager.stopMusic(Music.THUNDER);
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
			}
		}
		long elapsed = (System.nanoTime() - thunderTimer) / 1000000;
		if (elapsed > thunderWait) {
			if (!FadeManager.isWhite()) return;
			Sfx.playSound(Sfx.TYPE_MISC, Sfx.THUNDER);
			//FadeManager.fadeIn(0.5f, true);
			//striking = true;
			//if (Math.abs(strikeLocation - player.getX()) < 150) player.damage();
			setNextThunder();
			if (eventQueue != null) eventQueue.queue(new Event(Event.LIGHTNING));
		}
	}
	
	public void render() {
		fillScreen(new Color(0, 0, 0, 80));
		if (striking) {
			Texture tex = lightningFlash.getCurrentFrame();
			drawImage(tex, strikeLocation + tm.getX() - tex.getImageWidth() / 2, tm.getY() + tm.getGroundY(strikeLocation) - tex.getImageHeight());
		}
		for (RainDrop r : raindrops) r.render();
	}
	
	private class RainDrop extends Entity {
		
		private boolean landed;
		
		public RainDrop(DynamicTilemap tm, float x, float y) {
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
			return (landed && animation.hasPlayedOnce()) || y > tm.getHeight() + 10;
		}
	}
}