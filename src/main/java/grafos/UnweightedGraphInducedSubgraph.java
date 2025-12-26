package grafos;

import java.util.List;

public class UnweightedGraphInducedSubgraph<V> extends UnweightedGraph<V> {

	public UnweightedGraphInducedSubgraph() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphInducedSubgraph(int vertices, int[][] edges) {
		super(vertices, edges);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphInducedSubgraph(int vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphInducedSubgraph(List<V> vertices, List<Edges> vecinos) {
		super(vertices, vecinos);
		// TODO Auto-generated constructor stub
	}

	public UnweightedGraphInducedSubgraph(V[] verticesarray, int[][] edges) {
		super(verticesarray, edges);
		// TODO Auto-generated constructor stub
	}
	
	
	public  Graph<V> grafoInducido(Graph<V> grafo, int grado){
	
		for(int i=0;i<grafo.getSize();i++) {
			if(grafo.getDegree(i)<grado) {
				grafo.removeVertex(grafo.getVertex(i));
			}
		}
			
		return grafo;
	}
	
//	public void removerVertice(V v) {
//		
//		if(vertices.contains(v)) {
//			
//			int index= vertices.indexOf(v);
//			vertices.remove(v);
//			vecinos.remove(index);
//			  for (List<Edges> list : vecinos) {
//		          for (int i = 0; i < list.size();i++) {
//		            if (list.get(i).getV() == index) {
//		              list.remove(i);
//		              break;
//		            } else {
//		             continue;
//		            }
//		          }
//		        }
//			  
//		        for (List<Edges> list : vecinos) {
//		          for (int i = 0; i < list.size(); i++) {
//		            if (list.get(i).getU() > index) {
//		              list.get(i).setU(list.get(i).getU()-1);
//		            }
//		            if (list.get(i).v > index) {
//		            	 list.get(i).setV(list.get(i).getV()-1);
//		            }
//		          }
//		        }
//			}
//		
//		}
}
