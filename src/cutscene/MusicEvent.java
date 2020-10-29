package cutscene;

import assets.Music;
import errors.EventTypeException;

/*
 * An event during a cutscene that causes a change in the music.
 * It utilizes the music class that plays a track, stops the
 * currently playing track, or changes the volume of the current
 * track. This can be used to fade music in and out.
 */
public class MusicEvent extends CutsceneEvent {
	
	// TYPES
	public static final int PLAY = 0;
	public static final int STOP = 1;
	public static final int FADE = 2;
	private int type;
	
	// music ID
	private int musicID;
	
	// target and duration
	private float target;
	private int duration;
	
	// complete
	private boolean complete;
	
	// constructor
	public MusicEvent(int type) {
		this.type = type;
	}
	
	// set data
	public void setMusicID(int musicID) {
		this.musicID = musicID;
	}
	
	public void setTarget(float target) throws EventTypeException {
		if (type != FADE) throw new EventTypeException("Event is not type FADE");
		this.target = target;
	}
	
	public void setDuration(int duration) throws EventTypeException {
		if (type != FADE) throw new EventTypeException("Event is not type FADE");
		this.duration = duration;
	}
	
	// run the event
	public void update() {
		if (complete) return;
		switch(type) {
		case PLAY:
			Music.resetVolume();
			Music.play(musicID);
			break;
		case STOP:
			Music.stop();
			break;
		case FADE:
			Music.fade(target, duration);
			break;
		}
		complete = true;
	}
	
	public boolean finished() {
		return complete;
	}
}