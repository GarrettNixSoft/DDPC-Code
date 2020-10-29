package util.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileUtil {
	
	public static ArrayList<String> getFileData(String path) {
		path = path.replace("\\", "/");
		System.out.println("[FileUtil] Loading file: " + path);
		ArrayList<String> data = new ArrayList<String>();
		try {
			//InputStream in = new FileInputStream(path);
			InputStream in = FileUtil.class.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				data.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}
	
	public static void writeFile(String path, String name, String extension, ArrayList<String> data) {
		try {
			File target = new File(path + "\\" + name + "." + extension);
			FileWriter fw = new FileWriter(target);
			BufferedWriter writer = new BufferedWriter(fw);
			for (String line : data) {
				writer.write(line);
				writer.write(System.lineSeparator());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}