package application.view.utils;

import application.model.validation.ValidationResult;
import javafx.scene.control.Alert;

/** Utilidades centralizadas para la interfaz de usuario. */
public class UIUtils {

  /**
   * Muestra una alerta de advertencia basada en un resultado de validación fallido.
   *
   * @param result El resultado de la validación.
   * @return true si se mostró la alerta (cuando es inválido), false de lo contrario.
   */
  public static boolean showValidationWarning(ValidationResult result) {
    if (!result.isValid()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Validación de Negocio");
      alert.setHeaderText("Operación no permitida");
      alert.setContentText(result.getMessage());
      alert.showAndWait();
      return true;
    }
    return false;
  }
}
