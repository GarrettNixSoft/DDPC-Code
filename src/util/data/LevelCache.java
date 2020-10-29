package util.data;

import cutscene.Camera;
import entity.Character;
import entity.PlayerEnhanced;
import gameState.PlayStateEP;
import tile.DynamicTilemap;

/*
 * This class contains all the data needed to load cutscenes. It
 * must be created before starting the cutscene loading process.
 */

public class LevelCache {
	
	// constructor
	public LevelCache() { }
	
	// parent
	public PlayStateEP parent;
	
	// map
	public DynamicTilemap tilemap;
	
	// camera
	public Camera camera;
	
	// entities
	public PlayerEnhanced player;
	public Character[] characters;
	
	// locations
	public int winX;
	public int winY;
	
}