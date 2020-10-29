package background;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import tile.DynamicTilemap;
import util.system.FileUtil;

public class AnimatedBackground {
	
	// base
	private AnimatedBase base;
	
	// elements
	private ArrayList<AnimatedElement> elements;
	
	public AnimatedBackground(String configFile, DynamicTilemap tm) {
		loadConfig(configFile, tm);
	}
	
	public AnimatedBackground(Texture bg) {
		// override animation, static background
		base = new AnimatedBase(new Texture[] {bg}, -1);
		elements = new ArrayList<AnimatedElement>();
	}
	
	// TODO: fix this, all of it; maybe rewrite it if necessary
	
	private void loadConfig(String configFile, DynamicTilemap tm) {
		try {
			// FETCH FILE
			System.out.println("[Animated Background] Loading BG config: " + "/backgrounds/" + configFile + ".cnfg");
			// get file data
			ArrayList<String> lines = FileUtil.getFileData("/backgrounds/" + configFile + ".cnfg");
			String baseLine = lines.get(0).trim();
			// PROCESS FILE DATA
			// get base
			String base = baseLine.substring(baseLine.indexOf(" ") + 1, baseLine.lastIndexOf(" "));
			int delay = Integer.parseInt(baseLine.substring(baseLine.lastIndexOf(" ") + 1));
			// loop through possible base names
			for (int i = 0; i < ElementIDs.BASE_ID.length; i++) {
				String str = ElementIDs.BASE_ID[i];
				// if it matches, use a switch with the index to assign the value
				if (str.equals(base)) {
					switch (i) {
					case 0: // s_default
						this.base = new AnimatedBase(new Texture[] {Textures.backgrounds[1]}, delay);
						break;
					case 1: // n_default
						this.base = new AnimatedBase(new Texture[] {Textures.backgrounds[2]}, delay);
						break;
					case 2: // y_default
						this.base = new AnimatedBase(new Texture[] {Textures.backgrounds[3]}, delay);
						break;
					case 3: // m_default
						this.base = new AnimatedBase(new Texture[] {Textures.backgrounds[4]}, delay);
						break;
					case 4: // noise
						this.base = new AnimatedBase(Textures.bg_noise, delay);
						break;
					}
					break;
				}
			}
			// get elements
			elements = new ArrayList<AnimatedElement>();
			for (int i = 1; i < lines.size(); i++) {
				// parse the line
				String[] elementTokens = lines.get(i).split("\\s+");
				String elementID = elementTokens[0];
				for (int j = 0; j < ElementIDs.ELEMENT_ID.length; j++) {
					String str = ElementIDs.ELEMENT_ID[j];
					if (str.equals(elementID)) {
						System.out.println("[AnimatedBackground] Element match found: " + str);
						switch(j) {
						case 0: // grid_fade
							
							break;
						case 1: // grid_glitch
							FlashElement gee = new FlashElement(tm, new Texture[] {Textures.glitch_grid}, 400, 300);
							gee.setGlitch(true);
							gee.setTime(5);
							gee.setMax(0.3f);
							elements.add(gee);
							break;
						case 2: // error
							ErrorElement e = new ErrorElement(tm, new Texture[] {Textures.error_message}, Textures.error_glitch, 400, 300);
							elements.add(e);
							break;
						case 3: // error_glitch
							ErrorElement ee = new ErrorElement(tm, new Texture[] {Textures.error_message}, Textures.error_glitch, 400, 300);
							ee.setGlitch(true);
							elements.add(ee);
							break;
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void update() {
		base.update();
		for (AnimatedElement e : elements) e.update();
	}
	
	public void render() {
		base.render();
		for (AnimatedElement e : elements) e.render();
	}
	
}