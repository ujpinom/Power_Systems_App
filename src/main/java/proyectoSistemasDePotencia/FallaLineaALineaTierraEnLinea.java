package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.List;

public class FallaLineaALineaTierraEnLinea {

	private double[][] matrizImpedanciaZ0;
	private double[][] matrizImpedanciaZ1;
	private double[][] matrizImpedanciaZ2;
	private List<Barras> barras;
	private List<Lineas> lineas;
	private List<Transformador> trafo;
	private List<Generadores> generador;
	private Lineas elementoFallado;
	private CreacionZBarra zb1;
	private CreacionZBarra zb0;
	private CreacionZBarra zb2;
	private Complejo corrientePuntoFallaSecuencia1;
	private Complejo corrientePuntoFallaSecuencia2;
	private Complejo corrientePuntoFallaSecuencia0;
	private List<Double> vectorVoltajesSecuencia0 = new ArrayList<>();
	private List<Double> vectorVoltajesSecuencia1 = new ArrayList<>();
	private List<Double> vectorVoltajesSecuencia2 = new ArrayList<>();
	private Complejo vA0Imaginario;
	private Complejo vA1Imaginario;
	private Complejo vA2Imaginario;

	public FallaLineaALineaTierraEnLinea(double[][] matrizSecuencia0, double[][] matrizSecuencia1,
			double[][] matrizSecuencia2, List<Barras> barras, List<Lineas> lineas,
			List<Transformador> trafo, List<Generadores> generador, Lineas elementoFallado) {

		super();
		this.matrizImpedanciaZ0 = matrizSecuencia0;
		this.matrizImpedanciaZ1 = matrizSecuencia1;
		this.matrizImpedanciaZ2 = matrizSecuencia2;
		this.barras = barras;
		this.lineas = lineas;
		this.trafo = trafo;
		this.generador = generador;
		this.elementoFallado = elementoFallado;

		zb1 = new CreacionZBarra(barras, matrizSecuencia1, elementoFallado);
		zb0 = new CreacionZBarra(barras, matrizSecuencia0, elementoFallado);
		zb2 = new CreacionZBarra(barras, matrizSecuencia2, elementoFallado);

		double[][] matrizInserccionBarra1 = zb1.inserccionABarraExistente(matrizSecuencia1);
		double[][] matrizInserccionBarra2 = zb2.inserccionABarraExistente2(matrizSecuencia2);
		double[][] matrizInserccionBarra0 = zb0.inserccionABarraExistente0(matrizSecuencia0);

		double[][] matrizInserccionEntreDosBarras1 = zb1.inserccionEntreBarras(matrizInserccionBarra1);
		double[][] matrizInserccionEntreDosBarras2 = zb2.inserccionEntreBarras2(matrizInserccionBarra2);
		double[][] matrizInserccionEntreDosBarras0 = zb0.inserccionEntreBarras0(matrizInserccionBarra0);

		double[][] zBarrafinal1 = zb1.inserccionEntreBarras(matrizInserccionEntreDosBarras1,
				barras.indexOf(elementoFallado.getBarra1()), barras.indexOf(elementoFallado.getBarra2()));

		double[][] zBarrafinal2 = zb2.inserccionEntreBarras2(matrizInserccionEntreDosBarras2,
				barras.indexOf(elementoFallado.getBarra1()), barras.indexOf(elementoFallado.getBarra2()));

		double[][] zBarrafinal0 = zb0.inserccionEntreBarras0(matrizInserccionEntreDosBarras0,
				barras.indexOf(elementoFallado.getBarra1()), barras.indexOf(elementoFallado.getBarra2()));

		this.matrizImpedanciaZ0 = zBarrafinal0;
		this.matrizImpedanciaZ1 = zBarrafinal1;
		this.matrizImpedanciaZ2 = zBarrafinal2;

		calculoFallas();

	}

	public void calculoFallas() {

		corrientePuntoFalla();
		vectorVoltajes();
		contribucionMaquina();
		corrienteLineas();
		contribucionBarras();

	}

