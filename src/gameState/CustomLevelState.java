package gameState;

import static main.Render.drawImage;
import static main.Render.drawStringShadowed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import assets.Fonts;
import assets.Sfx;
import assets.Textures;
import effects.MenuSnowEffect;
import gui.Button;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.input.MouseInput;

public class CustomLevelState extends GameState {
	
	// background
	private Texture bg;
	private float bgX;
	private float bgY;
	
	// layout
	private Texture title;
	private Texture previewLabel;
	private Texture preview;
	private int prevX, prevY, scale;
	private String[] details;
	
	// buttons
	private Button loadLevel;
	private Button play;
	private Button arrow;
	
	// colors
	private Color[][] s_colors;
	private Color[][] n_colors;
	private Color[][] y_colors;
	private Color[] bgColors;
	
	// level
	private String path;
	private boolean loaded;
	
	// winter theme
	private MenuSnowEffect snowEffect;
	
	public CustomLevelState(GameStateManager gsm) {
		this.gsm = gsm;
		// background
		bg = Textures.backgrounds[Textures.MENU];
		bgX = DataCache.bgX;
		bgY = DataCache.bgY;
		// layout
		title = Textures.menu[Textures.CUSTOM_LEVEL];
		previewLabel = Textures.preview;
		prevX = 621;
		prevY = 285;
		scale = 4;
		details = new String[] {
				"Level Name: ...",
				"Tiles Across: ...",
				"Tiles Down: ...",
				"Enemies: ..."
		};
		// buttons
		loadLevel = new Button(Textures.menubuttons[10][0], Textures.menubuttons[10][1], 108, 120);
		play = new Button(Textures.menubuttons[11][0], Textures.menubuttons[11][1], 344, 450);
		arrow = new Button(Textures.menubuttons[9][0], Textures.menubuttons[9][1], 60, 545);
		// colors
		s_colors = new Color[2][10];
		s_colors[0][0] = s_colors[0][1] = s_colors[0][2] = s_colors[0][3] = s_colors[0][4] = s_colors[0][5] = s_colors[0][6] = s_colors[0][7] = s_colors[0][8] = new Color(0, 0, 0, 0);
		s_colors[0][9] = s_colors[1][1] = s_colors[1][2] = s_colors[1][3] = s_colors[1][7] = s_colors[1][8] = s_colors[1][9] = new Color(175, 67, 0, 255);
		s_colors[1][0] = s_colors[1][4] = s_colors[1][5] = s_colors[1][6] = new Color(145, 255, 0, 255);
		n_colors = new Color[2][10];
		n_colors[0][0] = n_colors[0][1] = n_colors[0][2] = n_colors[0][3] = n_colors[0][4] = n_colors[0][5] = n_colors[0][9] = new Color(0, 0, 0, 0);
		n_colors[0][6] = n_colors[0][7] = n_colors[0][8] = new Color(129, 13, 0, 255);
		n_colors[1][0] = n_colors[1][4] = n_colors[1][5] = n_colors[1][6] = new Color(255, 173, 208, 255);
		n_colors[1][1] = n_colors[1][2] = n_colors[1][3] = n_colors[1][7] = n_colors[1][8] = n_colors[1][9] = new Color(229, 113, 64, 255);
		y_colors = new Color[2][10];
		y_colors[0][0] = y_colors[0][1] = y_colors[0][2] = y_colors[0][3] = y_colors[0][7] = y_colors[0][8] = y_colors[0][9] = new Color(0, 0, 0, 0);
		y_colors[0][4] = y_colors[0][5] = y_colors[0][6] = new Color(64, 64 ,64, 255);
		y_colors[1][0] = y_colors[1][4] = y_colors[1][5] = y_colors[1][6] = new Color(0, 127, 14, 255);
		y_colors[1][1] = y_colors[1][2] = y_colors[1][3] = y_colors[1][7] = y_colors[1][8] = y_colors[1][9] = new Color(76, 30, 0, 255);
		bgColors = new Color[7];
		bgColors[0] = new Color(77, 234, 255);
		bgColors[1] = new Color(255, 196, 211);
		bgColors[2] = new Color(26, 36, 36);
		bgColors[3] = new Color(3, 3, 26);
		loaded = false;
		// winter theme
		snowEffect = DataCache.snowEffect;
	}
	
