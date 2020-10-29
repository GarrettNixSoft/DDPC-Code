package effects.entity;

import util.MathUtil;

public class EffectRandomizer {
	
	// effect
	private EntityEffect effect;
	
	// timing
	private long timer;
	private boolean active;
	private int activationDelay;
	private int duration;
	private int averageDelay;
	private int delayVariance;
	private int averageDuration;
	private int durationVariance;
	
	// default
	public EffectRandomizer(EntityEffect effect) {
		this.effect = effect;
		averageDelay = 3000;
		delayVariance = 1500;
		averageDuration = 400;
		durationVariance = 200;
		timer = System.nanoTime();
	}
	
	// overloaded
	public EffectRandomizer(EntityEffect effect, int averageDelay, int averageDuration) {
		this.effect = effect;
		this.averageDelay = averageDelay;
		delayVariance = averageDelay / 10;
		this.averageDuration = averageDuration;
		durationVariance = averageDuration / 10;
		timer = System.nanoTime();
	}
	
	// fully overloaded
	public EffectRandomizer(EntityEffect effect, int averageDelay, int averageDuration, int delayVariance, int durationVariance) {
		this.effect = effect;
		this.averageDelay = averageDelay;
		this.averageDuration = averageDuration;
		this.delayVariance = delayVariance;
		this.durationVariance = durationVariance;
		timer = System.nanoTime();
	}
	
	public EntityEffect getEffect() { return effect; }
	
	private void getNextValues() {
		activationDelay = MathUtil.randInt(averageDelay - delayVariance, averageDelay + delayVariance);
		duration = MathUtil.randInt(averageDuration - durationVariance, averageDuration + durationVariance);
		timer = System.nanoTime();
	}
	
	public void update() {
		effect.update();
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (active) {
			if (elapsed > duration) {
				effect.deactivate();
				active = false;
				getNextValues();
				System.out.println("[EffectRandomizer] Deactivate.");
			}
		}
		else {
			if (elapsed > activationDelay) {
				effect.activate();
				active = true;
				timer = System.nanoTime();
				System.out.println("[EffectRandomizer] Activate!");
			}
		}
	}
	
}