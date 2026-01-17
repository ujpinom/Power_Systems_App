package proyectoSistemasDePotencia;

import java.util.ArrayList;

import javafx.scene.shape.Line;

public class FallaTrifasica {
	
	private double [][] zBarra;
	private String elementoFallado;
	private double corrientePuntoFalla;
	private int columnaElementoFallado;
	private ArrayList<Barras> barras;
	private ArrayList<Lineas> linea;
	private ArrayList<Transformador> trafo;
	private ArrayList<Double> vectorVoltajesFalla= new ArrayList<>();
	private ArrayList<Generadores> generador;
	
	
	public FallaTrifasica(double[][] zBarra, String elementoFallado,ArrayList<Barras> barras,
			ArrayList<Lineas> lineas,ArrayList<Transformador> trafo,ArrayList<Generadores> generador) {
		
		super();
		this.zBarra = zBarra;
		this.elementoFallado = elementoFallado;
		this.barras=barras;
		this.linea=lineas;
		this.trafo=trafo;
		this.generador=generador;
		calculoFallas();
		
	}
	
	
	private void calculoFallas() {
		
		
		char b=getElementoFallado().charAt(1);
		
		columnaElementoFallado= Integer.parseInt(Character.toString(b));
		
		
		setCorrienteFallaElementoFallado();
		
		setVectorVoltajesFalla();
		
		setCorrientesDeLinea();
		
		
		
	}
	
	
	public void setVectorVoltajesFalla() {
		char b=getElementoFallado().charAt(1);
		
		Barras barraFallada= barras.get(Integer.parseInt(Character.toString(b)));
		
		for(int i=0;i<zBarra.length;i++) {
			
			 double elemento=barraFallada.getVoltajePrefalla()-(zBarra[i][columnaElementoFallado-1]/
					 (zBarra[columnaElementoFallado-1][columnaElementoFallado-1]+barraFallada.getImpedanciaFalla()))*barraFallada.getVoltajePrefalla();
			
			vectorVoltajesFalla.add(elemento);
			
			barras.get(i+1).setVoltajePosFallaFaseA(vectorVoltajesFalla.get(i));
			barras.get(i+1).setVoltajePosFallaFaseB(vectorVoltajesFalla.get(i));
			barras.get(i+1).setVoltajePosFallaFaseC(vectorVoltajesFalla.get(i));
			
			barras.get(i+1).setAnguloVoltajeFaseA(0);
			barras.get(i+1).setAnguloVoltajeFaseB(-120);
			barras.get(i+1).setAnguloVoltajeFaseC(120);
			
		}
				
	}
	
