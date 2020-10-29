package util.misc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PixelRandomizer {
	
	public static void randomize() {
		try {
			BufferedImage image = ImageIO.read(PixelRandomizer.class.getResourceAsStream("/glitch_tiles/y_level.png"));
			ArrayList<Color[][]> pixels = new ArrayList<Color[][]>();
			System.out.println("image loaded with width " + image.getWidth() + "px and height " + image.getHeight() + "px");
			for (int i = 0; i < image.getHeight() / 15; i++) {
				for (int j = 0; j < image.getWidth() / 15; j++) {
					Color[][] pix = new Color[15][15];
					for (int r = 0; r < 15; r++) {
						for (int c = 0; c < 15; c++) {
							pix[r][c] = new Color(image.getRGB(j * 15 + c, i * 15 + r));
						}
					}
					pixels.add(pix);
				}
			}
			System.out.println("pixels populated with " + pixels.size() + " blocks");
			ArrayList<Color[][]> destination = new ArrayList<Color[][]>();
			for (int i = 0; i < pixels.size(); i++) {
				destination.add(new Color[15][15]);
			}
			// randomize blocks
			for (int i = 0; i < pixels.size(); i++) {
				Color[][] block = pixels.get(i);
				ArrayList<Color> pix = new ArrayList<Color>();
				for (Color[] list : block) {
					for (Color c : list) {
						pix.add(c);
					}
				}
				Collections.shuffle(pix);
				Color[][] end = destination.get(i);
				for (int r = 0; r < 15; r++) {
					for (int c = 0; c < 15; c++) {
						int index = (int) (Math.random() * pix.size());
						end[r][c] = pix.get(index);
						pix.remove(index);
					}
				}
			}
			// render pixels to new image
			BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < destination.size(); i++) {
				Color[][] block = destination.get(i);
				int blockX = i % (image.getWidth() / 15);
				int blockY = i / (image.getWidth() / 15);
				for (int r = 0; r < block.length; r++) {
					for (int c = 0; c < block[r].length; c++) {
						int pixX = blockX * 15 + c;
						int pixY = blockY * 15 + r;
						result.setRGB(pixX, pixY, block[r][c].getRGB());
					}
				}
			}
			// save file
			String name = JOptionPane.showInputDialog(null, "File name:", "Choose name");
			File file = new File("C:/Users/Garrett/workspace - games/DDLC Platformer/src/res/textures/tilesets/glitch_tiles/" + name + ".png");
			boolean res = ImageIO.write(result, "png", file);
			if (res) System.out.println("file should be saved");
			else System.out.println("oof");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}