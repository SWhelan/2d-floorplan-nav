package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import containers.Directions;

public class MatlabService {

	private static final String FILENAMES_TXT_PATH = "src/matlab/filenames.txt";
	private static final String MATLAB_PATH = "\"E:\\matlab2016\\bin\\matlab.exe\"";
	private static final String[] STANDARD_COMMAND = {MATLAB_PATH, "-nodisplay", "-nosplash", "-nodesktop", "-wait", "-r"};
	private static final String SCRIPT_PATH = "src\\matlab\\DotGrid";
	
	public static void getRoute(Directions directions) {
		makeFilenamesFile(directions.getOriginalFileNames());
		String customRunCommand = generateRunCommand(
				directions.getA().getX(), directions.getA().getY(), directions.getFileName(directions.getA().getIndex()), 
				directions.getB().getX(), directions.getB().getY(), directions.getFileName(directions.getA().getIndex()), 
				directions.getOriginalFileNames());
		List<String> command = Arrays.asList(STANDARD_COMMAND);
		command.add(customRunCommand);
		executeCommand(command);
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
	
	private static String executeCommand(List<String> commandParts){
		String[] command = commandParts.toArray(new String[commandParts.size()]);
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

}
