package entity.enemy;

import static main.Render.drawImage;

import assets.Textures;
import entity.util.AnimationRandom;
import tile.TileMap;

public class Glitch extends Enemy {
	
	private AnimationRandom animation;
	
	public Glitch(TileMap tm, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		width = height = 60;
		cwidth = cheight = 50;
		health = Integer.MAX_VALUE;
		animation = new AnimationRandom();
		animation.setFrames(Textures.glitch);
		animation.setMin(250);
		animation.setMax(350);
	}
	
	@Override
	public void update() {
		animation.update();
	}
	
	@Override
	public void render() {
		setMapPosition();
		drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height);
		//Render.drawOutline(getScreenRectangle(), Color.red);
	}
	
	public void damage(int damage) {
		// do nothing, these things are INVINCIBLE
	}
	
	public boolean remove() {
		return false;
	}
}