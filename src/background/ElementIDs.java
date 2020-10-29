package background;

public class ElementIDs {
	
	// base IDs
	public static final String[] BASE_ID = {
			"s_default",
			"n_default",
			"y_default",
			"m_default",
			"noise"
	};
	
	// element IDs
	public static final String[] ELEMENT_ID = {
			"grid_fade",
			"grid_glitch",
			"error",
			"error_glitch"
	};
	
	public static String getBackgroundID(String level) {
		if (level.equals("s1_2")) return "glitch_standard";
		if (level.equals("test")) return "y_default";
		return null;
	}
	
}