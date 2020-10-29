package entity.util;

import org.newdawn.slick.opengl.Texture;

public class Animation {
	
	protected Texture[] frames;
	protected int currentFrame;
	protected boolean playedOnce;
	protected long startTime;
	protected int delay;
	
	// single play
	protected boolean single;
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(Texture[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setSingle(boolean single) {
		this.single = single;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public boolean hasPlayedOnce() {
		return playedOnce;
	}
	
	public void update() {
		if (delay == -1) return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.length) {
			if (single) currentFrame = frames.length - 1;
			else currentFrame = 0;
			playedOnce = true;
		}
	}
	
	public Texture getCurrentFrame() {
		return frames[currentFrame];
	}
	
	public int getFrameIndex() { return currentFrame; }
	
	public void reset() {
		currentFrame = 0;
		playedOnce = false;
	}
}