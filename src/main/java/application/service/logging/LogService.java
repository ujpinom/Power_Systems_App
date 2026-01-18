package application.service.logging;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Servicio centralizado para el registro de eventos, advertencias y errores. Permite que
 * componentes de la UI se suscriban para mostrar logs en tiempo real.
 */
public class LogService {

  private static LogService instance;
  private final List<Consumer<String>> listeners = new ArrayList<>();
  private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  private LogService() {}

  public static LogService getInstance() {
    if (instance == null) {
      instance = new LogService();
    }
    return instance;
  }

  /** Registra un mensaje informativo. */
  public void info(String message) {
    log(LogLevel.INFO, message);
  }

  /** Registra una advertencia. */
  public void warn(String message) {
    log(LogLevel.WARNING, message);
  }

  /** Registra un error. */
  public void error(String message) {
    log(LogLevel.ERROR, message);
  }

  /** Registra un cambio de propiedad en el modelo. */
  public void propertyChange(
      String elementLabel, String propertyName, Object oldValue, Object newValue) {
    info(
        String.format(
            "%s: Propiedad '%s' cambiada de %s a %s",
            elementLabel, propertyName, oldValue, newValue));
  }

  /** Registra un error con su excepción. */
  public void error(String message, Throwable t) {
    log(LogLevel.ERROR, message + (t != null ? ": " + t.getMessage() : ""));
    if (t != null) t.printStackTrace();
  }

  private void log(LogLevel level, String message) {
    String timestamp = LocalTime.now().format(timeFormatter);
    String formattedMessage = String.format("[%s] %s: %s", timestamp, level, message);

    // Notificar a los listeners (normalmente la consola de la UI)
    synchronized (listeners) {
      for (Consumer<String> listener : listeners) {
        listener.accept(formattedMessage);
      }
    }
  }

  /** Añade un listener para recibir las entradas del log. */
  public void addListener(Consumer<String> listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }
}
