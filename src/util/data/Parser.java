package util.data;

import errors.ParserException;

/*
 * This class is used to handle String parsing done in the loader classes.
 * Common parsing strategies are implemented here to reduce redundancy.
 */
public class Parser {
	
	public static int[] parseCoordinates(String data) throws ParserException {
		try {
			int[] coords = new int[2];
			String[] tokens = data.split(",");
			coords[0] = Integer.parseInt(tokens[0]);
			coords[1] = Integer.parseInt(tokens[1]);
			return coords;
		} catch (Exception e) {
			throw new ParserException("parsing failed with exception: " + e.getClass() + ", message: " + e.getMessage());
		}
	}
	
	public static int[] parseCoordinatesDouble(String data) throws ParserException {
		try {
			int[] coords = new int[4];
			String[] pair = data.split(":");
			String[] tokens1 = pair[0].split(",");
			String[] tokens2 = pair[1].split(",");
			coords[0] = Integer.parseInt(tokens1[0]);
			coords[1] = Integer.parseInt(tokens1[1]);
			coords[2] = Integer.parseInt(tokens2[0]);
			coords[3] = Integer.parseInt(tokens2[1]);
			return coords;
		} catch (Exception e) {
			throw new ParserException("parsing failed with exception: " + e.getClass() + ", message: " + e.getMessage());
		}
	}
	
}