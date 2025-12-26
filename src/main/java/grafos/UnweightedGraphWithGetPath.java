package grafos;

import java.util.ArrayList;
import java.util.List;

public class UnweightedGraphWithGetPath<V> extends UnweightedGraph<V> {

	public UnweightedGraphWithGetPath() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphWithGetPath(int vertices, int[][] edges) {
		super(vertices, edges);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphWithGetPath(int vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphWithGetPath(List<V> vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphWithGetPath(V[] verticesarray, int[][] edges) {
		super(verticesarray, edges);
		// TODO Auto-generated constructor stub
	}
	

	public List<Integer> findPath(int u,int v){
		
		SerchTree busqueda= bfs(v);
		
		int [] padres= busqueda.padres();
		
		List<Integer> trayectoria= new ArrayList<>();
		
		do {
			trayectoria.add(u);
			u=padres[u];
		} while(u!=-1);
		
		
		if(trayectoria.size()==0) {
			return null;
		}
		else {
			return trayectoria;
		}
		
		
		
	}

}
