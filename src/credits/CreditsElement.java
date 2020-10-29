package credits;

public abstract class CreditsElement {
	
	protected long start;
	
	public void start() {
		start = System.nanoTime();
	}
	
	public abstract void update();
	public abstract void render();
	public abstract boolean finished();
	
}