package application.view.shapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import proyectoSistemasDePotencia.Lineas;

/**
 * Representa un punto de quiebre (waypoint) arrastrable de una línea.
 * Hereda de NetworkShape para reutilizar la lógica de arrastre (drag).
 */
public class WaypointShape extends NetworkShape<Lineas> {

    private final Circle visualCircle;
    private final int indexX;
    private final int indexY;

    public WaypointShape(Lineas model, int indexX, int indexY, double startX, double startY) {
        super(model);
        this.indexX = indexX;
        this.indexY = indexY;

        // Visualización del handle
        this.visualCircle = new Circle(5, Color.WHITE);
        this.visualCircle.setStroke(Color.RED);
        this.visualCircle.setStrokeWidth(1);

        this.getChildren().add(visualCircle);

        // Posicionar inicialmente
        this.setLayoutX(startX);
        this.setLayoutY(startY);

        // Habilitar arrastre heredado de NetworkShape
        enableDrag();
    }

    @Override
    protected boolean isSelected() {
        // Por ahora los waypoints no tienen estado de selección propio,
        // solo aparecen cuando la línea está seleccionada.
        return false;
    }

    @Override
    protected void applySelectionEffect() {
        // No aplica efecto de selección individual
    }

    @Override
    protected void updateModelCoordinates(double x, double y) {
        // Actualizar los puntos en la lista observable del modelo de la línea
        if (model.getListPuntosPolyLine().size() > indexY) {
            model.getListPuntosPolyLine().set(indexX, x);
            model.getListPuntosPolyLine().set(indexY, y);
        }
    }

    @Override
    public void setSeleccionado(boolean seleccionado) {
        // No tiene estado de selección individual
    }
}
