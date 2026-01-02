package application.view.shapes;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
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

        // Añadir al grupo (Hitbox primero para que esté al fondo)
        this.getChildren().addAll(hitBox, visualLine);

        // Crear etiqueta
        createLabel(model.getNombreLinea(), 0, 0);

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
     * Aquí es donde podemos implementar lógica de Ruteo Ortogonal (Codos) en el futuro.
     * Por ahora, haremos una conexión directa inteligente (Centro a Centro).
     */
    private void updateConnectionPoints() {
        // Obtener coordenadas centrales de los nodos (asumiendo que el layoutX/Y es la esquina superior izquierda)
        // Nota: Para ser más precisos, deberíamos usar los límites (Bounds) del shape, 
        // pero por ahora usaremos un offset fijo basado en la Barra (que mide aprox 6x60).
        
        double startX = startShape.getLayoutX() + 3; // +3 para centrar en ancho de barra (6px)
        double startY = startShape.getLayoutY() + 30; // +30 para centrar en alto de barra (60px)
        
        double endX = endShape.getLayoutX() + 3;
        double endY = endShape.getLayoutY() + 30;

        // --- Ruteo (Actualmente: Recto / Directo) ---
        // Para hacer giros, aquí calcularíamos puntos intermedios.
        
        setPoints(startX, startY, endX, endY);
        
        // Actualizar posición de la etiqueta (Punto medio)
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;
        
        if (this.label != null) {
            this.label.setLayoutX(midX);
            this.label.setLayoutY(midY - 15); // Un poco arriba
        }
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
        } else {
            visualLine.setStroke(Color.BLACK);
            visualLine.setEffect(null);
        }
    }

    @Override
    protected void updateModelCoordinates(double x, double y) {
        // Las líneas generalmente se definen por sus nodos, no por su propia posición absoluta.
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
