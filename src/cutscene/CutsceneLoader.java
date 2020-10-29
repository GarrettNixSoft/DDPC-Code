package cutscene;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import entity.Entity;
import event.EventLoader;
import event.EventTrigger;
import event.GameEvent;
import event.StrikeEvent;
import main.Render;
import util.data.Converter;
import util.data.DataCache;
import util.data.LevelCache;
import util.data.Parser;

public class CutsceneLoader {
	
	public static Cutscene[] loadCutscenes(String level, String map, LevelCache lc) {
		Cutscene[] scenes = null;
		try {
			// load key
			System.out.println("[CutsceneLoader] *** Loading cutscene file: " + "/cutscenes/" + level + "/" + map + "/" + map + ".key");
			InputStream in = CutsceneLoader.class.getResourceAsStream("/cutscenes/" + level + "/" + map + "/" + map + ".key");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			ArrayList<String> files = new ArrayList<String>();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				files.add(line.trim());
			}
			// load files
			scenes = new Cutscene[files.size()];
			System.out.println("[CutsceneLoader] This area has " + files.size() + " cutscenes.");
			for (int i = 0; i < scenes.length; i++) {
				scenes[i] = new Cutscene(lc);
				InputStream stream = CutsceneLoader.class.getResourceAsStream("/cutscenes/" + level + "/" + map + "/" + files.get(i) + ".ctsc");
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				ArrayList<String> events = new ArrayList<String>();
				while (true) {
					String line = br.readLine();
					if (line == null) break;
					events.add(line.trim());
				}
				System.out.println("[CutsceneLoader] There are " + (events.size() - 2) + " events in this scene.");
				// read dependency
				String dependencyLine = events.get(0);
				String[] dependencyTokens = dependencyLine.split(" ");
				int dependency = -1;
				if (!dependencyTokens[1].equals("none")) {
					for (int j = 0; j < files.size(); j++) {
						if (files.get(j).equals(dependencyTokens[1])) {
							if (j == i) {
								br.close();
								throw new IllegalArgumentException("Cutscene cannot depend on itself");
							}
							else dependency = j;
						}
					}
				}
				if (dependency != -1) scenes[i].setDependent(dependency);
				// read trigger
				String triggerLine = events.get(1);
				String[] triggerTokens = triggerLine.split(" ");
				String triggerType = triggerTokens[1];
				Trigger t = null;
				if (triggerType.equals("t")) {
					// time trigger
					System.out.println("[CutsceneLoader] Time trigger");
					float time = Float.parseFloat(triggerTokens[2]);
					t = new Trigger((int) (time * 1000));
				}
				else if (triggerType.equals("d")) {
					// delay trigger
					System.out.println("[CutsceneLoader] Delay trigger");
					float time = Float.parseFloat(triggerTokens[2]);
					int dependency2 = Integer.parseInt(triggerTokens[3]);
					t = new Trigger((int) (time * 1000), dependency2);
				}
				else if (triggerType.equals("a")) {
					// area trigger
					System.out.println("[CutsceneLoader] Area trigger");
					int[] coords = Parser.parseCoordinatesDouble(triggerTokens[2].trim());
					coords = Converter.tile(coords);
					Rectangle area = new Rectangle(coords[0], coords[1], (coords[2] - coords[0]), (coords[3] - coords[1]));
					System.out.println("[CutsceneLoader] Area begins at [" + coords[0] + "," + coords[1] + "] and ends at [" + coords[2] + "," + coords[3] + "]");
					t = new Trigger(area, DataCache.player);
				}
				else if (triggerType.equals("k")) {
					// kill trigger
					System.out.println("[CutsceneLoader] Kill trigger");
					String id = triggerTokens[2].trim();
					t = new Trigger(id);
				}
				else {
					System.out.println("IT'S NOT A VALID TRIGGER!");
					System.exit(-1);
				}
				scenes[i].setTrigger(t);
				// read events
				for (int event = 2; event < events.size(); event++) {
					String eventLine = events.get(event);
					String[] eventTokens = eventLine.trim().split(" ");
					if (eventTokens.length == 1) {
						if (eventTokens[0].equals("end")) break;
						// delay event
						float delay = Float.parseFloat(eventTokens[0]);
						DelayEvent e = new DelayEvent((int) (delay * 1000));
						scenes[i].addEvent(e);
						System.out.println("[CutsceneLoader] Delay event for " + delay + "s");
					}
					else {
						if (eventTokens[0].equals("camera")) {
							// camera focus event
							if (eventTokens[1].equals("lock")) {
								// get entity
								Entity e = null;
								if (eventTokens[2].equals("player")) {
									e = lc.player;
								}
								else if (eventTokens[2].equals("sayori")) {
									e = lc.characters[0];
								}
								else if (eventTokens[2].equals("natsuki")) {
									e = lc.characters[1];
								}
								else if (eventTokens[2].equals("yuri")) {
									e = lc.characters[2];
								}
								else if (eventTokens[2].equals("mirror")) {
									e = lc.characters[3];
								}
								else {
									// entity with ID
								}
								CameraLockEvent ev = new CameraLockEvent(lc.camera, e);
								scenes[i].addEvent(ev);
								System.out.println("[CutsceneLoader] Camera lock event on entity[hash=" + e.hashCode() + "]");
							}
							if (eventTokens[1].equals("focus")) {
								if (eventTokens[1].contains(",")) {
									// coordinate lock
									int[] coords = Parser.parseCoordinates(eventTokens[1]);
									float speed = Float.parseFloat(eventTokens[2]);
									CameraFocusEvent e = new CameraFocusEvent(lc, coords[0], coords[1], speed);
									scenes[i].addEvent(e);
									System.out.println("[CutsceneLoader] Camera lock event on coordinate (" + coords[0] + ", " + coords[1] + ")");
								}
								else {
									// get entity
									Entity e = null;
									String entity = null;
									if (eventTokens[2].equals("player")) {
										e = lc.player;
									}
									if (eventTokens[2].equals("sayori")) {
										e = lc.characters[0];
									}
									if (eventTokens[2].equals("natsuki")) {
										e = lc.characters[1];
									}
									if (eventTokens[2].equals("yuri")) {
										e = lc.characters[2];
									}
									if (eventTokens[2].equals("mirror")) {
										e = lc.characters[3];
									}
									entity = eventTokens[2];
									// get speed
									float speed = Float.parseFloat(eventTokens[3]);
									// entity focus
									CameraFocusEvent ev = new CameraFocusEvent(lc, e, speed);
									scenes[i].addEvent(ev);
									System.out.println("[CutsceneLoader] Camera focus event on entity[" + entity + "]");
								}
							}
							if (eventTokens[1].equals("end")) {
								// end lock
								float speed = Float.parseFloat(eventTokens[2]);
								int x = lc.tilemap.getWinX();
								int y = lc.tilemap.getWinY();
								// fix target
								if (x < Render.WIDTH / 2) x = Render.WIDTH / 2;
								else if (x > lc.tilemap.getWidth() - Render.WIDTH / 2) x = lc.tilemap.getWidth() - Render.WIDTH / 2;
								if (y < Render.HEIGHT / 2) y = Render.HEIGHT / 2;
								else if (y > lc.tilemap.getHeight() - Render.HEIGHT / 2) y = lc.tilemap.getHeight() - Render.HEIGHT / 2;
								CameraFocusEvent e = new CameraFocusEvent(lc, x, y, speed);
								scenes[i].addEvent(e);
							}
						}
						else if (eventTokens[0].equals("dialogue")) {
							scenes[i].addEvent(new DialogueEvent(eventTokens[1]));
							System.out.println("[CutsceneLoader] DialogueEvent with file " + eventTokens[1]);
						}
						else if (eventTokens[0].equals("event")) {
							// parse event data
							EventTrigger trigger = null;
							int index = 0;
							// load trigger
							if (eventTokens[1].equals("area")) {
								if (eventTokens[2].equals("r")) {
									// rectangle trigger
									int[] start = Parser.parseCoordinates(eventTokens[3]);
									start = Converter.tile(start);
									int[] size = Parser.parseCoordinates(eventTokens[4]);
									size = Converter.tilePlus(size);
									int[] location = Parser.parseCoordinates(eventTokens[5]);
									location = Converter.tileCenterX(location);
									trigger = new EventTrigger(new Rectangle(start[0], start[1], size[0], size[1]), location[0], location[1]);
									index = 6;
								}
								else if (eventTokens[2].equals("c")) {
									
								}
							}
							// load event
							GameEvent evt = EventLoader.getEvent(eventTokens[index], trigger, lc);
							if (evt instanceof StrikeEvent) {
								StrikeEvent se = (StrikeEvent) evt;
								if (eventTokens[index + 1] != null) {
									if (eventTokens[index + 1].equals("notile")) se.setTile(false);
									scenes[i].addEvent(new CutsceneGameEvent(se));
									System.out.println("[CutsceneLoader] Strike event - notile");
								}
							}
							else {
								System.out.println("[CutsceneLoader] Game event [type=" + eventTokens[index] + "]");
								scenes[i].addEvent(new CutsceneGameEvent(evt));
							}
						}
						else if (eventTokens[0].equals("spawn")) {
							// spawn event
							// get orientation
							boolean right = false;
							if (eventTokens[2].equals("r")) right = true;
							// get location
							if (eventTokens[3].equals("player")) { // relative spawns
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 0, right));
							}
							else if (eventTokens[3].equals("sayori")) {
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 1, right));
							}
							else if (eventTokens[3].equals("natsuki")) {
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 2, right));
							}
							else if (eventTokens[3].equals("yuri")) {
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 3, right));
							}
							else if (eventTokens[3].equals("monika")) {
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 4, right));
							}
							else if (eventTokens[3].equals("mirror")) {
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], 5, right));
							}
							else {
								// spawn at a specific location
								int[] coords = Parser.parseCoordinates(eventTokens[3]);
								scenes[i].addEvent(new SpawnEvent(lc, eventTokens[1], coords[0], coords[1], right));
							}
							System.out.println("[CutsceneLoader] Spawn event [entity=" + eventTokens[1] + "]");
						}
						else if (eventTokens[0].equals("entity")) {
							System.out.println("[CutsceneLoader] Entity event");
							// entity event
							// get entity
							String ent = eventTokens[2];
							int entity = 0;
							if (ent.equals("player")) {
								entity = 0;
								if (lc.player == null) System.out.println("[CutsceneLoader] Null player! AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
							}
							else if (ent.equals("sayori")) {
								entity = 1;
							}
							else if (ent.equals("natsuki")) {
								entity = 2;
							}
							else if (ent.equals("yuri")) {
								entity = 3;
							}
							else if (ent.equals("monika")) {
								entity = 4;
							}
							else if (ent.equals("mirror")) {
								entity = 5;
							}
							else {
								// get entity by ID
							}
							if (eventTokens[1].equals("glitch")) {
								// glitch event
								System.out.println("[CutsceneLoader] Glitch event [entity=" + eventTokens[2] + "]");
								// get duration
								float duration = Float.parseFloat(eventTokens[3]);
								// add event
								EntityEvent evt = new EntityEvent(entity, EntityEvent.GLITCH);
								evt.setGlitchDuration(duration);
								scenes[i].addEvent(evt);
							}
							else if (eventTokens[1].equals("move")) {
								// move event
								System.out.println("[CutsceneLoader] Move event [entity=" + eventTokens[2] + "]");
								// get direction
								int direction = Integer.parseInt(eventTokens[3]);
								// get ticks
								int ticks = Integer.parseInt(eventTokens[4]);
								// add event
								EntityEvent evt = new EntityEvent(entity, EntityEvent.MOVE);
								evt.setMoveTicks(ticks);
								evt.setDirection(direction);
								scenes[i].addEvent(evt);
							}
							else if (eventTokens[1].equals("jump")) {
								// jump event
								System.out.println("[CutsceneLoader] Jump event [entity=" + eventTokens[2] + "]");
								// get direction
								int direction = Integer.parseInt(eventTokens[3]);
								// get ticks
								int ticks = Integer.parseInt(eventTokens[4]);
								// add event
								EntityEvent evt = new EntityEvent(entity, EntityEvent.JUMP);
								evt.setJumpTicks(ticks);
								evt.setJumpDirection(direction);
								scenes[i].addEvent(evt);
							}
						}
						else if (eventTokens[0].equals("teleporter")) {
							// spawn teleporter event
							int type = Integer.parseInt(eventTokens[1]);
							int[] coords = Parser.parseCoordinatesDouble(eventTokens[2]);
							coords = Converter.tileCenter(coords);
							scenes[i].addEvent(new TeleporterEvent(lc, type, coords));
						}
						// TODO add more event types (char move, follow, etc)
					}
				}
			}
			System.out.println("[CutsceneLoader] *** Finished loading cutscenes for this map.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scenes;
	}
	
}