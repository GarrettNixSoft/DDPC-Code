package gui;

import org.newdawn.slick.opengl.Texture;

import entity.util.Animation;
import errors.EffectTypeException;

public class DialogueEffect {
	
	// TYPES
	public static final int DIALOGUE = 0;
	public static final int CHARACTER = 1;
	private int type;
	private int duration;
	private long timer;
	
	// dialogue
	private int frequency;
	private int length;
	private String glitchText;
	
	// character
	private Texture[] effect;
	private Animation animation;
	
	// construct
	public DialogueEffect(int type) {
		this.type = type;
		if (type == DIALOGUE) {
			duration = 1000;
			generateGlitchText();
		}
		timer = -1;
	}
	
	// set parameters
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setFrequency(int frequency) throws EffectTypeException {
		if (type != DIALOGUE) throw new EffectTypeException("effect is not type: DIALOGUE");
		else {
			this.frequency = frequency;
		}
	}
	
	public void setLength(int length) throws EffectTypeException {
		if (type != DIALOGUE) throw new EffectTypeException("effect is not type: DIALOGUE");
		else {
			this.length = length;
			System.out.println("[DialogueEffect] Type DIALOGUE with length=" + length);
		}
	}
	
	public void setEffect(Texture[] effect) throws EffectTypeException {
		if (type != CHARACTER) throw new EffectTypeException("effect is not type: CHARACTER");
		else {
			this.effect = effect;
			animation = new Animation();
			animation.setFrames(effect);
			animation.setDelay(50);
		}
	}
	
	// get parameters
	public int getType() { return type; }
	public Texture[] getEffect() { return effect; }
	public Texture getCurrentFrame() { return animation.getCurrentFrame(); }
	public String getGlitchText() { return glitchText; }
	
	// methods
	private void generateGlitchText() {
		String charBank = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-=_+[]{}\\|\'\";:.>,</?`~";
		glitchText = "";
		for (int i = 0; i < length; i++) {
			glitchText += charBank.charAt((int) (Math.random() * charBank.length())); 
		}
	}
	
	// function
	public void update() {
		// init
		if (timer == -1) {
			System.out.println("[DialogueEffect] Starting effect [type=" + type + ", duration=" + duration + "]");
			timer = System.nanoTime();
		}
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (type == DIALOGUE) {
			// update text
			if (elapsed / (1000 / frequency) % 2 == 0) generateGlitchText();
		}
		if (type == CHARACTER) {
			// update animation
			animation.update();
		}
	}
	
	public boolean complete() {
		return ((System.nanoTime() - timer) / 1000000) > duration;
	}
}