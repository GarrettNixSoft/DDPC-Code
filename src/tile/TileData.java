package tile;

import java.util.ArrayList;

import entity.SpawnPoint;

public class TileData {
	
	// data
	private int tilesetID;
	private int bgValue;
	private int[][] map;
	private int[] spawnCoords;
	private int[] winCoords;
	private ArrayList<SpawnPoint> healSpawns;
	private ArrayList<SpawnPoint> enemySpawns;
	
	// default
	public TileData() {
		tilesetID = 0;
		bgValue = 0;
		map = new int[1][1];
		spawnCoords = new int[2];
		winCoords = new int[2];
		healSpawns = new ArrayList<SpawnPoint>();
		enemySpawns = new ArrayList<SpawnPoint>();
	}
	
	// overloaded
	public TileData(int tilesetID, int bgValue, int[][] map, int[] spawnCoords, int[] winCoords, ArrayList<SpawnPoint> healSpawns, ArrayList<SpawnPoint> enemySpawns) {
		this.tilesetID = tilesetID;
		this.bgValue = bgValue;
		this.spawnCoords = spawnCoords;
		this.winCoords = winCoords;
		this.map = map;
		this.healSpawns = healSpawns;
		this.enemySpawns = enemySpawns;
	}
	
	// getters
	public int getTilesetID() { return tilesetID; }
	public int getBgValue() { return bgValue; }
	public int[][] getMap() { return map; }
	public int[] getSpawnCoords() { return spawnCoords; }
	public int[] getWinCoords() { return winCoords; }
	public ArrayList<SpawnPoint> getHealSpawns() { return healSpawns; }
	public ArrayList<SpawnPoint> getEnemySpawns() { return enemySpawns; }
	
	// setters
	public void setTilesetID(int tilesetID) { this.tilesetID = tilesetID; }
	public void setBgValue(int bgValue) { this.bgValue = bgValue; }
	public void setMap(int[][] map) { this.map = map; }
	public void setSpawnCoords(int[] spawnCoords) { this.spawnCoords = spawnCoords; }
	public void setWinCoords(int[] winCoords) { this.winCoords = winCoords; }
	public void setHealSpawns(ArrayList<SpawnPoint> healSpawns) { this.healSpawns = healSpawns; }
	public void setEnemySpawns(ArrayList<SpawnPoint> enemySpawns) { this.enemySpawns = enemySpawns; }
	
}