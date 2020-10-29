package credits;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.newdawn.slick.Color;

import assets.Fonts;
import main.Render;

import static main.Render.drawString;

public class ScrollingParagraphElement extends ScrollingElement {
	
	// content and location
	private String[] text;
	private float[] x;
	private float[] y;
	
	public ScrollingParagraphElement(String[] text) {
		this.text = text;
		yPos = Render.HEIGHT;
		init();
	}
	
	private void init() {
		// init coord arrays
		x = new float[text.length];
		y = new float[text.length];
		// calculate string sizes and locations
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		// calculate total height, and set x coordinates
		height = 0;
		for (int i = 0; i < text.length; i++) {
			Rectangle2D r = Fonts.AWT_CENTERED_MED.getStringBounds(text[i], ((Graphics2D) g).getFontRenderContext());
			int len = (int) r.getWidth();
			int ht = (int) r.getHeight() - 8;
			height += ht;
			x[i] = 400 - (len / 2);
			y[i] = ht * i;
		}
		scrollSpeed = 0.5f;
	}
	
	public void update() {
		yPos -= scrollSpeed;
	}
	
	public void render() {
		for (int i = 0; i < text.length; i++) {
			drawString(text[i], Fonts.CENTERED_MED, Color.white, x[i], y[i] + yPos);
		}
	}
	
	public boolean finished() {
		return yPos + height < 0;
	}
}