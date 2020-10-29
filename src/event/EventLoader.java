package event;

import java.awt.Rectangle;
import java.util.ArrayList;

import util.data.Converter;
import util.data.LevelCache;
import util.data.Parser;
import util.system.FileUtil;

public class EventLoader {
	
	// load game events
	public static GameEvent[] loadEvents(String level, String room, LevelCache lc) {
		try {
			ArrayList<String> lines = FileUtil.getFileData("/maps/expansion/" + level + "/" + room + "/" + room + ".events");
			System.out.println("[EventLoader] Loading events for level " + level + ", " + room);
			GameEvent[] events = new GameEvent[lines.size()];
			System.out.println("[EventLoader] There are " + lines.size() + " events");
			for (int i = 0; i < events.length; i++) {
				String[] tokens = lines.get(i).trim().split(" ");
				if (tokens[0].equals("area")) {
					if (tokens[1].equals("r")) {
						System.out.println("[EventLoader] Rectangle trigger");
						// rectangle trigger
						int[] start = Parser.parseCoordinates(tokens[2]);
						start = Converter.tile(start);
						int[] size = Parser.parseCoordinates(tokens[3]);
						size = Converter.tilePlus(size);
						int[] location = Parser.parseCoordinates(tokens[4]);
						location = Converter.tileCenterX(location);
						EventTrigger trigger = new EventTrigger(new Rectangle(start[0], start[1], size[0], size[1]), location[0], location[1]);
						// load event
						events[i] = getEvent(tokens[5], trigger, lc);
					}
					else if (tokens[2].equals("c")) {
						// coordinate trigger
						int[] coords1 = Parser.parseCoordinates(tokens[2]);
						coords1 = Converter.tile(coords1);
						int[] coords2 = Parser.parseCoordinates(tokens[3]);
						coords2 = Converter.tilePlus(coords2);
						int[] location = Parser.parseCoordinates(tokens[4]);
						location = Converter.tileCenterX(location);
						EventTrigger trigger = new EventTrigger(coords1[0], coords1[1], coords2[0], coords2[1], location[0], location[1]);
						// load event
						events[i] = getEvent(tokens[5], trigger, lc);
					}
				}
			}
			return events;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GameEvent getEvent(String type, EventTrigger trigger, LevelCache lc) {
		if (type.equals("glitch")) {
			
		}
		else if (type.equals("strike")) {
			System.out.println("[EventLoader] Loaded StrikeEvent at " + trigger.getArea().x + "," + trigger.getArea().y + " width " + trigger.getArea().width + " height " + trigger.getArea().height);
			return new StrikeEvent(trigger, lc, trigger.getLocation()[0], trigger.getLocation()[1]);
		}
		return null;
	}
	
}