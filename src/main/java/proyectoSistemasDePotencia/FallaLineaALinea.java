package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grafos.Edges;

public class FallaLineaALinea {

	private double[][] matrizSecuencia1;
	private double[][] matrizSecuencia2;
	private List<Barras> barras;
	private List<Lineas> lineas;
	private List<Transformador> trafo;
	private List<Generadores> generador;
	private Barras barraFallada;
	private Complejo corrientePuntoFalla;
	private Complejo corrientePuntoFalla2;
	private ArrayList<Double> vectorVoltajesSecuencia1 = new ArrayList<>();
	private ArrayList<Double> vectorVoltajesSecuencia2 = new ArrayList<>();
	// private String [] conexionesBarras;
	// private List<List<Edges>> adyacencias;

	public FallaLineaALinea(double[][] matrizSecuencia1, double[][] matrizSecuencia2, List<Barras> barras,
			List<Lineas> lineas, List<Transformador> trafo,
			List<Generadores> generador, Barras barraFallada, List<List<Edges>> adyacencias) {

		this.barraFallada = barraFallada;
		this.matrizSecuencia1 = matrizSecuencia1;
		this.matrizSecuencia2 = matrizSecuencia2;
		this.barras = barras;
		this.lineas = lineas;
		this.trafo = trafo;
		this.generador = generador;
		// this.adyacencias=adyacencias;

		calculoFallas();

	}

	public void calculoFallas() {

		corrientePuntoFalla();
		setConexionesBarras();
		setAngulosBarras();
		vectorVoltajes();
		contribuccionesMaquinas();
		corrienteLineas();

	}

	public void corrientePuntoFalla() {

		double impedanciaTotal = matrizSecuencia1[barras.indexOf(barraFallada) - 1][barras.indexOf(barraFallada) - 1] +
				matrizSecuencia2[barras.indexOf(barraFallada) - 1][barras.indexOf(barraFallada) - 1]
				+ barraFallada.getImpedanciaFalla();

		try {

			corrientePuntoFalla = Complejo.cociente(new Complejo(barraFallada.getVoltajePrefalla(), 0),
					new Complejo(0, impedanciaTotal));
			corrientePuntoFalla2 = Complejo.producto(new Complejo(-1, 0), corrientePuntoFalla);

			barraFallada.setAngCorrientePuntoFallaFaseA(0);
			barraFallada.setMagcorrientePuntoFallaFaseA(0);

			Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
			Complejo a = new Complejo(-0.5, 0.8660254038);

			Complejo iB = Complejo.suma(Complejo.producto(aCuadrado, corrientePuntoFalla),
					Complejo.producto(a, corrientePuntoFalla2));

			barraFallada.setAngCorrientePuntoFallaFaseB(iB.argumento());
			barraFallada.setMagcorrientePuntoFallaFaseB(iB.modulo());

			Complejo iC = Complejo.producto(new Complejo(-1, 0), iB);

			barraFallada.setAngCorrientePuntoFallaFaseC(iC.argumento());
			barraFallada.setMagcorrientePuntoFallaFaseC(iC.modulo());

		} catch (ExcepcionDivideCero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void vectorVoltajes() {

		Complejo aCuadrado = new Complejo(-0.5, -0.8660254038);
		Complejo a = new Complejo(-0.5, 0.8660254038);

		for (int i = 0; i < matrizSecuencia1.length; i++) {

			if (i == (barras.indexOf(barraFallada) - 1)) {

				double vA0 = 0;
				double vA1 = barraFallada.getVoltajePrefalla()
						- matrizSecuencia1[barras.indexOf(barraFallada) - 1][barras.indexOf(barraFallada) - 1]
								* corrientePuntoFalla.modulo();
				double vA2 = vA1;

				vectorVoltajesSecuencia1.add(i, vA1);
				vectorVoltajesSecuencia2.add(i, vA2);

				double vA = vA0 + vA1 * 2;

				barraFallada.setAnguloVoltajeFaseA(0);
				barraFallada.setVoltajePosFallaFaseA(vA);

				Complejo iB = Complejo.suma(Complejo.producto(aCuadrado, new Complejo(vA1, 0)),
						Complejo.producto(a, new Complejo(vA2, 0)));

				barraFallada.setVoltajePosFallaFaseB(iB.modulo());
				barraFallada.setAnguloVoltajeFaseB(iB.argumento());
				barraFallada.setVoltajePosFallaFaseC(iB.modulo());
				barraFallada.setAnguloVoltajeFaseC(iB.argumento());

			} else {

				double vA1 = barras.get(i + 1).getVoltajePrefalla()
						- matrizSecuencia1[barras.indexOf(barraFallada) - 1][i] * corrientePuntoFalla.modulo();
				double vA2 = matrizSecuencia2[barras.indexOf(barraFallada) - 1][i] * corrientePuntoFalla.modulo();

				vectorVoltajesSecuencia1.add(vA1);
				vectorVoltajesSecuencia2.add(vA2);

				Complejo vASecuencia1 = Complejo.polar2Cartesiano(vA1, barras.get(i + 1).getAnguloVoltajeSecuencia1());
				Complejo vASecuencia2 = Complejo.polar2Cartesiano(vA2, barras.get(i + 1).getAnguloVoltajeSecuencia2());

				Complejo vFaseA = Complejo.suma(vASecuencia1, vASecuencia2);

				barras.get(i + 1).setAnguloVoltajeFaseA(vFaseA.argumento());
				barras.get(i + 1).setVoltajePosFallaFaseA(vFaseA.modulo());

				Complejo vFaseB = Complejo.suma(Complejo.producto(aCuadrado, vASecuencia1),
						Complejo.producto(a, vASecuencia2));

				barras.get(i + 1).setAnguloVoltajeFaseB(vFaseB.argumento());
				barras.get(i + 1).setVoltajePosFallaFaseB(vFaseB.modulo());

				Complejo vFaseC = Complejo.suma(Complejo.producto(a, vASecuencia1),
						Complejo.producto(aCuadrado, vASecuencia2));

				barras.get(i + 1).setAnguloVoltajeFaseC(vFaseC.argumento());
				barras.get(i + 1).setVoltajePosFallaFaseC(vFaseC.modulo());

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

	public void contribuccionesMaquinas() {

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

				Complejo iA2 = Complejo.cociente(Complejo.producto(new Complejo(-1, 0),
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

	public void setAngulosBarras() {

		// AngulosDesfazamiento.establecerAngulosDeDesfazamiento(barras, barraFallada,
		// conexionesBarras, adyacencias);

	}

	public void setConexionesBarras() {

		// this.conexionesBarras=ConexionesBarras.establecerConexionesBarras(trafo,
		// barras);

	}

	public double getmagCorrientePuntoFalla() {
		return corrientePuntoFalla.modulo();
	}

	public double getanguloCorrientePuntoFalla() {
		return corrientePuntoFalla.argumento();
	}

	public void imprimir(double[][] resultado) {

		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < resultado.length; j++) {

				System.out.print(resultado[i][j] + " ");
			}
			System.out.println();
		}

	}

	public static <E> void imprimir(E[] resultado) {

		for (int i = 0; i < resultado.length; i++) {
			System.out.println(resultado[i] + " ");
		}
	}

	public void imprimir(double[] resultado) {

		for (int i = 0; i < resultado.length; i++) {
			System.out.println(resultado[i] + " ");
		}
	}

}
