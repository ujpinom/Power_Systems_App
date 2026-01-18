package application.model.project;

import java.util.Stack;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.CompensadorEstatico;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

/** Gestiona el historial de acciones (Undo/Redo) del modelo de red. */
public class NetworkHistoryManager {

  public enum ActionType {
    ADD,
    REMOVE
  }

  public static class HistoryAction {
    final Object element;
    final ActionType type;

    HistoryAction(Object element, ActionType type) {
      this.element = element;
      this.type = type;
    }
  }

  private final Stack<HistoryAction> creationHistory = new Stack<>();
  private final Stack<HistoryAction> redoHistory = new Stack<>();
  private boolean isProcessingInternalAction = false;

  private final NetworkModel model;

  public NetworkHistoryManager(NetworkModel model) {
    this.model = model;
  }

  public boolean isProcessingInternalAction() {
    return isProcessingInternalAction;
  }

  public void clearRedo() {
    if (!isProcessingInternalAction) {
      redoHistory.clear();
    }
  }

  public void registerAction(Object element, ActionType type) {
    if (!isProcessingInternalAction) {
      redoHistory.clear();
      creationHistory.push(new HistoryAction(element, type));
    }
  }

  public void undo() {
    if (!creationHistory.isEmpty()) {
      isProcessingInternalAction = true;
      try {
        HistoryAction action = creationHistory.pop();
        Object element = action.element;

        if (action.type == ActionType.ADD) {
          redoHistory.push(new HistoryAction(element, ActionType.ADD));
          performRemove(element);
        } else {
          redoHistory.push(new HistoryAction(element, ActionType.REMOVE));
          performAdd(element);
        }
      } finally {
        isProcessingInternalAction = false;
      }
    }
  }

  public void redo() {
    if (!redoHistory.isEmpty()) {
      isProcessingInternalAction = true;
      try {
        HistoryAction action = redoHistory.pop();
        Object element = action.element;

        if (action.type == ActionType.ADD) {
          creationHistory.push(new HistoryAction(element, ActionType.ADD));
          performAdd(element);
        } else {
          creationHistory.push(new HistoryAction(element, ActionType.REMOVE));
          performRemove(element);
        }
      } finally {
        isProcessingInternalAction = false;
      }
    }
  }

  public void clear() {
    creationHistory.clear();
    redoHistory.clear();
  }

  private void performAdd(Object item) {
    if (item instanceof Barras) model.addBarra((Barras) item);
    else if (item instanceof Transformador) model.addTransformador((Transformador) item);
    else if (item instanceof Lineas) model.addLinea((Lineas) item);
    else if (item instanceof Generadores) model.addGenerador((Generadores) item);
    else if (item instanceof Carga) model.addCarga((Carga) item);
    else if (item instanceof Bancos) model.addBanco((Bancos) item);
    else if (item instanceof CompensadorEstatico) model.addCompensador((CompensadorEstatico) item);
  }

  private void performRemove(Object item) {
    if (item instanceof Barras) model.removeBarra((Barras) item);
    else if (item instanceof Transformador) model.removeTransformador((Transformador) item);
    else if (item instanceof Lineas) model.removeLinea((Lineas) item);
    else if (item instanceof Generadores) model.removeGenerador((Generadores) item);
    else if (item instanceof Carga) model.removeCarga((Carga) item);
    else if (item instanceof Bancos) model.removeBanco((Bancos) item);
    else if (item instanceof CompensadorEstatico)
      model.removeCompensador((CompensadorEstatico) item);
  }
}
