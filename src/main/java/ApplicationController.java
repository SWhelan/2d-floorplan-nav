import java.util.HashMap;
import java.util.Map;

import service.UploadFileService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationController {

	public static ModelAndView renderHome(Request rq, Response rs) {
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, "main.mustache");
	}

	public static ModelAndView uploadImage(Request rq, Response rs) {
		UploadFileService.saveFileToDisk(rq);
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, "main.mustache");
	}
}
