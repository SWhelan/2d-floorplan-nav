package Pathfinding;
import java.util.*;
import Pathfinding.AdjacencyMatrix;

public class AStar {
	//contains location and other information used by A* search
	private class Node {
		private int x,y,z;
		public float cost, hValue, fValue;
		public Node parent;
		
		public Node (int[] point){
			this.x = point[0];
			this.y = point[1];
			this.z = point[2];
		}
		
		public Node (int[] point, float cost, Node parent){
			this.x = point[0];
			this.y = point[1];
			this.z = point[2];
			this.cost = cost;
			this.parent = parent;
		}
		
		//returns this Node's coordinates
		public int[] getLocation(){
			int[] r = {this.x, this.y, this.z};
			return r;
		}
		
		//checks this Node's coordinates against another set of coordinates
		public boolean isLocation(int[] loc){
			return loc[0] == this.x && loc[1] == this.y && loc[2] == this.z;
		}
	}
	
	//uses Pythagorian distance as a heuristic of distance between two points
	private static float pythagDist(int[] p1, int[] p2){
		float value = 0;
		for (int i=0; i<p1.length; i++){
			value += Math.pow(p1[i]-p2[i], 2);
		}
		return (float) Math.sqrt(value);
	}
	
	//takes two points of the form [x,y,z] and and AdjacencyMatrix, and returns a path between them
	public int[][] aStarSearch(int[] point1, int[] point2, AdjacencyMatrix matrix) {
		//comparator for priority queue
		class nodeComparator implements Comparator<Node>{
			@Override
			public int compare(Node o1, Node o2) {
				return Float.compare(o1.fValue, o2.fValue);
			}		
		}
		
		PriorityQueue<Node> openList = new PriorityQueue<Node>(1, new nodeComparator());
		LinkedList<Node> closedList = new LinkedList<Node>();
		Node start = new Node(point1, 0, null);
		start.hValue = pythagDist(point1, point2);
		openList.add(start);
		Node goal = new Node(point2);
		int[][] path = new int[0][0];
		
		while (!openList.isEmpty()){
			Node n = openList.poll();
			if (this.exploreNode(n, openList, closedList, goal, matrix)){
				path = createPath(goal, closedList).toArray(path);
				break;
			}
		}
		if (path == null){
			System.out.println("NO PATH FOUND");
		}
		return path;
	}
	
	//returns true if the Node is the goal, otherwise checks locations adjacent to the Node
	private boolean exploreNode(Node n, PriorityQueue<Node> openList, List<Node> closedList, Node goal, AdjacencyMatrix matrix){
		for (int[] adj: matrix.getAdjacencies(n.getLocation())){
			if (goal.isLocation(adj)){
				goal.parent = n;
				return true;
			} else {
				this.examineNeighbor(adj, n, goal, openList, closedList);
			}
		}
		openList.remove(n);
		closedList.add(n);
		return false;
	}
	
	//Checks a location, adding it to the open list if it's new, and adjusting its value in the open list if it's old but unexplored
	private void examineNeighbor(int[] location, Node n, Node goal, PriorityQueue<Node> openList, List<Node> closedList){
		boolean valid = true;
		//checked in closed list
		for (Node closedN: closedList){
			valid = valid && !closedN.isLocation(location);
		}
		//check open list
		if (valid){
			for (Node openN: openList){
				if (openN.isLocation(location)){
					valid = false;
					//if we've reached an old location by a faster means, update it
					if (openN.cost-1 > n.cost){
						openN.cost = n.cost+pythagDist(n.getLocation(), openN.getLocation());
						openN.parent = n;
					}
				}
			}
		}
		if (valid){
			Node newN = new Node(location, n.cost+pythagDist(n.getLocation(), location), n);
			newN.hValue = pythagDist(newN.getLocation(), goal.getLocation());
			newN.fValue = newN.cost + newN.hValue;
			openList.add(newN);
		}
	}
	
	//Returns a list of the points in the path start->finish, adjusting to 1-indexing for Matlab
	private List<int[]> createPath(Node goal, List<Node> closedList){
		ArrayList<int[]> path = new ArrayList<int[]>();
		Node n = goal;
		while (n != null){
			path.add(0, n.getLocation());
			n = n.parent;
		}
		for (int[] a: path){
			a[0] = a[0]+1;
			a[1] = a[1]+1;
			a[2] = a[2]+1;
		}
		return path;
	}
}