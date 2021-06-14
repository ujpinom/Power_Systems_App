package proyectoSistemasDePotencia;

import java.util.ArrayList;

public class ZBarraSecuencia0 {

	private ArrayList<Lineas> conexiones;
	private ArrayList<Barras> barras;
	private ArrayList<Transformador> conexiones1;
	
	
	public ZBarraSecuencia0() {
		
	}
	
	public ZBarraSecuencia0(ArrayList<Lineas> conexiones,ArrayList<Barras> barras, ArrayList<Transformador> conexiones1) {
		
		this.barras=barras;
		this.conexiones=conexiones;
		this.conexiones1=conexiones1;
		
	}
	
}
