package effects;

import assets.Textures;
import entity.Player;
import main.Render;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import tile.TileMap;

public class JumpProgressMeter {

	private final TileMap map;
	private final Player player;

	private final Texture mFace;
	private float progress;

	private final float width;
	private final float height;
	private final float endWidth;
	private final float endHeight;

	public JumpProgressMeter(TileMap map, Player player) {
		// references
		this.map = map;
		this.player = player;
		// image
		mFace = Textures.dialogue_faces[3];
		// dimensions
		width = 8;
		height = 300;
		endWidth = 24;
		endHeight = 8;
	}

	public void update() {
		float height = map.getHeight();
		float y = player.getY();
		progress = y / height;
	}

	public void render() {
		// draw the bar
		Render.drawRect(Render.WIDTH - 50 - width / 2, Render.HEIGHT / 2f - height / 2, width, height, Color.white);
		Render.drawOutline(Render.WIDTH - 50 - width / 2, Render.HEIGHT / 2f - height / 2, width, height, Color.black);
		Render.drawRect(Render.WIDTH - 50 - endWidth / 2, Render.HEIGHT / 2f - height / 2 - endHeight / 2, endWidth, endHeight, Color.red);
		Render.drawOutline(Render.WIDTH - 50 - endWidth / 2, Render.HEIGHT / 2f - height / 2 - endHeight / 2, endWidth, endHeight, Color.black);
		Render.drawRect(Render.WIDTH - 50 - endWidth / 2, Render.HEIGHT / 2f + height / 2 - endHeight / 2, endWidth, endHeight, Color.green);
		Render.drawOutline(Render.WIDTH - 50 - endWidth / 2, Render.HEIGHT / 2f + height / 2 - endHeight / 2, endWidth, endHeight, Color.black);
		// draw the location
		float y = Render.HEIGHT / 2f - height / 2 + height * progress;
		Render.drawImageC(mFace, Render.WIDTH - 50, y, 30, 30);
	}

}
