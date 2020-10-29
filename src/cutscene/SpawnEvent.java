package cutscene;

import entity.Character;
import entity.Entity;
import entity.enemy.Climber;
import entity.enemy.RainCloud;
import entity.enemy.Spider;
import util.data.LevelCache;

public class SpawnEvent extends CutsceneEvent {
	
	private LevelCache lc;
	private Entity entity;
	private int x, y;
	private boolean right;
	
	private boolean spawned;
	
	// custom spawns
	private boolean playerSpawn;
	
	public SpawnEvent(LevelCache lc, String entityID, int x, int y, boolean right) {
		this.lc = lc;
		this.x = x;
		this.y = y;
		this.right = right;
		loadEntity(entityID);
	}
	
	public SpawnEvent(LevelCache lc, String entityID, int spawnID, boolean right) {
		System.out.println("[SpawnEvent] Relative spawn constructed!");
		this.lc = lc;
		setSpawn(spawnID);
		this.right = right;
		loadEntity(entityID);
	}
	
	private void loadEntity(String entityID) {
		System.out.println("[SpawnEvent] Loading entity[id=" + entityID + "]");
		if (entityID.equals("sayori")) {
			entity = new Character(lc.tilemap, 0);
		}
		else if (entityID.equals("natsuki")) {
			entity = new Character(lc.tilemap, 1);
		}
		else if (entityID.equals("yuri")) {
			entity = new Character(lc.tilemap, 2);
		}
		else if (entityID.equals("monika")) {
			entity = new Character(lc.tilemap, 3);
		}
		else if (entityID.equals("mirror")) {
			entity = new Character(lc.tilemap, 4);
		}
		else if (entityID.equals("cloud_0")) {
			entity = new RainCloud(lc.tilemap, 0, x, y);
		}
		else if (entityID.equals("cloud_1")) {
			entity = new RainCloud(lc.tilemap, 1, x, y);
		}
		else if (entityID.equals("spider_0")) {
			entity = new Spider(lc.tilemap, x, y, 0);
		}
		else if (entityID.equals("spider_1")) {
			entity = new Climber(lc.tilemap, x, y, 0);
		}
		else if (entityID.equals("ghost_0")) {
			
		}
		else if (entityID.equals("ghost_1")) {
			
		}
		entity.setFacingRight(right);
	}
	
	private void setSpawn(int spawnID) {
		System.out.println("[SpawnEvent] spawnID = " + spawnID);
		switch (spawnID) {
		case 0:
			playerSpawn = true;
			break;
		}
	}
	
	private void spawn() {
		System.out.println("[SpawnEvent] Spawning entity!");
		if (playerSpawn) {
			System.out.println("[SpawnEvent] Spawning on player.");
			lc.parent.spawnOnPlayer(entity);
		}
		else {
			System.out.println("[SpawnEvent] Spawning... somewhere?");
			lc.parent.spawn(entity);
		}
	}
	
	public void update() {
		if (spawned) return;
		spawned = true;
		spawn();
	}
	
	public boolean finished() {
		return spawned;
	}
}