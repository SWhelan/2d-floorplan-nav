import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		rs.cookie("filenames", "");
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
		rs.cookie("filenames", Util.getFileNamesString(filenames));
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
			directions.setOriginalFileNames(Util.getFileNames(rq.cookie("filenames")));
			
			List<Coordinate> exludePoints = new ArrayList<>();
			exclude.elements().forEachRemaining(e -> exludePoints.add(Util.makeCoord(e, directions.getOriginalFileNames())));
			directions.setExludePoints(exludePoints);
			directions.setA(Util.makeCoord(pointA, directions.getOriginalFileNames()), Util.getFileNameOnly(pointA.get("filename").asText()));
			directions.setB(Util.makeCoord(pointB, directions.getOriginalFileNames()), Util.getFileNameOnly(pointB.get("filename").asText()));
			
			Util.deleteOldPathImages(directions.getOriginalFileNames());
			
			MatlabService.getRoute(directions);
			directions.preprocessForResponse();
			return directions;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
