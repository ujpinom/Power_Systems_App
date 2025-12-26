package proyectoSistemasDePotencia;

import java.util.ArrayList;

public class FallaLineaALineaTierra {

	private double [][] matrizImpedanciaZ0;
	private double [][] matrizImpedanciaZ1;
	private double [][] matrizImpedanciaZ2;
	private ArrayList<Barras> barras;
	private ArrayList<Lineas> lineas;
	private ArrayList<Transformador> trafo;
	private Barras barraFallada;
	private ArrayList<Generadores> generador;
	private Complejo corrientePuntoFallaSecuencia1;
	private Complejo corrientePuntoFallaSecuencia2;
	private Complejo corrientePuntoFallaSecuencia0;
	private ArrayList<Double> vectorVoltajesSecuencia0= new ArrayList<>();
	private ArrayList<Double> vectorVoltajesSecuencia1= new ArrayList<>();
	private ArrayList<Double> vectorVoltajesSecuencia2= new ArrayList<>();
	
	
	public FallaLineaALineaTierra(double[][] matrizImpedanciaZ0, double[][] matrizImpedanciaZ1,
			double[][] matrizImpedanciaZ2, ArrayList<Barras> barras, ArrayList<Lineas> lineas,
			ArrayList<Transformador> trafo, Barras barraFallada, ArrayList<Generadores> generador) {
		
		super();
		this.matrizImpedanciaZ0 = matrizImpedanciaZ0;
		this.matrizImpedanciaZ1 = matrizImpedanciaZ1;
		this.matrizImpedanciaZ2 = matrizImpedanciaZ2;
		this.barras = barras;
		this.lineas = lineas;
		this.trafo = trafo;
		this.barraFallada = barraFallada;
		this.generador = generador;
		
		calculoFalla();
		
	}
	
