package effects;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import cutscene.Camera;
import entity.util.Animation;

public class CenteredEffect extends VisualEffect {
	
	protected Animation animation;
	protected Camera target;
	
	public CenteredEffect(Texture[] tex, Camera target) {
		animation = new Animation();
		animation.setFrames(tex);
		this.target = target;
	}
	
	public void setDelay(int delay) {
		animation.setDelay(delay);
	}
	
	public void update() {
		animation.update();
	}
	
	public void render() {
		Texture tex = animation.getCurrentFrame();
		drawImage(tex, target.getScreenX() - tex.getImageWidth() / 2, target.getScreenY() - tex.getImageHeight() / 2);
	}
}