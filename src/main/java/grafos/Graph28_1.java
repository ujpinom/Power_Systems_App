package grafos;
import java.util.*;
import java.net.*;

public class Graph28_1 {

	public static void main(String[] args) throws Exception {
		
		Scanner entrada= new Scanner(System.in);
		System.out.println("Ingrese una URL :");
		String ingreso= entrada.nextLine();
		URL url= new URL(ingreso);
		Scanner infile= new Scanner(url.openStream());
		
		String s= infile.nextLine();
		int numeroVertices= Integer.parseInt(s);System.out.println("El numero de vertices es :"+ numeroVertices);
		
		ArrayList<Edges> bordes= new ArrayList<>();
		
		
		while(infile.hasNext()) {
			
			s=infile.nextLine();
			String[] tokens= s.split("\\s");
		
			int u= Integer.parseInt(tokens[0]);
			
			for(int i=1;i<tokens.length;i++) {
				int v= Integer.parseInt(tokens[i]);
				bordes.add(new Edges(u,v));
			}
		}
		
		Graph<Integer> grafo= new UnweightedGraph<Integer>(numeroVertices,bordes);
		
		
		grafo.printEdges();
		
		UnweightedGraph<Integer>.SerchTree arbol= grafo.bfs(0);
		
		if(arbol.getSerchOrder().size()==numeroVertices) {
			System.out.println("El grafo es conectado");
		}
		else {
			System.out.println("El grafo no es conectado");
		}
	}

}
