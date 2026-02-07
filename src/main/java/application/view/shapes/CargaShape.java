package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import proyectoSistemasDePotencia.Carga;

/**
 * Representación visual de una Carga en el diagrama. La carga se muestra como un triángulo
 * apuntando hacia afuera de la barra.
 */
public class CargaShape extends SingleTerminalShape<Carga> {

  // Dimensiones de la carga
  private static final double LOAD_SIZE = 15.0;
  private static final double LOAD_STROKE_WIDTH = 2.0;
  private static final double LOAD_CONNECTION_OFFSET = 4.0;
  private static final double FLOW_ARROW_LATERAL_OFFSET = 8.0;
  private static final double FLOW_ARROW_HEAD_LENGTH = 6.0;
  private static final double FLOW_ARROW_HEAD_HALF_WIDTH = 3.5;
  private static final double FLOW_LABEL_PERP_OFFSET = 14.0;
  private static final double FLOW_LABEL_TANGENTIAL_SHIFT = -2.0;

  // Componentes visuales
  private final Polygon simboloTriangulo;
  private final Line lineaConexion;
  private final Line flechaP;
  private final Line flechaQ;
  private final Polygon cabezaFlechaP;
  private final Polygon cabezaFlechaQ;
  private final Text textoP;
  private final Text textoQ;
  private boolean showPowerFlow = false;

  private boolean isSelected = false;

