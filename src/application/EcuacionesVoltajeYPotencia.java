package application;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Stack;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.Complejo;
import proyectoSistemasDePotencia.ExcepcionDivideCero;
import proyectoSistemasDePotencia.Generadores;
import java.lang.Exception;

public class EcuacionesVoltajeYPotencia {
	
	private double factorAceleracion;
	
	private List<Complejo>[] infoIteraciones;
	
	private int numeroIteraciones=20;
	
	private List<Barras> barras ;
	
	private double epsilon=0.01;
	
	private  Complejo [][] matrizAdj;
	
	private boolean llamadaDesdeBarraPV=false;  // Cuando la potencia Imaginaria calculada en una barra PV es superior al maximo programado del generador. Se haya el voltaje cambiando esta barra
	// a barra tipo PQ
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public EcuacionesVoltajeYPotencia(List<Barras> barras,int numeroIteraciones, double factorAceleracion,double epsilon) {
	
		this.factorAceleracion=factorAceleracion;
		this.barras=barras;
		
		this.epsilon=epsilon;
		
		this.numeroIteraciones=numeroIteraciones;
		infoIteraciones= (List<Complejo>[])new List[barras.size()];
		
		for(int i=0;i<barras.size();i++) {
			
			infoIteraciones[i]= new ArrayList<Complejo>();
		
		}
		
		for(int i=1;i<barras.size();i++) {

			infoIteraciones[i].add(Complejo.polar2Cartesiano(barras.get(i).getVoltajePrefalla(), barras.get(i).getAnguloVoltajeBarra()));
			
		}
		
	}
	

	public int getNumeroIteraciones() {
		return numeroIteraciones;
	}



	public void setNumeroIteraciones(int numeroIteraciones) {
		this.numeroIteraciones = numeroIteraciones;
	}



