package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import containers.Directions.Coordinate;

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
	
	public static String getFileNameOnly(String text) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		return text.substring(text.lastIndexOf("/") + 1);
	}

	public static String removeFileExtension(String filename) {
		if (filename == null || filename.isEmpty()) {
			return "";
		}
		return filename.substring(0, filename.lastIndexOf("."));
	}
	
	public static void deleteOldPathImages(List<String> originalFileNames) {
		originalFileNames.stream()
		.map(e -> Util.getFileNameOnly(e))
		.forEach(filename -> {
		String pathFileName = getPathImageFileName(filename);
			try {
				String temp = UploadFileService.DEFAULT_MULTIPART_WRITE_LOCATION + pathFileName;
				Files.deleteIfExists(Paths.get(temp));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static String getPathImageFileName(String filename) {
		String base = filename.substring(0, filename.lastIndexOf("."));
		return base + "_path.jpg";
	}

	public static Coordinate makeCoord(JsonNode node, List<String> possibleFileNames) {
		return new Coordinate(
				Util.getJavaIndex(Util.getFileNameOnly(node.get("filename").asText()), possibleFileNames),
				node.get("xcoord").asInt(),
				node.get("ycoord").asInt()
				);		
	}

	public static List<String> getFileNames(String filenames) {
		return Arrays.asList(filenames.split(",")).stream().map(e -> e.trim()).collect(Collectors.toList());		
	}
	
	public static String getFileNamesString(String[] filenames) {
		StringBuilder builder = new StringBuilder();
		for(String file : filenames) {
			builder.append(file);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}
