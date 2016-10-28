package Pathfinding;
import java.util.*;

public class AdjacencyMatrix {
	//List of floors -> list of x coordinates -> list of y coordinates -> list of adjacent point coordinates
	private List<List<List<List<int[]>>>> matrix;
	
	//creates a list of empty floors representing the building
	public AdjacencyMatrix(int floors){
		matrix = new ArrayList<List<List<List<int[]>>>>();
	}
	
	//creates a list of empty connections for each point on a floor of specified size
	public void createFloor(int rows, int columns){
		ArrayList<List<List<int[]>>> floor = new ArrayList<List<List<int[]>>>();
		for (int x=0; x<columns; x++){
			floor.add(new ArrayList<List<int[]>>());
			for (int y=0; y<rows; y++){
				floor.get(x).add(new ArrayList<int[]>());
			}
		}
		matrix.add(floor);
	}
	
	//Adds connection between two points assuming the point format is [x,y,z]
	public void addAdjacency(int[] point1, int[] point2){
		this.matrix.get(point1[2]).get(point1[0]).get(point1[1]).add(point2);
		this.matrix.get(point2[2]).get(point2[0]).get(point2[1]).add(point1);
	}
	
	
	//Returns list of adjacent coordinates (List<[x,y,z]>) assuming point format is [x,y,z]
	public List<int[]> getAdjacencies(int[] point){
		return this.matrix.get(point[2]).get(point[0]).get(point[1]);
	}
	
	public int getFloors(){
		return matrix.size();
	}
}
