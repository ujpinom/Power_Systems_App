package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import proyectoSistemasDePotencia.Generadores;

/**
 * Representación visual de un Generador en el diagrama. El generador se muestra como un círculo con
 * una onda senoidal (~) conectado a una barra mediante una línea.
 */
public class GenShape extends SingleTerminalShape<Generadores> {

  // Dimensiones del generador
  private static final double GEN_RADIUS = 18.0;
  private static final double CONNECTION_LINE_LENGTH = 30.0;
  private static final double FLOW_ARROW_LATERAL_OFFSET = 8.0;
  private static final double FLOW_ARROW_HEAD_LENGTH = 6.0;
  private static final double FLOW_ARROW_HEAD_HALF_WIDTH = 3.5;
  private static final double FLOW_LABEL_PERP_OFFSET = 14.0;
  private static final double FLOW_LABEL_TANGENTIAL_SHIFT = -2.0;

  // Componentes visuales
  private final Circle cuerpoGenerador;
  private final SVGPath simboloOnda;
  private final Line lineaConexion;
  private final Line flechaP;
  private final Line flechaQ;
  private final Polygon cabezaFlechaP;
  private final Polygon cabezaFlechaQ;
  private final Text textoP;
  private final Text textoQ;

  private boolean isSelected = false;

  public GenShape(Generadores generador, BusShape busShape) {
    super(generador, busShape);

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

    // 4. Flechas de potencia (hacia la barra): P (activa) y Q (reactiva)
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
            cuerpoGenerador,
            simboloOnda);

    // Crear etiqueta
    createLabel(generador.getNombreGenerador(), GEN_RADIUS + 5, -GEN_RADIUS);

    // Inicializar listeners de barra/anchor (común de terminal único)
    initializeSingleTerminalConnection();
    updatePowerText();

    // Calcular posición inicial
    onAnchorOrBusMoved();

    // Suscribirse a cambios del modelo
    model.addPropertyChangeListener(
        evt -> {
          String prop = evt.getPropertyName();
          if ("nombreGenerador".equals(prop) || "nombrePersonalizado".equals(prop)) {
            javafx.application.Platform.runLater(() -> updateLabelText(model.getNombreGenerador()));
          } else if ("anchorIndex1".equals(prop)) {
            javafx.application.Platform.runLater(this::refreshConnectionAnchor);
          } else if ("MWSalida".equals(prop) || "MVarSalida".equals(prop)) {
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
      double[] defaultCenter =
          getDefaultCenterFromAnchor(anchorX, anchorY, CONNECTION_LINE_LENGTH + GEN_RADIUS);
      genX = defaultCenter[0];
      genY = defaultCenter[1];
      model.setXCenter(genX);
      model.setYCenter(genY);
    }

    this.setLayoutX(genX);
    this.setLayoutY(genY);

    double toAnchorX = anchorX - genX;
    double toAnchorY = anchorY - genY;
    double dist = Math.hypot(toAnchorX, toAnchorY);

    updateConnectionLine(lineaConexion, toAnchorX, toAnchorY, GEN_RADIUS);
    updatePowerArrows(toAnchorX, toAnchorY, dist);

    cuerpoGenerador.setCenterX(0);
    cuerpoGenerador.setCenterY(0);
    simboloOnda.setLayoutX(0);
    simboloOnda.setLayoutY(0);
    updateLabelPosition(GEN_RADIUS + 5, -GEN_RADIUS);
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

    MenuItem itemReconectar = createReconnectMenuItem("Reconectar Anclaje");

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
    textoP.setText(String.format("P %.1f", model.getMWSalida()));
    textoQ.setText(String.format("Q %.1f", model.getMVarSalida()));
  }

  private void updatePowerArrows(double toAnchorX, double toAnchorY, double dist) {
    if (dist < 1e-6) {
      hidePowerArrows();
      return;
    }

    double ux = toAnchorX / dist;
    double uy = toAnchorY / dist;
    double px = -uy;
    double py = ux;

    double usable = Math.max(10.0, dist - GEN_RADIUS - 2.0);
    double arrowLength = Math.min(20.0, usable * 0.45);
    double baseDistance = GEN_RADIUS + 3.0;

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
}
