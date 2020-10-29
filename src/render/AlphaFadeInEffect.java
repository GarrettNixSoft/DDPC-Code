package render;

public class AlphaFadeInEffect extends AlphaFadeEffect {
	
	public AlphaFadeInEffect() {
		super();
		alpha = 0;
	}
	
	public AlphaFadeInEffect(float time) {
		super(time);
		alpha = 0;
	}
	
	public void max() {
		alpha = max;
	}
	
	@Override
	public boolean complete() {
		return alpha == max;
	}
	
	@Override
	public void reset() {
		alpha = 0;
		timer = System.nanoTime();
	}
	
	@Override
	public void update() {
		if (alpha >= max) return;
		long elapsed = (System.nanoTime() - timer) / 1000000;
		float percent = elapsed / (float) (time * 1000);
		alpha = percent * max;
		if (alpha > max) {
			alpha = max;
		}
	}
	
}