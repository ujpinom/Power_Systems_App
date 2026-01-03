package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.List;

public class FallaLineaALineaLinea {

	private double[][] matrizSecuencia1;
	private double[][] matrizSecuencia2;
	private List<Barras> barras;
	private List<Lineas> lineas;
	private List<Transformador> trafo;
	private List<Generadores> generador;
	private Lineas elementoFallado;
	private CreacionZBarra zb1;
	private CreacionZBarra zb2;
	private Complejo corrientePuntoFalla1;
	private Complejo corrientePuntoFalla2;
	private List<Double> vectorVoltajesSecuencia1 = new ArrayList<>();
	private List<Double> vectorVoltajesSecuencia2 = new ArrayList<>();
	private Complejo vA1Imaginario;
	private Complejo vA2Imaginario;

	public FallaLineaALineaLinea(double[][] matrizSecuencia1, double[][] matrizSecuencia2, List<Barras> barras,
			List<Lineas> lineas, List<Transformador> trafo,
			List<Generadores> generador, Lineas elementoFallado) {

		this.barras = barras;
		this.elementoFallado = elementoFallado;
		this.lineas = lineas;
		this.generador = generador;
		this.trafo = trafo;

		zb1 = new CreacionZBarra(barras, matrizSecuencia1, elementoFallado);
		zb2 = new CreacionZBarra(barras, matrizSecuencia2, elementoFallado);

		double[][] matrizInserccionBarra1 = zb1.inserccionABarraExistente(matrizSecuencia1);
		double[][] matrizInserccionBarra2 = zb2.inserccionABarraExistente2(matrizSecuencia2);

		double[][] matrizInserccionEntreDosBarras1 = zb1.inserccionEntreBarras(matrizInserccionBarra1);
		double[][] matrizInserccionEntreDosBarras2 = zb2.inserccionEntreBarras2(matrizInserccionBarra2);

		double[][] zBarrafinal1 = zb1.inserccionEntreBarras(matrizInserccionEntreDosBarras1,
				barras.indexOf(elementoFallado.getBarra1()),
				barras.indexOf(elementoFallado.getBarra2()));

		double[][] zBarrafinal2 = zb2.inserccionEntreBarras2(matrizInserccionEntreDosBarras2,
				barras.indexOf(elementoFallado.getBarra1()),
				barras.indexOf(elementoFallado.getBarra2()));

		this.matrizSecuencia1 = zBarrafinal1;
		this.matrizSecuencia2 = zBarrafinal2;

		calculoFallas();

	}

	public void calculoFallas() {

		calculoCorrientePuntoFalla();
		setConexionesBarras();
		setAngulosBarras();
		vectoresVoltaje();
		contribuccionMaquinas();
		corrientesLinea();
		contribucionesBarras();
	}

	public void calculoCorrientePuntoFalla() {

		double impedanciaTotal = matrizSecuencia1[matrizSecuencia1.length - 1][matrizSecuencia1.length - 1] +
				matrizSecuencia2[matrizSecuencia2.length - 1][matrizSecuencia2.length - 1]
				+ 3 * elementoFallado.getImpedanciaFalla();

		try {

			corrientePuntoFalla1 = Complejo.cociente(new Complejo(elementoFallado.getTensionLineaPuntoFalla(), 0),
					new Complejo(0, impedanciaTotal));

			corrientePuntoFalla2 = Complejo.producto(new Complejo(-1, 0), corrientePuntoFalla1);

			elementoFallado.setAngCorrientePuntoFallaFaseA(0);
			elementoFallado.setMagcorrientePuntoFallaFaseA(0);

			Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
			Complejo a = new Complejo(-0.5, 0.8660254038);

			Complejo iB = Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFalla1),
					Complejo.producto(a, corrientePuntoFalla2));

			elementoFallado.setAngCorrientePuntoFallaFaseB(iB.argumento());
			elementoFallado.setMagcorrientePuntoFallaFaseB(iB.modulo());

			Complejo iC = Complejo.producto(new Complejo(-1, 0), iB);

