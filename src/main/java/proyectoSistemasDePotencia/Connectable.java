package proyectoSistemasDePotencia;

/**
 * Interfaz para elementos que se conectan a barras mediante puntos de anclaje. Permite que el
 * DiagramManager y el NetworkValidator manejen de forma genérica líneas, transformadores,
 * generadores, cargas, etc.
 */
public interface Connectable {
  // Terminal 1 (Todas las protecciones/elementos lo tienen)
  Barras getBarra1();

  void setBarra1(Barras barra);

  int getAnchorIndex1();

  void setAnchorIndex1(int index);

  // Terminal 2 (Opcional, null para elementos de terminal único como
  // Generadores/Cargas)
  Barras getBarra2();

  void setBarra2(Barras barra);

  int getAnchorIndex2();

  void setAnchorIndex2(int index);

  /** Indica si el elemento es de terminal único. */
  default boolean isSingleTerminal() {
    return getBarra2() == null;
  }
}
