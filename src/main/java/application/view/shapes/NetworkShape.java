package application.view.shapes;

import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class NetworkShape<T> extends Group {

  protected final T model;
  protected Label label;

  // Variables para persistencia de posición de etiqueta
  protected double labelOffsetX = 0, labelOffsetY = 0;
  protected double labelBaseX = 0, labelBaseY = 0;

  // Configuración de animación
  private final ScaleTransition hoverAnimation;

  // Variables para Arrastre (Drag)
  private double anchorX, anchorY;
  private double initialLayoutX, initialLayoutY;
  private boolean isDragging = false;

  // Variables para Anchor Points
  protected final javafx.collections.ObservableList<AnchorPoint> anchors =
      javafx.collections.FXCollections.observableArrayList();
  protected final Group anchorVisuals = new Group();

  public NetworkShape(T model) {
    this.model = model;
    this.setCursor(Cursor.HAND);

    // Añadir grupo de visualización de anchors (siempre encima)
    this.getChildren().add(anchorVisuals);

    // Inicializar animación de Hover (Zoom)
    hoverAnimation = new ScaleTransition(Duration.millis(200), this);
    initHoverEffects();

    // Inicializar el Menú Contextual
    initContextMenu();
  }

  public javafx.collections.ObservableList<AnchorPoint> getAnchors() {
    return anchors;
  }

  public int getAnchorIndex(AnchorPoint anchor) {
    return anchors.indexOf(anchor);
  }

  protected void addAnchor(double x, double y) {
    AnchorPoint anchor = new AnchorPoint(this, x, y);
    anchors.add(anchor);
    anchorVisuals.getChildren().add(anchor.getVisual());
  }

  /**
   * Crea y posiciona la etiqueta estandarizada para el componente.
   *
   * @param text Texto inicial.
   * @param x Posición X relativa al centro del grupo.
   * @param y Posición Y relativa al centro del grupo.
   */
  protected void createLabel(String text, double x, double y) {
    this.label = new Label(text);
    this.label.setFont(new Font("Arial", 12));
    this.label.setTextFill(Color.BLACK);
    this.label.setLayoutX(x);
    this.label.setLayoutY(y);

    // Opcional: Fondo semitransparente para la etiqueta para mejorar lectura
    this.label.setStyle(
        "-fx-background-color: rgba(68, 64, 64, 0.7); -fx-background-radius: 3; -fx-padding: 0 2 0 2;");

    // Habilitar arrastre independiente para la etiqueta
    enableLabelDrag(this.label);

    this.getChildren().add(label);
  }

  /** Variables para el arrastre específico de la etiqueta */
  private double labelAnchorX, labelAnchorY;

  /**
   * Habilita el arrastre independiente para una etiqueta.
   *
   * @param lbl Etiqueta a la que se le añadirá la interactividad.
   */
  protected void enableLabelDrag(Label lbl) {
    lbl.setCursor(Cursor.MOVE);

    lbl.setOnMousePressed(
        e -> {
          labelAnchorX = e.getX();
          labelAnchorY = e.getY();
          lbl.toFront();
          e.consume(); // Evita que se dispare el drag del componente principal
        });

    lbl.setOnMouseDragged(
        e -> {
          // Calcular nueva posición relativa al grupo
          double newX = lbl.getLayoutX() + (e.getX() - labelAnchorX);
          double newY = lbl.getLayoutY() + (e.getY() - labelAnchorY);

          // Snap to Grid (5px para etiquetas permite más libertad que el de 10px)
          newX =
              Math.round(newX / ShapeConstants.LABEL_SNAP_GRID_SIZE)
                  * ShapeConstants.LABEL_SNAP_GRID_SIZE;
          newY =
              Math.round(newY / ShapeConstants.LABEL_SNAP_GRID_SIZE)
                  * ShapeConstants.LABEL_SNAP_GRID_SIZE;

          lbl.setLayoutX(newX);
          lbl.setLayoutY(newY);

          // Actualizar offsets persistentes
          this.labelOffsetX = newX - labelBaseX;
          this.labelOffsetY = newY - labelBaseY;

          e.consume();
        });
  }

  /**
   * Actualiza la posición de la etiqueta respetando los offsets manuales.
   *
   * @param baseX Posición base automática (ej: centro del componente).
   * @param baseY Posición base automática.
   */
  public void updateLabelPosition(double baseX, double baseY) {
    this.labelBaseX = baseX;
    this.labelBaseY = baseY;
    if (this.label != null) {
      this.label.setLayoutX(baseX + labelOffsetX);
      this.label.setLayoutY(baseY + labelOffsetY);
    }
  }

  public void updateLabelText(String newText) {
    if (this.label != null) {
      this.label.setText(newText);
    }
  }

  private void initHoverEffects() {
    // Al entrar el mouse
    this.setOnMouseEntered(
        e -> {
          if (!isDragging) { // No animar si se está arrastrando
            if (isZoomOnHoverEnabled()) {
              hoverAnimation.setToX(ShapeConstants.HOVER_SCALE);
              hoverAnimation.setToY(ShapeConstants.HOVER_SCALE);
              hoverAnimation.playFromStart();
            }
            this.toFront();
            this.setEffect(
                new DropShadow(ShapeConstants.SELECTION_SHADOW_RADIUS, Color.rgb(0, 0, 0, 0.3)));
            onHoverEntered();
          }
        });

    // Al salir el mouse
    this.setOnMouseExited(
        e -> {
          if (!isDragging) {
            if (isZoomOnHoverEnabled()) {
              hoverAnimation.setToX(1.0);
              hoverAnimation.setToY(1.0);
              hoverAnimation.playFromStart();
            }

            if (!isSelected()) {
              this.setEffect(null);
            } else {
              applySelectionEffect();
            }
            onHoverExited();
          }
        });
  }

  /**
   * Hooks para que las subclases añadan comportamiento extra al pasar el mouse sin sobreescribir la
   * lógica base (toFront, efectos, etc).
   */
  protected void onHoverEntered() {
    // Show anchors when hovering, even if not selected
    if (!isSelected()) {
      anchors.forEach(a -> a.setVisible(true));
    }
  }

  protected void onHoverExited() {
    // Hide anchors when mouse leaves, unless the shape is selected
    if (!isSelected()) {
      anchors.forEach(a -> a.setVisible(false));
    }
  }

  /**
   * Define si el componente debe escalarse (Zoom) al pasar el mouse. Las subclases pueden
   * sobrescribirlo para desactivar este comportamiento.
   */
  protected boolean isZoomOnHoverEnabled() {
    return true;
  }

  protected void enableDrag() {
    this.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        e -> {
          if (e.isPrimaryButtonDown()) {
            // Guardar posición inicial del mouse y del objeto
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
            initialLayoutX = getLayoutX();
            initialLayoutY = getLayoutY();
            isDragging = true;
            e.consume(); // Importante para que no propague
          }
        });

    this.addEventHandler(
        MouseEvent.MOUSE_DRAGGED,
        e -> {
          if (e.isPrimaryButtonDown() && isDragging) {
            double deltaX = e.getSceneX() - anchorX;
            double deltaY = e.getSceneY() - anchorY;

            double newX = initialLayoutX + deltaX;
            double newY = initialLayoutY + deltaY;

            // Aplicar SNAP TO GRID (Cuadrícula de 10px para mayor precisión)
            newX = Math.round(newX / ShapeConstants.SNAP_GRID_SIZE) * ShapeConstants.SNAP_GRID_SIZE;
            newY = Math.round(newY / ShapeConstants.SNAP_GRID_SIZE) * ShapeConstants.SNAP_GRID_SIZE;

            this.setLayoutX(newX);
            this.setLayoutY(newY);

            // Actualizar modelo
            updateModelCoordinates(newX, newY);

            // Forzar actualización de anchors
            anchors.forEach(AnchorPoint::updateSceneCoordinates);
          }
        });

    this.addEventHandler(
        MouseEvent.MOUSE_RELEASED,
        e -> {
          isDragging = false;
          // Restaurar efectos si es necesario
          if (isSelected()) applySelectionEffect();
          else this.setEffect(null);
        });
  }

  protected abstract boolean isSelected();

  protected abstract void applySelectionEffect();

  /** Sincroniza las coordenadas visuales con el objeto lógico (Barra, Linea, etc). */
  protected abstract void updateModelCoordinates(double x, double y);

  /** Inicializa y configura el menú contextual genérico. */
  protected void initContextMenu() {
    ContextMenu contextMenu = new ContextMenu();

    // Delegar en las subclases para llenar el menú
    fillContextMenu(contextMenu);

    // Si el menú tiene items, configurar el evento para mostrarlo
    this.setOnContextMenuRequested(
        e -> {
          if (!contextMenu.getItems().isEmpty()) {
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
          }
        });
  }

  /** Las subclases sobrescriben este método para añadir sus opciones locales al menú. */
  protected abstract void fillContextMenu(ContextMenu menu);

  /**
   * Método abstracto para establecer el estado de selección. Las subclases definen cómo se ven
   * cuando se seleccionan.
   */
  /**
   * Método plantilla para la selección. Maneja la visibilidad de los anchors y delega los efectos
   * visuales específicos a las subclases.
   */
  public void setSeleccionado(boolean seleccionado) {
    // Mostrar u ocultar anchors visualmente
    anchors.forEach(a -> a.setVisible(seleccionado));
    internalSetSeleccionado(seleccionado);
  }

  protected abstract void internalSetSeleccionado(boolean seleccionado);

  public void showAnchors(boolean show) {
    anchors.forEach(a -> a.setVisible(show));
  }

  public T getModel() {
    return model;
  }

  public boolean isDragging() {
    return isDragging;
  }

  /** Puntos de control visuales para el redimensionamiento. */
  protected class ResizeHandle extends Circle {
    private double lastX, lastY;

    public ResizeHandle(double radius) {
      super(radius, Color.WHITE);
      setStroke(Color.BLUE);
      setStrokeWidth(1);
      setCursor(Cursor.NW_RESIZE); // Valor por defecto, subclases cambian según posición
      setVisible(false);
    }

    public void setupDrag(java.util.function.BiConsumer<Double, Double> onResize) {
      this.setOnMousePressed(
          e -> {
            lastX = e.getSceneX();
            lastY = e.getSceneY();
            e.consume();
          });

      this.setOnMouseDragged(
          e -> {
            double deltaX = e.getSceneX() - lastX;
            double deltaY = e.getSceneY() - lastY;

            onResize.accept(deltaX, deltaY);

            lastX = e.getSceneX();
            lastY = e.getSceneY();
            e.consume();
          });
    }
  }
}
