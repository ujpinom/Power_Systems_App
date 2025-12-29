package application.model.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoSistemasDePotencia.Barras;

public class NetworkModel {
    
    private static NetworkModel instance;
    
    // ObservableList es clave: lanza eventos cuando se modifica la lista
    private final ObservableList<Barras> barras = FXCollections.observableArrayList();

    private NetworkModel() {
        // Inicialización privada (Singleton)
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

    public void addBarra(Barras barra) {
        if (!barras.contains(barra)) {
            this.barras.add(barra);
            System.out.println("Modelo: Barra agregada -> " + barra.getNombreBarra());
        }
    }

    public void removeBarra(Barras barra) {
        if (barras.contains(barra)) {
            // Esto disparará el evento 'wasRemoved' en el DiagramManager
            this.barras.remove(barra);
            System.out.println("Modelo: Barra eliminada -> " + barra.getNombreBarra());
        }
    }
    
    // Método opcional útil: Limpiar todo el proyecto
    public void clearAll() {
        this.barras.clear();
        // Aquí limpiarías también líneas, generadores, etc.
    }
}