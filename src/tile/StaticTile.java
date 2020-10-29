package tile;

import org.newdawn.slick.opengl.Texture;

import entity.util.Animation;

public class StaticTile extends DynamicTile {

	public StaticTile(Texture[] tex, boolean blocked) {
		this.tex = tex;
		this.blocked = blocked;
		this.tileID = STATIC;
		animation = new Animation();
		animation.setFrames(tex);
		animation.setDelay(-1);
	}
	
	public void update() {
		animation.update();
	}
	
}