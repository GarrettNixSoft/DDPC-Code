package tile;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import entity.util.Animation;
import util.data.Settings;

public class SwitchTile extends DynamicTile {
	
	// switch
	private int delay;
	private long timer;
	private Texture[] switch_tex;
	
	// animation
	private int animationSpeed;
	private int switchAnimationSpeed;
	
	// sound effect
	private Audio sfx;
	
	public SwitchTile(Texture[] tex, boolean blocked) {
		this.tex = new Texture[1];
		this.tex[0] = tex[0];
		this.switch_tex = new Texture[4];
		for (int i = 0; i < 4; i++) {
			this.switch_tex[i] = tex[i + 1];
		}
		this.blocked = blocked;
		this.delay = (int) (Math.random() * 4000) + 1000;
		this.animationSpeed = -1;
		this.switchAnimationSpeed = 100;
		timer = System.nanoTime();
		animation = new Animation();
		animation.setFrames(tex);
		animation.setDelay(animationSpeed);
		this.tileID = SWITCH;
		this.sfx = Sfx.misc[Sfx.STATIC_1];
	}
	
	public void update() {
		animation.update();
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > delay) {
			blocked = !blocked;
			timer = System.nanoTime();
			if (blocked) {
				animation.setFrames(tex);
				animation.setDelay(animationSpeed);
				delay = (int) (Math.random() * 5000) + 3000;
				sfx.stop();
			}
			else {
				animation.setFrames(switch_tex);
				animation.setDelay(switchAnimationSpeed);
				delay = (int) (Math.random() * 1700) + 300;
				sfx.playAsSoundEffect(1.0f, Settings.sfxVolume * 0.5f, false);
				//System.out.println("[SwitchTile] Play glitch sound effect");
			}
		}
	}
	
}