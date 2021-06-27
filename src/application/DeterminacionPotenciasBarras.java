package application;

import java.util.List;

import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Complejo;
import proyectoSistemasDePotencia.Lineas;

public class DeterminacionPotenciasBarras {
	

	public static void potenciasEnBarrasComPV(List<Barras> barras,List<Complejo>[] solucion,Complejo[][] matAdj) {
	
		for(int i=1;i<barras.size();i++) {
			
			Barras b = barras.get(i);
			
			
			if(b.isBarraCompensacion()) {
				
				double poteciaReal= potenciaReal(i,b, solucion,matAdj);
				
				double potenciaImag= potenciaImaginaria(i,b, solucion,matAdj);
				
				b.setFlowPowerRealCalculada(poteciaReal);
				b.setFlowPowerImagCalculada(potenciaImag);
				
			}
			
			else if(b.isBarraPV()) {
				
				double potenciaImag= potenciaImaginaria(i,b, solucion,matAdj);
				
				double potenciaReal=0.0;
				
				if(b.containsGenerador()) {
					potenciaReal= b.getGenerador().getMWSalida();
				}
				
				
				b.setFlowPowerRealCalculada(potenciaReal);
				b.setFlowPowerImagCalculada(potenciaImag);
				
			}
			
		}
		
		
	}
	
	
	private static double potenciaReal(int indexBarra,Barras b,List<Complejo>[] solucion,Complejo[][] matAdj) {
		
		double potenciaCarga=0.0;
		
		if(b.containsCarga()) {
			
			potenciaCarga=b.getCarga().getPotenciaActiva();
			
		}
		
		
		Complejo potenciaCompleja=new Complejo();
		
		Complejo tensionBarraConjugada=Complejo.conjugado(solucion[indexBarra].get(solucion[indexBarra].size()-1)) ;
		
		for(int i=1;i<solucion.length;i++) {
			
			Complejo Yin=matAdj[indexBarra][i];
			Complejo Vn= solucion[i].get(solucion[i].size()-1);
	
			potenciaCompleja= Complejo.suma(potenciaCompleja, Complejo.producto(Yin, Vn));
			
		}
		
		
		potenciaCompleja= Complejo.producto(potenciaCompleja, tensionBarraConjugada);
	
		return potenciaCompleja.getReal()+potenciaCarga;
	
	}
	
	private static double potenciaImaginaria(int indexBarra,Barras b,List<Complejo>[] solucion,Complejo[][] matAdj) {
		
	double potenciaCarga=0.0;
		
		if(b.containsCarga()) {
			
			potenciaCarga=b.getCarga().getPotenciaReactiva();
			
		}
		
		
		Complejo potenciaCompleja=new Complejo();
		
		Complejo tensionBarraConjugada=Complejo.conjugado(solucion[indexBarra].get(solucion[indexBarra].size()-1)) ;
		
		for(int i=1;i<solucion.length;i++) {
			
			Complejo Yin=matAdj[indexBarra][i];
			Complejo Vn= solucion[i].get(solucion[i].size()-1);
	
			potenciaCompleja= Complejo.suma(potenciaCompleja, Complejo.producto(Yin, Vn));
			
		}
		
		
		potenciaCompleja= Complejo.producto(potenciaCompleja, tensionBarraConjugada);
	
		return -potenciaCompleja.getImag()+potenciaCarga;
	}
	
	
	public static void calculoPerdidasPotenciaLineas(List<Barras> barras,List<Complejo>[] solucion,List<Lineas> lineas, Complejo[][] yBus,Complejo[][] perdidasLineas,
			Complejo[][] potenciaEnBarras) {
		
		int contador=0;
		for(Lineas linea: lineas) {
			
			int indexB1= barras.indexOf(linea.getBarra1());
			
			int indexB2= barras.indexOf(linea.getBarra2());
			
			
			Complejo tensionB1= solucion[indexB1].get(solucion[indexB1].size()-1);
			
			Complejo tensionB2= solucion[indexB2].get(solucion[indexB2].size()-1);
			
			Complejo Y_B1_B2= Complejo.producto(new Complejo(-1,0), yBus[indexB1][indexB2]);
			
			// Potencia Compleja = VI*  B1-->B2
			
			// #####  Cálculo de I: B1-->B2
			
			Complejo I= Complejo.producto(Complejo.resta(tensionB1, tensionB2), Y_B1_B2);
			
			I = Complejo.conjugado(I);
			
			Complejo SB1B2= Complejo.producto(new Complejo(SPController.BASE_SISTEMA,0.0), Complejo.producto(tensionB1, I));
			
			perdidasLineas[indexB1][indexB2]= SB1B2;  
			
			double cargabilidad= linea.getYMediaParalela()*SPController.BASE_SISTEMA;
			
			double cargabilidadB1= cargabilidad*tensionB1.modulo()*tensionB1.modulo();
			
			SB1B2= Complejo.resta(SB1B2, new Complejo(0.0,cargabilidadB1));
			
			potenciaEnBarras[indexB1][indexB2]= SB1B2;
	
			// Potencia Compleja = VI*  B2-->B1
			
			// #####  Cálculo de I: B2-->B1
			
			
			I= Complejo.producto(Complejo.resta(tensionB2, tensionB1), Y_B1_B2);
			
			I = Complejo.conjugado(I);
			
			SB1B2= Complejo.producto(new Complejo(SPController.BASE_SISTEMA,0.0), Complejo.producto(tensionB2, I));
			
			perdidasLineas[indexB2][indexB1]= SB1B2; 
			
			double cargabilidadB2= cargabilidad*tensionB2.modulo()*tensionB2.modulo();
			
			SB1B2= Complejo.resta(SB1B2, new Complejo(0.0,cargabilidadB2));
			
			potenciaEnBarras[indexB2][indexB1]= SB1B2;
	
		}
	
	}
	
	

}
