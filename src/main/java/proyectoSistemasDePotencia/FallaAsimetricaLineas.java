package proyectoSistemasDePotencia;

import java.util.ArrayList;

public class FallaAsimetricaLineas {
	
	
	private double[][] matrizSecuencia0;
	private double[][] matrizSecuencia1;
	private double[][] matrizSecuencia2;
	private ArrayList<Barras> barras;
	private ArrayList<Lineas> lineas;
	private ArrayList<Transformador> trafo;
	private ArrayList<Generadores> generador;
	private Lineas elementoFallado;
	private CreacionZBarra zb1;
	private CreacionZBarra zb0;
	private CreacionZBarra zb2;
	private double corrienteFalla;
	private ArrayList<Double> vectorVoltajesSecuencia0= new ArrayList<>();
	private ArrayList<Double> vectorVoltajesSecuencia1= new ArrayList<>();
	private ArrayList<Double> vectorVoltajesSecuencia2= new ArrayList<>();
	private double vA0Imaginario;
	private double vA1Imaginario;
	private double vA2Imaginario;
	
	
	public FallaAsimetricaLineas(double[][] matrizSecuencia0,double[][] matrizSecuencia1,double[][] matrizSecuencia2,
			ArrayList<Barras> barras,ArrayList<Lineas> lineas,ArrayList<Transformador> trafo,
			ArrayList<Generadores> generador,Lineas elementoFallado) {
		
		this.barras=barras;
		this.matrizSecuencia0=matrizSecuencia0;
		this.matrizSecuencia1=matrizSecuencia1;
		this.matrizSecuencia2=matrizSecuencia2;
		this.lineas=lineas;
		this.trafo=trafo;
		this.generador=generador;
		this.elementoFallado=elementoFallado;
		
		zb1= new CreacionZBarra(barras, matrizSecuencia1, elementoFallado);
		zb0= new CreacionZBarra(barras, matrizSecuencia0, elementoFallado);
		zb2= new CreacionZBarra(barras, matrizSecuencia2, elementoFallado);
		
		double [][] matrizInserccionBarra1=zb1.inserccionABarraExistente( matrizSecuencia1);
		double [][] matrizInserccionBarra2=zb2.inserccionABarraExistente2( matrizSecuencia2);
		double [][] matrizInserccionBarra0=zb0.inserccionABarraExistente0( matrizSecuencia0);
		
		double [][] matrizInserccionEntreDosBarras1=zb1.inserccionEntreBarras(matrizInserccionBarra1);
		double [][] matrizInserccionEntreDosBarras2=zb2.inserccionEntreBarras2(matrizInserccionBarra2);
		double [][] matrizInserccionEntreDosBarras0=zb0.inserccionEntreBarras0(matrizInserccionBarra0);
		
		double [][]zBarrafinal1 = zb1.inserccionEntreBarras(matrizInserccionEntreDosBarras1, barras.indexOf(elementoFallado.getBarra1()),
				barras.indexOf(elementoFallado.getBarra2()));
		
		double [][]zBarrafinal2 = zb2.inserccionEntreBarras2(matrizInserccionEntreDosBarras2, barras.indexOf(elementoFallado.getBarra1()),
				barras.indexOf(elementoFallado.getBarra2()));
		
		double [][]zBarrafinal0 = zb0.inserccionEntreBarras0(matrizInserccionEntreDosBarras0, barras.indexOf(elementoFallado.getBarra1()),
				barras.indexOf(elementoFallado.getBarra2()));
		
		this.matrizSecuencia0=zBarrafinal0;this.matrizSecuencia1=zBarrafinal1;this.matrizSecuencia2=zBarrafinal2;
		
		calculoFallaAsimetricaLinea();
		
	}
	
	
	public void calculoFallaAsimetricaLinea() {
		
		calculoCorrienteFalla();
		calculoVectorVoltajes();
		calculoCorrientesFalla();
		setContribuciones();
	}
	
	
	public void setContribuciones() {
		
		for (int i=0;i<barras.size();i++) {
		
			if(elementoFallado.getBarra1()==barras.get(i)||elementoFallado.getBarra2()==barras.get(i) ) {
				
				
				try {
					
					Complejo iA0=Complejo.cociente(new Complejo(vectorVoltajesSecuencia0.get(i-1)-vA0Imaginario,0),
							new Complejo(0,elementoFallado.getimpedanciaLineaZ0()/2));
					
					Complejo iA2=Complejo.cociente(new Complejo(vectorVoltajesSecuencia2.get(i-1)-vA2Imaginario,0),
							new Complejo(0,elementoFallado.getimpedanciaLineaZ2()/2));
					
					Complejo iA1= Complejo.cociente(new Complejo(vectorVoltajesSecuencia1.get(i-1)-vA1Imaginario,0), 
							new Complejo(0,elementoFallado.getimpedanciaLineaZ1()/2));
					
					Complejo iA=Complejo.suma(Complejo.suma(iA2, iA1), iA0);
					
					double magIA=iA.modulo();double angIA=iA.argumento();
					
					barras.get(i).setContribuccionFallaFaseA(magIA);
					barras.get(i).setAnguloContribucionFaseA(angIA);
					
					
					Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
					Complejo a= new Complejo(-0.5,0.8660254038);
					
					Complejo Acuadrado= Complejo.producto(aCuadrado, iA1);
					Complejo A= Complejo.producto(a, iA2);
					
					Complejo iB=Complejo.suma(Complejo.suma(Acuadrado, A), iA0);
					
					double magIB=new Complejo(iB.getReal(),iB.getImag()).modulo();
					double angIB= new Complejo(iB.getReal(),iB.getImag()).argumento();
					
					barras.get(i).setContribuccionFallaFaseB(magIB);
					barras.get(i).setAnguloContribucionFaseB(angIB);
					
					
					A=Complejo.producto(a, iA1);
					Acuadrado=Complejo.producto(aCuadrado, iA2);
					
					Complejo iC= Complejo.suma(Complejo.suma(A, Acuadrado), iA0);
					
					double magIC=new Complejo(iC.getReal(),iC.getImag()).modulo();
					double angIC= new Complejo(iC.getReal(),iC.getImag()).argumento();
					
					barras.get(i).setContribuccionFallaFaseC(magIC);
					barras.get(i).setAnguloContribucionFaseC(angIC);
		
					
				} catch (ExcepcionDivideCero e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		
	}
	
	
	public void calculoCorrienteFalla() {
		
		double corrienteFalla=elementoFallado.getTensionLineaPuntoFalla()/
				(matrizSecuencia0[matrizSecuencia0.length-1][matrizSecuencia0.length-1]+
				matrizSecuencia1[matrizSecuencia1.length-1][matrizSecuencia1.length-1]+
				matrizSecuencia2[matrizSecuencia2.length-1][matrizSecuencia2.length-1]+3*elementoFallado.getImpedanciaFalla());
		
		elementoFallado.setAngCorrientePuntoFallaFaseA(-90);
		elementoFallado.setAngCorrientePuntoFallaFaseB(0);
		elementoFallado.setAngCorrientePuntoFallaFaseC(0);
		elementoFallado.setMagcorrientePuntoFallaFaseA(3*corrienteFalla);
		elementoFallado.setMagcorrientePuntoFallaFaseB(0);
		elementoFallado.setMagcorrientePuntoFallaFaseC(0);
		
		this.corrienteFalla=(-1)*corrienteFalla;
		
	}
	
	public double getCorrientePuntoFallaTotal() {
		
		return corrienteFalla*3;
	}
	
	public void calculoVectorVoltajes() {
		
		vA0Imaginario=matrizSecuencia0[matrizSecuencia0.length-1][matrizSecuencia0.length-1]*corrienteFalla;
		vA1Imaginario=elementoFallado.getTensionLineaPuntoFalla()+matrizSecuencia1[matrizSecuencia0.length-1][matrizSecuencia0.length-1]*corrienteFalla;
		vA2Imaginario=matrizSecuencia2[matrizSecuencia0.length-1][matrizSecuencia0.length-1]*corrienteFalla;
		
		
		
		for(int i=0;i<matrizSecuencia1.length-1;i++) {
			
			double vA0= matrizSecuencia0[i][matrizSecuencia0.length-1]*corrienteFalla;
			vectorVoltajesSecuencia0.add(vA0);
			
			double vA2=matrizSecuencia2[i][matrizSecuencia2.length-1]*corrienteFalla;
			vectorVoltajesSecuencia2.add(vA2);
			
			double vA1= barras.get(i+1).getVoltajePrefalla()+matrizSecuencia1[i][matrizSecuencia1.length-1]*corrienteFalla;
			vectorVoltajesSecuencia1.add(vA1);
			
			double VFaseA=vA0+vA1+vA2;
			
			barras.get(i+1).setVoltajePosFallaFaseA(VFaseA);
			barras.get(i+1).setAnguloVoltajeFaseA(0);
			
			Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
			Complejo a= new Complejo(-0.5,0.8660254038);
			
			Complejo vA1aCuadrado= Complejo.producto(aCuadrado, vA1);
			Complejo vA2a=Complejo.producto(a, vA2);
			
			Complejo sumavA1aCuadradovA2a= Complejo.suma(vA1aCuadrado, vA2a);
			Complejo ResultadoFaseB= Complejo.suma(sumavA1aCuadradovA2a, new Complejo(vA0,0));
			
			double magFaseB=new Complejo(ResultadoFaseB.getReal(),ResultadoFaseB.getImag()).modulo();
			double angFaseB=new Complejo(ResultadoFaseB.getReal(),ResultadoFaseB.getImag()).argumento();
			
			barras.get(i+1).setVoltajePosFallaFaseB(magFaseB);
			barras.get(i+1).setAnguloVoltajeFaseB(angFaseB);
			
			 vA1aCuadrado=Complejo.producto(a, vA1);
			 vA2a=Complejo.producto(aCuadrado, vA2);
			 sumavA1aCuadradovA2a= Complejo.suma(vA1aCuadrado, vA2a);
			 Complejo resultadoFaseC=Complejo.suma(sumavA1aCuadradovA2a, new Complejo(vA0,0));
			 
			 double magFaseC=new Complejo(resultadoFaseC.getReal(),resultadoFaseC.getImag()).modulo();
			 double angFaseC=new Complejo(resultadoFaseC.getReal(),resultadoFaseC.getImag()).argumento();
			 
			 barras.get(i+1).setVoltajePosFallaFaseC(magFaseC);
			barras.get(i+1).setAnguloVoltajeFaseC(angFaseC);
			 
		}
		
		
		
	}
	
public void calculoCorrientesFalla()  {
		
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
		
		for(int i=0;i<trafo.size();i++) {
			Complejo iA0;
			
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
				
				Complejo aCuadradoiA1= Complejo.producto(aCuadrado, iA1);Complejo aIA2= Complejo.producto(a, iA2);
				Complejo iB= Complejo.suma(iA0, Complejo.suma(aCuadradoiA1, aIA2));
				
				double magIB= new Complejo(iB.getReal(),iB.getImag()).modulo();
				double angIB=new Complejo(iB.getReal(),iB.getImag()).argumento();
				
				trafo.get(i).setCorrienteFallaFaseB(magIB);trafo.get(i).setAnguloCorrienteFaseB(angIB);
				
				Complejo aCuadradoiA2= Complejo.producto(aCuadrado, iA2);Complejo aIA1= Complejo.producto(a, iA1);
				Complejo iC= Complejo.suma(iA0, Complejo.suma(aCuadradoiA2, aIA1));
				
				double magIC= new Complejo(iC.getReal(),iC.getImag()).modulo();
				double angIC=new Complejo(iC.getReal(),iC.getImag()).argumento();
				
				trafo.get(i).setCorrienteFallaFaseC(magIC);trafo.get(i).setAnguloCorrienteFaseC(angIC);
				
			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		for(int i=0;i<generador.size();i++) {
			
			
			int indexBarraGe= barras.indexOf(generador.get(i).getBarra());
		
			
			double iA0=  vectorVoltajesSecuencia0.get(indexBarraGe-1)/(generador.get(i).getImpedanciaZ0()+generador.get(i).getImpedanciaAterrizamiento());
			double iA1=(-1)*(generador.get(i).getBarra().getVoltajePrefalla()-vectorVoltajesSecuencia1.get(indexBarraGe-1))/generador.get(i).getImpedanciaZ1();
			double iA2=  vectorVoltajesSecuencia2.get(indexBarraGe-1)/generador.get(i).getImpedanciaZ2();
			
			double corrinteFaseA=iA0+iA1+iA2;
			
			generador.get(i).setCorrienteFaseA((-1)*corrinteFaseA);
			generador.get(i).setAnguloCorrienteFaseA(-90);
			
			Complejo aCuadrado= new Complejo(-0.5,-0.8660254038);
			Complejo a= new Complejo(-0.5,0.8660254038);
			
			
			Complejo acuadradoIa= Complejo.producto(aCuadrado, new Complejo(0,iA1));
			Complejo aIa= Complejo.producto(a, new Complejo(0,iA2));
			
			Complejo sumaacuadradoIaaIa= Complejo.suma(acuadradoIa, aIa);
			Complejo sumaFinal= Complejo.suma(sumaacuadradoIaaIa,new Complejo(0,iA0) );
			
			double magCorrienteFaseB= new Complejo(sumaFinal.getReal(),sumaFinal.getImag()).modulo();
			double angCorrienteFaseB= new Complejo(sumaFinal.getReal(),sumaFinal.getImag()).argumento();
			
			generador.get(i).setCorrienteFaseB(magCorrienteFaseB);
			generador.get(i).setAnguloCorrienteFaseB(angCorrienteFaseB);
			
			acuadradoIa=Complejo.producto(aCuadrado, new Complejo(0,iA2));
			aIa= Complejo.producto(a, new Complejo(0,iA1));
			 
			sumaacuadradoIaaIa= Complejo.suma(acuadradoIa, aIa);
			sumaFinal= Complejo.suma(sumaacuadradoIaaIa,new Complejo(0,iA0) );
			 
			double magCorrienteFaseC= new Complejo(sumaFinal.getReal(),sumaFinal.getImag()).modulo();
			double angCorrienteFaseC= new Complejo(sumaFinal.getReal(),sumaFinal.getImag()).argumento();
			
			generador.get(i).setCorrienteFaseC(magCorrienteFaseC);
			generador.get(i).setAnguloCorrienteFaseC(angCorrienteFaseC);
				
		}	
	}
	

	public void imprimir(double [][] resultado){
		
		for(int i=0;i<resultado.length;i++) {
			for(int j=0;j<resultado.length;j++) {
				
				System.out.print(resultado[i][j]+" ");
			}
			System.out.println();
		}

	}

}
