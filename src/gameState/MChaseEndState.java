package gameState;

import minigame.GhostMonika;
import util.InputData;
import util.input.KeyInput;
import util.input.MouseInput;

public class MChaseEndState extends MEndState {

	// input
	private static final int DATA_COUNT = 150;
	private InputData[] inputData;
	private int dataIndex;

	private GhostMonika ghost;

	public MChaseEndState(GameStateManager gsm, boolean challenge) {
		super(gsm, challenge);
		ghost = new GhostMonika(tilemap);
		ghost.setPosition(player.getX(), player.getY());
		// input data
		inputData = new InputData[DATA_COUNT];
		player.invincible((int) (1000 / 60.0 * DATA_COUNT));
	}

	private void checkGhostCollision() {
		if (player.intersects(ghost) && !player.isDead()) {
			if (player.damage())
				deadMenu.start();
		}
	}

	@Override
	public void update() {
		super.update();
		// update ghost
		ghost.update();
		// check ghost collision
		checkGhostCollision();
	}

	@Override
	public void render() {
		super.render();
		ghost.render();
		processInputData();
	}

	private void processInputData() {
		// process data
		InputData data = new InputData();
		data.W          = KeyInput.isDown(KeyInput.W);
		data.A          = KeyInput.isDown(KeyInput.A);
		data.S          = KeyInput.isDown(KeyInput.S);
		data.D          = KeyInput.isDown(KeyInput.D);
		data.UP         = KeyInput.isDown(KeyInput.UP);
		data.DOWN       = KeyInput.isDown(KeyInput.DOWN);
		data.LEFT       = KeyInput.isDown(KeyInput.LEFT);
		data.RIGHT      = KeyInput.isDown(KeyInput.RIGHT);
		data.SPACE      = KeyInput.isDown(KeyInput.SPACE);
		data.LEFT_CLICK = MouseInput.isPressed(MouseInput.LEFT);
		data.locationData = player.getLocationData();
		if (dataIndex % 5 == 0) data.locationData.use = true;
		inputData[dataIndex] = data;
		// send data to ghost
		int sendIndex = (dataIndex + 1) % inputData.length;
		ghost.processData(inputData[sendIndex]);
		// DEBUG OUTPUT
		//System.out.printf("[ChaseChallengeState] InputData[%03d] = " + inputData[dataIndex] + "\n", dataIndex);
		// continue
		dataIndex++;
		if (dataIndex >= inputData.length) dataIndex = 0;
	}
}
