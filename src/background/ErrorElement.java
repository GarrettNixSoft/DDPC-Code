package background;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import render.AlphaEffect;
import render.AlphaPulseEffect;
import tile.DynamicTilemap;
import util.MathUtil;
import util.data.Settings;

public class ErrorElement extends AnimatedElement {
	
	// effect
	private AlphaEffect alphaEffect;
	private Texture[] frames;
	private Texture[] glitchFrames;
	
	// timing
	private boolean doGlitch;
	private long timer;
	private int wait;
	private boolean glitch;
	private int duration;
	
	public ErrorElement(DynamicTilemap tm, Texture[] frames, Texture[] glitchFrames, int x, int y) {
		super(tm, frames, x, y);
		this.frames = frames;
		this.glitchFrames = glitchFrames;
		alphaEffect = new AlphaPulseEffect();
		animation.setFrames(frames);
		animation.setDelay(-1);
		nextGlitch();
		timer = System.nanoTime();
	}
	
	public void setGlitch(boolean b) { doGlitch = b; }
	
	private void nextGlitch() {
		wait = MathUtil.randInt(2000, 6000);
		duration = MathUtil.randInt(300, 1000);
	}
	
	@Override
	public void update() {
		// update
		animation.update();
		alphaEffect.update();
		if (!doGlitch) return;
		// check timer
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (glitch) {
			if (elapsed > duration) {
				glitch = false;
				animation.setFrames(frames);
				animation.setDelay(-1);
				nextGlitch();
				timer = System.nanoTime();
				Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_2);
			}
		}
		else {
			if (elapsed > wait) {
				glitch = true;
				animation.setFrames(glitchFrames);
				animation.setDelay(35);
				timer = System.nanoTime();
				Sfx.playSound(Sfx.TYPE_MISC, Sfx.STATIC_2, 0.3f * Settings.sfxVolume);
			}
		}
	}
	
	@Override
	public void render() {
		drawImage(animation.getCurrentFrame(), x - width / 2, y - height / 2, width, height, alphaEffect.getAlpha());
	}
	
}