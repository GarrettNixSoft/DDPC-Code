package entity;

import static main.Render.drawImage;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import entity.util.Animation;
import entity.util.Interactions;
import entity.util.Interactive;
import entity.util.TeleportEntity;
import main.Render;
import tile.DynamicTilemap;
import util.FadeManager;

public class Teleporter extends Entity implements Interactive {
	
	// zones
	protected Rectangle[] zones;
	protected int x1, y1, x2, y2, size;
	
	// interactivity
	
	// textures
	protected Texture[] sprites;
	
	// open/close
	private int transitionTime = 1500;
	private boolean opening;
	private boolean closing;
	private boolean open;
	private float scale;
	private long timer;
	
	// removal
	protected boolean remove;
	
	public Teleporter(DynamicTilemap tm, int x1, int y1, int x2, int y2) {
		super(tm);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.size = 60;
		this.scale = 1.0f;
		zones = new Rectangle[3];
		zones[0] = new Rectangle(x1 - size / 2, y1 - size / 2, size, size);
		zones[1] = new Rectangle(x2 - size / 2, y2 - size / 2, size, size);
		zones[2] = new Rectangle(x1 - 5, y1 - 5, 10, 10);
		sprites = Textures.teleport_1;
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(750);
	}
	
	public void interact(Entity e) {
		if (!open || !(e instanceof TeleportEntity)) return;
		if (e.getRectangle().intersects(zones[2])) {
			e.setPosition(x2, y2);
			FadeManager.fadeIn(1.0f, true);
			tm.setPositionAbsolute(Render.WIDTH / 2 - e.getX(), Render.HEIGHT / 2 - e.getY());
			Sfx.playSound(Sfx.TYPE_ENTITY, Sfx.TELEPORT);
		}
	}
	
	// actions
	public void open() {
		if (open) return;
		closing = open = false;
		opening = true;
		timer = System.nanoTime();
	}
	
	public void close() {
		if (!open) return;
		opening = open = false;
		closing = true;
		timer = System.nanoTime();
	}
	
	public void setOpen(boolean b) { open = b; }
	public void setRemove() { remove = true; }
	
	// update
	public void update() {
		animation.update();
		if (opening) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > transitionTime) {
				opening = false;
				open = true;
				scale = 1.0f;
			}
			else scale = (float) elapsed / transitionTime;
		}
		else if (closing) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > transitionTime) {
				closing = false;
				scale = 0;
			}
			else scale = (float) elapsed / (transitionTime - elapsed);
		}
	}
	
	// render
	public void render() {
		if (!open && !(opening || closing)) return;
		setMapPosition();
		if (!notOnScreen(zones[0])) drawImage(animation.getCurrentFrame(), x1 + xmap - (size * scale) / 2, y1 + ymap - (size * 2)/ 2, size * 2, size * 2);
		if (!notOnScreen(zones[1])) drawImage(animation.getCurrentFrame(), x2 + xmap - (size * scale) / 2, y2 + ymap - (size * 2) / 2, size * 2, size * 2);
	}
	
	public boolean remove() {
		return remove;
	}
	
	public void interact(Interactions e) {
		
	}
	
}