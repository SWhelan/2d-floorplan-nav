import static spark.Spark.get;
import static spark.Spark.post;

import spark.template.mustache.MustacheTemplateEngine;

public class Routes {

	private static final MustacheTemplateEngine MUSTACHE_RESPONSE = new MustacheTemplateEngine();
	private static final JsonTransformer JSON_RESPONSE = new JsonTransformer();
	
	public static void registerAll() {
		get("/", (rq, rs) -> ApplicationController.renderHome(rq, rs), MUSTACHE_RESPONSE);
		post("/uploadImage", (rq, rs) -> ApplicationController.uploadImage(rq, rs), MUSTACHE_RESPONSE);
		post("/processImageOrder", (rq, rs) -> ApplicationController.uploadImageOrdering(rq, rs), MUSTACHE_RESPONSE);
		post("/uploadPoints", (rq, rs) -> ApplicationController.getRoute(rq, rs), JSON_RESPONSE);
	}

}
