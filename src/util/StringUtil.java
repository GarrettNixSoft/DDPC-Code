package util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

public class StringUtil {
	
	public static int getOffset(String str, Font font) {
		FontMetrics metrics = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
		int len = SwingUtilities.computeStringWidth(metrics, str);
		return - len / 2;
	}
	
	public static int[][] getOffset(String[] str, Font font) {
		int[][] result = new int[str.length][2];
		FontMetrics metrics = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
		for (int i = 0; i < str.length; i++) {
			int width = SwingUtilities.computeStringWidth(metrics, str[i]);
			int height = 28;
			result[i][0] = -width / 2;
			result[i][1] = -str.length * height / 2 + height * i;
		}
		return result;
	}
	
}