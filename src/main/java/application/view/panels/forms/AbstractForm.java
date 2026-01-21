package application.view.panels.forms;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public abstract class AbstractForm<T> {

  protected GridPane grid;

  public AbstractForm() {
    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    // Configurar restricciones de columna para proteger las etiquetas
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setMinWidth(120); // Ancho mínimo para etiquetas
    col1.setPrefWidth(120);

    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.ALWAYS); // El campo de texto crece

    grid.getColumnConstraints().addAll(col1, col2);
  }

  /**
   * Builds and returns the TitledPane containing the form for the specific element.
   *
   * @param element The model element to edit.
   * @return A TitledPane with the form fields.
   */
  public abstract TitledPane render(T element);

  protected void addField(String labelText, int row, Node field) {
    grid.add(createLabel(labelText), 0, row);
    grid.add(field, 1, row);
  }

  protected Label createLabel(String text) {
    Label label = new Label(text);
    label.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
    return label;
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

  protected int parseInt(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }
}
