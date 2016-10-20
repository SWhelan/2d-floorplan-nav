import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import containers.FoundPath;
import service.MatlabService;
import service.UploadFileService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationController {
	
	private static final String FILENAMES_KEY = "filename";
	private static final String LANDNG_PAGE_TEMPLATE = "uploadImages.mustache";
	private static final String ROUTE_TEMPLATE = "displayRoute.mustache";

	public static ModelAndView renderHome(Request rq, Response rs) {
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, LANDNG_PAGE_TEMPLATE);
	}

	public static ModelAndView uploadImage(Request rq, Response rs) {
		UploadFileService.saveFileToDisk(rq);
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, LANDNG_PAGE_TEMPLATE);
	}

	public static ModelAndView uploadImageOrdering(Request rq, Response rs) {
		String[] filenames = rq.queryParamsValues(FILENAMES_KEY);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("filenames", filenames);
		attributes.put("route", UploadFileService.DEFAULT_IMAGE_LOCATION);
		rs.cookie("filenames", getFileNamesString(filenames));
		return new ModelAndView(attributes, ROUTE_TEMPLATE);
	}

	public static ModelAndView getRoute(Request rq, Response rs) {
		String rawJSON = rq.body();
		ObjectMapper mapper = new ObjectMapper();		
		try {
			JsonNode node = mapper.readValue(rawJSON, JsonNode.class);
			JsonNode pointA = node.get("pointA");
			JsonNode pointB = node.get("pointB");
			int pointAX = pointA.get("xcoord").asInt();
			int pointAY = pointA.get("ycoord").asInt();
			String fileA = getFileNameOnly(pointA.get("filename").asText());
			int pointBX = pointB.get("xcoord").asInt();
			int pointBY = pointB.get("ycoord").asInt();
			String fileB = getFileNameOnly(pointB.get("filename").asText());
			String[] filenames = getFileNamesArray(rq.cookie("filenames"));
			
			FoundPath path = MatlabService.getRoute(pointAX, pointAY, fileA, pointBX, pointBY, fileB, filenames);
			
			Map<String, Object> attributes = new HashMap<>();
			return new ModelAndView(attributes, ROUTE_TEMPLATE);	
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String[] getFileNamesArray(String filenames) {
		String[] temp = filenames.split(",");
		for(int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].trim();
		}
		return temp;
	}

	private static String getFileNameOnly(String text) {
		return text.substring(text.lastIndexOf("/") + 1);
	}
	
	private static String getFileNamesString(String[] filenames) {
		StringBuilder builder = new StringBuilder();
		for(String file : filenames) {
			builder.append(file);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}
