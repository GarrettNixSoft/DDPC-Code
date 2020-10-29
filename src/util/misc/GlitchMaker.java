package util.misc;

import static util.data.ImageUtil.save;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import util.MathUtil;
import util.data.ImageUtil;

public class GlitchMaker {
	
	public static BufferedImage[] glitch(String texture, int count, int scale) {
		BufferedImage tex = ImageUtil.getBufferedImage(texture);
		BufferedImage[] glitch = new BufferedImage[count];
		for (int i = 0; i < count; i++) {
			// shred up image
			BufferedImage[] shreds = new BufferedImage[tex.getHeight() / scale];
			for (int row = 0; row < shreds.length; row++) {
				int pixels = tex.getWidth() / scale;
				int shredSize = (int) (Math.random() * (pixels - 1)) + 1;
				int buffer = pixels - shredSize;
				int shredStart = (int) (Math.random() * buffer / scale);
				System.out.println("[GlitchMaker] shred #" + row + ", size=" + shredSize + ", start=" + shredStart);
				shreds[row] = tex.getSubimage(shredStart * scale, row * scale, shredSize * scale, scale);
			}
			// generate new image
			glitch[i] = new BufferedImage((int) (tex.getWidth() * 1.2), (int) (tex.getHeight()), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) glitch[i].getGraphics();
			for (int row = 0; row < shreds.length; row++) {
				int shredStart = (int) (Math.random() * ((tex.getWidth() - (shreds[row].getWidth()) / scale)));
				g.drawImage(shreds[row], shredStart, row * scale, null);
			}
		}
		save(glitch, texture.substring(texture.lastIndexOf("/") + 1));
		return glitch;
	}
	
	public static BufferedImage[] glitchRect(String texture, int count, int scale) {
		BufferedImage tex = ImageUtil.getBufferedImage(texture);
		BufferedImage[] glitch = new BufferedImage[count];
		for (int i = 0; i < count; i++) {
			// get parts
			int layerCount = MathUtil.randInt(1, 7);
			BufferedImage[] layers = new BufferedImage[layerCount];
			int imgWidth = tex.getWidth();
			int imgHeight = tex.getHeight();
			// start canvas
			int canvasWidth = (int) (tex.getWidth() * 1.4);
			int canvasHeight = (int) (tex.getHeight() * 1.2);
			//int canvasXBuffer = (int) (tex.getWidth() * 0.2);
			//int canvasYBuffer = (int) (tex.getHeight() * 0.1);
			BufferedImage canvas = new BufferedImage(canvasWidth, canvasHeight, tex.getType());
			// determine what part of the base to keep
			int corner = MathUtil.randInt(4);
			int cutStartX, cutStartY;
			switch (corner) {
			case 0:
				cutStartX = 0;
				cutStartY = 0;
				break;
			case 1:
				cutStartX = imgWidth;
				cutStartY = 0;
				break;
			case 2:
				cutStartX = 0;
				cutStartY = imgHeight;
				break;
			case 3:
				cutStartX = imgWidth;
				cutStartY = imgHeight;
				break;
			default:
				cutStartX = cutStartY = 0;
			}
			int width = MathUtil.randInt(imgWidth);
			int height = MathUtil.randInt(imgHeight);
			// keep that part of the base
			//int[] xpoints = {cutStartX, cutStartX + (width * dirX), cutStartX + (width * dirX), cutStartX};
			//int[] ypoints = {cutStartY, cutStartY + (height * dirY), cutStartY + (height * dirY), cutStartY};
			//Polygon region = new Polygon(xpoints, ypoints, 4);
			int xPos, yPos;
			if (cutStartX > 0) xPos = imgWidth - width;
			else xPos = cutStartX;
			if (cutStartY > 0) yPos = imgHeight - height;
			else yPos = cutStartY;
			BufferedImage baseSect = tex.getSubimage(xPos, yPos, width, height); // TODO get this all refined
			// determine and set pieces
			for (int j = 0; j < layers.length; j++) {
				// get the pieces
				int pieceCount = MathUtil.randInt(1, 4);
				BufferedImage[] pieces = new BufferedImage[pieceCount];
				for (int c = 0; c < pieceCount; c++) {
					int startX = MathUtil.randInt(imgWidth / 2);
					int startY = MathUtil.randInt(imgHeight / 2);
					int w = MathUtil.randInt(imgWidth / 2) + 1;
					int h = MathUtil.randInt(imgHeight / 2) + 1;
					pieces[c] = tex.getSubimage(startX, startY, w, h);
				}
				// put them on the image
				layers[j] = new BufferedImage(canvasWidth, canvasHeight, tex.getType());
				Graphics2D g1 = layers[j].createGraphics();
				for (int c = 0; c < pieces.length; c++) {
					int startX = MathUtil.randInt(canvasWidth - pieces[c].getWidth());
					int startY = MathUtil.randInt(canvasHeight - pieces[c].getHeight());
					g1.drawImage(pieces[c], startX, startY, null);
				}
			}
			// render it together
			if (i == 0) save(baseSect, "base");
			Graphics2D g = canvas.createGraphics();
			g.drawImage(baseSect, cutStartX, cutStartY, null);
			for (int j = 0; j < layers.length; j++) {
				g.drawImage(layers[j], 0, 0, null);
			}
			glitch[i] = canvas;
		}
		save(glitch, texture.substring(texture.lastIndexOf("/") + 1));
		return glitch;
	}
	
	public static BufferedImage[] glitch2(String texture, int count, int intensity) {
		BufferedImage tex = ImageUtil.getBufferedImage(texture);
		BufferedImage[] result = new BufferedImage[count];
		for (int i = 0; i < count; i++) {
			// create a canvas to work with
			BufferedImage canvas = new BufferedImage((int) (tex.getWidth() * 1.4), (int) (tex.getHeight() * 1.4), tex.getType());
			BufferedImage sample = ImageUtil.deepCopy(tex);
			int x0 = (int) (tex.getWidth() * 0.2);
			int y0 = (int) (tex.getHeight() * 0.2);
			Graphics2D g = canvas.createGraphics();
			// determine number of pieces to take out
			int pieces = MathUtil.randInt(1, intensity);
			for (int p = 0; p < pieces; p++) {
				int height = MathUtil.randInt(3, 6);
				int startY = MathUtil.randInt(0, sample.getHeight() - height);
				int width = MathUtil.randInt(15, (int) (sample.getWidth() * 0.75));
				int startX = MathUtil.randInt(0, sample.getWidth() - width);
				BufferedImage slice = sample.getSubimage(startX, startY, width, height);
				int[] xpoints = {startX, startX + width, startX + width, startX};
				int[] ypoints = {startY, startY, startY + height, startY + height};
				Polygon region = new Polygon(xpoints, ypoints, 4);
				ImageUtil.removeRegion(sample, region);
				g.drawImage(sample, x0, y0, null);
				startY = MathUtil.randInt(0, canvas.getHeight() - height);
				startX = MathUtil.randInt(0, canvas.getWidth() - width);
				g.drawImage(slice, startX, startY, null);
			}
			result[i] = canvas;
		}
		save(result, "ghost_glitch");
		return result;
	}
	
}