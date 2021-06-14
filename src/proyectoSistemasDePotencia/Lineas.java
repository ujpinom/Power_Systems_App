package proyectoSistemasDePotencia;

import com.sun.javafx.scene.paint.GradientUtils.Point;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class Lineas {
	
	private Barras barra1;
	private Barras barra2;
	private double z1=1;
	private double z2=1;
	private double z0=1;
	private String nombreLinea;
	private double corrienteFallaFaseA;
	private double corrienteFallaFaseB;
	private double corrienteFallaFaseC;
	private double anguloCorrienteFaseA=0;
	private double anguloCorrienteFaseB=-120;
	private double anguloCorrienteFaseC=120;
	private double impedanciaFalla=0;
	private double tensionLineaPuntoFalla=1.0;
	private double magcorrientePuntoFallaFaseA;
	private double magcorrientePuntoFallaFaseB;
	private double magcorrientePuntoFallaFaseC;
	private double angCorrientePuntoFallaFaseA;
	private double angCorrientePuntoFallaFaseB;
	private double angCorrientePuntoFallaFaseC;
	private Point2D puntomedio;
	private double resitencia;
	private double mVarDeCargaTotales;
	private double YMediaParalela;
	
	
	
		
	public double getResitencia() {
		return resitencia;
	}


	public void setResitencia(double resitencia) {
		this.resitencia = resitencia;
	}


	public double getmVarDeCargaTotales() {
		return mVarDeCargaTotales;
	}


	public void setmVarDeCargaTotales(double mVarDeCargaTotales) {
		this.mVarDeCargaTotales = mVarDeCargaTotales;
	}


	public double getYMediaParalela() {
		return YMediaParalela;
	}


	public void setYMediaParalela(double yMediaParalela) {
		YMediaParalela = yMediaParalela;
	}


	public Point2D getPuntomedio() {
		return puntomedio;
	}


	public void setPuntomedio(Point2D puntomedio) {
		this.puntomedio = puntomedio;
	}


	public double getMagcorrientePuntoFallaFaseA() {
		return magcorrientePuntoFallaFaseA;
		
	}


	public void setMagcorrientePuntoFallaFaseA(double magcorrientePuntoFallaFaseA) {
		this.magcorrientePuntoFallaFaseA = magcorrientePuntoFallaFaseA;
	}


	public double getMagcorrientePuntoFallaFaseB() {
		return magcorrientePuntoFallaFaseB;
	}


	public void setMagcorrientePuntoFallaFaseB(double magcorrientePuntoFallaFaseB) {
		this.magcorrientePuntoFallaFaseB = magcorrientePuntoFallaFaseB;
	}


	public double getMagcorrientePuntoFallaFaseC() {
		return magcorrientePuntoFallaFaseC;
	}


	public void setMagcorrientePuntoFallaFaseC(double magcorrientePuntoFallaFaseC) {
		this.magcorrientePuntoFallaFaseC = magcorrientePuntoFallaFaseC;
	}


	public double getAngCorrientePuntoFallaFaseA() {
		return angCorrientePuntoFallaFaseA;
	}


	public void setAngCorrientePuntoFallaFaseA(double angCorrientePuntoFallaFaseA) {
		this.angCorrientePuntoFallaFaseA = angCorrientePuntoFallaFaseA;
	}


	public double getAngCorrientePuntoFallaFaseB() {
		return angCorrientePuntoFallaFaseB;
	}


	public void setAngCorrientePuntoFallaFaseB(double angCorrientePuntoFallaFaseB) {
		this.angCorrientePuntoFallaFaseB = angCorrientePuntoFallaFaseB;
	}


	public double getAngCorrientePuntoFallaFaseC() {
		return angCorrientePuntoFallaFaseC;
	}


	public void setAngCorrientePuntoFallaFaseC(double angCorrientePuntoFallaFaseC) {
		this.angCorrientePuntoFallaFaseC = angCorrientePuntoFallaFaseC;
	}


	public double getTensionLineaPuntoFalla() {
		return tensionLineaPuntoFalla;
	}


	public void setTensionLineaPuntoFalla(double tensionLineaPuntoFalla) {
		this.tensionLineaPuntoFalla = tensionLineaPuntoFalla;
	}


	public Barras getBarra1() {
		return barra1;
	}


	public void setBarra1(Barras barra1) {
		this.barra1 = barra1;
	}


	public Barras getBarra2() {
		return barra2;
	}


	public void setBarra2(Barras barra2) {
		this.barra2 = barra2;
	}


	public Lineas(Barras barra1, Barras barra2,double impedancia1,double impedancia2,double impedancia0) {
		
		this.barra1 = barra1;
		this.barra2 = barra2;
		
		this.z1=impedancia1;
		this.z0=impedancia0;
		this.z1=impedancia1;
		
	}
	
	public String getNombreLinea() {
		return nombreLinea;
	}
	
	public void setNombreLinea(String nombreLinea) {
		this.nombreLinea=nombreLinea;
	}
	
	public void setimpedanciaLineaZ1(double impedancia1) {
		this.z1=impedancia1;
	}
	public void setimpedanciaLineaZ2(double impedancia2) {
		this.z2=impedancia2;
	}
	public void setimpedanciaLineaZ0(double impedancia0) {
		this.z0=impedancia0;
	}
	
	public double getimpedanciaLineaZ1() {
		return z1;
	}
	public double getimpedanciaLineaZ2() {
		return z2;
	}
	public double getimpedanciaLineaZ0() {
		return z0;
	}

	
	public void setCorrienteFallaFaseA(double corrienteFalla) {
		this.corrienteFallaFaseA=corrienteFalla;
	}
	
	public double getCorrienteFallaFaseA() {
		return corrienteFallaFaseA;
	}
	public void setCorrienteFallaFaseB(double corrienteFalla) {
		this.corrienteFallaFaseB=corrienteFalla;
	}
	
	public double getCorrienteFallaFaseB() {
		return corrienteFallaFaseB;
	}
	public void setCorrienteFallaFaseC(double corrienteFalla) {
		this.corrienteFallaFaseC=corrienteFalla;
	}
	
	public double getCorrienteFallaFaseC() {
		return corrienteFallaFaseC;
	}
	
	public void setAnguloCorrienteFaseA(double angulo) {
		this.anguloCorrienteFaseA=angulo;
	}
	
	public double getAnguloCorrienteFaseA() {
		return anguloCorrienteFaseA;
	}
	
	public void setAnguloCorrienteFaseB(double angulo) {
		this.anguloCorrienteFaseB=angulo;
	}
	
	public double getAnguloCorrienteFaseB() {
		return anguloCorrienteFaseB;
	}
	
	public void setAnguloCorrienteFaseC(double angulo) {
		this.anguloCorrienteFaseC=angulo;
	}
	
	public double getAnguloCorrienteFaseC() {
		return anguloCorrienteFaseC;
	}
	public void setImpedanciafalla(double impedanciaFalla) {
		this.impedanciaFalla=impedanciaFalla;
	}
	
	public double getImpedanciaFalla() {
		return impedanciaFalla;
	}
}
