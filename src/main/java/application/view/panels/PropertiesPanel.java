package application.view.panels;

import application.model.project.NetworkModel;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import proyectoSistemasDePotencia.Barras;

import application.view.panels.forms.BusForm;

public class PropertiesPanel extends VBox {

    private final NetworkModel model;
    private final VBox contentBox;

    public PropertiesPanel() {
        this.model = NetworkModel.getInstance();
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        // Asegura que el panel ocupe el espacio vertical si es necesario
        VBox.setVgrow(this, Priority.ALWAYS); 
        this.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 0 0 0 1;");

        Label title = new Label("Propiedades");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        this.contentBox = new VBox();
        this.contentBox.setSpacing(10);
        this.getChildren().addAll(title, contentBox);
    }

    public void mostrarPropiedades(Object elemento) {
        contentBox.getChildren().clear();

        if (elemento == null) {
            contentBox.getChildren().add(new Label("Seleccione un elemento..."));
            return;
        }

        if (elemento instanceof Barras) {
            contentBox.getChildren().add(new BusForm().render((Barras) elemento));
        } 
        // Aquí irían los else if para Lineas, Transformadores, etc.
    }
}