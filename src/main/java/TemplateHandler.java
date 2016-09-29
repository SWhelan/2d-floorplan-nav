import static spark.Spark.get;

import spark.template.mustache.MustacheTemplateEngine;

public class TemplateHandler {

	private static final MustacheTemplateEngine ENGINE = new MustacheTemplateEngine();
	
	public static void registerRoutes() {
		get("/", (rq, rs) -> ApplicationHandler.renderHome(rq, rs), ENGINE);
	}

}
