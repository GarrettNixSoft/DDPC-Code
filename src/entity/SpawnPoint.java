package entity;

public class SpawnPoint {
	
	// type IDs
	public static final int HEAL = 0;
	public static final int SPAWN = 1;
	public static final int END = 2;
	public static final int CLOUD_0 = 3;
	public static final int CLOUD_1 = 4;
	public static final int SPIDER_0 = 5;
	public static final int SPIDER_1 = 6;
	public static final int GHOST_0 = 7;
	public static final int GHOST_1 = 8;
	public static final int GLITCH = 9;
	
	// fields
	private int type;
	private int x;
	private int y;
	
	public SpawnPoint(int type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public SpawnPoint(int type, int[] coords) {
		this.type = type;
		this.x = coords[0];
		this.y = coords[1];
	}
	
	public int getType() { return type; }
	public int getX() { return x; }
	public int getY() { return y; }
	
}