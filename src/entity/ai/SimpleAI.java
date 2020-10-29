package entity.ai;

/*
 * Used for A* pathfinding. An entity can use an object of this class
 * for navigating around a level.
 */
public class SimpleAI {
	
	// grid data
	private byte[][] grid;
	
	// nodes
	//private List<Node>[] nodes;
	
	// touched
	//private List<Integer> touchedLocations;
	
	// good values to have
	//private short gridWidthLog2;
	
	public SimpleAI() {
		
	}
	
	/*
	 * Give the AI the map to be simplified to 0 (open tiles) and 1 (closed tiles) values.
	 */
	public void setGrid(int[][] map) {
		grid = new byte[map.length][map[0].length];
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				int val = map[r][c] / 20;
				if (val > 1) val = 1;
				grid[r][c] = (byte) val;
			}
		}
		
	}
	
	// TODO
}