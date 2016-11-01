package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import containers.Directions;
import containers.Directions.Coordinate;

public class MatlabService {

	private static final String FILENAMES_TXT_PATH = "src/matlab/filenames.txt";
	private static final String EXCLUDE_POINTS_TXT_PATH = "src/matlab/exclude_points.txt";
	private static final String PATH_TXT_PATH = "src/matlab/path.txt";	
	private static final String MATLAB_CONFIG_PATH = "src/matlab/matlabinstallpath.txt";
	private static final String[] STANDARD_COMMAND = {"-nodisplay", "-nosplash", "-nodesktop", "-wait", "-r"};
	private static final String SCRIPT_PATH = "src\\matlab\\DotGrid";
	
	public static void getRoute(Directions directions) {
		makeFilenamesFile(directions.getOriginalFileNames());
		makeExcludePointsFile(directions.getExludePoints());
		String customRunCommand = generateRunCommand(
				directions.getA().getX(), directions.getA().getY(), directions.getFileName(directions.getA().getIndex()), 
				directions.getB().getX(), directions.getB().getY(), directions.getFileName(directions.getA().getIndex()), 
				directions.getOriginalFileNames());
		String[] command = new String[STANDARD_COMMAND.length + 2];
		command[0] = getMatlabLocation();
		for (int i = 0; i < STANDARD_COMMAND.length; i++) {
			command[i + 1] = STANDARD_COMMAND[i];
		}
		command[command.length - 1] = customRunCommand;
		executeCommand(command);
		directions.setSteps(Util.readFile(PATH_TXT_PATH));
	}

	private static String generateRunCommand(int pointAX, int pointAY, String fileA, int pointBX, int pointBY, String fileB, List<String> filenames) {
		StringBuilder builder = new StringBuilder();
		builder
		.append("run('")
		.append(SCRIPT_PATH)
		.append("(")
		.append(pointAX)
		.append(", ")
		.append(pointAY)
		.append(", ")
		.append(Util.getMatlabIndex(fileA, filenames))
		.append(", ")
		.append(pointBX)
		.append(", ")
		.append(pointBY)
		.append(", ")
		.append(Util.getMatlabIndex(fileB, filenames))
		.append(")")
		.append("');");
		return builder.toString();
	}

	private static void makeFilenamesFile(List<String> filenames) {
		StringBuilder builder = new StringBuilder();		
		for (String item : filenames) {
			builder
			.append(UploadFileService.MATLAB_PATH)
			.append(item)
			.append("\n");
		}
		if(filenames.size() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		Util.writeFile(FILENAMES_TXT_PATH, builder.toString());
	}
	
	private static void makeExcludePointsFile(List<Coordinate> excludePoints) {
		StringBuilder builder = new StringBuilder();		
		for (Coordinate point : excludePoints) {
			builder
			.append(point.index + 1) // Matlab indexing starts at 1
			.append(",")
			.append(Integer.toString(point.x))
			.append(",")
			.append(Integer.toString(point.y))			
			.append("\n");
		}
		if(excludePoints.size() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		Util.writeFile(EXCLUDE_POINTS_TXT_PATH, builder.toString());
	}
	
	private static String executeCommand(String[] command){
		Runtime rt = Runtime.getRuntime();
		try {
			Process pr = rt.exec(command);
			InputStream inputStream = pr.getInputStream();
			List<Byte> results = new ArrayList<>();
			int next = -1;
			while((next = inputStream.read())  != -1){
			        results.add((byte)next);
			}
			byte[] array = new byte[results.size()];
			for (int i = 0; i < results.size(); i++) {
			        array[i] = results.get(i);
			}
			String result = new String(array);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private static String getMatlabLocation() {
		List<String> data = Util.readFile(MATLAB_CONFIG_PATH);
		if (data.isEmpty()) {
			return "";
		}
		return data.get(0);
	}

}