  public CargaShape(Carga carga, BusShape busShape) {
    super(carga, busShape);

    // 1. Línea de conexión
    lineaConexion = new Line();
    lineaConexion.setStroke(Color.BLACK);
    lineaConexion.setStrokeWidth(LOAD_STROKE_WIDTH);

    // 2. Símbolo de carga (Triángulo): base en el origen, punta hacia +X.
    // Esto permite orientar la punta alejándose de la barra.
    simboloTriangulo = new Polygon();
    simboloTriangulo
        .getPoints()
        .addAll(0.0, -LOAD_SIZE * 0.75, 0.0, LOAD_SIZE * 0.75, LOAD_SIZE, 0.0);
    simboloTriangulo.setFill(Color.WHITE);
    simboloTriangulo.setStroke(Color.BLACK);
    simboloTriangulo.setStrokeWidth(LOAD_STROKE_WIDTH);
    simboloTriangulo.setStrokeLineJoin(StrokeLineJoin.ROUND);

    // 3. Flechas de potencia para la carga (sentido opuesto al generador)
    flechaP = createFlowArrowShaft(Color.DODGERBLUE);
    flechaQ = createFlowArrowShaft(Color.DARKORANGE);
    cabezaFlechaP = createFlowArrowHead(Color.DODGERBLUE);
    cabezaFlechaQ = createFlowArrowHead(Color.DARKORANGE);
    textoP = createFlowLabel(Color.DODGERBLUE);
    textoQ = createFlowLabel(Color.DARKORANGE);

    // Añadir componentes al grupo
    this.getChildren()
        .addAll(
            lineaConexion,
            flechaP,
            flechaQ,
            cabezaFlechaP,
            cabezaFlechaQ,
            textoP,
            textoQ,
            simboloTriangulo);

    // Crear etiqueta
    createLabel(carga.getNombreCarga(), LOAD_SIZE + 5, -LOAD_SIZE);

    // Inicializar listeners de barra/anchor (común de terminal único)
    initializeSingleTerminalConnection();
    updatePowerText();
    applyPowerFlowVisibility();

    // Calcular posición inicial
    onAnchorOrBusMoved();

    // Suscribirse a cambios del modelo
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombreCarga".equals(prop) || "nombrePersonalizado".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreCarga()));
          } else if ("anchorIndex1".equals(prop)) {
            javafx.application.Platform.runLater(this::refreshConnectionAnchor);
          } else if ("potenciaActiva".equals(prop) || "potenciaReactiva".equals(prop)) {
            javafx.application.Platform.runLater(
                () -> {
                  updatePowerText();
                  onAnchorOrBusMoved();
                });
          }
        });
  }

  @Override
  protected void onAnchorOrBusMoved() {
    double[] anchorCoords = getAnchorCoordinates();
    double anchorX = anchorCoords[0];
    double anchorY = anchorCoords[1];

    double genX = model.getXCenter();
    double genY = model.getYCenter();

    if (genX == 0 && genY == 0) {
      double[] defaultCenter = getDefaultCenterFromAnchor(anchorX, anchorY, 50);
      genX = defaultCenter[0];
      genY = defaultCenter[1];
      model.setXCenter(genX);
      model.setYCenter(genY);
    }

    this.setLayoutX(genX);
    this.setLayoutY(genY);

    // Actualizar línea de conexión:
    // se recorta el inicio para que no atraviese el símbolo ni llegue a un vértice.
    double toAnchorX = anchorX - genX;
    double toAnchorY = anchorY - genY;
    updateConnectionLine(lineaConexion, toAnchorX, toAnchorY, LOAD_CONNECTION_OFFSET);
    if (showPowerFlow) {
      updatePowerArrows(toAnchorX, toAnchorY);
    } else {
      hidePowerArrows();
    }

    // Rotar el triángulo para que la punta quede alejándose de la barra.
    double dx = genX - anchorX;
    double dy = genY - anchorY;
    double angle = Math.toDegrees(Math.atan2(dy, dx));
    simboloTriangulo.setRotate(angle);

    updateLabelPosition(LOAD_SIZE + 5, -LOAD_SIZE);
  }

  @Override
  protected void updateModelCoordinates(double x, double y) {
    model.setXCenter(x);
    model.setYCenter(y);
    onAnchorOrBusMoved();
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

    MenuItem itemReconectar = createReconnectMenuItem("Reconectar Anclaje");

    CheckMenuItem itemMostrarFlujo = new CheckMenuItem("Mostrar Flechas P/Q");
    itemMostrarFlujo.setSelected(showPowerFlow);
    itemMostrarFlujo.setOnAction(
        e -> {
          showPowerFlow = itemMostrarFlujo.isSelected();
          applyPowerFlowVisibility();
          onAnchorOrBusMoved();
        });

    MenuItem itemEliminar = new MenuItem("Eliminar");
    itemEliminar.setOnAction(
        e -> {
          NetworkModel.getInstance().removeCarga(model);
        });

    menu.getItems().addAll(itemRenombrar, itemReconectar, itemMostrarFlujo, itemEliminar);
  }

  @Override
  protected boolean isZoomOnHoverEnabled() {
    return false;
  }

  public void dispose() {
    disposeSingleTerminalConnection();
  }

  private Line createFlowArrowShaft(Color color) {
    Line arrow = new Line();
    arrow.setStroke(color);
    arrow.setStrokeWidth(1.8);
    return arrow;
  }

  private Polygon createFlowArrowHead(Color color) {
    Polygon head = new Polygon();
    head.setFill(color);
    return head;
  }

  private Text createFlowLabel(Color color) {
    Text text = new Text();
    text.setFill(color);
    text.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
    return text;
  }

  private void updatePowerText() {
    textoP.setText(String.format("P %.1f", model.getPotenciaActiva()));
    textoQ.setText(String.format("Q %.1f", model.getPotenciaReactiva()));
  }

  private void updatePowerArrows(double toAnchorX, double toAnchorY) {
    double dist = Math.hypot(toAnchorX, toAnchorY);
    if (dist < 1e-6) {
      hidePowerArrows();
      return;
    }

    // Carga: dirección opuesta al generador (de barra hacia carga)
    double ux = -toAnchorX / dist;
    double uy = -toAnchorY / dist;
    double px = -uy;
    double py = ux;

    double usable = Math.max(10.0, dist - LOAD_CONNECTION_OFFSET - 2.0);
    double arrowLength = Math.min(18.0, usable * 0.4);
    double baseDistance = LOAD_CONNECTION_OFFSET + 2.0;

    updateArrowGeometry(
        flechaP,
        cabezaFlechaP,
        textoP,
        ux,
        uy,
        px,
        py,
        baseDistance,
        arrowLength,
        +FLOW_ARROW_LATERAL_OFFSET);
    updateArrowGeometry(
        flechaQ,
        cabezaFlechaQ,
        textoQ,
        ux,
        uy,
        px,
        py,
        baseDistance,
        arrowLength,
        -FLOW_ARROW_LATERAL_OFFSET);
  }

  private void updateArrowGeometry(
      Line shaft,
      Polygon head,
      Text label,
      double ux,
      double uy,
      double px,
      double py,
      double baseDistance,
      double arrowLength,
      double lateralOffset) {

    double startX = ux * baseDistance + px * lateralOffset;
    double startY = uy * baseDistance + py * lateralOffset;
    double endX = startX + ux * arrowLength;
    double endY = startY + uy * arrowLength;

    shaft.setStartX(startX);
    shaft.setStartY(startY);
    shaft.setEndX(endX);
    shaft.setEndY(endY);

    double backX = endX - ux * FLOW_ARROW_HEAD_LENGTH;
    double backY = endY - uy * FLOW_ARROW_HEAD_LENGTH;
    double leftX = backX + px * FLOW_ARROW_HEAD_HALF_WIDTH;
    double leftY = backY + py * FLOW_ARROW_HEAD_HALF_WIDTH;
    double rightX = backX - px * FLOW_ARROW_HEAD_HALF_WIDTH;
    double rightY = backY - py * FLOW_ARROW_HEAD_HALF_WIDTH;
    head.getPoints().setAll(endX, endY, leftX, leftY, rightX, rightY);

    double midX = startX + ux * (arrowLength * 0.5);
    double midY = startY + uy * (arrowLength * 0.5);
    double side = lateralOffset >= 0 ? 1.0 : -1.0;
    label.setLayoutX(
        midX + (px * side * FLOW_LABEL_PERP_OFFSET) + (ux * FLOW_LABEL_TANGENTIAL_SHIFT));
    label.setLayoutY(
        midY + (py * side * FLOW_LABEL_PERP_OFFSET) + (uy * FLOW_LABEL_TANGENTIAL_SHIFT));
  }

  private void hidePowerArrows() {
    flechaP.setStartX(0);
    flechaP.setStartY(0);
    flechaP.setEndX(0);
    flechaP.setEndY(0);
    flechaQ.setStartX(0);
    flechaQ.setStartY(0);
    flechaQ.setEndX(0);
    flechaQ.setEndY(0);
    cabezaFlechaP.getPoints().clear();
    cabezaFlechaQ.getPoints().clear();
  }

  private void applyPowerFlowVisibility() {
    flechaP.setVisible(showPowerFlow);
    flechaQ.setVisible(showPowerFlow);
    cabezaFlechaP.setVisible(showPowerFlow);
    cabezaFlechaQ.setVisible(showPowerFlow);
    textoP.setVisible(showPowerFlow);
    textoQ.setVisible(showPowerFlow);
  }
}
