package application.model.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import proyectoSistemasDePotencia.Bancos;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.CompensadorEstatico;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

public class NetworkModel {

  private static NetworkModel instance;
  private final ObjectProperty<Object> seleccionActual = new SimpleObjectProperty<>();

  // ObservableList es clave: lanza eventos cuando se modifica la lista
  private final ObservableList<Barras> barras = FXCollections.observableArrayList();
  private final ObservableList<Lineas> lineas = FXCollections.observableArrayList();
  private final ObservableList<Transformador> transformadores = FXCollections.observableArrayList();
  private final ObservableList<Generadores> generadores = FXCollections.observableArrayList();
  private final ObservableList<Carga> cargas = FXCollections.observableArrayList();
  private final ObservableList<Bancos> bancos = FXCollections.observableArrayList();
  private final ObservableList<CompensadorEstatico> compensadores =
      FXCollections.observableArrayList();

  // Historial de creación para deshacer/rehacer
  private final Stack<Object> creationHistory = new Stack<>();
  private final Stack<Object> redoHistory = new Stack<>();

  // Bandera para evitar que acciones internas (Undo/Redo) limpien el stack de
  // rehacer
  private boolean isProcessingInternalAction = false;

  // Lista de observadores externos (DiagramManager, etc.)
  private final List<NetworkChangeListener> listeners = new ArrayList<>();

  private NetworkModel() {
    // Inicialización privada (Singleton)
    barras.add(new Barras("Tierra"));

    // Listeners para recomputar nombres
    barras.addListener(
        (javafx.collections.ListChangeListener.Change<? extends Barras> c) ->
            recomputeLogicalNames());
    lineas.addListener(
        (javafx.collections.ListChangeListener.Change<? extends Lineas> c) ->
            recomputeLogicalNames());
    transformadores.addListener(
        (javafx.collections.ListChangeListener.Change<? extends Transformador> c) ->
            recomputeLogicalNames());
    generadores.addListener(
        (javafx.collections.ListChangeListener.Change<? extends Generadores> c) ->
            recomputeLogicalNames());

    // Inicializar listeners para el historial de deshacer y despacho visual
    initUniversalDispatcher();
  }

  public void addChangeListener(NetworkChangeListener l) {
    if (!listeners.contains(l)) listeners.add(l);
  }

  public void removeChangeListener(NetworkChangeListener l) {
    listeners.remove(l);
  }

  private void notifyAdded(Object element) {
    for (NetworkChangeListener l : listeners) {
      l.onAdded(element);
    }
  }

  private void notifyRemoved(Object element) {
    for (NetworkChangeListener l : listeners) {
      l.onRemoved(element);
    }
  }

  private void recomputeLogicalNames() {
    // 1. Barras (Skip index 0 which is "Tierra")
    for (int i = 1; i < barras.size(); i++) {
      barras.get(i).setNombreBarra("Bus " + i);
    }

    // 2. Lineas
    for (int i = 0; i < lineas.size(); i++) {
      lineas.get(i).setNombreLinea("Line " + (i + 1));
    }

    // 3. Transformadores
    for (int i = 0; i < transformadores.size(); i++) {
      transformadores.get(i).setNombreLinea("Trafo " + (i + 1));
    }

    // 4. Generadores
    for (int i = 0; i < generadores.size(); i++) {
      generadores.get(i).setNombreGenerador("Gen " + (i + 1));
    }
  }

  public static NetworkModel getInstance() {
    if (instance == null) {
      instance = new NetworkModel();
    }
    return instance;
  }

  public ObservableList<Barras> getBarras() {
    return barras;
  }

  public ObservableList<Lineas> getLineas() {
    return lineas;
  }

  public ObservableList<Transformador> getTransformadores() {
    return transformadores;
  }

  public ObservableList<Generadores> getGeneradores() {
    return generadores;
  }

  public ObservableList<Carga> getCargas() {
    return cargas;
  }

  public ObservableList<Bancos> getBancos() {
    return bancos;
  }

  public ObservableList<CompensadorEstatico> getCompensadores() {
    return compensadores;
  }

  public void addBarra(Barras barra) {
    if (!barras.contains(barra)) {
      this.barras.add(barra);
      System.out.println("Modelo: Barra agregada -> " + barra.getNombreBarra());
    }
  }

  public void addLinea(Lineas linea) {
    if (!lineas.contains(linea)) {
      this.lineas.add(linea);
      System.out.println("Modelo: Línea agregada -> " + linea.getNombreLinea());
    }
  }

