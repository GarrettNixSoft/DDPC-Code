package render;

import util.MathUtil;

public class AlphaBounceEffect extends AlphaEffect {
	
	// bounds
	private float min;
	private float max;
	
	// config
	private float speed;
	private float bounceSpeed;
	private float bounceProgress;
	private float bounceIntensity;
	
	// timing
	private long timer;
	private int bounceTime;
	private boolean bouncing;
	
	// default
	public AlphaBounceEffect() {
		this.min = 0.5f;
		this.max = this.alpha = 0.75f;
		this.speed = 0.125f;
		this.bounceIntensity = 0.3f;
		timer = System.nanoTime();
		setBounceTime();
	}
	
	public AlphaBounceEffect(float min, float max, float speed, float bounceSpeed, float bounceIntensity) {
		this.min = min;
		this.max = this.alpha = max;
		this.speed = speed;
		this.bounceSpeed = bounceSpeed;
		this.bounceIntensity = bounceIntensity;
		timer = System.nanoTime();
		setBounceTime();
	}
	
	private void setBounceTime() {
		bounceTime = MathUtil.randInt(500, 3500);
		System.out.println("[AlphaBounceEffect] bounceTime = " + bounceTime);
	}
	
	private void bounce() {
		timer = System.nanoTime();
		bouncing = true;
	}
	
	@Override
	public void update() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (bouncing) {
			float diff = (float) (elapsed / 1000.0 * bounceSpeed);
			bounceProgress += diff;
			alpha += diff;
			//System.out.println("[AlphaBounceEffect] bouncing, diff=" + diff + ", progress=" + bounceProgress);
			if (alpha >= max) {
				alpha = max;
				bouncing = false;
				bounceProgress = 0;
				timer = System.nanoTime();
				setBounceTime();
			}
			if (bounceProgress >= bounceIntensity) {
				bouncing = false;
				bounceProgress = 0;
				timer = System.nanoTime();
				setBounceTime();
			}
		}
		else {
			if (elapsed > bounceTime) {
				System.out.println("[AlphaBounceEffect] time reached");
				bounce();
			}
			else {
				float diff = (float) (elapsed / 1000.0 * speed);
				alpha -= diff;
				//System.out.println("[AlphaBounceEffect] fade, diff = " + diff + ", alpha = " + alpha);
				if (alpha < min) {
					System.out.println("[AlphaBounceEffect] min reached");
					bounce();
				}
			}
		}
	}
	
	@Override
	public boolean complete() {
		return false;
	}
	
}