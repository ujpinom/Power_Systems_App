package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.List;

public class CreacionZBarra {

	private ArrayList<ArrayList<Double>> contruccionZBarra = new ArrayList<>();
	private List<Barras> barras;
	private double[][] zBarra;
	private Lineas elementoFallado;

	public CreacionZBarra() {

	}

	public CreacionZBarra(List<Barras> barras, double[][] zBarra, Lineas elementoFallado) {

		this.barras = barras;
		this.zBarra = zBarra;
		this.elementoFallado = elementoFallado;

	}

	public double[][] inserccionABarraExistente(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.min(b1, b2);

		double ZkkZb = zBarra[barra - 1][barra - 1] + elementoFallado.getimpedanciaLineaZ1() / 2;

		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = ZkkZb;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1];

		}

		return matrizCambiante;

	}

	public double[][] inserccionABarraExistente0(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.min(b1, b2);

		double ZkkZb = zBarra[barra - 1][barra - 1] + elementoFallado.getimpedanciaLineaZ0() / 2;
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = ZkkZb;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1];

		}

		return matrizCambiante;

	}

	public double[][] inserccionABarraExistente2(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.min(b1, b2);

		double ZkkZb = zBarra[barra - 1][barra - 1] + elementoFallado.getimpedanciaLineaZ2() / 2;
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = ZkkZb;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1];

		}

		return matrizCambiante;

	}

	public double[][] inserccionEntreBarras(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.max(b1, b2);

		double zThevenin = zBarra[zBarra.length - 1][zBarra.length - 1] + zBarra[barra - 1][barra - 1]
				- 2 * zBarra[barra - 1][zBarra.length - 1];

		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				+ elementoFallado.getimpedanciaLineaZ1() / 2;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;

	}

	public double[][] inserccionEntreBarras0(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.max(b1, b2);

		double zThevenin = zBarra[zBarra.length - 1][zBarra.length - 1] + zBarra[barra - 1][barra - 1]
				- 2 * zBarra[barra - 1][zBarra.length - 1];
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				+ elementoFallado.getimpedanciaLineaZ0() / 2;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;

	}

	public double[][] inserccionEntreBarras2(double[][] zBarra) {

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		int b1 = barras.indexOf(elementoFallado.getBarra1());
		int b2 = barras.indexOf(elementoFallado.getBarra2());

		int barra = Math.max(b1, b2);

		double zThevenin = zBarra[zBarra.length - 1][zBarra.length - 1] + zBarra[barra - 1][barra - 1]
				- 2 * zBarra[barra - 1][zBarra.length - 1];
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				+ elementoFallado.getimpedanciaLineaZ2() / 2;

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][barra - 1] - zBarra[i][zBarra.length - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;
	}

	public double[][] inserccionEntreBarras(double[][] zBarra, int barra1, int barra2) {

		int b1 = Math.min(barra1, barra2);
		int b2 = Math.max(barra1, barra2);

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		double zThevenin = zBarra[b1 - 1][b1 - 1] + zBarra[b2 - 1][b2 - 1] - 2 * zBarra[b1 - 1][b2 - 1];
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				- elementoFallado.getimpedanciaLineaZ1();

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;

	}

	public double[][] inserccionEntreBarras0(double[][] zBarra, int barra1, int barra2) {

		int b1 = Math.min(barra1, barra2);
		int b2 = Math.max(barra1, barra2);

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		double zThevenin = zBarra[b1 - 1][b1 - 1] + zBarra[b2 - 1][b2 - 1] - 2 * zBarra[b1 - 1][b2 - 1];
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				- elementoFallado.getimpedanciaLineaZ0();

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;

	}

	public double[][] inserccionEntreBarras2(double[][] zBarra, int barra1, int barra2) {

		int b1 = Math.min(barra1, barra2);
		int b2 = Math.max(barra1, barra2);

		double[][] matrizCambiante = new double[zBarra.length + 1][zBarra.length + 1];

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				matrizCambiante[i][j] = zBarra[i][j];

			}
		}

		double zThevenin = zBarra[b1 - 1][b1 - 1] + zBarra[b2 - 1][b2 - 1] - 2 * zBarra[b1 - 1][b2 - 1];
		matrizCambiante[matrizCambiante.length - 1][matrizCambiante.length - 1] = zThevenin
				- elementoFallado.getimpedanciaLineaZ2();

		for (int i = 0; i < zBarra.length; i++) {

			matrizCambiante[i][matrizCambiante.length - 1] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];
			matrizCambiante[matrizCambiante.length - 1][i] = zBarra[i][b1 - 1] - zBarra[i][b2 - 1];

		}

		double[][] matrizReducida = reduccionKron(matrizCambiante);

		return matrizReducida;

	}

	public double[][] reduccionKron(double[][] zBarra) {

		double[][] zBarraNueva = new double[zBarra.length - 1][zBarra.length - 1];

		for (int i = 0; i < zBarraNueva.length; i++) {

			for (int j = 0; j < zBarraNueva.length; j++) {

				zBarraNueva[i][j] = zBarra[i][j];

				double aa = zBarra[zBarra.length - 1][j];
				double bb = zBarra[i][zBarra.length - 1];
				zBarraNueva[i][j] = zBarraNueva[i][j] - (aa * bb / zBarra[zBarra.length - 1][zBarra.length - 1]);

			}
		}

		return zBarraNueva;

	}

	public void print(double[][] zBarra) {

		for (int i = 0; i < zBarra.length; i++) {
			for (int j = 0; j < zBarra.length; j++) {

				System.out.print(zBarra[i][j] + "  ");

			}
			System.out.println();
		}

	}

}
