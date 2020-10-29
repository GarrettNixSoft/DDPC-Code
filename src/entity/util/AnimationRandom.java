package entity.util;

import util.MathUtil;

/*
 * This extension of Animation allows for animations that have a random
 * delay between frames, specified by minDuration and maxDuration, which
 * represent the min and max range for the duration, in milliseconds.
 */
public class AnimationRandom extends Animation {
	
	// timing
	private int duration;
	private int minDuration;
	private int maxDuration;
	
	public AnimationRandom() {
		minDuration = 500;
		maxDuration = 1000;
		nextDuration();
	}
	
	public AnimationRandom(int min, int max) {
		minDuration = min;
		maxDuration = max;
		nextDuration();
	}
	
	// getters
	public int getMin() { return minDuration; }
	public int getMax() { return maxDuration; }
	
	// setters
	public void setMin(int min) { minDuration = min; }
	public void setMax(int max) { maxDuration = max; }
	
	private void nextDuration() {
		duration = MathUtil.randInt(minDuration, maxDuration);
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > duration) {
			currentFrame++;
			startTime = System.nanoTime();
			nextDuration();
		}
		if (currentFrame == frames.length) {
			if (single) currentFrame = frames.length - 1;
			else currentFrame = 0;
			playedOnce = true;
		}
	}
	
}