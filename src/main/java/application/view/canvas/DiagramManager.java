package application.view.canvas;

import application.model.project.NetworkChangeListener;
import application.model.project.NetworkModel;
import application.model.validation.ValidationResult;
import application.view.shapes.BusShape;
import application.view.shapes.LineShape;
import application.view.shapes.NetworkShape;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Lineas;

public class DiagramManager implements NetworkChangeListener {
  private final AnchorPane canvas;
  private final NetworkModel model;

  // Variable para recordar quién está seleccionado (Genérico)
  private NetworkShape<?> seleccionActual = null;

  // --- Variables para Creación de Conexiones ---
  private boolean isConnecting = false;
  private NetworkShape<?> connectionSource = null;
  private Polyline ghostLine = null;
  private final java.util.List<Double> currentWaypoints = new java.util.ArrayList<>();
  private int startAnchorIndex = -1;
  private int endAnchorIndex = -1;

  public DiagramManager(AnchorPane canvas) {
    this.canvas = canvas;
    this.model = NetworkModel.getInstance();

    // Registrarse como observador universal
    this.model.addChangeListener(this);

    // Manejo de clic en fondo vacío
    this.canvas.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        e -> {
          if (e.getTarget() == canvas) {
            System.out.println("Manager: Clic en fondo vacío");
            if (isConnecting) {
              // Añadir un waypoint intermedio
              addWaypoint(e.getX(), e.getY());
            } else {
              deseleccionarTodo();
            }
          }
        });