	public List<Complejo>[] calcularFlujoPotencia(List<Generadores> generadores,List<Bancos> bancos,List<Carga> cargas,
			Complejo[][] matrizAdj){
		
		this.matrizAdj=matrizAdj;
		
		calcularFlujoPotenciaHelper(generadores,bancos,cargas,matrizAdj);

		return infoIteraciones;
		
	}
	
	
	private void calcularFlujoPotenciaHelper(List<Generadores> generadores,List<Bancos> bancos,List<Carga> cargas,
			Complejo[][] matrizAdj) {
		
		int iteracionActual=1;
		
		int objetosCumplenEpsilon=0;  // Valida cuantos objetos han cumplido epsilon. Todos los objetos menos la barra de tierra deben cumplir este criterio
		while(iteracionActual<=numeroIteraciones) {
			
			for(int i=1;i<barras.size();i++) {
				Barras b= barras.get(i);
				
				if(b.isBarraCompensacion()) {
					objetosCumplenEpsilon++;
					continue;
				}
				
				else if(b.isBarraPQ()) {
					
					
					try {
						calculoBarrasPQ(b);
					} catch (ExcepcionDivideCero e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					acelerar(i);

					if(Math.abs(infoIteraciones[i].get(infoIteraciones[i].size()-1).modulo()-infoIteraciones[i].get(infoIteraciones[i].size()-2).modulo())<=epsilon) {
						
						if(Math.abs(infoIteraciones[i].get(infoIteraciones[i].size()-1).argumento()-infoIteraciones[i].get(infoIteraciones[i].size()-2).argumento())<=epsilon)
						
						objetosCumplenEpsilon++;
					}
			
				} 
				
				else if(b.isBarraPV()) {
					
					calculoBarrasPV(b);
					
					
					if(Math.abs(infoIteraciones[i].get(infoIteraciones[i].size()-1).argumento()-infoIteraciones[i].get(infoIteraciones[i].size()-2).argumento())<=epsilon) {
						objetosCumplenEpsilon++;
					}
					
				}
				
				
				
			}
			
			if(objetosCumplenEpsilon==barras.size()-1) {
				break;
			}
			
			objetosCumplenEpsilon=0;

			iteracionActual++;
		}

	}
	
	
	private void acelerar(int i) {
		
		Complejo voltajeAnterior= infoIteraciones[i].get(infoIteraciones[i].size()-2);
		Complejo voltajeActual= infoIteraciones[i].get(infoIteraciones[i].size()-1);
		
		Complejo voltajeAcelerado= Complejo.suma(Complejo.producto(new Complejo(factorAceleracion,0), Complejo.resta(voltajeActual, voltajeAnterior)), voltajeAnterior);
		
		infoIteraciones[i].remove(infoIteraciones[i].size()-1);
		
		infoIteraciones[i].add(voltajeAcelerado);

	}
	
	
	private void calculoBarrasPV(Barras b) {
		int indexBarra=barras.indexOf(b);
		
		Complejo voltajeIteAnteriorConjugado= Complejo.conjugado(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1));
		
		double potenciaReactivaMaximaGenerador= b.getGenerador().getMVarSalidaMax();
		
		double potenciaReactivaMinimaGenerador=b.getGenerador().getMVarSalidaMin();
		
		double potenciaActivaEntregada= b.getGenerador().getMWSalida();
		
		if(b.containsCarga()) {
			potenciaActivaEntregada-=b.getCarga().getPotenciaActiva();
		}
		
		
		List<Complejo> primeraSumatoria= primeraSumatoria(indexBarra, matrizAdj);
	
		List<Complejo> segundaSumatoria= new ArrayList<>();
		
		Complejo resultado= new Complejo();
		

		
		for(int i=1;i<infoIteraciones.length;i++) {
			
			resultado=Complejo.suma(resultado, Complejo.producto(matrizAdj[indexBarra][i], infoIteraciones[i].get(infoIteraciones[i].size()-1)));
			
		}
		
		resultado= Complejo.producto(voltajeIteAnteriorConjugado,resultado);
		
		double potenciaImCalculada= -resultado.getImag();
		
		if(potenciaImCalculada>potenciaReactivaMaximaGenerador) {
			
			potenciaImCalculada=potenciaReactivaMaximaGenerador;
			
		}
		
		else if(potenciaImCalculada<potenciaReactivaMinimaGenerador) {
			
			
			potenciaImCalculada=potenciaReactivaMinimaGenerador;
		}
		
		
		
		Complejo potenciaCompleja= new Complejo(potenciaActivaEntregada,-potenciaImCalculada);
		
		resultado= new Complejo();
		
		try {
		
			resultado= Complejo.cociente(potenciaCompleja, voltajeIteAnteriorConjugado);
			
			
			for(int ii=0;ii<primeraSumatoria.size();ii++) {
				
				Complejo temp= primeraSumatoria.get(ii);
				
				resultado= Complejo.resta(resultado, temp);
				
			}
			
			
			segundaSumatoria= segundaSumatoria(indexBarra, matrizAdj);
			
			for(int ii=0;ii<segundaSumatoria.size();ii++) {
				
				Complejo temp= segundaSumatoria.get(ii);
				
				resultado= Complejo.resta(resultado, temp);
				
			}
			
			resultado= Complejo.cociente(resultado, matrizAdj[indexBarra][indexBarra]);
			
			double magResultado=resultado.modulo();
			
			/**
			 * Se corrige la magnitud del voltaje dado que es magnitud especificada en el problema de flujo de potencia.
			 */
			
			resultado= Complejo.cociente(Complejo.producto(new Complejo(b.getVoltajePrefalla(),0), resultado),new Complejo(magResultado,0) ) ;
			
			infoIteraciones[indexBarra].add(resultado);
			
			
		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	private void calculoBarrasPQ(Barras b) throws ExcepcionDivideCero {
		int indexBarra=barras.indexOf(b);
		Complejo potenciaComplejaProgramada= new Complejo();
		
		Complejo voltajeIteAnteriorConjugado= Complejo.conjugado(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1));
		
		
		
		if(!b.containsBanco()&& !b.containsCarga()) {
			potenciaComplejaProgramada= new Complejo(0,0);

		}
		
		else if(b.containsCarga()) {
			
			
			potenciaComplejaProgramada= Complejo.producto(new Complejo(-1,0), new Complejo(b.getCarga().getPotenciaActiva(),-b.getCarga().getPotenciaReactiva()));
			
				
		}
		
		
		
		
		try {
			
			List<Complejo> primeraSumatoria= primeraSumatoria(indexBarra, matrizAdj);
			Complejo resultado= Complejo.cociente(potenciaComplejaProgramada, voltajeIteAnteriorConjugado);
			
			
			for(int ii=0;ii<primeraSumatoria.size();ii++) {
				
				Complejo temp= primeraSumatoria.get(ii);
				
				resultado= Complejo.resta(resultado, temp);
				
			}
			
			
			
			
			List<Complejo> segundaSumatoria= segundaSumatoria(indexBarra, matrizAdj);
			
			for(int ii=0;ii<segundaSumatoria.size();ii++) {
				
				Complejo temp= segundaSumatoria.get(ii);
				
				resultado= Complejo.resta(resultado, temp);
				
			}
			
			
			
			
			resultado= Complejo.cociente(resultado, matrizAdj[indexBarra][indexBarra]);
			
			
			infoIteraciones[indexBarra].add(resultado);

		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private List<Complejo> segundaSumatoriaPV(int barraActual){
		
		List<Complejo> segundaSum= new ArrayList<>();
		
		for(int j=barraActual;j<barras.size();j++) {
			
			Complejo admitancia= matrizAdj[barraActual][j];
			
			Complejo voltajeActual= infoIteraciones[j].get(infoIteraciones[j].size()-1);
			
			Complejo producto= Complejo.producto(admitancia, voltajeActual);
			
			segundaSum.add(producto);
			
		}
		
		
		return segundaSum;
		
	}
	
	private List<Complejo> segundaSumatoria(int barraActual,Complejo[][] matrizAdj){
		
		List<Complejo> segundaSum= new ArrayList<>();
		
		
		for(int j=barraActual+1;j<barras.size();j++) {
			
			Complejo admitancia= matrizAdj[barraActual][j];
			
			Complejo voltajeActual= infoIteraciones[j].get(infoIteraciones[j].size()-1);
			
			Complejo producto= Complejo.producto(admitancia, voltajeActual);
			
			segundaSum.add(producto);
			
		}
		
		
		return segundaSum;
		
		
	}
	
	private List<Complejo> primeraSumatoria(int barraActual,Complejo[][] matrizAdj){
		
		List<Complejo> primeraSum= new ArrayList<>();
		
		
		for(int j=1;j<barraActual;j++) {
			
			Complejo admitancia= matrizAdj[barraActual][j];
			
			Complejo voltajeActual= infoIteraciones[j].get(infoIteraciones[j].size()-1);
			
			
			Complejo producto= Complejo.producto(admitancia, voltajeActual);
			
			
			primeraSum.add(producto);
			

		}
		
		
		return primeraSum;
		
	}

}


