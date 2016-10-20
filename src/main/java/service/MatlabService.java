package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import containers.FoundPath;

public class MatlabService {

	private static final String MATLAB_PATH = "\"E:\\matlab2016\\bin\\matlab.exe\"";
	private static final String COMMAND_ARGS = " -nodisplay -nosplash -nodesktop -wait -r ";
	private static final String SCRIPT_PATH = "src\\matlab\\DotGrid";
	
	public static FoundPath getRoute(int pointAX, int pointAY, String fileA, int pointBX, int pointBY, String fileB, String[] filenames) {
		FoundPath path = new FoundPath();
		StringBuilder command = new StringBuilder();
		command.append(MATLAB_PATH)
		.append(COMMAND_ARGS)
		.append("\"run('")
		.append(SCRIPT_PATH)
		.append("');\"");
		callMatlab(command.toString());
		return path;
	}
	
	private static void callMatlab(String command){
		Runtime rt = Runtime.getRuntime();
		try {
			Process pr = rt.exec(command);
			InputStream inputStream = pr.getInputStream();
			List<Byte> results = new ArrayList<>();
			int next = -1;
			while((next = inputStream.read())  != -1){
			        results.add((byte)next);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
