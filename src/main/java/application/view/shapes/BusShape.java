package application.view.shapes;

import application.model.project.NetworkModel;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import proyectoSistemasDePotencia.Barras;

public class BusShape extends StackPane {

    private final Barras barraLogica;
    private final Rectangle cuerpoBarra;
    private final Label etiquetaNombre;
    private final Paint colorBase;

    public BusShape(Barras barra) {
        this.barraLogica = barra;

        // Determinar el color base según el tipo de barra
        if (barra.isBarraCompensacion()) {
            this.colorBase = Color.BLUE;
        } else {
            this.colorBase = Color.BLACK;
        }

        this.cuerpoBarra = new Rectangle(6, 60, Color.BLACK);
        this.cuerpoBarra.setStroke(Color.TRANSPARENT);

        this.etiquetaNombre = new Label(barra.getNombreBarra());
        this.etiquetaNombre.setFont(new Font("Arial", 10));
        this.etiquetaNombre.setTranslateX(15);
        this.etiquetaNombre.setTranslateY(-35);

        this.setLayoutX(barra.getXbarra());
        this.setLayoutY(barra.getYbarra());
        this.setCursor(Cursor.HAND);
        this.getChildren().addAll(cuerpoBarra, etiquetaNombre);
        this.setUserData(barra);
        configurarEventos();
    }

    private void configurarEventos() {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemRotar = new MenuItem("Rotar 90°");
        itemRotar.setOnAction(e -> this.setRotate(this.getRotate() + 90));

        MenuItem itemRenombrar = new MenuItem("Cambiar Nombre");
        itemRenombrar.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(barraLogica.getNombreBarra());
            dialog.setTitle("Renombrar Barra");
            dialog.setHeaderText("Ingrese el nuevo ID:");
            dialog.showAndWait().ifPresent(nuevoNombre -> {
                barraLogica.setNombrePersonalizado(nuevoNombre); // Actualiza lógica
                etiquetaNombre.setText(nuevoNombre); // Actualiza visual
            });
        });

        MenuItem itemEliminar = new MenuItem("Eliminar");
        itemEliminar.setOnAction(e -> {
            NetworkModel.getInstance().removeBarra(barraLogica);
        });

        contextMenu.getItems().addAll(itemRenombrar, itemRotar, itemEliminar);

        this.setOnContextMenuRequested(e -> {
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

    // Métodos para cambiar apariencia desde fuera si es necesario
    public void setSeleccionado(boolean seleccionado) {
        if (seleccionado) {
            this.cuerpoBarra.setFill(Color.RED);
            this.cuerpoBarra.setEffect(new javafx.scene.effect.DropShadow(10, Color.CYAN)); // Brillo extra
        } else {
            // Restaurar color original
            this.cuerpoBarra.setFill(colorBase);
            this.cuerpoBarra.setEffect(null);
        }
    }

}
