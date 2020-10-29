package cutscene;

import entity.Entity;
import main.Render;
import tile.TileMap;
import util.MathUtil;
import util.data.LevelCache;

public class CameraFocusEvent extends CutsceneEvent {
	
	// camera
	private Camera camera;
	private TileMap tilemap;
	private LevelCache lc;
	
	// values
	private int targetX;
	private int targetY;
	
	// velocity
	private float dx;
	private float dy;
	
	// speed
	private float maxSpeed;
	
	// finished
	private boolean finished;
	
	// started
	private boolean started;
	
	public CameraFocusEvent(LevelCache lc, int x, int y, float speed) {
		this.camera = lc.camera;
		this.tilemap = lc.tilemap;
		this.lc = lc;
		this.targetX = x;
		this.targetY = y;
		System.out.println("[CameraFocusEvent] Target: (" + x + ", " + y + ")");
		this.maxSpeed = speed;
	}
	
	public CameraFocusEvent(LevelCache lc, Entity e, float speed) {
		this.camera = lc.camera;
		this.tilemap = lc.tilemap;
		this.lc = lc;
		this.targetX = (int) e.getX();
		this.targetY = (int) e.getY();
		System.out.println("[CameraFocusEvent] Target: (" + e.getX() + ", " + e.getY() + ")");
		this.maxSpeed = speed;
	}
	
	private void calculateVelocity() {
		this.camera.clearEntityLock();
		float x = camera.getX();
		float y = camera.getY();
		if (x < Render.WIDTH / 2) x = Render.WIDTH / 2;
		else if (x > lc.tilemap.getWidth() - Render.WIDTH / 2) x = lc.tilemap.getWidth() - Render.WIDTH / 2;
		if (y < Render.HEIGHT / 2) y = Render.HEIGHT / 2;
		else if (y > lc.tilemap.getHeight() - Render.HEIGHT / 2) y = lc.tilemap.getHeight() - Render.HEIGHT / 2;
		camera.setPosition(x, y);
		// REVISED MATH/LOGIC
		float xDelta = targetX - x;
		float yDelta = targetY - y;
		xDelta = Math.abs(xDelta);
		yDelta = Math.abs(yDelta);
		double angle = Math.atan(yDelta / xDelta);
		dx = (float) Math.cos(angle) * maxSpeed;
		dy = (float) Math.sin(angle) * maxSpeed;
		if (targetX < x) dx *= -1;
		if (targetY < y) dy *= -1;
		System.out.println("[CameraFocusEvent] Camera[hash=" + camera.hashCode() + "] x: " + x);
		System.out.println("[CameraFocusEvent] Camera[hash=" + camera.hashCode() + "] y: " + y);
		System.out.println("[CameraFocusEvent] Camera velocity: " + dx + "x, " + dy + "y");
	}
	
	public void update() {
		//System.out.println("camera focus");
		if (!started) {
			calculateVelocity();
			started = true;
			System.out.println("[CameraFocusEvent] Target: (" + targetX + ", " + targetY + ")");
		}
		if (MathUtil.distance(camera.getX(), camera.getY(), targetX, targetY) < maxSpeed) {
			camera.setPosition(targetX, targetY);
			finished = true;
		}
		else if (tilemap.isVisible(targetX, targetY) && tilemap.fixX() && tilemap.fixY()) {
			finished = true;
		}
		else if (!finished) {
			camera.move(dx, dy);
			finished = (camera.getX() == targetX && camera.getY() == targetY);
		}
	}
	
	public boolean finished() {
		return finished;
	}
	
}