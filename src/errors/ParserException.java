package errors;

public class ParserException extends Exception {
	
	private static final long serialVersionUID = -8094983511705417427L;
	
	public ParserException() {
		super("failed to parse String");
	}
	
	public ParserException(String error) {
		super(error);
	}
	
}