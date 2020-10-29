package errors;

public class EventTypeException extends Exception {
	
	private static final long serialVersionUID = 5038575686351110799L;
	
	public EventTypeException() {
		super("wrong event type");
	}
	
	public EventTypeException(String error) {
		super(error);
	}
	
}