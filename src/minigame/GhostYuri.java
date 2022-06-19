package minigame;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import effects.entity.GlitchRandom;
import effects.entity.ShakeEffect;
import entity.Entity;
import entity.util.GlitchAnimation;
import render.RenderData;
import tile.TileMap;
import util.InputData;
import util.MathUtil;

public class GhostYuri extends Entity {
	
	// speed
	//private float acceleration;
	private float maxSpeed;
	
	// pathfinding
	//private Pathfinder pathfinder;
	
	// glitch effect
	protected GlitchAnimation animation;
	protected GlitchRandom glitch;
	//private ShakeEffect shakeEffect;
	
	// actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACKING = 4;
	
	// sprites
	protected Texture[] idleFrames;
	protected Texture[] walkFrames;
	protected Texture[] jumpFrames;
	protected Texture[] fallFrames;
	protected Texture[] attackFrames;
	// glitched sprites
	protected Texture[] idleGlitchFrames;
	protected Texture[][] walkGlitchFrames;
	protected Texture[] jumpGlitchFrames;
	protected Texture[] fallGlitchFrames;
	protected Texture[] attackGlitchFrames;
	
	// attack
	protected boolean attacking;
	protected int attackRange;
	protected int attackDamage;
	protected long attackStart;
	
	// stop
	protected boolean stop;
	
	// render
	private RenderData renderData;
	
	public GhostYuri(TileMap tm) {
		super(tm);
		renderData = new RenderData();
		renderData.alpha = 0.7f;
		initFrames();
		//pathfinder = new Pathfinder();
		animation = new GlitchAnimation();
		animation.setGlitchFrames(idleGlitchFrames);
		animation.setGlitchDelay(100);
		glitch = new GlitchRandom(animation, new ShakeEffect());
		initEffects();
		animation.setFrames(idleFrames);
		animation.setDelay(-1);
		// set motion values
		width = 60;
		height = 80;
		cwidth = 52;
		cheight = 80;
		moveSpeed = 0.8f;
		maxSpeed = 4f;
		stopSpeed = 0.6f;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		jumpStart = -15f;
		stopJumpSpeed = 0.5f;
		facingRight = true;
		attackRange = 28;
		attackDamage = 5;
	}
	
	/*
	 * Get the sprites from Textures to use in this class.
	 */
	private void initFrames() {
		// init sprites
		idleFrames = Textures.y_ghost_idle;
		walkFrames = Textures.y_ghost_walk;
		jumpFrames = Textures.y_ghost_jump;
		fallFrames = Textures.y_ghost_fall;
		attackFrames = Textures.y_ghost_attack;
		// init glitched sprites
		idleGlitchFrames = Textures.y_ghost_idle_glitch;
		walkGlitchFrames = Textures.y_ghost_walk_glitch;
		jumpGlitchFrames = Textures.y_ghost_jump_glitch;
		fallGlitchFrames = Textures.y_ghost_fall_glitch;
		attackGlitchFrames = Textures.y_ghost_attack_glitch;
	}
	
	/*
	 * Initialize the effects to use in this class.
	 */
	private void initEffects() {
		
	}
	
	// PHYSICS
	// OVERRIDE for custom collision checking
	protected void checkTileCollision(float x, float y) {
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - 24) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		if (topTile < 0 || bottomTile >= tm.getNumRows() || leftTile < 0 || rightTile >= tm.getNumCols()) {
			if (leftTile >= 0 && rightTile >= tm.getNumCols()) {
				if (bottomTile >= tm.getNumRows()) {
					topLeft = topRight = bottomLeft = bottomRight = false;
					return;
				}
				bottomLeft = tm.isBlocked(bottomTile, leftTile);
				bottomRight = bottomLeft;
				topLeft = tm.isBlocked(topTile, leftTile);
				topRight = topLeft;
			}
			else topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		topLeft = tm.isBlocked(topTile, leftTile);
		topRight = tm.isBlocked(topTile, rightTile);
		bottomLeft = tm.isBlocked(bottomTile, leftTile);
		bottomRight = tm.isBlocked(bottomTile, rightTile);
	}
	
