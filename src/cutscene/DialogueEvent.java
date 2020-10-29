package cutscene;

import gui.Dialogue;
import util.data.DataCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class DialogueEvent extends CutsceneEvent {
	
	// dialogue
	private Dialogue dialogue;
	
	// start
	private boolean start;
	
	// specific lines
	//private int startingLine;
	//private int endingLine;
	
	public DialogueEvent(String file) {
		dialogue = new Dialogue(file);
		//startingLine = endingLine = -1;
	}
	
	public DialogueEvent(String file, int start, int end) {
		dialogue = new Dialogue(file);
		//startingLine = start;
		//endingLine = end;
	}
	
	public void update() {
		if (!start) {
			dialogue.open();
			start = true;
			DataCache.player.idle();
		}
		handleInput();
		dialogue.update();
	}
	
	public void render() {
		dialogue.render();
	}
	
	private void handleInput() {
		if (dialogue.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				dialogue.next();
			}
		}
	}
	
	public boolean finished() {
		return dialogue.isOpen() && dialogue.isClosing();
	}
}