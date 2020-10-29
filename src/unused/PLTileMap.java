package unused;

import static main.Render.drawImage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import entity.SpawnPoint;
//import assets.Textures;
import main.Render;
import tile.TileMap;

public class PLTileMap extends TileMap {
	
	// map data
	protected int[][] map;
	protected boolean[][] tiles;
	protected Texture[][] tileset;
	protected int spawnRow;
	protected int spawnCol;
	protected int spawnX;
	protected int spawnY;
	protected int winRow;
	protected int winCol;
	protected int winX;
	protected int winY;
	protected int bgValue;
	
	// entity data
	protected ArrayList<SpawnPoint> healSpawns;
	protected ArrayList<SpawnPoint> enemySpawns;
	
	// map attributes
	protected int tileSize;
	protected int numRows;
	protected int numCols;
	protected int width;
	protected int height;
	
	// position
	protected float x;
	protected float y;
	protected float ease;
	
	// bounds
	protected int xmin;
	protected int ymin;
	protected int xmax;
	protected int ymax;
	
	//rendering
	protected int rowOffset;
	protected int colOffset;
	protected int rowsToDraw;
	protected int colsToDraw;
	
	public PLTileMap() {
		tileSize = 64;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("cols to draw: " + colsToDraw);
		System.out.println("rows to draw: " + rowsToDraw);
		// TEMP
		//tileset = Textures.pl_tiles;
		ease = 0.07f;
		loadMap();
	}
	
