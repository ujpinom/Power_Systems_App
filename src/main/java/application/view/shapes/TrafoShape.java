package application.view.shapes;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import proyectoSistemasDePotencia.Transformador;

/**
 * Representación visual de un Transformador (IEC) con iconos vectoriales de devanado (Δ, Y, Yn) y
 * esquemas de puesta a tierra.
 */
public class TrafoShape extends LineShape {

  private final Circle circle1;
  private final Circle circle2;
  private final SVGPath iconPrimario;
  private final SVGPath iconSecundario;
  private final SVGPath groundPrimario;
  private final SVGPath groundSecundario;

  private static final double TRAFO_CIRCLE_RADIUS = 18.0;
  private static final double TRAFO_OFFSET = 14.0;

  // Rutas SVG Estándar (Escaladas para mejor visibilidad)
  private static final String SVG_DELTA = "M 0,14 L 7,0 L 14,14 Z";
  private static final String SVG_WYE = "M 7,7 L 7,0 M 7,7 L 0,14 M 7,7 L 14,14";
  private static final String SVG_GROUND =
      "M 0,0 L 0,10 M -6,10 L 6,10 M -4,13 L 4,13 M -2,16 L 2,16";
  private static final String SVG_REACTOR =
      "M 0,0 C 4,0 4,2 0,2 C -4,2 -4,4 0,4 C 4,4 4,6 0,6 C -4,6 -4,8 0,8";

  public enum WindingType {
    YN,
    Y,
    DELTA;

    public static WindingType fromString(String str) {
      if (str == null) return YN;
      String clean = str.toUpperCase();
      if (clean.startsWith("YN")) return YN;
      if (clean.startsWith("Y")) return Y;
      if (clean.startsWith("D") || clean.contains("DELTA")) return DELTA;
      return YN;
    }
  }

  public TrafoShape(Transformador model, NetworkShape<?> startShape, NetworkShape<?> endShape) {
    super(model, startShape, endShape);

    circle1 = createCircle(Color.RED);
    circle2 = createCircle(Color.GREEN);

    iconPrimario = createSVGIcon();
    iconSecundario = createSVGIcon();
    groundPrimario = createSVGIcon();
    groundSecundario = createSVGIcon();

    this.getChildren()
        .addAll(circle1, circle2, iconPrimario, iconSecundario, groundPrimario, groundSecundario);

    // Reactividad: Escuchar cambios de conexión y de impedancia de tierra
    model.addPropertyChangeListener(
        evt -> {
          Platform.runLater(this::updateWindingIcons);
        });

    updateWindingIcons();
    updateConnectionPoints();
  }

  private Circle createCircle(Color stroke) {
    Circle c = new Circle(TRAFO_CIRCLE_RADIUS);
    c.setStroke(stroke);
    c.setFill(Color.WHITE);
    c.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);
    return c;
  }

  private SVGPath createSVGIcon() {
    SVGPath p = new SVGPath();
    p.setStroke(Color.BLACK);
    p.setStrokeWidth(1.2);
    p.setFill(Color.TRANSPARENT);
    p.setMouseTransparent(true);
    return p;
  }

  private void updateWindingIcons() {
    Transformador t = (Transformador) getModel();

    // 1. Iconos Base
    setupWindingIcon(iconPrimario, WindingType.fromString(t.getConexionPrimaria()));
    setupWindingIcon(iconSecundario, WindingType.fromString(t.getConexionSecundaria()));

    // 2. Lógica de Tierra Primaria
    setupGroundLogic(
        groundPrimario, t.getConexionPrimaria(), t.getImpedanciaAterrizamientoPrimaria());
    // 3. Lógica de Tierra Secundaria
    setupGroundLogic(
        groundSecundario, t.getConexionSecundaria(), t.getImpedanciaAterrizamientoSecundaria());
  }

  private void setupWindingIcon(SVGPath icon, WindingType type) {
    switch (type) {
      case DELTA:
        icon.setContent(SVG_DELTA);
        break;
      case Y:
      case YN:
        icon.setContent(SVG_WYE);
        break;
    }
  }

  private void setupGroundLogic(SVGPath gIcon, String connection, double zGround) {
    WindingType type = WindingType.fromString(connection);
    if (type == WindingType.YN) {
      if (zGround == 0) {
        gIcon.setContent(SVG_GROUND);
        gIcon.setVisible(true);
      } else {
        // Reactor + Ground (Combinado en una ruta para simplificar el posicionamiento)
        gIcon.setContent(SVG_REACTOR + " " + "M 0,8" + " " + SVG_GROUND);
        gIcon.setVisible(true);
      }
    } else {
      gIcon.setVisible(false);
    }
  }

  @Override
  protected void updateConnectionPoints() {
    super.updateConnectionPoints();

    if (visualLine.getPoints().size() >= 4) {
      double totalX = 0, totalY = 0;
      int count = visualLine.getPoints().size() / 2;
      for (int i = 0; i < visualLine.getPoints().size(); i += 2) {
        totalX += visualLine.getPoints().get(i);
        totalY += visualLine.getPoints().get(i + 1);
      }

      double centerX = totalX / count;
      double centerY = totalY / count;

      // Determinar orientación (Horizontal vs Vertical)
      double dx =
          Math.abs(
              visualLine.getPoints().get(0)
                  - visualLine.getPoints().get(visualLine.getPoints().size() - 2));
      double dy =
          Math.abs(
              visualLine.getPoints().get(1)
                  - visualLine.getPoints().get(visualLine.getPoints().size() - 1));
      boolean isHorizontal = dx > dy;

      Platform.runLater(
          () -> {
            if (isHorizontal) {
              circle1.setCenterX(centerX - TRAFO_OFFSET);
              circle1.setCenterY(centerY);
              circle2.setCenterX(centerX + TRAFO_OFFSET);
              circle2.setCenterY(centerY);

              positionIcons(iconPrimario, groundPrimario, centerX - TRAFO_OFFSET, centerY);
              positionIcons(iconSecundario, groundSecundario, centerX + TRAFO_OFFSET, centerY);
            } else {
              circle1.setCenterX(centerX);
              circle1.setCenterY(centerY - TRAFO_OFFSET);
              circle2.setCenterX(centerX);
              circle2.setCenterY(centerY + TRAFO_OFFSET);

              positionIcons(iconPrimario, groundPrimario, centerX, centerY - TRAFO_OFFSET);
              positionIcons(iconSecundario, groundSecundario, centerX, centerY + TRAFO_OFFSET);
            }
          });
    }
  }

  private void positionIcons(SVGPath winding, SVGPath ground, double cx, double cy) {
    // Escalar un poco si queremos iconos más grandes, pero 1.0 suele estar bien
    // para este radio
    winding.setLayoutX(cx - 7); // Centrar el icono de 14x14 (Neutral en cx, cy)
    winding.setLayoutY(cy - 7);

    if (ground.isVisible()) {
      ground.setLayoutX(cx);
      ground.setLayoutY(cy); // Conectar directamente al centro de la Estrella
    }
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    super.internalSetSeleccionado(seleccionado);
    // Mantener colores base (Rojo/Verde) pero resaltar con sombra o ancho si se
    // desea
    if (seleccionado) {
      circle1.setStrokeWidth(ShapeConstants.LINE_HOVER_WIDTH);
      circle2.setStrokeWidth(ShapeConstants.LINE_HOVER_WIDTH);
    } else {
      circle1.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);
      circle2.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);
    }
  }
}
