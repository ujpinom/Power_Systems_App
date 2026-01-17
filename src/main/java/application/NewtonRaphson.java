package application;

import java.util.ArrayList;
import java.util.List;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.Complejo;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.Zbarra;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;

public class NewtonRaphson implements Cloneable {

	private int dimensionMatrizjacobiana = 0; // Determina cuantas filas y columnas hay que quitar de la jacobiana.
	private List<Integer> indicesBarrasCompPV = new ArrayList<>(); // Almacena el indice de las barras que son de
	// compensaciÃ³n o PV.
	private List<Double>[] infoIteracionesVoltajes;

	private List<Double>[] infoIteracionesAngulos;

	private int numeroIteraciones = 20;

	private List<Barras> barras;

	private double epsilon = 0.001;

	private Complejo[][] matrizAdj;
	// {{new Complejo(),new Complejo(),new Complejo(),new Complejo(),new Complejo()}
	// ,{new Complejo(),new Complejo(8.985190,-44.835953),new
	// Complejo(-3.815629,19.078144),new Complejo(-5.169561,25.847809),new
	// Complejo()}
	// ,{new Complejo(),new Complejo(-3.815629,19.078144),new
	// Complejo(8.985190,-44.835953),new Complejo(),new
	// Complejo(-5.169561,25.847809)}
	// ,{new Complejo(),new Complejo(-5.169561,25.847809),new Complejo(),new
	// Complejo(8.193267,-40.863838),new Complejo(-3.023705,15.118528)},
	// {new Complejo(),new Complejo(),new Complejo(-5.169561,25.847809),new
	// Complejo(-3.023705,15.118528),new Complejo(8.193267,-40.863838)}};
	//

	private double[] erroresPotencia;

	private double[][] jacobiana;

	private double[] erroresParametros;

	private int iteConvergencia;

	@SuppressWarnings("unchecked")
	public NewtonRaphson(List<Barras> barras, int numeroIteraciones, double epsilon, Complejo[][] matrizAdj) {

		this.barras = barras;
		this.erroresPotencia = new double[2 * (barras.size() - 1)];
		this.matrizAdj = matrizAdj;
		this.numeroIteraciones = numeroIteraciones;
		this.epsilon = epsilon;

		jacobiana = new double[2 * (barras.size() - 1)][2 * (barras.size() - 1)];
		infoIteracionesVoltajes = (List<Double>[]) new List[barras.size()];
		infoIteracionesAngulos = (List<Double>[]) new List[barras.size()];

		for (int i = 0; i < erroresPotencia.length; i++) {
			erroresPotencia[i] = Double.POSITIVE_INFINITY;
		}

		for (int i = 0; i < barras.size(); i++) {

			infoIteracionesAngulos[i] = new ArrayList<Double>();
			infoIteracionesVoltajes[i] = new ArrayList<Double>();

		}

		for (int i = 1; i < barras.size(); i++) {

			infoIteracionesAngulos[i].add(barras.get(i).getAnguloVoltajeBarra());
			infoIteracionesVoltajes[i].add(barras.get(i).getVoltajePrefalla());

		}
		
		adjustarIndicesBarrasCompPV();

	}
	
	private void adjustarIndicesBarrasCompPV() {
		
		indicesBarrasCompPV.clear();
		dimensionMatrizjacobiana=0;
		
		for (int i = 0; i < barras.size(); i++) {

			Barras b = barras.get(i);

			if (b.isBarraCompensacion()) {
				indicesBarrasCompPV.add(i);
				dimensionMatrizjacobiana += 2;
			}

			else if (b.isBarraPV()) {
				indicesBarrasCompPV.add(i);
				dimensionMatrizjacobiana += 1;
			}


		}
		
	}

	@SuppressWarnings("unchecked")
	public NewtonRaphson(List<Barras> barras) {
		this.erroresPotencia = new double[2 * (barras.size() - 1)];
		this.barras = barras;

		for (int i = 0; i < erroresPotencia.length; i++) {
			erroresPotencia[i] = Double.POSITIVE_INFINITY;
		}

		jacobiana = new double[2 * (barras.size() - 1)][2 * (barras.size() - 1)];
		infoIteracionesVoltajes = (List<Double>[]) new List[5];
		infoIteracionesAngulos = (List<Double>[]) new List[5];

		for (int i = 0; i < barras.size(); i++) {
			Barras b = barras.get(i);

			if (b.isBarraCompensacion()) {
				indicesBarrasCompPV.add(i);
				dimensionMatrizjacobiana += 2;
			}

			else if (b.isBarraPV()) {
				indicesBarrasCompPV.add(i);
				dimensionMatrizjacobiana += 1;
			}

			infoIteracionesAngulos[i] = new ArrayList<Double>();
			infoIteracionesVoltajes[i] = new ArrayList<Double>();

		}

		for (int i = 1; i < barras.size(); i++) {

			infoIteracionesAngulos[i].add(barras.get(i).getAnguloVoltajeBarra());
			infoIteracionesVoltajes[i].add(barras.get(i).getVoltajePrefalla());

		}

	}