	public void contribucionBarras() {

		for (int i = 0; i < barras.size(); i++) {

			if (elementoFallado.getBarra1() == barras.get(i) || elementoFallado.getBarra2() == barras.get(i)) {

				try {

					Complejo iA0 = Complejo.cociente(
							new Complejo(vectorVoltajesSecuencia0.get(i - 1) - vA0Imaginario.modulo(), 0),
							new Complejo(0, elementoFallado.getimpedanciaLineaZ0() / 2));

					Complejo iA2 = Complejo.cociente(
							new Complejo(vectorVoltajesSecuencia2.get(i - 1) - vA2Imaginario.modulo(), 0),
							new Complejo(0, elementoFallado.getimpedanciaLineaZ2() / 2));

					Complejo iA1 = Complejo.cociente(
							new Complejo(vectorVoltajesSecuencia1.get(i - 1) - vA1Imaginario.modulo(), 0),
							new Complejo(0, elementoFallado.getimpedanciaLineaZ1() / 2));

					Complejo iA = Complejo.suma(Complejo.suma(iA2, iA1), iA0);
					double magIA = iA.modulo();
					double angIA = iA.argumento();

					barras.get(i).setContribuccionFallaFaseA(magIA);
					barras.get(i).setAnguloContribucionFaseA(angIA);

					Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
					Complejo a = new Complejo(-0.5, 0.8660254038);

					Complejo Acuadrado = Complejo.producto(aCuadrado, iA1);
					Complejo A = Complejo.producto(a, iA2);

					Complejo iB = Complejo.suma(Complejo.suma(Acuadrado, A), iA0);

					double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
					double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

					barras.get(i).setContribuccionFallaFaseB(magIB);
					barras.get(i).setAnguloContribucionFaseB(angIB);

					A = Complejo.producto(a, iA1);
					Acuadrado = Complejo.producto(aCuadrado, iA2);

					Complejo iC = Complejo.suma(Complejo.suma(A, Acuadrado), iA0);

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

	public void contribucionMaquina() {

		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		for (int i = 0; i < generador.size(); i++) {

			int indexBarraGe = barras.indexOf(generador.get(i).getBarra());

			try {

				Complejo iA0 = Complejo.cociente(Complejo.producto(new Complejo(-1, 0),
						Complejo.polar2Cartesiano(vectorVoltajesSecuencia0.get(indexBarraGe - 1),
								generador.get(i).getBarra().getAnguloVoltajeSecuencia0())),
						new Complejo(0,
								generador.get(i).getImpedanciaZ0() + generador.get(i).getImpedanciaAterrizamiento()));

				Complejo iA1 = Complejo.cociente(
						Complejo.resta(new Complejo(generador.get(i).getBarra().getVoltajePrefalla(), 0),
								Complejo.polar2Cartesiano(vectorVoltajesSecuencia1.get(indexBarraGe - 1),
										generador.get(i).getBarra().getAnguloVoltajeSecuencia1())),
						new Complejo(0, generador.get(i).getImpedanciaZ1()));

				Complejo iA2 = Complejo.cociente(Complejo.producto(new Complejo(-1, 0),
						Complejo.polar2Cartesiano(vectorVoltajesSecuencia2.get(indexBarraGe - 1),
								generador.get(i).getBarra().getAnguloVoltajeSecuencia2())),
						new Complejo(0, generador.get(i).getImpedanciaZ2()));

				Complejo iA = Complejo.suma(Complejo.suma(iA1, iA2), iA0);

				generador.get(i).setCorrienteFaseA(iA.modulo());
				generador.get(i).setAnguloCorrienteFaseA(iA.argumento());

				Complejo iB = Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, iA1), Complejo.producto(a, iA2)),
						iA0);

				generador.get(i).setCorrienteFaseB(iB.modulo());
				generador.get(i).setAnguloCorrienteFaseB(iB.argumento());

				Complejo iC = Complejo.suma(Complejo.suma(Complejo.producto(a, iA1), Complejo.producto(aCuadrado, iA2)),
						iA0);

				generador.get(i).setCorrienteFaseC(iC.modulo());
				generador.get(i).setAnguloCorrienteFaseC(iC.argumento());

			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void corrienteLineas() {

		for (int i = 0; i < lineas.size(); i++) {

			int barra1 = barras.indexOf(lineas.get(i).getBarra1());
			int barra2 = barras.indexOf(lineas.get(i).getBarra2());

			int minimo = Math.min(barra1, barra2);
			int maximo = Math.max(barra2, barra1);

			try {

				Complejo iA0 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia0.get(minimo - 1) - vectorVoltajesSecuencia0.get(maximo - 1)), 0),
						new Complejo(0, lineas.get(i).getimpedanciaLineaZ0()));

				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia1.get(minimo - 1) - vectorVoltajesSecuencia1.get(maximo - 1)), 0),
						new Complejo(0, lineas.get(i).getimpedanciaLineaZ1()));

				Complejo iA2 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia2.get(minimo - 1) - vectorVoltajesSecuencia2.get(maximo - 1)), 0),
						new Complejo(0, lineas.get(i).getimpedanciaLineaZ2()));

				Complejo iA = Complejo.suma(Complejo.suma(iA1, iA2), iA0);

				double magIA = new Complejo(iA.getReal(), iA.getImag()).modulo();
				double angIa = new Complejo(iA.getReal(), iA.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseA(magIA);
				lineas.get(i).setAnguloCorrienteFaseA(angIa);

				Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
				Complejo a = new Complejo(-0.5, 0.8660254038);

				Complejo aCuadradoiA1 = Complejo.producto(aCuadrado, iA1);
				Complejo aIA2 = Complejo.producto(a, iA2);

				Complejo iB = Complejo.suma(iA0, Complejo.suma(aCuadradoiA1, aIA2));

				double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
				double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseB(magIB);
				lineas.get(i).setAnguloCorrienteFaseB(angIB);

				Complejo aCuadradoiA2 = Complejo.producto(aCuadrado, iA2);
				Complejo aIA1 = Complejo.producto(a, iA1);

				Complejo iC = Complejo.suma(iA0, Complejo.suma(aCuadradoiA2, aIA1));

				double magIC = new Complejo(iC.getReal(), iC.getImag()).modulo();
				double angIC = new Complejo(iC.getReal(), iC.getImag()).argumento();

				lineas.get(i).setCorrienteFallaFaseC(magIC);
				lineas.get(i).setAnguloCorrienteFaseC(angIC);

			} catch (ExcepcionDivideCero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Complejo iA0;

		for (int i = 0; i < trafo.size(); i++) {

			int barra1 = barras.indexOf(trafo.get(i).getBarra1());
			int barra2 = barras.indexOf(trafo.get(i).getBarra2());

			int minimo = Math.min(barra1, barra2);
			int maximo = Math.max(barra2, barra1);

			try {

				if ((trafo.get(i).getConexionPrimaria().contains("Y-")
						|| trafo.get(i).getConexionSecundaria().contains("Y-")) ||
						(trafo.get(i).getConexionPrimaria().contains("DELTA")
								&& trafo.get(i).getConexionSecundaria().contains("DELTA"))) {

					iA0 = new Complejo(0, 0);
				} else {

					iA0 = Complejo.cociente(new Complejo(
							(vectorVoltajesSecuencia0.get(minimo - 1) - vectorVoltajesSecuencia0.get(maximo - 1)), 0),
							new Complejo(0,
									trafo.get(i).getimpedanciaLineaZ0()
											+ 3 * trafo.get(i).getImpedanciaAterrizamientoPrimaria() +
											3 * trafo.get(i).getImpedanciaAterrizamientoSecundaria()));
				}

				Complejo iA1;
				iA1 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia1.get(minimo - 1) - vectorVoltajesSecuencia1.get(maximo - 1)), 0),
						new Complejo(0, trafo.get(i).getimpedanciaLineaZ1()));

				Complejo iA2 = Complejo.cociente(new Complejo(
						(vectorVoltajesSecuencia2.get(minimo - 1) - vectorVoltajesSecuencia2.get(maximo - 1)), 0),
						new Complejo(0, trafo.get(i).getimpedanciaLineaZ2()));

				Complejo iA = Complejo.suma(Complejo.suma(iA1, iA2), iA0);

				double magIA = new Complejo(iA.getReal(), iA.getImag()).modulo();
				double angIa = new Complejo(iA.getReal(), iA.getImag()).argumento();

				trafo.get(i).setCorrienteFallaFaseA(magIA);
				trafo.get(i).setAnguloCorrienteFaseA(angIa);

				Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
				Complejo a = new Complejo(-0.5, 0.8660254038);

				Complejo aCuadradoiA1 = Complejo.producto(aCuadrado, iA1);
				Complejo aIA2 = Complejo.producto(a, iA2);

				Complejo iB = Complejo.suma(iA0, Complejo.suma(aCuadradoiA1, aIA2));

				double magIB = new Complejo(iB.getReal(), iB.getImag()).modulo();
				double angIB = new Complejo(iB.getReal(), iB.getImag()).argumento();

				trafo.get(i).setCorrienteFallaFaseB(magIB);
				trafo.get(i).setAnguloCorrienteFaseB(angIB);

				Complejo aCuadradoiA2 = Complejo.producto(aCuadrado, iA2);
				Complejo aIA1 = Complejo.producto(a, iA1);
				Complejo iC = Complejo.suma(iA0, Complejo.suma(aCuadradoiA2, aIA1));

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

	public void vectorVoltajes() {

		vA1Imaginario = Complejo.resta(new Complejo(elementoFallado.getTensionLineaPuntoFalla(), 0),
				Complejo.producto(
						new Complejo(0,
								matrizImpedanciaZ1[matrizImpedanciaZ2.length - 1][matrizImpedanciaZ2.length - 1]),
						corrientePuntoFallaSecuencia1));

		vA2Imaginario = vA1Imaginario;
		vA0Imaginario = vA1Imaginario;

		double vA1, vA2, vA0;
		Complejo vA, vB, vC;
		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		for (int i = 1; i < barras.size(); i++) {

			vA1 = barras.get(i).getVoltajePrefalla() - (corrientePuntoFallaSecuencia1.modulo()
					* matrizImpedanciaZ1[i - 1][matrizImpedanciaZ2.length - 1]);
			vA2 = corrientePuntoFallaSecuencia2.modulo() * matrizImpedanciaZ2[i - 1][matrizImpedanciaZ2.length - 1];
			vA0 = corrientePuntoFallaSecuencia0.modulo() * matrizImpedanciaZ0[i - 1][matrizImpedanciaZ2.length - 1];

			vectorVoltajesSecuencia0.add(vA0);
			vectorVoltajesSecuencia1.add(vA1);
			vectorVoltajesSecuencia2.add(vA2);

			vA = Complejo.suma(Complejo.suma(new Complejo(vA1, 0), new Complejo(vA2, 0)), new Complejo(vA0, 0));
			barras.get(i).setAnguloVoltajeFaseA(vA.argumento());
			barras.get(i).setVoltajePosFallaFaseA(vA.modulo());

			vB = Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA1, 0), aCuadrado),
					Complejo.producto(new Complejo(vA2, 0), a)), new Complejo(vA0, 0));
			barras.get(i).setAnguloVoltajeFaseB(vB.argumento());
			barras.get(i).setVoltajePosFallaFaseB(vB.modulo());

			vC = Complejo.suma(Complejo.suma(Complejo.producto(new Complejo(vA2, 0), aCuadrado),
					Complejo.producto(new Complejo(vA1, 0), a)), new Complejo(vA0, 0));
			barras.get(i).setAnguloVoltajeFaseC(vC.argumento());
			barras.get(i).setVoltajePosFallaFaseC(vC.modulo());

		}

	}

	public void corrientePuntoFalla() {

		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		try {

			double impedanciTotal = matrizImpedanciaZ1[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] +
					((matrizImpedanciaZ2[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] *
							(matrizImpedanciaZ0[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] +
									3 * elementoFallado.getImpedanciaFalla()))
							/ (matrizImpedanciaZ2[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] +
									matrizImpedanciaZ0[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1]
									+ 3 * elementoFallado.getImpedanciaFalla()));

			corrientePuntoFallaSecuencia1 = Complejo.cociente(
					new Complejo(elementoFallado.getTensionLineaPuntoFalla(), 0), new Complejo(0, impedanciTotal));

			impedanciTotal = (matrizImpedanciaZ0[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] +
					3 * elementoFallado.getImpedanciaFalla())
					/ (matrizImpedanciaZ2[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] +
							matrizImpedanciaZ0[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1]
							+ 3 * elementoFallado.getImpedanciaFalla());

			corrientePuntoFallaSecuencia2 = Complejo.producto(
					Complejo.producto(new Complejo(-1, 0), corrientePuntoFallaSecuencia1),
					new Complejo(impedanciTotal, 0));

			impedanciTotal = matrizImpedanciaZ2[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1] /
					(matrizImpedanciaZ2[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1]
							+ matrizImpedanciaZ0[matrizImpedanciaZ1.length - 1][matrizImpedanciaZ1.length - 1]
							+ 3 * elementoFallado.getImpedanciaFalla());

			corrientePuntoFallaSecuencia0 = Complejo.producto(
					Complejo.producto(new Complejo(-1, 0), corrientePuntoFallaSecuencia1),
					new Complejo(impedanciTotal, 0));

			Complejo iA = Complejo.suma(Complejo.suma(corrientePuntoFallaSecuencia1, corrientePuntoFallaSecuencia2),
					corrientePuntoFallaSecuencia0);

			elementoFallado.setAngCorrientePuntoFallaFaseA(iA.argumento());
			elementoFallado.setMagcorrientePuntoFallaFaseA(iA.modulo());

			Complejo iB = Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFallaSecuencia1),
					Complejo.producto(a, corrientePuntoFallaSecuencia2)), corrientePuntoFallaSecuencia0);

			elementoFallado.setAngCorrientePuntoFallaFaseB(iB.argumento());
			elementoFallado.setMagcorrientePuntoFallaFaseB(iB.modulo());

			Complejo iC = Complejo.suma(Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFallaSecuencia2),
					Complejo.producto(a, corrientePuntoFallaSecuencia1)), corrientePuntoFallaSecuencia0);

			elementoFallado.setAngCorrientePuntoFallaFaseC(iC.argumento());
			elementoFallado.setMagcorrientePuntoFallaFaseC(iC.modulo());

		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
