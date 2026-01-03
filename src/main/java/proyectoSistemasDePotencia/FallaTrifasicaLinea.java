package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.List;

public class FallaTrifasicaLinea {

	private double[][] zBarra;
	private Lineas elementoFallado;
	private double corrientePuntoFalla;
	private List<Barras> barras;
	private List<Lineas> linea;
	private List<Transformador> trafo;
	private List<Double> vectorVoltajesFalla = new ArrayList<>();
	private CreacionZBarra crz;
	private double voltajeBarraImaginaria;
	private List<Generadores> generador;

	public FallaTrifasicaLinea(double[][] zBarra, Lineas elementoFallado, List<Barras> barras,
			List<Lineas> lineas, List<Transformador> trafo, List<Generadores> generador) {

		super();

		this.elementoFallado = elementoFallado;
		this.barras = barras;
		this.linea = lineas;
		this.trafo = trafo;
		this.generador = generador;

		crz = new CreacionZBarra(barras, zBarra, elementoFallado);

		double[][] matrizInserccionBarra = crz.inserccionABarraExistente(zBarra);

		double[][] matrizInserccionEntreDosBarras = crz.inserccionEntreBarras(matrizInserccionBarra);

		double[][] zBarrafinal = crz.inserccionEntreBarras(matrizInserccionEntreDosBarras,
				barras.indexOf(elementoFallado.getBarra1()), barras.indexOf(elementoFallado.getBarra2()));

		this.zBarra = zBarrafinal;

		calculoFallas();
	}

	private void calculoFallas() {

		setCorrienteFallaElementoFallado();
		setVectorVoltajesFalla();
		setCorrientesDeLinea();
		setContribuciones();

	}

	public void setCorrientesDeLinea() {
		double anguloImpedancia = -90;

		for (int i = 0; i < linea.size(); i++) {

			double vFaseABarra1 = linea.get(i).getBarra1().getVoltajePosFallaFaseA();
			double vFaseABarra2 = linea.get(i).getBarra2().getVoltajePosFallaFaseA();

			double aFaseABarra1 = linea.get(i).getBarra1().getAnguloVoltajeFaseA();
			double aFaseBBarra1 = linea.get(i).getBarra1().getAnguloVoltajeFaseB();
			double aFaseCBarra1 = linea.get(i).getBarra1().getAnguloVoltajeFaseC();

			linea.get(i).setAnguloCorrienteFaseA(aFaseABarra1 + anguloImpedancia);
			linea.get(i).setAnguloCorrienteFaseB(aFaseBBarra1 + anguloImpedancia);
			linea.get(i).setAnguloCorrienteFaseC(aFaseCBarra1 + anguloImpedancia);

			double corrienteFalla = Math.abs(vFaseABarra1 - vFaseABarra2) / linea.get(i).getimpedanciaLineaZ1();

			linea.get(i).setCorrienteFallaFaseA(corrienteFalla);
			linea.get(i).setCorrienteFallaFaseB(corrienteFalla);
			linea.get(i).setCorrienteFallaFaseC(corrienteFalla);

		}

		for (int i = 0; i < trafo.size(); i++) {

			double vFaseABarra1 = trafo.get(i).getBarra1().getVoltajePosFallaFaseA();
			double vFaseABarra2 = trafo.get(i).getBarra2().getVoltajePosFallaFaseA();

			double aFaseABarra1 = trafo.get(i).getBarra1().getAnguloVoltajeFaseA();
			double aFaseBBarra1 = trafo.get(i).getBarra1().getAnguloVoltajeFaseB();
			double aFaseCBarra1 = trafo.get(i).getBarra1().getAnguloVoltajeFaseC();

			trafo.get(i).setAnguloCorrienteFaseA(aFaseABarra1 + anguloImpedancia);
			trafo.get(i).setAnguloCorrienteFaseB(aFaseBBarra1 + anguloImpedancia);
			trafo.get(i).setAnguloCorrienteFaseC(aFaseCBarra1 + anguloImpedancia);

			double corrienteFalla = Math.abs(vFaseABarra1 - vFaseABarra2) / trafo.get(i).getimpedanciaLineaZ1();

			trafo.get(i).setCorrienteFallaFaseA(corrienteFalla);
			trafo.get(i).setCorrienteFallaFaseB(corrienteFalla);
			trafo.get(i).setCorrienteFallaFaseC(corrienteFalla);

		}

		for (int i = 0; i < generador.size(); i++) {

			generador.get(i).setCorrienteFaseA((generador.get(i).getBarra().getVoltajePrefalla() -
					generador.get(i).getBarra().getVoltajePosFallaFaseA()) / (generador.get(i).getImpedanciaZ1()));

			generador.get(i).setCorrienteFaseB((generador.get(i).getBarra().getVoltajePrefalla() -
					generador.get(i).getBarra().getVoltajePosFallaFaseB()) / (generador.get(i).getImpedanciaZ1()));

			generador.get(i).setCorrienteFaseC((generador.get(i).getBarra().getVoltajePrefalla() -
					generador.get(i).getBarra().getVoltajePosFallaFaseC()) / (generador.get(i).getImpedanciaZ1()));

			generador.get(i).setAnguloCorrienteFaseA(-90);
			generador.get(i).setAnguloCorrienteFaseB(-210);
			generador.get(i).setAnguloCorrienteFaseC(30);

		}

	}

