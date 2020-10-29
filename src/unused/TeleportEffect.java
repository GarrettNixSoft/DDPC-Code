package unused;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import effects.DynamicEffect;
import entity.Player;
import entity.util.Animation;
import tile.TileMap;

public class TeleportEffect extends DynamicEffect {
	
	// assets
	private TileMap tilemap;
	private Player player;
	
	// location
	private int x, y;
	private float xmap, ymap;
	
	// animation
	private Animation animation;
	
	public TeleportEffect(TileMap tilemap, Player player, int x, int y) {
		this.tilemap = tilemap;
		this.player = player;
		this.x = x;
		this.y = y;
		animation = new Animation();
		//animation.setFrames(Textures.teleportEffect);
		animation.setDelay(15);
	}
	
	private void setMapPosition() {
		this.xmap = tilemap.getX();
		this.ymap = tilemap.getY();
	}
	
	public void update() {
		animation.update();
		x = (int) player.getX();
		y = (int) player.getY();
	}
	
	public void render() {
		setMapPosition();
		Texture tex = animation.getCurrentFrame();
		drawImage(tex, x + xmap - (tex.getImageWidth() / 2), y + ymap - (tex.getImageHeight() / 2));
	}
	
	public boolean remove() {
		return animation.hasPlayedOnce();
	}
}