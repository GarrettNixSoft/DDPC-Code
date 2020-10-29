package tile;

import static main.Render.drawImage;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.SpawnPoint;
import main.Render;
import util.data.DataCache;

public class DynamicTilemap extends TileMap {
	
	// tiles
	private DynamicTile[][] tiles;
	private Texture[][] tileset;
	private Texture[][][] glitch_tileset;
	protected int spawnRow;
	
	// next
	private boolean last;
	private Rectangle exit;
	
	// winter theme
	private boolean winter;
	
	public DynamicTilemap(String mapFile) {
		// set up size
		tileSize = 60;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("[DynamicTilemap] cols to draw: " + colsToDraw);
		System.out.println("[DynamicTilemap] rows to draw: " + rowsToDraw);
		healSpawns = new ArrayList<SpawnPoint>();
		enemySpawns = new ArrayList<SpawnPoint>();
		ease = 0.07f;
		loadMap(mapFile);
	}
	
	public DynamicTilemap(String mapFile, boolean winter) {
		// set up size
		this.winter = winter;
		tileSize = 60;
		rowsToDraw = Render.HEIGHT / tileSize + 2;
		colsToDraw = Render.WIDTH / tileSize + 2;
		System.out.println("[DynamicTilemap] cols to draw: " + colsToDraw);
		System.out.println("[DynamicTilemap] rows to draw: " + rowsToDraw);
		healSpawns = new ArrayList<SpawnPoint>();
		enemySpawns = new ArrayList<SpawnPoint>();
		ease = 0.07f;
		loadMap(mapFile);
	}
	
	public void setWinter(boolean winter) {
		this.winter = winter;
	}
	
