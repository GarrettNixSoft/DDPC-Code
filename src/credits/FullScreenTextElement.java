package credits;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.newdawn.slick.Color;

import assets.Fonts;

import static main.Render.drawString;

public class FullScreenTextElement extends StaticTextElement {
	
	// location
	private int[] x;
	private int[] y;
	
	public FullScreenTextElement(String[] text, Color color) {
		super(text, Fonts.FULLSCREEN, color);
		init();
	}
	
	public FullScreenTextElement(String[] text, Color color, int duration, int fadeDuration) {
		super(text, Fonts.FULLSCREEN, color, duration, fadeDuration);
		init();
	}
	
	private void init() {
		// init coord arrays
		x = new int[text.length];
		y = new int[text.length];
		// calculate string sizes and locations
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setFont(Fonts.AWT_FULLSCREEN);
		// calculate total height, and set x coordinates
		int height = 0;
		int indivHeight = 0;
		for (int i = 0; i < text.length; i++) {
			Rectangle2D r = Fonts.AWT_FULLSCREEN.getStringBounds(text[i], ((Graphics2D) g).getFontRenderContext());
			int len = (int) r.getWidth();
			int ht = (int) r.getHeight();
			ht -= 20;
			indivHeight = ht;
			height += ht;
			x[i] = 400 - (len / 2);
		}
		System.out.println("LINE HEIGHT: " + indivHeight);
		// calculate y coordinates
		int yStart = 300 - (height / 2);
		for (int i = 0; i < text.length; i++) {
			y[i] = yStart + (i * indivHeight);
		}
	}
	
	public void render() {
		long elapsed = (System.nanoTime() - start) / 1000000;
		float percent = 0;
		Color c;
		switch (stage) {
			case 0:
				percent = elapsed / (float) fadeDuration;
				c = new Color(color.r, color.g, color.b, percent);
				for (int i = 0; i < text.length; i++) {
					drawString(text[i], font, c, x[i], y[i]);
				}
				break;
			case 1:
				percent = 1;
				for (int i = 0; i < text.length; i++) {
					drawString(text[i], font, color, x[i], y[i]);
				}
				break;
			case 2:
				elapsed -= (duration + fadeDuration);
				percent = (fadeDuration - elapsed) / (float) fadeDuration;
				c = new Color(color.r, color.g, color.b, percent);
				for (int i = 0; i < text.length; i++) {
					drawString(text[i], font, c, x[i], y[i]);
				}
				break;
		}
	}
}