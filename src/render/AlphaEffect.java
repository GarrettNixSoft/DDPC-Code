package render;

public abstract class AlphaEffect {
	
	// timing
	protected long timer;
	
	// alpha value
	protected float alpha;
	
	public float getAlpha() { return alpha; }
	
	public abstract void update();
	public abstract boolean complete();
	
}