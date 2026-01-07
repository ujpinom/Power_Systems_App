package application.controller.main;

import application.DeterminacionPotenciasBarras;
import application.EcuacionesVoltajeYPotencia;
import application.NewtonRaphson;
import application.enums.ToolType;
import application.model.project.NetworkModel;
import application.view.canvas.DiagramManager;
import application.view.panels.PropertiesPanel;
import grafos.Edges;
import grafos.MyGraph;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.CompensadorEstatico;
import proyectoSistemasDePotencia.Complejo;
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
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;
import proyectoSistemasDePotencia.Zbarra;
import weightedGraphs.WeightEdeges;
import weightedGraphs.WeightedGraph;

public class SPController implements Initializable {

  @FXML private ToggleButton Compensador;

  // --- VARIABLES DE LA NUEVA INTERFAZ ---
  @FXML private ScrollPane scrollContainer;
  @FXML private StackPane zoomContainer;
  @FXML private Label zoomLabel;
  @FXML private TableView<?> tablaBarras; // El <?> evita errores si no tienes clase modelo aún
  @FXML private TableView<?> tablaRamas;

  @FXML private TextArea infoFlujo;

  @FXML private MenuItem Action1;

  @FXML private MenuItem GaussSeidel;

  @FXML private MenuItem matrizSecuencia;

  @FXML private MenuItem flujoTabular;

  @FXML private MenuItem fallaTabular;

  @FXML private AnchorPane areaDibujo = new AnchorPane();
  DoubleProperty myScale = new SimpleDoubleProperty(1.0);
  @FXML private TextField infoElemento;

  @FXML private TextField infoTare;

  @FXML private TextField infoPosiMouse;
  @FXML private TextArea display;

  @FXML private ToggleButton barra;

  @FXML private Button undo;

  @FXML private Button undo1;

  @FXML private ToggleButton banco;

  @FXML private ToggleButton generador;

  @FXML private ToggleButton trafo;

  @FXML private ToggleButton none;

  @FXML private Button ejecutar;

  @FXML private ToggleButton linea;

  @FXML private ToggleButton carga;

  @FXML private ToggleGroup group;

  @FXML private TextField factorAceleracion;

  public static double BASE_SISTEMA = 100;
  private Complejo[][] perdidadsPotencia;
  private Complejo[][] potenciaEntranteBarras;

  private List<Complejo>[] perdidasLineas;

  // --- State Management ---
  private ToolType currentTool = ToolType.NONE;

  private boolean trifasica;
  private boolean monofasica;
  private boolean lineaALinea;
  private boolean bifasicaATierra;
  private boolean fPotencia;
  private List<Barras> barras;
  private List<Lineas> conexiones;
  private List<Transformador> conexiones1;
  private List<Generadores> conexiongene;
  private List<Carga> cargas;
  private List<Bancos> bancos;
  private List<CompensadorEstatico> compensadores;
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
  private String nombreCompensador = "E";

  private ArrayList<Double> distanciasLineas = new ArrayList<>();
  private ArrayList<Double> corGenerador = new ArrayList<>();
  private ArrayList<Double> corCarga = new ArrayList<>();
  private ArrayList<Double> corBanco = new ArrayList<>();
  private ArrayList<Double> corCompensador = new ArrayList<>();
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
  private double currentScale = 1.0;
  private static boolean borrarUltimoElemento = false;
  private double FACTOR_ACELERACION = 1.6;
  private int NUMERO_ITERACIONES =
      50; // numero de iteraciones para el problema de flujo de potencia.

  private LinkedList<Object> restablecerElementos = new LinkedList<>();

  private String metodoFlujoPotencia = "Seidel";

  private double epsilon = 0.01;

  private Barras barraCompensacion = null;
  private NetworkModel networkModel;
  private DiagramManager diagramManager;
  private PropertiesPanel propertiesPanel;
  @FXML private VBox rightPanelContainer;
  ;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    scrollContainer.setHvalue(0.5);
    scrollContainer.setVvalue(0.5);
    this.networkModel = NetworkModel.getInstance();

