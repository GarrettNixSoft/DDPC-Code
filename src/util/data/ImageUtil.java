package util.data;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class ImageUtil {
	
	// unused until I figure out how Texture data is stored
	public static BufferedImage textureToBufferedImage(Texture texture) {
		int width = texture.getImageWidth();
		int height = texture.getImageHeight();
		byte[] data = texture.getTextureData();
		System.out.println("Texture contains " + data.length + " bytes");
		System.out.println("Texture is " + width + "x" + height + "px");
		for (int i = 0; i < data.length / width; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print((int) data[i * width + j] + " ");
			}
			System.out.println();
		}
		return null;
	}
	
	// get a texture from a bufferedimage
	public static Texture bufferedImageToTexture(BufferedImage image) {
		try {
			return BufferedImageUtil.getTexture("", image);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// load the bufferedimage from a path to a texture
	public static BufferedImage getBufferedImage(String path) {
		try {
			System.out.println("[ImageUtil] Loading image file: /res/textures/" + path + ".png");
			return ImageIO.read(ImageUtil.class.getResourceAsStream("/res/textures/" + path + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// copy a bufferedimage (thanks StackOverflow)
	public static BufferedImage deepCopy(BufferedImage bi) {
	    ColorModel cm = bi.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	// remove a section from a bufferedimage (also thanks StackOverflow)
	public static BufferedImage removeRegion(BufferedImage image, Polygon region) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics2D g = result.createGraphics();
		Rectangle wholeImage = new Rectangle(image.getWidth(), image.getHeight());
		Area clip = new Area(wholeImage);
		clip.subtract(new Area(region));
		g.clip(clip);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return result;
	}
	
	// cut out a region from a bufferedimage (ty SO)
	public static BufferedImage cutOutRegion(BufferedImage image, Polygon region) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics2D g = result.createGraphics();
		g.clip(region);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return result;
	}
	
	// save an array of images
	public static void save(BufferedImage[] images, String name) {
		try {
			System.out.println("[GlitchMaker] Saving " + images.length + " files");
			File dir = new File(System.getProperty("user.home") + "\\glitch_tex");
			if (!dir.exists()) dir.mkdirs();
			for (int i = 0; i < images.length; i++) {
				String fileName = name + "_" + i + ".png";
				ImageIO.write(images[i], "png", new File(dir.getPath() + "\\" + fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// save one image
	public static void save(BufferedImage image, String name) {
		try {
			File dir = new File(System.getProperty("user.home") + "\\glitch_tex");
			if (!dir.exists()) dir.mkdirs();
			String fileName = name + ".png";
			ImageIO.write(image, "png", new File(dir.getPath() + "\\" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}