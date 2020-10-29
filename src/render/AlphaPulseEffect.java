package render;

public class AlphaPulseEffect extends AlphaEffect {
	
	// timing
	private int frequency;
	
	// scale
	private float startAlpha;
	private float range;
	
	// default constructor
	public AlphaPulseEffect() {
		timer = System.nanoTime();
		frequency = 1000;
		startAlpha = 0.5f;
		range = 0.25f;
	}
	
	// constructor with frequency
	public AlphaPulseEffect(int frequency) {
		timer = System.nanoTime();
		this.frequency = frequency;
		this.startAlpha = 0.5f;
		this.range = 0.25f;
	}
	
	// fully overloaded
	public AlphaPulseEffect(int frequency, float startAlpha, float range) {
		timer = System.nanoTime();
		this.frequency = frequency;
		this.startAlpha = startAlpha;
		this.range = range;
	}
	
	// this one is never complete
	public boolean complete() {
		return false;
	}
	
	public void update() {
		// get time elapsed
		long elapsed = (System.nanoTime() - timer) / 1000000;
		// update alpha
		float modifier = (float) (Math.cos(elapsed / (float) frequency));
		alpha = (float) (range * modifier + startAlpha);
	}
	
}