	public List<Double>[] solucionVoltajes() {
		return infoIteracionesVoltajes;
	}

	public List<Double>[] solucionAngulos() {

		return infoIteracionesAngulos;
	}

	public List<Complejo>[] solucionFormaCompleja() {

		List<Complejo>[] solucion = (List<Complejo>[]) new List[barras.size()];

		for (int i = 0; i < solucion.length; i++) {

			solucion[i] = new ArrayList<Complejo>();

			if (i > 0) {

				double voltaje = infoIteracionesVoltajes[i].get(infoIteracionesVoltajes[i].size() - 1);
				double angulo = infoIteracionesAngulos[i].get(infoIteracionesAngulos[i].size() - 1);

				Complejo voltajeComplejo = Complejo.polar2CartesianoAnguloRad(voltaje, angulo);

				solucion[i].add(voltajeComplejo);
			}

		}

		return solucion;
	}

	public void calcularFlujoPotencia(List<Generadores> generadores, List<Bancos> bancos, List<Carga> cargas) {

		int iteracionActual = 1;

		while (iteracionActual <= numeroIteraciones) {


				for (int i = 1; i < barras.size(); i++) {

					Barras b = barras.get(i);

					if (b.isBarraCompensacion()) {
						continue;
					}

					else if (b.isBarraPQ()) {

						calcularJacobiana(i, b);
						calcularPotencias(i, b);

					}

					else if (b.isBarraPV()) {
						calcularJacobiana(i, b);
						calcularPotencias(i, b);
					}

				}
			

			RealMatrix jaco = new Array2DRowRealMatrix(jacobiana);
			
			adjustarIndicesBarrasCompPV();

			List<Integer> indicesAEliminar = dropIndices();

			int[] indicesAMantener = new int[jacobiana.length - dimensionMatrizjacobiana]; /// La soluciÃ³n del problema
			/// de flujos de potencia
			/// corresponden a estas
			/// barras +1.
			int contador = 0;
			for (int i = 0; i < jacobiana.length; i++) {

				if (!indicesAEliminar.contains(i)) {

					indicesAMantener[contador++] = i;

				}

			}

			RealMatrix jacobianDroped = jaco.getSubMatrix(indicesAMantener, indicesAMantener);

			double[] erroresPotenciaDroped = new double[jacobiana.length - dimensionMatrizjacobiana];
			contador = 0;

			for (int i = 0; i < erroresPotencia.length; i++) {

				if (erroresPotencia[i] == Double.POSITIVE_INFINITY) {
					continue;
				} else {

					erroresPotenciaDroped[contador++] = erroresPotencia[i];

				}

			}

			RealVector erroresPotencia = new ArrayRealVector(erroresPotenciaDroped);

			SingularValueDecomposition svd = new SingularValueDecomposition(jacobianDroped);
			DecompositionSolver solver = svd.getSolver();

			RealVector sol = solver.solve(erroresPotencia);

			erroresParametros = sol.toArray();

			correcionParametros(indicesAMantener);
			
			
			verificarLimitesPotenciaReactiva();
			

			int cont = 0;

			for (int i = 1; i < infoIteracionesAngulos.length; i++) { // VerificaciÃ³n con el proposito de verificar la convergencia del sistema de potencia  
				// Diferencia de Angulos y voltajes menor que el epsilon estipulado.Si todas las barras cumplen este criterio se da por entendido que una soluciÃ³n ha sido 
				// encontrada y por consiguiente el algoritmo es detenido.

				int angulos = infoIteracionesAngulos[i].size();
				int voltajes = infoIteracionesVoltajes[i].size();

				if (barras.get(i).isBarraCompensacion()) {
					++cont;
					continue;
				}

				else if (barras.get(i).isBarraPV()) {

					if (Math.abs(infoIteracionesAngulos[i].get(angulos - 1)
							- infoIteracionesAngulos[i].get(angulos - 2)) <= epsilon) {

						++cont;

					} else {
						break;
					}

				} else if (barras.get(i).isBarraPQ()) {

					if (Math.abs(infoIteracionesAngulos[i].get(angulos - 1)
							- infoIteracionesAngulos[i].get(angulos - 2)) <= epsilon
							&& (Math.abs(infoIteracionesVoltajes[i].get(voltajes - 1)
									- infoIteracionesVoltajes[i].get(voltajes - 2)) <= epsilon)) {
						++cont;

					} else {
						break;
					}
				}

			}

			if (cont == barras.size() - 1) {
				break;
			}

			++iteracionActual;

		}

		iteConvergencia = iteracionActual;

	}

