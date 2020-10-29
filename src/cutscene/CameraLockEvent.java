package cutscene;

import entity.Entity;
import util.data.DataCache;

public class CameraLockEvent extends CutsceneEvent {
	
	// entity
	private String entityID;
	private Entity entity;
	
	// camera
	private Camera camera;
	
	// started
	private boolean started;
	
	// finished
	private boolean finished;
	
	public CameraLockEvent(Camera c, Entity e) {
		this.camera = c;
		this.entity = e;
		System.out.println("[CameraLockEvent] Camera lock event on entity[hash=" + e.hashCode() + "]");
		finished = false;
	}
	
	public void init() {
		System.out.println("[CameraLockEvent] Initializng...");
		if (entityID.equals("player")) {
			entity = DataCache.player;
			System.out.println("[CameraLockEvent] Locked on Player");
		}
		if (entityID.equals("sayori")) {
			entity = DataCache.characters[0];
			if (entity == null) System.out.println("[CameraLockEvent] Sayori is null.");
			System.out.println("[CameraLockEvent] Locked on Sayori");
		}
		if (entityID.equals("natsuki")) {
			entity = DataCache.characters[1];
			System.out.println("[CameraLockEvent] Locked on Natsuki");
		}
		if (entityID.equals("yuri")) {
			entity = DataCache.characters[2];
			System.out.println("[CameraLockEvent] Locked on Yuri");
		}
		if (entityID.equals("mirror")) {
			entity = DataCache.characters[3];
			System.out.println("[CameraLockEvent] Locked on Mirror");
		}
		else {
			// other entity
		}
	}
	
	public void update() {
		if (!started) {
			//init();
			started = true;
		}
		if (!finished) camera.lockOnEntity(entity);
	}
	
	public boolean finished() {
		return true;
	}
}