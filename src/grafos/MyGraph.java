package grafos;

import java.util.ArrayList;
import java.util.List;

import grafos.Edges;
import grafos.UnweightedGraph;
import grafos.UnweightedGraph.SerchTree;

public class MyGraph<V> extends UnweightedGraph<V> {

	public MyGraph() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyGraph(int vertices, int[][] edges) {
		super(vertices, edges);
		// TODO Auto-generated constructor stub
	}

	public MyGraph(int vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public MyGraph(List<V> vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public MyGraph(V[] verticesarray, int[][] edges) {
		super(verticesarray, edges);
		// TODO Auto-generated constructor stub
	}
	

	  public List<List<Integer>> getConnectedComponents() {
		    List<List<Integer>> list = new ArrayList<>();

		    List<Integer> vertexIndices = new ArrayList<>();
		    for (int i = 0; i < vertices.size(); i++)
		      vertexIndices.add(i);

		    while (vertexIndices.size() > 0) {
		      SerchTree tree = dfs(vertexIndices.get(0));
		      list.add(tree.getSerchOrder());
		      vertexIndices.removeAll(tree.getSerchOrder());
		    }
		    
		    for(int i=0;i<list.size();i++) {
		    	
		    	modificarLista(list.get(i));
		    	
		    }

		    return list;
		  }
		  
		  public void modificarLista(List<Integer> list) {
			 
			  for(int j=0;j<list.size();j++) {
		    		
				  list.set(j, list.get(j)+1);
				 
		    	}
		  }

}
