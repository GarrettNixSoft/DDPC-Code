package gui;

import static main.Render.drawImage;
import static main.Render.drawRect;
import static main.Render.drawString;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import org.newdawn.slick.Color;

import assets.Fonts;
import assets.Textures;
import main.Loader;

public class Dialogue {
	
	// content
	private DialogueLine[] lines;
	private int currentLine;
	private int currentChar;
	private int charDelay;
	private int split1;
	private int split2;
	
	// timing
	private long eventTimer;
	private int eventDuration;
	
	// actions
	private boolean opening;
	private boolean closing;
	private boolean open;
	private boolean advancing;
	private boolean waiting;
	
	// string width
	private BufferedImage image;
	private Graphics2D g;
	private FontMetrics metrics;
	private FontMetrics metrics2;
	private String glitchText;
	private boolean glitchComplete;
	
	// rendering
	private Color backgroundColor;
	
	// locations
	private int lineStart = 225;
	private int line1Y = 510, line2Y = 535, line3Y = 560;
	
	// create a new Dialog with the given lines
	public Dialogue(DialogueLine[] lines) {
		this.lines = lines;
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.setFont(Fonts.AWT_TEXT);
		metrics = g.getFontMetrics(Fonts.AWT_TEXT);
		metrics2 = g.getFontMetrics(Fonts.AWT_NAME);
		backgroundColor = new Color(12, 12, 12, 137);
	}
	
	public Dialogue(String file) {
		lines = Loader.loadDialogue(file);
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.setFont(Fonts.AWT_TEXT);
		metrics = g.getFontMetrics(Fonts.AWT_TEXT);
		metrics2 = g.getFontMetrics(Fonts.AWT_NAME);
		backgroundColor = new Color(12, 12, 12, 137);
	}
	
	// open the dialog box
	public void open() {
		if (open) {
			close();
			return;
		}
		opening = true;
		advancing = waiting = closing = false;
		eventTimer = System.nanoTime();
		eventDuration = 300;
	}
	
	// open the dialog box to a specified line
	public void open(int line) {
		if (open) {
			close();
			return;
		}
		opening = true;
		advancing = waiting = closing = false;
		eventTimer = System.nanoTime();
		eventDuration = 300;
		currentLine = line;
	}
	
	// close the dialog box
	public void close() {
		closing = true;
		opening = advancing = waiting = false;
		eventTimer = System.nanoTime();
		eventDuration = 300;
	}
	
	// advance to the next line
	public void advance() {
		advancing = true;
		opening = waiting = closing = false;
		eventTimer = System.nanoTime();
		currentLine++;
		if (currentLine == lines.length) {
			close();
			return;
		}
		if (lines[currentLine].textGlitch()) {
			System.out.println("[Dialogue] Text glitch! Skipping text roll...");
			skip();
			return;
		}
		charDelay = 25;
		int[] splits = getSplits(lines[currentLine].getLine());
		split1 = splits[0];
		split2 = splits[1];
		currentChar = 0;
	}
	
	// skip advancing
	public void skip() {
		if (lines[currentLine].isAuto()) return;
		int[] splits = getSplits(lines[currentLine].getLine());
		split1 = splits[0];
		split2 = splits[1];
		waiting = true;
		opening = advancing = closing = false;
		eventTimer = System.nanoTime();
		lines[currentLine].update();
	}
	
	public void skipAll() {
		currentLine = lines.length - 1;
		closing = true;
		opening = waiting = advancing = false;
		eventTimer = System.nanoTime();
	}
	
	public void next() {
		if (opening || closing || !open) return;
		if (advancing) skip();
		else advance();
	}
	
	// util
	private int[] getSplits(String line) {
		int s1 = -1, s2 = -1;
		for (int i = 0; i < line.length() + 1; i++) {
			currentChar = i;
			String str = line.substring(0, currentChar);
			if (s1 == -1) {
				int len = SwingUtilities.computeStringWidth(metrics, str);
				if (len > 450) {
					// check for word split
					int lastWordEnd = str.lastIndexOf(" ");
					if (lastWordEnd == -1) s1 = currentChar - 1;
					else s1 = lastWordEnd;
				}
			}
			else if (s2 == -1) {
				str = line.substring(s1, currentChar);
				int len = metrics.stringWidth(str);
				if (len > 450) {
					// check for word split
					int lastWordEnd = str.lastIndexOf(" ") + s1;
					s2 = lastWordEnd;
				}
			}
		}
		return new int[] {s1, s2};
	}
	
	// getters
	public boolean isOpen() { return open; }
	public boolean isOpening() { return opening; }
	public boolean isAdvancing() { return advancing; }
	public boolean isWaiting() { return waiting; }
	public boolean isClosing() { return closing; }
	public int getCurrentLine() { return currentLine; }
	
