import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.MatlabService;
import service.UploadFileService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationController {
	
	private static final String FILENAME_KEY = "filename";
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
		String[] filenames = rq.queryParamsValues(FILENAME_KEY);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("filenames", filenames);
		attributes.put("route", UploadFileService.DEFAULT_IMAGE_LOCATION);
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
			
			MatlabService.getRoute(pointAX, pointAY, fileA, pointBX, pointBY, fileB);
			
			Map<String, Object> attributes = new HashMap<>();
			return new ModelAndView(attributes, ROUTE_TEMPLATE);	
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getFileNameOnly(String text) {
		return text.substring(text.lastIndexOf("/") + 1);
	}
}
