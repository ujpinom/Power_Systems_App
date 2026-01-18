package application.view.shapes;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import proyectoSistemasDePotencia.Transformador;

/**
 * Representación visual de un Transformador. Extiende LineShape para reutilizar la lógica de
 * conexión y waypoints, añadiendo el símbolo característico de dos círculos entrelazados.
 */
public class TrafoShape extends LineShape {

  private final Circle circle1;
  private final Circle circle2;
  private static final double TRAFO_CIRCLE_RADIUS = 10.0;

  public TrafoShape(Transformador model, NetworkShape<?> startShape, NetworkShape<?> endShape) {
    super(model, startShape, endShape);

    // Crear los dos círculos del transformador (Estilo IEC)
    circle1 = new Circle(TRAFO_CIRCLE_RADIUS);
    circle1.setStroke(Color.BLACK);
    circle1.setFill(Color.WHITE);
    circle1.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);

    circle2 = new Circle(TRAFO_CIRCLE_RADIUS);
    circle2.setStroke(Color.BLACK);
    circle2.setFill(Color.WHITE);
    circle2.setStrokeWidth(ShapeConstants.LINE_STROKE_WIDTH);

    // Añadir a la jerarquía visual
    this.getChildren().addAll(circle1, circle2);

    // Forzar actualización inicial
    updateConnectionPoints();
  }

  @Override
  protected void updateConnectionPoints() {
    super.updateConnectionPoints();

    // Si super.updateConnectionPoints fue exitoso, colocar los círculos en el punto
    // medio
    if (visualLine.getPoints().size() >= 4) {
      // Calculamos el punto central de todos los segmentos de la línea
      double totalX = 0;
      double totalY = 0;
      int count = visualLine.getPoints().size() / 2;

      for (int i = 0; i < visualLine.getPoints().size(); i += 2) {
        totalX += visualLine.getPoints().get(i);
        totalY += visualLine.getPoints().get(i + 1);
      }

      double centerX = totalX / count;
      double centerY = totalY / count;

      // Posicionar los círculos entrelazados horizontalmente o verticalmente
      // dependiendo de la orientación general de la línea?
      // Por simplicidad ahora: horizontalmente desplazados.
      Platform.runLater(
          () -> {
            circle1.setCenterX(centerX - 5);
            circle1.setCenterY(centerY);
            circle2.setCenterX(centerX + 5);
            circle2.setCenterY(centerY);
          });
    }
  }

  @Override
  protected void internalSetSeleccionado(boolean seleccionado) {
    super.internalSetSeleccionado(seleccionado);
    if (seleccionado) {
      circle1.setStroke(Color.RED);
      circle2.setStroke(Color.GREEN);
    } else {
      circle1.setStroke(Color.RED);
      circle2.setStroke(Color.GREEN);
    }
  }
}
