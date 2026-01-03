package proyectoSistemasDePotencia;

import java.util.*;

public class FallaAsimetricas {

	private double[][] matrizImpedanciaZ0;
	private double[][] matrizImpedanciaZ1;
	private double[][] matrizImpedanciaZ2;
	private List<Barras> barras;
	private List<Lineas> lineas;
	private List<Transformador> trafo;
	private double corrienteFalla;
	private Barras barraFallada;
	private List<Double> vectorVoltajesSecuencia0 = new ArrayList<>();
	private List<Double> vectorVoltajesSecuencia1 = new ArrayList<>();
	private List<Double> vectorVoltajesSecuencia2 = new ArrayList<>();
	private List<Generadores> generador;

	public FallaAsimetricas(double[][] matrizImpedanciaZ0, double[][] matrizImpedanciaZ1, double[][] matrizImpedanciaZ2,
			List<Barras> barras, List<Lineas> lineas, List<Transformador> trafo,
			Barras barraFallada, List<Generadores> generador) {

		super();
		this.matrizImpedanciaZ0 = matrizImpedanciaZ0;
		this.matrizImpedanciaZ1 = matrizImpedanciaZ1;
		this.matrizImpedanciaZ2 = matrizImpedanciaZ2;
		this.barras = barras;
		this.lineas = lineas;
		this.trafo = trafo;
		this.barraFallada = barraFallada;
		this.generador = generador;

		calculoFallas();

	} // Fin del constructor

	public void calculoFallas() {

		calculoCorrienteFalla();
		calculoVectorVoltajes();
		calculoCorrientesFalla();

	}

	private void calculoCorrienteFalla() {

		int indexBarraFallada = barras.indexOf(barraFallada) - 1;

		double corrienteFalla = barraFallada.getVoltajePrefalla()
				/ (matrizImpedanciaZ0[indexBarraFallada][indexBarraFallada] +
						matrizImpedanciaZ1[indexBarraFallada][indexBarraFallada] +
						matrizImpedanciaZ2[indexBarraFallada][indexBarraFallada]
						+ 3 * barraFallada.getImpedanciaFalla());

		this.corrienteFalla = (-1) * corrienteFalla;

		barraFallada.setAngCorrientePuntoFallaFaseA(-90);
		barraFallada.setAngCorrientePuntoFallaFaseB(0);
		barraFallada.setAngCorrientePuntoFallaFaseC(0);
		barraFallada.setMagcorrientePuntoFallaFaseA(3 * corrienteFalla);
		barraFallada.setMagcorrientePuntoFallaFaseB(0);
		barraFallada.setMagcorrientePuntoFallaFaseC(0);

	}

	public double getCorrientePuntoFallaSecuencia0() {

		return corrienteFalla;
	}

	public double getCorrientePuntoFallaTotal() {

		return corrienteFalla * 3;
	}

