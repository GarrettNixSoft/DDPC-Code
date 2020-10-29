package tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cutscene.Camera;
import cutscene.Cutscene;
import cutscene.CutsceneLoader;
import entity.PlayerV2;
import entity.util.EntityLoader;
import event.EventLoader;
import event.GameEvent;
import gameState.PlayStateEP;
import util.data.LevelCache;

public class MapLoader {
	
	// maps and entities
	private DynamicTilemap[] maps;
	private EntityLoader[] entityLoaders;
	private GameEvent[][] events;
	private Cutscene[][] cutscenes;
	
	// winter theme
	private boolean winter;
	
	public MapLoader() {
		// this object now exists
	}
	
	public void setWinterTheme(boolean winter) {
		this.winter = winter;
	}
	
	public LevelCache[] loadLevel(String level, PlayStateEP parent) {
		LevelCache[] levels = null;
		maps = null;
		try {
			// load key
			InputStream in = MapLoader.class.getResourceAsStream("/maps/expansion/" + level + "/" + level + ".key");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			ArrayList<String> files = new ArrayList<String>();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				files.add(line.trim());
			}
			// load map files and entities
			levels = new LevelCache[files.size()];
			maps = new DynamicTilemap[files.size()];
			entityLoaders = new EntityLoader[files.size()];
			events = new GameEvent[files.size()][];
			cutscenes = new Cutscene[files.size()][];
			System.out.println("[MapLoader] There are " + files.size() + " maps to load for this level");
			for (int i = 0; i < files.size(); i++) {
				maps[i] = new DynamicTilemap("/maps/expansion/" + level + "/" + files.get(i) + "/" + files.get(i) + ".dmap", winter);
				if (i == files.size() - 1) maps[i].setLast();
				entityLoaders[i] = new EntityLoader(maps[i]);
				entityLoaders[i].load(level, files.get(i));
				LevelCache lc = new LevelCache();
				lc.tilemap = maps[i];
				lc.camera = new Camera(lc.tilemap);
				lc.camera.setPosition(lc.tilemap.getSpawnX(), lc.tilemap.getSpawnY());
				lc.player = new PlayerV2(lc);
				lc.player.setPosition(lc.tilemap.getSpawnX(), lc.tilemap.getSpawnY());
				lc.characters = entityLoaders[i].getCharacters();
				lc.winX = maps[i].getWinX();
				lc.winY = maps[i].getWinY();
				lc.parent = parent;
				levels[i] = lc;
				events[i] = EventLoader.loadEvents(level, files.get(i), lc);
				if (events[i] == null) System.out.println("[MapLoader] EventLoader returned null list!");	
				cutscenes[i] = CutsceneLoader.loadCutscenes(level, files.get(i), lc);
			}
			System.out.println("[MapLoader] All maps loaded. Events array is " + events.length + " rows long. First array has " + events[0].length + " events.");
			return levels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DynamicTilemap[] getMaps() { return maps; }
	public EntityLoader[] getEntities() { return entityLoaders; }
	public GameEvent[][] getEvents() { return events; }
	public Cutscene[][] getCutscenes() { return cutscenes; }
	
}