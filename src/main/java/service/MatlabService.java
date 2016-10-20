package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import containers.FoundPath;

public class MatlabService {

	public static FoundPath getRoute(int pointAX, int pointAY, String fileA, int pointBX, int pointBY, String fileB, String[] filenames) {
		FoundPath path = new FoundPath();
		StringBuilder command = new StringBuilder();
		String results = callMatlab("\"E:\\matlab2016\\bin\\matlab.exe\" -nodisplay -nosplash -nodesktop -wait -r \"run('C:\\Users\\Sarah Whelan\\Desktop\\test1.m');\"");
		return path;
	}
	
	private static String callMatlab(String command){
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
			return "Something went wrong";
		}
	}

}
