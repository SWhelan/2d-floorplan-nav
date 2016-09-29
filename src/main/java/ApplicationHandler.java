import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationHandler {

	public static ModelAndView renderHome(Request rq, Response rs) {
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, "main.mustache");
	}	

}
