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
	
	private int iteConvergencia;

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
		
		iteConvergencia=iteracionActual;

	}

	
	/**
	 * Retorna el número de la iteración para la cual el algoritmo alcanza la convergencia según el epsilón especificado.
	 * @return
	 */
	public int getIteConvergencia() {
		return iteConvergencia;
	}


	public void setIteConvergencia(int iteConvergencia) {
		this.iteConvergencia = iteConvergencia;
	}


	private void acelerar(int i) {
		
		Complejo voltajeAnterior= infoIteraciones[i].get(infoIteraciones[i].size()-2);
		Complejo voltajeActual= infoIteraciones[i].get(infoIteraciones[i].size()-1);
		
		Complejo voltajeAcelerado= Complejo.suma(Complejo.producto(new Complejo(factorAceleracion,0), Complejo.resta(voltajeActual, voltajeAnterior)), voltajeAnterior);
		
		infoIteraciones[i].remove(infoIteraciones[i].size()-1);
		
		infoIteraciones[i].add(voltajeAcelerado);

	}
	
	
	
	private void calculoBarraPVCompensador(Barras b) {
		
		boolean violaLimites=false;
		
	int indexBarra=barras.indexOf(b);
		
	Complejo voltajeIteAnteriorConjugado= Complejo.conjugado(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1));
		
	double potenciaReactivaMaximaCompensador= b.getCompensador().getPotenciaReactivaMax();
		
	double potenciaReactivaMinimaCompensador=b.getCompensador().getPotenciaReactivaMin();
	
	double potenciaActivaEntregada=0.0;
	
	if(b.containsCarga()) {
		potenciaActivaEntregada-=b.getCarga().getPotenciaActiva()/SPController.BASE_SISTEMA;
	}
	
	List<Complejo> primeraSumatoria= primeraSumatoria(indexBarra, matrizAdj);
	
	List<Complejo> segundaSumatoria= new ArrayList<>();
	
	Complejo resultado= new Complejo();
	
	for(int i=1;i<infoIteraciones.length;i++) {
		
		resultado=Complejo.suma(resultado, Complejo.producto(matrizAdj[indexBarra][i], infoIteraciones[i].get(infoIteraciones[i].size()-1)));
		
	}
	
	resultado= Complejo.producto(voltajeIteAnteriorConjugado,resultado);
	
	double potenciaImCalculada= -resultado.getImag();
	
	double potenciaReactivaCarga= 0.0;
	
	if(b.containsCarga()) {
		
		potenciaReactivaCarga= b.getCarga().getPotenciaReactiva()/SPController.BASE_SISTEMA;
	}


	if((potenciaImCalculada+potenciaReactivaCarga)>potenciaReactivaMaximaCompensador) {
		
		// Se trata la barra PV como barra PQ ya que el generador no es capaz de suplir la Q para mantener el nivel de voltaje  especificado.
		
		violaLimites=true;
	
		potenciaImCalculada=potenciaReactivaMaximaCompensador-potenciaReactivaCarga;

	}
	
	else if(potenciaImCalculada<potenciaReactivaMinimaCompensador) {
		
		violaLimites=true;
		potenciaImCalculada=potenciaReactivaMinimaCompensador;
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
		
		if(violaLimites) {
			
			infoIteraciones[indexBarra].add(resultado);
			
			acelerar(indexBarra);
			
			b.getCompensador().setMvarSalida(b.getCompensador().getPotenciaReactivaMax());
			
		
			Carga c= new Carga(); // La carga combinada del generador y la carga de la barra;
			
			c.setPotenciaActiva(potenciaActivaEntregada*SPController.BASE_SISTEMA);
			c.setPotenciaReactiva(potenciaImCalculada*SPController.BASE_SISTEMA);	
			b.setCargaFromPVtoPQ(c);			
			b.setBarraPV(false);
			b.setBarraPQ(true);
			b.setBarraFromPV2PQ(true);
			
		}
		else {
		
		double magResultado=resultado.modulo();
		
		/**
		 * Se corrige la magnitud del voltaje dado que es magnitud especificada en el problema de flujo de potencia.
		 */
		
		resultado= Complejo.cociente(Complejo.producto(new Complejo(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1).modulo(),0), resultado),new Complejo(magResultado,0) ) ;
		
		infoIteraciones[indexBarra].add(resultado);
	}
		
	} catch (ExcepcionDivideCero e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	
	
	private void calculoBarrasPV(Barras b) {
		
		boolean violaLimiites=false;
		
		if(b.containsCompensador()) {
			calculoBarraPVCompensador(b);
			
			return;
		}

		int indexBarra=barras.indexOf(b);
		
		Complejo voltajeIteAnteriorConjugado= Complejo.conjugado(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1));
		
		double potenciaReactivaMaximaGenerador= b.getGenerador().getMVarSalidaMax()/SPController.BASE_SISTEMA;
		
		double potenciaReactivaMinimaGenerador=b.getGenerador().getMVarSalidaMin()/SPController.BASE_SISTEMA;
		
		double potenciaActivaEntregada= b.getGenerador().getMWSalida()/SPController.BASE_SISTEMA;
		
		if(b.containsCarga()) {
			potenciaActivaEntregada-=b.getCarga().getPotenciaActiva()/SPController.BASE_SISTEMA;
		}
		
		
		List<Complejo> primeraSumatoria= primeraSumatoria(indexBarra, matrizAdj);
	
		List<Complejo> segundaSumatoria= new ArrayList<>();
		
		Complejo resultado= new Complejo();
		

		
		for(int i=1;i<infoIteraciones.length;i++) {
			
			resultado=Complejo.suma(resultado, Complejo.producto(matrizAdj[indexBarra][i], infoIteraciones[i].get(infoIteraciones[i].size()-1)));
			
		}
		
		resultado= Complejo.producto(voltajeIteAnteriorConjugado,resultado);
		
		double potenciaImCalculada= -resultado.getImag();
		
		double potenciaReactivaCarga= 0.0;
		
		if(b.containsCarga()) {
			
			potenciaReactivaCarga= b.getCarga().getPotenciaReactiva()/SPController.BASE_SISTEMA;
		}

	
		if((potenciaImCalculada+potenciaReactivaCarga)>potenciaReactivaMaximaGenerador) {
			
			// Se trata la barra PV como barra PQ ya que el generador no es capaz de suplir la Q para mantener el nivel de voltaje  especificado.
			
			violaLimiites=true;
		
			potenciaImCalculada=potenciaReactivaMaximaGenerador-potenciaReactivaCarga;
				
		
			
		}
		
		else if(potenciaImCalculada<potenciaReactivaMinimaGenerador) {
			
			violaLimiites=true;
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
			
			if(violaLimiites) {
				
				infoIteraciones[indexBarra].add(resultado);
				
				acelerar(indexBarra);
				
				b.getGenerador().setMVarSalida(b.getGenerador().getMVarSalidaMax() );
				
				Carga c= new Carga(); // La carga combinada del generador y la carga de la barra;
				
				c.setPotenciaActiva(potenciaActivaEntregada*SPController.BASE_SISTEMA);
				c.setPotenciaReactiva(potenciaImCalculada*SPController.BASE_SISTEMA);	
			
				b.setCargaFromPVtoPQ(c);			
				b.setBarraPV(false);
				b.setBarraPQ(true);
				b.setBarraFromPV2PQ(true);
	
			}
			else {
			double magResultado=resultado.modulo();
			
			/**
			 * Se corrige la magnitud del voltaje dado que es magnitud especificada en el problema de flujo de potencia.
			 */
			
			resultado= Complejo.cociente(Complejo.producto(new Complejo(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1).modulo(),0), resultado),new Complejo(magResultado,0) ) ;
			infoIteraciones[indexBarra].add(resultado);
			}
	
		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	private void calculoBarrasPQ(Barras b) throws ExcepcionDivideCero {
		int indexBarra=barras.indexOf(b);
		Complejo potenciaComplejaProgramada= new Complejo();
		
		Complejo voltajeIteAnteriorConjugado= Complejo.conjugado(infoIteraciones[indexBarra].get(infoIteraciones[indexBarra].size()-1));
		
		
		
		if(!b.containsCarga()&& ! b.isBarraFromPV2PQ()) {
			potenciaComplejaProgramada= new Complejo(0,0);

		}
		
		else if(b.containsCarga() && ! b.isBarraFromPV2PQ()) {
			
			potenciaComplejaProgramada= Complejo.producto(new Complejo(-1,0), new Complejo(b.getCarga().getPotenciaActiva()/SPController.BASE_SISTEMA,-b.getCarga().getPotenciaReactiva()/SPController.BASE_SISTEMA));
				
		}
		
		else if(b.isBarraFromPV2PQ()) {
			
			
			potenciaComplejaProgramada= new Complejo(b.getCargaFromPVtoPQ().getPotenciaActiva()/SPController.BASE_SISTEMA,-b.getCargaFromPVtoPQ().getPotenciaReactiva()/SPController.BASE_SISTEMA);
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



