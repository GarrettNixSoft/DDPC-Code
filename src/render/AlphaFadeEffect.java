package render;

public class AlphaFadeEffect extends AlphaEffect {
	
	// timing
	protected long timer;
	
	// speed
	protected float time;
	
	// max alpha
	protected float max;
	
	// default
	public AlphaFadeEffect() {
		time = 1.0f;
		alpha = max = 1.0f;
		timer = System.nanoTime();
	}
	
	// overloaded
	public AlphaFadeEffect(float time) {
		this.time = time;
		alpha = max = 1.0f;
		timer = System.nanoTime();
	}
	
	// modify the delay
	public void setTime(float time) {
		this.time = time;
	}
	
	// modify the max
	public void setMax(float max) {
		this.max = max;
	}
	
	// reset fade
	public void reset() {
		timer = System.nanoTime();
		alpha = max;
		System.out.println("[AlphaFadeEffect] reset");
	}
	
	// override (dangerous)
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public boolean complete() {
		return alpha == 0;
	}
	
	@Override
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		float percent = elapsed / (float) (time * 1000);
		percent = max - percent;
		alpha = percent;
		if (alpha < 0) alpha = 0;
	}

}