			elementoFallado.setAngCorrientePuntoFallaFaseC(iC.argumento());
			elementoFallado.setMagcorrientePuntoFallaFaseC(iC.modulo());

		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void contribuccionMaquinas() {

		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		for (int i = 0; i < generador.size(); i++) {

			int indexBarraGe = barras.indexOf(generador.get(i).getBarra());

			try {
				Complejo iA1 = Complejo.cociente(
						Complejo.resta(new Complejo(generador.get(i).getBarra().getVoltajePrefalla(), 0),
								Complejo.polar2Cartesiano(vectorVoltajesSecuencia1.get(indexBarraGe - 1),
										generador.get(i).getBarra().getAnguloVoltajeSecuencia1())),
						new Complejo(0, generador.get(i).getImpedanciaZ1()));

				Complejo iA2 = Complejo.cociente(
						Complejo.producto(new Complejo(-1, 0),
								Complejo.polar2Cartesiano(vectorVoltajesSecuencia2.get(indexBarraGe - 1),
										generador.get(i).getBarra().getAnguloVoltajeSecuencia2())),
						new Complejo(0, generador.get(i).getImpedanciaZ2()));

				Complejo iA = Complejo.suma(iA1, iA2);

				generador.get(i).setCorrienteFaseA(iA.modulo());
				generador.get(i).setAnguloCorrienteFaseA(iA.argumento());

				Complejo iB = Complejo.suma(Complejo.producto(aCuadrado, iA1), Complejo.producto(a, iA2));

				generador.get(i).setCorrienteFaseB(iB.modulo());
				generador.get(i).setAnguloCorrienteFaseB(iB.argumento());

				Complejo iC = Complejo.suma(Complejo.producto(a, iA1), Complejo.producto(aCuadrado, iA2));

				generador.get(i).setCorrienteFaseC(iC.modulo());
				generador.get(i).setAnguloCorrienteFaseC(iC.argumento());

			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void vectoresVoltaje() {

		vA1Imaginario = Complejo.resta(new Complejo(elementoFallado.getTensionLineaPuntoFalla(), 0),
				Complejo.producto(
						new Complejo(0, matrizSecuencia1[matrizSecuencia1.length - 1][matrizSecuencia1.length - 1]),
						corrientePuntoFalla1));

		vA2Imaginario = Complejo.producto(new Complejo(-1, 0),
				Complejo.producto(
						new Complejo(0, matrizSecuencia2[matrizSecuencia1.length - 1][matrizSecuencia1.length - 1]),
						corrientePuntoFalla2));

		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		for (int i = 1; i < barras.size(); i++) {

			double vA1 = barras.get(i).getVoltajePrefalla()
					- matrizSecuencia1[matrizSecuencia1.length - 1][i - 1] * corrientePuntoFalla1.modulo();

			double vA2 = matrizSecuencia2[matrizSecuencia1.length - 1][i - 1] * corrientePuntoFalla2.modulo();

			vectorVoltajesSecuencia1.add(vA1);
			vectorVoltajesSecuencia2.add(vA2);

			Complejo vASecuencia1 = Complejo.polar2Cartesiano(vA1, barras.get(i).getAnguloVoltajeSecuencia1());
			Complejo vASecuencia2 = Complejo.polar2Cartesiano(vA2, barras.get(i).getAnguloVoltajeSecuencia2());

			Complejo vFaseA = Complejo.suma(vASecuencia1, vASecuencia2);

			barras.get(i).setAnguloVoltajeFaseA(vFaseA.argumento());
			barras.get(i).setVoltajePosFallaFaseA(vFaseA.modulo());

			Complejo vFaseB = Complejo.suma(Complejo.producto(aCuadrado, vASecuencia1),
					Complejo.producto(a, vASecuencia2));

			barras.get(i).setAnguloVoltajeFaseB(vFaseB.argumento());
			barras.get(i).setVoltajePosFallaFaseB(vFaseB.modulo());

			Complejo vFaseC = Complejo.suma(Complejo.producto(a, vASecuencia1),
					Complejo.producto(aCuadrado, vASecuencia2));

			barras.get(i).setAnguloVoltajeFaseC(vFaseC.argumento());
			barras.get(i).setVoltajePosFallaFaseC(vFaseC.modulo());

		}

	}

	public void corrientesLinea() {

		for (int i = 0; i < lineas.size(); i++) {

			int barra1 = barras.indexOf(lineas.get(i).getBarra1());
			int barra2 = barras.indexOf(lineas.get(i).getBarra2());

			int minimo = Math.min(barra1, barra2);
			int maximo = Math.max(barra2, barra1);

			try {

				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia1.get(minimo - 1) - vectorVoltajesSecuencia1.get(maximo - 1)), 0),
						new Complejo(0, lineas.get(i).getimpedanciaLineaZ1()));

				Complejo iA2 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia2.get(minimo - 1) - vectorVoltajesSecuencia2.get(maximo - 1)), 0),
						new Complejo(0, lineas.get(i).getimpedanciaLineaZ2()));

				Complejo iA = Complejo.suma(iA1, iA2);