  public void removeBarra(Barras barra) {
    if (barras.contains(barra)) {
      // Cascade Delete: Remover objetos dependientes

      // 1. Lineas
      // Usamos una copia para evitar ConcurrentModificationException
      java.util.List<Lineas> lineasToRemove = new java.util.ArrayList<>();
      for (Lineas l : lineas) {
        if (l.getBarra1() == barra || l.getBarra2() == barra) {
          lineasToRemove.add(l);
        }
      }
      lineas.removeAll(lineasToRemove);

      // 2. Transformadores
      java.util.List<Transformador> trafosToRemove = new java.util.ArrayList<>();
      for (Transformador t : transformadores) {
        if (t.getBarra1() == barra || t.getBarra2() == barra) {
          trafosToRemove.add(t);
        }
      }
      transformadores.removeAll(trafosToRemove);

      // 3. Generadores
      java.util.List<Generadores> gensToRemove = new java.util.ArrayList<>();
      for (Generadores g : generadores) {
        if (g.getBarra() == barra) {
          gensToRemove.add(g);
        }
      }
      generadores.removeAll(gensToRemove);

      // 4. Cargas
      java.util.List<Carga> cargasToRemove = new java.util.ArrayList<>();
      for (Carga c : cargas) {
        if (c.getBarra() == barra) {
          cargasToRemove.add(c);
        }
      }
      cargas.removeAll(cargasToRemove);

      // 5. Bancos
      java.util.List<Bancos> bancosToRemove = new java.util.ArrayList<>();
      for (Bancos b : bancos) {
        if (b.getBarra() == barra) {
          bancosToRemove.add(b);
        }
      }
      bancos.removeAll(bancosToRemove);

      // 6. Compensadores
      java.util.List<CompensadorEstatico> compsToRemove = new java.util.ArrayList<>();
      for (CompensadorEstatico c : compensadores) {
        if (c.getBarra() == barra) {
          compsToRemove.add(c);
        }
      }
      compensadores.removeAll(compsToRemove);

      // Finalmente, remover la barra
      this.barras.remove(barra);
      System.out.println("Modelo: Barra eliminada -> " + barra.getNombreBarra());
    }
  }

  public void removeLinea(Lineas linea) {
    if (lineas.contains(linea)) {
      this.lineas.remove(linea);
      System.out.println("Modelo: Línea eliminada -> " + linea.getNombreLinea());
    }
  }

  public void addTransformador(Transformador trafo) {
    if (!transformadores.contains(trafo)) {
      this.transformadores.add(trafo);
    }
  }

  public void removeTransformador(Transformador trafo) {
    this.transformadores.remove(trafo);
  }

  public void addGenerador(Generadores generador) {
    if (!generadores.contains(generador)) {
      this.generadores.add(generador);
    }
  }

  public void removeGenerador(Generadores generador) {
    this.generadores.remove(generador);
  }

  public void addCarga(Carga carga) {
    if (!cargas.contains(carga)) {
      this.cargas.add(carga);
    }
  }

  public void removeCarga(Carga carga) {
    this.cargas.remove(carga);
  }

  public void addBanco(Bancos banco) {
    if (!bancos.contains(banco)) {
      this.bancos.add(banco);
    }
  }

  public void removeBanco(Bancos banco) {
    this.bancos.remove(banco);
  }

  public void addCompensador(CompensadorEstatico compensador) {
    if (!compensadores.contains(compensador)) {
      this.compensadores.add(compensador);
    }
  }

  public void removeCompensador(CompensadorEstatico compensador) {
    this.compensadores.remove(compensador);
  }

  // Método opcional útil: Limpiar todo el proyecto
  public void clearAll() {
    this.barras.clear();
    this.lineas.clear();
    this.transformadores.clear();
    this.generadores.clear();
    this.cargas.clear();
    this.bancos.clear();
    this.compensadores.clear();

    // Limpiar historial
    this.creationHistory.clear();
    this.redoHistory.clear();

    // Re-agregar bus tierra si es necesario para el sistema
    this.barras.add(new Barras("Tierra"));
    System.out.println("Modelo: Proyecto limpiado completamente.");
  }

