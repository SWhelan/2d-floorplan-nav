package containers;

import java.util.ArrayList;
import java.util.List;

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
		List<Coordinate> temp = new ArrayList<>(); 
				this.steps.stream()
				.map(string -> string.split(","))
				.forEach(array -> {
					try {
						temp.add(new Coordinate(Integer.parseInt(array[2]), Integer.parseInt(array[0]), Integer.parseInt(array[1])));
					} catch (Exception e) {
						// The point was malformed this can happen for the last point printed by matlab
						// is often only one number. If this happens just skip this coordinate.
						// The correct thing will still be printed on the map but we can ignore it for
						// our text directions.
					}
				});
		return temp;
	}
	
	private void generatePrettySteps(List<Coordinate> coords) {
		this.prettySteps = new ArrayList<>();
		prettySteps.add("Assuming that north is up");
		prettySteps.add("Start");
		while(coords.size() > 1) {
			prettySteps.add(generatePrettyStep(coords.remove(0), coords.remove(0), coords.remove(0), coords.get(0)));
		}
		prettySteps.add("End");
	}

	private String generatePrettyStep(Coordinate coord1, Coordinate coord2, Coordinate coord3, Coordinate coord4) {
		if(coord1.getX()>coord4.getX() && coord1.getY()==coord4.getY()){
			return "Go east 1 unit";
		}
		if(coord1.getX()<coord4.getX() && coord1.getY()==coord4.getY()){
			return "Go west 1 unit";
		}
		if(coord1.getX()==coord4.getX() && coord1.getY()>coord4.getY()){
			return "Go north 1 unit";
		}
		if(coord1.getX()==coord4.getX() && coord1.getY()<coord4.getY()){
			return "Go south 1 unit";
		}
		if(coord1.getX()>coord4.getX() && coord1.getY()<coord4.getY()){
			return "Go southeast 1 unit";
		}
		if(coord1.getX()>coord4.getX() && coord1.getY()>coord4.getY()){
			return "Go northeast 1 unit";
		}
		if(coord1.getX()<coord4.getX() && coord1.getY()<coord4.getY()){
			return "Go southwest 1 unit";
		}
		if(coord1.getX()<coord4.getX() && coord1.getY()>coord4.getY()){
			return "Go northwest 1 unit";
		}
		else{
			return "mistakes were made";
		}
		//return "Go to " + coord1.getX() + "x and " + coord1.getY() + "y.";
	}
}
