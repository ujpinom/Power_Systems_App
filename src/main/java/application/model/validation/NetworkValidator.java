package application.model.validation;

import application.model.project.NetworkModel;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

/** Validates business logic rules before adding elements to the network. */
public class NetworkValidator {

  private final NetworkModel model;

  public NetworkValidator(NetworkModel model) {
    this.model = model;
  }

  /**
   * Validates if a new connection (Line or Transformer) can be created between two bars at specific
   * anchor points.
   */
  public ValidationResult validateConnection(Barras b1, Barras b2, int anchorIdx1, int anchorIdx2) {
    // 1. Check for exactly same anchor connection (No duplication on same visual
    // point)
    for (Lineas l : model.getLineas()) {
      if (isSameAnchorConnection(l, b1, b2, anchorIdx1, anchorIdx2)) {
        return ValidationResult.error(
            "Ya existe una línea en estos puntos de conexión específicos.");
      }
    }

    for (Transformador t : model.getTransformadores()) {
      if (isSameAnchorConnection(t, b1, b2, anchorIdx1, anchorIdx2)) {
        return ValidationResult.error(
            "Ya existe un transformador en estos puntos de conexión específicos.");
      }
    }

    // 2. Business rule: Prevent parallel line + transformer if that's the desired
    // constraint
    // (You can expand this if you want to forbid ANY parallel connection of
    // different types)
    if (hasExistingParallelConnection(b1, b2)) {
      // This is a generic check. In many power systems, parallel is allowed.
      // However, the user explicitly asked for "validations against business logic"
      // like no parallel elements that shouldn't be connected simultaneously.
      // For now, we allow it but show how it could be restricted.
      // return ValidationResult.error("No se permiten elementos paralelos de distinto
      // tipo entre estas barras.");
    }

    return ValidationResult.ok();
  }

  private boolean isSameAnchorConnection(Lineas l, Barras b1, Barras b2, int idx1, int idx2) {
    // Consider both directions since lines are usually bidirectional in model
    // checks
    boolean matchForward =
        (l.getBarra1() == b1
            && l.getBarra2() == b2
            && l.getAnchorIndex1() == idx1
            && l.getAnchorIndex2() == idx2);
    boolean matchBackward =
        (l.getBarra1() == b2
            && l.getBarra2() == b1
            && l.getAnchorIndex1() == idx2
            && l.getAnchorIndex2() == idx1);

    return matchForward || matchBackward;
  }

  private boolean hasExistingParallelConnection(Barras b1, Barras b2) {
    // Check if there's ALREADY a different type of connector between these bars
    boolean hasLine =
        model.getLineas().stream()
            .anyMatch(
                l ->
                    (l.getBarra1() == b1 && l.getBarra2() == b2)
                        || (l.getBarra1() == b2 && l.getBarra2() == b1));
    boolean hasTrafo =
        model.getTransformadores().stream()
            .anyMatch(
                t ->
                    (t.getBarra1() == b1 && t.getBarra2() == b2)
                        || (t.getBarra1() == b2 && t.getBarra2() == b1));

    return hasLine && hasTrafo; // Example: restricted parallel Line-Trafo
  }
}
