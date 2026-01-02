package application.view.canvas;

import application.model.project.NetworkModel;
import application.view.shapes.BusShape;
import application.view.shapes.LineShape;
import application.view.shapes.NetworkShape;
import javafx.collections.ListChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Lineas;

public class DiagramManager {
    private final AnchorPane canvas;
    private final NetworkModel model;
    
    // Variable para recordar quién está seleccionado (Genérico)
    private NetworkShape<?> seleccionActual = null;
    
    // --- Variables para Creación de Conexiones ---
    private boolean isConnecting = false;
    private NetworkShape<?> connectionSource = null;
    private Line ghostLine = null;

    public DiagramManager(AnchorPane canvas) {
        this.canvas = canvas;
        this.model = NetworkModel.getInstance();
        initModelListener();
        
        // Manejo de clic en fondo vacío
        this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == canvas) { 
                deseleccionarTodo();
                cancelConnection(); // Cancelar conexión si se hace clic en la nada
            }
        });

        // Manejo de movimiento del mouse (para la línea fantasma)
        this.canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if (isConnecting && ghostLine != null) {
                ghostLine.setEndX(e.getX());
                ghostLine.setEndY(e.getY());
            }
        });
    }

    private void initModelListener() {
        // Listener para BARRAS
        model.getBarras().addListener((ListChangeListener<Barras>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Barras b : c.getAddedSubList()) agregarBarraVisual(b);
                }
                if (c.wasRemoved()) {
                    for (Barras b : c.getRemoved()) removerBarraVisual(b);
                }
            }
        });
        
        // Listener para LÍNEAS (Necesario para que el modelo actualice la vista)
        // Nota: Asumimos que NetworkModel tiene una lista observable de líneas, si no, habría que añadirla.
        // Por ahora, manejaremos la creación visual directa al completar la conexión.
    }

    // --- Control de Modo ---
    private boolean connectionModeEnabled = false;

    public void setConnectionMode(boolean enabled) {
        this.connectionModeEnabled = enabled;
        if (!enabled) cancelConnection();
    }

    private void agregarBarraVisual(Barras barra) {
        BusShape shape = new BusShape(barra);
        shape.setOnMouseClicked(e -> {
            e.consume();
            // Lógica de Estado: ¿Estamos seleccionando o conectando?
            if (isConnecting) {
                completeConnection(shape);
            } else if (connectionModeEnabled) {
                startConnection(shape);
            } else {
                seleccionarShape(shape);
            }
        });
        
        canvas.getChildren().add(shape);
    }
    
    // --- Lógica de Conexión ---

    public void startConnection(NetworkShape<?> source) {
        this.isConnecting = true;
        this.connectionSource = source;
        
        // Crear línea fantasma visual
        this.ghostLine = new Line();
        this.ghostLine.setStroke(Color.GRAY);
        this.ghostLine.getStrokeDashArray().addAll(5d, 5d); // Línea punteada
        this.ghostLine.setStrokeWidth(2);
        this.ghostLine.setStrokeLineCap(StrokeLineCap.ROUND);
        
        // Punto inicial fijo en el centro del origen
        double startX = source.getLayoutX() + 3; // +3 (centro barra)
        double startY = source.getLayoutY() + 30; // +30 (centro barra)
        
        this.ghostLine.setStartX(startX);
        this.ghostLine.setStartY(startY);
        this.ghostLine.setEndX(startX); // Inicialmente longitud 0
        this.ghostLine.setEndY(startY);
        
        canvas.getChildren().add(ghostLine);
        System.out.println("Manager: Iniciando conexión desde " + source.getModel().toString());
    }

    private void completeConnection(NetworkShape<?> target) {
        if (!isConnecting || connectionSource == null) return;
        
        if (target == connectionSource) {
            System.out.println("Manager: No se puede conectar un elemento consigo mismo.");
            return;
        }
        
        // Aquí validamos tipos. Por ahora asumimos Barra -> Barra = Línea
        Object sourceModel = connectionSource.getModel();
        Object targetModel = target.getModel();
        
        if (sourceModel instanceof Barras && targetModel instanceof Barras) {
            crearLinea((Barras)sourceModel, (Barras)targetModel, connectionSource, target);
        }
        
        cancelConnection(); // Limpiar estado
    }
    
    private void crearLinea(Barras b1, Barras b2, NetworkShape<?> shape1, NetworkShape<?> shape2) {
        // 1. Crear Modelo
        Lineas nuevaLinea = new Lineas(b1, b2);
        nuevaLinea.setNombreLinea("L-" + (model.getLineas().size() + 1)); // Nombre temporal
        
        // Agregar al NetworkModel
        model.addLinea(nuevaLinea);
        
        // 2. Crear Vista
        LineShape lineShape = new LineShape(nuevaLinea, shape1, shape2);
        
        // Evento de selección para la línea
        lineShape.setOnMouseClicked(e -> {
            e.consume();
            if (!isConnecting) seleccionarShape(lineShape);
        });
        
        // Añadir al canvas (Al fondo, index 0, para que quede detrás de las barras)
        canvas.getChildren().add(0, lineShape);
        
        System.out.println("Manager: Conexión creada entre " + b1.getNombreBarra() + " y " + b2.getNombreBarra());
    }

    public void cancelConnection() {
        this.isConnecting = false;
        this.connectionSource = null;
        if (this.ghostLine != null) {
            canvas.getChildren().remove(ghostLine);
            this.ghostLine = null;
        }
    }

    // --- Fin Lógica de Conexión ---
    
    // Método genérico para seleccionar cualquier NetworkShape
    private void seleccionarShape(NetworkShape<?> shape) {
        // 1. Si había algo seleccionado antes, lo apagamos
        if (seleccionActual != null) {
            seleccionActual.setSeleccionado(false);
        }
        seleccionActual = shape;
        seleccionActual.setSeleccionado(true);
        
        // Debug
        Object modelData = shape.getModel();
        if (modelData instanceof Barras) {
             System.out.println("Manager: Seleccionada barra -> " + ((Barras)modelData).getNombreBarra());
        }
        
        model.setSeleccionActual(modelData);
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