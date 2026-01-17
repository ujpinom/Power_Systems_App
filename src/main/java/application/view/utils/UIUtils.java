package application.view.utils;

import application.model.validation.ValidationResult;
import application.service.logging.LogService;
import javafx.scene.control.Alert;

/** Utilidades centralizadas para la interfaz de usuario. */
public class UIUtils {

  /**
   * Alinea un valor a la cuadrícula.
   *
   * @param value El valor original.
   * @param gridSize El tamaño de la cuadrícula.
   * @return El valor alineado.
   */
  public static double snap(double value, double gridSize) {
    return Math.round(value / gridSize) * gridSize;
  }

  /**
   * Valida si un punto está lo suficientemente lejos de las barras existentes.
   *
   * @param x Coordenada X del punto.
   * @param y Coordenada Y del punto.
   * @param barras Lista de barras existentes.
   * @return true si no hay conflicto, false de lo contrario.
   */
  public static boolean validarProximidadBarra(
      double x, double y, java.util.List<proyectoSistemasDePotencia.Barras> barras) {
    double radioMinimo = 80.0;
    for (proyectoSistemasDePotencia.Barras b : barras) {
      if ("Tierra".equalsIgnoreCase(b.getNombreBarra())) continue;
      double dist = Math.sqrt(Math.pow(x - b.getXbarra(), 2) + Math.pow(y - b.getYbarra(), 2));
      if (dist < radioMinimo) return false;
    }
    return true;
  }

  /**
   * Muestra una alerta de advertencia basada en un resultado de validación fallido.
   *
   * @param result El resultado de la validación.
   * @return true si se mostró la alerta (cuando es inválido), false de lo contrario.
   */
  public static boolean showValidationWarning(ValidationResult result) {
    if (!result.isValid()) {
      LogService.getInstance().warn("Validación fallida: " + result.getMessage());
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
