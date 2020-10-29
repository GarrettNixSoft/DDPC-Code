package cutscene;

import event.GameEvent;

/*
 * A wrapper class used to store GameEvents that
 * can be triggered during Cutscenes.
 */
public class CutsceneGameEvent extends CutsceneEvent {
	
	private GameEvent event;
	private boolean active;
	
	public CutsceneGameEvent(GameEvent event) {
		this.event = event;
		active = false;
	}
	
	public void update() {
		//System.out.println("[CutsceneGameEvent] update");
		if (!active) {
			event.activate();
			active = true;
		}
		event.update();
	}
	
	public void render() {
		event.render();
	}
	
	public boolean finished() {
		return event.complete();
	}

}