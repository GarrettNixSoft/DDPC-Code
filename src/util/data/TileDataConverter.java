package util.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import entity.SpawnPoint;
import tile.TileData;
import tile.TileMap;

public class TileDataConverter {
	
	public String[] convertMapData(TileMap tilemap) {
		// get map data
		int[][] map = tilemap.getMap();
		// use that to determine how many lines are needed
		String[] result = new String[map.length + 7];
		// insert the first line (tileset ID)
		result[0] = tilemap.getTilesetID() + "";
		// insert the next 2 lines (row count and col count)
		result[1] = tilemap.getNumRows() + "";
		result[2] = tilemap.getNumCols() + "";
		// insert the next 2 lines (spawn point and end point)
		result[3] = tilemap.getSpawnX() + "," + tilemap.getSpawnY();
		result[4] = tilemap.getWinX() + "," + tilemap.getWinY();
		// format the map data and insert it
		for (int r = 0; r < map.length; r++) {
			String line = "";
			for (int c = 0; c < map[r].length; c++) {
				line += map[r][c];
				if (c < map[r].length - 1) line += ",";
			}
			result[r + 4] = line;
		}
		// format the spawn data and insert it
		String healLine = "";
		for (SpawnPoint sp : tilemap.getHealSpawns()) {
			healLine += sp.getType() + "_" + sp.getX() + "," + sp.getY() + "-";
		}
		healLine = healLine.substring(0, healLine.length() - 1);
		result[map.length + 5] = healLine;
		String enemyLine = "";
		for (SpawnPoint sp : tilemap.getEnemySpawns()) {
			enemyLine += sp.getType() + "_" + sp.getX() + "," + sp.getY() + "-";
		}
		enemyLine = enemyLine.substring(0, enemyLine.length() - 1);
		result[map.length + 6] = enemyLine;
		return result;
	}
	
	public TileData getTileData(String mapFile, boolean external) {
		if (!mapFile.endsWith("map")) {
			throw new IllegalArgumentException("File is not a map file");
		}
		try {
			// open file
			InputStream in = null;
			if (external) in = new FileInputStream(mapFile);
			else in = getClass().getResourceAsStream(mapFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// read top data
			int tilesetID = Integer.parseInt(reader.readLine());
			int bgValue = Integer.parseInt(reader.readLine());
			int[] spawnCoords = Parser.parseCoordinates(reader.readLine());
			int[] winCoords = Parser.parseCoordinates(reader.readLine());
			int numRows = Integer.parseInt(reader.readLine());
			int numCols = Integer.parseInt(reader.readLine());
			// read map data
			int[][] map = new int[numRows][numCols];
			for (int r = 0; r < numRows; r++) {
				String line = reader.readLine();
				String[] tokens = line.split(" ");
				for (int c = 0; c < tokens.length; c++) {
					map[r][c] = Integer.parseInt(tokens[c]);
				}
			}
			// read heal data
			ArrayList<SpawnPoint> healSpawns = new ArrayList<SpawnPoint>();
			// get separate entries
			String healLine = reader.readLine();
			String[] healEntries = healLine.split("-");
			// parse the data per entry
			for (String s : healEntries) {
				if (s.equals("null")) break;
				int[] coords = Parser.parseCoordinates(s);
				healSpawns.add(new SpawnPoint(0, coords));
			}
			// read enemy data
			ArrayList<SpawnPoint> enemySpawns = new ArrayList<SpawnPoint>();
			// get separate entires
			String enemyLine = reader.readLine();
			String[] enemyEntries = enemyLine.split("-");
			// parse the data per entry
			for (String s :enemyEntries) {
				if (s.equals("null")) break;
				String[] sections = s.split("_");
				int type = Integer.parseInt(sections[0]);
				int[] coords = Parser.parseCoordinates(sections[1]);
				enemySpawns.add(new SpawnPoint(type, coords));
			}
			// insert data into new TileData object and return it
			TileData result = new TileData();
			result.setTilesetID(tilesetID);
			result.setBgValue(bgValue);
			result.setMap(map);
			result.setSpawnCoords(spawnCoords);
			result.setWinCoords(winCoords);
			result.setHealSpawns(healSpawns);
			result.setEnemySpawns(enemySpawns);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}