	public void update() {
		if (opening) { // dialogue box opening at the bottom
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			if (elapsed > eventDuration) {
				opening = false;
				open = true;
				currentLine = -1;
				advance();
			}
		}
		else if (advancing) { // text is appearing on screen
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			if (elapsed > charDelay) {
				currentChar++;
				eventTimer = System.nanoTime();
				if (currentChar >= lines[currentLine].getLine().length()) {
					if (lines[currentLine].isAuto()) {
						advance();
					}
					else {
						advancing = false;
						waiting = true;
						eventTimer = System.nanoTime();
					}
				}
			}
		}
		else if (waiting) { // waiting for user to advance
			lines[currentLine].update();
			if (lines[currentLine].textGlitch()) {
				glitchText = lines[currentLine].getGlitchText();
				int[] splits = getSplits(glitchText);
				split1 = splits[0];
				split2 = splits[1];
			}
			else if (!glitchComplete) {
				glitchComplete = true;
				int[] splits = getSplits(lines[currentLine].getLine());
				split1 = splits[0];
				split2 = splits[1];
			}
		}
		else if (closing) { // closing dialogue box
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			if (elapsed > eventDuration) {
				closing = false;
				open = false;
				split1 = split2 = -1;
				currentChar = 0;
			}
		}
	}
	
	public void render() {
		if (opening) {
			// draw box opening
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			float progress = (float) elapsed / eventDuration;
			int xStart = 150, yStart = 550;
			float xDelta = -50 * progress, yDelta = -50 * progress;
			float widthDelta = 100 * progress, heightDelta = 100 * progress;
			drawRect(xStart + xDelta, yStart + yDelta, 500 + widthDelta, heightDelta, backgroundColor);
		}
		else if (advancing) {
			// draw background
			drawRect(100, 500, 600, 100, backgroundColor);
			// draw face
			int width = lines[currentLine].getFace().getImageWidth();
			int height = lines[currentLine].getFace().getImageHeight();
			drawImage(lines[currentLine].getFace(), 150 - width / 2, 545 - height / 2, width, height);
			// draw name
			int nameWidth = metrics2.stringWidth(lines[currentLine].getName());
			drawString(lines[currentLine].getName(), Fonts.DIALOGUE_NAME, Color.white, 150 - nameWidth / 2, 572);
			// handle string splits
			if (split1 == -1) { // no split
				String line = lines[currentLine].getLine().substring(0, currentChar);
				line = line.trim();
				drawString(line, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
			}
			else if (split2 == -1) { // one split
				if (currentChar > split1) {
					String line1 = lines[currentLine].getLine().substring(0, split1);
					String line2 = lines[currentLine].getLine().substring(split1, currentChar);
					line1 = line1.trim();
					line2 = line2.trim();
					drawString(line1, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
					drawString(line2, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line2Y);
				}
				else {
					String line = lines[currentLine].getLine().substring(0, currentChar);
					line = line.trim();
					drawString(line, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
				}
			}
			else { // two splits
				if (currentChar > split2) {
					String line1 = lines[currentLine].getLine().substring(0, split1);
					String line2 = lines[currentLine].getLine().substring(split1, split2);
					String line3 = lines[currentLine].getLine().substring(split2, currentChar);
					line1 = line1.trim();
					line2 = line2.trim();
					line3 = line3.trim();
					drawString(line1, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
					drawString(line2, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line2Y);
					drawString(line3, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line3Y);
				}
				else {
					if (currentChar > split1) {
						String line1 = lines[currentLine].getLine().substring(0, split1);
						String line2 = lines[currentLine].getLine().substring(split1, currentChar);
						line1 = line1.trim();
						line2 = line2.trim();
						drawString(line1, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
						drawString(line2, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line2Y);
					}
					else {
						String line = lines[currentLine].getLine().substring(0, currentChar);
						line = line.trim();
						drawString(line, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
					}
				}
			}
		}
		else if (waiting) {
			// draw background
			drawRect(100, 500, 600, 100, backgroundColor);
			// draw face
			lines[currentLine].render();
			// draw name
			int nameWidth = metrics2.stringWidth(lines[currentLine].getName());
			drawString(lines[currentLine].getName(), Fonts.DIALOGUE_NAME, Color.white, 150 - nameWidth / 2, 572);
			// if glitch text, render that
			String text;
			if (lines[currentLine].textGlitch()) text = lines[currentLine].getGlitchText();
			else text = lines[currentLine].getLine();
			// handle string splits
			if (split1 == -1) { // no split
				String line = text.substring(0, currentChar);
				line = line.trim();
				drawString(line, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
			}
			else if (split2 == -1) { // one split
				String line1 = text.substring(0, split1);
				String line2 = text.substring(split1, currentChar);
				line1 = line1.trim();
				line2 = line2.trim();
				drawString(line1, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
				drawString(line2, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line2Y);
			}
			else { // two splits
				String line1 = text.substring(0, split1);
				String line2 = text.substring(split1, split2);
				String line3 = text.substring(split2, currentChar);
				line1 = line1.trim();
				line2 = line2.trim();
				line3 = line3.trim();
				drawString(line1, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line1Y);
				drawString(line2, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line2Y);
				drawString(line3, Fonts.DIALOGUE_TEXT, Color.white, lineStart, line3Y);
			}
			// draw arrow
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			if (elapsed / 700 % 2 == 0) drawImage(Textures.arrow, 680, 575);
		}
		else if (closing) {
			// draw box closing
			long elapsed = (System.nanoTime() - eventTimer) / 1000000;
			float progress = 1 - ((float) elapsed / eventDuration);
			int xStart = 150, yStart = 550;
			float xDelta = -50 * progress, yDelta = -50 * progress;
			float widthDelta = 100 * progress, heightDelta = 100 * progress;
			drawRect(xStart + xDelta, yStart + yDelta, 500 + widthDelta, heightDelta, backgroundColor);
		}
		else return; // this line is pointless but who cares
	}
}