	private void generatePreview(String path) {
		loaded = true;
		this.path = path;
		// load map
		TileMap tm = new TileMap(path, false);
		int[][] map = tm.getMap();
		int bgVal = tm.getBgValue();
		// calculate dimensions to use
		int width, height;
		int maxWidth = 80;
		int maxHeight = 60;
		if (map[0].length > 125) width = maxWidth;
		else width = map[0].length;
		if (map.length > maxHeight) height = maxHeight;
		else height = map.length;
		String levelName = path.substring(path.lastIndexOf("\\") + 1, path.length() - 4);
		details[0] = "Level Name: " + levelName;
		details[1] = "Tiles Across: " + map[0].length;
		details[2] = "Tiles Down: " + map.length;
		details[3] = "Enemies: " + tm.getEnemySpawns().size();
		// set up BufferedImage
		BufferedImage image = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		// get colors
		Color[][] colors = null;
		Color bgColor = null;
		switch (bgVal) {
			case 0:
				colors = s_colors;
				bgColor = bgColors[0];
				break;
			case 1:
				colors = n_colors;
				bgColor = bgColors[1];
				break;
			case 2:
				colors = y_colors;
				bgColor = bgColors[2];
				break;
			case 3:
				colors = s_colors;
				bgColor = bgColors[0];
				break;
			case 4:
				colors = s_colors;
				bgColor = bgColors[1];
				break;
			case 5:
				colors = n_colors;
				bgColor = bgColors[2];
				break;
			case 6:
				colors = y_colors;
				bgColor = bgColors[2];
				break;
			case 7:
				colors = s_colors;
				bgColor = bgColors[3];
				break;
		}
		g.setColor(bgColor);
		g.fillRect(0, 0, width * scale, height * scale);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int colorVal = map[row][col];
				if (colorVal > 19) g.setColor(colors[0][0]);
				else {
					int r = colorVal / 10;
					int c = colorVal % 10;
					g.setColor(colors[r][c]);
				}
				g.fillRect(col * scale, row * scale, scale, scale);
			}
		}
		// convert to Texture
		try {
			preview = BufferedImageUtil.getTexture("", image);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error generating level preview.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void update() {
		// move background
		bgX -= 1f;
		bgY -= 1f;
		// reset once moved all the way
		if (bgX <= -800) bgX = 0;
		if (bgY <= -600) bgY = 0;
		// update buttons
		loadLevel.update();
		play.update();
		arrow.update();
		// winter theme
		if (snowEffect != null) snowEffect.update();
	}
	
	public void render() {
		// draw background
		drawImage(bg, bgX, bgY);
		// draw layout
		drawImage(title, 190, -7);
		drawImage(previewLabel, 550, 120);
		for (int i = 0; i < details.length; i++) {
			drawStringShadowed(details[i], Fonts.MENU_MED, org.newdawn.slick.Color.white, 106, 180 + i * 45, 2, 2);
		}
		if (preview != null) drawImage(preview, prevX - preview.getImageWidth() / 2, prevY - preview.getImageHeight() / 2);
		// draw buttons
		loadLevel.render();
		play.render();
		arrow.render();
		//drawRect(prevX - 2, prevY - 2, 4, 4, org.newdawn.slick.Color.black);
		// winter theme
		if (snowEffect != null) snowEffect.render();
	}
	
	protected void handleInput() {
		/*
		if (KeyInput.isPressed(KeyInput.ESC)) {
			DataCache.bgX = bgX;
			DataCache.bgY = bgY;
			gsm.setState(GameStateManager.LEVEL_SELECT_STATE);
		}
		*/
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (loadLevel.isHover()) { // load a level
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				chooser.setDialogTitle("Load level");
				chooser.setFileFilter(new FileNameExtensionFilter("map file","map"));
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					// check if it's a map file
					File file = chooser.getSelectedFile();
					if (!file.getName().endsWith(".map")) JOptionPane.showMessageDialog(null, "The file selected is not\na map file.", "File Type Error", JOptionPane.ERROR_MESSAGE);
					// use file data
					else generatePreview(file.getPath());
				}
			}
			if (play.isHover() && loaded) { // play level
				DataCache.levelSelected = path;
				DataCache.snowEffect = snowEffect;
				FadeManager.fadeOut(60, GameStateManager.PLAY_STATE, true);
				DataCache.custom = true;
			}
			if (arrow.isHover()) { // back to level select
				DataCache.bgX = bgX;
				DataCache.bgY = bgY;
				DataCache.snowEffect = snowEffect;
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				Sfx.mute(5);
				gsm.setState(GameStateManager.LEVEL_SELECT_STATE);
			}
		}
	}
}