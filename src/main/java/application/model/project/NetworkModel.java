package application.model.project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Lineas;

public class NetworkModel {

    private static NetworkModel instance;
    private final ObjectProperty<Object> seleccionActual = new SimpleObjectProperty<>();

    // ObservableList es clave: lanza eventos cuando se modifica la lista
    private final ObservableList<Barras> barras = FXCollections.observableArrayList();
    private final ObservableList<Lineas> lineas = FXCollections.observableArrayList();

    private NetworkModel() {
        // Inicialización privada (Singleton)
        barras.add(new Barras("Tierra"));
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
            // Esto disparará el evento 'wasRemoved' en el DiagramManager
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

    // Método opcional útil: Limpiar todo el proyecto
    public void clearAll() {
        this.barras.clear();
        // Aquí limpiarías también líneas, generadores, etc.
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