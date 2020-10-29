package cutscene;

import java.util.ArrayList;

import util.data.LevelCache;

public class Cutscene {
	
	// trigger
	private Trigger trigger;
	
	// timing
	//private long masterTimer;
	//private long currentTimer;
	
	// events
	private int currentEvent;
	private ArrayList<CutsceneEvent> events;
	private boolean complete;
	
	// dependency
	private boolean dependent;
	private boolean unlocked;
	private int dependency;
	
	// parent
	private LevelCache lc;
	
	public Cutscene(LevelCache lc) {
		this.lc = lc;
		events = new ArrayList<CutsceneEvent>();
		currentEvent = 0;
		dependent = false;
		dependency = -1;
	}
	
	public void addEvent(CutsceneEvent c) {
		events.add(c);
	}
	
	// INITIALIZE EVENTS
	public void init() {
		System.out.println("[Cutscene] Cutscene initializing");
		//for (CutsceneEvent event : events) {
		//	if (event instanceof CameraFocusEvent) {
		//		CameraFocusEvent e = (CameraFocusEvent) event;
		//		e.init();
		//	}
		//	if (event instanceof CameraLockEvent) {
		//		CameraLockEvent e = (CameraLockEvent) event;
		//		e.init();
		//	}
		//}
	}
	
	// trigger
	public void setTrigger(Trigger t) { this.trigger = t; }
	public Trigger getTrigger() { return trigger; }
	
	// dependency
	public void setDependent(int dependency) {
		dependent = true;
		this.dependency = dependency;
	}
	public boolean isDependent() { return dependent; }
	public boolean unlocked() { return unlocked; }
	public int getDependency() { return dependency; }
	
	public void update() {
		if (currentEvent >= events.size()) return;
		events.get(currentEvent).update();
		if (events.get(currentEvent).finished()) {
			// check if it was dialogue; if so, cancel player attack
			if (events.get(currentEvent) instanceof DialogueEvent) lc.player.idle();
			// move on
			currentEvent++;
			if (currentEvent >= events.size()) complete = true;
		}
	}
	
	public void render() {
		if (currentEvent >= events.size()) return;
		events.get(currentEvent).render();
	}
	
	public boolean complete() {
		return complete;
	}
	
}