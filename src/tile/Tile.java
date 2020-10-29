package tile;

public class Tile {
	
	private boolean blocked;
	
	public Tile(boolean blocked) {
		this.blocked = blocked;
	}
	
	public boolean isBlocked() { return blocked; }
}