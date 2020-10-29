package entity;

import java.awt.Rectangle;

import entity.util.Animation;
import main.Render;
import tile.TileMap;

public abstract class Entity {
	
	// position and motion
	protected float x;
	protected float y;
	protected float xmap;
	protected float ymap;
	protected float dx;
	protected float dy;
	protected float moveSpeed;
	protected float maxSpeed;
	protected float stopSpeed;
	protected float fallSpeed;
	protected float maxFallSpeed;
	protected float jumpStart;
	protected float stopJumpSpeed;
	protected boolean up;
	protected boolean down;
	protected boolean left;
	protected boolean right;
	protected boolean falling;
	protected boolean jumping;
	
	// size
	protected int width;
	protected int height;
	
	// collisions
	protected int currRow;
	protected int currCol;
	protected float xdest;
	protected float ydest;
	protected float xtemp;
	protected float ytemp;
	protected int cwidth;
	protected int cheight;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected TileMap tm;
	protected int tileSize;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected boolean facingRight;
	
	// entity id
	protected String id;
	
	// expansion
	protected boolean glitch;
	protected int glitchDuration;
	protected long glitchTimer;
	
	public Entity(TileMap tm) {
		this.tm = tm;
		if (tm != null) this.tileSize = tm.getTileSize();
		id = "entity#" + hashCode();
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public boolean intersects(Entity e) {
		return getRectangle().intersects(e.getRectangle());
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int) (x - cwidth / 2), (int) (y - cheight / 2), cwidth, cheight);
	}
	
	public Rectangle getScreenRectangle() {
		return new Rectangle((int) (x + xmap - cwidth / 2), (int) (y + ymap  - cheight / 2), cwidth, cheight);
	}
	
	protected void checkTileCollision(float x, float y) {
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		if (topTile < 0 || bottomTile >= tm.getNumRows() || leftTile < 0 || rightTile >= tm.getNumCols()) {
			if (leftTile >= 0 && rightTile >= tm.getNumCols()) {
				if (bottomTile >= tm.getNumRows()) {
					topLeft = topRight = bottomLeft = bottomRight = false;
					return;
				}
				else if (leftTile >= tm.getNumRows()) {
					topRight = true;
					bottomRight = false;
					bottomLeft = true;
					topLeft = false;
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
	
	protected void checkCollisions() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		//check top or bottom tile collision
		checkTileCollision(x, ydest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else ytemp += dy;
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else ytemp += dy;
		}
		
		// check left or right tile collision
		checkTileCollision(xdest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else xtemp += dx;
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else xtemp += dx;
		}
		
		// check falling
		if (!falling) {
			checkTileCollision(x, ydest + 1);
			if (!(bottomLeft || bottomRight)) falling = true;
		}
	}

	public float getX() { return x; }
	public float getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCwidth() { return cwidth; }
	public int getCheight() { return cheight; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setJumping(boolean b) { jumping = b; }
	public void setFacingRight(boolean b) { facingRight = b; }
	
	public TileMap getTileMap() { return tm; }
	
	public abstract void update();
	public abstract void render();
	
	public void glitch(float duration) {
		// to be overridden
	}
	
	public boolean damage() {
		// to be overridden
		return false;
	}
	
	public void idle() {
		up = down = left = right = jumping = false;
	}
	
	public boolean isDead() {
		return false;
	}
	
	public void setMapPosition() {
		xmap = tm.getX();
		ymap = tm.getY();
	}
	
	/*
	 * Set position with raw coordinates.
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Set position with a coordinate array.
	 */
	public void setPosition(float[] coords) {
		
	}
	
	public boolean notOnScreen() {
		return x + xmap + width / 2 < 0 ||
				x + xmap - width / 2 > Render.WIDTH ||
				y + ymap + height / 2 < 0 ||
				y + ymap - height / 2 > Render.HEIGHT;
	}
	
	public boolean notOnScreen(Rectangle r) {
		return r.x + xmap + r.width / 2 < 0 ||
				r.x + xmap - r.width / 2 > Render.WIDTH ||
				r.y + ymap + r.height / 2 < 0 ||
				r.y + ymap - r.height / 2 > Render.HEIGHT;
	}
}