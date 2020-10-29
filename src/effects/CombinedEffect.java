package effects;

import java.util.List;

public abstract class CombinedEffect extends VisualEffect {
	
	protected List<VisualEffect> effects;
	
	public abstract void update();
	public abstract void render();
	
}