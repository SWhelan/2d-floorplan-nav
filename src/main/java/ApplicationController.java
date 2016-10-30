import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import containers.Directions;
import containers.Directions.Coordinate;
import containers.MustacheListItem;
import service.MatlabService;
import service.UploadFileService;
import service.Util;
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
		attributes.put("filenames", MustacheListItem.makeList(Arrays.asList(filenames)));
		attributes.put("route", UploadFileService.DEFAULT_IMAGE_LOCATION);
		rs.cookie("filenames", getFileNamesString(filenames));
		return new ModelAndView(attributes, ROUTE_TEMPLATE);
	}

	public static Directions getRoute(Request rq, Response rs) {
		String rawJSON = rq.body();
		ObjectMapper mapper = new ObjectMapper();		
		try {
			JsonNode node = mapper.readValue(rawJSON, JsonNode.class);
			JsonNode pointA = node.get("pointA");
			JsonNode pointB = node.get("pointB");
			JsonNode exclude = node.get("excludeList");
			
			Directions directions = new Directions();
			directions.setOriginalFileNames(getFileNames(rq.cookie("filenames")));
			
			List<Coordinate> exludePoints = new ArrayList<>();
			exclude.elements().forEachRemaining(e -> exludePoints.add(makeCoord(e, directions.getOriginalFileNames())));
			directions.setExludePoints(exludePoints);
			directions.setA(makeCoord(pointA, directions.getOriginalFileNames()), getFileNameOnly(pointA.get("filename").asText()));
			directions.setB(makeCoord(pointB, directions.getOriginalFileNames()), getFileNameOnly(pointB.get("filename").asText()));
			
			MatlabService.getRoute(directions);
			directions.preprocessForResponse();
			return directions;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Coordinate makeCoord(JsonNode node, List<String> possibleFileNames) {
		return new Coordinate(
				Util.getJavaIndex(getFileNameOnly(node.get("filename").asText()), possibleFileNames),
				node.get("xcoord").asInt(),
				node.get("ycoord").asInt()
				);		
	}

	private static List<String> getFileNames(String filenames) {
		return Arrays.asList(filenames.split(",")).stream().map(e -> e.trim()).collect(Collectors.toList());		
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
