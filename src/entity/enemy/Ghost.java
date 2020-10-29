package entity.enemy;

import org.newdawn.slick.opengl.Texture;

import assets.Textures;
import entity.util.Animation;
import tile.TileMap;

import static main.Render.drawImage;

public class Ghost extends Enemy {
	
	// sprites
	private Texture[] sprites;
	private int tier;
	private boolean glitch;
		
	// movement
	private int xmin;
	private int xmax;
		
	// flinching
	private long flinchTimer;
	
	// death animation
	private long killTimer;
	
	public Ghost(TileMap tm, int tier, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		xmin = x - 180;
		xmax = x + 180;
		health = 8;
		moveSpeed = 2.5f;
		fallSpeed = 0.56f;
		maxFallSpeed = 14f;
		right = false;
		dx = -moveSpeed;
		width = 60;
		height = 80;
		cwidth = 56;
		cheight = 80;
		this.tier = tier;
		System.out.println("TIER: " + tier);
		switch (tier) {
			case 0:
				sprites = Textures.ghost_1;
				health = 5;
				value = 200;
				break;
			case 1:
				sprites = Textures.ghost_2;
				health = 8;
				value = 500;
				break;
			case 2:
				sprites = Textures.ghost_1_glitch;
				health = 5;
				value = 200;
				glitch = true;
				break;
			case 3:
				sprites = Textures.ghost_2_glitch;
				health = 8;
				value = 500;
				glitch = true;
				break;
		}
		// set up animation
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(250);
		killTimer = -1;
	}
	
	public int getTier() { return tier; }
	
	private void getNextPosition() {
		if (right) {
			if (x > xmax) {
				x = xmax;
				dx = -moveSpeed;
				right = false;
			}
		}
		else {
			if (x < xmin) {
				x = xmin;
				dx = moveSpeed;
				right = true;
			}
		}
		if (falling) {
			dy += fallSpeed;
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
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
				dx = moveSpeed;
				right = true;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else xtemp += dx;
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = -moveSpeed;
				right = false;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else if (bottomLeft && !bottomRight) {
				dx = -moveSpeed;
				right = false;
			}
			else xtemp += dx;
		}
		
		// check falling
		if (!falling) {
			checkTileCollision(x, ydest + 1);
			if (!(bottomLeft || bottomRight)) falling = true;
		}
	}
	
	private void checkFall() {
		if (!falling) {
			checkTileCollision(x, ydest + 1);
			if (!(bottomLeft || bottomRight)) falling = true;
		}
	}
	
	private void checkTurn() {
		checkTileCollision(x, y + 1);
		if (dx > 0) {
			if (bottomLeft && !bottomRight) {
				right = false;
				dx = -moveSpeed;
			}
		}
		else {
			if (bottomRight && !bottomLeft) {
				right = true;
				dx = moveSpeed;
			}
		}
	}
	
	public void update() {
		if (killTimer == -1) {
			getNextPosition();
			checkCollisions();
			setPosition(xtemp, ytemp);
			checkTurn();
			if (x < width / 2) {
				x = width / 2;
				if (!right) {
					right = true;
					dx = moveSpeed;
				}
			}
			if (x > tm.getWidth() - width / 2) {
				x = tm.getWidth() - width / 2;
				if (right) {
					right = false;
					dx = -moveSpeed;
				}
			}
			animation.update();
		}
		else {
			getNextPosition();
			checkFall();
			setPosition(xtemp, ytemp);
		}
	}
	
	public void render() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 2000) flinching = false;
			if (elapsed / 100 % 2 == 0) return;
		}
		setMapPosition();
		if (notOnScreen()) return;
		if (killTimer == -1) {
			if (right) drawImage(animation.getCurrentFrame(), x + xmap - width / 2, y + ymap - height / 2, width, height, 0.8f);
			else drawImage(animation.getCurrentFrame(), x + xmap + width / 2, y + ymap - height / 2, -width, height, 0.8f);
		}
		else {
			long elapsed = (System.nanoTime() - killTimer) / 1000000;
			float percent = (1.0f - (elapsed / 1500.0f)) * 0.8f;
			if (right) drawImage(sprites[1], x + xmap - width / 2, y + ymap - height / 2, width, height, percent);
			else drawImage(sprites[1], x + xmap + width / 2, y + ymap - height / 2, -width, height, percent);
		}
	}
	
	public void damage(int damage) {
		if (flinching || health <= 0) return;
		health -= damage;
		if (health <= 0) {
			killTimer = System.nanoTime();
			dx = 0;
		}
		else {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	public boolean remove() {
		if (glitch) return health <= 0;
		else return health <= 0 && (System.nanoTime() - killTimer) / 1000000 >= 1500;
	}
} 