package grafos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import genericos.GenericStack;

public class UnweightedGraphDetectCycle<V> extends UnweightedGraph<V> {

	
	
	public UnweightedGraphDetectCycle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphDetectCycle(int vertices, int[][] edges) {
		super(vertices, edges);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphDetectCycle(int vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphDetectCycle(List<V> vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphDetectCycle(V[] verticesarray, int[][] edges) {
		super(verticesarray, edges);
		// TODO Auto-generated constructor stub
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
	
	public boolean isCyclic() {
	
		List<Integer> indices= new ArrayList<>();
		for(int i=0;i<vertices.size();i++) {
			indices.add(i);
					
		}
		int [] padre= new int [vertices.size()];
		Arrays.fill(padre, -1);
		boolean control[] =new boolean[vertices.size()];
		int index= indices.get(0);
		while(indices.size()>0) {
			
			if(helper(index,padre,indices,control)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean helper(int index, int[] padre, List<Integer> indices,boolean [] control) {
	
		indices.remove(new Integer(index));
		control[index]=true;
		
		for(Edges e: vecinos.get(index)) {
			
			if(!control[e.getV()]) {
				padre[e.getV()]=index;
				helper(e.getV(),padre,indices,control);
				
			}
			
			else if(padre[index]!=e.getV()) {
				
				return true;
			}
		}
		
		return false;
		
	}
	
	public List<Integer> getACycle() {
		List<Integer> allVertices = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			allVertices.add(i);
		}

		List<List<Edges>> neighbors = cloneEdges();

		List<Integer> searchOrder = new ArrayList<>();
		int[] parent = new int[vertices.size()];
		for (int i = 0; i < parent.length; i++)
			parent[i] = -1; // Initialize parent[i] to -1

		// Mark visited vertices
		boolean[] isVisited = new boolean[vertices.size()];

		while (allVertices.size() > 0) {
			int v = allVertices.get(0);

			Stack<Integer> stack = new Stack<>();
			stack.push(v);
			searchOrder.add(v);
			allVertices.remove(new Integer(v));
			isVisited[v] = true; // Vertex x visited

			while (!stack.isEmpty()) {
				int x = stack.peek();
				if (neighbors.get(x).size() == 0) {
					stack.pop();
					continue;
				} else {
					// Find the next unvisited neighbor of x
					for (int i = neighbors.get(x).size() - 1; i >= 0; i--) {
						Edges e = neighbors.get(x).get(i);
						if (!isVisited[e.getV()]) {
							parent[e.getV()] = x; // The parent of vertex e.v is x
							stack.push(e.getV()); // Add a new neighbor to the stack
							isVisited[e.getV()] = true; // Vertex x visited
							searchOrder.add(e.getV());
							allVertices.remove(new Integer(e.getV()));
							neighbors.get(x).remove(i);
							break;
						} else if (e.getV() != parent[x]) {
							// A path is found
							List<Integer> list = new ArrayList<>();

							list.add(e.getV());
							while (x != e.getV() && x != -1) {
								list.add(x);
								x = parent[x];
							}

							return list;
						} else {
							neighbors.get(x).remove(i);
						}
					}
				}
			}
		}

		return null;
	}

}