	protected void loadEmptyMap() {
		numRows = 16;
		numCols = 50;
		map = new int[numRows][numCols];
		width = numCols * tileSize;
		height = numRows * tileSize;
		System.out.println("TILEMAP IS " + width + "x" + height);
		xmin = Render.WIDTH - width;
		xmax = 0;
		ymin = Render.HEIGHT - height;
		ymax = 0;
		spawnRow = 8;
		spawnCol = 2;
		spawnX = spawnCol * tileSize + tileSize / 2;
		spawnY = (spawnRow + 1) * tileSize - 40;
		map[spawnRow][spawnCol] = 99;
		winRow = 0;
		winCol = 1;
		winX = winCol * tileSize + tileSize / 2;
		winY = (winRow + 1) * tileSize - 40; // MAYBE TWEAK THIS
		bgValue = 0;
		map[winRow][winCol] = 98;
		tiles = new boolean[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i < 14) tiles[i][j] = false;
				else tiles[i][j] = true;
			}
		}
	}
	
	protected void loadMap() {
		// set map size
		numRows = 16;
		numCols = 50;
		map = new int[numRows][numCols];
		// calculate total map width
		width = numCols * tileSize;
		height = numRows * tileSize;
		System.out.println("TILEMAP IS " + width + "x" + height);
		// set min and max for camera
		xmin = Render.WIDTH - width;
		xmax = 0;
		ymin = Render.HEIGHT - height;
		ymax = 0;
		// set spawn location
		spawnRow = 8;
		spawnCol = 2;
		spawnX = spawnCol * tileSize + tileSize / 2;
		spawnY = (spawnRow + 1) * tileSize - 40;
		// create map
		tiles = new boolean[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i < 12) {
					if (i > 8 && j == 10) {
						map[i][j] = 32;
						tiles[i][j] = true;
					}
					else {
						map[i][j] = 5;
						tiles[i][j] = false;
					}
				}
				else {
					map[i][j] = 32;
					tiles[i][j] = true;
				}
			}
		}
	}
	
	protected int[][] loadMap(String level) {
		System.out.println("LOADING: " + level);
		try {
			// load file
			BufferedReader input = null;
			System.out.println("level: " + level);
			InputStream in = getClass().getResourceAsStream(level);
			input = new BufferedReader(new InputStreamReader(in));
			// READ FILE
			int set = Integer.parseInt(input.readLine());
			System.out.println("set: " + set);
			//tileset = Textures.pl_tiles;
			bgValue = Integer.parseInt(input.readLine());
			String spawnLine = input.readLine();
			String[] values = spawnLine.split(",");
			spawnRow = Integer.parseInt(values[0]);
			spawnCol = Integer.parseInt(values[1]);
			spawnX = spawnCol * tileSize + tileSize / 2;
			spawnY = (spawnRow + 1) * tileSize - 40;
			String winLine = input.readLine();
			values = winLine.split(",");
			winRow = Integer.parseInt(values[0]);
			winCol = Integer.parseInt(values[1]);
			winX = winCol * tileSize + tileSize / 2;
			winY = (winRow + 1) * tileSize - 40; // MAYBE TWEAK THIS
			// get dimensions
			numRows = Integer.parseInt(input.readLine());
			numCols = Integer.parseInt(input.readLine());
			map = new int[numRows][numCols];
			tiles = new boolean[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			System.out.println("map width: " + width);
			System.out.println("map height: " + height);
			// set bounds
			xmin = Render.WIDTH - width;
			xmax = 0;
			ymin = Render.HEIGHT - height;
			ymax = 0;
			// load map data
			ArrayList<String> lines = new ArrayList<String>();
			for (int i = 0; i < numRows; i++) {
				lines.add(input.readLine());
			}
			// read map data
			for (int i = 0; i < lines.size(); i++) {
				//System.out.println();
				String data = lines.get(i);
				String[] tokens = data.split("\\s+");
				for (int j = 0; j < tokens.length; j++) {
					map[i][j] = Integer.parseInt(tokens[j]);
					if (map[i][j] > 9 && map[i][j] < 98) tiles[i][j] = true;
					else tiles[i][j] = false;
					//System.out.print(map[i][j] + ", ");
				}
			}
			// load entity data
			String healData = input.readLine();
			if (healData.equals("null"));
			else {
				System.out.println("PARSING DATA");
				values = healData.split("-");
				for (String value : values) {
					String[] data = value.split(",");
					int xPos = Integer.parseInt(data[0]);
					int yPos = Integer.parseInt(data[1]);
					healSpawns.add(new SpawnPoint(0, xPos, yPos));
				}
			}
			String enemyData = input.readLine();
			if (enemyData.equals("null"));
			else {
				values = enemyData.split("-");
				for (int i = 0; i < values.length; i++) {
					String[] parse = values[i].split("_");
					int type = Integer.parseInt(parse[0]);
					String[] data = parse[1].split(",");
					int xPos = Integer.parseInt(data[0]);
					int yPos = Integer.parseInt(data[1]);
					enemySpawns.add(new SpawnPoint(type, xPos, yPos));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	// load file from certain file
	/*public static int[][] loadMapFile(String level) {
		int[][] map = null;
		try {
			// load file
			InputStream in = TileMap.class.getResourceAsStream(level);
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			// READ FILE
			input.readLine();
			input.readLine();
			input.readLine();
			input.readLine();
			int numRows = Integer.parseInt(input.readLine());
			int numCols = Integer.parseInt(input.readLine());
			map = new int[numRows][numCols];
			// load map data
			ArrayList<String> lines = new ArrayList<String>();
			for (int i = 0; i < numRows; i++) {
				lines.add(input.readLine());
			}
			// read map data
			for (int i = 0; i < lines.size(); i++) {
				//System.out.println();
				String data = lines.get(i);
				String[] tokens = data.split("\\s+");
				for (int j = 0; j < tokens.length; j++) {
					map[i][j] = Integer.parseInt(tokens[j]);
				}
			}
			input.readLine();
			input.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}*/
	
	public int getTileSize() { return tileSize; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public int[][] getMap() { return map; }
	public boolean isBlocked(int row, int col) { return tiles[row][col]; }
	public int getX() { return (int) x; }
	public int getY() { return (int) y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getSpawnX() { return spawnX; }
	public int getSpawnY() { return spawnY; }
	public int getWinRow() { return winRow; }
	public int getWinCol() { return winCol; }
	public int getWinX() { return winX; }
	public int getWinY() { return winY; }
	public int getBgValue() { return bgValue; }
	public ArrayList<SpawnPoint> getHealSpawns() { return healSpawns; }
	public ArrayList<SpawnPoint> getEnemySpawns() { return enemySpawns; }
	
	public void setPositionAbsolute(float x, float y) {
		this.x = x;
		this.y = y;
		fixBounds();
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	}
	
	public void setPosition(float x, float y) {
		this.x += (x - this.x) * ease;
		this.y += (y - this.y) * ease;
		fixBounds();
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	}
	
	protected void fixBounds() {
		if (x < xmin) x = xmin;
		if (x > xmax) x = xmax;
		if (y < ymin) y = ymin;
		if (y > ymax) y = ymax;
	}
	
	// draw the map
	public void render() {
		for (int row = rowOffset; row < rowOffset + rowsToDraw; row++) {
			if (row >= numRows) break;
			for (int col = colOffset; col < colOffset + colsToDraw; col++) {
				if (col >= numCols) break;
				if (map[row][col] == 0 || map[row][col] == 98 || map[row][col] == 99) continue;
				else {
					int tileRow = map[row][col] / 20;
					int tileCol = map[row][col] % 20;
					drawImage(tileset[tileRow][tileCol], x + col * tileSize, y + row * tileSize);
				}
			}
		}
	}
}