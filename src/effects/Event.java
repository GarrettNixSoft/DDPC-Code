package effects;

public class Event {
	
	// types
	public static final int EMPTY = 0;
	public static final int LIGHTNING = 1;
	
	// id
	private int eventID;
	
	public Event(int eventID) {
		this.eventID = eventID;
	}
	
	public int getEventID() {
		return eventID;
	}
	
}