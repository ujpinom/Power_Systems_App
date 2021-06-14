package grafos;
import java.util.*;

import bts.BinarySerchTree;

public class UnweightedGraph<V> implements Graph<V> {
	
	protected List<V> vertices= new ArrayList<V>();
	protected List<List<Edges>> vecinos=new ArrayList<>();
	
	public UnweightedGraph() {
		
	}
	
	public UnweightedGraph(V [] verticesarray,int [][] edges) {
		
		for(int i=0;i<verticesarray.length;i++) {
		
			 addVertex(verticesarray[i]);
	
		}
		 crearListaDeAdyacencia(edges,verticesarray.length);
	}
	
	public  UnweightedGraph(List<V> vertices,List<Edges> vecinos) {
	
		for(int i=0;i<vertices.size();i++) {
			
			addVertex(vertices.get(i));
			
		}
		
		crearListaDeAdyacencia(vecinos,vertices.size());
		
	}
	
	public UnweightedGraph(int vertices,int [][] edges) {
		
		for(int i=0;i<vertices;i++) {
			addVertex((V)(new Integer(i)));
		}
		
		 crearListaDeAdyacencia(edges,vertices);
		
	}
	
	public UnweightedGraph(int vertices,List<Edges> vecinos) {
		
		for(int i=0;i<vertices;i++) {
			addVertex((V)(new Integer(i)));
		}
		
		 crearListaDeAdyacencia(vecinos,vertices);
		
	}
	
	
	public void crearListaDeAdyacencia(List<Edges> edge,int numeroVertices) {
		
		for(Edges edges: edge) {
			addEdge(edges.getU(),edges.getV());
		}
		
	}
	
