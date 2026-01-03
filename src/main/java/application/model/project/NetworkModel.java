package application.model.project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
    private final ObservableList<CompensadorEstatico> compensadores = FXCollections.observableArrayList();

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