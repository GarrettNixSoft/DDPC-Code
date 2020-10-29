package main;

import javax.swing.JOptionPane;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import gameState.GameStateManager;
import util.data.SaveData;

public class Platformer {
	
	public static void main(String[] args) {
		new Platformer();
	}
	
	public Platformer() {
		// init game window
		Render.beginSession();
		//PixelRandomizer.randomize();
		// load assets
		long start = System.nanoTime();
		try {
			Loader.loadAssets();
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder(e.toString());
		    for (StackTraceElement ste : e.getStackTrace()) {
		        sb.append("\n\tat ");
		        sb.append(ste);
		    }
		    String trace = sb.toString();
			JOptionPane.showMessageDialog(null, "Oops! Something went wrong while loading the game. Please contact the developer\nwith the information provided below so it can be reviewed:\n" + trace, "Error", JOptionPane.ERROR_MESSAGE);
		}
		long elapsed = System.nanoTime() - start;
		System.out.println("[Platformer] Assets loaded: " + (elapsed / 1000000) + "ms");
		// load preferences
		SaveData.load();
		// init gamestatemanager
		GameStateManager gsm = new GameStateManager();
		// game loop init
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		int frames = 0;
		//int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		// run game loop
		while (!Display.isCloseRequested()) {
			try {
				long now = System.nanoTime();
				delta += (now - lastTime) / nsPerTick;
				lastTime = now;
				// update the game
				while (delta >= 1) {
					//ticks++;
					gsm.update();
					//if (Display.wasResized()) {
					//	GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
					//}
					delta -= 1;
				}
				// render the game
				//int buffer = Render.createFrameBuffer();
				//Render.bindFrameBuffer(buffer);
				gsm.render();
				// post-process
				//Render.revertFrameBuffer();
				// render to screen
				frames++;
				// update the display with the new frame
				Display.update();
				// increment counter to next second
				if (System.currentTimeMillis() - lastTimer >= 1000) {
					lastTimer += 1000;
					gsm.frameRate(frames);
					frames = 0;
					//ticks = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringBuilder sb = new StringBuilder(e.toString());
			    for (StackTraceElement ste : e.getStackTrace()) {
			        sb.append("\n\tat ");
			        sb.append(ste);
			    }
			    String trace = sb.toString();
				JOptionPane.showMessageDialog(null, "Oops! Looks like something went wrong. Please contact the game's developer with the\ninformation provided below so it can be reviewed:\n" + trace, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// exit game on close
		cleanUp();
		System.exit(0);
	}
	
	private void cleanUp() {
		Render.cleanUp();
		AL.destroy();
		Display.destroy();
	}
	
}