	public void verificarLimitesPotenciaReactiva() {
		
		double potenciaReactivaCarga=0.0;
		double potenciaRealCarga=0.0;

		for (int i = 0; i < barras.size(); i++) {

			Barras b = barras.get(i);

			if (b.isBarraPV()) {


				Complejo potenciaCompleja = new Complejo();

				double mag = infoIteracionesVoltajes[i].get(infoIteracionesVoltajes[i].size() - 1);

				double ang = infoIteracionesAngulos[i].get(infoIteracionesAngulos[i].size() - 1);

				Complejo tensionBarra = Complejo.polar2CartesianoAnguloRad(mag, ang);

				Complejo tensionBarraConjugada = Complejo.conjugado(tensionBarra);

				for (int j = 1; j < barras.size(); j++) {

					Complejo Yin = matrizAdj[i][j];
					double magn = infoIteracionesVoltajes[j].get(infoIteracionesVoltajes[j].size() - 1);

					double angn = infoIteracionesAngulos[j].get(infoIteracionesAngulos[j].size() - 1);

					Complejo Vn = Complejo.polar2CartesianoAnguloRad(magn, angn);

					potenciaCompleja = Complejo.suma(potenciaCompleja, Complejo.producto(Yin, Vn));

				}

				potenciaCompleja = Complejo.producto(potenciaCompleja, tensionBarraConjugada);

				double Q = -potenciaCompleja.getImag();

				if (b.containsCarga()) {

					 potenciaReactivaCarga= b.getCarga().getPotenciaReactiva() / SPController.BASE_SISTEMA;
					 potenciaRealCarga= b.getCarga().getPotenciaActiva() / SPController.BASE_SISTEMA;
				}

				if ( (Q+potenciaReactivaCarga) > b.getGenerador().getMVarSalidaMax() / SPController.BASE_SISTEMA) {
					
					b.getGenerador().setMVarSalida(b.getGenerador().getMVarSalidaMax());
					
					double potenciaActivaEntregada= b.getGenerador().getMWSalida()-potenciaRealCarga;
					double potenciaImCalculada= b.getGenerador().getMVarSalida()-potenciaReactivaCarga;
					
					Carga c= new Carga(); // La carga combinada del generador y la carga de la barra;
					
					c.setPotenciaActiva(potenciaActivaEntregada*SPController.BASE_SISTEMA);
					c.setPotenciaReactiva(potenciaImCalculada*SPController.BASE_SISTEMA);	
				
					b.setCargaFromPVtoPQ(c);			
					b.setBarraPV(false);
					b.setBarraPQ(true);
					b.setBarraFromPV2PQ(true);
					
				} 
			}

		}

	}

	public int getIteConvergencia() {
		return iteConvergencia;
	}

	public void setIteConvergencia(int iteConvergencia) {
		this.iteConvergencia = iteConvergencia;
	}