	public void setCorrientesDeLinea()
	{
		
		double anguloImpedancia=-90;
		
		for(int i=0;i<linea.size();i++) {
			
			double vFaseABarra1= linea.get(i).getBarra1().getVoltajePosFallaFaseA();
			double vFaseABarra2= linea.get(i).getBarra2().getVoltajePosFallaFaseA();
	
			double aFaseABarra1= linea.get(i).getBarra1().getAnguloVoltajeFaseA();
			double aFaseBBarra1= linea.get(i).getBarra1().getAnguloVoltajeFaseB();
			double aFaseCBarra1= linea.get(i).getBarra1().getAnguloVoltajeFaseC();
			
			linea.get(i).setAnguloCorrienteFaseA(aFaseABarra1+anguloImpedancia);
			linea.get(i).setAnguloCorrienteFaseB(aFaseBBarra1+anguloImpedancia);
			linea.get(i).setAnguloCorrienteFaseC(aFaseCBarra1+anguloImpedancia);
			
			double corrienteFalla= Math.abs(vFaseABarra1-vFaseABarra2)/linea.get(i).getimpedanciaLineaZ1();
			
			linea.get(i).setCorrienteFallaFaseA(corrienteFalla);
			linea.get(i).setCorrienteFallaFaseB(corrienteFalla);
			linea.get(i).setCorrienteFallaFaseC(corrienteFalla);
			
			
		}
		
		
		for(int i=0;i<trafo.size();i++) {
			
			double vFaseABarra1= trafo.get(i).getBarra1().getVoltajePosFallaFaseA();
			double vFaseABarra2= trafo.get(i).getBarra2().getVoltajePosFallaFaseA();
	
			double aFaseABarra1= trafo.get(i).getBarra1().getAnguloVoltajeFaseA();
			double aFaseBBarra1= trafo.get(i).getBarra1().getAnguloVoltajeFaseB();
			double aFaseCBarra1= trafo.get(i).getBarra1().getAnguloVoltajeFaseC();
			
			trafo.get(i).setAnguloCorrienteFaseA(aFaseABarra1+anguloImpedancia);
			trafo.get(i).setAnguloCorrienteFaseB(aFaseBBarra1+anguloImpedancia);
			trafo.get(i).setAnguloCorrienteFaseC(aFaseCBarra1+anguloImpedancia);
			
			double corrienteFalla= Math.abs(vFaseABarra1-vFaseABarra2)/trafo.get(i).getimpedanciaLineaZ1();
			
			trafo.get(i).setCorrienteFallaFaseA(corrienteFalla);
			trafo.get(i).setCorrienteFallaFaseB(corrienteFalla);
			trafo.get(i).setCorrienteFallaFaseC(corrienteFalla);
			
		}
		
		for(int i=0;i<generador.size();i++) {
			
			generador.get(i).setCorrienteFaseA((generador.get(i).getBarra().getVoltajePrefalla()-
					generador.get(i).getBarra().getVoltajePosFallaFaseA())/(generador.get(i).getImpedanciaZ1()));
			
			generador.get(i).setCorrienteFaseB((generador.get(i).getBarra().getVoltajePrefalla()-
					generador.get(i).getBarra().getVoltajePosFallaFaseB())/(generador.get(i).getImpedanciaZ1()));
			
			generador.get(i).setCorrienteFaseC((generador.get(i).getBarra().getVoltajePrefalla()-
					generador.get(i).getBarra().getVoltajePosFallaFaseC())/(generador.get(i).getImpedanciaZ1()));
			
			generador.get(i).setAnguloCorrienteFaseA(-90);
			generador.get(i).setAnguloCorrienteFaseB(-210);
			generador.get(i).setAnguloCorrienteFaseC(30);
			
		}
		
		
	}
	public void setCorrienteFallaElementoFallado(){
		
		char b=getElementoFallado().charAt(1);
		
		Barras barraFallada= barras.get(Integer.parseInt(Character.toString(b)));
		
		corrientePuntoFalla=(-1)*barraFallada.getVoltajePrefalla()/(zBarra[columnaElementoFallado-1][columnaElementoFallado-1]+barraFallada.getImpedanciaFalla());
		
		barraFallada.setAngCorrientePuntoFallaFaseA(-90);
		barraFallada.setAngCorrientePuntoFallaFaseB(-210);
		barraFallada.setAngCorrientePuntoFallaFaseC(30);
		
		barraFallada.setMagcorrientePuntoFallaFaseA((-1)*corrientePuntoFalla);
		barraFallada.setMagcorrientePuntoFallaFaseB((-1)*corrientePuntoFalla);
		barraFallada.setMagcorrientePuntoFallaFaseC((-1)*corrientePuntoFalla);
		
	}
	
	
	public double getCorrientePuntoFallado() {
		
		return corrientePuntoFalla;
	}
	
	
	public double[][] getzBarra() {
		return zBarra;
	}
	public void setzBarra(double[][] zBarra) {
		this.zBarra = zBarra;
	}
	public String getElementoFallado() {
		return elementoFallado;
	}
	public void setElementoFallado(String elementoFallado) {
		this.elementoFallado = elementoFallado;
	}
	
}
