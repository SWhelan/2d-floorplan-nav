import spark.Spark;

public class Main {
	
    public static void main(String[] args) {
    	Spark.staticFileLocation("/static");
    	Routes.registerAll();
    }
    
}