	protected int[][] loadMap(String mapFile) {
		// FETCH MAP FILE
		System.out.println("[DynamicTilemap] LOADING MAP FILE: " + mapFile);
		// check for filetype
		if (!mapFile.endsWith(".dmap")) {
			mapFile += ".dmap";
		}
		// get the file
		InputStream in = getClass().getResourceAsStream(mapFile);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		// PROCESS FILE DATA
		try {
			// get tileset
			int set = Integer.parseInt(input.readLine());
			if (winter) {
				switch(set) {
				case 0:
					tileset = Textures.s_snow_level;
					glitch_tileset = Textures.glitchTiles1;
					break;
				case 1:
					tileset = Textures.n_snow_level;
					glitch_tileset = Textures.glitchTiles2;
					break;
				case 2:
					tileset = Textures.y_snow_level;
					glitch_tileset = Textures.glitchTiles3;
					break;
				}
			}
			else {
				switch(set) {
					case 0:
						tileset = Textures.tileset1;
						glitch_tileset = Textures.glitchTiles1;
						break;
					case 1:
						tileset = Textures.tileset2;
						glitch_tileset = Textures.glitchTiles2;
						break;
					case 2:
						tileset = Textures.tileset3;
						glitch_tileset = Textures.glitchTiles3;
						break;
				}
			}
			// load spawn point
			String spawnLine = input.readLine();
			String[] values = spawnLine.split(",");
			spawnRow = Integer.parseInt(values[0]);
			spawnCol = Integer.parseInt(values[1]);
			spawnX = spawnCol * tileSize + tileSize / 2;
			spawnY = (spawnRow + 1) * tileSize - 40;
			// load win point
			String winLine = input.readLine();
			values = winLine.split(" ");
			String[] start = values[0].split(",");
			String[] dimensions = values[1].split(",");
			int endCol = Integer.parseInt(start[0]);
			int endRow = Integer.parseInt(start[1]);
			int tileWidth = Integer.parseInt(dimensions[0]);
			int tileHeight = Integer.parseInt(dimensions[1]);
			exit = new Rectangle(endCol * tileSize, endRow * tileSize, tileWidth * tileSize, tileHeight * tileSize);
			System.out.println("[DynamicTilemap] End col: " + endCol + ", end row: " + endRow);
			winX = exit.x + exit.width / 2;
			winY = exit.y + exit.height / 2 - 40;
			System.out.println("[DynamicTilemap] winX: " + winX);
			System.out.println("[DynamicTilemap] winY: " + winY);
			// get dimensions
			numRows = Integer.parseInt(input.readLine());
			numCols = Integer.parseInt(input.readLine());
			map = new int[numRows][numCols];
			tiles = new DynamicTile[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			System.out.println("[DynamicTilemap] map width: " + width);
			System.out.println("[DynamicTilemap] map height: " + height);
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
			//System.out.println("[DynamicTilemap] glitch_tileset is " + glitch_tileset.length + " arrays long, each is " + glitch_tileset[0].length + " rows by " + glitch_tileset[0][0].length + " cols");
			// process map data
			for (int i = 0; i < lines.size(); i++) {
				//System.out.println();
				String data = lines.get(i);
				String[] tokens = data.split("\\s+");
				for (int j = 0; j < tokens.length; j++) {
					int tile = Integer.parseInt(tokens[j]);
					generateTile(i, j, tile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to load map " + mapFile + ".", "File Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	// set tile
	private void generateTile(int r, int c, int tile) {
		map[r][c] = tile;
		int val = tile;
		if (val < 20) {
			int tileX = val % 10;
			int tileY = val / 10;
			Texture[] tex = new Texture[1];
			tex[0] = tileset[tileY][tileX];
			tiles[r][c] = new StaticTile(tex, tileY > 0);
		}
		else if (val < 40) {
			int tileX = val % 10;
			int tileY = val / 10 - 2;
			Texture[] tex = new Texture[5];
			tex[0] = tileset[tileY][tileX];
			for (int num = 1; num < 5; num++) {
				tex[num] = glitch_tileset[num - 1][tileY][tileX];
			}
			tiles[r][c] = new SwitchTile(tex, tileY > 0);
		}
		else {
			if (val == 98) { // win
				Texture[] tex = new Texture[1];
				tiles[r][c] = new StaticTile(tex, false);
			}
			else if (val == 99) { // spawn
				Texture[] tex = new Texture[1];
				tiles[r][c] = new StaticTile(tex, false);
			}
			else {
				System.out.println("value");
				int value = val - 40;
				tiles[r][c] = new BounceTile(DataCache.character, value);
			}
		}
	}
	
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
	public Rectangle getExit() { return exit; }
	public Rectangle getExitScreen() {
		Rectangle result = new Rectangle((int)exit.getX(), (int)exit.getY(), (int)exit.getWidth(), (int)exit.getHeight());
		result.translate(getX(), getY());
		return result;
	}
	
	// this is the last map for the level
	public void setLast() { last = true; }
	public boolean isLast() { return last; }
	
	// glitch lightning effect
	public void destroy(int[][] config, int x, int y) {
		int centerCol = x / tileSize;
		int centerRow = y / tileSize;
		int offsetRow = -(config.length / 2);
		int offsetCol = -(config[0].length / 2);
		for (int r = 0; r < config.length; r++) {
			int rowIndex = centerRow + r + offsetRow;
			if (rowIndex < 0 || rowIndex >= numRows) continue;
			for (int c = 0; c < config[r].length; c++) {
				int colIndex = centerCol + c + offsetCol;
				if (colIndex < 0 || colIndex >= numCols) continue;
				tileDestroy(rowIndex, colIndex, config[r][c]);
			}
		}
	}
	
	private void tileDestroy(int row, int col, int value) {
		int tile = map[row][col];
		if (value == 0) map[row][col] = 0;
		else {
			int tileValue = tile % 20;
			if (value == 1) {
				if (tileValue == 0) {
					map[row][col] = 30 + (int) (Math.random() * 6);
				}
				else map[row][col] = tileValue + 20;
			}
			else { // value == 2
				if (tileValue == 0) map[row][col] = 10;
				else map[row][col] = tileValue;
			}
		}
		generateTile(row, col, map[row][col]);
	}
	
	public void update() {
		for (int row = rowOffset; row < rowOffset + rowsToDraw; row++) {
			if (row >= numRows || row < 0) break;
			for (int col = colOffset; col < colOffset + colsToDraw; col++) {
				if (col >= numCols || col < 0) break;
				tiles[row][col].update();
			}
		}
	}
	
	public void render() {
		for (int row = rowOffset; row < rowOffset + rowsToDraw; row++) {
			if (row >= numRows || row < 0) break;
			for (int col = colOffset; col < colOffset + colsToDraw; col++) {
				if (col >= numCols || col < 0) break;
				if (map[row][col] == 0 || map[row][col] == 98 || map[row][col] == 99) continue;
				else {
					drawImage(tiles[row][col].getTex(), x + col * tileSize, y + row * tileSize);
				}
			}
		}
	}
	
}