package bts;
import java.util.*;

public class TestBinaryTree {

	public static void main(String[] args) {
	
		Integer [] numeros={60,55,100,45,57,67,43,59};
			
		BinarySerchTree<Integer> listanumeros= new BinarySerchTree<>(numeros);
		System.out.println("Altura: ");
		System.out.println(listanumeros.altura());
		System.out.println("\nEl arbol binario es perfecto? "+listanumeros.esArbolPerfecto());
		System.out.println("\nNumero de hojas: "+ listanumeros.numeroHojas());
		
		Iterator iterator= listanumeros.breadthoirderr();
		
		while(iterator.hasNext()) {
			System.out.println((Integer)(iterator.next()));
		}
		
		
		System.out.println("\nNumero de no hojas: " + listanumeros.getNumberofNonLeaves());
		
	}

}
