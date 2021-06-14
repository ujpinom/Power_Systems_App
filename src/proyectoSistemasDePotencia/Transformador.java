package proyectoSistemasDePotencia;

import javafx.geometry.Point2D;

public class Transformador extends Lineas {
	
	private String conexionPrimaria="YN-";
	private String conexionSecundaria="YN-";
	private double impedanciaAterrizamientoPrimaria;
	private double impedanciaAterrizamientoSecundaria;
	private Point2D puntoMedio;
	
	public Transformador(Barras barra1, Barras barra2, double impedancia1,
			double impedancia2,double impedancia0) {
		
		super(barra1, barra2, impedancia1,impedancia2,impedancia0);
		this.conexionPrimaria="YN-"+barra1.getNombreBarra();
		this.conexionSecundaria="YN-"+barra2.getNombreBarra();
		
		
	}
	
	public Point2D getPuntoMedio() {
		return puntoMedio;
	}



	public void setPuntoMedio(Point2D puntoMedio) {
		this.puntoMedio = puntoMedio;
	}



	public void setConexionPrimaria(String tipoConexion) {
		this.conexionPrimaria=tipoConexion;
	}
	
	public void setConexionSecundaria(String tipoConexion) {
		this.conexionSecundaria=tipoConexion;
	}
	
	public String getConexionPrimaria() {
		return conexionPrimaria;
	}
	
	public String getConexionSecundaria() {
		return conexionSecundaria;
	}
	
	public void setImpedanciaAterrizamientoPrimaria(double impedanciaAterrizamientoPrimaria) {
		this.impedanciaAterrizamientoPrimaria=3*impedanciaAterrizamientoPrimaria;
	}
	
	public double getImpedanciaAterrizamientoPrimaria() {
		return impedanciaAterrizamientoPrimaria;
	}
	
	public void setImpedanciaAterrizamientoSecundaria(double impedanciaAterrizamientoSecundaria) {
		this.impedanciaAterrizamientoSecundaria=3*impedanciaAterrizamientoSecundaria;
	}
	
	public double getImpedanciaAterrizamientoSecundaria() {
		return impedanciaAterrizamientoSecundaria;
	}
	

}
