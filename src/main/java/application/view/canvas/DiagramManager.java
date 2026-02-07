package application.view.canvas;

import application.enums.ToolType;
import application.model.project.NetworkChangeListener;
import application.model.project.NetworkModel;
import application.model.validation.ValidationResult;
import application.service.logging.LogService;
import application.view.canvas.handlers.*;
import application.view.panels.PropertiesPanel;
import application.view.shapes.BusShape;
import application.view.shapes.CargaShape;
import application.view.shapes.GenShape;
import application.view.shapes.LineShape;
import application.view.shapes.NetworkShape;
import application.view.shapes.TrafoShape;
import application.view.utils.UIUtils;
import java.util.function.Consumer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

/**
 * Mediador central para la gestión del diagrama. Coordina la interacción entre el lienzo, el modelo
 * y los gestores especializados.
 */
public class DiagramManager implements NetworkChangeListener {
  private final AnchorPane canvas;
  private final NetworkModel model;

  // Gestores especializados
  private final SelectionHandler selectionHandler;
  private final ShapeFactory shapeFactory;
  private final ConnectionHandler connectionHandler;
  private final ReconnectionHandler reconnectionHandler;
  private final SingleTerminalHandler singleTerminalHandler;

  // --- Tool and Feedback ---
  private ToolType currentTool = ToolType.NONE;
  private boolean connectionModeEnabled = false;
  private boolean singleConnectionMode = false;
  private Consumer<String> statusMessenger;

  public DiagramManager(AnchorPane canvas, PropertiesPanel propertiesPanel) {
    this.canvas = canvas;
    this.model = NetworkModel.getInstance();

    // Inicializar gestores
    this.selectionHandler = new SelectionHandler(model);
    this.shapeFactory = new ShapeFactory(this);
    this.connectionHandler = new ConnectionHandler(canvas, model, this);
    this.reconnectionHandler = new ReconnectionHandler(canvas, model, this);
    this.singleTerminalHandler = new SingleTerminalHandler(model, this);

    // Registrarse como observador universal
    this.model.addChangeListener(this);

    // Manejo de clic en fondo vacío
    this.canvas.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        e -> {
          if (e.getTarget() == canvas) {
            handleCanvasClick(e);
          }
        });

    // Manejo de movimiento del mouse (para la línea fantasma)
    this.canvas.addEventHandler(
        MouseEvent.MOUSE_MOVED,
        e -> {
          if (connectionHandler.isConnecting() && connectionHandler.getGhostLine() != null) {
            int size = connectionHandler.getGhostLine().getPoints().size();
            connectionHandler.getGhostLine().getPoints().set(size - 2, e.getX());
            connectionHandler.getGhostLine().getPoints().set(size - 1, e.getY());
          }
        });

