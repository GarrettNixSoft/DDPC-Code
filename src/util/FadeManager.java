package util;

import static main.Render.drawRect;

import org.newdawn.slick.Color;

import gameState.GameStateManager;
import main.Render;


public class FadeManager {

	private static boolean in;
	private static double inPercent;
	private static long inTimer;
	private static int inTarget;
	private static boolean out;
	private static double outPercent;
	private static long outTimer;
	private static int outTarget;
	
	private static GameStateManager gsm;
	private static int nextState;
	private static boolean next;
	//private static boolean level;
	//private static boolean finish;
	//private static boolean win;
	private static boolean white = true;
	//private static long score;
	
	public static void initGSM(GameStateManager manager) {
		gsm = manager;
	}
	
	public static boolean isWhite() {
		return white;
	}
	
	public static void fadeIn() {
		in = true;
		inTimer = System.nanoTime();
		inTarget = 1000;
		white = false;
	}
	
	public static void fadeIn(float time) {
		in = true;
		inTimer = System.nanoTime();
		inTarget = (int) (time * 1000);
		white = false;
	}
	
	public static void fadeIn(float time, boolean isWhite) {
		in = true;
		inTimer = System.nanoTime();
		inTarget = (int) (time * 1000);
		white = isWhite;
	}
	
	public static void fadeOut() {
		out = true;
		outTimer = System.nanoTime();
		outTarget = 1000;
		white = true;
	}
	
	public static void fadeOut(float time) {
		out = true;
		outTimer = System.nanoTime();
		outTarget = (int) (time * 1000);
		white = true;
	}
	
	public static void fadeOut(float time, boolean isWhite) {
		out = true;
		outTimer = System.nanoTime();
		outTarget = (int) (time * 1000);
		white = isWhite;
	}
	
	public static void fadeOut(float time, int nextSt, boolean isWhite) {
		out = true;
		outTimer = System.nanoTime();
		outTarget = (int) (time * 1000);
		nextState = nextSt;
		next = true;
		white = isWhite;
	}
	
	public static boolean inProgress() {
		return in || out;
	}
	
	public static void update() {
		if (in) {
			long elapsed = (System.nanoTime() - inTimer) / 1000000;
			if (elapsed >= inTarget) {
				in = false;
			}
		}
		if (out) {
			long elapsed = (System.nanoTime() - outTimer) / 1000000;
			if (elapsed >= outTarget) {
				out = false;
				if (next) {
					next = false;
					gsm.setState(nextState);
				}
			}
		}
	}
	
	public static void render() {
		if (in) {
			long elapsed = (System.nanoTime() - inTimer) / 1000000;
			inPercent = (double) (inTarget - elapsed) / inTarget;
			Color c = null;
			if (white) c = new Color(255, 255, 255, (int) (255 * inPercent));
			else c = new Color(0, 0, 0, (int) (255 * inPercent));
			drawRect(0, 0, Render.WIDTH, Render.HEIGHT, c);
		}
		if (out) {
			long elapsed = (System.nanoTime() - outTimer) / 1000000;
			outPercent = (double) elapsed / outTarget;
			Color c = null;
			if (white) c = new Color(255, 255, 255, (int) (255 * outPercent));
			else c = new Color(0, 0, 0, (int) (255 * outPercent));
			drawRect(0, 0, Render.WIDTH, Render.HEIGHT, c);
		}
	}
	
}