package application.view.canvas.handlers;

import application.model.project.NetworkModel;
import application.view.shapes.NetworkShape;

/** Gestiona el estado de selección de componentes en el lienzo. */
public class SelectionHandler {
  private final NetworkModel model;
  private NetworkShape<?> seleccionActual = null;

  public SelectionHandler(NetworkModel model) {
    this.model = model;
  }

  /**
   * Selecciona una figura específica.
   *
   * @param shape La figura a seleccionar.
   */
  public void seleccionar(NetworkShape<?> shape) {
    if (seleccionActual != null) {
      seleccionActual.setSeleccionado(false);
    }
    seleccionActual = shape;
    if (seleccionActual != null) {
      seleccionActual.setSeleccionado(true);
      Object modelData = seleccionActual.getModel();

      // Restaurar logs detallados
      if (modelData instanceof proyectoSistemasDePotencia.Barras) {
        application.service.logging.LogService.getInstance()
            .info(
                "Seleccionada barra -> "
                    + ((proyectoSistemasDePotencia.Barras) modelData).getNombreBarra());
      } else if (modelData instanceof proyectoSistemasDePotencia.Transformador) {
        application.service.logging.LogService.getInstance()
            .info(
                "Seleccionado transformador -> "
                    + ((proyectoSistemasDePotencia.Transformador) modelData).getNombreLinea());
      } else if (modelData instanceof proyectoSistemasDePotencia.Lineas) {
        application.service.logging.LogService.getInstance()
            .info(
                "Seleccionada linea -> "
                    + ((proyectoSistemasDePotencia.Lineas) modelData).getNombreLinea());
      } else if (modelData instanceof proyectoSistemasDePotencia.Generadores) {
        application.service.logging.LogService.getInstance()
            .info(
                "Seleccionado generador -> "
                    + ((proyectoSistemasDePotencia.Generadores) modelData).getNombreGenerador());
      } else if (modelData instanceof proyectoSistemasDePotencia.Carga) {
        application.service.logging.LogService.getInstance()
            .info(
                "Seleccionada carga -> "
                    + ((proyectoSistemasDePotencia.Carga) modelData).getNombreCarga());
      }

      model.setSeleccionActual(modelData);
    } else {
      model.setSeleccionActual(null);
    }
  }

  /** Deselecciona cualquier elemento actualmente seleccionado. */
  public void deseleccionarTodo() {
    if (seleccionActual != null) {
      seleccionActual.setSeleccionado(false);
      seleccionActual = null;
      model.setSeleccionActual(null);
    }
  }

  /**
   * @return La figura seleccionada actualmente o null.
   */
  public NetworkShape<?> getSeleccionActual() {
    return seleccionActual;
  }

  /**
   * Limpia la referencia de selección interna si el objeto seleccionado coincide.
   *
   * @param shape La figura a limpiar de la referencia.
   */
  public void limpiarReferencia(NetworkShape<?> shape) {
    if (seleccionActual == shape) {
      seleccionActual = null;
    }
  }
}
