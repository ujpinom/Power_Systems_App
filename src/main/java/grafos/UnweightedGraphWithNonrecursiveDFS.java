package grafos;

import java.util.ArrayList;
import java.util.List;

import genericos.GenericStack;
import grafos.UnweightedGraph.SerchTree;

public class UnweightedGraphWithNonrecursiveDFS<V> extends UnweightedGraph<V> {
	
	
	
	public UnweightedGraphWithNonrecursiveDFS() {
		
	}
	
	
	public UnweightedGraphWithNonrecursiveDFS(V [] verticesarray,int [][] edges) {
		super(verticesarray,edges);
	}
	
	public UnweightedGraphWithNonrecursiveDFS(List<V> vertices,List<Edges> vecinos) {
		super(vertices,vecinos);
	}
	

	public UnweightedGraphWithNonrecursiveDFS(int vertices,int [][] edges) {
		super(vertices,edges);
	}
	
	public UnweightedGraphWithNonrecursiveDFS(int vertices,List<Edges> vecinos) {
		super(vertices,vecinos);
	}
	
	  public List<List<Edges>> cloneEdges() {
		    List<List<Edges>> neigborCopy = new ArrayList<>();
		    
		    for (int i = 0; i < vecinos.size(); i++) {
		      List<Edges> edges = new ArrayList<>();
		      for (Edges e: vecinos.get(i)) {
		        edges.add(e);
		      }
		      neigborCopy.add(edges);
		    }
		    
		    return neigborCopy;
		  }

	
	@Override
	public UnweightedGraph<V>.SerchTree dfs(int index){
		
		
		 List<List<Edges>> neigborCopy=cloneEdges();
		
		
		int [] padre= new int [vertices.size()];
		ArrayList<Integer> orden= new ArrayList<>();
		for(int i=0;i<padre.length;i++) {
			padre[i]=-1;
		}
		boolean control [] = new boolean[vertices.size()];
		
		GenericStack<Integer> stack= new GenericStack<>();
		stack.addElemento(index);
		control[index]=true;
		
		while(!stack.isempty()) {
			
			int u= stack.remover();
			orden.add(u);
			
			
			
			for(int i=0;i<neigborCopy.get(u).size();i++) {
				Edges e= neigborCopy.get(u).get(i);
				if(!control[e.getV()]) {
					
					stack.addElemento(e.getV());
					padre[e.getV()]=u;
					control[e.getV()]=true;
			}
				
				
//				
				
			}
			
		}
		
		return new SerchTree(index,padre,orden);

	}

}
