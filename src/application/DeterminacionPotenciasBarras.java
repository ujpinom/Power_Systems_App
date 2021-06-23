package application;

import java.util.List;

import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Complejo;

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
	
	

}
