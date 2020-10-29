package credits;

public class Delay extends CreditsElement {
	
	private int duration;
	
	public Delay(int duration) {
		this.duration = duration;
	}
	
	public void update() {
		// nothing
	}
	
	public void render() {
		// nothing
	}
	
	public boolean finished() {
		long elapsed = (System.nanoTime() - start) / 1000000;
		return elapsed > duration;
	}
}