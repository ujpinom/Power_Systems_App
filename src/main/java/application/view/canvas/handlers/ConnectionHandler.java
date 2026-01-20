package application.view.canvas.handlers;

import application.enums.ToolType;
import application.model.project.NetworkModel;
import application.model.validation.ValidationResult;
import application.service.logging.LogService;
import application.view.canvas.DiagramManager;
import application.view.shapes.NetworkShape;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

/** Maneja la lógica de creación de nuevas conexiones (Líneas/Transformadores). */
public class ConnectionHandler {
  private final AnchorPane canvas;
  private final NetworkModel model;
  private final DiagramManager mediator;

  private boolean isConnecting = false;
  private NetworkShape<?> connectionSource = null;
  private Polyline ghostLine = null;
  private final List<Double> currentWaypoints = new ArrayList<>();
  private int startAnchorIndex = -1;
  private int endAnchorIndex = -1;

  public ConnectionHandler(AnchorPane canvas, NetworkModel model, DiagramManager mediator) {
    this.canvas = canvas;
    this.model = model;
    this.mediator = mediator;
  }

  public boolean isConnecting() {
    return isConnecting;
  }

  public Polyline getGhostLine() {
    return ghostLine;
  }

  public void start(NetworkShape<?> source, MouseEvent event) {
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
        LogService.getInstance().info("Conexión iniciada en Anchor " + startAnchorIndex);
      }
    }

    this.ghostLine = new Polyline();
    this.ghostLine.setStroke(Color.GRAY);
    this.ghostLine.getStrokeDashArray().addAll(5d, 5d);
    this.ghostLine.setStrokeWidth(2);
    this.ghostLine.setStrokeLineCap(StrokeLineCap.ROUND);
    this.ghostLine.setMouseTransparent(true);

    double startX, startY;
    if (startAnchorIndex != -1) {
      application.view.shapes.AnchorPoint ap = source.getAnchors().get(startAnchorIndex);
      startX = ap.sceneXProperty().get();
      startY = ap.sceneYProperty().get();
    } else {
      startX = source.getLayoutX() + 3;
      startY = source.getLayoutY() + 30;
    }

    this.ghostLine.getPoints().addAll(startX, startY, startX, startY);
    canvas.getChildren().add(ghostLine);
  }

  public void addWaypoint(double x, double y) {
    if (!isConnecting || ghostLine == null) return;

    double snapX = Math.round(x / 10) * 10;
    double snapY = Math.round(y / 10) * 10;

    currentWaypoints.add(snapX);
    currentWaypoints.add(snapY);

    int size = ghostLine.getPoints().size();
    ghostLine.getPoints().add(size - 2, snapX);
    ghostLine.getPoints().add(size - 1, snapY);

    LogService.getInstance().info("Añadido waypoint en " + snapX + ", " + snapY);
  }

  public void complete(NetworkShape<?> target, MouseEvent event, ToolType currentTool) {
    if (!isConnecting || connectionSource == null) return;

    if (target == connectionSource) {
      LogService.getInstance().warn("No se puede conectar un elemento consigo mismo.");
      return;
    }

    if (event.getTarget() instanceof javafx.scene.shape.Circle) {
      Object userData = ((javafx.scene.shape.Circle) event.getTarget()).getUserData();
      if (userData instanceof application.view.shapes.AnchorPoint) {
        application.view.shapes.AnchorPoint ap = (application.view.shapes.AnchorPoint) userData;
        this.endAnchorIndex = target.getAnchorIndex(ap);
        LogService.getInstance().info("Conexión terminada en Anchor " + endAnchorIndex);
      }
    }

    Object sourceModel = connectionSource.getModel();
    Object targetModel = target.getModel();

    if (sourceModel instanceof Barras && targetModel instanceof Barras) {
      Barras b1 = (Barras) sourceModel;
      Barras b2 = (Barras) targetModel;

      ValidationResult result =
          model.getValidator().validateConnection(b1, b2, startAnchorIndex, endAnchorIndex);
      if (mediator.showValidationError(result)) {
        return;
      }

      if (currentTool == ToolType.LINEA) {
        crearLinea(b1, b2);
      } else if (currentTool == ToolType.TRANSFORMADOR) {
        crearTransformador(b1, b2);
      }
    }

    cancel();
  }

  private void crearLinea(Barras b1, Barras b2) {
    Lineas nuevaLinea = new Lineas(b1, b2);
    nuevaLinea.setNombreLinea("L-" + (model.getLineas().size() + 1));
    nuevaLinea.getListPuntosPolyLine().addAll(currentWaypoints);
    nuevaLinea.setAnchorIndex1(startAnchorIndex);
    nuevaLinea.setAnchorIndex2(endAnchorIndex);
    model.addLinea(nuevaLinea);

    LogService.getInstance()
        .info(
            "Línea "
                + nuevaLinea.getNombreLinea()
                + " creada entre "
                + b1.getNombreBarra()
                + " y "
                + b2.getNombreBarra());
  }

  private void crearTransformador(Barras b1, Barras b2) {
    Transformador trafo = new Transformador(b1, b2);
    trafo.setNombreLinea("T-" + (model.getTransformadores().size() + 1));
    trafo.getListPuntosPolyLine().addAll(currentWaypoints);
    trafo.setAnchorIndex1(startAnchorIndex);
    trafo.setAnchorIndex2(endAnchorIndex);
    model.addTransformador(trafo);

    LogService.getInstance()
        .info(
            "Transformador "
                + trafo.getNombreLinea()
                + " creado entre "
                + b1.getNombreBarra()
                + " y "
                + b2.getNombreBarra());
  }

  public void cancel() {
    this.isConnecting = false;
    this.connectionSource = null;
    this.currentWaypoints.clear();
    if (this.ghostLine != null) {
      canvas.getChildren().remove(ghostLine);
      this.ghostLine = null;
    }
  }
}
