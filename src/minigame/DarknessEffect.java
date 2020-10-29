package minigame;

import static main.Render.drawImageCSA;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import cutscene.Camera;
import effects.CenteredEffect;
import render.AlphaFadeInEffect;

public class DarknessEffect extends CenteredEffect {
	
	// scale and timer
	private boolean shrink;
	private long timer;
	private float scale;
	private float maxScale;
	private float minScale;
	
	// alpha
	private AlphaFadeInEffect alphaEffect;
	
	private float shrinkFactor = 0.004f;
	
	public DarknessEffect(Camera target) {
		super(new Texture[] {Textures.dark_vignette}, target);
		animation.setDelay(-1);
		minScale = 0.38f;
		alphaEffect = new AlphaFadeInEffect(0.75f);
		alphaEffect.max();
	}
	
	public void shrink() { 
		shrink = true;
		timer = System.nanoTime();
	}
	public void freeze() {
		shrink = false;
	}
	
	public void setMaxScale(float maxScale) {
		this.maxScale = maxScale;
	}
	
	public void reset() {
		scale = maxScale;
		timer = System.nanoTime();
	}
	
	public void flash() {
		alphaEffect.reset();
	}
	
	@Override
	public void update() {
		animation.update();
		alphaEffect.update();
		if (shrink && scale != minScale) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			scale = maxScale - (elapsed / 1000.0f * shrinkFactor);
			if (scale < minScale) scale = minScale;
		}
	}
	
	@Override
	public void render() {
		Texture tex = animation.getCurrentFrame();
		drawImageCSA(tex, target.getScreenX(), target.getScreenY(), scale, alphaEffect.getAlpha());
	}
	
}