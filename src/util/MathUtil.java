package util;

import java.awt.Point;
import java.awt.Rectangle;

public class MathUtil {
	
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
	}
	
	public static float distance(Point p1, Point p2) {
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		float xDelta = (float) Math.abs(x1 - x2);
		float yDelta = (float) Math.abs(y1 - y2);
		return (float) Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
	}
	
	public static int randInt(int max) {
		return (int) (Math.random() * max);
	}
	
	public static int randInt(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}
	
	public static float randFloat(double min, double max) {
		return (float) ((Math.random() * (max - min)) + min);
	}
	
	public static boolean randBool() {
		return Math.random() < 0.5;
	}
	
	public static float[] center(Rectangle r) {
		float[] result = new float[2];
		result[0] = (float) (r.getX() + (r.getWidth() / 2.0));
		result[1] = (float) (r.getY() + (r.getHeight() / 2.0));
		return result;
	}
	
}