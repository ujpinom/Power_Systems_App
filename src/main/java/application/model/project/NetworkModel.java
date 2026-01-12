package application.model.project;

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

  // Datos del Modelo
  private final ObservableList<Barras> barras = FXCollections.observableArrayList();
  private final ObservableList<Lineas> lineas = FXCollections.observableArrayList();
  private final ObservableList<Transformador> transformadores = FXCollections.observableArrayList();
  private final ObservableList<Generadores> generadores = FXCollections.observableArrayList();
  private final ObservableList<Carga> cargas = FXCollections.observableArrayList();
  private final ObservableList<Bancos> bancos = FXCollections.observableArrayList();
  private final ObservableList<CompensadorEstatico> compensadores =
      FXCollections.observableArrayList();

  // Delegados (Managers)
  private final NetworkEventDispatcher eventDispatcher = new NetworkEventDispatcher();
  private final NetworkHistoryManager historyManager = new NetworkHistoryManager(this);

  private NetworkModel() {
    // Inicialización del Nodo Tierra
    barras.add(new Barras("Tierra"));

    // Listeners para recomputar nombres lógicos (Bus 1, Line 1, etc.)
    setupNameRecomputation();

    // Despacho universal de eventos y registro de historial
    initUniversalDispatcher();
  }

  public static NetworkModel getInstance() {
    if (instance == null) {
      instance = new NetworkModel();
    }
    return instance;
  }

  private void setupNameRecomputation() {
    barras.addListener((ListChangeListener<Barras>) c -> recomputeLogicalNames());
    lineas.addListener((ListChangeListener<Lineas>) c -> recomputeLogicalNames());
    transformadores.addListener((ListChangeListener<Transformador>) c -> recomputeLogicalNames());
    generadores.addListener((ListChangeListener<Generadores>) c -> recomputeLogicalNames());
  }

  private void recomputeLogicalNames() {
    for (int i = 1; i < barras.size(); i++) barras.get(i).setNombreBarra("Bus " + i);
    for (int i = 0; i < lineas.size(); i++) lineas.get(i).setNombreLinea("Line " + (i + 1));
    for (int i = 0; i < transformadores.size(); i++)
      transformadores.get(i).setNombreLinea("Trafo " + (i + 1));
    for (int i = 0; i < generadores.size(); i++)
      generadores.get(i).setNombreGenerador("Gen " + (i + 1));
  }

  // --- GETTERS ---
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

  // --- GESTIÓN DE ELEMENTOS ---

  public void addBarra(Barras b) {
    if (!barras.contains(b)) barras.add(b);
  }

  public void addLinea(Lineas l) {
    if (!lineas.contains(l)) lineas.add(l);
  }

  public void addTransformador(Transformador t) {
    if (!transformadores.contains(t)) transformadores.add(t);
  }

  public void addGenerador(Generadores g) {
    if (!generadores.contains(g)) generadores.add(g);
  }

  public void addCarga(Carga c) {
    if (!cargas.contains(c)) cargas.add(c);
  }

  public void addBanco(Bancos b) {
    if (!bancos.contains(b)) bancos.add(b);
  }

  public void addCompensador(CompensadorEstatico c) {
    if (!compensadores.contains(c)) compensadores.add(c);
  }

  public void removeBarra(Barras barra) {
    if (barras.contains(barra)) {
      // Cascada de eliminación
      lineas.removeIf(l -> l.getBarra1() == barra || l.getBarra2() == barra);
      transformadores.removeIf(t -> t.getBarra1() == barra || t.getBarra2() == barra);
      generadores.removeIf(g -> g.getBarra() == barra);
      cargas.removeIf(c -> c.getBarra() == barra);
      bancos.removeIf(b -> b.getBarra() == barra);
      compensadores.removeIf(c -> c.getBarra() == barra);
      barras.remove(barra);
    }
  }

  public void removeLinea(Lineas l) {
    lineas.remove(l);
  }

  public void removeTransformador(Transformador t) {
    transformadores.remove(t);
  }

  public void removeGenerador(Generadores g) {
    generadores.remove(g);
  }

  public void removeCarga(Carga c) {
    cargas.remove(c);
  }

  public void removeBanco(Bancos b) {
    bancos.remove(b);
  }

  public void removeCompensador(CompensadorEstatico c) {
    compensadores.remove(c);
  }

  public void clearAll() {
    barras.clear();
    lineas.clear();
    transformadores.clear();
    generadores.clear();
    cargas.clear();
    bancos.clear();
    compensadores.clear();
    historyManager.clear();
    barras.add(new Barras("Tierra"));
  }

  // --- HISTORY & EVENTS BRIDGE ---

  public void undoLastAction() {
    historyManager.undo();
  }

  public void redoLastAction() {
    historyManager.redo();
  }

  public void addChangeListener(NetworkChangeListener l) {
    eventDispatcher.addChangeListener(l);
  }

  public void removeChangeListener(NetworkChangeListener l) {
    eventDispatcher.removeChangeListener(l);
  }

  private void initUniversalDispatcher() {
    setupListDispatcher(barras, true); // Especial para Barra Tierra
    setupListDispatcher(lineas, false);
    setupListDispatcher(transformadores, false);
    setupListDispatcher(generadores, false);
    setupListDispatcher(cargas, false);
    setupListDispatcher(bancos, false);
    setupListDispatcher(compensadores, false);
  }

  private <T> void setupListDispatcher(ObservableList<T> list, boolean skipTierra) {
    list.addListener(
        (ListChangeListener<T>)
            c -> {
              while (c.next()) {
                if (c.wasAdded()) {
                  for (T item : c.getAddedSubList()) {
                    boolean ignore =
                        skipTierra
                            && (item instanceof Barras)
                            && ((Barras) item).getNombreBarra().equalsIgnoreCase("Tierra");
                    if (!ignore) {
                      historyManager.registerAction(item, NetworkHistoryManager.ActionType.ADD);
                    }
                    eventDispatcher.notifyAdded(item);
                  }
                }
                if (c.wasRemoved()) {
                  for (T item : c.getRemoved()) {
                    historyManager.registerAction(item, NetworkHistoryManager.ActionType.REMOVE);
                    eventDispatcher.notifyRemoved(item);
                  }
                }
              }
            });
  }

  // --- UI STATE ---
  public ObjectProperty<Object> seleccionActualProperty() {
    return seleccionActual;
  }

  public void setSeleccionActual(Object obj) {
    seleccionActual.set(obj);
  }

  public Object getSeleccionActual() {
    return seleccionActual.get();
  }
}
