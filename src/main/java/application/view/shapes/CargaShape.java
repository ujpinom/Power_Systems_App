package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import proyectoSistemasDePotencia.Carga;

/**
 * Representación visual de una Carga en el diagrama. La carga se muestra como un triángulo
 * apuntando hacia afuera de la barra.
 */
public class CargaShape extends NetworkShape<Carga> {

  // Dimensiones de la carga
  private static final double LOAD_SIZE = 15.0;

  // Componentes visuales
  private final Polygon simboloTriangulo;
  private final Line lineaConexion;

  // Referencia al shape de la barra conectada
  private final BusShape busShape;
  private AnchorPoint connectedAnchor;

  // Listener para actualizar posición cuando la barra se mueve
  private final ChangeListener<Number> positionListener = (obs, oldVal, newVal) -> updatePosition();

  private boolean isSelected = false;

  // Handler para reconexión
  private java.util.function.BiConsumer<proyectoSistemasDePotencia.Connectable, Boolean>
      onReconnectHandler;

  public void setOnReconnectRequest(
      java.util.function.BiConsumer<proyectoSistemasDePotencia.Connectable, Boolean> handler) {
    this.onReconnectHandler = handler;
  }

  public CargaShape(Carga carga, BusShape busShape) {
    super(carga);
    this.busShape = busShape;
    enableDrag();

    // Resolver el anchor de conexión
    resolveAnchor();

    // 1. Línea de conexión
    lineaConexion = new Line();
    lineaConexion.setStroke(Color.BLACK);
    lineaConexion.setStrokeWidth(2.0);

    // 2. Símbolo de carga (Triángulo)
    simboloTriangulo = new Polygon();
    // Triángulo que apunta hacia abajo por defecto: (0,0), (-10,-20), (10,-20)
    simboloTriangulo
        .getPoints()
        .addAll(0.0, 0.0, -LOAD_SIZE, -LOAD_SIZE * 1.5, LOAD_SIZE, -LOAD_SIZE * 1.5);
    simboloTriangulo.setFill(Color.WHITE);
    simboloTriangulo.setStroke(Color.BLACK);
    simboloTriangulo.setStrokeWidth(2.0);

    // Añadir componentes al grupo
    this.getChildren().addAll(lineaConexion, simboloTriangulo);

    // Crear etiqueta
    createLabel(carga.getNombreCarga(), LOAD_SIZE + 5, -LOAD_SIZE);

    // Inicializar listeners de posición
    initPositionListeners();

    // Calcular posición inicial
    updatePosition();

    // Suscribirse a cambios del modelo
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombreCarga".equals(prop) || "nombrePersonalizado".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreCarga()));
          } else if ("anchorIndex1".equals(prop)) {
            javafx.application.Platform.runLater(
                () -> {
                  removePositionListeners();
                  resolveAnchor();
                  initPositionListeners();
                  updatePosition();
                });
          }
        });
  }

  private void resolveAnchor() {
    int anchorIndex = model.getAnchorIndex1();
    if (anchorIndex >= 0 && anchorIndex < busShape.getAnchors().size()) {
      this.connectedAnchor = busShape.getAnchors().get(anchorIndex);
    } else if (!busShape.getAnchors().isEmpty()) {
      this.connectedAnchor = busShape.getAnchors().get(0);
      model.setAnchorIndex1(0);
    }
  }

  private void initPositionListeners() {
    if (connectedAnchor != null) {
      connectedAnchor.sceneXProperty().addListener(positionListener);
      connectedAnchor.sceneYProperty().addListener(positionListener);
    } else {
      busShape.layoutXProperty().addListener(positionListener);
      busShape.layoutYProperty().addListener(positionListener);
    }
  }

  private void removePositionListeners() {
    if (connectedAnchor != null) {
      connectedAnchor.sceneXProperty().removeListener(positionListener);
      connectedAnchor.sceneYProperty().removeListener(positionListener);
    } else {
      busShape.layoutXProperty().removeListener(positionListener);
      busShape.layoutYProperty().removeListener(positionListener);
    }
  }

  private void updatePosition() {
    double anchorX, anchorY;

    if (connectedAnchor != null) {
      anchorX = connectedAnchor.sceneXProperty().get();
      anchorY = connectedAnchor.sceneYProperty().get();
    } else {
      anchorX = busShape.getLayoutX() + ShapeConstants.BUS_HALF_WIDTH;
      anchorY = busShape.getLayoutY() + ShapeConstants.BUS_DEFAULT_HEIGHT / 2.0;
    }

    double genX = model.getXCenter();
    double genY = model.getYCenter();

    if (genX == 0 && genY == 0) {
      // Posición inicial por defecto
      if (connectedAnchor != null && connectedAnchor.getRelX() <= 0) {
        genX = anchorX - 50;
      } else {
        genX = anchorX + 50;
      }
      genY = anchorY;
      model.setXCenter(genX);
      model.setYCenter(genY);
    }

    this.setLayoutX(genX);
    this.setLayoutY(genY);

    // Actualizar línea de conexión
    lineaConexion.setStartX(0);
    lineaConexion.setStartY(0);
    lineaConexion.setEndX(anchorX - genX);
    lineaConexion.setEndY(anchorY - genY);

    // Rotar el triángulo según la dirección de la conexión
    double dx = genX - anchorX;
    double dy = genY - anchorY;
    double angle = Math.toDegrees(Math.atan2(dy, dx));
    simboloTriangulo.setRotate(angle + 90);

    updateLabelPosition(LOAD_SIZE + 5, -LOAD_SIZE);
  }

  @Override
  protected void updateModelCoordinates(double x, double y) {
    model.setXCenter(x);
    model.setYCenter(y);
    updatePosition();
  }

  @Override
  protected boolean isSelected() {
    return isSelected;
  }

  @Override
  protected void applySelectionEffect() {
    simboloTriangulo.setStroke(Color.RED);
    this.setEffect(new DropShadow(ShapeConstants.SELECTION_SHADOW_RADIUS, Color.CYAN));
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    this.isSelected = seleccionado;
    if (seleccionado) {
      applySelectionEffect();
    } else {
      simboloTriangulo.setStroke(Color.BLACK);
      this.setEffect(null);
    }
  }

  @Override
  protected void fillContextMenu(javafx.scene.control.ContextMenu menu) {
    MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
    itemRenombrar.setOnAction(
        e -> {
          TextInputDialog dialog = new TextInputDialog(model.getNombreCarga());
          dialog.setTitle("Renombrar Carga");
          dialog.setHeaderText("Ingrese el nuevo nombre:");
          dialog
              .showAndWait()
              .ifPresent(
                  nuevoNombre -> {
                    model.setNombrePersonalizado(nuevoNombre);
                    updateLabelText(nuevoNombre);
                  });
        });

    MenuItem itemReconectar = new MenuItem("Reconectar Anclaje");
    itemReconectar.setOnAction(
        e -> {
          if (onReconnectHandler != null) {
            onReconnectHandler.accept(model, true);
          }
        });

    MenuItem itemEliminar = new MenuItem("Eliminar");
    itemEliminar.setOnAction(
        e -> {
          NetworkModel.getInstance().removeCarga(model);
        });

    menu.getItems().addAll(itemRenombrar, itemReconectar, itemEliminar);
  }

  @Override
  protected boolean isZoomOnHoverEnabled() {
    return false;
  }

  public void dispose() {
    removePositionListeners();
  }
}
