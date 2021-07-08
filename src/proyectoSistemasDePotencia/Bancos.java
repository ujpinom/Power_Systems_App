package proyectoSistemasDePotencia;

import application.SPController;
import javafx.geometry.Point2D;

public class Bancos {
	
	
	private double potenciaReactiva;
	private Point2D puntoMedio;
	private Point2D inicio;
	private Barras barra;
	private String nombreCarga;
	static final String LEFT="LEFT";
	static final String RIGHT="RIGHT";
	static final String ARRIBA="ARRIBA";
	static final String ABAJO="ABAJO";
	private String orientacion="LEFT";
	
	public Bancos() {
	
	}

	public double getPotenciaReactiva() {
		return potenciaReactiva;
	}

	public void setPotenciaReactiva(double potenciaReactiva) {
		this.potenciaReactiva = potenciaReactiva;
	}

	public Point2D getPuntoMedio() {
		return puntoMedio;
	}

	public void setPuntoMedio(Point2D puntoMedio) {
		this.puntoMedio = puntoMedio;
	}

	public Point2D getInicio() {
		return inicio;
	}

	public void setInicio(Point2D inicio) {
		this.inicio = inicio;
	}

	public Barras getBarra() {
		return barra;
	}

	public void setBarra(Barras barra) {
		this.barra = barra;
	}

	public String getNombreCarga() {
		return nombreCarga;
	}

	public void setNombreCarga(String nombreCarga) {
		this.nombreCarga = nombreCarga;
	}

	public String getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	public Bancos(Point2D inicio, Barras barra, String nombreCarga) {
		super();
		this.inicio = inicio;
		this.barra = barra;
		this.nombreCarga = nombreCarga;
	}
	
}