    // Manejo de movimiento del mouse (para la línea fantasma)
    this.canvas.addEventHandler(
        MouseEvent.MOUSE_MOVED,
        e -> {
          if (isConnecting && ghostLine != null) {
            // Actualizar el último punto de la polyline fantasma al mouse
            int size = ghostLine.getPoints().size();
            ghostLine.getPoints().set(size - 2, e.getX());
            ghostLine.getPoints().set(size - 1, e.getY());
          }
        });
  }

  @Override
  public void onAdded(Object element) {
    if (element instanceof Barras) {
      agregarBarraVisual((Barras) element);
    } else if (element instanceof Lineas) {
      agregarLineaVisual((Lineas) element);
    }
  }

  @Override
  public void onRemoved(Object element) {
    if (element instanceof Barras) {
      removerBarraVisual((Barras) element);
    } else if (element instanceof Lineas) {
      removerLineaVisual((Lineas) element);
    }
  }

  // --- Control de Modo ---
  private boolean connectionModeEnabled = false;

  public void setConnectionMode(boolean enabled) {
    this.connectionModeEnabled = enabled;
    if (!enabled) cancelConnection();
  }

  private void agregarBarraVisual(Barras barra) {
    // Evitar pintar la barra de "Tierra" (Bus 0) que es solo lógica
    if ("Tierra".equalsIgnoreCase(barra.getNombreBarra())) {
      return;
    }

    BusShape shape = new BusShape(barra);
    shape.setOnMouseClicked(
        e -> {
          e.consume();
          // Lógica de Estado: ¿Estamos seleccionando o conectando?
          if (isConnecting) {
            completeConnection(shape, e);
          } else if (connectionModeEnabled) {
            startConnection(shape, e);
          } else {
            seleccionarShape(shape);
          }
        });

    canvas.getChildren().add(shape);
  }

  // --- Lógica de Conexión ---

  public void startConnection(NetworkShape<?> source, MouseEvent event) {
    this.isConnecting = true;
    this.connectionSource = source;
    this.currentWaypoints.clear();
    this.startAnchorIndex = -1;
    this.endAnchorIndex = -1;

    // Detectar si el clic fue en un anchor específico
    if (event.getTarget() instanceof javafx.scene.shape.Circle) {
      Object userData = ((javafx.scene.shape.Circle) event.getTarget()).getUserData();
      if (userData instanceof application.view.shapes.AnchorPoint) {
        application.view.shapes.AnchorPoint ap = (application.view.shapes.AnchorPoint) userData;
        this.startAnchorIndex = source.getAnchorIndex(ap);
        System.out.println("Manager: Conexión iniciada en Anchor " + startAnchorIndex);
      }
    }

    // Crear polyline fantasma visual
    this.ghostLine = new Polyline();
    this.ghostLine.setStroke(Color.GRAY);
    this.ghostLine.getStrokeDashArray().addAll(5d, 5d);
    this.ghostLine.setStrokeWidth(2);
    this.ghostLine.setStrokeLineCap(StrokeLineCap.ROUND);

    // CRITICAL FIX: Hacer que la línea fantasma sea transparente al mouse
    // para que no intercepte los clics dirigidos al canvas.
    this.ghostLine.setMouseTransparent(true);

    // Punto inicial: Si hay anchor específico, usar su posición. Si no, centro.
    double startX, startY;
    if (startAnchorIndex != -1) {
      application.view.shapes.AnchorPoint ap = source.getAnchors().get(startAnchorIndex);
      startX = ap.sceneXProperty().get();
      startY = ap.sceneYProperty().get();
    } else {
      startX = source.getLayoutX() + 3;
      startY = source.getLayoutY() + 30;
    }

    // Añadir punto inicial y punto temporal del mouse
    this.ghostLine.getPoints().addAll(startX, startY, startX, startY);

    canvas.getChildren().add(ghostLine);
  }

  private void addWaypoint(double x, double y) {
    if (!isConnecting || ghostLine == null) return;

    // Aplicar Snap to Grid
    double snapX = Math.round(x / 10) * 10;
    double snapY = Math.round(y / 10) * 10;

    // Guardar en la lista de waypoints
    currentWaypoints.add(snapX);
    currentWaypoints.add(snapY);

    // Añadir a la polyline fantasma (el último punto sigue siendo el mouse)
    // Insertamos antes del último par de coordenadas
    int size = ghostLine.getPoints().size();
    ghostLine.getPoints().add(size - 2, snapX);
    ghostLine.getPoints().add(size - 1, snapY);

    System.out.println("Manager: Añadido waypoint en " + snapX + ", " + snapY);
  }

  private void completeConnection(NetworkShape<?> target, MouseEvent event) {
    if (!isConnecting || connectionSource == null) return;

    if (target == connectionSource) {
      System.out.println("Manager: No se puede conectar un elemento consigo mismo.");
      return;
    }

    // Detectar si el clic fue en un anchor específico del target
    if (event.getTarget() instanceof javafx.scene.shape.Circle) {
      Object userData = ((javafx.scene.shape.Circle) event.getTarget()).getUserData();
      if (userData instanceof application.view.shapes.AnchorPoint) {
        application.view.shapes.AnchorPoint ap = (application.view.shapes.AnchorPoint) userData;
        this.endAnchorIndex = target.getAnchorIndex(ap);
        System.out.println("Manager: Conexión terminada en Anchor " + endAnchorIndex);
      }
    }

    Object sourceModel = connectionSource.getModel();
    Object targetModel = target.getModel();

    if (sourceModel instanceof Barras && targetModel instanceof Barras) {
      Barras b1 = (Barras) sourceModel;
      Barras b2 = (Barras) targetModel;

      // VALIDACIÓN DE LOGICA DE NEGOCIO
      ValidationResult result =
          model.getValidator().validateConnection(b1, b2, startAnchorIndex, endAnchorIndex);

      if (!result.isValid()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación de Negocio");
        alert.setHeaderText("Operación no permitida");
        alert.setContentText(result.getMessage());
        alert.showAndWait();
        cancelConnection();
        return;
      }

      crearLinea(b1, b2, connectionSource, target);
    }

    cancelConnection(); // Limpiar estado
  }

  private void crearLinea(Barras b1, Barras b2, NetworkShape<?> shape1, NetworkShape<?> shape2) {
    // 1. Crear Modelo
    Lineas nuevaLinea = new Lineas(b1, b2);
    nuevaLinea.setNombreLinea("L-" + (model.getLineas().size() + 1));

    // Transferir waypoints al modelo
    nuevaLinea.getListPuntosPolyLine().addAll(currentWaypoints);

    // Guardar índices de anchors
    nuevaLinea.setAnchorIndex1(startAnchorIndex);
    nuevaLinea.setAnchorIndex2(endAnchorIndex);

    // Agregar al NetworkModel (El listener se encargará de crear la visual)
    model.addLinea(nuevaLinea);

    System.out.println(
        "Manager: Conexión creada entre " + b1.getNombreBarra() + " y " + b2.getNombreBarra());
  }

  private void agregarLineaVisual(Lineas linea) {
    // Buscar las figuras de las barras correspondientes
    NetworkShape<?> shape1 = buscarShapePorModelo(linea.getBarra1());
    NetworkShape<?> shape2 = buscarShapePorModelo(linea.getBarra2());

    if (shape1 != null && shape2 != null) {
      LineShape lineShape = new LineShape(linea, shape1, shape2);

      // Evento de selección para la línea
      lineShape.setOnMouseClicked(
          e -> {
            e.consume();
            if (!isConnecting) seleccionarShape(lineShape);
          });

      // Añadir al canvas (Al fondo, index 0, para que quede detrás de las barras)
      canvas.getChildren().add(0, lineShape);
    }
  }

  public void cancelConnection() {
    this.isConnecting = false;
    this.connectionSource = null;
    this.currentWaypoints.clear();
    if (this.ghostLine != null) {
      canvas.getChildren().remove(ghostLine);
      this.ghostLine = null;
    }
  }

  // --- Fin Lógica de Conexión ---

  // Método genérico para seleccionar cualquier NetworkShape
  private void seleccionarShape(NetworkShape<?> shape) {
    // 1. Si había algo seleccionado antes, lo apagamos
    if (seleccionActual != null) {
      seleccionActual.setSeleccionado(false);
    }
    seleccionActual = shape;
    seleccionActual.setSeleccionado(true);

    // Debug
    Object modelData = shape.getModel();
    if (modelData instanceof Barras) {
      System.out.println("Manager: Seleccionada barra -> " + ((Barras) modelData).getNombreBarra());
    }

    model.setSeleccionActual(modelData);
  }

  public void deseleccionarTodo() {
    if (seleccionActual != null) {
      seleccionActual.setSeleccionado(false);
      seleccionActual = null;
      model.setSeleccionActual(null);
    }
  }

  private void removerBarraVisual(Barras barra) {
    canvas
        .getChildren()
        .removeIf(
            node -> {
              if (node instanceof BusShape && node.getUserData() == barra) {
                // Si borramos la barra seleccionada, limpiamos la referencia
                if (node == seleccionActual) seleccionActual = null;
                return true;
              }
              return false;
            });
  }

  private void removerLineaVisual(Lineas linea) {
    canvas
        .getChildren()
        .removeIf(
            node -> {
              boolean isTarget = false;
              if (node instanceof LineShape) {
                if (((LineShape) node).getModel() == linea) {
                  isTarget = true;
                }
              }

              if (isTarget) {
                if (node == seleccionActual) {
                  seleccionActual = null;
                }
                return true;
              }
              return false;
            });
  }

  private NetworkShape<?> buscarShapePorModelo(Object modelo) {
    for (javafx.scene.Node node : canvas.getChildren()) {
      if (node instanceof NetworkShape) {
        Object m = ((NetworkShape<?>) node).getModel();
        // Importante: comparar referencias o equals
        if (m == modelo) {
          return (NetworkShape<?>) node;
        }
      }
    }
    return null;
  }
}
