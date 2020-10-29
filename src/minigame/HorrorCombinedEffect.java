package minigame;

import java.util.ArrayList;
import java.util.Collections;

import effects.CombinedEffect;
import effects.Event;
import effects.EventQueue;
import effects.VisualEffect;
import util.data.LevelCache;
import util.data.Settings;

public class HorrorCombinedEffect extends CombinedEffect {
	
	// access to level data
	private LevelCache lc;
	
	// effects
	private ThunderStormEffect thunderstorm;
	private FlashEffect flash;
	private DarknessEffect darkness;
	
	// events
	private EventQueue eventQueue;
	
	public HorrorCombinedEffect(LevelCache lc) {
		this.lc = lc;
		eventQueue = new EventQueue();
		initEffects();
	}
	
	// add all the effects we will use
	private void initEffects() {
		// initialize the effects
		thunderstorm = new ThunderStormEffect(lc.tilemap, lc.camera);
		thunderstorm.setEventQueue(eventQueue);
		thunderstorm.soundOn();
		thunderstorm.setRainVolume(Settings.musicVolume * 0.1f);
		thunderstorm.setPriority(3);
		flash = new FlashEffect();
		flash.setPriority(2);
		darkness = new DarknessEffect(lc.camera);
		darkness.setPriority(1);
		darkness.setMaxScale(1.6f);
		darkness.reset();
		darkness.shrink();
		// add the effects and sort them
		effects = new ArrayList<VisualEffect>();
		effects.add(thunderstorm);
		effects.add(flash);
		effects.add(darkness);
		Collections.sort(effects);
	}
	
	@Override
	public void update() {
		// update effects in order
		thunderstorm.update();
		flash.update();
		darkness.update();
		// check for events
		while (eventQueue.hasMoreEvents()) {
			Event event = eventQueue.poll();
			switch (event.getEventID()) {
			case Event.LIGHTNING:
				darkness.flash();
				flash.play();
				break;
			}
		}
	}

	@Override
	public void render() {
		for (VisualEffect ve : effects) ve.render();
	}

}