package entity.util;

import org.newdawn.slick.opengl.Texture;

import util.MathUtil;

public class GlitchAnimation extends Animation {
	
	private Texture[] glitchFrames;
	private int currentGlitchFrame;
	private boolean glitchedOnce;
	private long glitchStartTime;
	private long glitchFrameTimer;
	private int glitchDelay;
	private int glitchDuration;
	private boolean glitching;
	private Texture glitchFrame;
	
	public GlitchAnimation() {
		super();
	}
	
	public void setGlitchFrames(Texture[] frames) {
		this.glitchFrames = frames;
		currentGlitchFrame = 0;
		glitchedOnce = false;
	}
	
	public void setGlitchDelay(int delay) {
		this.glitchDelay = delay;
	}
	
	public void glitch(int duration) {
		this.glitchDuration = duration;
		System.out.println("[GlitchAnimation] Glitching for " + duration + "ms");
		glitching = true;
		getGlitchFrame();
		glitchStartTime = System.nanoTime();
	}
	
	private void getGlitchFrame() {
		int index = MathUtil.randInt(glitchFrames.length);
		glitchFrame = glitchFrames[index];
	}
	
	public boolean hasGlitchedOnce() {
		return glitchedOnce;
	}
	
	@Override
	public void update() {
		if (glitchFrames == null) System.out.println("[GlitchAnimation] WARNING: THE GLITCH FRAMES ARE CURRENTLY NULL");
		if (glitching) {
			long elapsed = (System.nanoTime() - glitchStartTime) / 1000000;
			long frameElapsed = (System.nanoTime() - glitchFrameTimer) / 1000000;
			//System.out.println("[GlitchAnimation] Glitching, elapsed=" + elapsed + "/" + glitchDelay);
			if (frameElapsed > glitchDelay) {
				currentGlitchFrame++;
				glitchFrameTimer = System.nanoTime();
				getGlitchFrame();
				if (currentGlitchFrame >= glitchFrames.length) {
					glitchedOnce = true;
					currentGlitchFrame = 0;
				}
			}
			if (elapsed > glitchDuration) {
				glitching = false;
				startTime = System.nanoTime();
			}
		}
		else super.update();
	}
	
	@Override
	public Texture getCurrentFrame() {
		if (glitching) return glitchFrame;
		else return super.getCurrentFrame();
	}
	
}