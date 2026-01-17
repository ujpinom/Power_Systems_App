package grafos;
import java.util.*;
import bts.BinarySerchTree;
public interface Graph<V> {
	
	public int getSize();
	
	public List<V> getVertices();
	
	public V getVertex(int index);
	
	public int getIndex(V v);
	
	public List<Integer> getVecinos(int index);
	
	public int getDegree(int index);
	 
	public void printEdges();
	
	public void clear();
	
	public boolean addVertex(V v);
	
	public boolean addEdge(int u,int v);
	
	public boolean	addEdge(Edges e);
	
	public boolean removeVertex(V v);
	
	public boolean removeEdge(int u,int v);
	
	
	public List<List<Integer>> componentesConectados();	

	public UnweightedGraph<V>.SerchTree bfs(int index);
	
	public UnweightedGraph<V>.SerchTree dfs(int index);
	
	

}
