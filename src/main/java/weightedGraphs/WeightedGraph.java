package weightedGraphs;

import java.util.ArrayList;
import java.util.List;

import grafos.Edges;
import grafos.UnweightedGraph;

public class WeightedGraph<V> extends UnweightedGraph<V> {

	public WeightedGraph() {
		super();
	}

	public WeightedGraph(int numerovertices, int[][] edges) {
		
		List<V> vertices= new ArrayList<>();
		
		
		for(int i=0;i<numerovertices;i++) {
			vertices.add((V)(new Integer(i)));
		}
		
		createWeightedGraph(vertices, edges);
	
	}

	public WeightedGraph(int numerovertices, List<WeightEdeges> vecinos) {
		
		List<V> vertices= new ArrayList<>();
		
		for(int i=0;i<numerovertices;i++) {
			vertices.add((V)(new Integer(i)));
		}
		
		createWeightedGraph(vertices, vecinos);
		
	}

	public WeightedGraph(List<V> vertices, List<WeightEdeges> vecinos) {
		
		
		createWeightedGraph(vertices, vecinos );
	}

	public WeightedGraph(V[] verticesarray, int[][] edges) {
		List<V> vertices= new ArrayList<>();
		
		for(int i=0;i<verticesarray.length;i++) {
			vertices.add(verticesarray[i]);
			
		}
		
		createWeightedGraph(vertices, edges);
		
	}
	
	public void createWeightedGraph(List<V> vertices, int [][]edges) {
		this.vertices=vertices;
		for(int i=0;i<vertices.size();i++) {
			vecinos.add(new ArrayList<Edges>());
		}
		
		for(int i=0;i<edges.length;i++) {
			vecinos.get(edges[i][0]).add(new WeightEdeges(edges[i][0],edges[i][1],edges[i][2]));
		}
	
	}
	public void createWeightedGraph(List<V> vertices, List<WeightEdeges> edges) {
		this.vertices=vertices;
		
		for(int i=0;i<vertices.size();i++) {
			vecinos.add(new ArrayList<Edges>());
		}
	
		for(WeightEdeges e: edges) {
			
			vecinos.get(e.getU()).add(e);
			
		}

	}


	
	public double getWeight(int u, int v) throws Exception{
		
		List<Edges> bordes= vecinos.get(u);
		
		for(int i=0;i<bordes.size();i++) {
			
			if(bordes.get(i).getV()==v) {
				return ((WeightEdeges)(bordes.get(i))).getWeight();
			}
		}
		
		throw new Exception("La conexión entre los nodos especificados no existe!");
	
	}
	
	
	public void addEdges(int u,int v,double weight) {
		vecinos.get(u).add(crearEdge(u,v,weight));
	}
	
	
	public WeightEdeges crearEdge(int u,int v,double weight) {
	
		if(u<0|| u>getSize()-1) {
			throw new IllegalArgumentException("No existe tal indice"+u);
		}
		if(v<0|| v>getSize()-1)
			throw new IllegalArgumentException("No existe tal indice"+v);
		
		return new WeightEdeges(u,v,weight);
		
	}
	
	public void printWeightEdges() {
		System.out.println("h");
		for(int i=0;i<vertices.size();i++) {
			System.out.print("Vertice "+ getVertex(i)+ " (" + i + "): ");
			for(Edges  e: vecinos.get(i)) {
				System.out.print("(" + getVertex(((WeightEdeges)e).getU()) + ", " + getVertex(((WeightEdeges)e).getV()) + ", " +((WeightEdeges)e).getWeight()+ ") ");
			}
			System.out.println();
		}
		
	}

}
