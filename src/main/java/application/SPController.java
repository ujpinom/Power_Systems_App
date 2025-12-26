package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import edu.princeton.cs.algs4.Stack;
import grafos.Edges;
import grafos.MyGraph;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.CompensadorEstatico;
import proyectoSistemasDePotencia.Complejo;
import proyectoSistemasDePotencia.DibujarCarga;
import proyectoSistemasDePotencia.ExcepcionDivideCero;
import proyectoSistemasDePotencia.FallaAsimetricaLineas;
import proyectoSistemasDePotencia.FallaAsimetricas;
import proyectoSistemasDePotencia.FallaLineaALinea;
import proyectoSistemasDePotencia.FallaLineaALineaLinea;
import proyectoSistemasDePotencia.FallaLineaALineaTierra;
import proyectoSistemasDePotencia.FallaLineaALineaTierraEnLinea;
import proyectoSistemasDePotencia.FallaTrifasica;
import proyectoSistemasDePotencia.FallaTrifasicaLinea;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.InfoBarras;
import proyectoSistemasDePotencia.InfoCarga;
import proyectoSistemasDePotencia.InfoGeneradores;
import proyectoSistemasDePotencia.InfoImpedanciaFalla;
import proyectoSistemasDePotencia.InfoImpedanciaFallaLinea;
import proyectoSistemasDePotencia.InfoLineas;
import proyectoSistemasDePotencia.InfoTrafo;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.TapsTrafoController;
import proyectoSistemasDePotencia.Transformador;
import proyectoSistemasDePotencia.Zbarra;
import proyectoSistemasDePotencia.infoBanco;
import weightedGraphs.WeightEdeges;
import weightedGraphs.WeightedGraph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SPController implements Initializable {

	 @FXML
	  private ToggleButton Compensador;
	
	
	@FXML
	private TextArea infoFlujo;

	@FXML
	private MenuItem Action1;

	@FXML
	private MenuItem GaussSeidel;

	@FXML
	private MenuItem matrizSecuencia;

	@FXML
	private MenuItem flujoTabular;

	@FXML
	private MenuItem fallaTabular;

	@FXML
	private AnchorPane areaDibujo = new AnchorPane();
	DoubleProperty myScale = new SimpleDoubleProperty(1.0);
	@FXML
	private TextField infoElemento;

	@FXML
	private TextField infoTare;

	@FXML
	private TextField infoPosiMouse;
	@FXML
	private TextArea display;

	@FXML
	private ToggleButton barra;

	@FXML
	private Button undo;

	@FXML
	private Button undo1;

	@FXML
	private ToggleButton banco;

	@FXML
	private ToggleButton generador;

	@FXML
	private ToggleButton trafo;

	@FXML
	private ToggleButton none;

	@FXML
	private Button ejecutar;

	@FXML
	private ToggleButton linea;

	@FXML
	private ToggleButton carga;

	@FXML
	private ToggleGroup group;

	@FXML
	private TextField factorAceleracion;

	public static double BASE_SISTEMA = 100;
	private Complejo[][] perdidadsPotencia;
	private Complejo [][] potenciaEntranteBarras;
	
	private List<Complejo>[] perdidasLineas;
	private boolean lselected = false;
	private boolean tselected = false;
	private boolean gselected = false;
	private boolean bselected = false;
	private boolean noneselected = false;
	private boolean compensadorSelected=false;
	private boolean cSelected;
	private boolean bancoSelected;
	private boolean trifasica;
	private boolean monofasica;
	private boolean lineaALinea;
	private boolean bifasicaATierra;
	private boolean fPotencia;
	private ArrayList<Barras> barras = new ArrayList<>();
	private ArrayList<Lineas> conexiones = new ArrayList<>();
	private ArrayList<Transformador> conexiones1 = new ArrayList<>();
	private ArrayList<Generadores> conexiongene = new ArrayList<>();
	private ArrayList<Carga> cargas = new ArrayList<>();
	private ArrayList<Bancos> bancos = new ArrayList<>();
	private ArrayList<CompensadorEstatico> compensadores = new ArrayList<>();
	private double endOfLineX, endOfLineY;
	private Barras startB = null;
	private boolean isLineOn = false;
	private int radioCirculo = 14;
	private String nombreBarra = "B";
	private String nombreLinea = "L";
	private String nombreTrafo = "T";
	private String nombreGenerador = "G";
	private String nombreCarga = "C";
	private String nombreBanco = "BA";
	private String nombreCompensador="E";
	
	private ArrayList<Double> distanciasLineas = new ArrayList<>();
	private ArrayList<Double> corGenerador = new ArrayList<>();
	private ArrayList<Double> corCarga = new ArrayList<>();
	private ArrayList<Double> corBanco = new ArrayList<>();
	private ArrayList<Double> corCompensador= new ArrayList<>();
	private ObservableList<Node> lista;
	private ArrayList<Double> posBarra = new ArrayList<>();
	private WeightedGraph<Barras> grafo1;
	private WeightedGraph<Barras> grafo2;
	private WeightedGraph<Barras> grafo0;
	private double impedanciaDeFalla;
	private String tipoElementoFallado;
	private ArrayList<Double> coorFalla = new ArrayList<>();
	private boolean fallaEnLinea = false;
	private Lineas lineaFallada;
	private Barras barraFallada;
	private double xCoorG;
	private double yCoorG;
	private double magCorrientePuntoFallaFaseA;
	private double magCorrientePuntoFallaFaseB;
	private double magCorrientePuntoFallaFaseC;
	private double angCorrientePuntoFallaFaseA;
	private double angCorrientePuntoFallaFaseB;
	private double angCorrientePuntoFallaFaseC;
	private double largoBarra = 70;
	private double ancho = 5;
	private static boolean borrarUltimoElemento = false;
	private double FACTOR_ACELERACION = 1.6;
	private int NUMERO_ITERACIONES = 50; // numero de iteraciones para el problema de flujo de potencia.

	private LinkedList<Object> objetosCreados = new LinkedList<>(); // Almacena todos los objectos creados para llevar
	// un record de ellos;
	private LinkedList<Object> restablecerElementos = new LinkedList<>();

	private String metodoFlujoPotencia = "Seidel";

	private double epsilon = 0.01;

	private Barras barraCompensacion = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Metodo destinado para Newton-raphson
	 * 
	 * @param e
	 * @throws IOException
	 */

	@FXML
	public void Action1(ActionEvent e) throws IOException {

		matrizSecuencia.setDisable(true);
		fallaTabular.setDisable(true);
		fPotencia = true;
		metodoFlujoPotencia = "Raphson";
		infoTare.setText("Tarea: Flujo de Potencia: Newton-Raphson");

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MatricesSeq.fxml"));
		Parent root = fxmlLoader.load();

		Scene scene = new Scene(root);

		Stage s = new Stage();

		MatricesController p = fxmlLoader.getController();
		p.initialize(this);
		p.setTexte("" + epsilon);
		p.setTextfac("" + FACTOR_ACELERACION);
		p.setTextIter("" + NUMERO_ITERACIONES);

		s.setTitle("Especificaciones Flujo de Potencia");
		s.setScene(scene);
		s.show();
		s.setResizable(false);

		setTextInfoPotencia();
	}

	@FXML
	public void GaussSeidel(ActionEvent e) throws IOException {

		matrizSecuencia.setDisable(true);
		fallaTabular.setDisable(true);
		fPotencia = true;
		metodoFlujoPotencia = "Seidel";
		infoTare.setText("Tarea: Flujo de Potencia: Gauss-Seidel");

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MatricesSeq.fxml"));
		Parent root = fxmlLoader.load();

		Scene scene = new Scene(root);

		Stage s = new Stage();

		MatricesController p = fxmlLoader.getController();
		p.initialize(this);
		p.setTexte("" + epsilon);
		p.setTextfac("" + FACTOR_ACELERACION);
		p.setTextIter("" + NUMERO_ITERACIONES);

		s.setTitle("Especificaciones Flujo de Potencia");
		s.setScene(scene);
		s.show();
		s.setResizable(false);

		setTextInfoPotencia();

	}

	public void setTextInfoPotencia() {
		infoFlujo.setText("MÃ©todo: " + metodoFlujoPotencia + "\nAceleraciÃ³n: " + FACTOR_ACELERACION + "\nIteraciones: "
				+ String.format("%d", NUMERO_ITERACIONES) + "\nEpsilon: " + epsilon + "\nConvergencia: " + 0);
	}

	@FXML
	public void mouseMoved(MouseEvent e) {

		double x = e.getX();
		double y = e.getY();

		infoPosiMouse.setText(String.format("X=%.2f     Y=%.2f", x, y));

	}

	@FXML
	void fallaTrifasica(ActionEvent event) {

		trifasica = true;
		monofasica = false;
		lineaALinea = false;
		bifasicaATierra = false;
		fPotencia = false;

		infoTare.setText("Tarea: Falla TrifÃ¡sica");

		matrizSecuencia.setDisable(false);
		fallaTabular.setDisable(false);
		fPotencia = false;

	}

	@FXML
	void fallaMono(ActionEvent event) {
		fPotencia = false;
		trifasica = false;
		monofasica = true;
		lineaALinea = false;
		bifasicaATierra = false;
		fPotencia = false;

		infoTare.setText("Tarea: Falla MonofÃ¡sica");

		matrizSecuencia.setDisable(false);
		fallaTabular.setDisable(false);

	}

	@FXML
	void fallaLaL(ActionEvent event) {
		fPotencia = false;

		trifasica = false;
		monofasica = false;
		lineaALinea = true;
		bifasicaATierra = false;
		fPotencia = false;

		infoTare.setText("Tarea: Falla LÃ­nea a LÃ­nea");

		matrizSecuencia.setDisable(false);
		fallaTabular.setDisable(false);
	}

	@FXML
	void fallaLLTierra(ActionEvent event) {
		fPotencia = false;

		trifasica = false;
		monofasica = false;
		lineaALinea = false;
		bifasicaATierra = true;
		fPotencia = false;

		infoTare.setText("Tarea: Falla LÃ­nea-LÃ­nea a tierra");

		matrizSecuencia.setDisable(false);
		fallaTabular.setDisable(false);
	}

	@FXML
	private void lineaSelected(ActionEvent e) {
		lselected = true;
		tselected = false;
		gselected = false;
		bselected = false;
		noneselected = false;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: LÃ­nea");
	}

	@FXML
	private void barraSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = false;
		bselected = true;
		noneselected = false;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: Barra");

	}
	
	@FXML
	private void compensadorSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = false;
		bselected = false;
		noneselected = false;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=true;
		infoElemento.setText("Elemento: Compensador EstÃ¡tico");
	
	}
	@FXML
	private void trafoSelected(ActionEvent e) {
		lselected = false;
		tselected = true;
		gselected = false;
		bselected = false;
		noneselected = false;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: Transformador");
	}

	@FXML
	private void genSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = true;
		bselected = false;
		noneselected = false;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: Generador");
	}

	@FXML
	private void cargaSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = false;
		bselected = false;
		noneselected = false;
		cSelected = true;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: Carga");
	}

	@FXML
	private void bancoSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = false;
		bselected = false;
		noneselected = false;
		cSelected = false;
		bancoSelected = true;
		compensadorSelected=false;
		infoElemento.setText("Elemento: Banco");
	}

	@FXML
	private void editionSelected(ActionEvent e) {
		lselected = false;
		tselected = false;
		gselected = false;
		bselected = false;
		noneselected = true;
		cSelected = false;
		bancoSelected = false;
		compensadorSelected=false;
		infoElemento.setText("Elemento: EdiciÃ³n");
	}

	@FXML
	private void borrarUltimoElemento(ActionEvent e) {

		borrarUltimoElemento();

	}

	@FXML
	private void limpiarArea(ActionEvent e) {

		limpiarArea();
	}

	@FXML
	private void ejecutar(ActionEvent e) throws ExcepcionDivideCero {

		List<List<Integer>> bb = getGraph().componentesConectados();

		if (bb.size() == 2) {

			if (fPotencia) {

				Complejo[][] m3 = calculoMatrizAdyacenciaFlujo();
		
				perdidadsPotencia = new Complejo[barras.size()][barras.size()];
				potenciaEntranteBarras= new Complejo[barras.size()][barras.size()];
				
				for(int i=0;i<perdidadsPotencia.length;i++) {
					for(int j=0;j<perdidadsPotencia.length;j++) {
						
						perdidadsPotencia[i][j]= new Complejo(); // No se tiene en cuenta la admitancia de carga de la lÃ­nea
						potenciaEntranteBarras[i][j]= new Complejo();  // Se tiene en cuenta la admitancia de carga de la linea.
					
					}
				}
				

				if (metodoFlujoPotencia.equals("Seidel")) {

					EcuacionesVoltajeYPotencia gaussS = new EcuacionesVoltajeYPotencia(barras, NUMERO_ITERACIONES,
							FACTOR_ACELERACION, epsilon);

					List<Complejo>[] solucion = gaussS.calcularFlujoPotencia(conexiongene, bancos, cargas, m3);
					
					DeterminacionPotenciasBarras.potenciasEnBarrasComPV(barras, solucion, m3);
					
			
					
		
					DeterminacionPotenciasBarras.calculoPerdidasPotenciaLineas(barras,solucion, conexiones, conexiones1,m3,perdidadsPotencia,potenciaEntranteBarras);
					
					
					
			
					System.out.println("RESULTADOS Angulos Y voltajes:");

					for (int i = 1; i < solucion.length; i++) {

						for (Complejo c : solucion[i]) {

							System.out.println("Barra" + i);

							System.out.println(c.modulo() + "  " + c.argumento());

						}

						System.out.println();

					}
					
					System.out.println();

					for(int i=1;i<barras.size();i++) {
						
						Barras b= barras.get(i);
						
						if(b.isBarraCompensacion()) {
							
							System.out.println("Generacion: ");
							
							System.out.println("Real: "+b.getFlowPowerRealCalculada()+" Imag: "+b.getFlowPowerImagCalculada());
							
							
						}
						
						if(b.isBarraPV()) {
							
							
							System.out.println("Generacion: ");
							
							System.out.println("Real: "+b.getGenerador().getMWSalida()+" Imag: "+b.getFlowPowerImagCalculada());
							
							
						}
						
						
						if(b.isBarraFromPV2PQ()) {
							
							System.out.println("Generacion: ");
							
							System.out.println("Real: "+b.getGenerador().getMWSalida()+" Imag: "+b.getGenerador().getMVarSalida());
							
						}
				
						
						if(b.containsCarga()) {
							
							System.out.println("Carga: ");
							
							System.out.println("Real: "+b.getCarga().getPotenciaActiva()+" Imag: "+b.getCarga().getPotenciaReactiva());
							
						}
					
					}
					
					
					
					System.out.println("Potencia en barras");
					
					for(int i=0;i<perdidadsPotencia.length;i++) {
						for(int j=0;j<perdidadsPotencia.length;j++) {
							
							System.out.print(potenciaEntranteBarras[i][j]+" ");
							
							
						}
						
						System.out.println();
					}
					
					
					////

				}

				else if (metodoFlujoPotencia.equals("Raphson")) {

					NewtonRaphson raphson = new NewtonRaphson(barras, NUMERO_ITERACIONES, epsilon, m3);

					raphson.calcularFlujoPotencia(conexiongene, bancos, cargas);

					List<Double>[] solucionVoltajes = raphson.solucionVoltajes();

					List<Double>[] solucionAngulos = raphson.solucionAngulos();
					
					List<Complejo>[] solucion=raphson.solucionFormaCompleja();
					
					DeterminacionPotenciasBarras.potenciasEnBarrasComPV(barras, solucion, m3);
		
	
					DeterminacionPotenciasBarras.calculoPerdidasPotenciaLineas(barras,solucion, conexiones,conexiones1 ,m3,perdidadsPotencia,potenciaEntranteBarras); 
					
		

					System.out.println("\n\nSoluciones: ");

					for (int i = 1; i < solucionVoltajes.length; i++) {
						System.out.println("BarraVoltaje" + i);

						for (Double d : solucionVoltajes[i]) {

							System.out.print(d + " ");

						}

						System.out.println();

					}

					System.out.println();

					for (int i = 1; i < solucionAngulos.length; i++) {
						System.out.println("BarraAngulo" + i);

						for (Double d : solucionAngulos[i]) {

							System.out.print(d*180/Math.PI + " ");

						}

						System.out.println();

					}
					
					System.out.println();

					for(int i=1;i<barras.size();i++) {
						
						Barras b= barras.get(i);
						
						if(b.isBarraCompensacion()) {
							
							System.out.println("Generacion: ");
							
							System.out.println("Real: "+b.getFlowPowerRealCalculada()+" Imag: "+b.getFlowPowerImagCalculada());
							
							
						}
						
						else if(b.isBarraPV()) {
							
							
							System.out.println("Generacion: ");
							
							System.out.println("Real: "+b.getGenerador().getMWSalida()+" Imag: "+b.getFlowPowerImagCalculada());
							
							
						}
						
					if(b.containsCarga()) {
							
							System.out.println("Carga: ");
							
							System.out.println("Real: "+b.getCarga().getPotenciaActiva()+" Imag: "+b.getCarga().getPotenciaReactiva());
							
						}
				
					}
					
					
					System.out.println("Potencia en barras");
					
					for(int i=0;i<perdidadsPotencia.length;i++) {
						for(int j=0;j<perdidadsPotencia.length;j++) {
							
							System.out.print(potenciaEntranteBarras[i][j]+" ");
							
							
						}
						
						System.out.println();
					}


				}

			} else {
				crearGrafos();

			}
		} else {

			JOptionPane.showMessageDialog(null, "El sistema no es cerrado. RÃ©viselo por favor.");
		}

	}

	public double getFACTOR_ACELERACION() {
		return FACTOR_ACELERACION;
	}

	public void setFACTOR_ACELERACION(double fACTOR_ACELERACION) {
		FACTOR_ACELERACION = fACTOR_ACELERACION;
	}

	public int getNUMERO_ITERACIONES() {
		return NUMERO_ITERACIONES;
	}

	public void setNUMERO_ITERACIONES(int nUMERO_ITERACIONES) {
		NUMERO_ITERACIONES = nUMERO_ITERACIONES;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	private ObservableList<Double> listPuntosPolyLine;

	private boolean isLineOn2 = false;

	private Point2D ultimoPunto;

	private Polyline poliactual;

	private Path path;

	private LinkedList<Integer>[] listaBarras = (LinkedList<Integer>[]) new LinkedList[100];

	public SPController() {

		barras.add(new Barras("Tierra"));

		for (int i = 0; i < listaBarras.length; i++) {

			listaBarras[i] = new LinkedList<Integer>();
		}

	}

	private void dibujarPOly() {

		if (isLineOn && lselected) {
			Line perro = new Line();

			perro.setStartX(ultimoPunto.getX());

			perro.setStartY(ultimoPunto.getY());

			perro.setEndX(endOfLineX);

			perro.setEndY(endOfLineY);

			areaDibujo.getChildren().addAll(perro, path);
		}

		int cont = 0;
		for (Lineas l : conexiones) {

			l.setNombreLinea(nombreLinea + (++cont));
			MoveTo mt = new MoveTo(l.getBarra1().getPuntoMedioBarra().getX(),
					l.getBarra1().getPuntoMedioBarra().getY());

			LineTo lt = new LineTo(l.getBarra2().getPuntoMedioBarra().getX(),
					l.getBarra2().getPuntoMedioBarra().getY());

			ObservableList<PathElement> pt = l.getPath().getElements();

			Path path = new Path();
			path.setStrokeWidth(1.5);
			path.setStroke(Color.GREEN);
			path.getElements().add(mt);

			for (int i = 1; i < pt.size() - 1; i++) {

				path.getElements().add(pt.get(i));

			}

			path.getElements().add(lt);

			double puntoMedioX = 0.0;
			double puntoMedioY;

			if (path.getElements().size() == 2) {
				MoveTo m1 = (MoveTo) path.getElements().get(0);
				LineTo m2 = (LineTo) path.getElements().get(1);

				double m1X = m1.getX();
				double m1Y = m1.getY();
				double m2X = m2.getX();
				double m2Y = m2.getY();

				puntoMedioX = Math.abs(m1X + m2X) / 2;
				puntoMedioY = Math.abs(m1Y + m2Y) / 2;
			} else {

				LineTo m1 = (LineTo) path.getElements().get(1);
				LineTo m2 = (LineTo) path.getElements().get(2);
				double m1X = m1.getX();
				double m1Y = m1.getY();
				double m2X = m2.getX();
				double m2Y = m2.getY();

				puntoMedioX = Math.abs(m1X + m2X) / 2;
				puntoMedioY = Math.abs(m1Y + m2Y) / 2;
			}

			l.setPuntomedio(new Point2D(puntoMedioX, puntoMedioY));

			Rectangle rec = new Rectangle();
			rec.setX(puntoMedioX - 15);
			rec.setY(puntoMedioY - 15);
			rec.setWidth(30);
			rec.setHeight(30);
			rec.setStroke(Color.RED);
			rec.setFill(Color.WHITE);

			Text textLinea = new Text(l.getNombreLinea());
			textLinea.setStrokeWidth(1);
			textLinea.setStroke(Color.RED);

			textLinea.setX(puntoMedioX - 3);
			textLinea.setY(puntoMedioY + 2);

			areaDibujo.getChildren().addAll(path, rec, textLinea);

		}

	}

	public void repaint() {

		areaDibujo.getChildren().clear();
		dibujarBarra();
		dibujarLineas();
		dibujarGenerador();
		dibujarFalla();
		dibujarCarga();
		dibujarCompensador();
		dibujarBanco();
		dibujarPOly();
		coorFalla.clear();
		posBarra.clear();

	}

	@FXML
	private void mouseClicked(MouseEvent e) throws IOException {
		lista = areaDibujo.getChildren();
		areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
		double x = e.getX();
		double y = e.getY();
		Node tipoElemento = tipoElemento(x, y);

		if (e.getButton() == MouseButton.SECONDARY) {

			Barras b = getContainingVertex(x, y);
			tipoElemento = tipoElemento(x, y);

			if ((tipoElemento instanceof Rectangle) && ((Rectangle) tipoElemento).getWidth() != 30) {

				String nombreBarra;

				if (b.getNombrePersonalizado() == null) {
					nombreBarra = b.getNombreBarra();
				} else {
					nombreBarra = b.getNombrePersonalizado();
				}

				if (fPotencia) {
					display.setText("Elemento:   " + nombreBarra + "    Voltaje: " + b.getVoltajePrefalla() + " [p,u]"
							+ "    Ãngulo: " + b.getAnguloVoltajeBarra() + "    Barra de CompensaciÃ³n: "
							+ b.isBarraCompensacion() + "\n" + b.isBarraCompensacion() + "  " + b.isBarraPQ() + "  "
							+ b.isBarraPV());
				} else {
					display.setText("Elemento:   " + nombreBarra + "    Voltaje: " + b.getVoltajePrefalla() + " [p,u]"
							+ "\nCorriente punto de falla (Elemento " + tipoElementoFallado + ")" + ":" + "\nFase A: "
							+ String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
							+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° " + "[p,u]         Fase B: "
							+ String.format("%.4f", magCorrientePuntoFallaFaseB) + " Ang. "
							+ String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° " + "[p,u]         Fase C: "
							+ String.format("%.4f", magCorrientePuntoFallaFaseC) + " Ang. "
							+ String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
							+ "\nVoltaje/Fase Post-Falla:  " + "\nFase A: "
							+ String.format("%.4f", b.getVoltajePosFallaFaseA()) + " Ang. "
							+ String.format("%.4f", b.getAnguloVoltajeFaseA()) + "Â° " + "[p,u]         Fase B: "
							+ String.format("%.4f", b.getVoltajePosFallaFaseB()) + " Ang. "
							+ String.format("%.4f", b.getAnguloVoltajeFaseB()) + "Â° " + "[p,u]         " + "Fase C: "
							+ String.format("%.4f", b.getVoltajePosFallaFaseC()) + " Ang. "
							+ String.format("%.4f", b.getAnguloVoltajeFaseC()) + "Â° " + "[p,u]");
				}

				if (e.isShiftDown()) {

					InfoBarras infoBarra = new InfoBarras(b);
					Scene dad = new Scene(infoBarra);
					Stage sta = new Stage();

					sta.setScene(dad);
					sta.setTitle("INFORMACIÃN DE BARRAS");
					sta.setResizable(false);
					sta.initModality(Modality.APPLICATION_MODAL);
					sta.showAndWait();
					b.setVoltajePrefalla(infoBarra.getvoltajePrefalla());

					if (b.isBarraCompensacion()) {
						b.setBarraPQ(false);
						b.setBarraPV(false);
						if (barraCompensacion == null)
							barraCompensacion = b;
						else if (barraCompensacion != null && barraCompensacion != b) {

							if ((barraCompensacion.containsBanco()
									|| barraCompensacion.containsCarga()) && (!barraCompensacion.containsGenerador() && !barraCompensacion.containsCompensador())) {
								int index = barras.indexOf(barraCompensacion);

								barras.get(index).setBarraPQ(true);
								barras.get(index).setBarraPV(false);
								barras.get(index).setBarraCompensacion(false);
								barraCompensacion = b;

							}

							else if (barraCompensacion.containsGenerador() || barraCompensacion.containsCompensador()) {
								int index = barras.indexOf(barraCompensacion);

								barras.get(index).setBarraPQ(false);
								barras.get(index).setBarraPV(true);
								barras.get(index).setBarraCompensacion(false);
								barraCompensacion = b;

							}
							
							else if (!barraCompensacion.containsBanco()
									&&! barraCompensacion.containsCarga() && !barraCompensacion.containsGenerador()) {
								int index = barras.indexOf(barraCompensacion);

								barras.get(index).setBarraPQ(true);
								barras.get(index).setBarraPV(false);
								barras.get(index).setBarraCompensacion(false);
								barraCompensacion = b;

							}

						}

					}

					if (!b.getOrientacion().equals(b.getOrientacionPrimaria())) {

						if (b.getOrientacion().equals("H")) {

							double xCoor = b.getXbarra() - b.getLargo() / 2;
							double yCoor = b.getYbarra() + b.getLargo() / 2 - b.getAncho() / 2;
							b.setXbarra(xCoor);
							b.setYbarra(yCoor);
							b.setAncho(largoBarra);
							b.setLargo(ancho);
							b.setOrientacionPrimaria(b.getOrientacion());

						} else if (b.getOrientacion().equals("V")) {

							double xCoor = b.getXbarra() + b.getAncho() / 2 - ancho / 2;
							double yCoor = b.getYbarra() - largoBarra / 2 + ancho / 2;
							b.setXbarra(xCoor);
							b.setYbarra(yCoor);
							b.setAncho(ancho);
							b.setLargo(largoBarra);
							b.setOrientacionPrimaria(b.getOrientacion());

						}

					}

					repaint();
					return;

				}
			}

			else if (tipoElemento instanceof Line) {

				boolean bandera = false;
				boolean bandera1 = false;
				boolean bandera2 = false;

				if (!bandera) {

					for (int i = 0; i < conexiones1.size(); i++) {

						Point2D pntmedio = conexiones1.get(i).getPuntoMedio();

						if (((Line) tipoElemento).contains(pntmedio)) {
							System.out.println("ENTRO AQUI EN TRAFO");
							display.setText("Elemento:   " + conexiones1.get(i).getNombreLinea() + "   ConexiÃ³n( "
									+ conexiones1.get(i).getConexionPrimaria() + ","
									+ conexiones1.get(i).getConexionSecundaria() + " )"
									+ "\nCorriente punto de falla (Elemento " + tipoElementoFallado + ")" + ":"
									+ "\nFase A: " + String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
									+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° "
									+ "[p,u]         Fase B: " + String.format("%.4f", magCorrientePuntoFallaFaseB)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° "
									+ "[p,u]         Fase C: " + String.format("%.4f", magCorrientePuntoFallaFaseC)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
									+ "\nCorriente/Fase Post-Falla:  " + "\nFase A: "
									+ String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseA()) + "[p,u]"
									+ "         Fase B: "
									+ String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseB())
									+ "[p,u]         " + "Fase C: "
									+ String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseC()) + "[p,u]");

							bandera1 = true;

							
							if (e.isShiftDown()) {
								System.out.println("ENTRO AQUIIII");
								InfoTrafo infotrafo = new InfoTrafo(conexiones1.get(i));
								Scene dad = new Scene(infotrafo);
								Stage sta = new Stage();

								sta.setScene(dad);
								sta.setTitle("INFORMACIÃN DE TRANSFORMADORES");
								sta.setResizable(false);
								sta.initModality(Modality.APPLICATION_MODAL);
								sta.showAndWait();

								conexiones1.get(i).setImpedanciaAterrizamientoPrimaria(
										infotrafo.getImpedanciaAterrizamientoPrimaria());
								conexiones1.get(i).setImpedanciaAterrizamientoSecundaria(
										infotrafo.getImpedanciaAterrizamientoSecundaria());
							
								
								repaint();
							}
							break;

						}

					}
				}

				if (!bandera && !bandera1) {

					for (int i = 0; i < cargas.size(); i++) {

						Point2D pntMedio = cargas.get(i).getPuntoMedio();

						if (((Line) tipoElemento).contains(pntMedio)) {

							display.setText("Elemento:   " + cargas.get(i).getNombreCarga() + "\nPotencia Activa [MW]: "
									+ cargas.get(i).getPotenciaActiva()+ "\nPotencia Reactiva [MVars]: "
									+ cargas.get(i).getPotenciaReactiva());

							bandera2 = true;

							if (e.isShiftDown()) {

								InfoCarga infoCarga = new InfoCarga(cargas.get(i));
								Scene dad = new Scene(infoCarga);
								Stage sta = new Stage();
								sta.setScene(dad);
								sta.setTitle("INFORMACIÃN DE CARGAS");
								sta.setResizable(false);
								sta.initModality(Modality.APPLICATION_MODAL);
								sta.showAndWait();
								repaint();
							}
						}

					}

				}

				if (!bandera && !bandera1 && !bandera2) {

					for (int i = 0; i < bancos.size(); i++) {

						Point2D pntmedio = bancos.get(i).getPuntoMedio();

						if (((Line) tipoElemento).contains(pntmedio)) {

							display.setText("Elemento:   " + bancos.get(i).getNombreCarga()
									+ "\nPotencia Reactiva [MVars]: " + bancos.get(i).getPotenciaReactiva());

							if (e.isShiftDown()) {

								infoBanco infoBanco = new infoBanco(bancos.get(i));
								Scene dad = new Scene(infoBanco);
								Stage sta = new Stage();

								sta.setScene(dad);
								sta.setTitle("INFORMACIÃN DEl BANCO");
								sta.setResizable(false);
								sta.initModality(Modality.APPLICATION_MODAL);
								sta.showAndWait();
								repaint();

							}
						}
					}

				}
			}

			if (tipoElemento instanceof Circle && ((Circle) tipoElemento).getRadius() == 15) {

				for (int i = 0; i < conexiones1.size(); i++) {

					Point2D pntMedio = conexiones1.get(i).getPuntoMedio();

					if (((Circle) tipoElemento).contains(pntMedio)) {

						display.setText("Elemento:   " + conexiones1.get(i).getNombreLinea() + "   ConexiÃ³n( "
								+ conexiones1.get(i).getConexionPrimaria() + ","
								+ conexiones1.get(i).getConexionSecundaria() + " )"
								+ "\nCorriente punto de falla (Elemento " + tipoElementoFallado + ")" + ":"
								+ "\nFase A: " + String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° " + "[p,u]         Fase B: "
								+ String.format("%.4f", magCorrientePuntoFallaFaseB) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° " + "[p,u]         Fase C: "
								+ String.format("%.4f", magCorrientePuntoFallaFaseC) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
								+ "\nCorriente/Fase Post-Falla:  " + "\nFase A: "
								+ String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseA()) + "[p,u]"
								+ "         Fase B: "
								+ String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseB()) + "[p,u]         "
								+ "Fase C: " + String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseC())
								+ "[p,u]");

						if (e.isShiftDown()) {

							InfoTrafo infotrafo = new InfoTrafo(conexiones1.get(i));
							Scene dad = new Scene(infotrafo);
							Stage sta = new Stage();

							sta.setScene(dad);
							sta.setTitle("INFORMACIÃN DE TRANSFORMADORES");
							sta.setResizable(false);
							sta.initModality(Modality.APPLICATION_MODAL);
							sta.showAndWait();

							conexiones1.get(i).setImpedanciaAterrizamientoPrimaria(
									infotrafo.getImpedanciaAterrizamientoPrimaria());
							conexiones1.get(i).setImpedanciaAterrizamientoSecundaria(
									infotrafo.getImpedanciaAterrizamientoSecundaria());
						
							repaint();
							
						}
						break;

					}

				}
				return;

			}

			else if (tipoElemento instanceof Rectangle && ((Rectangle) tipoElemento).getWidth() == 30) {

				for (int i = 0; i < conexiones.size(); i++) {

					Point2D pntMedio = conexiones.get(i).getPuntomedio();

					if (((Rectangle) tipoElemento).contains(pntMedio)) {

						if (e.isShiftDown()) {

							InfoLineas infolinea = new InfoLineas(conexiones.get(i));
							Scene dad = new Scene(infolinea);
							Stage sta = new Stage();
							sta.setScene(dad);
							sta.setTitle("INFORMACIÃN DE LÃNEAS");
							sta.setResizable(false);
							sta.initModality(Modality.APPLICATION_MODAL);
							sta.showAndWait();

						}

						if (!fallaEnLinea || conexiones.get(i) != lineaFallada) {

							display.setText("Elemento:   " + conexiones.get(i).getNombreLinea()
									+ "\nCorriente punto de falla (Elemento " + tipoElementoFallado + ")" + ":"
									+ "\nFase A: " + String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
									+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° "
									+ "[p,u]         Fase B: " + String.format("%.4f", magCorrientePuntoFallaFaseB)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° "
									+ "[p,u]         Fase C: " + String.format("%.4f", magCorrientePuntoFallaFaseC)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
									+ "\nCorriente/Fase Post-Falla:  " + "\nFase A: "
									+ String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseA()) + "[p,u]"
									+ "         Fase B: "
									+ String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseB())
									+ "[p,u]         " + "Fase C: "
									+ String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseC()) + "[p,u]");

						} else {

							display.setText("Elemento:   " + conexiones.get(i).getNombreLinea()
									+ "\nCorriente punto de falla (Elemento 50%-" + tipoElementoFallado + ")" + ":"
									+ "\nFase A: " + String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
									+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° "
									+ "[p,u]         Fase B: " + String.format("%.4f", magCorrientePuntoFallaFaseB)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° "
									+ "[p,u]         Fase C: " + String.format("%.4f", magCorrientePuntoFallaFaseC)
									+ " Ang. " + String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
									+ "\nContribucciÃ³n Barra-" + conexiones.get(i).getBarra1().getNombreBarra() + " : "
									+ "\nFase A: "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseA())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseA())
									+ "Â° " + "[p,u]" + "         Fase B: "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseB())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseB())
									+ "Â° " + "[p,u]" + "         Fase C: "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseC())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseC())
									+ "Â° " + "[p,u]" + "\nContribucciÃ³n Barra-"
									+ conexiones.get(i).getBarra2().getNombreBarra() + " : " + "\nFase A: "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseA())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseA())
									+ "Â° " + "[p,u]" + "         Fase B: "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseB())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseB())
									+ "Â° " + "[p,u]" + "         Fase C: "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseC())
									+ " Ang. "
									+ String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseC())
									+ "Â° " + "[p,u]");

						}

						break;

					}

				}

			}

			else if (tipoElemento instanceof Circle && ((Circle) tipoElemento).getRadius() == 20) {

				double xcenter = ((Circle) tipoElemento).getCenterX();
				for (int i = 0; i < conexiongene.size(); i++) {

					if (conexiongene.get(i).getBarra().getxCoorG() - 30 == xcenter
							|| conexiongene.get(i).getBarra().getxCoorG() + 40 == xcenter || ((Circle) tipoElemento)
							.contains(conexiongene.get(i).getXCenter(), conexiongene.get(i).getYCenter())) {

						Generadores g = conexiongene.get(i);

						display.setText("Elemento:   " + conexiongene.get(i).getNombreGenerador() + " MW.Out: "
								+ g.getMWSalida()  + "  MW.OutMin: " + g.getMWSalidaMin() + "  MW.OutMax: "
								+ g.getMWSalidaMax() + "\n" + "MVar.Out: " + g.getMVarSalida() + "  MVar.OutMin: "
								+ g.getMVarSalidaMin() + "  MVar.OutMax: " + g.getMVarSalidaMax()
								+ "\nCorriente punto de falla (Elemento " + tipoElementoFallado + ")" + ":"
								+ "\nFase A: " + String.format("%.4f", magCorrientePuntoFallaFaseA) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseA) + "Â° " + "[p,u]         Fase B: "
								+ String.format("%.4f", magCorrientePuntoFallaFaseB) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseB) + "Â° " + "[p,u]         Fase C: "
								+ String.format("%.4f", magCorrientePuntoFallaFaseC) + " Ang. "
								+ String.format("%.4f", angCorrientePuntoFallaFaseC) + "Â° " + "[p,u]"
								+ "\nContribucciÃ³n de la mÃ¡quina a la falla: " + "\nFase A: "
								+ String.format("%.2f", conexiongene.get(i).getCorrienteFaseA()) + " Ang. "
								+ String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseA()) + "Â° " + "[p,u]"
								+ "         Fase B: " + String.format("%.2f", conexiongene.get(i).getCorrienteFaseB())
								+ " Ang. " + String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseB()) + "Â° "
								+ "[p,u]         " + "Fase C: "
								+ String.format("%.2f", conexiongene.get(i).getCorrienteFaseC()) + " Ang. "
								+ String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseC()) + "Â° "
								+ "[p,u]");

						if (e.isShiftDown()) {

							InfoGeneradores infog = new InfoGeneradores(conexiongene.get(i));
							Scene dad = new Scene(infog);
							Stage sta = new Stage();

							sta.setScene(dad);
							sta.setTitle("INFORMACIÃN DE GENERADORES");
							sta.setResizable(false);
							sta.initModality(Modality.APPLICATION_MODAL);
							sta.showAndWait();

							conexiongene.get(i).setImpedanciaAterrizamiento(infog.getImpedanciaAterrizamiento());
							repaint();
						}

						break;
					}

				}

			}
			
			else if (tipoElemento instanceof Circle && ((Circle) tipoElemento).getRadius() == 15.5) {
				
				double xcenter = ((Circle) tipoElemento).getCenterX();
				double ycenter = ((Circle) tipoElemento).getCenterY();
				
				for(int i=0;i<compensadores.size();i++) {
					
					Circle circle= compensadores.get(i).getCiculoCompensador();
					
					if(circle.contains(xcenter,ycenter)) {
						
						CompensadorEstatico compensador= compensadores.get(i);
						
						display.setText("Elemento: Compensador EstÃ¡tico " + compensadores.get(i).getNombreCompensador() + " Mvar.Min: "
								+ compensador.getPotenciaReactivaMin() +" [p,u] "+ " Mvar.Max: " + compensador.getPotenciaReactivaMax()+ " [p,u] "
							);
						
						if(e.isShiftDown()) {
							
							
							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CompensadorEstaticoFXML.fxml"));
							Parent root = fxmlLoader.load();

							Scene scene = new Scene(root);

							Stage s = new Stage();

							CompensadorController p = fxmlLoader.getController();
						
							p.initialize(compensador,this);
							p.setTitle(compensador.getNombreCompensador());
							p.setTextMax(""+compensador.getPotenciaReactivaMax());
							p.setTextMin(""+compensador.getPotenciaReactivaMin());

							s.setTitle("Especificaciones Flujo de Potencia");
							s.setScene(scene);
							s.show();
							s.setResizable(false);
					
						}
						
						
						break;
						
					}
					
				}
				
				repaint();
				
				
				
			}
			
	
		}

		if (e.getButton() == MouseButton.PRIMARY) {

			if (!barraMuycerca(x, y)) {

				if (path != null) {
					path.getElements().add(new LineTo(x, y));
					ultimoPunto = new Point2D(x, y);

					repaint();
				}

			}

			if ((bselected) && !barraMuycerca(x, y)) {

				Barras barra = new Barras(x, y, nombreBarra);
				barras.add(barra);
				barra.setBarraPQ(true);
				barra.setNombreBarra(nombreBarra + (barras.size() - 1));
				objetosCreados.add(barra);

				repaint();
				return;
			}

			Barras b = getContainingVertex(x, y);

			if (!isLineOn && (lselected || tselected) && b != null) {

				path = new Path();
				path.getElements().add(new MoveTo(x, y));

				ultimoPunto = new Point2D(x, y);
				startB = b;
				endOfLineX = e.getX();
				isLineOn = true;
			}

			if ((lselected || tselected) && (isLineOn)) {

				if (b != null) {

					if (!sonIguales(b, startB)) {

						int one = barras.indexOf(startB);
						int other = barras.indexOf(b);

						if (!listaBarras[one].contains(other) && !listaBarras[other].contains(one) && lselected) {
							path.getElements().add(new LineTo(x, y));

							Lineas l = new Lineas(startB, b, 1, 1, 1, path);

							conexiones.add(l);
							objetosCreados.add(l);
							listaBarras[one].add(other);
							listaBarras[other].add(one);

						}

						else if (!listaBarras[one].contains(other) && !listaBarras[other].contains(one) && tselected) {

							Transformador t = new Transformador(startB, b, 1, 1, 1, new Path());
							conexiones1.add(t);
							objetosCreados.add(t);
							listaBarras[one].add(other);
							listaBarras[other].add(one);
						}

						isLineOn = false;
						repaint();
						poliactual = null;
						path = null;
						return;

					}
				}
			}

			if (gselected && b != null) {

				if (!corGenerador.contains(b.getXbarra())) {

					xCoorG = e.getX();
					yCoorG = e.getY();

					b.setxCoorG(xCoorG);
					b.setyCoorG(yCoorG);

					Generadores gene = new Generadores(nombreGenerador, 1, 1, 1, b);

					conexiongene.add(gene);
					b.setBarraPV(true);
					b.setBarraPQ(false);
					b.setGenerador(gene);
					objetosCreados.add(gene);

					corGenerador.add(b.getXbarra());
				}

				repaint();
				return;
			}

			if (cSelected && b != null) {

				if (!corCarga.contains(b.getXbarra())) {

					b.setCoordenadasCarga(new Point2D(e.getX(), e.getY()));
					Carga carga = new Carga(new Point2D(e.getX(), e.getY()), b, nombreCarga);
					cargas.add(carga);
					objetosCreados.add(carga);
					if (b.isBarraPV()) {
						b.setBarraPQ(false);
					} else {
						b.setBarraPQ(true);
					}

					b.setCarga(carga);
					corCarga.add(b.getXbarra());

					repaint();
					return;
				}

			}

			if (bancoSelected && b != null) {

				if (!corBanco.contains(b.getXbarra())) {

					b.setCoordenadasBanco(new Point2D(e.getX(), e.getY()));
					Bancos banco = new Bancos(new Point2D(e.getX(), e.getY()), b, nombreBanco);
					bancos.add(banco);
					objetosCreados.add(banco);
					b.setBanco(banco);

					corBanco.add(b.getXbarra());
					repaint();
					return;
				}

			}
			
			if(compensadorSelected && b!=null) {
	
	
				if(!corCompensador.contains(b.getXbarra())) {
				
					b.setCoordenadaCompensador(new Point2D(e.getX(), e.getY()));
					CompensadorEstatico cp= new CompensadorEstatico(new Point2D(e.getX(), e.getY()), b, nombreCompensador);
					b.setBarraPV(true);
					b.setBarraPQ(false);
					compensadores.add(cp);
					objetosCreados.add(cp);
					b.setCompensador(cp);
					corCompensador.add(b.getXbarra());
					repaint();
					return;
					
			
				}
				
				
			}

		}

		Barras b = getContainingVertex(x, y);
		if (e.getButton() == MouseButton.SECONDARY && isLineOn && (lselected||tselected)) {

			isLineOn = false;

			repaint();

			return;
		}

		if (e.getButton() == MouseButton.MIDDLE) {

			tipoElemento = tipoElemento(x, y);

			if ((tipoElemento instanceof Rectangle) && ((Rectangle) tipoElemento).getWidth() != 30) {

				InfoImpedanciaFalla infolinea = new InfoImpedanciaFalla(b.getNombreBarra());
				Scene dad = new Scene(infolinea);
				Stage sta = new Stage();

				sta.setScene(dad);
				sta.setTitle("INFORMACIÃN IMPEDANCIA FALLA");
				sta.setResizable(false);
				sta.initModality(Modality.APPLICATION_MODAL);
				sta.showAndWait();

				impedanciaDeFalla = infolinea.getImpedanciaFalla();
				barraFallada = b;
				b.setImpedanciaFalla(impedanciaDeFalla);

				tipoElementoFallado = b.getNombreBarra();

				double xFalla = b.getXbarra() - 5;
				double yFalla = b.getYbarra() + b.getLargo() / 2;

				coorFalla.add(xFalla);
				coorFalla.add(yFalla);
				fallaEnLinea = false;

				repaint();

			}

			else if (tipoElemento instanceof Line) {

				boolean bandera = false;

				for (int i = 0; i < conexiones.size(); i++) {

					Point2D puntmedio = conexiones.get(i).getPuntomedio();

					if (((Line) tipoElemento).contains(puntmedio)) {

						InfoImpedanciaFallaLinea infolinea = new InfoImpedanciaFallaLinea(
								conexiones.get(i).getNombreLinea(), conexiones.get(i));
						Scene dad = new Scene(infolinea);
						Stage sta = new Stage();

						sta.setScene(dad);
						sta.setTitle("INFORMACIÃN IMPEDANCIA FALLA");
						sta.setResizable(false);
						sta.initModality(Modality.APPLICATION_MODAL);
						sta.showAndWait();

						impedanciaDeFalla = infolinea.getImpedanciaFalla();
						conexiones.get(i).setImpedanciafalla(impedanciaDeFalla);
						tipoElementoFallado = conexiones.get(i).getNombreLinea();
						lineaFallada = conexiones.get(i);

						double xFalla = e.getX() - 6;
						double yFalla = e.getY() + 8;

						bandera = true;

						coorFalla.add(xFalla);
						coorFalla.add(yFalla);
						fallaEnLinea = true;

						repaint();
						break;
					}

				}

				if (!bandera) {

					JOptionPane.showMessageDialog(null, "La falla no se puede ubicar dentro del transformador");
				}

			}

			else if (tipoElemento instanceof Rectangle && ((Rectangle) tipoElemento).getWidth() == 30) {

				for (int i = 0; i < conexiones.size(); i++) {

					Point2D pntmedio = conexiones.get(i).getPuntomedio();

					if (((Rectangle) tipoElemento).contains(pntmedio)) {

						InfoImpedanciaFallaLinea infolinea = new InfoImpedanciaFallaLinea(
								conexiones.get(i).getNombreLinea(), conexiones.get(i));
						Scene dad = new Scene(infolinea);
						Stage sta = new Stage();

						sta.setScene(dad);
						sta.setTitle("INFORMACIÃN IMPEDANCIA FALLA");
						sta.setResizable(false);
						sta.initModality(Modality.APPLICATION_MODAL);
						sta.showAndWait();

						impedanciaDeFalla = infolinea.getImpedanciaFalla();
						conexiones.get(i).setImpedanciafalla(impedanciaDeFalla);
						tipoElementoFallado = conexiones.get(i).getNombreLinea();
						lineaFallada = conexiones.get(i);

						double xFalla = e.getX() - 6;
						double yFalla = e.getY() + 8;

						coorFalla.add(xFalla);
						coorFalla.add(yFalla);
						fallaEnLinea = true;

						repaint();
						break;

					}

				}

			}

			else if (tipoElemento instanceof Circle && ((Circle) tipoElemento).getRadius() == 15) {

				JOptionPane.showMessageDialog(null, "La falla no se puede ubicar dentro del transformador");
			}

		}

	}

	@FXML

	private void dragEvent(MouseEvent e) {
		areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
		if (noneselected) {

			lista = areaDibujo.getChildren();

			double x = e.getX();
			double y = e.getY();

			Node tipoElemento = tipoElemento(x, y);

			if (tipoElemento instanceof Ellipse) {

				Barras temp = new Barras();

				for (int i = 1; i < barras.size(); i++) {

					Barras b = barras.get(i);

					if (b.getEllipse().equals(((Ellipse) tipoElemento))) {

						temp = b;
						break;

					}

				}

				((Ellipse) tipoElemento).setCenterX(x);
				((Ellipse) tipoElemento).setCenterY(y);

				temp.setNombreBarraX(((Ellipse) tipoElemento).getCenterX());
				temp.setNombreBarraY(((Ellipse) tipoElemento).getCenterY());

				repaint();

				return;
			}

			else if (tipoElemento instanceof Circle && ((Circle) tipoElemento).getRadius() == 14) {

				Barras b = getContainingVertex(((Circle) tipoElemento).getCenterX(),
						((Circle) tipoElemento).getCenterY());

				((Circle) tipoElemento).setCenterX(x);
				((Circle) tipoElemento).setCenterY(y);

				b.setXbarra(((Circle) tipoElemento).getCenterX() - b.getAncho() / 2);
				b.setYbarra(((Circle) tipoElemento).getCenterY() - b.getLargo() / 2);

				b.setxCoorG(b.getXbarra() + b.getAncho() / 2);
				b.setyCoorG(b.getYbarra() + b.getLargo() / 2);

				repaint();
				return;

			}

			if (tipoElemento instanceof Text) {
				if (((Text) tipoElemento).getText().equals("X")) {
					coorFalla.add(0, x);
					coorFalla.add(1, y);
					repaint();
					return;

				}

			}

			if (tipoElemento instanceof Circle) {

				double xcenter = ((Circle) tipoElemento).getCenterX();
				double ycenter = ((Circle) tipoElemento).getCenterY();

				boolean bandera1 = false;

				for (int i = 0; i < cargas.size(); i++) {

					double xcarga = cargas.get(i).getBarra().getCoordenadasCarga().getX();
					double ycarga = cargas.get(i).getBarra().getCoordenadasCarga().getY();

					if (((Circle) tipoElemento).contains(xcarga,ycarga)) {

						((Circle) tipoElemento).setCenterX(x);
						((Circle) tipoElemento).setCenterY(y);

						bandera1 = true;

						Barras b = cargas.get(i).getBarra();

						b.setCoordenadasCarga(new Point2D(((Circle) tipoElemento).getCenterX(),
								((Circle) tipoElemento).getCenterY()));

						repaint();
						return;

					}

				}

				for (int i = 0; i < bancos.size(); i++) {

					double xcarga = bancos.get(i).getBarra().getCoordenadasBanco().getX();
					double ycarga = bancos.get(i).getBarra().getCoordenadasBanco().getY();

					if (((Circle) tipoElemento).contains(xcarga,ycarga)) {

						((Circle) tipoElemento).setCenterX(x);
						((Circle) tipoElemento).setCenterY(y);

						bandera1 = true;

						Barras b = bancos.get(i).getBarra();

						b.setCoordenadasBanco(new Point2D(((Circle) tipoElemento).getCenterX(),
								((Circle) tipoElemento).getCenterY()));

						repaint();
						return;

					}

				}
				
				for(int i=0;i<compensadores.size();i++) {
					
					double xcarga = compensadores.get(i).getBarra().getCoordenadaCompensador().getX();
					double ycarga = compensadores.get(i).getBarra().getCoordenadaCompensador().getY();

					
					if (((Circle) tipoElemento).contains(xcarga,ycarga)) {

						((Circle) tipoElemento).setCenterX(x);
						((Circle) tipoElemento).setCenterY(y);

						bandera1 = true;

						Barras b = compensadores.get(i).getBarra();

						b.setCoordenadaCompensador(new Point2D(((Circle) tipoElemento).getCenterX(),
								((Circle) tipoElemento).getCenterY()));

						repaint();
						return;

					}
					
					
					
				}

			}

		}

	}

	@FXML
	private void mouseEvent(MouseEvent e) {
		areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
		lista = areaDibujo.getChildren();

		double x = e.getX();
		double y = e.getY();
		infoPosiMouse.setText(String.format("X=%.3f   Y=%.3f", x, y));
		Node tipoElemento = tipoElemento(x, y);

		if (((tselected || lselected)) && isLineOn) {

			endOfLineX = e.getX();
			endOfLineY = e.getY();
			repaint();

		}

	}

	// public void grid() {
	// double w = 1044;
	// double h =areaDibujo.getHeight();
	// Canvas grid = new Canvas(w,h);
	//
	// // don't catch mouse events
	// grid.setMouseTransparent(true);
	//
	// GraphicsContext gc = grid.getGraphicsContext2D();
	//
	// gc.setStroke(Color.GRAY);
	// gc.setLineWidth(0.3);
	//
	// // draw grid lines
	// double offset = 15;
	// for( double i=offset; i < w; i+=offset) {
	// gc.strokeLine( i, 0, i, h);
	// gc.strokeLine( 0, i, w, i);
	// }
	//
	// areaDibujo.getChildren().add( grid);
	// lista=areaDibujo.getChildren();
	//
	// grid.toBack();
	//
	//
	// }

	@FXML
	private TextField MVAbase;

	public double longitudLinea(Barras b1, Barras b2) {

		return Math.sqrt((b1.getXbarra() - b2.getXbarra()) * (b1.getXbarra() - b2.getXbarra())
				+ (b1.getYbarra() - b2.getYbarra()) * (b1.getYbarra() - b2.getYbarra()));
	}

	public double longitudLinea(double x1, double y1, double x2, double y2) {

		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public void removeGeneradorAdyacente(Barras b) {

		for (int i = 0; i < conexiongene.size(); i++) {

			if (conexiongene.get(i).getBarra() == b) {
				conexiongene.remove(i--);
			}
		}

	}

	public void removeCargaAdyacente(Barras b) {

		for (int i = 0; i < cargas.size(); i++) {

			if (cargas.get(i).getBarra() == b) {
				cargas.remove(i--);
			}
		}

	}

	public void removerBancoAdyacente(Barras b) {

		for (int i = 0; i < bancos.size(); i++) {

			if (bancos.get(i).getBarra() == b) {
				bancos.remove(i--);
			}
		}

	}

	public void removeAdjacentEdges(Barras b) {

		for (int i = 0; i < conexiones.size(); i++) {

			if (conexiones.get(i).getBarra1() == b || conexiones.get(i).getBarra2() == b) {
				conexiones.remove(i--);
			}

		}

		for (int i = 0; i < conexiones1.size(); i++) {

			if (conexiones1.get(i).getBarra1() == b || conexiones1.get(i).getBarra2() == b) {
				conexiones1.remove(i--);
			}
		}

	}

	public Node tipoElemento(double x, double y) {

		for (int i = 0; i < lista.size(); i++) {

			if (lista.get(i).contains(x, y)) {
				return lista.get(i);
			}

		}

		return null;

	}

	public Circle getCirculo(double x, double y) {

		for (Node list : lista) {

			if (list instanceof Circle) {

				if (((Circle) list).contains(x, y)) {
					return (Circle) list;
				}
			}

		}

		return null;
	}

	public boolean sonIguales(Barras barra1, Barras barra2) {

		if (barra1.getXbarra() == barra2.getXbarra() && barra1.getYbarra() == barra2.getYbarra()) {

			return true;
		} else {

			return false;
		}
	}

	public Barras getContainingVertex(double x, double y) {

		for (int i = 0; i < barras.size(); i++) {

			if (dentroBarra(barras.get(i), x, y)) {

				return barras.get(i);
			}

		}

		return null;

	}

	public boolean dentroBarra(Barras barra, double x, double y) {

		if (barra.getOrientacion().equals("V")) {

			if (x >= barra.getXbarra() && x <= barra.getXbarra() + barra.getAncho() && y >= barra.getYbarra()
					&& y <= barra.getYbarra() + barra.getLargo()) {

				return true;
			}

			else {

				return false;
			}

		} else {

			if (x >= barra.getXbarra() && x <= barra.getXbarra() + barra.getAncho() && y >= barra.getYbarra()
					&& y <= barra.getYbarra() + barra.getLargo()) {

				return true;
			}

			else {
				return false;
			}

		}

	}

	public boolean barraMuycerca(double x, double y) {

		for (int i = 1; i < barras.size(); i++) {

			if (obtenerDistancia(x, y, barras.get(i).getPuntoMedioBarra().getX(),
					barras.get(i).getPuntoMedioBarra().getY()) && barras.get(i).getOrientacion().equals("V") ) {
				return true;
			}

		}

		return false;

	}

	public boolean obtenerDistancia(double x1, double y1, double x2, double y2) {

		if (Math.abs(x1 - x2) < 10 + 5 && Math.abs(y1 - y2) < 70 + 10) {

			return true;

		} else {

			return false;
		}

	}

	public void limpiarArea() {

		for (int i = 0; i < listaBarras.length; i++) {

			listaBarras[i].clear();

		}

		conexiones.clear();
		conexiones1.clear();
		conexiongene.clear();
		barras.clear();
		cargas.clear();
		bancos.clear();
		distanciasLineas.clear();
		corGenerador.clear();
		corCarga.clear();
		corBanco.clear();
		posBarra.clear();
		objetosCreados.clear();
		restablecerElementos.clear();
		repaint();
		barras.add(new Barras("Tierra"));
		tipoElementoFallado = null;

	}

	public void borrarUltimoElemento() {

		Object elemento = objetosCreados.pollLast();
		if (elemento != null)

			restablecerElementos.add(elemento);

		if (elemento instanceof Barras && elemento != null && barras.size() > 1) {

			Barras b = ((Barras) elemento);

			if(b.isBarraCompensacion()) {
					barraCompensacion=null;
			}
			
			int indexBarrab = barras.indexOf(b);

			for (int i : listaBarras[indexBarrab]) {

				listaBarras[i].remove(listaBarras[i].indexOf(indexBarrab));

			}

			listaBarras[indexBarrab].clear();

			barras.remove(b);
			repaint();

		}

		else if (elemento instanceof Lineas && !(elemento instanceof Transformador) && elemento != null
				&& conexiones.size() > 0) {

			int one = barras.indexOf(conexiones.get(conexiones.size() - 1).getBarra1());
			int other = barras.indexOf(conexiones.get(conexiones.size() - 1).getBarra2());

			listaBarras[one].remove(listaBarras[one].indexOf(other));
			listaBarras[other].remove(listaBarras[other].indexOf(one));
			conexiones.remove(conexiones.size() - 1);
			distanciasLineas.clear();
			repaint();

		} else if (elemento instanceof Generadores && elemento != null && conexiongene.size() > 0) {
			
			Barras b= conexiongene.get(conexiongene.size() - 1).getBarra();
			if(!b.containsCompensador()) {
				b.setBarraPQ(true);
				b.setBarraPV(false);
			}
			
			b.setGenerador(null);
			conexiongene.remove(conexiongene.size() - 1);
			corGenerador.clear();
			
			repaint();
		}
		
		else if (elemento instanceof CompensadorEstatico && elemento != null && compensadores.size() > 0) {
			
			Barras b= compensadores.get(compensadores.size() - 1).getBarra();
			if(!b.containsGenerador()) {
			b.setBarraPQ(true);
			b.setBarraPV(false);}
			b.setCompensador(null);
			compensadores.remove(compensadores.size() - 1);
			corCompensador.clear();
			
			repaint();
		}
		
		

		else if ((elemento instanceof Transformador) && (elemento instanceof Lineas) && elemento != null
				&& conexiones1.size() > 0) {

			int one = barras.indexOf(conexiones1.get(conexiones1.size() - 1).getBarra1());
			int other = barras.indexOf(conexiones1.get(conexiones1.size() - 1).getBarra2());

			listaBarras[one].remove(listaBarras[one].indexOf(other));
			listaBarras[other].remove(listaBarras[other].indexOf(one));

			conexiones1.remove(conexiones1.size() - 1);
			distanciasLineas.clear();
			repaint();
		}

		else if (elemento instanceof Carga && elemento != null && cargas.size() > 0) {
			Barras b= cargas.get(cargas.size() - 1).getBarra();
			b.setCarga(null);
			cargas.remove(cargas.size() - 1);
			corCarga.clear();
			repaint();
		}

		else if (elemento instanceof Bancos && elemento != null && bancos.size() > 0) {
			Barras b= bancos.get(bancos.size() - 1).getBarra();
			b.setBanco(null);
			bancos.remove(bancos.size() - 1);
			corBanco.clear();
			repaint();

		}

	}

	public void dibujarBarra() {

		for (int i = 0; i < barras.size(); i++) {

			if (barras.get(i).getOrientacion().equals("H")) {

				posBarra.add(barras.get(i).getXbarra());

				Collections.sort(posBarra);
				Rectangle barra = new Rectangle();

				if (barras.get(i).isBarraCompensacion()) {

					barra.setFill(Color.BLUE);

				} else {
					barra.setFill(Color.BLACK);
				}

				barra.setX(barras.get(i).getXbarra());
				barra.setY(barras.get(i).getYbarra());
				barra.setWidth(barras.get(i).getAncho());

				barra.setHeight(barras.get(i).getLargo());

				double xMedio = barras.get(i).getXbarra() + barras.get(i).getAncho() / 2;
				double yMedio = barras.get(i).getYbarra() + barras.get(i).getLargo() / 2;

				barras.get(i).setPuntoMedioBarra(new Point2D(xMedio, yMedio));
				// barra.setRotate(-90);

				Circle circulo = new Circle();
				circulo.setRadius(radioCirculo);
				circulo.setCenterX(barras.get(i).getXbarra() + barras.get(i).getAncho() / 2);
				circulo.setCenterY(barras.get(i).getYbarra() + barras.get(i).getLargo() / 2);
				circulo.setVisible(false);

				Text nbarra = new Text();

				if (barras.get(i).getNombrePersonalizado() == null) {
					String nombre = barras.get(i).getNombreBarra();

					nbarra.setText(nombre);
					// if(!nombreBarras.contains(nombre))
					// nombreBarras.put(nombre, barras.get(i));
					// else {
					// nombre=nombre+i;
					// nombreBarras.put(nombre, barras.get(i));
					//
					// }
				} else {

					String nombre = barras.get(i).getNombrePersonalizado();
					nbarra.setText(nombre);
					// if(!nombreBarras.contains(nombre))
					// nombreBarras.put(nombre, barras.get(i));
					// else {
					// nombre=nombre+i;
					// nombreBarras.put(nombre, barras.get(i));
					//
					// }

				}

				nbarra.setStroke(Color.ORANGE);
				nbarra.setStrokeWidth(1);

				nbarra.setStyle("-fx-font: 12 arial;");

				nbarra.setX(barras.get(i).getNombreBarraX());
				nbarra.setY(barras.get(i).getNombreBarraY());

				Ellipse ellipse = new Ellipse();

				ellipse.setCenterX(barras.get(i).getNombreBarraX() + nbarra.getText().length() * 3);
				ellipse.setCenterY(barras.get(i).getNombreBarraY());
				ellipse.setRadiusX(nbarra.getText().length() * 6);
				ellipse.setRadiusY(8);
				ellipse.setVisible(false);

				barras.get(i).setEllipse(ellipse);

				areaDibujo.getChildren().addAll(barra, ellipse, nbarra, circulo);

			} else if (barras.get(i).getOrientacion().equals("V")) {

				posBarra.add(barras.get(i).getXbarra());
				Collections.sort(posBarra);
				if (i == 0) {
					continue;
				} else {

					double xMedio = barras.get(i).getXbarra() + barras.get(i).getAncho() / 2;
					double yMedio = barras.get(i).getYbarra() + barras.get(i).getLargo() / 2;

					barras.get(i).setPuntoMedioBarra(new Point2D(xMedio, yMedio));

					Rectangle barra = new Rectangle();

					if (barras.get(i).isBarraCompensacion()) {

						barra.setFill(Color.BLUE);

					} else {
						barra.setFill(Color.BLACK);
					}

					barra.setX(barras.get(i).getXbarra());
					barra.setY(barras.get(i).getYbarra());
					barra.setWidth(barras.get(i).getAncho());
					barra.setHeight(barras.get(i).getLargo());

					Circle circulo = new Circle();
					circulo.setRadius(radioCirculo);
					circulo.setCenterX(barras.get(i).getXbarra() + barras.get(i).getAncho() / 2);
					circulo.setCenterY(barras.get(i).getYbarra() + barras.get(i).getLargo() / 2);
					circulo.setVisible(false);

					Text nbarra = new Text();

					if (barras.get(i).getNombrePersonalizado() == null) {

						String nombre = barras.get(i).getNombreBarra();

						nbarra.setText(nombre);
						// if(!nombreBarras.contains(nombre))
						// nombreBarras.put(nombre, barras.get(i));
						// else {
						// nombre=nombre+i;
						// nombreBarras.put(nombre, barras.get(i));
						//
						// }
					} else {
						String nombre = barras.get(i).getNombrePersonalizado();
						nbarra.setText(nombre);
						// if(!nombreBarras.contains(nombre))
						// nombreBarras.put(nombre, barras.get(i));
						// else {
						// nombre=nombre+i;
						// nombreBarras.put(nombre, barras.get(i));
						//
						// }

					}
					nbarra.setStroke(Color.ORANGE);
					nbarra.setStrokeWidth(1);

					nbarra.setStyle("-fx-font: 12 arial;");
					nbarra.setX(barras.get(i).getNombreBarraX());
					nbarra.setY(barras.get(i).getNombreBarraY());

					Ellipse ellipse = new Ellipse();

					ellipse.setCenterX(barras.get(i).getNombreBarraX() + nbarra.getText().length() * 3);
					ellipse.setCenterY(barras.get(i).getNombreBarraY());
					ellipse.setRadiusX(nbarra.getText().length() * 6);
					ellipse.setRadiusY(8);
					ellipse.setVisible(false);
					barras.get(i).setEllipse(ellipse);
					areaDibujo.getChildren().addAll(barra, ellipse, nbarra, circulo);

				}
			}

		}

	}

	public void dibujarFalla() {

		if (coorFalla.size() > 0) {

			Text ubicarFalla = new Text("X");
			ubicarFalla.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 25));

			ubicarFalla.setFill(Color.RED);
			ubicarFalla.setX(coorFalla.get(0));
			ubicarFalla.setY(coorFalla.get(1));
			areaDibujo.getChildren().add(ubicarFalla);
		}

	}

	public void dibujarCarga() {

		for (int i = 0; i < cargas.size(); i++) {

			corCarga.add(cargas.get(i).getBarra().getXbarra());
			cargas.get(i).setNombreCarga(nombreCarga + (i + 1));

			areaDibujo.getChildren().addAll(DibujarCarga.dibujarCargaBanco(cargas.get(i), "C"));
			areaDibujo.getChildren().add(DibujarCarga.nombreCargaBanco(cargas.get(i), "C"));

		}

	}

	public void dibujarBanco() {

		for (int i = 0; i < bancos.size(); i++) {

			corBanco.add(bancos.get(i).getBarra().getXbarra());
			bancos.get(i).setNombreCarga(nombreBanco + (i + 1));

			areaDibujo.getChildren().addAll(DibujarCarga.dibujarCargaBanco(bancos.get(i), "B"));
			areaDibujo.getChildren().add(DibujarCarga.nombreCargaBanco(bancos.get(i), "E")); // Modificar el metodos
			// para establecer el
			// nombre

		}

	}
	
	public void dibujarCompensador() {
		
		for(int i=0;i<compensadores.size();i++) {
			
			corCompensador.add(compensadores.get(i).getBarra().getXbarra());
			compensadores.get(i).setNombreCompensador(nombreCompensador+(i+1));
			
			areaDibujo.getChildren().addAll(DibujarCarga.dibujarCargaBanco(compensadores.get(i), "E"));
	
		}
		
		
		
	}

	public void dibujarGenerador() {

		for (int i = 0; i < conexiongene.size(); i++) {
			corGenerador.add(conexiongene.get(i).getBarra().getXbarra());

			conexiongene.get(i).setNombreGenerador(nombreGenerador + (i + 1));
			if (conexiongene.get(i).getOrientacion().equals(Generadores.RIGHT)) {

				Line lineg = new Line(conexiongene.get(i).getBarra().getxCoorG(),
						conexiongene.get(i).getBarra().getyCoorG(), conexiongene.get(i).getBarra().getxCoorG() + 20,
						conexiongene.get(i).getBarra().getyCoorG());

				lineg.setStrokeWidth(4);

				Circle cirg = new Circle();
				cirg.setRadius(20);
				cirg.setCenterX(lineg.getEndX() + 20);
				cirg.setCenterY(lineg.getEndY());
				cirg.setFill(Color.WHITE);
				cirg.setStroke(Color.BLACK);

				Arc arc1 = new Arc(cirg.getCenterX() - 9, cirg.getCenterY(), 9, 9, 0, 180);
				Arc arc2 = new Arc(cirg.getCenterX() + 9, cirg.getCenterY(), 9, 9, 180, 180);
				Text nombreg = new Text(conexiongene.get(i).getNombreGenerador());

				nombreg.setFill(Color.BLUE);
				nombreg.setX(cirg.getCenterX() - 3);
				nombreg.setY(cirg.getCenterY() - 25);

				arc1.setFill(Color.WHITE);
				arc1.setStroke(Color.BLACK);
				arc2.setFill(Color.WHITE);
				arc2.setStroke(Color.BLACK);

				areaDibujo.getChildren().addAll(lineg, cirg, arc1, arc2, nombreg);

			} else if (conexiongene.get(i).getOrientacion().equals(Generadores.LEFT)) {

				Line lineg = new Line(conexiongene.get(i).getBarra().getxCoorG(),
						conexiongene.get(i).getBarra().getyCoorG(), conexiongene.get(i).getBarra().getxCoorG() - 10,
						conexiongene.get(i).getBarra().getyCoorG());

				lineg.setStrokeWidth(4);

				Circle cirg = new Circle();
				cirg.setRadius(20);
				cirg.setCenterX(lineg.getEndX() - 20);
				cirg.setCenterY(lineg.getEndY());
				cirg.setFill(Color.WHITE);
				cirg.setStroke(Color.BLACK);

				Arc arc1 = new Arc(cirg.getCenterX() - 9, cirg.getCenterY(), 9, 9, 0, 180);
				Arc arc2 = new Arc(cirg.getCenterX() + 9, cirg.getCenterY(), 9, 9, 180, 180);

				Text nombreg = new Text(conexiongene.get(i).getNombreGenerador());
				nombreg.setFill(Color.BLUE);
				nombreg.setX(cirg.getCenterX() - 3);
				nombreg.setY(cirg.getCenterY() - 25);

				arc1.setFill(Color.WHITE);
				arc1.setStroke(Color.BLACK);
				arc2.setFill(Color.WHITE);
				arc2.setStroke(Color.BLACK);

				areaDibujo.getChildren().addAll(lineg, cirg, arc1, arc2, nombreg);
			}

			else if (conexiongene.get(i).getOrientacion().equals(Generadores.ARRIBA)) {

				Line lineg = new Line(
						conexiongene.get(i).getBarra().getXbarra() + conexiongene.get(i).getBarra().getAncho() / 2,
						conexiongene.get(i).getBarra().getYbarra() + conexiongene.get(i).getBarra().getLargo() / 2,
						conexiongene.get(i).getBarra().getXbarra() + conexiongene.get(i).getBarra().getAncho() / 2,
						(conexiongene.get(i).getBarra().getYbarra() + conexiongene.get(i).getBarra().getLargo() / 2)
						- 10);

				lineg.setStrokeWidth(4);

				Circle cirg = new Circle();
				cirg.setRadius(20);
				cirg.setCenterX(lineg.getEndX());
				cirg.setCenterY(lineg.getEndY() - 20);

				conexiongene.get(i).setXCenter(lineg.getEndX());
				conexiongene.get(i).setYCenter(lineg.getEndY() - 20);

				cirg.setFill(Color.WHITE);
				cirg.setStroke(Color.BLACK);

				Arc arc1 = new Arc(cirg.getCenterX() - 9, cirg.getCenterY(), 9, 9, 0, 180);
				Arc arc2 = new Arc(cirg.getCenterX() + 9, cirg.getCenterY(), 9, 9, 180, 180);

				Text nombreg = new Text(conexiongene.get(i).getNombreGenerador());
				nombreg.setFill(Color.BLUE);
				nombreg.setX(cirg.getCenterX() - 3);
				nombreg.setY(cirg.getCenterY() - 25);

				arc1.setFill(Color.WHITE);
				arc1.setStroke(Color.BLACK);
				arc2.setFill(Color.WHITE);
				arc2.setStroke(Color.BLACK);
				areaDibujo.getChildren().addAll(lineg, cirg, arc1, arc2, nombreg);

			}

			else if (conexiongene.get(i).getOrientacion().equals(Generadores.ABAJO)) {

				Line lineg = new Line(
						conexiongene.get(i).getBarra().getXbarra() + conexiongene.get(i).getBarra().getAncho() / 2,
						conexiongene.get(i).getBarra().getYbarra() + conexiongene.get(i).getBarra().getLargo() / 2,
						conexiongene.get(i).getBarra().getXbarra() + conexiongene.get(i).getBarra().getAncho() / 2,
						(conexiongene.get(i).getBarra().getYbarra() + conexiongene.get(i).getBarra().getLargo() / 2)
						+ 10);

				lineg.setStrokeWidth(4);
				Circle cirg = new Circle();

				cirg.setRadius(20);
				cirg.setCenterX(lineg.getEndX());
				cirg.setCenterY(lineg.getEndY() + 20);

				conexiongene.get(i).setXCenter(lineg.getEndX());
				conexiongene.get(i).setYCenter(lineg.getEndY() + 20);

				cirg.setFill(Color.WHITE);
				cirg.setStroke(Color.BLACK);

				Arc arc1 = new Arc(cirg.getCenterX() - 9, cirg.getCenterY(), 9, 9, 0, 180);
				Arc arc2 = new Arc(cirg.getCenterX() + 9, cirg.getCenterY(), 9, 9, 180, 180);

				Text nombreg = new Text(conexiongene.get(i).getNombreGenerador());

				nombreg.setFill(Color.BLUE);
				nombreg.setX(cirg.getCenterX() - 3);
				nombreg.setY(cirg.getCenterY() + 30);

				arc1.setFill(Color.WHITE);
				arc1.setStroke(Color.BLACK);
				arc2.setFill(Color.WHITE);
				arc2.setStroke(Color.BLACK);

				areaDibujo.getChildren().addAll(lineg, cirg, arc1, arc2, nombreg);

			}
		}

	}

	public void dibujarLineas() {

		if (isLineOn && tselected) {

			Line linea = new Line(startB.getXbarra() + startB.getAncho() / 2,
					startB.getYbarra() + startB.getLargo() / 2, endOfLineX, endOfLineY);
			linea.setStroke(Color.RED);
			linea.setStrokeWidth(2);
			areaDibujo.getChildren().add(linea);
		}

		for (int i = 0; i < conexiones1.size(); i++) {
			
			
			

			Line linea = new Line(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getX(),
					conexiones1.get(i).getBarra1().getPuntoMedioBarra().getY(),
					conexiones1.get(i).getBarra2().getPuntoMedioBarra().getX(),
					conexiones1.get(i).getBarra2().getPuntoMedioBarra().getY());
			linea.setStrokeWidth(1);
			double pe = longitudLinea(linea.getStartX(), linea.getStartY(), linea.getEndX(), linea.getEndY());
			distanciasLineas.add(pe);

			conexiones1.get(i).setNombreLinea(nombreTrafo + (i + 1));
			double puntoMedioX = Math.abs(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getX()
					+ conexiones1.get(i).getBarra2().getPuntoMedioBarra().getX()) / 2;

			double puntoMedioY = Math.abs(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getY()
					+ conexiones1.get(i).getBarra2().getPuntoMedioBarra().getY()) / 2;

			conexiones1.get(i).setPuntoMedio(new Point2D(puntoMedioX, puntoMedioY));
			Circle cc1 = new Circle();
			Circle cc2 = new Circle();

			cc1.setRadius(15);
			cc1.setCenterX(puntoMedioX - 5);
			cc1.setCenterY(puntoMedioY);
			cc1.setStroke(Color.GREEN);
			cc1.setFill(Color.WHITE);
			cc2.setRadius(15);
			cc2.setCenterX(puntoMedioX + 5);
			cc2.setCenterY(puntoMedioY);
			cc2.setStroke(Color.RED);
			cc2.setFill(Color.WHITE);

			;
			
			

			linea.setStroke(Color.BLACK);
			linea.setStrokeWidth(2);

			areaDibujo.getChildren().add(linea);
			areaDibujo.getChildren().addAll(cc1, cc2);
			
			

			Text textLinea = new Text(conexiones1.get(i).getNombreLinea());
			textLinea.setStroke(Color.BLACK);
			textLinea.setStrokeWidth(1);
			
			if(conexiones1.get(i).isHasTap()) {
				textLinea.setStroke(Color.BLUE);
			}
			

			if(conexiones1.get(i).isHasTap() && conexiones1.get(i).getAngtab()!=0) {
				textLinea.setStroke(Color.GREEN);
			}

			textLinea.setX(puntoMedioX);
			textLinea.setY(puntoMedioY);

			areaDibujo.getChildren().addAll(textLinea);
		}

	}

	public MyGraph<Barras> getGraph() {

		List<Edges> edges = new ArrayList<>();

		for (int i = 0; i < conexiones.size(); i++) {

			int x = barras.indexOf(conexiones.get(i).getBarra1());
			int y = barras.indexOf(conexiones.get(i).getBarra2());

			edges.add(new Edges(x, y));
			edges.add(new Edges(y, x));
		}

		for (int i = 0; i < conexiones1.size(); i++) {

			int x = barras.indexOf(conexiones1.get(i).getBarra1());
			int y = barras.indexOf(conexiones1.get(i).getBarra2());

			edges.add(new Edges(x, y));
			edges.add(new Edges(y, x));
		}

		MyGraph<Barras> graph = new MyGraph<>(barras, edges);
		return graph;
	}

	public void crearGrafos() { // Crea las matrices de ayancencia y los diferentes tipos de matrices de
		// impedancia de sencuencia

		if (tipoElementoFallado != null) {

			if (trifasica)
				fallaTrifasica();

			if (monofasica || bifasicaATierra)
				fallaMonofasica();

			// if(bifasicaATierra);
			// fallaBifasicaATierra();

			if (lineaALinea)
				fallaLineaALinea();
		} else {

			JOptionPane.showMessageDialog(null, "POR FAVOR UBIQUE LA FALLA YA SEA SOBRE UNA BARRA O LÃNEA");
		}

	}

	public void fallaLineaALinea() {

		List<WeightEdeges> bordes1 = new ArrayList<>();
		List<WeightEdeges> bordes2 = new ArrayList<>();

		for (int i = 0; i < conexiones.size(); i++) {

			int x = barras.indexOf(conexiones.get(i).getBarra1());
			int y = barras.indexOf(conexiones.get(i).getBarra2());
			double z1 = conexiones.get(i).getimpedanciaLineaZ1();
			double z2 = conexiones.get(i).getimpedanciaLineaZ2();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

		}

		for (int i = 0; i < conexiones1.size(); i++) {

			int x = barras.indexOf(conexiones1.get(i).getBarra1());
			int y = barras.indexOf(conexiones1.get(i).getBarra2());

			double z1 = conexiones1.get(i).getimpedanciaLineaZ1();
			double z2 = conexiones1.get(i).getimpedanciaLineaZ2();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

		}

		for (int i = 0; i < conexiongene.size(); i++) {

			int x = barras.indexOf(conexiongene.get(i).getBarra());
			int y = 0;

			double z1 = conexiongene.get(i).getImpedanciaZ1();
			double z2 = conexiongene.get(i).getImpedanciaZ2();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

		}

		grafo1 = new WeightedGraph<>(barras, bordes1);
		grafo2 = new WeightedGraph<>(barras, bordes2);

		//
		List<List<Edges>> borde1 = grafo1.getConexiones();
		List<List<Edges>> borde2 = grafo2.getConexiones();

		double[][] yBarraSecuencia1 = obtenerMatrizAdyacencia(borde1);
		double[][] yBarraSecuencia2 = obtenerMatrizAdyacencia(borde2);
		//
		double[][] zBarraSecuencia1 = Zbarra.getZbarra(yBarraSecuencia1);
		double[][] zBarraSecuencia2 = Zbarra.getZbarra(yBarraSecuencia2);

		if (tipoElementoFallado.charAt(0) == 'B') {

			FallaLineaALinea fallalinealinea = new FallaLineaALinea(zBarraSecuencia1, zBarraSecuencia2, barras,
					conexiones, conexiones1, conexiongene, barraFallada, borde1);

			angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

			magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

		}

		else if (tipoElementoFallado.charAt(0) == 'L') {

			FallaLineaALineaLinea fallaLineaALineaLinea = new FallaLineaALineaLinea(zBarraSecuencia1, zBarraSecuencia2,
					barras, conexiones, conexiones1, conexiongene, lineaFallada);

			angCorrientePuntoFallaFaseA = lineaFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB = lineaFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC = lineaFallada.getAngCorrientePuntoFallaFaseC();

			magCorrientePuntoFallaFaseA = lineaFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB = lineaFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC = lineaFallada.getMagcorrientePuntoFallaFaseC();
		}

	}

	public void fallaTrifasica() {

		List<WeightEdeges> bordes1 = new ArrayList<>();

		for (int i = 0; i < conexiones.size(); i++) {

			int x = barras.indexOf(conexiones.get(i).getBarra1());
			int y = barras.indexOf(conexiones.get(i).getBarra2());
			double z1 = conexiones.get(i).getimpedanciaLineaZ1();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

		}

		for (int i = 0; i < conexiones1.size(); i++) {

			int x = barras.indexOf(conexiones1.get(i).getBarra1());
			int y = barras.indexOf(conexiones1.get(i).getBarra2());

			double z1 = conexiones1.get(i).getimpedanciaLineaZ1();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

		}

		for (int i = 0; i < conexiongene.size(); i++) {

			int x = barras.indexOf(conexiongene.get(i).getBarra());
			int y = 0;

			double z1 = conexiongene.get(i).getImpedanciaZ1();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

		}

		grafo1 = new WeightedGraph<>(barras, bordes1);

		List<List<Edges>> borde1 = grafo1.getConexiones();

		double[][] yBarraSecuencia1 = obtenerMatrizAdyacencia(borde1);

		double[][] zBarraSecuencia1 = Zbarra.getZbarra(yBarraSecuencia1);

		if (tipoElementoFallado.charAt(0) == 'B') {

			FallaTrifasica calculoFalla = new FallaTrifasica(zBarraSecuencia1, tipoElementoFallado, barras, conexiones,
					conexiones1, conexiongene);

			angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

			magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();
		}

		else if (tipoElementoFallado.charAt(0) == 'L') {

			FallaTrifasicaLinea fallatrifasicalinea = new FallaTrifasicaLinea(zBarraSecuencia1, lineaFallada, barras,
					conexiones, conexiones1, conexiongene);

			angCorrientePuntoFallaFaseA = lineaFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB = lineaFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC = lineaFallada.getAngCorrientePuntoFallaFaseC();

			magCorrientePuntoFallaFaseA = lineaFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB = lineaFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC = lineaFallada.getMagcorrientePuntoFallaFaseC();

		}
	}

	public void fallaMonofasica() {

		List<WeightEdeges> bordes1 = new ArrayList<>();
		List<WeightEdeges> bordes2 = new ArrayList<>();
		List<WeightEdeges> bordes0 = new ArrayList<>();

		for (int i = 0; i < conexiones.size(); i++) {

			int x = barras.indexOf(conexiones.get(i).getBarra1());
			int y = barras.indexOf(conexiones.get(i).getBarra2());

			double z1 = conexiones.get(i).getimpedanciaLineaZ1();
			double z2 = conexiones.get(i).getimpedanciaLineaZ2();
			double z0 = conexiones.get(i).getimpedanciaLineaZ0();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

			bordes0.add(new WeightEdeges(x, y, z0));
			bordes0.add(new WeightEdeges(y, x, z0));

		}

		for (int i = 0; i < conexiones1.size(); i++) {

			int x = barras.indexOf(conexiones1.get(i).getBarra1());
			int y = barras.indexOf(conexiones1.get(i).getBarra2());

			double z1 = conexiones1.get(i).getimpedanciaLineaZ1();
			double z2 = conexiones1.get(i).getimpedanciaLineaZ2();
			double z0 = conexiones1.get(i).getimpedanciaLineaZ0();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

			if (conexiones1.get(i).getConexionPrimaria().contains("YN")
					&& conexiones1.get(i).getConexionSecundaria().contains("YN")) {

				z0 += conexiones1.get(i).getImpedanciaAterrizamientoPrimaria()
						+ conexiones1.get(i).getImpedanciaAterrizamientoSecundaria();

			} else if (conexiones1.get(i).getConexionPrimaria().contains("Y-")
					&& conexiones1.get(i).getConexionSecundaria().contains("Y-")) {

				z0 += 10000000;

			}

			else if ((conexiones1.get(i).getConexionPrimaria().contains("Y-")
					&& conexiones1.get(i).getConexionSecundaria().contains("YN"))
					|| (conexiones1.get(i).getConexionPrimaria().contains("YN")
							&& conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {

				z0 += 10000000;
			}

			else if (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
					&& conexiones1.get(i).getConexionSecundaria().contains("DELTA")) {

				z0 += 10000000;

			} else if ((conexiones1.get(i).getConexionPrimaria().contains("Y-")
					&& conexiones1.get(i).getConexionSecundaria().contains("DELTA"))
					|| (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
							&& conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {

				z0 += 10000000;
			}

			else if ((conexiones1.get(i).getConexionPrimaria().contains("YN")
					&& conexiones1.get(i).getConexionSecundaria().contains("DELTA"))
					|| (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
							&& conexiones1.get(i).getConexionSecundaria().contains("YN"))) {

				if (conexiones1.get(i).getConexionPrimaria().contains("YN")) {

					z0 += conexiones1.get(i).getImpedanciaAterrizamientoPrimaria();

					String xx = Character.toString(conexiones1.get(i).getConexionPrimaria()
							.charAt(conexiones1.get(i).getConexionPrimaria().length() - 1));

					x = Integer.parseInt(xx);
					y = 0;

				} else if (conexiones1.get(i).getConexionSecundaria().contains("YN")) {

					String xx = Character.toString(conexiones1.get(i).getConexionSecundaria()
							.charAt(conexiones1.get(i).getConexionSecundaria().length() - 1));
					z0 += conexiones1.get(i).getImpedanciaAterrizamientoSecundaria();
					x = Integer.parseInt(xx);
					y = 0;

				}

			}

			bordes0.add(new WeightEdeges(x, y, z0));
			bordes0.add(new WeightEdeges(y, x, z0));

		}

		for (int i = 0; i < conexiongene.size(); i++) {

			int x = barras.indexOf(conexiongene.get(i).getBarra());
			int y = 0;

			double z1 = conexiongene.get(i).getImpedanciaZ1();
			double z2 = conexiongene.get(i).getImpedanciaZ2();
			double z0 = conexiongene.get(i).getImpedanciaZ0();

			bordes1.add(new WeightEdeges(x, y, z1));
			bordes1.add(new WeightEdeges(y, x, z1));

			bordes2.add(new WeightEdeges(x, y, z2));
			bordes2.add(new WeightEdeges(y, x, z2));

			if (conexiongene.get(i).getAterrizamiento().equals(Generadores.conexion1)) {

				z0 += conexiongene.get(i).getImpedanciaAterrizamiento();

			} else if (conexiongene.get(i).getAterrizamiento().equals(Generadores.conexion2)) {

				z0 += 10000000;
			}

			bordes0.add(new WeightEdeges(x, y, z0));
			bordes0.add(new WeightEdeges(y, x, z0));

		}

		grafo1 = new WeightedGraph<>(barras, bordes1);
		grafo2 = new WeightedGraph<>(barras, bordes2);
		grafo0 = new WeightedGraph<>(barras, bordes0);
		//
		List<List<Edges>> borde1 = grafo1.getConexiones();
		List<List<Edges>> borde2 = grafo2.getConexiones();
		List<List<Edges>> borde0 = grafo0.getConexiones();

		double[][] yBarraSecuencia1 = obtenerMatrizAdyacencia(borde1);
		double[][] yBarraSecuencia2 = obtenerMatrizAdyacencia(borde2);
		double[][] yBarraSecuencia0 = obtenerMatrizAdyacencia(borde0);
		//
		double[][] zBarraSecuencia1 = Zbarra.getZbarra(yBarraSecuencia1);
		double[][] zBarraSecuencia2 = Zbarra.getZbarra(yBarraSecuencia2);
		double[][] zBarraSecuencia0 = Zbarra.getZbarra(yBarraSecuencia0);

		if (monofasica) {
			if (tipoElementoFallado.charAt(0) == 'B') {

				FallaAsimetricas calculoFallaAsimetrica = new FallaAsimetricas(zBarraSecuencia0, zBarraSecuencia1,
						zBarraSecuencia2, barras, conexiones, conexiones1, barraFallada, conexiongene);

				angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

				magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

			}

			else if (tipoElementoFallado.charAt(0) == 'L') {

				FallaAsimetricaLineas calculaFallaAsimetricaLinea = new FallaAsimetricaLineas(zBarraSecuencia0,
						zBarraSecuencia1, zBarraSecuencia2, barras, conexiones, conexiones1, conexiongene,
						lineaFallada);

				angCorrientePuntoFallaFaseA = lineaFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB = lineaFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC = lineaFallada.getAngCorrientePuntoFallaFaseC();

				magCorrientePuntoFallaFaseA = lineaFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB = lineaFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC = lineaFallada.getMagcorrientePuntoFallaFaseC();

			}
		} else if (bifasicaATierra) {

			if (tipoElementoFallado.charAt(0) == 'B') {

				FallaLineaALineaTierra biaTierra = new FallaLineaALineaTierra(zBarraSecuencia0, zBarraSecuencia1,
						zBarraSecuencia2, barras, conexiones, conexiones1, barraFallada, conexiongene);

				angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

				magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

			} else if (tipoElementoFallado.charAt(0) == 'L') {

				FallaLineaALineaTierraEnLinea bifasicaATierraEnLinea = new FallaLineaALineaTierraEnLinea(
						zBarraSecuencia0, zBarraSecuencia1, zBarraSecuencia2, barras, conexiones, conexiones1,
						conexiongene, lineaFallada);

				angCorrientePuntoFallaFaseA = lineaFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB = lineaFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC = lineaFallada.getAngCorrientePuntoFallaFaseC();

				magCorrientePuntoFallaFaseA = lineaFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB = lineaFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC = lineaFallada.getMagcorrientePuntoFallaFaseC();

			}

		}

	}

	public double[][] obtenerMatrizAdyacencia(List<List<Edges>> borde) {

		double[][] resultado = new double[borde.size() - 1][borde.size() - 1];

		for (int i = 1; i < borde.size(); i++) {
			for (int j = 0; j < borde.get(i).size(); j++) {

				int u = borde.get(i).get(j).getU();
				int v = borde.get(i).get(j).getV();
				double peso = ((WeightEdeges) borde.get(i).get(j)).getWeight();

				resultado[u - 1][u - 1] -= 1 / peso;

				if (v != 0) {
					resultado[u - 1][v - 1] = 1 / peso;
				}

			}
		}

		return resultado;

	}

	public Complejo[][] calculoMatrizAdyacenciaFlujo() throws ExcepcionDivideCero {

		Complejo[][] resultado = new Complejo[barras.size()][barras.size()];

		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < resultado.length; j++) {

				resultado[i][j] = new Complejo();
			}
		}

		for (int i = 0; i < conexiones.size(); i++) {

			int x = barras.indexOf(conexiones.get(i).getBarra1());
			int y = barras.indexOf(conexiones.get(i).getBarra2());

			double B = -conexiones.get(i).getimpedanciaLineaZ1()
					/ (conexiones.get(i).getResitencia() * conexiones.get(i).getResitencia()
							+ conexiones.get(i).getimpedanciaLineaZ1() * conexiones.get(i).getimpedanciaLineaZ1());
			double G = conexiones.get(i).getResitencia()
					/ (conexiones.get(i).getResitencia() * conexiones.get(i).getResitencia()
							+ conexiones.get(i).getimpedanciaLineaZ1() * conexiones.get(i).getimpedanciaLineaZ1());
			double Y_medio = conexiones.get(i).getYMediaParalela();

			Complejo fueraDi = new Complejo(G, B);

			resultado[x][y] = Complejo.producto(new Complejo(-1, 0), fueraDi);

			resultado[y][x] = Complejo.producto(new Complejo(-1, 0), fueraDi);

			Complejo diagonal = resultado[x][x];
			double real = diagonal.getReal();
			double complejo = diagonal.getImag();
			real += G;
			complejo += B + Y_medio;

			resultado[x][x] = new Complejo(real, complejo);

			diagonal = resultado[y][y];
			real = diagonal.getReal();
			complejo = diagonal.getImag();
			real += G;
			complejo += B + Y_medio;

			resultado[y][y] = new Complejo(real, complejo);
		}
		
		for (int i = 0; i < conexiones1.size(); i++) {
			
			Transformador trafo= conexiones1.get(i);
			

			int x = barras.indexOf(conexiones1.get(i).getBarra1());
			int y = barras.indexOf(conexiones1.get(i).getBarra2());
			
			if(trafo.isHasTap()) {
				
				Complejo a= Complejo.polar2Cartesiano(trafo.getMagTab(), trafo.getAngtab());
				Complejo a_conj= Complejo.conjugado(a);
				
				double reactancia= -1/ trafo.getimpedanciaLineaZ1();
				
				Complejo Y_trafo= new Complejo(0.0,reactancia);
				
				resultado[y][y]= Complejo.suma(resultado[y][y], Complejo.cociente(Y_trafo, a));
				
				double magnitud_a_squared=1/a.getReal()*a.getReal()+a.getImag()*a.getImag(); 
				
				resultado[x][x]= Complejo.suma(resultado[x][x], Complejo.producto(new Complejo(magnitud_a_squared,0), Y_trafo));
				
				
				resultado[y][x]=Complejo.producto(new Complejo(-1,0), Complejo.cociente(Y_trafo, a));

				resultado[x][y]= Complejo.producto(new Complejo(-1,0), Complejo.cociente(Y_trafo, a_conj));
				
				continue;
				
			}
			else {
			

			double B = -conexiones1.get(i).getimpedanciaLineaZ1()
					/ (conexiones1.get(i).getResitencia() * conexiones1.get(i).getResitencia()
							+ conexiones1.get(i).getimpedanciaLineaZ1() * conexiones1.get(i).getimpedanciaLineaZ1());
			double G = conexiones1.get(i).getResitencia()
					/ (conexiones1.get(i).getResitencia() * conexiones1.get(i).getResitencia()
							+ conexiones1.get(i).getimpedanciaLineaZ1() * conexiones1.get(i).getimpedanciaLineaZ1());

			Complejo fueraDi = new Complejo(G, B);

			resultado[x][y] = Complejo.producto(new Complejo(-1, 0), fueraDi);

			resultado[y][x] = Complejo.producto(new Complejo(-1, 0), fueraDi);

			Complejo diagonal = resultado[x][x];
			double real = diagonal.getReal();
			double complejo = diagonal.getImag();
			real += G;
			complejo += B ;

			resultado[x][x] = new Complejo(real, complejo);

			diagonal = resultado[y][y];
			real = diagonal.getReal();
			complejo = diagonal.getImag();
			real += G;
			complejo += B ;

			resultado[y][y] = new Complejo(real, complejo);
			
			}
		}
		
		for(int i=0;i<bancos.size();i++) {
			
			int b= barras.indexOf(bancos.get(i).getBarra());
			
			resultado[b][b]= Complejo.suma(resultado[b][b], new Complejo(0,bancos.get(i).getPotenciaReactiva()/SPController.BASE_SISTEMA));
			
		}
		
	
		return resultado;

	}

	class WeightEdeges2 {

		protected Complejo weight;

		public WeightEdeges2(int u, int v, Complejo weight) {
			this.weight = weight;

		}

		public Complejo getWeight() {
			return weight;
		}

	}

	public void imprimirGrafo(List<List<Edges>> bordes, List<Barras> vertices) {

		for (int i = 0; i < vertices.size(); i++) {
			System.out.print("Barra " + vertices.get(i).getNombreBarra() + " : ");
			for (int j = 0; j < bordes.get(i).size(); j++) {

				System.out
				.print("(Barra " + bordes.get(i).get(j).getU() + " , " + " Barra " + bordes.get(i).get(j).getV()
						+ " , " + ((WeightEdeges) bordes.get(i).get(j)).getWeight() + " )");

			}
			System.out.println();
		}

	}

	public void imprimir(double[][] resultado) {

		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < resultado.length; j++) {

				System.out.print(resultado[i][j] + " ");
			}
			System.out.println();
		}

	}

}