	private void imprimir(double[][] m, String title) {

		System.out.println(title);

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {

				System.out.print(m[i][j] + " ");
			}

			System.out.println();

		}

	}

	/**
	 * Calcula los indices que se deben quitar de la jacobiana.
	 * 
	 * @return
	 */

	private List<Integer> dropIndices() {

		List<Integer> indices = new ArrayList<>();

		for (int i = 0; i < indicesBarrasCompPV.size(); i++) {

			int index = indicesBarrasCompPV.get(i);

			Barras b = barras.get(index);

			if (b.isBarraCompensacion()) {

				indices.add(index - 1);
				indices.add(index + barras.size() - 2);

			}

			else if (b.isBarraPV()) {

				indices.add(index + barras.size() - 2);

			}

		}

		return indices;

	}

	private void correcionParametros(int[] indicesBarras) {

		for (int i = 0; i < indicesBarras.length; i++) {

			int barra = indicesBarras[i] + 1;

			double errorParametroCalculado = erroresParametros[i];

			if (indicesBarras[i] < jacobiana.length / 2) {

				double anguloPrevio = infoIteracionesAngulos[barra].get(infoIteracionesAngulos[barra].size() - 1);

				infoIteracionesAngulos[barra].add((anguloPrevio + errorParametroCalculado));

			}

			else {
				double voltajePrevio = infoIteracionesVoltajes[barra - (barras.size() - 1)]
						.get(infoIteracionesVoltajes[barra - (barras.size() - 1)].size() - 1);

				double resultado = voltajePrevio * (1 + (errorParametroCalculado / voltajePrevio));

				infoIteracionesVoltajes[barra - (barras.size() - 1)].add(resultado);

			}

		}

	}

	private void calcularPotencias(int index, Barras b) {

		int i = index - 1;

		double potenciaCalculada = 0.0;
		double potenciaProgramadaReal = 0.0;

		if (b.isBarraPV()) {
			if (b.containsGenerador())
				potenciaProgramadaReal = b.getGenerador().getMWSalida() / SPController.BASE_SISTEMA;
		}

		if (b.containsCarga() && !b.isBarraFromPV2PQ()) {
			potenciaProgramadaReal -= b.getCarga().getPotenciaActiva() / SPController.BASE_SISTEMA;
		}
		
		if(b.isBarraFromPV2PQ()) {
			
			potenciaProgramadaReal= b.getCargaFromPVtoPQ().getPotenciaActiva()/SPController.BASE_SISTEMA;
			
		}

		Complejo admitancia = matrizAdj[index][index];

		double resis = admitancia.getReal();

		potenciaCalculada = infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1)
				* infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1) * resis;

		// #######################################
		// Potencia real
		// ####################################

		for (int j = 1; j < barras.size(); j++) {

			if (j != index) {

				double V_i = infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1);
				double V_j = infoIteracionesVoltajes[j].get(infoIteracionesVoltajes[j].size() - 1);
				double magY_i_j = matrizAdj[index][j].modulo();

				double angY_i_j = matrizAdj[index][j].argumento();
				angY_i_j = (angY_i_j * Math.PI) / 180.0;

				double angV_j = infoIteracionesAngulos[j].get(infoIteracionesAngulos[j].size() - 1);
				// angV_j=(angV_j*Math.PI)/180.0;

				double angV_i = infoIteracionesAngulos[index].get(infoIteracionesAngulos[index].size() - 1);
				// angV_i= (angV_i*Math.PI)/180.0;

				potenciaCalculada += (V_i * V_j * magY_i_j * Math.cos(angY_i_j + angV_j - angV_i));

			}

		}

		double potenciaRealError = potenciaProgramadaReal - potenciaCalculada;

		erroresPotencia[i] = potenciaRealError;

		// #######################################
		// Potencia imaginaria
		// #######################################

		if (b.isBarraPV()) {
			return;
		}
		
		
		double potenciaImaginariaCalculada = 0.0;
		double potenciaImaginariaProgramada = 0.0;

		if (b.containsCarga() && ! b.isBarraFromPV2PQ()) {
			potenciaImaginariaProgramada = potenciaImaginariaProgramada
					- b.getCarga().getPotenciaReactiva() / SPController.BASE_SISTEMA;
		}
		
		if( b.isBarraFromPV2PQ()) {
			
			potenciaImaginariaProgramada= b.getCargaFromPVtoPQ().getPotenciaReactiva() / SPController.BASE_SISTEMA;
			
		}
		
		//		if (b.containsBanco()) {
		//			potenciaImaginariaProgramada = potenciaImaginariaProgramada - b.getBanco().getPotenciaReactiva()/SPController.BASE_SISTEMA;
		//		}

		double react = admitancia.getImag();

		potenciaImaginariaCalculada = (-1)
				* infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1)
				* infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1) * react;

		for (int j = 1; j < barras.size(); j++) {

			if (j != index) {
				double V_i = infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1);
				double V_j = infoIteracionesVoltajes[j].get(infoIteracionesVoltajes[j].size() - 1);
				double magY_i_j = matrizAdj[index][j].modulo();

				double angY_i_j = matrizAdj[index][j].argumento();
				angY_i_j = (angY_i_j * Math.PI) / 180.0;

				double angV_j = infoIteracionesAngulos[j].get(infoIteracionesAngulos[j].size() - 1);
				// angV_j=(angV_j*Math.PI)/180.0;

				double angV_i = infoIteracionesAngulos[index].get(infoIteracionesAngulos[index].size() - 1);
				// angV_i= (angV_i*Math.PI)/180.0;

				potenciaImaginariaCalculada -= (V_i * V_j * magY_i_j * Math.sin(angY_i_j + angV_j - angV_i));

			}

		}

		erroresPotencia[i + barras.size() - 1] = potenciaImaginariaProgramada - potenciaImaginariaCalculada;

	}

	private void calcularJacobiana(int index, Barras b) {

		int i = index - 1;

		// ############################

		// Calculo Jacobiana11

		// ###########################
		double resultadoDiagonal = 0.0;

		for (int j = 0; j < barras.size() - 1; j++) {

			if (j != i) {

				double V_i = infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1);
				double V_j = infoIteracionesVoltajes[j + 1].get(infoIteracionesVoltajes[j + 1].size() - 1);
				double magY_i_j = matrizAdj[index][j + 1].modulo();

				double angY_i_j = matrizAdj[index][j + 1].argumento();
				angY_i_j = (angY_i_j * Math.PI) / 180.0;

				double angV_j = infoIteracionesAngulos[j + 1].get(infoIteracionesAngulos[j + 1].size() - 1);
				// angV_j=(angV_j*Math.PI)/180.0;

				double angV_i = infoIteracionesAngulos[index].get(infoIteracionesAngulos[index].size() - 1);
				// angV_i= (angV_i*Math.PI)/180.0;

				double resultado = (-1) * (V_i * V_j * magY_i_j * Math.sin(angY_i_j + angV_j - angV_i));

				jacobiana[i][j] = resultado;

				resultadoDiagonal -= resultado;

			}

		}

		jacobiana[i][i] = resultadoDiagonal;

		// ############################

		// Calculo Jacobiana21

		// ###########################

		i = index - 1 + barras.size() - 1;

		resultadoDiagonal = 0.0;

		for (int j = 0; j < barras.size() - 1; j++) {

			if (j != index - 1) {

				double V_i = infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1);
				double V_j = infoIteracionesVoltajes[j + 1].get(infoIteracionesVoltajes[j + 1].size() - 1);
				double magY_i_j = matrizAdj[index][j + 1].modulo();

				double angY_i_j = matrizAdj[index][j + 1].argumento();
				angY_i_j = (angY_i_j * Math.PI) / 180.0;

				double angV_j = infoIteracionesAngulos[j + 1].get(infoIteracionesAngulos[j + 1].size() - 1);
				// angV_j=(angV_j*Math.PI)/180.0;

				double angV_i = infoIteracionesAngulos[index].get(infoIteracionesAngulos[index].size() - 1);
				// angV_i= (angV_i*Math.PI)/180.0;

				double resultado = (-1) * (V_i * V_j * magY_i_j * Math.cos(angY_i_j + angV_j - angV_i));

				jacobiana[i][j] = resultado;

				resultadoDiagonal -= resultado;

			}

		}

		jacobiana[i][index - 1] = resultadoDiagonal;

		// ############################

		// Calculo Jacobiana12

		// ###########################

		i = index - 1;

		resultadoDiagonal = 0.0;

		for (int j = barras.size() - 1; j < jacobiana.length; j++) {

			if (j - (barras.size() - 1) != i) {

				jacobiana[i][j] = (-1) * jacobiana[i + barras.size() - 1][j - (barras.size() - 1)];

			}

			else if (j - (barras.size() - 1) == i) {

				Complejo admitancia = matrizAdj[index][index];

				double resis = admitancia.getReal();

				jacobiana[i][j] = jacobiana[i + barras.size() - 1][j - (barras.size() - 1)]
						+ 2 * infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1)
						* infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1) * resis;

			}

		}

		// ############################

		// Calculo Jacobiana22

		// ###########################

		i = index - 1 + barras.size() - 1;

		for (int j = barras.size() - 1; j < jacobiana.length; j++) {

			if (j != i) {

				jacobiana[i][j] = jacobiana[i - (barras.size() - 1)][j - (barras.size() - 1)];

			}

			else if (j == i) {

				Complejo admitancia = matrizAdj[index][index];

				double react = admitancia.getImag();

				jacobiana[i][j] = -jacobiana[i - (barras.size() - 1)][j - (barras.size() - 1)]
						- 2 * infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1)
						* infoIteracionesVoltajes[index].get(infoIteracionesVoltajes[index].size() - 1) * react;

			}

		}

	}

}
