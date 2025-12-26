package bts;
import java.util.*;
public class prueba {

	public static void main(String[] args) {
		ArrayList<Integer> hola=new  ArrayList<Integer>();
		ArrayList<Character> letras=new  ArrayList<>();
		
		
		String palabra= "gonorrea";
	
		int [] frecuencia= new int[palabra.length()];
		
		
		for(int i=0;i<palabra.length();i++) {
			
			if(!letras.contains(palabra.charAt(i))) {
				letras.add(palabra.charAt(i));
				frecuencia[letras.size()-1]=1;
			
			}
			else {
				
				int indice= letras.indexOf(palabra.charAt(i));
				frecuencia[indice]+=1;
			}
			
		}
		
		for(int i=0;i<letras.size();i++) {
			System.out.println( letras.get(i)+"    "+frecuencia[i]);
		}
	}

}
