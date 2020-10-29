package util.data;

public class Converter {
	
	// COORDINATE CONVERSIONS
	public static int[] tile(int[] coords) {
		for (int i = 0; i < coords.length; i++) {
			coords[i] *= 60;
		}
		return coords;
	}
	
	public static int[] tileCenter(int[] coords) {
		for (int i = 0; i < coords.length; i++) {
			coords[i] = coords[i] * 60 + 30;
		}
		return coords;
	}
	
	public static int[] tileCenterX(int[] coords) {
		System.out.print("[Converter] Converting coords: ");
		for (int i = 0; i < coords.length; i++) {
			System.out.print(coords[i] + ", ");
			if (i % 2 == 0) coords[i] = coords[i] * 60 + 30;
			else coords[i] *= 60;
		}
		System.out.println();
		System.out.print("[Converter] Converted to: ");
		for (int i = 0; i < coords.length; i++) System.out.print(coords[i] + ", ");
		System.out.println();
		return coords;
	}
	
	public static int[] tilePlus(int[] coords) {
		for (int i = 0; i < coords.length; i++) {
			coords[i] = coords[i] * 60 + 60;
		}
		return coords;
	}
	
}