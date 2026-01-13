package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import proyectoSistemasDePotencia.Barras;

public class BusShape extends NetworkShape<Barras> {

  private final Rectangle cuerpoBarra;
  private final Paint colorBase;
  private boolean isSelected = false;

  private ResizeHandle handleSuperior;
  private ResizeHandle handleInferior;

  public BusShape(Barras barra) {
    super(barra); // Pasa el modelo al padre
    enableDrag();

    // Determinar el color base según el tipo de barra
    if (barra.isBarraCompensacion()) {
      this.colorBase = Color.BLUE;
    } else {
      this.colorBase = Color.BLACK;
    }

    // Cuerpo de la barra
    this.cuerpoBarra =
        new Rectangle(0, 0, ShapeConstants.BUS_WIDTH, ShapeConstants.BUS_DEFAULT_HEIGHT);
    this.cuerpoBarra.setFill(colorBase);
    this.cuerpoBarra.setStroke(Color.TRANSPARENT);

    // Añadir forma principal
    this.getChildren().add(cuerpoBarra);

    // Inicializar Anchors
    initAnchors();

    // Inicializar Handles de Redimensionamiento
    initResizeHandles();

    // Crear etiqueta usando el método del padre
    createLabel(
        barra.getNombreBarra(),
        ShapeConstants.BUS_LABEL_OFFSET_X,
        ShapeConstants.BUS_LABEL_OFFSET_Y);

    // Posicionar el Grupo en el Canvas
    this.setLayoutX(barra.getXbarra());
    this.setLayoutY(barra.getYbarra());

    this.setUserData(barra);

    // --- Suscripción a cambios del Modelo (Observer Pattern) ---
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombrePersonalizado".equals(prop) || "nombreBarra".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreBarra()));
          }
        });
  }

  @Override
  protected void fillContextMenu(javafx.scene.control.ContextMenu menu) {
    MenuItem itemRotar = new MenuItem("Rotar 90°");
    itemRotar.setOnAction(e -> this.setRotate(this.getRotate() + 90));

    MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
    itemRenombrar.setOnAction(
        e -> {
          TextInputDialog dialog = new TextInputDialog(model.getNombreBarra());
          dialog.setTitle("Renombrar Barra");
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
          NetworkModel.getInstance().removeBarra(model);
        });

    menu.getItems().addAll(itemRenombrar, itemRotar, itemEliminar);
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    this.isSelected = seleccionado;
    if (seleccionado) {
      applySelectionEffect();
    } else {
      // Restaurar estado normal
      this.cuerpoBarra.setFill(colorBase);
      this.setEffect(null);
    }
    // Mostrar/Ocultar handles
    if (handleSuperior != null) handleSuperior.setVisible(seleccionado);
    if (handleInferior != null) handleInferior.setVisible(seleccionado);
  }

  private void initResizeHandles() {
    handleSuperior = new ResizeHandle(ShapeConstants.RESIZE_HANDLE_RADIUS);
    handleSuperior.setCursor(javafx.scene.Cursor.S_RESIZE);
    handleSuperior.setCenterX(ShapeConstants.BUS_HALF_WIDTH);
    handleSuperior.setCenterY(0);
    handleSuperior.setVisible(false);

    handleInferior = new ResizeHandle(ShapeConstants.RESIZE_HANDLE_RADIUS);
    handleInferior.setCursor(javafx.scene.Cursor.S_RESIZE);
    handleInferior.setCenterX(ShapeConstants.BUS_HALF_WIDTH);
    handleInferior.setCenterY(cuerpoBarra.getHeight());
    handleInferior.setVisible(false);

    // Lógica de arrastre
    handleSuperior.setupDrag(
        (dx, dy) -> {
          double oldHeight = cuerpoBarra.getHeight();
          double newHeight = oldHeight - dy;
          if (newHeight > ShapeConstants.BUS_MIN_HEIGHT) { // Límite mínimo
            cuerpoBarra.setHeight(newHeight);
            this.setLayoutY(this.getLayoutY() + dy);
            // El handle superior no necesita moverse relativamente al grupo (sigue en Y=0)
            // Pero el inferior sí debe bajar/subir
            handleInferior.setCenterY(newHeight);
            updateModelCoordinates(getLayoutX(), getLayoutY());
            updateAnchorsAfterResize();
          }
        });

    handleInferior.setupDrag(
        (dx, dy) -> {
          double oldHeight = cuerpoBarra.getHeight();
          double newHeight = oldHeight + dy;
          if (newHeight > ShapeConstants.BUS_MIN_HEIGHT) {
            cuerpoBarra.setHeight(newHeight);
            handleInferior.setCenterY(newHeight);
            // Sincronizar coordenadas
            updateAnchorsAfterResize();
          }
        });

    this.getChildren().addAll(handleSuperior, handleInferior);
  }

  private void initAnchors() {
    // Inicializar anchors (Izquierda y Derecha)
    for (int i = 0; i <= 2; i++) {
      double y;
      if (i == 0) {
        y = ShapeConstants.BUS_ANCHOR_VERTICAL_INSET;
      } else if (i == 1) {
        y = cuerpoBarra.getHeight() / 2.0;
      } else {
        y = cuerpoBarra.getHeight() - ShapeConstants.BUS_ANCHOR_VERTICAL_INSET;
      }

      // Izquierda (X=0)
      AnchorPoint left = new AnchorPoint(this, 0, y);
      anchors.add(left);
      anchorVisuals.getChildren().add(left.getVisual());

      // Derecha (X=Width)
      AnchorPoint right = new AnchorPoint(this, ShapeConstants.BUS_WIDTH, y);
      anchors.add(right);
      anchorVisuals.getChildren().add(right.getVisual());
    }
  }

  private void updateAnchorsAfterResize() {
    // Recalcular posiciones de anchors verticales
    double h = cuerpoBarra.getHeight();

    // Asumimos orden de creación: 0:LeftTop, 1:RightTop, 2:LeftMid, 3:RightMid,
    // 4:LeftBot, 5:RightBot
    // No... el bucle hace: i=0 (Left, Right), i=1 (Left, Right)...
    // i=0 (Top): ratio 0. Y = 0.
    // i=1 (Mid): ratio 0.5. Y = h/2.
    // i=2 (Bot): ratio 1.0. Y = h.

    int index = 0;
    for (int i = 0; i <= 2; i++) {
      double y;
      if (i == 0) {
        y = ShapeConstants.BUS_ANCHOR_VERTICAL_INSET;
      } else if (i == 1) {
        y = h / 2.0;
      } else {
        y = h - ShapeConstants.BUS_ANCHOR_VERTICAL_INSET;
      }

      anchors.get(index++).setRelPosition(0, y); // Left
      anchors.get(index++).setRelPosition(ShapeConstants.BUS_WIDTH, y); // Right
    }
  }

  @Override
  protected boolean isSelected() {
    return isSelected;
  }

  @Override
  protected void applySelectionEffect() {
    this.cuerpoBarra.setFill(Color.RED);
    // Sombra cyan brillante para indicar selección
    this.setEffect(new DropShadow(15, Color.CYAN));
  }

  @Override
  protected void updateModelCoordinates(double x, double y) {
    model.setXbarra(x);
    model.setYbarra(y);

    // Actualizar coordenadas gráficas secundarias
    // Usamos las constantes para el centro
    double centerX = x + ShapeConstants.BUS_HALF_WIDTH;
    double centerY =
        y
            + (cuerpoBarra != null
                ? cuerpoBarra.getHeight() / 2
                : ShapeConstants.BUS_DEFAULT_HEIGHT / 2);

    model.setPuntoMedioBarra(new javafx.geometry.Point2D(centerX, centerY));
    model.setxCoorG(centerX);
    model.setyCoorG(centerY);
  }

  @Override
  protected boolean isZoomOnHoverEnabled() {
    return false;
  }
}