				double magIA = new Complejo(iA.getReal(), iA.getImag()).modulo();
				double angIa = new Complejo(iA.getReal(), iA.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseA(magIA);
				lineas.get(i).setAnguloCorrienteFaseA(angIa);

				Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
				Complejo a = new Complejo(-0.5, 0.8660254038);

				Complejo aCuadradoiA1 = Complejo.producto(aCuadrado, iA1);
				Complejo aIA2 = Complejo.producto(a, iA2);
				Complejo iB = Complejo.suma(aCuadradoiA1, aIA2);

				double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
				double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseB(magIB);
				lineas.get(i).setAnguloCorrienteFaseB(angIB);

				Complejo aCuadradoiA2 = Complejo.producto(aCuadrado, iA2);
				Complejo aIA1 = Complejo.producto(a, iA1);
				Complejo iC = Complejo.suma(aCuadradoiA2, aIA1);

				double magIC = new Complejo(iC.getReal(), iC.getImag()).modulo();
				double angIC = new Complejo(iC.getReal(), iC.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseC(magIC);
				lineas.get(i).setAnguloCorrienteFaseC(angIC);

			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		for (int i = 0; i < trafo.size(); i++) {

			int barra1 = barras.indexOf(trafo.get(i).getBarra1());
			int barra2 = barras.indexOf(trafo.get(i).getBarra2());

			int minimo = Math.min(barra1, barra2);
			int maximo = Math.max(barra2, barra1);

			try {

				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia1.get(minimo - 1) - vectorVoltajesSecuencia1.get(maximo - 1)), 0),
						new Complejo(0, trafo.get(i).getimpedanciaLineaZ1()));

				Complejo iA2 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia2.get(minimo - 1) - vectorVoltajesSecuencia2.get(maximo - 1)), 0),
						new Complejo(0, trafo.get(i).getimpedanciaLineaZ2()));

				Complejo iA = Complejo.suma(iA1, iA2);

				double magIA = new Complejo(iA.getReal(), iA.getImag()).modulo();
				double angIa = new Complejo(iA.getReal(), iA.getImag()).argumento();

				trafo.get(i).setCorrienteFallaFaseA(magIA);
				trafo.get(i).setAnguloCorrienteFaseA(angIa);

				Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
				Complejo a = new Complejo(-0.5, 0.8660254038);

				Complejo aCuadradoiA1 = Complejo.producto(aCuadrado, iA1);
				Complejo aIA2 = Complejo.producto(a, iA2);
				Complejo iB = Complejo.suma(aCuadradoiA1, aIA2);

				double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
				double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

				trafo.get(i).setCorrienteFallaFaseB(magIB);
				trafo.get(i).setAnguloCorrienteFaseB(angIB);

				Complejo aCuadradoiA2 = Complejo.producto(aCuadrado, iA2);
				Complejo aIA1 = Complejo.producto(a, iA1);
				Complejo iC = Complejo.suma(aCuadradoiA2, aIA1);

				double magIC = new Complejo(iC.getReal(), iC.getImag()).modulo();
				double angIC = new Complejo(iC.getReal(), iC.getImag()).argumento();

				trafo.get(i).setCorrienteFallaFaseC(magIC);
				trafo.get(i).setAnguloCorrienteFaseC(angIC);

			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void contribucionesBarras() {

		for (int i = 0; i < barras.size(); i++) {

			if (elementoFallado.getBarra1() == barras.get(i) || elementoFallado.getBarra2() == barras.get(i)) {

				try {

					Complejo iA2 = Complejo.cociente(
							new Complejo(vectorVoltajesSecuencia2.get(i - 1) - vA2Imaginario.modulo(), 0),
							new Complejo(0, elementoFallado.getimpedanciaLineaZ2() / 2));

					Complejo iA1 = Complejo.cociente(
							new Complejo(vectorVoltajesSecuencia1.get(i - 1) - vA1Imaginario.modulo(), 0),
							new Complejo(0, elementoFallado.getimpedanciaLineaZ1() / 2));

					Complejo iA = Complejo.suma(iA2, iA1);

					double magIA = iA.modulo();
					double angIA = iA.argumento();

					barras.get(i).setContribuccionFallaFaseA(magIA);
					barras.get(i).setAnguloContribucionFaseA(angIA);

					Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
					Complejo a = new Complejo(-0.5, 0.8660254038);

					Complejo Acuadrado = Complejo.producto(aCuadrado, iA1);
					Complejo A = Complejo.producto(a, iA2);

					Complejo iB = Complejo.suma(Acuadrado, A);

					double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
					double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

					barras.get(i).setContribuccionFallaFaseB(magIB);
					barras.get(i).setAnguloContribucionFaseB(angIB);

					A = Complejo.producto(a, iA1);
					Acuadrado = Complejo.producto(aCuadrado, iA2);

					Complejo iC = Complejo.suma(A, Acuadrado);

					double magIC = new Complejo(iC.getReal(), iC.getImag()).modulo();
					double angIC = new Complejo(iC.getReal(), iC.getImag()).argumento();

					barras.get(i).setContribuccionFallaFaseC(magIC);
					barras.get(i).setAnguloContribucionFaseC(angIC);

				} catch (ExcepcionDivideCero e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public void setAngulosBarras() {

		// AngulosDesfazamiento.establecerAngulosDeDesfazamiento(barras, barraFallada,
		// conexionesBarras, adyacencias);

	}

	public void setConexionesBarras() {

		// this.conexionesBarras=ConexionesBarras.establecerConexionesBarras(trafo,
		// barras);

	}

}
