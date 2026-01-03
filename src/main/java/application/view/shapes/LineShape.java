package application.view.shapes;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import proyectoSistemasDePotencia.Lineas;

public class LineShape extends NetworkShape<Lineas> {

    private final Polyline visualLine;
    private final Polyline hitBox; // Línea invisible gruesa para detectar clics

    private final NetworkShape<?> startShape;
    private final NetworkShape<?> endShape;

    // Handles para edición de waypoints (Círculos)
    private final Group waypointHandles = new Group();

    // Listener para recalcular la ruta cuando los nodos se mueven
    private final ChangeListener<Number> positionListener = (obs, oldVal, newVal) -> updateConnectionPoints();

    public LineShape(Lineas model, NetworkShape<?> startShape, NetworkShape<?> endShape) {
        super(model);
        this.startShape = startShape;
        this.endShape = endShape;

        // 1. Línea "HitBox" (Invisible, ancha, para facilitar clic)
        hitBox = new Polyline();
        hitBox.setStrokeWidth(15);
        hitBox.setStroke(Color.TRANSPARENT);

        // 2. Línea Visual (Fina, visible)
        visualLine = new Polyline();
        visualLine.setStrokeWidth(2);
        visualLine.setStroke(Color.BLACK);
        visualLine.setStrokeLineCap(StrokeLineCap.ROUND);

        // Añadir al grupo
        this.getChildren().addAll(hitBox, visualLine, waypointHandles);

        // Crear etiqueta
        createLabel(model.getNombreLinea(), 0, 0);

        // Escuchar cambios en los puntos del modelo para redibujar
        model.getListPuntosPolyLine()
                .addListener((javafx.collections.ListChangeListener<Double>) c -> updateConnectionPoints());

        // Inicializar listeners en los nodos conectados
        initConnectionListeners();

        // Calcular posición inicial
        updateConnectionPoints();
    }

    private void initConnectionListeners() {
        // Escuchar cambios en X e Y de ambos nodos
        startShape.layoutXProperty().addListener(positionListener);
        startShape.layoutYProperty().addListener(positionListener);
        endShape.layoutXProperty().addListener(positionListener);
        endShape.layoutYProperty().addListener(positionListener);
    }

    /**
     * Recalcula los puntos de la línea.
     * Aquí es donde podemos implementar lógica de Ruteo Ortogonal (Codos) en el
     * futuro.
     * Por ahora, haremos una conexión directa inteligente (Centro a Centro).
     */
    private void updateConnectionPoints() {
        // Coordenadas de inicio y fin
        double startX = startShape.getLayoutX() + 3;
        double startY = startShape.getLayoutY() + 30;
        double endX = endShape.getLayoutX() + 3;
        double endY = endShape.getLayoutY() + 30;

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

        if (this.label != null) {
            this.label.setLayoutX(avgX);
            this.label.setLayoutY(avgY - 15);
        }
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
        visualLine.setStrokeWidth(4);
    }

    @Override
    protected void onHoverExited() {
        visualLine.setStrokeWidth(2);
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
        visualLine.setEffect(new DropShadow(10, Color.CYAN));
    }

    @Override
    public void setSeleccionado(boolean seleccionado) {
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
        startShape.layoutXProperty().removeListener(positionListener);
        startShape.layoutYProperty().removeListener(positionListener);
        endShape.layoutXProperty().removeListener(positionListener);
        endShape.layoutYProperty().removeListener(positionListener);
    }
}
