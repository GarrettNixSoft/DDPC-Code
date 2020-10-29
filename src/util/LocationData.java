package util;

public class LocationData {
	
	// data
	public float x, y;
	public float dx, dy;
	
	// should this data be used
	public boolean use;
	
	// make an object
	public LocationData() {}
	
	public String toString() {
		return String.format("(%d,%d)-<%.2f,%.2f>", (int) x, (int) y, dx, dy);
	}
	
}