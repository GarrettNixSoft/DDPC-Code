package unused;

import effects.SnowEffect;
import gameState.GameState;
import main.Render;
import util.input.KeyInput;

public class ScreencapState extends GameState {
	
	// things to render
	private SnowEffect snow;
	
	public ScreencapState() {
		snow = new SnowEffect();
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		snow.update();
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		snow.render();
	}

	@Override
	protected void handleInput() {
		// TODO Auto-generated method stub
		if (KeyInput.isPressed(KeyInput.S)) Render.takeScreenshot(System.getProperty("user.home") + "\\");
	}
	
	
	
}