    this.diagramManager = new DiagramManager(areaDibujo);
    this.propertiesPanel = new PropertiesPanel();
    rightPanelContainer.getChildren().clear();
    rightPanelContainer.getChildren().add(propertiesPanel);
    VBox.setVgrow(propertiesPanel, Priority.ALWAYS);
    networkModel
        .seleccionActualProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              propertiesPanel.mostrarPropiedades(newVal);
            });

    // ... existing initializations ...

    // Manejo de tecla ESC para cancelar conexión/herramienta
    areaDibujo
        .sceneProperty()
        .addListener(
            (obs, oldScene, newScene) -> {
              if (newScene != null) {
                newScene.addEventFilter(
                    KeyEvent.KEY_PRESSED,
                    event -> {
                      if (event.getCode() == KeyCode.ESCAPE) {
                        diagramManager.cancelConnection();
                        none.setSelected(true);
                        currentTool = ToolType.NONE;
                        infoElemento.setText("Elemento: Edición");
                        diagramManager.setConnectionMode(false);
                      }
                    });
              }
            });
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

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/MatricesSeq.fxml"));
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

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/MatricesSeq.fxml"));
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
    infoFlujo.setText(
        "MÃ©todo: "
            + metodoFlujoPotencia
            + "\nAceleraciÃ³n: "
            + FACTOR_ACELERACION
            + "\nIteraciones: "
            + String.format("%d", NUMERO_ITERACIONES)
            + "\nEpsilon: "
            + epsilon
            + "\nConvergencia: "
            + 0);
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
    currentTool = ToolType.LINEA;
    infoElemento.setText("Elemento: Lí­nea");
    diagramManager.setConnectionMode(true);
  }

  @FXML
  private void barraSelected(ActionEvent e) {
    currentTool = ToolType.BARRA;
    infoElemento.setText("Elemento: Barra");
    diagramManager.setConnectionMode(false);
  }

  @FXML
  private void compensadorSelected(ActionEvent e) {
    currentTool = ToolType.COMPENSADOR;
    infoElemento.setText("Elemento: Compensador EstÃ¡tico");
  }

  @FXML
  private void trafoSelected(ActionEvent e) {
    currentTool = ToolType.TRANSFORMADOR;
    infoElemento.setText("Elemento: Transformador");
  }

  @FXML
  private void genSelected(ActionEvent e) {
    currentTool = ToolType.GENERADOR;
    infoElemento.setText("Elemento: Generador");
  }

  @FXML
  private void cargaSelected(ActionEvent e) {
    currentTool = ToolType.CARGA;
    infoElemento.setText("Elemento: Carga");
  }

  @FXML
  private void bancoSelected(ActionEvent e) {
    currentTool = ToolType.BANCO;
    infoElemento.setText("Elemento: Banco");
  }

  @FXML
  private void editionSelected(ActionEvent e) {
    currentTool = ToolType.NONE;
    infoElemento.setText("Elemento: EdiciÃ³n");
    diagramManager.setConnectionMode(false);
  }

  @FXML
  private void borrarUltimoElemento(ActionEvent e) {
    NetworkModel.getInstance().undoLastAction();
  }

  @FXML
  private void limpiarArea(ActionEvent e) {
    // Mostrar advertencia antes de proceder
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmar Limpieza");
    alert.setHeaderText("¿Estás seguro de que deseas limpiar el área?");
    alert.setContentText("Esta acción eliminará todos los elementos y no se puede deshacer.");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      // 1. Limpiar el modelo (Esto disparará los listeners de DiagramManager)
      NetworkModel.getInstance().clearAll();

      // 2. Resetear herramienta y selección
      currentTool = ToolType.NONE;
      infoElemento.setText("Elemento: Edición");
      diagramManager.deseleccionarTodo();
      diagramManager.setConnectionMode(false);

      // 3. Resetear Zoom a 100%
      if (currentScale != 1.0) {
        updateZoom(1.0 / currentScale);
      }

      System.out.println("Controller: Area de dibujo limpiada.");
    } else {
      System.out.println("Controller: Limpieza cancelada por el usuario.");
    }
  }

  @FXML
  private void ejecutar(ActionEvent e) throws ExcepcionDivideCero {

    List<List<Integer>> bb = getGraph().componentesConectados();

    if (bb.size() == 2) {

      if (fPotencia) {

        Complejo[][] m3 = calculoMatrizAdyacenciaFlujo();

        perdidadsPotencia = new Complejo[barras.size()][barras.size()];
        potenciaEntranteBarras = new Complejo[barras.size()][barras.size()];

        for (int i = 0; i < perdidadsPotencia.length; i++) {
          for (int j = 0; j < perdidadsPotencia.length; j++) {

            perdidadsPotencia[i][j] =
                new Complejo(); // No se tiene en cuenta la admitancia de carga de la
            // lÃ­nea
            potenciaEntranteBarras[i][j] =
                new Complejo(); // Se tiene en cuenta la admitancia de carga de
            // la linea.

          }
        }

        if (metodoFlujoPotencia.equals("Seidel")) {

          EcuacionesVoltajeYPotencia gaussS =
              new EcuacionesVoltajeYPotencia(
                  barras, NUMERO_ITERACIONES, FACTOR_ACELERACION, epsilon);

          List<Complejo>[] solucion =
              gaussS.calcularFlujoPotencia(conexiongene, bancos, cargas, m3);

          DeterminacionPotenciasBarras.potenciasEnBarrasComPV(barras, solucion, m3);

          DeterminacionPotenciasBarras.calculoPerdidasPotenciaLineas(
              barras,
              solucion,
              conexiones,
              conexiones1,
              m3,
              perdidadsPotencia,
              potenciaEntranteBarras);

          System.out.println("RESULTADOS Angulos Y voltajes:");

          for (int i = 1; i < solucion.length; i++) {

            for (Complejo c : solucion[i]) {

              System.out.println("Barra" + i);

              System.out.println(c.modulo() + "  " + c.argumento());
            }

            System.out.println();
          }

          System.out.println();

          for (int i = 1; i < barras.size(); i++) {

            Barras b = barras.get(i);

            if (b.isBarraCompensacion()) {

              System.out.println("Generacion: ");

              System.out.println(
                  "Real: "
                      + b.getFlowPowerRealCalculada()
                      + " Imag: "
                      + b.getFlowPowerImagCalculada());
            }

            if (b.isBarraPV()) {

              System.out.println("Generacion: ");

              System.out.println(
                  "Real: "
                      + b.getGenerador().getMWSalida()
                      + " Imag: "
                      + b.getFlowPowerImagCalculada());
            }

            if (b.isBarraFromPV2PQ()) {

              System.out.println("Generacion: ");

              System.out.println(
                  "Real: "
                      + b.getGenerador().getMWSalida()
                      + " Imag: "
                      + b.getGenerador().getMVarSalida());
            }

            if (b.containsCarga()) {

              System.out.println("Carga: ");

              System.out.println(
                  "Real: "
                      + b.getCarga().getPotenciaActiva()
                      + " Imag: "
                      + b.getCarga().getPotenciaReactiva());
            }
          }

          System.out.println("Potencia en barras");

          for (int i = 0; i < perdidadsPotencia.length; i++) {
            for (int j = 0; j < perdidadsPotencia.length; j++) {

              System.out.print(potenciaEntranteBarras[i][j] + " ");
            }

            System.out.println();
          }

          ////

        } else if (metodoFlujoPotencia.equals("Raphson")) {

          NewtonRaphson raphson = new NewtonRaphson(barras, NUMERO_ITERACIONES, epsilon, m3);

          raphson.calcularFlujoPotencia(conexiongene, bancos, cargas);

          List<Double>[] solucionVoltajes = raphson.solucionVoltajes();

          List<Double>[] solucionAngulos = raphson.solucionAngulos();

          List<Complejo>[] solucion = raphson.solucionFormaCompleja();

          DeterminacionPotenciasBarras.potenciasEnBarrasComPV(barras, solucion, m3);

          DeterminacionPotenciasBarras.calculoPerdidasPotenciaLineas(
              barras,
              solucion,
              conexiones,
              conexiones1,
              m3,
              perdidadsPotencia,
              potenciaEntranteBarras);

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

              System.out.print(d * 180 / Math.PI + " ");
            }

            System.out.println();
          }

          System.out.println();

          for (int i = 1; i < barras.size(); i++) {

            Barras b = barras.get(i);

            if (b.isBarraCompensacion()) {

              System.out.println("Generacion: ");

              System.out.println(
                  "Real: "
                      + b.getFlowPowerRealCalculada()
                      + " Imag: "
                      + b.getFlowPowerImagCalculada());

            } else if (b.isBarraPV()) {

              System.out.println("Generacion: ");

              System.out.println(
                  "Real: "
                      + b.getGenerador().getMWSalida()
                      + " Imag: "
                      + b.getFlowPowerImagCalculada());
            }

            if (b.containsCarga()) {

              System.out.println("Carga: ");

              System.out.println(
                  "Real: "
                      + b.getCarga().getPotenciaActiva()
                      + " Imag: "
                      + b.getCarga().getPotenciaReactiva());
            }
          }

          System.out.println("Potencia en barras");

          for (int i = 0; i < perdidadsPotencia.length; i++) {
            for (int j = 0; j < perdidadsPotencia.length; j++) {

              System.out.print(potenciaEntranteBarras[i][j] + " ");
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
    this.barras = NetworkModel.getInstance().getBarras();
    this.conexiones = NetworkModel.getInstance().getLineas();
    this.conexiones1 = NetworkModel.getInstance().getTransformadores();
    this.conexiongene = NetworkModel.getInstance().getGeneradores();
    this.cargas = NetworkModel.getInstance().getCargas();
    this.bancos = NetworkModel.getInstance().getBancos();
    this.compensadores = NetworkModel.getInstance().getCompensadores();

    for (int i = 0; i < listaBarras.length; i++) {

      listaBarras[i] = new LinkedList<Integer>();
    }
  }

  private double snap(double value) {
    double gridSize = 20.0;
    return Math.round(value / gridSize) * gridSize;
  }

  @FXML
  private void zoomIn(ActionEvent event) {
    updateZoom(1.2); // Aumentar 20%
  }

  @FXML
  private void zoomOut(ActionEvent event) {
    updateZoom(0.833); // Disminuir
  }

  private void updateZoom(double factor) {
    // Limites de zoom (entre 10% y 500%)
    double newScale = currentScale * factor;
    if (newScale < 0.1 || newScale > 5.0) return;

    currentScale = newScale;

    // Aplicar transformación al área de dibujo
    Scale scaleTransform = new Scale(currentScale, currentScale, 0, 0);
    areaDibujo.getTransforms().clear();
    areaDibujo.getTransforms().add(scaleTransform);

    // Ajustar el tamaño del contenedor para que el ScrollPane sepa que el contenido
    // cambió
    // Esto es clave para que las barras de scroll funcionen bien al hacer zoom
    zoomContainer.setPrefWidth(areaDibujo.getPrefWidth() * currentScale);
    zoomContainer.setPrefHeight(areaDibujo.getPrefHeight() * currentScale);

    zoomLabel.setText(String.format("%.0f%%", currentScale * 100));
  }
  // --- LÓGICA DE CREACIÓN DE OBJETOS ---

  private boolean validarProximidad(double x, double y) {
    double radioMinimo = 50.0; // Distancia mínima en píxeles
    for (Barras b : barras) {
      // Ignoramos la barra "Tierra" si es la primera (índice 0) o si no tiene
      // coordenadas reales aún
      if (b.getNombreBarra().equals("Tierra")) continue;

      // Calcular distancia Euclideana entre el punto de click y las barras existentes
      double dist = Math.sqrt(Math.pow(x - b.getXbarra(), 2) + Math.pow(y - b.getYbarra(), 2));

      if (dist < radioMinimo) {
        return false; // Conflicto espacial detectado
      }
    }
    return true; // Espacio libre
  }

  private void crearBarra(double x, double y) {

    if (!validarProximidad(x, y)) {
      infoTare.setText("Error: Espacio ocupado, seleccione otra ubicación.");
      return;
    }

    String nombreDefault = "Bus-" + (barras.size());
    Barras logicaBarra = new Barras(nombreDefault);
    logicaBarra.setXbarra(x - 3);
    logicaBarra.setYbarra(y - 30);
    // barras.add(logicaBarra); // Ya no es necesario, addBarra lo añade al modelo
    // centralizado
    System.out.println("Barra creada en: " + x + ", " + y);
    NetworkModel.getInstance().addBarra(logicaBarra);
    infoTare.setText("Barra creada exitosamente.");
  }

  @FXML
  private void mouseClicked(MouseEvent e) throws IOException {

    areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
    double x = snap(e.getX());
    double y = snap(e.getY());
    if (e.getButton() == MouseButton.PRIMARY) {
      switch (currentTool) {
        case BARRA:
          crearBarra(x, y);
          break;
        case NONE:
          // Lógica para seleccionar/ver propiedades
          System.out.println("Modo Selección: Click en " + x + ", " + y);
          break;
        case TRANSFORMADOR:
          break;
        case GENERADOR:
          break;
        case CARGA:
          break;
        case BANCO:
          break;
        case COMPENSADOR:
          break;
        default:
          break;
      }
      return;
    }
  }

  @FXML
  private void dragEvent(MouseEvent e) {
    areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
  }

  @FXML
  private void mouseEvent(MouseEvent e) {
    areaDibujo.setCursor(javafx.scene.Cursor.CROSSHAIR);
  }

  @FXML private TextField MVAbase;

  public Node tipoElemento(double x, double y) {

    for (int i = 0; i < lista.size(); i++) {

      if (lista.get(i).contains(x, y)) {
        return lista.get(i);
      }
    }

    return null;
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

  public void
      crearGrafos() { // Crea las matrices de ayancencia y los diferentes tipos de matrices de
    // impedancia de sencuencia

    if (tipoElementoFallado != null) {

      if (trifasica) fallaTrifasica();

      if (monofasica || bifasicaATierra) fallaMonofasica();

      // if(bifasicaATierra);
      // fallaBifasicaATierra();

      if (lineaALinea) fallaLineaALinea();
    } else {

      JOptionPane.showMessageDialog(
          null, "POR FAVOR UBIQUE LA FALLA YA SEA SOBRE UNA BARRA O LÃNEA");
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

      FallaLineaALinea fallalinealinea =
          new FallaLineaALinea(
              zBarraSecuencia1,
              zBarraSecuencia2,
              barras,
              conexiones,
              conexiones1,
              conexiongene,
              barraFallada,
              borde1);

      angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
      angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
      angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

      magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
      magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
      magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

    } else if (tipoElementoFallado.charAt(0) == 'L') {

      FallaLineaALineaLinea fallaLineaALineaLinea =
          new FallaLineaALineaLinea(
              zBarraSecuencia1,
              zBarraSecuencia2,
              barras,
              conexiones,
              conexiones1,
              conexiongene,
              lineaFallada);

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

      FallaTrifasica calculoFalla =
          new FallaTrifasica(
              zBarraSecuencia1, tipoElementoFallado, barras, conexiones, conexiones1, conexiongene);

      angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
      angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
      angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

      magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
      magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
      magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();
    } else if (tipoElementoFallado.charAt(0) == 'L') {

      FallaTrifasicaLinea fallatrifasicalinea =
          new FallaTrifasicaLinea(
              zBarraSecuencia1, lineaFallada, barras, conexiones, conexiones1, conexiongene);

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

        z0 +=
            conexiones1.get(i).getImpedanciaAterrizamientoPrimaria()
                + conexiones1.get(i).getImpedanciaAterrizamientoSecundaria();

      } else if (conexiones1.get(i).getConexionPrimaria().contains("Y-")
          && conexiones1.get(i).getConexionSecundaria().contains("Y-")) {

        z0 += 10000000;

      } else if ((conexiones1.get(i).getConexionPrimaria().contains("Y-")
              && conexiones1.get(i).getConexionSecundaria().contains("YN"))
          || (conexiones1.get(i).getConexionPrimaria().contains("YN")
              && conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {

        z0 += 10000000;
      } else if (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
          && conexiones1.get(i).getConexionSecundaria().contains("DELTA")) {

        z0 += 10000000;

      } else if ((conexiones1.get(i).getConexionPrimaria().contains("Y-")
              && conexiones1.get(i).getConexionSecundaria().contains("DELTA"))
          || (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
              && conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {

        z0 += 10000000;
      } else if ((conexiones1.get(i).getConexionPrimaria().contains("YN")
              && conexiones1.get(i).getConexionSecundaria().contains("DELTA"))
          || (conexiones1.get(i).getConexionPrimaria().contains("DELTA")
              && conexiones1.get(i).getConexionSecundaria().contains("YN"))) {

        if (conexiones1.get(i).getConexionPrimaria().contains("YN")) {

          z0 += conexiones1.get(i).getImpedanciaAterrizamientoPrimaria();

          String xx =
              Character.toString(
                  conexiones1
                      .get(i)
                      .getConexionPrimaria()
                      .charAt(conexiones1.get(i).getConexionPrimaria().length() - 1));

          x = Integer.parseInt(xx);
          y = 0;

        } else if (conexiones1.get(i).getConexionSecundaria().contains("YN")) {

          String xx =
              Character.toString(
                  conexiones1
                      .get(i)
                      .getConexionSecundaria()
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

        FallaAsimetricas calculoFallaAsimetrica =
            new FallaAsimetricas(
                zBarraSecuencia0,
                zBarraSecuencia1,
                zBarraSecuencia2,
                barras,
                conexiones,
                conexiones1,
                barraFallada,
                conexiongene);

        angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
        angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
        angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

        magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
        magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
        magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

      } else if (tipoElementoFallado.charAt(0) == 'L') {

        FallaAsimetricaLineas calculaFallaAsimetricaLinea =
            new FallaAsimetricaLineas(
                zBarraSecuencia0,
                zBarraSecuencia1,
                zBarraSecuencia2,
                barras,
                conexiones,
                conexiones1,
                conexiongene,
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

        FallaLineaALineaTierra biaTierra =
            new FallaLineaALineaTierra(
                zBarraSecuencia0,
                zBarraSecuencia1,
                zBarraSecuencia2,
                barras,
                conexiones,
                conexiones1,
                barraFallada,
                conexiongene);

        angCorrientePuntoFallaFaseA = barraFallada.getAngCorrientePuntoFallaFaseA();
        angCorrientePuntoFallaFaseB = barraFallada.getAngCorrientePuntoFallaFaseB();
        angCorrientePuntoFallaFaseC = barraFallada.getAngCorrientePuntoFallaFaseC();

        magCorrientePuntoFallaFaseA = barraFallada.getMagcorrientePuntoFallaFaseA();
        magCorrientePuntoFallaFaseB = barraFallada.getMagcorrientePuntoFallaFaseB();
        magCorrientePuntoFallaFaseC = barraFallada.getMagcorrientePuntoFallaFaseC();

      } else if (tipoElementoFallado.charAt(0) == 'L') {

        FallaLineaALineaTierraEnLinea bifasicaATierraEnLinea =
            new FallaLineaALineaTierraEnLinea(
                zBarraSecuencia0,
                zBarraSecuencia1,
                zBarraSecuencia2,
                barras,
                conexiones,
                conexiones1,
                conexiongene,
                lineaFallada);

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

      double B =
          -conexiones.get(i).getimpedanciaLineaZ1()
              / (conexiones.get(i).getResitencia() * conexiones.get(i).getResitencia()
                  + conexiones.get(i).getimpedanciaLineaZ1()
                      * conexiones.get(i).getimpedanciaLineaZ1());
      double G =
          conexiones.get(i).getResitencia()
              / (conexiones.get(i).getResitencia() * conexiones.get(i).getResitencia()
                  + conexiones.get(i).getimpedanciaLineaZ1()
                      * conexiones.get(i).getimpedanciaLineaZ1());
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

      Transformador trafo = conexiones1.get(i);

      int x = barras.indexOf(conexiones1.get(i).getBarra1());
      int y = barras.indexOf(conexiones1.get(i).getBarra2());

      if (trafo.isHasTap()) {

        Complejo a = Complejo.polar2Cartesiano(trafo.getMagTab(), trafo.getAngtab());
        Complejo a_conj = Complejo.conjugado(a);

        double reactancia = -1 / trafo.getimpedanciaLineaZ1();

        Complejo Y_trafo = new Complejo(0.0, reactancia);

        resultado[y][y] = Complejo.suma(resultado[y][y], Complejo.cociente(Y_trafo, a));

        double magnitud_a_squared = 1 / a.getReal() * a.getReal() + a.getImag() * a.getImag();

        resultado[x][x] =
            Complejo.suma(
                resultado[x][x], Complejo.producto(new Complejo(magnitud_a_squared, 0), Y_trafo));

        resultado[y][x] = Complejo.producto(new Complejo(-1, 0), Complejo.cociente(Y_trafo, a));

        resultado[x][y] =
            Complejo.producto(new Complejo(-1, 0), Complejo.cociente(Y_trafo, a_conj));

        continue;

      } else {

        double B =
            -conexiones1.get(i).getimpedanciaLineaZ1()
                / (conexiones1.get(i).getResitencia() * conexiones1.get(i).getResitencia()
                    + conexiones1.get(i).getimpedanciaLineaZ1()
                        * conexiones1.get(i).getimpedanciaLineaZ1());
        double G =
            conexiones1.get(i).getResitencia()
                / (conexiones1.get(i).getResitencia() * conexiones1.get(i).getResitencia()
                    + conexiones1.get(i).getimpedanciaLineaZ1()
                        * conexiones1.get(i).getimpedanciaLineaZ1());

        Complejo fueraDi = new Complejo(G, B);

        resultado[x][y] = Complejo.producto(new Complejo(-1, 0), fueraDi);

        resultado[y][x] = Complejo.producto(new Complejo(-1, 0), fueraDi);

        Complejo diagonal = resultado[x][x];
        double real = diagonal.getReal();
        double complejo = diagonal.getImag();
        real += G;
        complejo += B;

        resultado[x][x] = new Complejo(real, complejo);

        diagonal = resultado[y][y];
        real = diagonal.getReal();
        complejo = diagonal.getImag();
        real += G;
        complejo += B;

        resultado[y][y] = new Complejo(real, complejo);
      }
    }

    for (int i = 0; i < bancos.size(); i++) {

      int b = barras.indexOf(bancos.get(i).getBarra());

      resultado[b][b] =
          Complejo.suma(
              resultado[b][b],
              new Complejo(0, bancos.get(i).getPotenciaReactiva() / SPController.BASE_SISTEMA));
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

        System.out.print(
            "(Barra "
                + bordes.get(i).get(j).getU()
                + " , "
                + " Barra "
                + bordes.get(i).get(j).getV()
                + " , "
                + ((WeightEdeges) bordes.get(i).get(j)).getWeight()
                + " )");
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

  // --- HANDLERS FOR TOOLS ---

  // private void handleGeneradorCreation(double x, double y) {
  // Barras b = getContainingVertex(x, y);
  // if (b != null) {
  // if (!corGenerador.contains(b.getXbarra())) {
  // b.setxCoorG(x);
  // b.setyCoorG(y);

  // Generadores gene = new Generadores(nombreGenerador, 1, 1, 1, b);

  // conexiongene.add(gene);
  // b.setBarraPV(true);
  // b.setBarraPQ(false);
  // b.setGenerador(gene);
  // objetosCreados.add(gene);

  // corGenerador.add(b.getXbarra());
  // repaint();
  // }
  // }
  // }

  // private void handleCargaCreation(double x, double y) {
  // Barras b = getContainingVertex(x, y);
  // if (b != null) {
  // if (!corCarga.contains(b.getXbarra())) {
  // b.setCoordenadasCarga(new Point2D(x, y));
  // Carga carga = new Carga(new Point2D(x, y), b, nombreCarga);
  // cargas.add(carga);
  // objetosCreados.add(carga);
  // if (b.isBarraPV()) {
  // b.setBarraPQ(false);
  // } else {
  // b.setBarraPQ(true);
  // }
  // b.setCarga(carga);
  // corCarga.add(b.getXbarra());
  // repaint();
  // }
  // }
  // }

  // private void handleBancoCreation(double x, double y) {
  // Barras b = getContainingVertex(x, y);
  // if (b != null) {
  // if (!corBanco.contains(b.getXbarra())) {
  // b.setCoordenadasBanco(new Point2D(x, y));
  // Bancos banco = new Bancos(new Point2D(x, y), b, nombreBanco);
  // bancos.add(banco);
  // objetosCreados.add(banco);
  // b.setBanco(banco);
  // corBanco.add(b.getXbarra());
  // repaint();
  // }
  // }
  // }

  // private void handleCompensadorCreation(double x, double y) {
  // Barras b = getContainingVertex(x, y);
  // if (b != null) {
  // if (!corCompensador.contains(b.getXbarra())) {
  // b.setCoordenadaCompensador(new Point2D(x, y));
  // CompensadorEstatico cp = new CompensadorEstatico(new Point2D(x, y), b,
  // nombreCompensador);
  // b.setBarraPV(true);
  // b.setBarraPQ(false);
  // compensadores.add(cp);
  // objetosCreados.add(cp);
  // b.setCompensador(cp);
  // corCompensador.add(b.getXbarra());
  // repaint();
  // }
  // }
  // }
}