	public void calculoFalla() {
		
		corrientesPuntoFalla();
		vectorVoltajes();
		contribucionMaquina();
		corrienteLineas();
		
	}
	
	
	public void corrientesPuntoFalla() {
		
		Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
		Complejo a= new Complejo(-0.5,0.8660254038);
		
		try {
			
			int indexBarraFallada= barras.indexOf(barraFallada);
			
			
			double impedanciTotal= matrizImpedanciaZ1[indexBarraFallada-1][indexBarraFallada-1]+
					((matrizImpedanciaZ2[indexBarraFallada-1][indexBarraFallada-1]*(matrizImpedanciaZ0[indexBarraFallada-1][indexBarraFallada-1]+
					3*barraFallada.getImpedanciaFalla()))/(matrizImpedanciaZ2[indexBarraFallada-1][indexBarraFallada-1]+
							matrizImpedanciaZ0[indexBarraFallada-1][indexBarraFallada-1]+3*barraFallada.getImpedanciaFalla()));
			
			corrientePuntoFallaSecuencia1=Complejo.cociente(new Complejo(barraFallada.getVoltajePrefalla(),0), new Complejo(0,impedanciTotal));
					
			impedanciTotal=(matrizImpedanciaZ0[indexBarraFallada-1][indexBarraFallada-1]+
					3*barraFallada.getImpedanciaFalla())/(matrizImpedanciaZ2[indexBarraFallada-1][indexBarraFallada-1]+
							matrizImpedanciaZ0[indexBarraFallada-1][indexBarraFallada-1]+3*barraFallada.getImpedanciaFalla());
			
			corrientePuntoFallaSecuencia2=Complejo.producto(Complejo.producto(new Complejo(-1,0), corrientePuntoFallaSecuencia1), 
					new Complejo(impedanciTotal,0));
			
			impedanciTotal=matrizImpedanciaZ2[indexBarraFallada-1][indexBarraFallada-1]/(matrizImpedanciaZ2[indexBarraFallada-1][indexBarraFallada-1]+
					matrizImpedanciaZ0[indexBarraFallada-1][indexBarraFallada-1]+3*barraFallada.getImpedanciaFalla());
			
			corrientePuntoFallaSecuencia0=Complejo.producto(Complejo.producto(new Complejo(-1,0), corrientePuntoFallaSecuencia1),
						new Complejo(impedanciTotal,0));
			
			Complejo iA= Complejo.suma(Complejo.suma(corrientePuntoFallaSecuencia1, corrientePuntoFallaSecuencia2), corrientePuntoFallaSecuencia0);
			
			barraFallada.setAngCorrientePuntoFallaFaseA(iA.argumento());
			barraFallada.setMagcorrientePuntoFallaFaseA(iA.modulo());
			
			Complejo iB=Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFallaSecuencia1),
					Complejo.producto(a, corrientePuntoFallaSecuencia2)), corrientePuntoFallaSecuencia0);
			
			barraFallada.setAngCorrientePuntoFallaFaseB(iB.argumento());
			barraFallada.setMagcorrientePuntoFallaFaseB(iB.modulo());
			
			Complejo iC=Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFallaSecuencia2), 
					Complejo.producto(a, corrientePuntoFallaSecuencia1)), corrientePuntoFallaSecuencia0);
			
			barraFallada.setAngCorrientePuntoFallaFaseC(iC.argumento());
			barraFallada.setMagcorrientePuntoFallaFaseC(iC.modulo());
		
			
		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void corrienteLineas() {
	
		for(int i=0;i<lineas.size();i++) {
			
			
			int barra1= barras.indexOf( lineas.get(i).getBarra1()) ;
			int barra2=	barras.indexOf( lineas.get(i).getBarra2()) ;
			
			int minimo=Math.min(barra1, barra2);int maximo=Math.max(barra2,barra1);
			
			try {
				
				
				Complejo iA0= Complejo.cociente(new Complejo((vectorVoltajesSecuencia0.get(minimo-1)-vectorVoltajesSecuencia0.get(maximo-1)),0),
						new Complejo(0,lineas.get(i).getimpedanciaLineaZ0()));
				
				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo((vectorVoltajesSecuencia1.get(minimo-1)-vectorVoltajesSecuencia1.get(maximo-1)),0), 
						new Complejo(0,lineas.get(i).getimpedanciaLineaZ1()));
				
				Complejo iA2= Complejo.cociente(new Complejo((vectorVoltajesSecuencia2.get(minimo-1)-vectorVoltajesSecuencia2.get(maximo-1)),0),
						new Complejo(0,lineas.get(i).getimpedanciaLineaZ2()));
				
				Complejo iA= Complejo.suma(Complejo.suma(iA1, iA2), iA0);
				
				double magIA= new Complejo(iA.getReal(),iA.getImag()).modulo();
				double angIa=new Complejo(iA.getReal(),iA.getImag()).argumento();
				
				lineas.get(i).setCorrienteFallaFaseA(magIA);lineas.get(i).setAnguloCorrienteFaseA(angIa);
				
				Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
				Complejo a= new Complejo(-0.5,0.8660254038);
				
				Complejo aCuadradoiA1= Complejo.producto(aCuadrado, iA1);Complejo aIA2= Complejo.producto(a, iA2);
				Complejo iB= Complejo.suma(iA0, Complejo.suma(aCuadradoiA1, aIA2));
				
				double magIB= new Complejo(iB.getReal(),iB.getImag()).modulo();
				double angIB=new Complejo(iB.getReal(),iB.getImag()).argumento();
				
				lineas.get(i).setCorrienteFallaFaseB(magIB);lineas.get(i).setAnguloCorrienteFaseB(angIB);
				
				Complejo aCuadradoiA2= Complejo.producto(aCuadrado, iA2);Complejo aIA1= Complejo.producto(a, iA1);
				Complejo iC= Complejo.suma(iA0, Complejo.suma(aCuadradoiA2, aIA1));
				
				double magIC= new Complejo(iC.getReal(),iC.getImag()).modulo();
				double angIC=new Complejo(iC.getReal(),iC.getImag()).argumento();
				
				lineas.get(i).setCorrienteFallaFaseC(magIC);lineas.get(i).setAnguloCorrienteFaseC(angIC);
				
				
				
			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Complejo iA0;
			
		for(int i=0;i<trafo.size();i++) {
			
			int barra1= barras.indexOf( trafo.get(i).getBarra1()) ;
			int barra2=	barras.indexOf( trafo.get(i).getBarra2()) ;
			

			int minimo=Math.min(barra1, barra2);int maximo=Math.max(barra2,barra1);
			
			
			try {
				
				if((trafo.get(i).getConexionPrimaria().contains("Y-")||trafo.get(i).getConexionSecundaria().contains("Y-"))||
						(trafo.get(i).getConexionPrimaria().contains("DELTA")&&trafo.get(i).getConexionSecundaria().contains("DELTA"))) {
					
					iA0=new Complejo(0,0);
				}
				else {
					
					iA0= Complejo.cociente(new Complejo((vectorVoltajesSecuencia0.get(minimo-1)-vectorVoltajesSecuencia0.get(maximo-1)),0),
							new Complejo(0,trafo.get(i).getimpedanciaLineaZ0()+3*trafo.get(i).getImpedanciaAterrizamientoPrimaria()+
							3*trafo.get(i).getImpedanciaAterrizamientoSecundaria()));	
				}
				
				 
				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo((vectorVoltajesSecuencia1.get(minimo-1)-vectorVoltajesSecuencia1.get(maximo-1)),0), 
						new Complejo(0,trafo.get(i).getimpedanciaLineaZ1()));
				
				Complejo iA2= Complejo.cociente(new Complejo((vectorVoltajesSecuencia2.get(minimo-1)-vectorVoltajesSecuencia2.get(maximo-1)),0),
						new Complejo(0,trafo.get(i).getimpedanciaLineaZ2()));
				
				Complejo iA= Complejo.suma(Complejo.suma(iA1, iA2), iA0);
				
				double magIA= new Complejo(iA.getReal(),iA.getImag()).modulo();
				double angIa=new Complejo(iA.getReal(),iA.getImag()).argumento();
				
				trafo.get(i).setCorrienteFallaFaseA(magIA);trafo.get(i).setAnguloCorrienteFaseA(angIa);
				
				Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
				Complejo a= new Complejo(-0.5,0.8660254038);
				
				Complejo aCuadradoiA1= Complejo.producto(aCuadrado, iA1);
				Complejo aIA2= Complejo.producto(a, iA2);
				Complejo iB= Complejo.suma(iA0, Complejo.suma(aCuadradoiA1, aIA2));
				
				double magIB= new Complejo(iB.getReal(),iB.getImag()).modulo();
				double angIB=new Complejo(iB.getReal(),iB.getImag()).argumento();
				
				trafo.get(i).setCorrienteFallaFaseB(magIB);trafo.get(i).setAnguloCorrienteFaseB(angIB);
				
				Complejo aCuadradoiA2= Complejo.producto(aCuadrado, iA2);
				Complejo aIA1= Complejo.producto(a, iA1);
				Complejo iC= Complejo.suma(iA0, Complejo.suma(aCuadradoiA2, aIA1));
				
				double magIC= new Complejo(iC.getReal(),iC.getImag()).modulo();
				double angIC=new Complejo(iC.getReal(),iC.getImag()).argumento();
				
				trafo.get(i).setCorrienteFallaFaseC(magIC);trafo.get(i).setAnguloCorrienteFaseC(angIC);
	
			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	
	public void vectorVoltajes() {
		
		Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
		Complejo a= new Complejo(-0.5,0.8660254038);
		
		double vA1,vA2,vA0;
		Complejo vA,vB,vC;
		int indexBarraFallada= barras.indexOf(barraFallada);
		
		for (int i=0;i<matrizImpedanciaZ0.length;i++) {
			
			if(i==(indexBarraFallada-1)) {
				
				
				vA1= barraFallada.getVoltajePrefalla()-(corrientePuntoFallaSecuencia1.modulo()*matrizImpedanciaZ1[indexBarraFallada-1][indexBarraFallada-1]);
				vA2=vA1;
				vA0=vA1;

				vectorVoltajesSecuencia0.add(vA0);
				vectorVoltajesSecuencia1.add(vA1);
				vectorVoltajesSecuencia2.add(vA2);

				 vA= Complejo.suma(Complejo.suma(new Complejo(vA1,0), new Complejo(vA2,0)), new Complejo(vA0,0));
				barraFallada.setAnguloVoltajeFaseA(vA.argumento());
				barraFallada.setVoltajePosFallaFaseA(vA.modulo());
				
				 vB= Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA1,0), aCuadrado),
						 Complejo.producto(new Complejo(vA2,0), a)), new Complejo(vA0,0));
				 
				barraFallada.setAnguloVoltajeFaseB(vB.argumento());
				barraFallada.setVoltajePosFallaFaseB(vB.modulo());
				
				vC= Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA2,0), aCuadrado),
						Complejo.producto(new Complejo(vA1,0), a)), new Complejo(vA0,0));
				
				barraFallada.setAnguloVoltajeFaseC(vC.argumento());
				barraFallada.setVoltajePosFallaFaseC(vC.modulo());
				
				
			}
			else {
				
				 vA1= barras.get(i+1).getVoltajePrefalla()-(corrientePuntoFallaSecuencia1.modulo()*matrizImpedanciaZ1[i][indexBarraFallada-1]);
				 vA2=corrientePuntoFallaSecuencia2.modulo()*matrizImpedanciaZ2[i][indexBarraFallada-1];
				 vA0= corrientePuntoFallaSecuencia0.modulo()*matrizImpedanciaZ0[i][indexBarraFallada-1];
				 
				 vectorVoltajesSecuencia0.add(vA0);
				 vectorVoltajesSecuencia1.add(vA1);
				 vectorVoltajesSecuencia2.add(vA2);
				
				 vA= Complejo.suma(Complejo.suma(new Complejo(vA1,0),
						 new Complejo(vA2,0)), new Complejo(vA0,0));
				 
				 barras.get(i+1).setAnguloVoltajeFaseA(vA.argumento());
				 barras.get(i+1).setVoltajePosFallaFaseA(vA.modulo());
				 
				 vB=Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA1,0), aCuadrado),
						 Complejo.producto(new Complejo(vA2,0), a)), new Complejo(vA0,0));
				 
				 barras.get(i+1).setAnguloVoltajeFaseB(vB.argumento());
				 barras.get(i+1).setVoltajePosFallaFaseB(vB.modulo());
					
				vC= Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA2,0), aCuadrado),
						Complejo.producto(new Complejo(vA1,0), a)), new Complejo(vA0,0));
				
				barras.get(i+1).setAnguloVoltajeFaseC(vC.argumento());
				barras.get(i+1).setVoltajePosFallaFaseC(vC.modulo());

			}
			
		}
		
		
	}
	
	public void contribucionMaquina() {
		
		Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
		Complejo a= new Complejo(-0.5,0.8660254038);
		
		for (int i=0;i<generador.size();i++) {
			
			int indexBarraGe= barras.indexOf(generador.get(i).getBarra());
			
			try {
				
				Complejo iA0=Complejo.cociente(Complejo.producto(new Complejo(-1,0), 
						Complejo.polar2Cartesiano(vectorVoltajesSecuencia0.get(indexBarraGe-1), generador.get(i).getBarra().getAnguloVoltajeSecuencia0())),
						new Complejo(0,generador.get(i).getImpedanciaZ0()+generador.get(i).getImpedanciaAterrizamiento())) ;
				
				
				Complejo iA1=Complejo.cociente(Complejo.resta(new Complejo(generador.get(i).getBarra().getVoltajePrefalla(),0),
						Complejo.polar2Cartesiano(vectorVoltajesSecuencia1.get(indexBarraGe-1),generador.get(i).getBarra().getAnguloVoltajeSecuencia1() )),
						new Complejo(0,generador.get(i).getImpedanciaZ1()));
				
				Complejo iA2=Complejo.cociente(Complejo.producto(new Complejo(-1,0), 
						Complejo.polar2Cartesiano(vectorVoltajesSecuencia2.get(indexBarraGe-1), generador.get(i).getBarra().getAnguloVoltajeSecuencia2())), 
						new Complejo(0,generador.get(i).getImpedanciaZ2())) ;
				
				
				Complejo iA= Complejo.suma(Complejo.suma(iA1, iA2), iA0);
				
				generador.get(i).setCorrienteFaseA(iA.modulo());
				generador.get(i).setAnguloCorrienteFaseA(iA.argumento());
				
				Complejo iB= Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, iA1), Complejo.producto(a, iA2)), iA0);
				
				generador.get(i).setCorrienteFaseB(iB.modulo());
				generador.get(i).setAnguloCorrienteFaseB(iB.argumento());
				

				Complejo iC= Complejo.suma(Complejo.suma(Complejo.producto(a, iA1), Complejo.producto(aCuadrado, iA2)), iA0);
				
				generador.get(i).setCorrienteFaseC(iC.modulo());
				generador.get(i).setAnguloCorrienteFaseC(iC.argumento());
				
				
			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}

		
}
