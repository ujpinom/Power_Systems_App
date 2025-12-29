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
    
    // Aquí irían métodos para eliminar, limpiar todo, etc.
}