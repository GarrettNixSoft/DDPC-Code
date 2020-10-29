package effects;

import static main.Render.drawStringShadowed;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.newdawn.slick.Color;

import assets.Fonts;
import tile.TileMap;

public class TextEffect extends VisualEffect {
	
	// text
	private String text;
	
	// location
	private TileMap tm;
	private float x;
	private float y;
	private float xmap;
	private float ymap;
	private float xOffset;
	private float yOffset;
	
	// timing
	private long timer;
	
	public TextEffect(String text, TileMap tm, int x, int y) {
		this.text = text;
		this.tm = tm;
		this.x = x;
		this.y = y;
		System.out.println("starting x: " + x);
		System.out.println("starting y: " + y);
		timer = System.nanoTime();
		initLocation();
	}
	
	private void initLocation() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		//FontMetrics fm = g.getFontMetrics(Fonts.AWT_SCORE);
		Rectangle2D r = Fonts.AWT_SCORE.getStringBounds(text, ((Graphics2D) g).getFontRenderContext());
		//int width = SwingUtilities.computeStringWidth(fm, text);
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		xOffset = -width / 2;
		yOffset = -height / 2;
		System.out.println("x offset: " + xOffset);
		System.out.println("y offset: " + yOffset);
	}
	
	private void setMapPosition() {
		xmap = tm.getX();
		ymap = tm.getY();
	}
	
	public void update() {
		y -= 0.15f;
	}
	
	public void render() {
		// render
		setMapPosition();
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed <= 2000) drawStringShadowed(text, Fonts.SCORE, Color.white, x + xmap + xOffset, y + ymap + yOffset, 1, 1);
		else {
			float percent = 1.0f - ((elapsed - 2000.0f) / 1000.0f);
			drawStringShadowed(text, Fonts.SCORE, Color.white, x + xmap + xOffset, y + ymap + yOffset, 1, 1, percent);
		}
	}
	
	public boolean remove() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > 3000) return true;
		else return false;
	}
}