	public void crearListaDeAdyacencia(int [][] edges,int numVertices) {
		
		for(int i=0;i<edges.length;i++) {
			addEdge(edges[i][0],edges[i][1]);
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return vertices.size();
	}

	@Override
	public List getVertices() {
		// TODO Auto-generated method stub
		return vertices;
	}

	@Override
	public V getVertex(int index) {
		// TODO Auto-generated method stub
		return vertices.get(index);
	}

	@Override
	public int getIndex(V v) {
		
		if(!vertices.contains(v))
			throw new IllegalArgumentException("Tales objetos no estan dentro del grafo");
		// TODO Auto-generated method stub
		return vertices.indexOf(v);
	}

	@Override
	public List<Integer> getVecinos(int index) {
		
		ArrayList<Integer> resultado= new ArrayList<>();
		
		for(int i=0;i<vecinos.get(index).size();i++) {
			resultado.add(vecinos.get(index).get(i).getV());
		}
		
		// TODO Auto-generated method stub
		return resultado;
	}
	
	public List<List<Edges>> getConexiones(){
		return vecinos;
	}

	@Override
	public int getDegree(int index) {
		// TODO Auto-generated method stub
		return vecinos.get(index).size();
	}

	@Override
	public void printEdges() {
		for(int i=0;i<vertices.size();i++) {
			System.out.print("Vertice "+ getVertex(i)+ " (" + i + "): ");
			for(Edges e: vecinos.get(i)) {
				System.out.print("(" + getVertex(e.getU()) + ", " + getVertex(e.getV()) + ") ");
			}
			System.out.println();
		}
		
	}

	@Override
	public void clear() {
		vertices.clear();
		vecinos.clear();
		
	}

	@Override
	public boolean addVertex(V v) {
		
		
		if(!vertices.contains(v)) {
			vertices.add(v);
			vecinos.add(new ArrayList<Edges>());
			return true;
		}
		
		return false;
	}

	@Override
	public boolean addEdge(int u, int v) {
		// TODO Auto-generated method stub
		return addEdge((new Edges(u,v)));
	}

	@Override
	public boolean addEdge(Edges e) {
		
		if(e.getU()<0|| e.getU()>getSize()-1) {
			throw new IllegalArgumentException("No existe tal indice"+e.getU());
		}
		if(e.getV()<0|| e.getV()>getSize()-1)
			throw new IllegalArgumentException("No existe tal indice"+e.getV());
		
		
		if(!vecinos.get(e.getU()).contains(e)) {
			vecinos.get(e.getU()).add(e);
			return true;
		}
		else
		return false;
	}

	@Override
	public boolean removeVertex(V v) {
		
		
		if(!vertices.contains(v)) {
			return false;
		}
		else {
			int index= vertices.indexOf(v);
			vertices.remove(v);
			vecinos.remove(index);
			  for (List<Edges> list : vecinos) {
		          for (int i = 0; i < list.size();i++) {
		            if (list.get(i).getV() == index) {
		              list.remove(i);
		              break;
		            } else {
		             continue;
		            }
		          }
		        }
			  
		        for (List<Edges> list : vecinos) {
		          for (int i = 0; i < list.size(); i++) {
		            if (list.get(i).getU() > index) {
		              list.get(i).setU(list.get(i).getU()-1);
		            }
		            if (list.get(i).v > index) {
		            	 list.get(i).setV(list.get(i).getV()-1);
		            }
		          }
		        }
			return true;
		}
		
	}
	
	
	public List<List<Integer>> componentesConectados(){
		
		List<List<Integer>> componentes= new ArrayList<>();
	
		for(int i=0;i<vertices.size();i++) {
			
			SerchTree arbol= bfs(i);

			ArrayList<Integer> componente= arbol.getSerchOrder();
			Collections.sort(componente);
			
			if(!componentes.contains(componente)) {
				componentes.add(componente);
			}
			
		}
		
		return componentes;
		
	}

	@Override
	public boolean removeEdge(int u, int v) {
		return removeEdge(new Edges(u,v));
		
	}
	
	public boolean removeEdge(Edges e) {
		
		if(vecinos.get(e.getU()).contains(e)) {
			vecinos.get(e.getU()).remove(e);
			
			
			for(Edges ee:vecinos.get(e.getV())) {
				
			if(ee.getU()==e.getV()&&ee.getV()==e.getU()) {
				vecinos.get(e.getV()).remove(ee);
				break;
			}	
		}
		
			return true;
		}
		else {
			return false;
		}
		
	}
	
	@Override
	
	public SerchTree bfs(int index) {
		boolean [] control= new boolean[vertices.size()];
		ArrayList<Integer> searchOrder = new ArrayList<>();
		int [] padre= new int[vertices.size()];
		
		for(int i=0;i<padre.length;i++) {
			padre[i]=-1;
		}
		
		LinkedList<Integer> cola= new LinkedList<>();
		
		cola.offer(index);
		control[index]=true;
		while(!cola.isEmpty()) {
			int u=cola.poll();
			searchOrder.add(u);
			for(Edges e:vecinos.get(u)) {
				
				if(!control[e.getV()]) {
				cola.offer(e.getV());
				padre[e.getV()]=u;
				control[e.getV()]=true;
			
				}
				
		
			}		
		}
		
		return new SerchTree(index,padre,searchOrder);
	}
	
	public class SerchTree  {
     private  int root;
     private int [] padres;
     private ArrayList<Integer> serchOrder;
		
		
		public SerchTree(int root,int [] padres,ArrayList<Integer> serchOrder) {
			this.padres=padres;this.root=root;this.serchOrder=serchOrder;
			
		}
		
	public int getRoot() {
		return root;
	}
	
	public int [] padres() {
		return padres;
	}
	
	public ArrayList<Integer> getSerchOrder(){
		return serchOrder;
	}
	
	public int getParent(int index) {
		return padres[index];
	
	}
	public int getNumberOfVerticesFound() {
		return serchOrder.size();
	}
	
	public ArrayList<V> getPath(int index){
		ArrayList<V> path = new ArrayList<>();
		
		do {
			
			path.add(vertices.get(index));
			
			index=padres[index];
			
		} while (index!=-1);

		return path;
	}
	
	public void printPath(int index) {
		
		ArrayList<V> path= getPath(index);
		System.out.println("Una trayectoria desde la raiz "+vertices.get(root)+" hasta el indice "+ vertices.get(index)+" es: ");
		
		
		for(int i=path.size()-1;i>=0;i--) {
			System.out.print(path.get(i)+" ");
		}
	}
		
		
	}

	@Override
	public UnweightedGraph<V>.SerchTree dfs(int index) {
		
		ArrayList<Integer> orden= new ArrayList<>();
		int [] padre= new int[vertices.size()];
		for(int i=0;i<padre.length;i++) {
			padre[i]=-1;
		}
		
		boolean[] control= new boolean[vertices.size()];
		
		
		dfs(index,orden,padre,control);
		
		return new SerchTree(index,padre,orden);
	}
	
	public void dfs(int v,ArrayList<Integer> orden,int [] padre,boolean[] control ) {
		orden.add(v);
		control[v]=true;
		
		for(Edges e: vecinos.get(v)) {
			if(!control[e.getV()]) {
				padre[e.getV()]=v;
				dfs(e.getV(),orden,padre,control);
			}
		}
	}
	


}
