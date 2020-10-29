package effects.entity;

import util.MathUtil;

public class ShakeEffect extends EntityEffect {
	
	private int shakeDelay;
	private int shakeSeverity;
	private int currentOffset;
	
	public ShakeEffect() {
		timer = System.nanoTime();
		shakeDelay = 20;
		shakeSeverity = 6;
	}
	
	public int getOffset() { return currentOffset; }
	
	private void shake() {
		currentOffset = MathUtil.randInt(-shakeSeverity, shakeSeverity);
	}
	
	@Override
	public void update() {
		if (synchronize) {
			if (!active && syncEffect.isActive()) {
				//System.out.println("[ShakeEffect] Sync activate!");
				active = true;
				timer = System.nanoTime();
			}
			else if (active && !syncEffect.isActive()) {
				active = false;
				//System.out.println("[ShakeEffect] Sync deactivate!");
			}
		}
		if (active) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > shakeDelay) {
				shake();
				timer = System.nanoTime();
			}
		}
	}

}