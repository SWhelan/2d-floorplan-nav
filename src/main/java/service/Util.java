package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {
	public static int getMatlabIndex(String string, List<String> list) {
		return getIndex(string, list, true);
	}
	
	public static int getJavaIndex(String string, List<String> list) {
		return getIndex(string, list, false);
	}
	
	private static int getIndex(String string, List<String> list, boolean matlabIndex) {
		for(int i = 0; i < list.size(); i++) {
			if(string.equals(list.get(i))) {
				if(matlabIndex) {
					return i + 1; // MATLAB indexing starts at 1
				} else {
					return i;
				}
			}
		}
		return -1;
	}

	public static void writeFile(String path, String data) {
		try {
			Files.write(Paths.get(path), data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readFile(String path) {
		try {
			return Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
