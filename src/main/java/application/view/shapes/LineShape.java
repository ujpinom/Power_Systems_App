package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import proyectoSistemasDePotencia.Lineas;

public class LineShape extends NetworkShape<Lineas> {

  protected final Polyline visualLine;
  protected final Polyline hitBox; // Línea invisible gruesa para detectar clics

  private final NetworkShape<?> startShape;
  private final NetworkShape<?> endShape;

  private AnchorPoint startAnchor;
  private AnchorPoint endAnchor;

  // Handles para edición de waypoints (Círculos)
  private final Group waypointHandles = new Group();

  // Listener para recalcular la ruta cuando los nodos se mueven
  private final ChangeListener<Number> positionListener =
      (obs, oldVal, newVal) -> updateConnectionPoints();

  private java.util.function.BiConsumer<proyectoSistemasDePotencia.Connectable, Boolean>
      onReconnectHandler;

  public void setOnReconnectRequest(
      java.util.function.BiConsumer<proyectoSistemasDePotencia.Connectable, Boolean> handler) {
    this.onReconnectHandler = handler;
  }

  public LineShape(Lineas model, NetworkShape<?> startShape, NetworkShape<?> endShape) {
    super(model);
    this.startShape = startShape;
    this.endShape = endShape;

    // Resolver Anchors: Priorizar índices del modelo, fallback a proximidad
    resolveAnchors();

    // 1. Línea "HitBox" (Invisible, ancha, para facilitar clic)
    hitBox = new Polyline();
    hitBox.setStrokeWidth(ShapeConstants.LINE_HITBOX_WIDTH);
    hitBox.setStroke(Color.TRANSPARENT);

    // 2. Línea Visual (Fina, visible)
    visualLine = new Polyline();
    visualLine.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);
    visualLine.setStroke(Color.BLACK);
    visualLine.setStrokeLineCap(StrokeLineCap.ROUND);

    // Añadir al grupo
    this.getChildren().addAll(hitBox, visualLine, waypointHandles);

    // Crear etiqueta
    createLabel(model.getNombreLinea(), 10, 10);

    // Escuchar cambios en los puntos del modelo para redibujar
    model
        .getListPuntosPolyLine()
        .addListener((javafx.collections.ListChangeListener<Double>) c -> updateConnectionPoints());

    // Inicializar listeners en los nodos conectados
    initConnectionListeners();

    // Calcular posición inicial
    updateConnectionPoints();

