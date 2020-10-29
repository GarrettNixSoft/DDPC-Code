package effects.entity;

import entity.util.GlitchAnimation;
import entity.util.GlitchController;

public class GlitchRandom extends EntityEffect {
	
	private GlitchController glitch;
	private ShakeEffect shake;
	
	public GlitchRandom(GlitchAnimation animation, ShakeEffect shake) {
		glitch = new GlitchController(animation);
		this.shake = shake;
		shake.synchronize(glitch);
	}
	
	public void update() {
		glitch.update();
		shake.update();
	}
	
	public int getOffset() {
		return shake.getOffset();
	}
	
}