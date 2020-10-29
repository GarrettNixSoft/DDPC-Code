package cutscene;

import entity.Entity;
import errors.EventTypeException;
import util.data.DataCache;

public class EntityEvent extends CutsceneEvent {
	
	// types
	public static final int GLITCH = 0;
	public static final int MOVE = 1;
	public static final int JUMP = 2;
	private int type;
	
	// directions (for move events)
	public static final int DIR_UP = 0;
	public static final int DIR_DOWN = 1;
	public static final int DIR_LEFT = 2;
	public static final int DIR_RIGHT = 3;
	
	// entity
	private int entity;
	
	// glitch
	private float glitchDuration;
	
	// move
	private int moveTicks;
	private int direction;
	
	// jump
	private int jumpTicks;
	private int jumpDirection;
	
	// completion
	private boolean finished;
	
	public EntityEvent(int entity, int type) {
		this.entity = entity;
		this.type = type;
	}
	
	// set types
	public void setDirection(int direction) throws EventTypeException {
		if (type != MOVE) throw new EventTypeException("Event is not type: MOVE");
		else {
			this.direction = direction;
		}
	}
	
	public void setMoveTicks(int moveTicks) throws EventTypeException {
		if (type != MOVE) throw new EventTypeException("Event is not type: MOVE");
		else {
			this.moveTicks = moveTicks;
		}
	}
	
	public void setGlitchDuration(float glitchDuration) throws EventTypeException {
		if (type != GLITCH) throw new EventTypeException("Event is not type: GLITCH");
		else {
			this.glitchDuration = glitchDuration;
		}
	}
	
	public void setJumpDirection(int jumpDirection) throws EventTypeException {
		if (type != JUMP) throw new EventTypeException("Event is not type: JUMP");
		else {
			this.jumpDirection = jumpDirection;
		}
	}
	
	public void setJumpTicks(int jumpTicks) throws EventTypeException {
		if (type != JUMP) throw new EventTypeException("Event is not type: JUMP");
		else {
			this.jumpTicks = jumpTicks;
		}
	}
	
	public void update() {
		if (finished) return;
		Entity e = null;
		switch (entity) {
		case 0:
			e = DataCache.player;
			break;
		case 1:
			e = DataCache.characters[0];
			break;
		case 2:
			e = DataCache.characters[1];
			break;
		case 3:
			e = DataCache.characters[2];
			break;
		case 4:
			e = DataCache.characters[3];
			break;
		case 5:
			e = DataCache.characters[4];
			break;
		}
		if (type == GLITCH) {
			// handle glitch
			e.glitch(glitchDuration);
			finished = true;
		}
		else if (type == MOVE) {
			// handle move
			switch (direction) {
			case DIR_UP:
				e.setUp(true);
				break;
			case DIR_DOWN:
				e.setDown(true);
				break;
			case DIR_LEFT:
				e.setLeft(true);
				break;
			case DIR_RIGHT:
				e.setRight(true);
				break;
			}
			// check completion
			moveTicks--;
			if (moveTicks <= 0) {
				finished = true;
				e.idle();
			}
		}
		else if (type == JUMP) {
			// handle jump
			e.setJumping(true);
			switch(jumpDirection) {
			case 0:
				e.setLeft(true);
				break;
			case 1:
				e.setRight(true);
				break;
			case 2:
				e.setLeft(false);
				e.setRight(false);
				break;
			}
			// check completion
			jumpTicks--;
			if (jumpTicks <= 0) {
				finished = true;
				e.idle();
			}
		}
	}
	
	public boolean finished() {
		return finished;
	}
	
}