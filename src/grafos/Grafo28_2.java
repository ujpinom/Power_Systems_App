package grafos;
import java.util.*;
import java.io.*;
public class Grafo28_2 {


	
	public static void main(String[] args) throws Exception {
		
		try {
		Scanner entrada= new Scanner(System.in);
		System.out.println("Ingrese una direccion valida: ");
		String ingreso= entrada.nextLine();
		File input= new File(ingreso);
		Scanner infile= new Scanner(input);
		String s= infile.nextLine();
		int numVertices= Integer.parseInt(s);
		System.out.println("El numero de vertices es "+ numVertices);
		
		ArrayList<Edges> bordes= new ArrayList<>();
		
		while(infile.hasNext()) {
			
			s= infile.nextLine();
			String [] items= s.split("\s");
			
			int u=Integer.parseInt(items[0]);
			
			for(int i=1;i<items.length;i++) {
				
				int v=Integer.parseInt(items[i]);
				
				bordes.add(new Edges(u,v));
			}
		}
		
		UnweightedGraphDetectCycle<Integer> grafo=new  UnweightedGraphDetectCycle<>(numVertices,bordes);
		
		
		
//		Graph<Integer> grafo=  new UnweightedGraph<>(numVertices,bordes);
//		grafo.printEdges();
//		UnweightedGraph<Integer>.SerchTree arbol= grafo.bfs(0);
//		
//		
//		List<List<Integer>> componentes=grafo.componentesConectados();
//		
//		System.out.println("los componentes conectados son: ");
//		System.out.println(componentes);
//
//		if(arbol.getSerchOrder().size()==numVertices) {
//			System.out.println("El grafo es conectado");
//		}
//		else {
//			System.out.println("El grafo no es conectado");
//		}
		
		}
		catch (FileNotFoundException e) {
			
			System.out.println("El archivo ingresado no existe ");
		}
		
	}
	
	public static void imprimirComponentes(List<List<Integer>> componentes) {
		
		System.out.print("[ ");
		
		for(int i=0;i<componentes.size();i++) {
			System.out.print("[");
			for(int j=0;j<componentes.get(i).size();j++) {
				
				System.out.print(componentes.get(i).get(j)+" ");
				
			}
			System.out.print("] ");
		}
		
		System.out.print(" ]");
		
	}
	
	
}
