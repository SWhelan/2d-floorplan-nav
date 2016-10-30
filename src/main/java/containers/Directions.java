package containers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Directions {
	
	public static class Coordinate {
		public int index;
		public int x;
		public int y;
		
		public Coordinate(int index, int x, int y) {
			super();
			this.index = index;
			this.x = x;
			this.y = y;
		}

		public int getIndex() {
			return index;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
	}
	
	private Coordinate A;
	private String fileA;
	private Coordinate B;
	private String fileB;
	private List<Coordinate> exludePoints;
	private List<String> originalFileNames;
	private List<String> afterPathFileNames;
	private List<String> steps;
	private List<String> prettySteps;
	
	public Directions() {
		// No argument constructor for gson
	}

	public Coordinate getA() {
		return A;
	}

	public void setA(Coordinate a, String filename) {
		fileB = filename;
		A = a;
	}

	public Coordinate getB() {
		return B;
	}

	public void setB(Coordinate b, String filename) {
		fileB = filename;
		B = b;
	}

	public List<Coordinate> getExludePoints() {
		return exludePoints;
	}

	public void setExludePoints(List<Coordinate> exludePoints) {
		this.exludePoints = exludePoints;
	}

	public List<String> getOriginalFileNames() {
		return originalFileNames;
	}

	public void setOriginalFileNames(List<String> originalFileNames) {
		this.originalFileNames = originalFileNames;
	}

	public List<String> getAfterPathFileNames() {
		return afterPathFileNames;
	}

	public void setAfterPathFileNames(List<String> afterPathFileNames) {
		this.afterPathFileNames = afterPathFileNames;
	}

	public List<String> getSteps() {
		return steps;
	}

	public void setSteps(List<String> steps) {
		this.steps = steps;
	}

	public String getFileName(int index) {
		return originalFileNames.get(index);
	}

	public String getFileA() {
		return fileA;
	}

	public String getFileB() {
		return fileB;
	}
	
	public void preprocessForResponse() {
		this.generatePrettySteps(this.generateCoords());
	}
	
	private List<Coordinate> generateCoords() {
		return this.steps.stream()
				.map(string -> string.split(","))
				.map(array -> new Coordinate(0, Integer.parseInt(array[0]), Integer.parseInt(array[1])))
				.collect(Collectors.toList());
	}
	
	private void generatePrettySteps(List<Coordinate> coords) {
		this.prettySteps = new ArrayList<>();
		prettySteps.add("Start");
		while(coords.size() > 1) {
			prettySteps.add(generatePrettyStep(coords.remove(0), coords.remove(0)));
		}
		prettySteps.add("End");
	}

	private String generatePrettyStep(Coordinate coord1, Coordinate coord2) {
		return "Go to " + coord2.getX() + "x and " + coord2.getY() + "y.";
	}
}
