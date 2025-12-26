package genericos;
import java.util.*;
public class otros {
	
	private static ArrayList<Object> hola= new ArrayList<>();
	private static ArrayList<int [][]> soluciones = new ArrayList<>();
	
	public static void main (String[] args) {
	
	int [][] h1= {{1,2,4},{5,6,7},{8,9,10}};
	
	int [][] h2= {{1,2,4},{5,6,7},{8,9,10}};
	
	
	soluciones.add(h2);
	soluciones.add(h1);
	
 Object [] sol=soluciones.toArray();
 
 System.out.println(Arrays.deepEquals(h1, h2));
	
	
	}
	
	
}
