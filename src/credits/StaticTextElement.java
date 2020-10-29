package credits;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

public abstract class StaticTextElement extends CreditsElement {
	
	protected String[] text;
	protected UnicodeFont font;
	protected Color color;
	protected int stage;
	protected int duration;
	protected int fadeDuration;
	
	public StaticTextElement(String[] text, UnicodeFont font, Color color) {
		this.text = text;
		this.font = font;
		this.color = color;
		duration = 4000;
		fadeDuration = 1500;
	}
	
	public StaticTextElement(String[] text, UnicodeFont font, Color color, int duration, int fadeDuration) {
		this.text = text;
		this.font = font;
		this.color = color;
		this.duration = duration;
		this.fadeDuration = fadeDuration;
	}
	
	public void update() {
		long elapsed = (System.nanoTime() - start) / 1000000;
		switch (stage) {
			case 0:
				if (elapsed > fadeDuration) stage++;
				break;
			case 1:
				if (elapsed - fadeDuration > duration) stage++;
				break;
			case 2:
				if (elapsed > duration + (fadeDuration * 2)) {
					stage++;
				}
				break;
		}
	}
	
	public boolean finished() {
		return stage > 2;
	}
	
	public abstract void render();
}