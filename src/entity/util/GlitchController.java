package entity.util;

import effects.entity.EntityEffect;
import util.MathUtil;

public class GlitchController extends EntityEffect {
	
	// animation to control
	private GlitchAnimation animation;
	
	// timing
	private long timer;
	private int delay;
	private int duration;
	private int averageDelay;
	private int delayVariance;
	private int averageDuration;
	private int durationVariance;
	
	public GlitchController(GlitchAnimation animation) {
		this.animation = animation;
		timer = System.nanoTime();
		averageDelay = 3000;
		delayVariance = 2000;
		averageDuration = 400;
		durationVariance = 200;
	}
	
	private void getNextValues() {
		delay = MathUtil.randInt(averageDelay - delayVariance, averageDelay + delayVariance);
		duration = MathUtil.randInt(averageDuration - durationVariance, averageDuration + durationVariance);
		timer = System.nanoTime();
	}
	
	@Override
	public void update() {
		animation.update();
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (active) {
			if (elapsed > duration) {
				active = false;
				getNextValues();
			}
		}
		else {
			if (elapsed > delay) {
				active = true;
				animation.glitch(duration);
				timer = System.nanoTime();
			}
		}
	}
	
}