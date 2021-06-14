package bts;
import java.util.*;
public class HuffmanCode {

	
	private static ArrayList<Character> letras= new ArrayList<>();
	private static ArrayList<Integer> valores= new ArrayList<>();
	private static Map<Character,Integer> frecuencia= new TreeMap<>();
	public static void main(String[] args) {
		Scanner entrada= new Scanner(System.in);
		
		
		System.out.println("Ingrese un String: ");
		String palabra= entrada.nextLine();
		
		System.out.printf("%-15s%-15s\n","Character","Frecuencia");
		
		vectorValores(palabra);
		
	}
	
	public static void 	vectorValores(String palabra) {
		
		for(int i=0;i<palabra.length();i++) {
			
			if(!frecuencia.containsKey(palabra.charAt(i)))
			frecuencia.put(palabra.charAt(i), 1);
			else {
				int key= frecuencia.get(palabra.charAt(i));
				++key;
				frecuencia.put(palabra.charAt(i), key);
				
			}
		
		}
		
	Set<Map.Entry<Character, Integer>> entrySet = frecuencia.entrySet();
	
		for(Map.Entry<Character,Integer> entradaa:entrySet) {
			valores.add((entradaa.getValue()));
			letras.add(entradaa.getKey());
		}
		
		for(int i=0;i<letras.size();i++) {
			System.out.printf("%-15s%-15d\n",letras.get(i),valores.get(i));
		
		}
	}

}
