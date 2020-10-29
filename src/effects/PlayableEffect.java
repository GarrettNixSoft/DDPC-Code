package effects;

public abstract class PlayableEffect extends VisualEffect {
	
	protected boolean playing;
	
	public abstract void update();
	public abstract void render();
	public abstract void play();
	public boolean isPlaying() { return playing; }
	
}