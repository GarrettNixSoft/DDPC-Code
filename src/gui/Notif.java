package gui;

import assets.Textures;
import entity.util.Animation;

import static main.Render.drawImageC;

public class Notif {
	
	private Animation animation;
	private int x;
	private int y;
	
	private boolean visible;
	
	public Notif(int x, int y) {
		animation = new Animation();
		animation.setFrames(Textures.notif);
		animation.setDelay(750);
		this.x = x;
		this.y = y;
	}
	
	public void setVisible(boolean b) {
		System.out.println("[Notif] visible set to " + b);
		visible = b;
	}
	
	public boolean visible() {
		return visible;
	}
	
	public void update() {
		animation.update();
	}
	
	public void render() {
		if (visible) drawImageC(animation.getCurrentFrame(), x, y);
	}
	
}