package effects;

public abstract class VisualEffect implements Comparable<VisualEffect> {
	
	protected int priority;
	
	public abstract void update();
	public abstract void render();
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public int compareTo(VisualEffect ve) {
		return ve.getPriority() - priority;
	}
	
}