	// PLAYER INPUT DATA
	public void processData(InputData data) {
		if (data == null) return;
		setLeft(data.A || data.LEFT);
		setRight(data.D || data.RIGHT);
		setJumping(data.W || data.UP);
		if (data.SPACE || data.LEFT_CLICK) setAttacking();
		if (data.locationData.use) {
			this.x = data.locationData.x;
			this.y = data.locationData.y;
			this.dx = data.locationData.dx;
			this.dy = data.locationData.dy;
		}
		//System.out.println("[GhostYuri] Data received");
	}
	
	public void setAttacking() {
		long elapsed = (System.nanoTime() - attackStart) / 1000000;
		if (elapsed < 600) return;
		attacking = true;
		attackStart = System.nanoTime();
	}
	
	// STANDARD ENTITY METHODS
	protected void getNextPosition() {
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) dx = -maxSpeed;
		}
		else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) dx = maxSpeed;
		}
		else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) dx = 0;
			}
			else {
				dx += stopSpeed;
				if (dx > 0) dx = 0;
			}
		}
		// can't move while attacking
		if (currentAction == ATTACKING && !(jumping || falling)) dx = 0;
		// jump
		if (jumping && !falling) {
			dy = jumpStart;
			Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.JUMP);
			falling = true;
		}
		// fall
		if (falling) {
			dy += fallSpeed;
			if (dy > 0) {
				jumping = false;
			}
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void stop(boolean stop) {
		this.stop = stop;
	}
	
	public void update() {
		glitch.update();
		if (stop) return;
		// move
		getNextPosition();
		checkCollisions();
		setPosition(xtemp, ytemp);
		if (x < width / 2) x = width / 2;
		if (x > tm.getWidth() - width / 2) x = tm.getWidth() - width / 2;
		// set animation
		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackFrames);
				animation.setDelay(150);
				animation.setGlitchFrames(attackGlitchFrames);
				animation.setGlitchDelay(100);
				Sfx.playSound(Sfx.TYPE_PLAYER, Sfx.ATTACK);
			}
			else {
				if (animation.hasPlayedOnce()) attacking = false;
			}
		}
		else if (jumping) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jumpFrames);
				animation.setDelay(-1);
				animation.setGlitchFrames(jumpGlitchFrames);
			}
		}
		else if (falling) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(fallFrames);
				animation.setDelay(-1);
				animation.setGlitchFrames(fallGlitchFrames);
			}
		}
		else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(walkFrames);
				animation.setDelay(200);
				int index = MathUtil.randInt(2);
				if (index == 0) animation.setGlitchFrames(walkGlitchFrames[0]);
				else animation.setGlitchFrames(walkGlitchFrames[2]);
			}
		}
		else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idleFrames);
				animation.setDelay(-1);
				animation.setGlitchFrames(idleGlitchFrames);
			}
		}
		// update animation
		animation.update();
		// update direction
		if (currentAction != ATTACKING) {
			if (right) facingRight = true;
			if (left) facingRight = false;
		}
	}
	
	public void render() {
		setMapPosition();
		if (currentAction == ATTACKING) {
			renderData.width = animation.getCurrentFrame().getImageWidth();
			renderData.height = animation.getCurrentFrame().getImageHeight();
			int offset = glitch.getOffset();
			renderData.x = x + xmap + offset;
			renderData.y = y + ymap;
			renderData.flip = !facingRight;
			renderData.texture = animation.getCurrentFrame();
			renderData.alpha = 0.84f;
			drawImage(renderData);
		}
		else {
			int offset = glitch.getOffset();
			renderData.flip = !facingRight;
			renderData.x = x + xmap + offset;
			renderData.y = y + ymap;
			renderData.width = animation.getCurrentFrame().getImageWidth();
			renderData.height = animation.getCurrentFrame().getImageHeight();
			renderData.texture = animation.getCurrentFrame();
			renderData.alpha = 0.84f;
			drawImage(renderData);
		}
	}
}