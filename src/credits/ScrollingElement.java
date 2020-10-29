package credits;

public abstract class ScrollingElement extends CreditsElement {
	
	protected float scrollSpeed;
	protected float yPos;
	protected int height;
	
	public float getEndPos() {
		return yPos + height;
	}
	
	public abstract void update();
	public abstract void render();
	public abstract boolean finished();
}