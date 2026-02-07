package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import proyectoSistemasDePotencia.Generadores;

/**
 * Representación visual de un Generador en el diagrama. El generador se muestra como un círculo con
 * una onda senoidal (~) conectado a una barra mediante una línea.
 */
public class GenShape extends NetworkShape<Generadores> {

  // Dimensiones del generador
  private static final double GEN_RADIUS = 18.0;
  private static final double CONNECTION_LINE_LENGTH = 30.0;

  // Componentes visuales
  private final Circle cuerpoGenerador;
  private final SVGPath simboloOnda;
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

  public GenShape(Generadores generador, BusShape busShape) {
    super(generador);
    this.busShape = busShape;
    enableDrag();

    // Resolver el anchor de conexión
    resolveAnchor();

    // 1. Línea de conexión (del generador hacia la barra)
    lineaConexion = new Line();
    lineaConexion.setStroke(Color.BLACK);
    lineaConexion.setStrokeWidth(2.0);

    // 2. Cuerpo del generador (Círculo)
    cuerpoGenerador = new Circle(GEN_RADIUS);
    cuerpoGenerador.setFill(Color.WHITE);
    cuerpoGenerador.setStroke(Color.BLACK);
    cuerpoGenerador.setStrokeWidth(2.0);

    // 3. Símbolo de onda senoidal (~) usando SVGPath
    simboloOnda = new SVGPath();
    // Path de una onda senoidal simple: M-12,0 Q-6,-8 0,0 Q6,8 12,0
    simboloOnda.setContent("M-10,0 Q-5,-7 0,0 Q5,7 10,0");
    simboloOnda.setStroke(Color.BLACK);
    simboloOnda.setStrokeWidth(2.0);
    simboloOnda.setFill(Color.TRANSPARENT);

    // Añadir componentes al grupo
    this.getChildren().addAll(lineaConexion, cuerpoGenerador, simboloOnda);

    // Crear etiqueta
    createLabel(generador.getNombreGenerador(), GEN_RADIUS + 5, -GEN_RADIUS);

    // Inicializar listeners de posición
    initPositionListeners();

    // Calcular posición inicial
    updatePosition();

    // Suscribirse a cambios del modelo
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombreGenerador".equals(prop) || "nombrePersonalizado".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreGenerador()));
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
      // Fallback: usar el primer anchor
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

  /**
   * Actualiza la posición del generador basándose en el anchor de la barra y su posición interna.
   */
  private void updatePosition() {
    double anchorX, anchorY;

    if (connectedAnchor != null) {
      anchorX = connectedAnchor.sceneXProperty().get();
      anchorY = connectedAnchor.sceneYProperty().get();
    } else {
      // Fallback: centro de la barra
      anchorX = busShape.getLayoutX() + ShapeConstants.BUS_HALF_WIDTH;
      anchorY = busShape.getLayoutY() + ShapeConstants.BUS_DEFAULT_HEIGHT / 2.0;
    }

    // Determinar dirección de conexión basada en la posición del anchor
    double dirX, dirY;
    if (connectedAnchor != null) {
      // Si el anchor está a la izquierda de la barra, el generador va hacia la
      // izquierda
      if (connectedAnchor.getRelX() <= 0) {
        dirX = -1;
      } else {
        dirX = 1;
      }
      dirY = 0; // Conexión horizontal por defecto
    } else {
      dirX = 0;
      dirY = 1; // Conexión vertical hacia abajo
    }

    // Calcular posición del centro del generador
    double genX = model.getXCenter();
    double genY = model.getYCenter();

    // Si las coordenadas no están inicializadas, calcularlas basándose en el anchor
    if (genX == 0 && genY == 0) {
      genX = anchorX + dirX * (CONNECTION_LINE_LENGTH + GEN_RADIUS);
      genY = anchorY + dirY * (CONNECTION_LINE_LENGTH + GEN_RADIUS);
      model.setXCenter(genX);
      model.setYCenter(genY);
    }

    // Posicionar el grupo en el centro del generador
    this.setLayoutX(genX);
    this.setLayoutY(genY);

    // La línea de conexión va desde el borde del círculo hasta el anchor
    // Calcular el punto en el borde del círculo más cercano al anchor
    double dx = anchorX - genX;
    double dy = anchorY - genY;
    double dist = Math.sqrt(dx * dx + dy * dy);

    if (dist > 0) {
      // Punto en el borde del círculo
      double borderX = (dx / dist) * GEN_RADIUS;
      double borderY = (dy / dist) * GEN_RADIUS;

      // La línea va del borde del círculo al anchor
      lineaConexion.setStartX(borderX);
      lineaConexion.setStartY(borderY);
      lineaConexion.setEndX(anchorX - genX);
      lineaConexion.setEndY(anchorY - genY);
    }

    // El cuerpo y símbolo están en 0,0 relativo al grupo (LayoutX/Y es el centro)
    cuerpoGenerador.setCenterX(0);
    cuerpoGenerador.setCenterY(0);
    simboloOnda.setLayoutX(0);
    simboloOnda.setLayoutY(0);

    // Actualizar posición de la etiqueta
    updateLabelPosition(GEN_RADIUS + 5, -GEN_RADIUS);
  }

  @Override
  protected void updateModelCoordinates(double x, double y) {
    model.setXCenter(x);
    model.setYCenter(y);
    // Actualizar visualmente la línea de conexión después del arrastre
    updatePosition();
  }

  @Override
  protected boolean isSelected() {
    return isSelected;
  }

  @Override
  protected void applySelectionEffect() {
    cuerpoGenerador.setStroke(Color.RED);
    this.setEffect(new DropShadow(ShapeConstants.SELECTION_SHADOW_RADIUS, Color.CYAN));
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    this.isSelected = seleccionado;
    if (seleccionado) {
      applySelectionEffect();
    } else {
      cuerpoGenerador.setStroke(Color.BLACK);
      this.setEffect(null);
    }
  }

  @Override
  protected void fillContextMenu(javafx.scene.control.ContextMenu menu) {
    MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
    itemRenombrar.setOnAction(
        e -> {
          TextInputDialog dialog = new TextInputDialog(model.getNombreGenerador());
          dialog.setTitle("Renombrar Generador");
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
            onReconnectHandler.accept(model, true); // true = terminal 1 (único)
          }
        });

    MenuItem itemEliminar = new MenuItem("Eliminar");
    itemEliminar.setOnAction(
        e -> {
          NetworkModel.getInstance().removeGenerador(model);
        });

    menu.getItems().addAll(itemRenombrar, itemReconectar, itemEliminar);
  }

  @Override
  protected boolean isZoomOnHoverEnabled() {
    return false;
  }

  /** Limpia los listeners para evitar memory leaks. */
  public void dispose() {
    removePositionListeners();
  }
}
