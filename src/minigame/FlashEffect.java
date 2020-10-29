package minigame;

import effects.VisualEffect;
import render.AlphaFadeEffect;

import static main.Render.drawImage;

import assets.Textures;

public class FlashEffect extends VisualEffect {
	
	private AlphaFadeEffect alphaEffect;
	
	private boolean playing;
	
	public FlashEffect() {
		alphaEffect = new AlphaFadeEffect();
	}
	
	public void reset() {
		alphaEffect.reset();
	}
	
	public void play() {
		playing = true;
		reset();
	}
	
	@Override
	public void update() {
		if (playing) {
			alphaEffect.update();
			if (alphaEffect.complete()) playing = false;
		}
	}
	@Override
	public void render() {
		if (playing) drawImage(Textures.flash, 0, 0, alphaEffect.getAlpha());
	}
}