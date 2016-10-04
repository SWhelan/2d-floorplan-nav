import static spark.Spark.get;
import static spark.Spark.post;

import spark.template.mustache.MustacheTemplateEngine;

public class Routes {

	private static final MustacheTemplateEngine ENGINE = new MustacheTemplateEngine();
	
	public static void registerAll() {
		get("/", (rq, rs) -> ApplicationController.renderHome(rq, rs), ENGINE);
		post("/uploadImage", (rq, rs) -> ApplicationController.uploadImage(rq, rs), ENGINE);
	}

}