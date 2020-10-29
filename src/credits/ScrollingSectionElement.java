package credits;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.newdawn.slick.Color;

import assets.Fonts;
import main.Render;

import static main.Render.drawString;

public class ScrollingSectionElement extends ScrollingElement {
	
	// content
	private String title;
	private String[] col1;
	private String[] col2;
	
	// locations
	private float titleX;
	private float[] x1;
	private float[] y1;
	private float[] x2;
	private float[] y2;
	
	public ScrollingSectionElement(String title, String[] col1, String[] col2) {
		this.title = title;
		this.col1 = col1;
		this.col2 = col2;
		yPos = Render.HEIGHT;
		init();
	}
	
	private void init() {
		// set title position
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setFont(Fonts.AWT_FULLSCREEN);
		Rectangle2D r = Fonts.AWT_FULLSCREEN.getStringBounds(title, ((Graphics2D) g).getFontRenderContext());
		int wd = (int) r.getWidth();
		titleX = 400 - (wd / 2);
		// set column positions
		x1 = new float[col1.length];
		y1 = new float[col1.length];
		x2 = new float[col2.length];
		y2 = new float[col2.length];
		height = (int) r.getHeight() + 80;
		for (int i = 0; i < col1.length; i++) {
			Rectangle2D rect = Fonts.AWT_SCROLL.getStringBounds(col1[i], ((Graphics2D) g).getFontRenderContext());
			int ht = (int) rect.getHeight();
			height += ht;
			x1[i] = 100;
			y1[i] = 80 + (ht * i);
		}
		for (int i = 0; i < col2.length; i++) {
			Rectangle2D rect = Fonts.AWT_SCROLL.getStringBounds(col2[i], ((Graphics2D) g).getFontRenderContext());
			int width = (int) rect.getWidth();
			x2[i] = 700 - width;
			y2[i] = y1[i];
		}
		scrollSpeed = 0.5f;
	}
	
	public void update() {
		yPos -= scrollSpeed;
	}
	
	public void render() {
		drawString(title, Fonts.FULLSCREEN, Color.white, titleX, 0 + yPos);
		for (int i = 0; i < col1.length; i++) {
			drawString(col1[i], Fonts.SCROLL, Color.white, x1[i], y1[i] + yPos);
		}
		for (int i = 0; i < col2.length; i++) {
			drawString(col2[i], Fonts.SCROLL, Color.white, x2[i], y2[i] + yPos);
		}
	}
	
	public boolean finished() {
		return yPos + height < 0;
	}
}