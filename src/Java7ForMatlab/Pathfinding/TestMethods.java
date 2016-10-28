package Pathfinding;
import java.util.*;

public class TestMethods {
	public void HelloWorld(){
		System.out.println("Hello world");
	}
	
	public String GetString(){
		return "Hello world";
	}
	 
	public int GetInt(){
		return 123;
	}

	public String[] GetStringArray(){
		String[] array = {"this", "is", "an", "array"};
		return array;
	}
	
	public List<String> GetStringList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("this");
		list.add("is");
		list.add("a");
		list.add("list");
		return list;
	}
	
	public void PrintThings(Object[] matrix){
		Object[][][] m = (Object[][][])matrix;
		System.out.println("y dim");
		System.out.println(m.length);
		System.out.println("x dim");
		System.out.println(m[0].length);
		System.out.println("adjacencies");
		System.out.println(m[0][0].length);
		System.out.println("acoordinates");

		/*System.out.println("y dimension");
		System.out.println(matrix.length);
		System.out.println("x dimension");
		System.out.println(((Object[])matrix[0]).length);
		System.out.println("adjacencies");
		System.out.println(((Object[])((Object[])matrix[0])[0]).length);
		System.out.println("coordinates");
		System.out.println(((int[])((Object[])((Object[])matrix[0])[0])[0]).length);*/
	}
}