  public void undoLastAction() {
    if (!creationHistory.isEmpty()) {
      isProcessingInternalAction = true;
      try {
        Object ultimo = creationHistory.pop();
        redoHistory.push(ultimo); // Guardar en rehacer

        if (ultimo instanceof Barras) {
          removeBarra((Barras) ultimo);
        } else if (ultimo instanceof Lineas) {
          removeLinea((Lineas) ultimo);
        } else if (ultimo instanceof Transformador) {
          removeTransformador((Transformador) ultimo);
        } else if (ultimo instanceof Generadores) {
          removeGenerador((Generadores) ultimo);
        } else if (ultimo instanceof Carga) {
          removeCarga((Carga) ultimo);
        } else if (ultimo instanceof Bancos) {
          removeBanco((Bancos) ultimo);
        } else if (ultimo instanceof CompensadorEstatico) {
          removeCompensador((CompensadorEstatico) ultimo);
        }
        System.out.println("Modelo: Deshacer -> Elemento movido a Redo.");
      } finally {
        isProcessingInternalAction = false;
      }
    } else {
      System.out.println("Modelo: Historial de deshacer vacío.");
    }
  }

  public void redoLastAction() {
    if (!redoHistory.isEmpty()) {
      isProcessingInternalAction = true;
      try {
        Object item = redoHistory.pop();

        if (item instanceof Barras) {
          addBarra((Barras) item);
        } else if (item instanceof Lineas) {
          addLinea((Lineas) item);
        } else if (item instanceof Transformador) {
          addTransformador((Transformador) item);
        } else if (item instanceof Generadores) {
          addGenerador((Generadores) item);
        } else if (item instanceof Carga) {
          addCarga((Carga) item);
        } else if (item instanceof Bancos) {
          addBanco((Bancos) item);
        } else if (item instanceof CompensadorEstatico) {
          addCompensador((CompensadorEstatico) item);
        }
        System.out.println("Modelo: Rehacer -> Elemento restaurado.");
      } finally {
        isProcessingInternalAction = false;
      }
    } else {
      System.out.println("Modelo: Historial de rehacer vacío.");
    }
  }

  private void initUniversalDispatcher() {
    // 1. Barras
    barras.addListener(
        (ListChangeListener<Barras>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Barras b : c.getAddedSubList()) {
                    // Historial (Skip Tierra)
                    if (!b.getNombreBarra().equalsIgnoreCase("Tierra")) {
                      if (!isProcessingInternalAction) redoHistory.clear();
                      creationHistory.push(b);
                    }
                    // Visual/Externo
                    notifyAdded(b);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Barras b : c.getRemoved()) notifyRemoved(b);
                }
              }
            });

    // 2. Líneas
    lineas.addListener(
        (ListChangeListener<Lineas>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Lineas l : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(l);
                    notifyAdded(l);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Lineas l : c.getRemoved()) notifyRemoved(l);
                }
              }
            });

    // 3. Transformadores
    transformadores.addListener(
        (ListChangeListener<Transformador>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Transformador t : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(t);
                    notifyAdded(t);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Transformador t : c.getRemoved()) notifyRemoved(t);
                }
              }
            });

    // 4. Generadores
    generadores.addListener(
        (ListChangeListener<Generadores>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Generadores g : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(g);
                    notifyAdded(g);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Generadores g : c.getRemoved()) notifyRemoved(g);
                }
              }
            });

    // 5. Cargas
    cargas.addListener(
        (ListChangeListener<Carga>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Carga car : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(car);
                    notifyAdded(car);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Carga car : c.getRemoved()) notifyRemoved(car);
                }
              }
            });

    // 6. Bancos
    bancos.addListener(
        (ListChangeListener<Bancos>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (Bancos b : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(b);
                    notifyAdded(b);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (Bancos b : c.getRemoved()) notifyRemoved(b);
                }
              }
            });

    // 7. Compensadores
    compensadores.addListener(
        (ListChangeListener<CompensadorEstatico>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (CompensadorEstatico ce : c.getAddedSubList()) {
                    if (!isProcessingInternalAction) redoHistory.clear();
                    creationHistory.push(ce);
                    notifyAdded(ce);
                  }
                }
                if (c.wasRemoved()) {
                  if (!isProcessingInternalAction) redoHistory.clear();
                  for (CompensadorEstatico ce : c.getRemoved()) notifyRemoved(ce);
                }
              }
            });
  }

  public ObjectProperty<Object> seleccionActualProperty() {
    return seleccionActual;
  }

  public void setSeleccionActual(Object obj) {
    this.seleccionActual.set(obj);
  }

  public Object getSeleccionActual() {
    return seleccionActual.get();
  }
}
