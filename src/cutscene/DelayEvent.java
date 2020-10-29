package cutscene;

public class DelayEvent extends CutsceneEvent {

	private int duration;
	private long timer;
	
	public DelayEvent(int duration) {
		this.duration = duration;
		timer = -1;
	}
	
	public void update() {
		if (timer == -1) {
			timer = System.nanoTime();
			System.out.println("[DelayEvent] Duration " + duration + "ms");
		}
		return;
	}
	
	public boolean finished() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		return elapsed > duration;
	}

}