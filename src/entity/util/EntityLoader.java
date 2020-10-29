package entity.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import entity.Character;
import entity.Heal;
import entity.Teleporter;
import entity.Teleporter2Way;
import entity.enemy.Climber;
import entity.enemy.Enemy;
import entity.enemy.Ghost;
import entity.enemy.Glitch;
import entity.enemy.RainCloud;
import entity.enemy.Spider;
import tile.DynamicTilemap;
import util.data.Converter;
import util.data.DataCache;
import util.data.Parser;

public class EntityLoader {
	
	// tilemap
	private DynamicTilemap tm;
	
	// lists to populate
	private ArrayList<Enemy> enemies;
	private Character[] characters;
	private ArrayList<Interactive> interactives;
	private ArrayList<Collectible> collectibles;
	
	
	public EntityLoader(DynamicTilemap tm) {
		this.tm = tm;
		enemies = new ArrayList<Enemy>();
		characters = new Character[5];
		interactives = new ArrayList<Interactive>();
		collectibles = new ArrayList<Collectible>();
	}
	
	public void load(String level, String file) {
		try {
			// fetch file
			System.out.println("[EntityLoader] Loading entity file: " + "/maps/expansion/" + level + "/" + file + "/" + file +  ".edat");
			InputStream in = getClass().getResourceAsStream("/maps/expansion/" + level + "/" + file + "/" + file + ".edat");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// read file data
			ArrayList<String> lines = new ArrayList<String>();
			while(true) {
				String line = reader.readLine();
				if (line == null) break;
				else lines.add(line);
			}
			characters = new Character[5];
			// process file data
			for (String s : lines) {
				String line = s.trim();
				String[] tokens = line.split(" ");
				if (tokens[0].equals("heal")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					collectibles.add(new Heal(tm, DataCache.character, coords[0], coords[1]));
					System.out.println("[EntityLoader] Heal spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("cloud_1")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new RainCloud(tm, 0, coords[0], coords[1]));
					System.out.println("[EntityLoader] Cloud[0] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("cloud_2")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new RainCloud(tm, 0, coords[0], coords[1]));
					System.out.println("[EntityLoader] Cloud[1] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("spider_1")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new Spider(tm, 0, coords[0], coords[1] - 15));
					System.out.println("[EntityLoader] Spider[0] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("spider_2")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new Climber(tm, 0, coords[0], coords[1] - 15));
					System.out.println("[EntityLoader] Spider[1] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("ghost_1")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new Ghost(tm, 0, coords[0], coords[1] - 15));
					System.out.println("[EntityLoader] Ghost[0] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("ghost_2")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					enemies.add(new Ghost(tm, 0, coords[0], coords[1] - 15));
					System.out.println("[EntityLoader] Ghost[1] spawned at (" + coords[0] + "," + coords[1] + ")");
				}
				else if (tokens[0].equals("cloud_1_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("cloud_2_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("spider_1_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("spider_2_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("ghost_1_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("ghost_2_glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					// maybe
				}
				else if (tokens[0].equals("glitch")) {
					int[] coords = Parser.parseCoordinates(tokens[1]);
					coords = Converter.tileCenter(coords);
					Glitch g = new Glitch(tm, coords[0], coords[1]);
					if (tokens.length > 2) {
						g.setID(tokens[2]);
					}
					enemies.add(g);
				}
				else if (tokens[0].equals("teleporter_1")) {
					// add teleporter
					int[] coords = Parser.parseCoordinatesDouble(tokens[1]);
					coords = Converter.tileCenter(coords);
					Teleporter t = new Teleporter(tm, coords[0], coords[1], coords[2], coords[3]);
					if (tokens.length > 3) {
						t.setID(tokens[3]);
					}
					interactives.add(t);
				}
				else if (tokens[0].equals("teleporter_2")) {
					// add teleporter
					int[] coords = Parser.parseCoordinatesDouble(tokens[1]);
					coords = Converter.tileCenter(coords);
					Teleporter2Way t = new Teleporter2Way(tm, coords[0], coords[1], coords[2], coords[3]);
					if (tokens.length > 3) {
						t.setID(tokens[3]);
					}
					interactives.add(t);
				}
				else if (tokens[0].equals("sayori")) {
					// add sayori
					characters[0] = new Character(tm, Character.CHR_SAYORI);
					// get location
					String[] coords = tokens[1].split(",");
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					characters[0].setLocation(x * 60 + 30, y * 60 + 20);
					System.out.println("[EntityLoader] Sayori spawned at (" + (x * 60 + 30) + ", " + (y * 60 + 15) + ")");
					// get orientation (left/right)
					if (tokens[2].equals("r")) {
						characters[0].facingRight(true);
					}
					else if (tokens[2].equals("l")) {
						characters[0].facingRight(false);
					}
					else if (tokens[2].equals("f")) {
						characters[0].face();
					}
				}
				else if (tokens[0].equals("natsuki")) {
					// add natsuki
					characters[1] = new Character(tm, Character.CHR_NATSUKI);
					// get location
					String[] coords = tokens[1].split(",");
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					characters[1].setLocation(x * 60 + 30, y * 60 + 20);
					System.out.println("[EntityLoader] Natsuki spawned at (" + (x * 60 + 30) + ", " + (y * 60 + 15) + ")");
					// get orientation (left/right)
					if (tokens[2].equals("r")) {
						characters[1].facingRight(true);
					}
					else if (tokens[2].equals("l")) {
						characters[1].facingRight(false);
					}
					else if (tokens[2].equals("f")) {
						characters[1].face();
					}
				}
				else if (tokens[0].equals("yuri")) {
					// add yuri
					characters[2] = new Character(tm, Character.CHR_YURI);
					// get location
					String[] coords = tokens[1].split(",");
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					characters[2].setLocation(x * 60 + 30, y * 60 + 20);
					System.out.println("[EntityLoader] Yuri spawned at (" + (x * 60 + 30) + ", " + (y * 60 + 15) + ")");
					// get orientation (left/right)
					if (tokens[2].equals("r")) {
						characters[0].facingRight(true);
					}
					else if (tokens[2].equals("l")) {
						characters[2].facingRight(false);
					}
					else if (tokens[2].equals("f")) {
						characters[2].face();
					}
				}
				else if (tokens[0].equals("monika")) {
					// in case I ever use this
				}
				else if (tokens[0].equals("mirror")) {
					// add mirror
					characters[3] = new Character(tm, Character.CHR_MIRROR);
					// get location
					String[] coords = tokens[1].split(",");
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					characters[3].setLocation(x * 60 + 30, y * 60 + 20);
					System.out.println("[EntityLoader] Mirror spawned at (" + (x * 60 + 30) + ", " + (y * 60 + 15) + ")");
					// get orientation (left/right)
					if (tokens[2].equals("r")) {
						characters[4].facingRight(true);
					}
					else if (tokens[2].equals("l")) {
						characters[4].facingRight(false);
					}
					else if (tokens[2].equals("f")) {
						characters[4].face();
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Enemy> getEnemies() { return enemies; }
	public Character[] getCharacters() { return characters; }
	public ArrayList<Interactive> getInteractives() { return interactives; }
	public ArrayList<Collectible> getCollectibles() { return collectibles; }
	
}