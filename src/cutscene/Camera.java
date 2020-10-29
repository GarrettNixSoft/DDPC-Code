package cutscene;

import entity.Entity;
import main.Render;
import tile.TileMap;

public class Camera {
	
	// tilemap
	private TileMap tm;
	
	
	
	// position
	private float x;
	private float y;
	
	// zoom
	//private boolean zoom;
	//private float scale;
	//private long zoomTimer;
	//private int zoomTransition;
	
	// shake
	public static final int SMOOTH = 0;
	public static final int ROUGH = 1;
	public static final int VERT_SMOOTH = 2;
	public static final int VERT_ROUGH = 3;
	public static final int HORIZ_SMOOTH = 4;
	public static final int HORIZ_ROUGH = 5;
	private boolean shake;
	private int shakeType;
	private int severity;
	private long shakeTimer;
	private int shakeDuration;
	private int shakeX, shakeY;
	private int lastQuad;
	
	// quads
	private int[][] quads = {
			{-1, -1},
			{1, -1},
			{1, 1},
			{-1, 1}
	};
	
	private int[] quad1Options = {1, 2, 3};
	private int[] quad2Options = {0, 2, 3};
	private int[] quad3Options = {0, 1, 3};
	private int[] quad4Options = {0, 1, 2};
	
	// entity lock
	private boolean locked;
	private Entity entity;
	
	// init a Camera object
	public Camera(TileMap tm) {
		this.tm = tm;
	}
	
	// move the camera
	public void move(float xDelta, float yDelta) {
		x += xDelta;
		y += yDelta;
		//System.out.println("[Camera] Moved by " + xDelta + "x, " + yDelta + "y; position is now (" + x + ", " + y + ")");
	}
	
	// set the camera position
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		System.out.println("[Camera hash=" + hashCode() + "] Moved to " + x + "x, " + y + "y");
	}
	
	// lock on to an entity
	public void lockOnEntity(Entity e) {
		locked = true;
		this.entity = e;
		System.out.println("[Camera hash=" + hashCode() + "]  Locked on entity [hash=" + e.hashCode() + "]");
	}
	
	// clear the lock
	public void clearEntityLock() {
		locked = false;
		this.entity = null;
		System.out.println("[Camera hash=" + hashCode() + "]  Entity lock cleared.");
	}
	
	// snap lock
	public void snapToLock() {
		if (!locked) return;
		x = entity.getX();
		y = entity.getY();
		tm.setPositionAbsolute(Render.WIDTH / 2 - x, Render.HEIGHT / 2 - y);
	}
	
	// zoom
	public void zoom(float scale, float time) {
		//if (scale != 1) zoom = true;
	}
	
	// shake
	public void shake(int type, int severity, float time) {
		shake = true;
		shakeType = type;
		this.severity = severity;
		shakeDuration = (int) (time * 1000);
		shakeTimer = System.nanoTime();
		System.out.println("[Camera] Shaking camera [type=" + type + "severity=" + severity + "] for " + time + "s");
	}
	
	// getters
	public float getX() { return x; }
	public float getY() { return y; }
	public float getScreenX() { return x + tm.getX(); }
	public float getScreenY() { return y + tm.getY(); }
	public boolean isLocked() { return locked; }
	public Entity getLock() { return entity; }
	public TileMap getTilemap() { return tm; }
	
	// update the camera
	public void update() {
		float xtemp = x;
		float ytemp = y;
		if (locked) {
			x = xtemp = entity.getX();
			y = ytemp = entity.getY();
			//System.out.println("[Camera] Locked, position set to (" + (int) x + "," + (int) y + ")");
		}
		if (shake) {
			// shake
			long time = System.nanoTime() / 1000000;
			if (time / 20 % 2 == 0) {
				switch (shakeType) {
				case SMOOTH:
					shakeX = (int) (-severity + (Math.random() * (2 * severity)));
					shakeY = (int) (-severity + (Math.random() * (2 * severity)));
					break;
				case ROUGH:
					shakeX = (int) (Math.random() * severity);
					shakeY = (int) (Math.random() * severity);
					int nextIndex = (int) (Math.random() * 3);
					int quad = -1;
					switch (lastQuad) {
					case 0:
						quad = quad1Options[nextIndex];
						break;
					case 1:
						quad = quad2Options[nextIndex];
						break;
					case 2:
						quad = quad3Options[nextIndex];
						break;
					case 3:
						quad = quad4Options[nextIndex];
						break;
					}
					shakeX *= quads[quad][0];
					shakeY *= quads[quad][1];
					lastQuad = quad;
					break;
				case VERT_SMOOTH:
					shakeX = 0;
					shakeY = (int) (-severity + (Math.random() * (2 * severity)));
					break;
				case VERT_ROUGH:
					
					break;
				case HORIZ_SMOOTH:
					//System.out.println("shake horiz smooth");
					shakeX = (int) (-severity + (Math.random() * (2 * severity)));
					System.out.println("shakeX = " + shakeX);
					shakeY = 0;
					break;
				case HORIZ_ROUGH:
					
					break;
				}
			}
			// check if it's over
			long elapsed = (System.nanoTime() - shakeTimer) / 1000000;
			if (elapsed > shakeDuration) {
				shake = false;
			}
			tm.shake(shakeX, shakeY, shakeType % 2 == 0);
			tm.setPosition(Render.WIDTH / 2 - xtemp, Render.HEIGHT / 2 - ytemp);
		}
		else tm.setPosition(Render.WIDTH / 2 - xtemp, Render.HEIGHT / 2 - ytemp);
	}
	
}