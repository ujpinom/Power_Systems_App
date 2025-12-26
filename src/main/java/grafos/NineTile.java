package grafos;

import java.util.ArrayList;
import java.util.List;

public class NineTile {
	
	private int nodos=512;
	private int dimensiones=9;
	
	private  UnweightedGraph<Integer>.SerchTree arbol;
	
	
	public NineTile() {
		List<Edges> bordes= obtenerBordes();
		Graph<Integer> grafo= new UnweightedGraph<Integer>(nodos,bordes);
		arbol= grafo.bfs(511);	

	}
	
	
	public UnweightedGraph<Integer>.SerchTree getArbol(){
		return arbol;
	}
	
	private List<Edges> obtenerBordes(){
		
		List<Edges> bordes= new ArrayList<>();
		
		for(int u=0;u<nodos;u++) {
			for(int k=0;k<9;k++) {
				char[] nodes=getNodes(u);				
				if(nodes[k]=='H') {
					int v= getNodoVecino(nodes,k);
					bordes.add(new Edges(v,u));
				}
			}
		}
		
		return bordes;
	}
	
	public int getNodoVecino(char [] nodo,int posi){
		
		int fila=posi/3;int columna=posi%3;
		
		voltearCelda(nodo,fila,columna);
		voltearCelda(nodo,fila-1,columna);
		voltearCelda(nodo,fila+1,columna);
		voltearCelda(nodo,fila,columna-1);
		voltearCelda(nodo,fila,columna+1);
		
		return getIndiceCeldaVolteada(nodo);
	}
	
	public int getIndiceCeldaVolteada(char [] nodo) {
		int resultado=0;
		
		for(int i=0;i<9;i++) {
			if(nodo[i]=='T') {
				resultado=resultado*2+1;
			}
			else {
				resultado=resultado*2+0;
			}
		}
		return resultado;
	}
	
	public void voltearCelda(char nodo [], int fila,int columna) {
		if(fila>=0 && fila <=2 && columna>=0 && columna<=2) {
			if(nodo[fila*3+columna]=='H') {
				
				nodo[fila*3+columna]='T';
			}
			else {
				nodo[fila*3+columna]='H';
			}
		}
	}
	
	public char [] getNodes(int index ) {
		
		char [] nodos= new char[9];
		
		
		for(int i=0;i<9;i++) {
			int posi= index%2;
			if(posi==0) {
				nodos[8-i]='H';
			}
			else {
				nodos[8-i]='T';
			}
			index=index/2;
		}
		
		return nodos;
		
	}
	
	public ArrayList<Integer> caminoCorto(int index){
		
		return arbol.getPath(index);
	}
	
	public void imprimirr(char node []) {
		
		for(int i=0;i<node.length;i++) {
		
			if(i%3!=2) {
				System.out.print(node[i]);
			}
			else System.out.println(node[i]);

		}
		System.out.println();
		
		
		
	}
	
	
	
	
}
