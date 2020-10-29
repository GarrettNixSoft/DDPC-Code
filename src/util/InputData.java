package util;

public class InputData {
	
	// data
	public boolean W, A, S, D;
	public boolean UP, DOWN, LEFT, RIGHT;
	public boolean SPACE;
	public boolean LEFT_CLICK;
	public LocationData locationData;
	
	// default
	public InputData() {}
	
	// overloaded
	public InputData(boolean w, boolean a, boolean s, boolean d, boolean u, boolean dwn, boolean l, boolean r, boolean sp, boolean lc) {
		W = w;
		A = a;
		S = s;
		D = d;
		UP = u;
		DOWN = dwn;
		LEFT = l;
		RIGHT = r;
		SPACE = sp;
		LEFT_CLICK = lc;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("0000000000");
		if (W)          result.setCharAt(0, '1');
		if (A)          result.setCharAt(1, '1');
		if (S)          result.setCharAt(2, '1');
		if (D)          result.setCharAt(3, '1');
		if (UP)         result.setCharAt(4, '1');
		if (DOWN)       result.setCharAt(5, '1');
		if (LEFT)       result.setCharAt(6, '1');
		if (RIGHT)      result.setCharAt(7, '1');
		if (SPACE)      result.setCharAt(8, '1');
		if (LEFT_CLICK) result.setCharAt(9, '1');
		return result.toString() + "-" + locationData.toString();
	}
	
}