	public void calculoVectorVoltajes() {

		int indexBarraFallada = barras.indexOf(barraFallada) - 1;

		for (int i = 0; i < matrizImpedanciaZ2.length; i++) {

			double vA0 = matrizImpedanciaZ0[i][indexBarraFallada] * corrienteFalla;
			vectorVoltajesSecuencia0.add(vA0);

			double vA2 = matrizImpedanciaZ2[i][indexBarraFallada] * corrienteFalla;
			vectorVoltajesSecuencia2.add(vA2);

			double vA1 = barras.get(i + 1).getVoltajePrefalla()
					+ matrizImpedanciaZ1[i][indexBarraFallada] * corrienteFalla;
			vectorVoltajesSecuencia1.add(vA1);

			double VFaseA = vA0 + vA1 + vA2;

			barras.get(i + 1).setVoltajePosFallaFaseA(VFaseA);
			barras.get(i + 1).setAnguloVoltajeFaseA(0);

			Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
			Complejo a = new Complejo(-0.5, 0.8660254038);

			Complejo vA1aCuadrado = Complejo.producto(aCuadrado, vA1);
			Complejo vA2a = Complejo.producto(a, vA2);

			Complejo sumavA1aCuadradovA2a = Complejo.suma(vA1aCuadrado, vA2a);
			Complejo ResultadoFaseB = Complejo.suma(sumavA1aCuadradovA2a, new Complejo(vA0, 0));

			double magFaseB = new Complejo(ResultadoFaseB.getReal(), ResultadoFaseB.getImag()).modulo();
			double angFaseB = new Complejo(ResultadoFaseB.getReal(), ResultadoFaseB.getImag()).argumento();

			barras.get(i + 1).setVoltajePosFallaFaseB(magFaseB);
			barras.get(i + 1).setAnguloVoltajeFaseB(angFaseB);

			vA1aCuadrado = Complejo.producto(a, vA1);
			vA2a = Complejo.producto(aCuadrado, vA2);

			sumavA1aCuadradovA2a = Complejo.suma(vA1aCuadrado, vA2a);
			Complejo resultadoFaseC = Complejo.suma(sumavA1aCuadradovA2a, new Complejo(vA0, 0));

			double magFaseC = new Complejo(resultadoFaseC.getReal(), resultadoFaseC.getImag()).modulo();
			double angFaseC = new Complejo(resultadoFaseC.getReal(), resultadoFaseC.getImag()).argumento();

			barras.get(i + 1).setVoltajePosFallaFaseC(magFaseC);
			barras.get(i + 1).setAnguloVoltajeFaseC(angFaseC);

		}

	}

	public void calculoCorrientesFalla() {

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

		for (int i = 0; i < trafo.size(); i++) {
			Complejo iA0;

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

				Complejo iA1 = Complejo.cociente(new Complejo(
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

		for (int i = 0; i < generador.size(); i++) {

			int indexBarraGe = barras.indexOf(generador.get(i).getBarra());

			double iA0 = vectorVoltajesSecuencia0.get(indexBarraGe - 1)
					/ (generador.get(i).getImpedanciaZ0() + generador.get(i).getImpedanciaAterrizamiento());

			double iA1 = (-1) * (Barras.vprefalla - vectorVoltajesSecuencia1.get(indexBarraGe - 1))
					/ generador.get(i).getImpedanciaZ1();

			double iA2 = vectorVoltajesSecuencia2.get(indexBarraGe - 1) / generador.get(i).getImpedanciaZ2();

			double corrinteFaseA = iA0 + iA1 + iA2;

			generador.get(i).setCorrienteFaseA((-1) * corrinteFaseA);
			generador.get(i).setAnguloCorrienteFaseA(-90);

			Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
			Complejo a = new Complejo(-0.5, 0.8660254038);

			Complejo acuadradoIa = Complejo.producto(aCuadrado, new Complejo(0, iA1));
			Complejo aIa = Complejo.producto(a, new Complejo(0, iA2));

			Complejo sumaacuadradoIaaIa = Complejo.suma(acuadradoIa, aIa);
			Complejo sumaFinal = Complejo.suma(sumaacuadradoIaaIa, new Complejo(0, iA0));

			double magCorrienteFaseB = new Complejo(sumaFinal.getReal(), sumaFinal.getImag()).modulo();
			double angCorrienteFaseB = new Complejo(sumaFinal.getReal(), sumaFinal.getImag()).argumento();

			generador.get(i).setCorrienteFaseB(magCorrienteFaseB);
			generador.get(i).setAnguloCorrienteFaseB(angCorrienteFaseB);

			acuadradoIa = Complejo.producto(aCuadrado, new Complejo(0, iA2));
			aIa = Complejo.producto(a, new Complejo(0, iA1));

			sumaacuadradoIaaIa = Complejo.suma(acuadradoIa, aIa);
			sumaFinal = Complejo.suma(sumaacuadradoIaaIa, new Complejo(0, iA0));

			double magCorrienteFaseC = new Complejo(sumaFinal.getReal(), sumaFinal.getImag()).modulo();
			double angCorrienteFaseC = new Complejo(sumaFinal.getReal(), sumaFinal.getImag()).argumento();

			generador.get(i).setCorrienteFaseC(magCorrienteFaseC);
			generador.get(i).setAnguloCorrienteFaseC(angCorrienteFaseC);

		}
	}

}
