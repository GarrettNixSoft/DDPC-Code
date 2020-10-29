package effects;

import java.util.ArrayList;
import java.util.List;

public class EventQueue {
	
	private List<Event> events;
	
	public EventQueue() {
		events = new ArrayList<Event>();
	}
	
	public boolean hasMoreEvents() {
		return events.size() > 0;
	}
	
	public Event poll() {
		if (events.size() > 0) {
			Event e = events.get(0);
			events.remove(0);
			return e;
		}
		else return null;
	}
	
	public void queue(Event e) {
		events.add(e);
	}
	
	public void clear() {
		events = new ArrayList<Event>();
	}
	
}