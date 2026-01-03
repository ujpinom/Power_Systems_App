package application.view.shapes;

import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class NetworkShape<T> extends Group {

    protected final T model;
    protected Label label;

    // Configuración de animación
    private final ScaleTransition hoverAnimation;

    // Variables para Arrastre (Drag)
    private double anchorX, anchorY;
    private double initialLayoutX, initialLayoutY;
    private boolean isDragging = false;

    public NetworkShape(T model) {
        this.model = model;
        this.setCursor(Cursor.HAND);

        // Inicializar animación de Hover (Zoom)
        hoverAnimation = new ScaleTransition(Duration.millis(200), this);
        initHoverEffects();

    }

    /**
     * Crea y posiciona la etiqueta estandarizada para el componente.
     * 
     * @param text Texto inicial.
     * @param x    Posición X relativa al centro del grupo.
     * @param y    Posición Y relativa al centro del grupo.
     */
    protected void createLabel(String text, double x, double y) {
        this.label = new Label(text);
        this.label.setFont(new Font("Arial", 12));
        this.label.setTextFill(Color.BLACK);
        this.label.setLayoutX(x);
        this.label.setLayoutY(y);

        // Opcional: Fondo semitransparente para la etiqueta para mejorar lectura
        this.label.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 3; -fx-padding: 0 2 0 2;");

        this.getChildren().add(label);
    }

    public void updateLabelText(String newText) {
        if (this.label != null) {
            this.label.setText(newText);
        }
    }

    private void initHoverEffects() {
        // Al entrar el mouse
        this.setOnMouseEntered(e -> {
            if (!isDragging) { // No animar si se está arrastrando
                if (isZoomOnHoverEnabled()) {
                    hoverAnimation.setToX(1.2);
                    hoverAnimation.setToY(1.2);
                    hoverAnimation.playFromStart();
                }
                this.toFront();
                this.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.3)));
                onHoverEntered();
            }
        });

        // Al salir el mouse
        this.setOnMouseExited(e -> {
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
     * Hooks para que las subclases añadan comportamiento extra al pasar el mouse
     * sin sobreescribir la lógica base (toFront, efectos, etc).
     */
    protected void onHoverEntered() {
    }

    protected void onHoverExited() {
    }

    /**
     * Define si el componente debe escalarse (Zoom) al pasar el mouse.
     * Las subclases pueden sobrescribirlo para desactivar este comportamiento.
     */
    protected boolean isZoomOnHoverEnabled() {
        return true;
    }

    protected void enableDrag() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
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

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.isPrimaryButtonDown() && isDragging) {
                double deltaX = e.getSceneX() - anchorX;
                double deltaY = e.getSceneY() - anchorY;

                double newX = initialLayoutX + deltaX;
                double newY = initialLayoutY + deltaY;

                // Aplicar SNAP TO GRID (Cuadrícula de 10px para mayor precisión)
                newX = Math.round(newX / 10) * 10;
                newY = Math.round(newY / 10) * 10;

                this.setLayoutX(newX);
                this.setLayoutY(newY);

                // Actualizar modelo
                updateModelCoordinates(newX, newY);
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            isDragging = false;
            // Restaurar efectos si es necesario
            if (isSelected())
                applySelectionEffect();
            else
                this.setEffect(null);
        });
    }

    protected abstract boolean isSelected();

    protected abstract void applySelectionEffect();

    /**
     * Sincroniza las coordenadas visuales con el objeto lógico (Barra, Linea, etc).
     */
    protected abstract void updateModelCoordinates(double x, double y);

    /**
     * Método abstracto para establecer el estado de selección.
     * Las subclases definen cómo se ven cuando se seleccionan.
     */
    public abstract void setSeleccionado(boolean seleccionado);

    public T getModel() {
        return model;
    }

    public boolean isDragging() {
        return isDragging;
    }
}
