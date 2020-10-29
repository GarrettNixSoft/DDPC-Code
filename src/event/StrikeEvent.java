package event;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import cutscene.Camera;
import entity.util.Animation;
import tile.DynamicTilemap;
import util.FadeManager;
import util.data.LevelCache;

public class StrikeEvent extends GameEvent {
	
	// animation
	private Animation animation;
	private Texture[] frames;
	
	// map
	private DynamicTilemap tilemap;
	
	// map damage config
	private int[][] damageConfig = {
			{0, 0, 1, 0, 0, 0, 0},
			{0, 0, 0, 1, 0, 0, 0},
			{1, 0, 1, 0, 0, 0, 0},
			{0, 0, 2, 0, 0, 0, 1},
			{1, 0, 1, 2, 0, 1, 0},
			{0, 1, 0, 0, 2, 0, 0},
			{0, 0, 1, 0, 0, 1, 0}
	};
	
	// location
	private int x;
	private int y;
	
	// tile destroy
	private boolean tile;
	
	public StrikeEvent(EventTrigger trigger, LevelCache level, int x, int y) {
		super(trigger, level);
		this.tilemap = level.tilemap;
		frames = Textures.lightning;
		animation = new Animation();
		animation.setFrames(this.frames);
		animation.setDelay(120);
		this.x = x;
		this.y = y;
		tile = true;
	}
	
	public void setTile(boolean tile) { this.tile = tile; }
	
	public void activate() {
		if (activated) return;
		System.out.println("[StrikeEvent] Strike occurring at " + x + "," + y);
		animation.reset();
		if (tile) level.tilemap.destroy(damageConfig, x, y);
		Sfx.playSound(Sfx.TYPE_MISC, Sfx.THUNDER);
		FadeManager.fadeIn(0.5f, true);
		level.camera.shake(Camera.SMOOTH, 10, 0.75f);
		active = activated = true;
	}
	
	public void update() {
		if (!active) return;
		animation.update();
		if (animation.hasPlayedOnce()) {
			active = false;
		}
	}
	
	public void render() {
		if (!active) return;
		Texture tex = animation.getCurrentFrame();
		drawImage(tex, x + tilemap.getX() - tex.getImageWidth() / 2, tilemap.getY() + y - tex.getImageHeight());
	}
	
}