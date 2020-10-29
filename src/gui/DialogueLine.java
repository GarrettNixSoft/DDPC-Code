package gui;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

public class DialogueLine {
	
	private Texture face;
	private String name;
	private String line;
	private boolean auto;
	private DialogueEffect effect;
	
	public DialogueLine(Texture face, String name, String line) {
		this.face = face;
		this.name = name;
		this.line = line;
	}
	
	public void setEffect(DialogueEffect effect) { this.effect = effect; }
	public void setAuto() { auto = true; }
	
	public Texture getFace() { return face; }
	public String getName() { return name; }
	public String getLine() { return line; }
	public boolean isAuto() { return auto; }
	
	public boolean textGlitch() {
		if (effect == null) return false;
		else return effect.getType() == DialogueEffect.DIALOGUE;
	}
	
	public String getGlitchText() {
		if (effect == null) return "";
		else if (effect.getType() == DialogueEffect.DIALOGUE) return effect.getGlitchText();
		else return "";
	}
	
	public void update() {
		if (effect == null) return;
		effect.update();
		if (effect.complete()) effect = null;
	}
	
	public void render() {
		if (effect == null || effect.getType() == DialogueEffect.DIALOGUE) {
			drawImage(face, 150 - face.getImageWidth() / 2, 545 - face.getImageHeight() / 2);
		}
		else if (effect.getType() == DialogueEffect.CHARACTER) {
			Texture tex = effect.getCurrentFrame();
			drawImage(tex, 150 - tex.getImageWidth() / 2, 545 - tex.getImageHeight() / 2);
		}
	}
	
}