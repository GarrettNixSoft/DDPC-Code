package cutscene;

import gameState.PlayState;

public abstract class CutsceneEvent {
	
	// values
	protected PlayState game;
	
	// methods
	public abstract void update();
	public abstract boolean finished();
	
	// to be overridden
	public void render() {
		return;
	}
	
}