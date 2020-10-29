package tile;

import static main.Render.drawImage;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.SpawnPoint;
import main.Render;
import util.data.Converter;
import util.data.DataCache;
import util.data.Settings;
import util.data.TileDataConverter;

public class TileMap {
	
	// map data
	protected int[][] map;
	protected Tile[][] tiles;
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
	protected int tilesetID;
	protected int tileSize;
	protected int numRows;
	protected int numCols;
	protected int width;
	protected int height;
	
	// position
	protected float x;
	protected float y;
	protected float ease;
	
	// shake modifiers
	protected float sx, prevsx;
	protected float sy, prevsy;
	protected boolean smooth;
	protected boolean allowBoundaryViolation;
	
	// bounds
	protected int xmin;
	protected int ymin;
	protected int xmax;
	protected int ymax;
	protected boolean fixX;
	protected boolean fixY;
	
	// custom bounds
	protected int xmargin;
	protected int ymargin;
	
	//rendering
	protected int rowOffset;
	protected int colOffset;
	protected int rowsToDraw;
	protected int colsToDraw;
	
	// stupid hack
	private boolean thisIsOnlyHereToDifferentiateTheConstructorsLol;
	
	public TileMap(String level) {
		// set up size
		tileSize = 60;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("[TileMap] Cols to draw: " + colsToDraw);
		System.out.println("[TileMap] Rows to draw: " + rowsToDraw);
		// get tileset
		//if (level.startsWith("s")) tileset = Textures.tileset1;
		//else if (level.startsWith("n")) tileset = Textures.tileset2;
		//else if (level.startsWith("y")) tileset = Textures.tileset3;
		//else tileset = Textures.tileset4;
		healSpawns = new ArrayList<SpawnPoint>();
		enemySpawns = new ArrayList<SpawnPoint>();
		// TEMP
		tileset = Textures.tileset1;
		ease = 0.07f;
		// load map
		if (DataCache.level > 10) loadMap("/maps/expansion/" + level + ".map");
		else if (level.equals("dummy")) loadEmptyMap();
		else loadMap("/maps/original/" + level + ".map");
	}
	
	public TileMap(String level, boolean thisIsOnlyHereToDifferentiateTheConstructorsLol) {
		// set up size
		this.thisIsOnlyHereToDifferentiateTheConstructorsLol = true;
		tileSize = 60;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("[TileMap] Cols to draw: " + colsToDraw);
		System.out.println("[TileMap] Rows to draw: " + rowsToDraw);
		// get tileset
		//if (level.startsWith("s")) tileset = Textures.tileset1;
		//else if (level.startsWith("n")) tileset = Textures.tileset2;
		//else if (level.startsWith("y")) tileset = Textures.tileset3;
		//else tileset = Textures.tileset4;
		healSpawns = new ArrayList<SpawnPoint>();
		enemySpawns = new ArrayList<SpawnPoint>();
		// TEMP
		tileset = Textures.tileset1;
		ease = 0.07f;
		// load map
		loadMap(level);
	}
	
	public TileMap() {
		tileSize = 60;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("[TileMap] Cols to draw: " + colsToDraw);
		System.out.println("[TileMap] Rows to draw: " + rowsToDraw);
		// TEMP
		tileset = Textures.tileset1;
		ease = 0.07f;
		loadEmptyMap();
	}
	
