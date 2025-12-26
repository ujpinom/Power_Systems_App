package grafos;
import java.util.*;
public class PruebaNineTiles {

	public static void main(String[] args) {
		
		Scanner entrada= new Scanner(System.in);
		System.out.print("Por favor ingrese las nueve Ts y Hs: ");
		String secuencia= entrada.nextLine();
		
		char[] nodo= secuencia.toCharArray();
		
		NineTile ninetile= new NineTile();
		System.out.println("The steps to flip the coins are ");
	
		ArrayList<Integer> patth= ninetile.caminoCorto(ninetile.getIndiceCeldaVolteada(nodo));
		
		System.out.println(ninetile.getIndiceCeldaVolteada(nodo));
	//	System.out.println(patth.get(0).intValue());
		
		for(int i=0;i<patth.size();i++) {
			ninetile.imprimirr(ninetile.getNodes(patth.get(i)));
			System.out.println();
		}
	
	}

}
