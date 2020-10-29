package errors;

public class EffectTypeException extends Exception {
	
	private static final long serialVersionUID = 4169300978397210194L;
	
	public EffectTypeException() {
		super("wrong effect type");
	}
	
	public EffectTypeException(String error) {
		super(error);
	}
	
}