	protected void loadEmptyMap() {
		numRows = 16;
		numCols = 50;
		map = new int[numRows][numCols];
		width = numCols * tileSize;
		height = numRows * tileSize;
		System.out.println("[TileMap] Tilemap is " + width + "x" + height);
		xmin = Render.WIDTH - width;
		xmax = 0;
		ymin = Render.HEIGHT - height;
		ymax = 0;
		spawnRow = spawnCol = 0;
		spawnX = spawnCol * tileSize + tileSize / 2;
		spawnY = (spawnRow + 1) * tileSize - 40;
		map[spawnRow][spawnCol] = 99;
		winRow = 0;
		winCol = 1;
		winX = winCol * tileSize + tileSize / 2;
		winY = (winRow + 1) * tileSize - 40; // MAYBE TWEAK THIS
		bgValue = 0;
		map[winRow][winCol] = 98;
		tiles = new Tile[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i < 14) tiles[i][j] = new Tile(false);
				else tiles[i][j] = new Tile(true);
			}
		}
	}
	
	protected int[][] loadMap(String level) {
		System.out.println("[TileMap] Loading: " + level);
		try {
			// check for special level file
			if (level.equals("/maps/original/s4.map")) {
				if (DataCache.s_end) level = "/maps/original/s4_1.map";
				else level = "/maps/original/s4_0.map";
				System.out.println("CHANGE");
			}
			else System.out.println("[TileMap] No change, level is " + level);
			// check file
			if (thisIsOnlyHereToDifferentiateTheConstructorsLol) {
				level = level.replace("\\", "/");
			}
			// load level
			TileData data = new TileDataConverter().getTileData(level, thisIsOnlyHereToDifferentiateTheConstructorsLol);
			// READ FILE
			int set = data.getTilesetID();
			Texture[][][] tilesets = new Texture[3][][];
			if (Settings.winterTheme) {
				tilesets[0] = Textures.s_snow_level;
				tilesets[1] = Textures.n_snow_level;
				tilesets[2] = Textures.y_snow_level;
			}
			else {
				tilesets[0] = Textures.tileset1;
				tilesets[1] = Textures.tileset2;
				tilesets[2] = Textures.tileset3;
			}
			switch (set) {
				case 0:
					tileset = tilesets[0];
					break;
				case 1:
					tileset = tilesets[1];
					break;
				case 2:
					tileset = tilesets[2];
					break;
				case 3:
					switch (DataCache.level) {
						case 0:
							tileset = Textures.tileset1;
							break;
						case 1:
							tileset = Textures.tileset2;
							break;
						case 2:
							tileset = Textures.tileset3;
							break;
						case 3:
							tileset = Textures.tileset1;
							break;
					}
					break;
			}
			// set map data
			bgValue = data.getBgValue();
			map = data.getMap();
			numRows = map.length;
			numCols = map[0].length;
			int[] spawnCoords = Converter.tileCenter(data.getSpawnCoords());
			spawnX = spawnCoords[1]; // these are flipped for some reason
			spawnY = spawnCoords[0] - 10;
			int[] winCoords = Converter.tileCenter(data.getWinCoords());
			winX = winCoords[1];
			winY = winCoords[0] - 10;
			System.out.println("[TileMap] Spawn: " + spawnX + "," + spawnY);
			System.out.println("[TileMap] Win: " + winX + "," + winY);
			tiles = new Tile[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			System.out.println("[TileMap] Map width: " + width);
			System.out.println("[TileMap] Map height: " + height);
			// set bounds
			xmin = Render.WIDTH - width;
			xmax = 0;
			ymin = Render.HEIGHT - height;
			ymax = 0;
			// process map data
			for (int r = 0; r < map.length; r++) {
				for (int c = 0; c < map[r].length; c++) {
					if (map[r][c] > 9 && map[r][c] < 98) tiles[r][c] = new Tile(true);
					else tiles[r][c] = new Tile(false);
				}
			}
			// get entity data
			healSpawns = data.getHealSpawns();
			enemySpawns = data.getEnemySpawns();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public int getTilesetID() { return tilesetID; }
	public int getTileSize() { return tileSize; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public int[][] getMap() { return map; }
	public boolean isBlocked(int row, int col) { return tiles[row][col].isBlocked(); }
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
	
	// set margins
	public void setMargins(int xmargin, int ymargin) {
		this.xmargin = xmargin;
		this.ymargin = ymargin;
		System.out.println("[TileMap] Margins set to " + xmargin + "," + ymargin);
		xmin = xmin + xmargin;
		ymin = ymin + ymargin;
		xmax = xmax - xmargin;
		ymax = ymax - ymargin;
		System.out.println("xmin: " + xmin + ", xmax: " + xmax + ", ymin: " + ymin + ", ymax: " + ymax);
	}
	
	// shake
	public void shake(float sx, float sy, boolean smooth) {
		this.sx = sx;
		this.sy = sy;
		this.smooth = smooth;
	}
	
	public void setAllowBoundaryViolation(boolean b) {
		allowBoundaryViolation = b;
	}
	
	// check things on map
	public int getGroundY(int x) {
		int col = x / tileSize;
		for (int r = 0; r < map.length; r++) {
			if (map[r][col] >= 10) return r * tileSize;
		}
		return height;
	}
	
	// bounds
	public int getXmin() { return xmin; }
	public int getXmax() { return xmax; }
	public int getYmin() { return ymin; }
	public int getYmax() { return ymax; }
	public boolean fixX() { return fixX; }
	public boolean fixY() { return fixY; }
	
	public void setPositionAbsolute(float x, float y) {
		if (allowBoundaryViolation) System.out.println("[TileMap] Boundaries may be crossed.");
		this.x = x;
		this.y = y;
		if (sx!= 0 || sy != 0) {
			this.x -= prevsx;
			this.y -= prevsy;
			if (allowBoundaryViolation) {
				fixBounds();
				if (smooth) {
					this.x += (y + sx - this.x) * ease;
					this.y += (y + sy - this.y) * ease;
				}
				else {
					this.x += sx;
					this.y += sy;
				}
			}
			else {
				if (smooth) {
					this.x += (y + sx - this.x) * ease;
					this.y += (y + sy - this.y) * ease;
				}
				else {
					this.x += sx;
					this.y += sy;
				}
				fixBounds();
			}
		}
		else {
			this.x = x;
			this.y = y;
			fixBounds();
		}
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
		prevsx = sx;
		prevsy = sy;
		sx = sy = 0;
	}
	
	public void setPosition(float x, float y) {
		if (sx != 0 || sy != 0) {
			this.x -= prevsx;
			this.y -= prevsy;
			if (allowBoundaryViolation) {
				fixBounds();
				if (smooth) { // TODO this is broken
					x += sx;
					y += sy;
					this.x += (x - this.x) * ease;
					this.y += (y - this.y) * ease;
				}
				else { // this works fine though
					this.x = x + sx;
					this.y = y + sy;
				}
			}
			else {
				if (smooth) { // TODO this is also broken
					x += sx;
					y += sy;
					this.x += (x - this.x) * ease;
					this.y += (y - this.y) * ease;
				}
				else { // this also works fine
					this.x = x + sx;
					this.y = y + sy;
				}
				fixBounds();
			}
		}
		else {
			this.x += (x - this.x) * ease;
			this.y += (y - this.y) * ease;
			fixBounds();
		}
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
		prevsx = sx;
		prevsy = sy;
		sx = sy = 0;
		//System.out.println("[TileMap] Position set, colOffset=" + colOffset + ",rowOffset=" + rowOffset);
	}
	
	protected void fixBounds() {
		//System.out.println("[TileMap] Fix bounds");
		fixX = fixY = false;
		if (x < xmin) {
			x = xmin;
			fixX = true;
		}
		if (x > xmax) {
			x = xmax;
			fixX = true;
		}
		if (y < ymin) {
			y = ymin;
			fixY = true;
		}
		if (y > ymax) {
			y = ymax;
			fixY = true;
		}
	}
	
	public boolean isVisible(int x, int y) {
		float screenX = x + this.x;
		float screenY = y + this.y;
		boolean off = screenX < 0 ||
					screenX > Render.WIDTH ||
					screenY < 0 ||
					screenY > Render.HEIGHT;
		return !off;
	}
	
	// draw the map
	public void render() {
		for (int row = rowOffset; row < rowOffset + rowsToDraw; row++) {
			if (row >= numRows) break;
			for (int col = colOffset; col < colOffset + colsToDraw; col++) {
				if (col >= numCols) break;
				if (map[row][col] == 0 || map[row][col] == 98 || map[row][col] == 99) continue;
				else {
					int tileRow = map[row][col] / 10;
					int tileCol = map[row][col] % 10;
					drawImage(tileset[tileRow][tileCol], x + col * tileSize, y + row * tileSize);
				}
			}
		}
	}
}