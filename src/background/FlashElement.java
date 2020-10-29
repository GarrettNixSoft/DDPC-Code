package background;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import render.AlphaFadeEffect;
import tile.DynamicTilemap;
import util.MathUtil;

public class FlashElement extends AnimatedElement {
	
	// effect
	private AlphaFadeEffect alphaEffect;
	
	// timing
	private boolean doGlitch;
	private long timer;
	private int[] delays;
	private int delayIndex;
	private boolean glitch;
	private int wait;
	
	public FlashElement(DynamicTilemap tm, Texture[] frames, int x, int y) {
		super(tm, frames, x, y);
		alphaEffect = new AlphaFadeEffect();
		animation.setFrames(frames);
		animation.setDelay(-1);
		nextGlitch();
		timer = System.nanoTime();
	}
	
	public void setGlitch(boolean b) { doGlitch = b; }
	public void setTime(float time) { alphaEffect.setTime(time); }
	public void setMax(float max) { alphaEffect.setMax(max); }
	
	private void nextGlitch() {
		wait = MathUtil.randInt(250, 1600);
		delays = new int[MathUtil.randInt(1, 4)];
		System.out.println("[GridElement] next delays: " + delays.length + ", in " + wait + "ms");
		for (int i = 0; i < delays.length; i++) {
			delays[i] = MathUtil.randInt(200, 600);
		}
	}
	
	@Override
	public void update() {
		// update
		animation.update();
		if (!doGlitch) return;
		// handle fade
		alphaEffect.update();
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (glitch) {
			if (elapsed > delays[delayIndex]) {
				timer = System.nanoTime();
				delayIndex++;
				//System.out.println("[GridElement] delayIndex=" + delayIndex);
				if (delayIndex >= delays.length) {
					nextGlitch();
					glitch = false;
					delayIndex = 0;
					//System.out.println("[GridElement] reset");
				}
				alphaEffect.reset();
			}
		}
		else {
			if (elapsed > wait) {
				timer = System.nanoTime();
				glitch = true;
			}
		}
	}
	
	@Override
	public void render() {
		drawImage(animation.getCurrentFrame(), x - width / 2, y - height / 2, width, height, alphaEffect.getAlpha());
	}
}