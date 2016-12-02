import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class TestController {
	
	private static final String TESTING_PAGE_TEMPLATE = "test.mustache";

	public static ModelAndView renderTests(Request rq, Response rs) {
		Map<String, Object> attributes = new HashMap<>();
		return new ModelAndView(attributes, TESTING_PAGE_TEMPLATE);
	}
}
