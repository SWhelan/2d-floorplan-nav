package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import spark.Request;

public class UploadFileService {
	public static final String DEFAULT_IMAGE_LOCATION = "tmp/";
	
	public static final String DEFAULT_MULTIPART_WRITE_LOCATION = "src/main/resources/static/" + DEFAULT_IMAGE_LOCATION;
	public static final String MATLAB_PATH = "..\\main\\resources\\static\\tmp\\";
	
	private static final String MULTIPART_CONFIG_KEY = "org.eclipse.jetty.multipartConfig";
	private static final String DEFAULT_MULTIPART_READ_LOCATION = "/tmp";
	private static final String FILE_KEY = "file";

	
	public static String saveFileToDisk(Request rq) {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(DEFAULT_MULTIPART_READ_LOCATION);
		rq.raw().setAttribute(MULTIPART_CONFIG_KEY, multipartConfigElement);
		try {
			Part uploadedFile = rq.raw().getPart(FILE_KEY);
			byte[] bytes = getFileBytes(uploadedFile);
			String fileLocation = DEFAULT_MULTIPART_WRITE_LOCATION + uploadedFile.getSubmittedFileName();
			Files.write(Paths.get(fileLocation), bytes);
			return fileLocation;
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static byte[] getFileBytes(Part uploadedFile) throws IOException {
		InputStream inputStream = uploadedFile.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean done = false;
		while(!done) {
			int nextByte = inputStream.read();
			if (endOfInputStream(nextByte)) {
				done = true;
			} else {
				out.write(nextByte);
			}
		}
		return out.toByteArray();
	}
	
	private static boolean endOfInputStream(int value) {
		return value == -1;
	}	
}