	public void setContribuciones() {

		for (int i = 0; i < barras.size(); i++) {

			if (barras.get(i) == elementoFallado.getBarra1() || barras.get(i) == elementoFallado.getBarra2()) {

				double contribuccion = (barras.get(i).getVoltajePosFallaFaseA() - voltajeBarraImaginaria)
						/ (elementoFallado.getimpedanciaLineaZ1() / 2);

				barras.get(i).setContribuccionFallaFaseA(contribuccion);
				barras.get(i).setContribuccionFallaFaseB(contribuccion);
				barras.get(i).setContribuccionFallaFaseC(contribuccion);

				barras.get(i).setAnguloContribucionFaseA(-90);
				barras.get(i).setAnguloContribucionFaseB(-210);
				barras.get(i).setAnguloContribucionFaseC(30);

			}
		}

	}

	public void setVectorVoltajesFalla() {

		for (int i = 0; i < zBarra.length - 1; i++) {

			double elemento = barras.get(i + 1).getVoltajePrefalla() - (zBarra[i][zBarra.length - 1] /
					(zBarra[zBarra.length - 1][zBarra.length - 1] + elementoFallado.getImpedanciaFalla()))
					* barras.get(i + 1).getVoltajePrefalla();

			vectorVoltajesFalla.add(elemento);

			barras.get(i + 1).setVoltajePosFallaFaseA(vectorVoltajesFalla.get(i));
			barras.get(i + 1).setVoltajePosFallaFaseB(vectorVoltajesFalla.get(i));
			barras.get(i + 1).setVoltajePosFallaFaseC(vectorVoltajesFalla.get(i));

			barras.get(i + 1).setAnguloVoltajeFaseA(0);
			barras.get(i + 1).setAnguloVoltajeFaseB(-120);
			barras.get(i + 1).setAnguloVoltajeFaseC(120);
		}

	}

	public void setCorrienteFallaElementoFallado() {

		corrientePuntoFalla = (-1) * elementoFallado.getTensionLineaPuntoFalla() /
				(zBarra[zBarra.length - 1][zBarra.length - 1] + elementoFallado.getImpedanciaFalla());

		voltajeBarraImaginaria = elementoFallado.getTensionLineaPuntoFalla()
				+ corrientePuntoFalla * zBarra[zBarra.length - 1][zBarra.length - 1];

		elementoFallado.setAngCorrientePuntoFallaFaseA(-90);
		elementoFallado.setAngCorrientePuntoFallaFaseB(-210);
		elementoFallado.setAngCorrientePuntoFallaFaseC(30);

		elementoFallado.setMagcorrientePuntoFallaFaseA((-1) * corrientePuntoFalla);
		elementoFallado.setMagcorrientePuntoFallaFaseB((-1) * corrientePuntoFalla);
		elementoFallado.setMagcorrientePuntoFallaFaseC((-1) * corrientePuntoFalla);

	}

	public double getCorrientePuntoFallado() {

		return corrientePuntoFalla;
	}
}