    // --- Suscripción a cambios del Modelo (Observer Pattern) ---
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombreLinea".equals(prop) || "nombrePersonalizado".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreLinea()));
          } else if ("anchorIndex1".equals(prop) || "anchorIndex2".equals(prop)) {
            javafx.application.Platform.runLater(
                () -> {
                  removeConnectionListeners();
                  resolveAnchors();
                  initConnectionListeners();
                  updateConnectionPoints();
                });
          }
        });
  }

  private void resolveAnchors() {
    // Start Anchor
    int idx1 = model.getAnchorIndex1();
    if (idx1 != -1 && idx1 < startShape.getAnchors().size()) {
      this.startAnchor = startShape.getAnchors().get(idx1);
    } else if (!startShape.getAnchors().isEmpty()) {
      this.startAnchor = findClosestAnchor(startShape, endShape);
    }

    // End Anchor
    int idx2 = model.getAnchorIndex2();
    if (idx2 != -1 && idx2 < endShape.getAnchors().size()) {
      this.endAnchor = endShape.getAnchors().get(idx2);
    } else if (!endShape.getAnchors().isEmpty()) {
      this.endAnchor = findClosestAnchor(endShape, startShape);
    }
  }

  private void initConnectionListeners() {
    // Si tenemos anchor, escuchamos sus coordenadas en escena
    if (startAnchor != null) {
      startAnchor.sceneXProperty().addListener(positionListener);
      startAnchor.sceneYProperty().addListener(positionListener);
    } else {
      // Fallback: Escuchar layout del shape
      startShape.layoutXProperty().addListener(positionListener);
      startShape.layoutYProperty().addListener(positionListener);
    }

    if (endAnchor != null) {
      endAnchor.sceneXProperty().addListener(positionListener);
      endAnchor.sceneYProperty().addListener(positionListener);
    } else {
      endShape.layoutXProperty().addListener(positionListener);
      endShape.layoutYProperty().addListener(positionListener);
    }
  }

  /**
   * Recalcula los puntos de la línea. Aquí es donde podemos implementar lógica de Ruteo Ortogonal
   * (Codos) en el futuro. Por ahora, haremos una conexión directa inteligente (Centro a Centro).
   */
  protected void updateConnectionPoints() {
    // Coordenadas de inicio y fin
    double startX, startY, endX, endY;

    if (startAnchor != null) {
      // Usar coordenadas de escena del anchor, convertidas a locales de este grupo
      // (si fuera necesario, pero el grupo LineShape suele estar en 0,0 del padre o
      // misma jerarquía)
      // Asumiendo que LineShape y BusShape comparten el mismo padre (Canvas), los
      // Scene coords del anchor pueden necesitar ajuste si el padre no es la Raíz.
      // PERO: AnchorPoint.sceneX es coord GLOBAL de escena.
      // LineShape.polyLine usa coordenadas locales.
      // Si LineShape está en un Pane, layoutX/Y son relativos al Pane.
      // Para simplificar: Asumimos que todos son hijos directos del
      // ZoomablePane/Canvas.
      // Entonces AnchorPoint calcula su posición respecto al Scene.
      // Necesitamos coordenadas respecto al PADRE de LineShape.

      // REVISIÓN: AnchorPoint.updateSceneCoordinates usa owner.localToParent.
      // "localToParent" da coordenadas en el sistema del padre (el Canvas).
      // Así que sceneX/Y en AnchorPoint son en realidad PARENT coords.
      startX = startAnchor.sceneXProperty().get();
      startY = startAnchor.sceneYProperty().get();
    } else {
      // Fallback usa la mitad del Bus si no hay anchors (Hardcoded 3 y 30)
      // Usamos constantes genéricas o calculamos centro
      // Es mejor si el NetworkShape tuviera un método getCenter()
      startX = startShape.getLayoutX() + ShapeConstants.BUS_HALF_WIDTH;
      startY = startShape.getLayoutY() + ShapeConstants.BUS_DEFAULT_HEIGHT / 2.0;
    }

    if (endAnchor != null) {
      endX = endAnchor.sceneXProperty().get();
      endY = endAnchor.sceneYProperty().get();
    } else {
      endX = endShape.getLayoutX() + ShapeConstants.BUS_HALF_WIDTH;
      endY = endShape.getLayoutY() + ShapeConstants.BUS_DEFAULT_HEIGHT / 2.0;
    }

    // Construir lista de todos los puntos: Inicio + Waypoints + Fin
    java.util.List<Double> allPoints = new java.util.ArrayList<>();
    allPoints.add(startX);
    allPoints.add(startY);

    // Waypoints del modelo
    allPoints.addAll(model.getListPuntosPolyLine());

    allPoints.add(endX);
    allPoints.add(endY);

    setPoints(allPoints.toArray(new Double[0]));

    // Actualizar handles de waypoints si está seleccionado
    if (isSelected()) {
      refreshWaypointHandles();
    }

    // Actualizar posición de la etiqueta (Promedio de todos los puntos)
    double avgX = 0, avgY = 0;
    for (int i = 0; i < allPoints.size(); i += 2) {
      avgX += allPoints.get(i);
      avgY += allPoints.get(i + 1);
    }
    avgX /= (allPoints.size() / 2);
    avgY /= (allPoints.size() / 2);

    updateLabelPosition(avgX, avgY);
  }

  private void refreshWaypointHandles() {
    ObservableList<Double> points = model.getListPuntosPolyLine();
    int waypointCount = points.size() / 2;

    // Si el número de handles no coincide, recrear todo (Ej: nuevo waypoint
    // añadido)
    if (waypointHandles.getChildren().size() != waypointCount) {
      waypointHandles.getChildren().clear();
      for (int i = 0; i < points.size(); i += 2) {
        WaypointShape handle = new WaypointShape(model, i, i + 1, points.get(i), points.get(i + 1));
        waypointHandles.getChildren().add(handle);
      }
      return;
    }

    // Si el número coincide, solo actualizar posiciones (Evita romper el Drag &
    // Drop)
    for (int i = 0; i < waypointCount; i++) {
      WaypointShape handle = (WaypointShape) waypointHandles.getChildren().get(i);

      // Solo actualizar si NO es el que se está arrastrando actualmente para evitar
      // saltos
      if (!handle.isDragging()) {
        handle.setLayoutX(points.get(i * 2));
        handle.setLayoutY(points.get(i * 2 + 1));
      }
    }
  }

  @Override
  protected boolean isZoomOnHoverEnabled() {
    return false; // Desactivar escalado geométrico para evitar que se desconecte de las barras
  }

  @Override
  protected void onHoverEntered() {
    visualLine.setStrokeWidth(ShapeConstants.LINE_HOVER_WIDTH);
    startAnchor.setVisible(true);
    endAnchor.setVisible(true);
  }

  @Override
  protected void onHoverExited() {
    visualLine.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);
    startAnchor.setVisible(false);
    endAnchor.setVisible(false);
  }

  private void setPoints(Double... coords) {
    visualLine.getPoints().setAll(coords);
    hitBox.getPoints().setAll(coords);
  }

  @Override
  protected boolean isSelected() {
    return visualLine.getEffect() != null;
  }

  @Override
  protected void applySelectionEffect() {
    visualLine.setStroke(Color.RED);
    visualLine.setEffect(new DropShadow(ShapeConstants.SELECTION_SHADOW_OFFSET, Color.CYAN));
  }

  @Override
  protected void fillContextMenu(javafx.scene.control.ContextMenu menu) {
    MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
    itemRenombrar.setOnAction(
        e -> {
          TextInputDialog dialog = new TextInputDialog(model.getNombreLinea());
          dialog.setTitle("Renombrar Línea");
          dialog.setHeaderText("Ingrese el nuevo ID:");
          dialog
              .showAndWait()
              .ifPresent(
                  nuevoNombre -> {
                    model.setNombrePersonalizado(nuevoNombre);
                    updateLabelText(nuevoNombre);
                  });
        });

    MenuItem itemEliminar = new MenuItem("Eliminar");
    itemEliminar.setOnAction(
        e -> {
          NetworkModel.getInstance().removeLinea(model);
        });

    MenuItem itemReconectarInicio = new MenuItem("Reconectar Anclaje Inicio");
    itemReconectarInicio.setOnAction(
        e -> {
          if (onReconnectHandler != null) {
            onReconnectHandler.accept(model, true); // true = start
          }
        });

    MenuItem itemReconectarFin = new MenuItem("Reconectar Anclaje Fin");
    itemReconectarFin.setOnAction(
        e -> {
          if (onReconnectHandler != null) {
            onReconnectHandler.accept(model, false); // false = end
          }
        });

    menu.getItems().addAll(itemRenombrar, itemReconectarInicio, itemReconectarFin, itemEliminar);
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    if (seleccionado) {
      applySelectionEffect();
      refreshWaypointHandles();
    } else {
      visualLine.setStroke(Color.BLACK);
      visualLine.setEffect(null);
      waypointHandles.getChildren().clear();
    }
  }

  @Override
  protected void updateModelCoordinates(double x, double y) {
    // Las líneas generalmente se definen por sus nodos, no por su propia posición
    // absoluta.
    // Sin embargo, si implementamos "waypoints" (puntos de quiebre) arrastrables,
    // aquí actualizaríamos esos puntos en el modelo.
  }

  // Método para limpiar listeners cuando se borre la línea (Evitar Memory Leaks)
  public void dispose() {
    removeConnectionListeners();
  }

  private void removeConnectionListeners() {
    if (startAnchor != null) {
      startAnchor.sceneXProperty().removeListener(positionListener);
      startAnchor.sceneYProperty().removeListener(positionListener);
    } else {
      startShape.layoutXProperty().removeListener(positionListener);
      startShape.layoutYProperty().removeListener(positionListener);
    }

    if (endAnchor != null) {
      endAnchor.sceneXProperty().removeListener(positionListener);
      endAnchor.sceneYProperty().removeListener(positionListener);
    } else {
      endShape.layoutXProperty().removeListener(positionListener);
      endShape.layoutYProperty().removeListener(positionListener);
    }
  }

  /** Encuentra el AnchorPoint del source más cercano al centro del target. */
  private AnchorPoint findClosestAnchor(NetworkShape<?> source, NetworkShape<?> target) {
    if (source.getAnchors().isEmpty()) return null;

    // Centro del target (aprox)
    double targetX = target.getLayoutX(); // + Width/2? No sabemos width genérico fácil
    double targetY = target.getLayoutY();

    AnchorPoint best = null;
    double minDst = Double.MAX_VALUE;

    for (AnchorPoint a : source.getAnchors()) {
      // Usamos sceneX/Y del anchor
      double dx = a.sceneXProperty().get() - targetX;
      double dy = a.sceneYProperty().get() - targetY;
      double dst = dx * dx + dy * dy;

      if (dst < minDst) {
        minDst = dst;
        best = a;
      }
    }
    return best;
  }
}
