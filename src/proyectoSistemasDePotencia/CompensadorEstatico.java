package proyectoSistemasDePotencia;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class CompensadorEstatico {

	private double potenciaReactivaMin=-0.5;
	private double potenciaReactivaMax=0.5;
	private Point2D inicio;
	private Barras barra;
	private String nombreCompensador;
	public static final String LEFT="IZQUIERDA";
	public static final String RIGHT="DERECHA";
	public static final String ARRIBA="ARRIBA";
	public static final String ABAJO="ABAJO";
	private String orientacion="IZQUIERDA";
	
	private Circle ciculoCompensador;
	
	public Circle getCiculoCompensador() {
		return ciculoCompensador;
	}


	public void setCiculoCompensador(Circle ciculoCompensador) {
		this.ciculoCompensador = ciculoCompensador;
	}


	public CompensadorEstatico() {
		
	}
	
	
	public CompensadorEstatico(Point2D inicio, Barras barra, String nombreCompensador) {
		
		this.barra=barra;
		this.inicio=inicio;
		this.nombreCompensador=nombreCompensador;
		
	}

	public double getPotenciaReactivaMin() {
		return potenciaReactivaMin;
	}
	public void setPotenciaReactivaMin(double potenciaReactivaMin) {
		this.potenciaReactivaMin = potenciaReactivaMin;
	}
	public double getPotenciaReactivaMax() {
		return potenciaReactivaMax;
	}
	public void setPotenciaReactivaMax(double potenciaReactivaMax) {
		this.potenciaReactivaMax = potenciaReactivaMax;
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
	public String getNombreCompensador() {
		return nombreCompensador;
	}
	public void setNombreCompensador(String nombreCompensador) {
		this.nombreCompensador = nombreCompensador;
	}
	public String getOrientacion() {
		return orientacion;
	}
	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}
	
	
	
	
}
