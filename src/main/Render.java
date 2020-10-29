package main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import render.RenderData;
import util.system.OS;

public class Render {
	
	// game dimensions
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	// frame buffers
	private static int frameBuffer;
	
	// INITIALIZING GAME WINDOW
	public static void beginSession() {
		Display.setTitle("Doki Doki Platformer Club!");
		try {
//			Display.setIcon(new ByteBuffer[] {
//		            new ImageIOImageData().imageToByteBuffer(ImageIO.read(Render.class.getResourceAsStream("/icon.png")), false, false, null),
//		            new ImageIOImageData().imageToByteBuffer(ImageIO.read(Render.class.getResourceAsStream("/icon.png")), false, false, null)
//		    });
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			//Display.setLocation(3000, 250);
			Display.create();
			//Display.setResizable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		TextureImpl.bindNone();
		// get the operating system
		OS.checkOS();
	}
	
	// RENDERING OBJECTS ON SCREEN
	// draw a rectangle with the given starting point and dimensions
	public static void drawRect(float x, float y, float width, float height, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a rectangle given a Rectangle object
	public static void drawRect(java.awt.Rectangle rect, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBegin(GL_QUADS);
		float x = (float) rect.getX();
		float y = (float) rect.getY();
		float width = (float) rect.getWidth();
		float height = (float) rect.getHeight();
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// draw a rectangular outline with the given starting point and dimensions
	public static void drawOutline(float x, float y, float width, float height, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBegin(GL_POLYGON);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a rectangular outline given a Rectangle object
	public static void drawOutline(java.awt.Rectangle rect, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBegin(GL_POLYGON);
		float x = (float) rect.getX();
		float y = (float) rect.getY();
		float width = (float) rect.getWidth();
		float height = (float) rect.getHeight();
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glEnable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// draw a line between the given coordinates
	public static void drawLine(float x1, float y1, float x2, float y2, Color c) {
		c.bind();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);
		glEnd();
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// draw a line given a Line2D object
	public static void drawLine(Line2D.Double line, Color c) {
		c.bind();
		float x1 = (float) line.getX1();
		float x2 = (float) line.getX2();
		float y1 = (float) line.getY1();
		float y2 = (float) line.getY2();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE, GL_ZERO, GL_ONE);
		glBegin(GL_LINES);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);
		glEnd();
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// universal draw method, uses a RenderData object to dynamically render anything given
	public static void drawImage(RenderData data) {
		Texture texture = data.texture;
		float width = data.width;
		if (data.width == 0) width = texture.getImageWidth();
		if (data.flip) width *= -1;
		float height = data.height;
		if (data.height == 0) texture.getImageHeight();
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, data.alpha);
		glTranslatef(data.x, data.y, 0);
		glRotatef(data.angle, 0, 0, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		if (data.center) {
			glVertex2f(-width / 2, -height / 2);
			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width / 2, -height / 2);
			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width / 2, height / 2);
			glTexCoord2f(0, texture.getHeight());
			glVertex2f(-width / 2, height / 2);
		}
		else {
			glVertex2f(0, 0);
			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width, 0);
			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);
			glTexCoord2f(0, texture.getHeight());
			glVertex2f(0, height);
		}
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// draw a texture starting at the given coordinates with the texture's width and height
	public static void drawImage(Texture texture, float x, float y) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(texture.getImageWidth(), 0);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(texture.getImageWidth(), texture.getImageHeight());
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, texture.getImageHeight());
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		//glDisable(GL_BLEND);
	}
	
	public static void drawImageNoBlend(Texture texture, float x, float y) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		x = (int) x;
		y = (int) y;
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(texture.getImageWidth(), 0);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(texture.getImageWidth(), texture.getImageHeight());
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, texture.getImageHeight());
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// draw a texture starting at the given coordinates scaled to the given width and height
	public static void drawImage(Texture texture, float x, float y, float width, float height) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		texture.bind();
		glTranslatef(x + width / 2, y + height / 2, 0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// same as drawImage, but rotated with the given angle
	public static void drawImageR(Texture texture, float x, float y, float angle) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		texture.bind();
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTranslatef(x + texture.getImageWidth() / 2, y + texture.getImageHeight() / 2, 0);
		glRotatef(angle, 0, 0, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-texture.getImageWidth() / 2, -texture.getImageHeight() / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(texture.getImageWidth() / 2, -texture.getImageHeight() / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(texture.getImageWidth() / 2, texture.getImageHeight() / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-texture.getImageHeight() / 2, texture.getImageHeight() / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// same as drawImage, but scaled and rotated
	public static void drawImageR(Texture texture, float x, float y, float width, float height, float angle) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		texture.bind();
		glTranslatef(x + width / 2, y + height / 2, 0);
		glRotatef(angle, 0, 0, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// same as drawImage, but with an alpha
	public static void drawImage(Texture texture, float x, float y, float a) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, a);
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(texture.getImageWidth(), 0);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(texture.getImageWidth(), texture.getImageHeight());
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, texture.getImageHeight());
		glEnd();
		glLoadIdentity();
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// same as drawImage, but scaled and with an alpha
	public static void drawImage(Texture texture, float x, float y, float width, float height, float a) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, a);
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width, 0);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width, height);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// draw an image faded and rotated
	public static void drawImageAR(Texture texture, float x, float y, float width, float height, float alpha, float angle) {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		glTranslatef(x, y, 0);
		glRotatef(angle, 0, 0, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width, 0);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width, height);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	// draw an image centered on (x,y)
	public static void drawImageC(Texture texture, float x, float y) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		float width = texture.getImageWidth();
		float height = texture.getImageHeight();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}
	
	// draw an image centered on (x,y) with an alpha
	public static void drawImageC(Texture texture, float x, float y, float a) {
		//System.out.println("[Render] a = " + a);
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, a);
		float width = texture.getImageWidth();
		float height = texture.getImageHeight();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		Color.white.bind();
	}
	
	// draw an image centered on (x,y) scaled to Width x Height
	public static void drawImageC(Texture texture, float x, float y, float width, float height) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}
	
	// draw an image centered on (x,y) and scaled
	public static void drawImageCS(Texture texture, float x, float y, float scale) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glEnable(GL_BLEND);
		texture.bind();
		float width = texture.getImageWidth();
		float height = texture.getImageHeight();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2 * scale, -height / 2 * scale);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2 * scale, -height / 2 * scale);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2 * scale, height / 2 * scale);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2 * scale, height / 2 * scale);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		//glDisable(GL_BLEND);
	}
	
	// draw an image centered on (x,y) and flipped horizontally
	public static void drawImageCF(Texture texture, float x, float y) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
		glEnable(GL_BLEND);
		texture.bind();
		float width = texture.getImageWidth() * -1;
		float height = texture.getImageHeight();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2, -height / 2);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2, height / 2);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2, height / 2);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}
	
	// draw image centered, scaled, and with an alpha
	public static void drawImageCSA(Texture texture, float x, float y, float scale, float alpha) {
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
		glEnable(GL_BLEND);
		texture.bind();
		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		float width = texture.getImageWidth();
		float height = texture.getImageHeight();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-width / 2 * scale, -height / 2 * scale);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(width / 2 * scale, -height / 2 * scale);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(width / 2 * scale, height / 2 * scale);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(-width / 2 * scale, height / 2 * scale);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		//glDisable(GL_BLEND);
	}
	
	// RENDER WITH RENDEREFFECTS
	
	// draw a string using a given TrueTypeFont
	public static void drawString(String s, TrueTypeFont f, Color c, float x, float y) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		f.drawString(x, y, s, c);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string using a given UnicodeFont
	public static void drawString(String s, UnicodeFont f, Color c, float x, float y) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		f.drawString(x, y, s, c);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string with the default font and color
	public static void drawString(String s, float x, float y) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		assets.Fonts.SCORE.drawString(x, y, s, Color.white);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string with a staggered shadow
	public static void drawStringShadowed(String s, UnicodeFont f, Color c, float x, float y, float xOffset, float yOffset) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		f.drawString(x + xOffset, y + yOffset, s, Color.black);
		f.drawString(x, y, s, c);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string with default font and color and a staggered shadow
	public static void drawStringShadowed(String s, float x, float y, float xOffset, float yOffset) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		assets.Fonts.SCORE.drawString(x + xOffset, y + yOffset, s, Color.black);
		assets.Fonts.SCORE.drawString(x, y, s, Color.white);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string with default font and color and a staggered shadow (default offset = 1)
	public static void drawStringShadowed(String s, float x, float y) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		assets.Fonts.SCORE.drawString(x + 1, y + 1, s, Color.black);
		assets.Fonts.SCORE.drawString(x, y, s, Color.white);
		glDisable(GL_BLEND);
		Color.white.bind();
	}
	
	// draw a string with a staggered shadow
	public static void drawStringShadowed(String s, UnicodeFont f, Color c, float x, float y, float xOffset, float yOffset, float a) {
		//System.out.println("shade!");
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float aTemp = a;
		a -= 0.15;
		if (a < 0) a = 0;
		f.drawString(x + xOffset, y + yOffset, s, new Color(0f, 0f, 0f, a));
		f.drawString(x, y, s, new Color(c.r, c.g, c.b, aTemp));
		glDisable(GL_BLEND);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Color.white.bind();
	}
	
	// draw a rect over the whole screen with the given color and alpha
	public static void fillScreen(Color c) {
		drawRect(0, 0, WIDTH, HEIGHT, c);
	}
	
	// render a light!
	public static void renderLight(FloatBuffer light, FloatBuffer position) {
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL11.GL_LIGHT_MODEL_TWO_SIDE, light);
		glLight(GL_LIGHT0, GL_POSITION, position);
		glDisable(GL_LIGHTING);
	}
	
	// take a screenshot!
	public static void takeScreenshot(String path) {
		glReadBuffer(GL_FRONT);
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(WIDTH * HEIGHT * bpp);
		glReadPixels(0, 0, WIDTH, HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy-HH.mm.ss");
		Date dateObj = new Date();
		String date = format.format(dateObj);
		File destination = new File(path + "\\screenshot-" + date + ".png");
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				int i = (x + (WIDTH * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, HEIGHT - (y + 1), (0xFF << 24 | (r << 16) | (g << 8) | b));
			}
		}
		try {
			ImageIO.write(image, "PNG", destination);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// EXPERIMENTAL
	// create alternate frame buffer
	public static int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return frameBuffer;
	}
	
	// get texture for frame buffer
	//public int createTextureAttachment() {
	//	int texture = GL11.glGenTextures();
	//	
	//	return texture;
	//}
	
	// set OpenGL to use the alternate frame buffer
	public static void bindFrameBuffer(int frameBuffer) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	// switch back to the old frame buffer
	public static void revertFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	// delete the alternate frame buffer on closing
	public static void cleanUp() {
		GL30.glDeleteFramebuffers(frameBuffer);
	}
}