package gameState;
public abstract class GameState {
	
	protected GameStateManager gsm;
	protected int audioChannel = -1;
	
	public abstract void update();
	public abstract void render();
	protected abstract void handleInput();
	
	public void refresh() {
		// nothing: used by menuState as an override (it's a janky fix I know)
	}
}