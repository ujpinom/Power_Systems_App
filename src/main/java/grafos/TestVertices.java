package grafos;
import java.util.*;
public class TestVertices {

	public static void main(String[] args) {
		
		String[] cities= {"Seattle", "San Francisco", "Los Angeles","Denver", "Kansas City", "Chicago", "Boston", "New York","Atlanta", "Miami", "Dallas", "Houston"};
	 
		int [][] edges= { {0, 3}, {0, 5},{1, 0}, {1, 2}, {1, 3},{2, 1}, {2, 3}, {2, 4}, {2, 10},
				{3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5},{4, 2}, {4, 3}, {4, 5}, {4, 7}, {4, 8}, {4, 10},
				{5, 0}, {5, 3}, {5, 4}, {5, 6}, {5, 7},{6, 5}, {6, 7},{7, 4}, {7, 5}, {7, 6}, {7, 8},{8, 4}, {8, 7}, {8, 9}, {8, 10}, {8, 11},
				{9, 8}, {9, 11},{10, 2}, {10, 4}, {10, 8}, {10, 11},{11, 8}, {11, 9}, {11, 10},{0, 1}};
		
		
		
		Graph<String> ciudades= new UnweightedGraph<String>(cities,edges);
		
		System.out.println(ciudades.getIndex("Seattle"));
		
		System.out.println("El numero de vertices del grafo es: "+ciudades.getSize());
		System.out.println("El vertice en el index 2 es:"+ ciudades.getVertex(2));
		System.out.println("El indice para Kansas City es: "+ ciudades.getIndex("Kansas City"));
		System.out.println("El grapho de las ciudades es: ");
		ciudades.printEdges();
		
		ciudades.removeVertex("Seattle");
		System.out.println(ciudades.getSize());
		ciudades.printEdges();
//		System.out.println();
//		UnweightedGraph<String>.SerchTree bfs= ciudades.bfs(ciudades.getIndex("Chicago"));
//		List<Integer> searchOrders = bfs.getSerchOrder();
//		System.out.println(bfs.getNumberOfVerticesFound() +" vertices are searched in this BFS order:");
//		for (int i = 0; i < searchOrders.size(); i++)System.out.print(ciudades.getVertex(searchOrders.get(i)) + " ");
//		System.out.println();
		
		
		
		
//		Graph<String> ciudades1= new  UnweightedGraphWithNonrecursiveDFS<String>(cities,edges);
//		
//		
//		UnweightedGraph<String>.SerchTree bfss=ciudades1.dfs(ciudades1.getIndex("Chicago"));
//			
//		List<Integer> searchOrderss = bfss.getSerchOrder();
//		 
//		System.out.println(bfss.getNumberOfVerticesFound() +" vertices are searched in this DFS 2.0 order:");
//		for (int i = 0; i < searchOrderss.size(); i++)System.out.print(ciudades1.getVertex(searchOrderss.get(i)) + " ");
//		System.out.println();
		
		
		
		
//		System.out.println("Trayectoria desde Seattle hasta Chicago");
//		System.out.println(ciudades.getIndex("Seattle"));
//		int [] perro= bfs.padres();
//		for(int i=0;i<perro.length;i++) {
//			System.out.print(perro[i]+" ");
//		}
//		System.out.println(ciudades.getVertex(bfs.getParent(0)));
//		bfs.getPath(ciudades.getIndex("Seattle"));
		
	}

}
