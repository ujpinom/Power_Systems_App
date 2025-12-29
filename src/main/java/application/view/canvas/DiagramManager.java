package application.view.canvas;

import application.model.project.NetworkModel;
import application.view.shapes.BusShape;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.AnchorPane;
import proyectoSistemasDePotencia.Barras;

public class DiagramManager {
    private final AnchorPane canvas;
    private final NetworkModel model;
    
    // Variable para recordar quién está seleccionado
    private BusShape seleccionActual = null;

    public DiagramManager(AnchorPane canvas) {
        this.canvas = canvas;
        this.model = NetworkModel.getInstance();
        initModelListener();
        
        // Opcional: Deseleccionar si se hace clic en el fondo vacío
        this.canvas.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == canvas) { // Solo si el clic es directo en el fondo
                deseleccionarTodo();
            }
        });
    }

    private void initModelListener() {
        model.getBarras().addListener((ListChangeListener<Barras>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Barras b : c.getAddedSubList()) {
                        agregarBarraVisual(b);
                    }
                }
                if (c.wasRemoved()) {
                    for (Barras b : c.getRemoved()) {
                        removerBarraVisual(b);
                    }
                }
            }
        });
    }

    private void agregarBarraVisual(Barras barra) {
        BusShape shape = new BusShape(barra);
        shape.setOnMouseClicked(e -> {
            e.consume();
            seleccionarShape(shape);
        });
        
        canvas.getChildren().add(shape);
    }
    
    private void seleccionarShape(BusShape shape) {
        // 1. Si había algo seleccionado antes, lo apagamos
        if (seleccionActual != null) {
            seleccionActual.setSeleccionado(false);
        }
        seleccionActual = shape;
        seleccionActual.setSeleccionado(true);
        
        System.out.println("Manager: Seleccionada barra -> " + ((Barras)shape.getUserData()).getNombreBarra());
        model.setSeleccionActual(shape.getUserData());
    }
    
    public void deseleccionarTodo() {
        if (seleccionActual != null) {
            seleccionActual.setSeleccionado(false);
            seleccionActual = null;
            model.setSeleccionActual(null);
        }
    }
    
    private void removerBarraVisual(Barras barra) {
        canvas.getChildren().removeIf(node -> {
            if (node instanceof BusShape && node.getUserData() == barra) {
                // Si borramos la barra seleccionada, limpiamos la referencia
                if (node == seleccionActual) seleccionActual = null;
                return true;
            }
            return false;
        });
    }
}