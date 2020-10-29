package background;

import static main.Render.drawImage;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import tile.TileMap;
import util.data.Settings;

public class DynamicBackground {
	
	private TileMap tilemap;
	private Texture background;
	private ArrayList<BackgroundElement> elements;
	private float bgX;
	private float bgY;
	
	public DynamicBackground(TileMap tm) {
		// init background based on character
		this.tilemap = tm;
		elements = new ArrayList<BackgroundElement>();
		System.out.println("[DynamicBckground] bgValue: " + tilemap.getBgValue());
		switch (tm.getBgValue()) {
			case 0: // sayori standard
				if (Settings.winterTheme) background = Textures.s_snow_bg;
				else background = Textures.backgrounds[Textures.S_LEVEL];
				elements.add(new BackgroundElement(Textures.cloud_large, 400, 200, 404, 210, 0.18f, 0.05f));
				elements.add(new BackgroundElement(Textures.cloud_medium[1], 100, 200, 140, 56, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[0], 580, 80, 136, 48, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[2], 480, 380, 136, 58, 0.3f, 0.08f));
				break;
			case 1: // natsuki standard
				if (Settings.winterTheme) background = Textures.n_snow_bg;
				else background = Textures.backgrounds[Textures.N_LEVEL];
				elements.add(new BackgroundElement(Textures.cloud_large, 400, 200, 404, 210, 0.18f, 0.05f));
				elements.add(new BackgroundElement(Textures.cloud_medium[1], 100, 200, 140, 56, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[0], 580, 80, 136, 48, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[2], 480, 380, 136, 58, 0.3f, 0.08f));
				break;
			case 2: // yuri standard
				background = Textures.backgrounds[Textures.Y_LEVEL];
				break;
			case 3: // sayori rain
				background = Textures.backgrounds[Textures.S_LEVEL_RAIN];
				elements.add(new BackgroundElement(Textures.sky_clouds, 0, 35, 800, 75, 0.08f, 0f));
				elements.add(new BackgroundElement(Textures.cloud_large, 400, 200, 404, 210, 0.18f, 0.05f));
				elements.add(new BackgroundElement(Textures.cloud_medium[1], 100, 200, 140, 56, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[0], 580, 80, 136, 48, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[2], 480, 380, 136, 58, 0.3f, 0.08f));
				break;
			case 4: // sayori glitch
				background = Textures.backgrounds[Textures.M_LEVEL_1];
				elements.add(new BackgroundElement(Textures.cloud_large, 400, 200, 404, 210, 0.18f, 0.05f));
				elements.add(new BackgroundElement(Textures.cloud_medium[1], 100, 200, 140, 56, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[0], 580, 80, 136, 48, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[2], 480, 380, 136, 58, 0.3f, 0.08f));
				break;
			case 5: // natsuki glitch
				background = Textures.backgrounds[Textures.M_LEVEL_2];
				elements.add(new BackgroundElement(Textures.cloud_large, 400, 200, 404, 210, 0.18f, 0.05f));
				elements.add(new BackgroundElement(Textures.cloud_medium[1], 100, 200, 140, 56, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[0], 580, 80, 136, 48, 0.3f, 0.08f));
				elements.add(new BackgroundElement(Textures.cloud_medium[2], 480, 380, 136, 58, 0.3f, 0.08f));
				break;
			case 6: // yuri glitch
				background = Textures.backgrounds[Textures.M_LEVEL_3];
				break;
			case 7: // monika end level (jump)
				background = Textures.backgrounds[Textures.M_END];
				break;
			case 8: // monika glitch level
				background = Textures.backgrounds[Textures.M_FINAL];
				break;
		}
	}
	
	public void update() {
		bgX = tilemap.getX() / 10;
		//bgY = tilemap.getY() / 10;
		if (bgX < -background.getImageWidth()) bgX = 0;
		else if (bgX > 0) bgX = -background.getImageWidth();
		for (BackgroundElement be : elements) be.update(tilemap.getX(), tilemap.getY());
	}
	
	public void render() {
		// render background
		drawImage(background, bgX, bgY);
		drawImage(background, bgX + background.getImageWidth(), bgY);
		// render elements
		for (BackgroundElement be : elements) be.render();
	}
}