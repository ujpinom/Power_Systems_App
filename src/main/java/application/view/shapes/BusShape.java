package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.scene.control.ContextMenu;
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

    public BusShape(Barras barra) {
        super(barra); // Pasa el modelo al padre

        // Determinar el color base según el tipo de barra
        if (barra.isBarraCompensacion()) {
            this.colorBase = Color.BLUE;
        } else {
            this.colorBase = Color.BLACK;
        }

        // Cuerpo de la barra (6x60)
        this.cuerpoBarra = new Rectangle(0, 0, 6, 60);
        this.cuerpoBarra.setFill(colorBase);
        this.cuerpoBarra.setStroke(Color.TRANSPARENT);

        // Añadir forma principal
        this.getChildren().add(cuerpoBarra);

        // Crear etiqueta usando el método del padre
        // Posición: X=10, Y=-15
        createLabel(barra.getNombreBarra(), 10, -15);

        // Posicionar el Grupo en el Canvas
        this.setLayoutX(barra.getXbarra());
        this.setLayoutY(barra.getYbarra());
        
        this.setUserData(barra);
        
        // --- Suscripción a cambios del Modelo (Observer Pattern) ---
        model.addPropertyChangeListener(evt -> {
            if ("nombrePersonalizado".equals(evt.getPropertyName())) {
                javafx.application.Platform.runLater(() -> 
                    updateLabelText((String) evt.getNewValue())
                );
            }
        });
        
        configurarEventos();
    }

    private void configurarEventos() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemRotar = new MenuItem("Rotar 90°");
        itemRotar.setOnAction(e -> this.setRotate(this.getRotate() + 90));

        MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
        itemRenombrar.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(model.getNombreBarra());
            dialog.setTitle("Renombrar Barra");
            dialog.setHeaderText("Ingrese el nuevo ID:");
            dialog.showAndWait().ifPresent(nuevoNombre -> {
                model.setNombrePersonalizado(nuevoNombre);
                updateLabelText(nuevoNombre); // Método del padre
            });
        });

        MenuItem itemEliminar = new MenuItem("Eliminar");
        itemEliminar.setOnAction(e -> {
            NetworkModel.getInstance().removeBarra(model);
        });

        contextMenu.getItems().addAll(itemRenombrar, itemRotar, itemEliminar);

        this.setOnContextMenuRequested(e -> {
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

    @Override
    public void setSeleccionado(boolean seleccionado) {
        this.isSelected = seleccionado;
        if (seleccionado) {
            applySelectionEffect();
        } else {
            // Restaurar estado normal
            this.cuerpoBarra.setFill(colorBase);
            this.setEffect(null);
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
        
        // Actualizar coordenadas gráficas secundarias si es necesario
        // (Por ejemplo, puntos de conexión para líneas)
        model.setPuntoMedioBarra(new javafx.geometry.Point2D(x + 3, y + 30));
        model.setxCoorG(x + 3);
        model.setyCoorG(y + 30);
    }
}
