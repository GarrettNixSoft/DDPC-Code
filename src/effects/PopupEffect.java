package effects;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Sfx;
import assets.Textures;
import gui.Button;
import util.input.MouseInput;

public class PopupEffect extends VisualEffect {
	
	// background
	private Texture bg;
	
	// OK button
	private Button ok;
	
	// state
	private boolean open;
	
	public PopupEffect() {
		bg = Textures.error_popup;
		ok = new Button(Textures.menubuttons[Textures.OK][0], Textures.menubuttons[Textures.OK][1], 380, 395);
	}
	
	public void open() { open = true; }
	public void close() { open = false; }
	public boolean isOpen() { return open; }
	
	public void update() {
		if (!open) return;
		ok.update();
		if (MouseInput.isClicked(MouseInput.LEFT)) {
			if (ok.isHover()) {
				Sfx.playSound(Sfx.TYPE_MENU, Sfx.SELECT);
				close();
			}
		}
	}
	
	public void render() {
		if (!open) return;
		drawImage(bg, 250, 125);
		ok.render();
	}
}