package service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import containers.FoundPath;

public class MatlabService {

	private static final String FILENAMES_TXT_PATH = "src/matlab/filenames.txt";
	private static final String MATLAB_PATH = "\"E:\\matlab2016\\bin\\matlab.exe\"";
	private static final String[] COMMAND_ARGS = {MATLAB_PATH, "-nodisplay", "-nosplash", "-nodesktop", "-wait", "-r"};
	private static final String SCRIPT_PATH = "src\\matlab\\DotGrid";
	
	public static FoundPath getRoute(int pointAX, int pointAY, String fileA, int pointBX, int pointBY, String fileB, String[] filenames) {
		FoundPath path = new FoundPath();
		makeFilenamesFile(filenames);
		String[] command = new String[COMMAND_ARGS.length + 1];
		for (int i = 0; i < COMMAND_ARGS.length; i++) {
			command[i] = COMMAND_ARGS[i];
		}
		command[command.length - 1] = createMatlabRunCommand(pointAX, pointAY, fileA, pointBX, pointBY, fileB, filenames);
		executeCommand(command);
		return path;
	}

	private static String createMatlabRunCommand(int pointAX, int pointAY, String fileA, int pointBX, int pointBY, String fileB, String[] filenames) {
		StringBuilder builder = new StringBuilder();
		builder
		.append("run('")
		.append(SCRIPT_PATH)
		.append("(")
		.append(pointAX)
		.append(", ")
		.append(pointAY)
		.append(", ")
		.append(getMatlabIndex(fileA, filenames))
		.append(", ")
		.append(pointBX)
		.append(", ")
		.append(pointBY)
		.append(", ")
		.append(getMatlabIndex(fileB, filenames))
		.append(")")
		.append("');");
		return builder.toString();
	}
	
	private static int getMatlabIndex(String fileA, String[] filenames) {
		for(int i = 0; i < filenames.length; i++) {
			if(fileA.equals(filenames[i])) {
				return i + 1; // MATLAB indexing starts at 1
			}
		}
		return -1;
	}

	private static void makeFilenamesFile(String[] array) {
		StringBuilder builder = new StringBuilder();		
		for (String item : array) {
			builder
			.append(UploadFileService.MATLAB_PATH)
			.append(item)
			.append("\n");
		}
		if(array.length > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		Path path = Paths.get(FILENAMES_TXT_PATH);
		try {
			Files.write(path, builder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
