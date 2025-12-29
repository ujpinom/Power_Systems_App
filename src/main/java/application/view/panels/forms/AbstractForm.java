package application.view.panels.forms;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public abstract class AbstractForm<T> {

    protected GridPane grid;

    public AbstractForm() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5));
    }

    /**
     * Builds and returns the TitledPane containing the form for the specific element.
     * @param element The model element to edit.
     * @return A TitledPane with the form fields.
     */
    public abstract TitledPane render(T element);

    protected void addField(String labelText, int row, Node field) {
        grid.add(new Label(labelText), 0, row);
        grid.add(field, 1, row);
    }
    
    protected void addFullRow(Node node, int row) {
        grid.add(node, 0, row, 2, 1);
    }

    protected double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
