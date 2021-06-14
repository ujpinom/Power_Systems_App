package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.Arrays;

public class ConexionesBarras {
	
	public static String [] establecerConexionesBarras(ArrayList<Transformador> trafo,ArrayList<Barras> barras) {
		
		String [] conexionesBarras=new String[barras.size()-1];
		Arrays.fill(conexionesBarras, "O");
		
		
		for(int i=0;i<trafo.size();i++) {
			
			if(trafo.get(i).getConexionPrimaria().contains("Y")) {
				
				String xx=Character.toString(trafo.get(i).getConexionPrimaria().charAt(trafo.get(i).getConexionPrimaria().length()-1));
				conexionesBarras[Integer.parseInt(xx)-1]="Y";
			}
			if(trafo.get(i).getConexionSecundaria().contains("Y")) {
				String xx=Character.toString(trafo.get(i).getConexionSecundaria().charAt(trafo.get(i).getConexionSecundaria().length()-1));
				conexionesBarras[Integer.parseInt(xx)-1]="Y";
			}
			
			if(trafo.get(i).getConexionPrimaria().contains("DELTA")) {
				
				String xx=Character.toString(trafo.get(i).getConexionPrimaria().charAt(trafo.get(i).getConexionPrimaria().length()-1));
				conexionesBarras[Integer.parseInt(xx)-1]="D";
			}
			if(trafo.get(i).getConexionSecundaria().contains("DELTA")) {
				String xx=Character.toString(trafo.get(i).getConexionSecundaria().charAt(trafo.get(i).getConexionSecundaria().length()-1));
				conexionesBarras[Integer.parseInt(xx)-1]="D";
			}
		}
		
		for(int i=0;i<conexionesBarras.length;i++) {
			barras.get(i+1).setTipoConexionBarra(conexionesBarras[i]);
		}
		
		return conexionesBarras;
		
	}

}