    model
        .seleccionActualProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              propertiesPanel.mostrarPropiedades(newVal);
            });
  }

  @Override
  public void onAdded(Object element) {
    if (element instanceof Barras) {
      agregarBarraVisual((Barras) element);
    } else if (element instanceof Transformador) {
      agregarTransformadorVisual((Transformador) element);
    } else if (element instanceof Lineas) {
      agregarLineaVisual((Lineas) element);
    } else if (element instanceof Generadores) {
      agregarGeneradorVisual((Generadores) element);
    } else if (element instanceof Carga) {
      agregarCargaVisual((Carga) element);
    }
  }

  @Override
  public void onRemoved(Object element) {
    NetworkShape<?> shape = buscarShapePorModelo(element);
    if (shape != null) {
      selectionHandler.limpiarReferencia(shape);
      canvas.getChildren().remove(shape);

      if (shape instanceof LineShape) {
        ((LineShape) shape).dispose();
      }

      if (shape instanceof GenShape) {
        ((GenShape) shape).dispose();
      }

      if (shape instanceof CargaShape) {
        ((CargaShape) shape).dispose();
      }

      String label = element.toString();
      if (element instanceof Barras) label = "Barra " + ((Barras) element).getNombreBarra();
      else if (element instanceof Lineas)
        label = "Línea/Trafo " + ((Lineas) element).getNombreLinea();
      else if (element instanceof Generadores)
        label = "Generador " + ((Generadores) element).getNombreGenerador();
      else if (element instanceof Carga) label = "Carga " + ((Carga) element).getNombreCarga();

      LogService.getInstance().info(label + " eliminado del diagrama.");
    }
  }

  public void setConnectionMode(boolean enabled) {
    this.connectionModeEnabled = enabled;
    if (!enabled) connectionHandler.cancel();
  }

  public void setCurrentTool(ToolType tool) {
    this.currentTool = tool;
    setConnectionMode(tool == ToolType.LINEA || tool == ToolType.TRANSFORMADOR);
    setSingleConnectionMode(tool == ToolType.GENERADOR || tool == ToolType.CARGA);
  }

  public void setSingleConnectionMode(boolean enabled) {
    this.singleConnectionMode = enabled;
  }

  public void setStatusMessenger(Consumer<String> statusMessenger) {
    this.statusMessenger = statusMessenger;
  }

  public void postStatus(String message) {
    if (statusMessenger != null) {
      statusMessenger.accept(message);
    }
    LogService.getInstance().info(message);
  }

  private void handleCanvasClick(MouseEvent e) {
    double x = UIUtils.snap(e.getX(), 20.0);
    double y = UIUtils.snap(e.getY(), 20.0);

    if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
      switch (currentTool) {
        case LINEA:
        case TRANSFORMADOR:
          if (connectionHandler.isConnecting()) {
            connectionHandler.addWaypoint(e.getX(), e.getY());
          }
          break;
        case BARRA:
          crearBarra(x, y);
          break;
        case NONE:
          deseleccionarTodo();
          break;
        default:
          break;
      }
    }
  }

  private void crearBarra(double x, double y) {
    if (!UIUtils.validarProximidadBarra(x, y, model.getBarras())) {
      postStatus("Error: Espacio ocupado, seleccione otra ubicación.");
      return;
    }

    Barras logicaBarra = new Barras("Bus-" + model.getBarras().size());
    logicaBarra.setXbarra(x - 3);
    logicaBarra.setYbarra(y - 30);
    model.addBarra(logicaBarra);

    LogService.getInstance()
        .info("Barra " + logicaBarra.getNombreBarra() + " creada en (" + x + ", " + y + ")");

    postStatus("Barra " + logicaBarra.getNombreBarra() + " creada exitosamente.");
  }

  private void agregarBarraVisual(Barras barra) {
    if ("Tierra".equalsIgnoreCase(barra.getNombreBarra())) return;
    BusShape shape = shapeFactory.createBusShape(barra);
    canvas.getChildren().add(shape);
  }

  private void agregarLineaVisual(Lineas linea) {
    BusShape s1 = (BusShape) buscarShapePorModelo(linea.getBarra1());
    BusShape s2 = (BusShape) buscarShapePorModelo(linea.getBarra2());
    if (s1 != null && s2 != null) {
      LineShape shape = shapeFactory.createLineShape(linea, s1, s2);
      canvas.getChildren().add(0, shape);
    }
  }

  private void agregarTransformadorVisual(Transformador trafo) {
    BusShape s1 = (BusShape) buscarShapePorModelo(trafo.getBarra1());
    BusShape s2 = (BusShape) buscarShapePorModelo(trafo.getBarra2());
    if (s1 != null && s2 != null) {
      TrafoShape shape = shapeFactory.createTrafoShape(trafo, s1, s2);
      canvas.getChildren().add(shape);
    }
  }

  private void agregarGeneradorVisual(Generadores generador) {
    BusShape busShape = (BusShape) buscarShapePorModelo(generador.getBarra());
    if (busShape != null) {
      GenShape shape = shapeFactory.createGenShape(generador, busShape);
      canvas.getChildren().add(shape);
    }
  }

  private void agregarCargaVisual(proyectoSistemasDePotencia.Carga carga) {
    BusShape busShape = (BusShape) buscarShapePorModelo(carga.getBarra());
    if (busShape != null) {
      CargaShape shape = shapeFactory.createCargaShape(carga, busShape);
      canvas.getChildren().add(shape);
    }
  }

  /** Punto de entrada centralizado para clics en Barras. */
  public void handleBusClick(NetworkShape<?> shape, MouseEvent e) {
    e.consume();
    if (reconnectionHandler.isReconnecting()) {
      reconnectionHandler.handleClick(shape, e);
    } else if (connectionHandler.isConnecting()) {
      connectionHandler.complete(shape, e, currentTool);
    } else if (singleConnectionMode) {
      singleTerminalHandler.handleBusClick(shape, e, currentTool);
    } else if (connectionModeEnabled) {
      connectionHandler.start(shape, e);
    } else {
      selectionHandler.seleccionar(shape);
    }
  }

  /** Punto de entrada para clics en otros componentes (Líneas/Trafos). */
  public void handleNonBusClick(NetworkShape<?> shape, MouseEvent e) {
    e.consume();
    if (!connectionHandler.isConnecting() && !reconnectionHandler.isReconnecting()) {
      selectionHandler.seleccionar(shape);
    }
  }

  public void startAnchorReselection(
      proyectoSistemasDePotencia.Connectable element, Boolean isStart) {
    deseleccionarTodo();
    Object targetModel = isStart ? element.getBarra1() : element.getBarra2();
    NetworkShape<?> targetShape = buscarShapePorModelo(targetModel);
    reconnectionHandler.start(element, isStart, targetShape);
  }

  public boolean showValidationError(ValidationResult result) {
    if (UIUtils.showValidationWarning(result)) {
      connectionHandler.cancel();
      reconnectionHandler.cancel();
      return true;
    }
    return false;
  }

  public void deseleccionarTodo() {
    reconnectionHandler.cancel();
    selectionHandler.deseleccionarTodo();
  }

  private NetworkShape<?> buscarShapePorModelo(Object modelo) {
    for (javafx.scene.Node node : canvas.getChildren()) {
      if (node instanceof NetworkShape) {
        if (((NetworkShape<?>) node).getModel() == modelo) {
          return (NetworkShape<?>) node;
        }
      }
    }
    return null;
  }
}
