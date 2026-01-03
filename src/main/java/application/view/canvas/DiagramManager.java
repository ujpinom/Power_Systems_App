package application.view.canvas;

import application.model.project.NetworkModel;
import application.view.shapes.BusShape;
import application.view.shapes.LineShape;
import application.view.shapes.NetworkShape;
import javafx.collections.ListChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
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
    private Polyline ghostLine = null;
    private final java.util.List<Double> currentWaypoints = new java.util.ArrayList<>();

    public DiagramManager(AnchorPane canvas) {
        this.canvas = canvas;
        this.model = NetworkModel.getInstance();
        initModelListener();

        // Manejo de clic en fondo vacío
        this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == canvas) {
                System.out.println("Manager: Clic en fondo vacío");
                if (isConnecting) {
                    // Añadir un waypoint intermedio
                    addWaypoint(e.getX(), e.getY());
                } else {
                    deseleccionarTodo();
                }
            }
        });

        // Manejo de movimiento del mouse (para la línea fantasma)
        this.canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if (isConnecting && ghostLine != null) {
                // Actualizar el último punto de la polyline fantasma al mouse
                int size = ghostLine.getPoints().size();
                ghostLine.getPoints().set(size - 2, e.getX());
                ghostLine.getPoints().set(size - 1, e.getY());
            }
        });
    }

    private void initModelListener() {
        // Listener para BARRAS
        model.getBarras().addListener((ListChangeListener<Barras>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Barras b : c.getAddedSubList())
                        agregarBarraVisual(b);
                }
                if (c.wasRemoved()) {
                    for (Barras b : c.getRemoved())
                        removerBarraVisual(b);
                }
            }
        });

        // Listener para LÍNEAS
        model.getLineas().addListener((ListChangeListener<Lineas>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Lineas l : c.getAddedSubList())
                        agregarLineaVisual(l);
                }
                if (c.wasRemoved()) {
                    for (Lineas l : c.getRemoved())
                        removerLineaVisual(l);
                }
            }
        });
    }

    // --- Control de Modo ---
    private boolean connectionModeEnabled = false;

    public void setConnectionMode(boolean enabled) {
        this.connectionModeEnabled = enabled;
        if (!enabled)
            cancelConnection();
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
        this.currentWaypoints.clear();

        // Crear polyline fantasma visual
        this.ghostLine = new Polyline();
        this.ghostLine.setStroke(Color.GRAY);
        this.ghostLine.getStrokeDashArray().addAll(5d, 5d);
        this.ghostLine.setStrokeWidth(2);
        this.ghostLine.setStrokeLineCap(StrokeLineCap.ROUND);

        // CRITICAL FIX: Hacer que la línea fantasma sea transparente al mouse
        // para que no intercepte los clics dirigidos al canvas.
        this.ghostLine.setMouseTransparent(true);

        // Punto inicial fijo en el centro del origen
        double startX = source.getLayoutX() + 3;
        double startY = source.getLayoutY() + 30;

        // Añadir punto inicial y punto temporal del mouse
        this.ghostLine.getPoints().addAll(startX, startY, startX, startY);

        canvas.getChildren().add(ghostLine);
    }

    private void addWaypoint(double x, double y) {
        if (!isConnecting || ghostLine == null)
            return;

        // Aplicar Snap to Grid
        double snapX = Math.round(x / 10) * 10;
        double snapY = Math.round(y / 10) * 10;

        // Guardar en la lista de waypoints
        currentWaypoints.add(snapX);
        currentWaypoints.add(snapY);

        // Añadir a la polyline fantasma (el último punto sigue siendo el mouse)
        // Insertamos antes del último par de coordenadas
        int size = ghostLine.getPoints().size();
        ghostLine.getPoints().add(size - 2, snapX);
        ghostLine.getPoints().add(size - 1, snapY);

        System.out.println("Manager: Añadido waypoint en " + snapX + ", " + snapY);
    }

    private void completeConnection(NetworkShape<?> target) {
        if (!isConnecting || connectionSource == null)
            return;

        if (target == connectionSource) {
            System.out.println("Manager: No se puede conectar un elemento consigo mismo.");
            return;
        }

        // Aquí validamos tipos. Por ahora asumimos Barra -> Barra = Línea
        Object sourceModel = connectionSource.getModel();
        Object targetModel = target.getModel();

        if (sourceModel instanceof Barras && targetModel instanceof Barras) {
            crearLinea((Barras) sourceModel, (Barras) targetModel, connectionSource, target);
        }

        cancelConnection(); // Limpiar estado
    }

    private void crearLinea(Barras b1, Barras b2, NetworkShape<?> shape1, NetworkShape<?> shape2) {
        // 1. Crear Modelo
        Lineas nuevaLinea = new Lineas(b1, b2);
        nuevaLinea.setNombreLinea("L-" + (model.getLineas().size() + 1));

        // Transferir waypoints al modelo
        nuevaLinea.getListPuntosPolyLine().addAll(currentWaypoints);

        // Agregar al NetworkModel (El listener se encargará de crear la visual)
        model.addLinea(nuevaLinea);

        System.out.println("Manager: Conexión creada entre " + b1.getNombreBarra() + " y " + b2.getNombreBarra());
    }

    private void agregarLineaVisual(Lineas linea) {
        // Buscar las figuras de las barras correspondientes
        NetworkShape<?> shape1 = buscarShapePorModelo(linea.getBarra1());
        NetworkShape<?> shape2 = buscarShapePorModelo(linea.getBarra2());

        if (shape1 != null && shape2 != null) {
            LineShape lineShape = new LineShape(linea, shape1, shape2);

            // Evento de selección para la línea
            lineShape.setOnMouseClicked(e -> {
                e.consume();
                if (!isConnecting)
                    seleccionarShape(lineShape);
            });

            // Añadir al canvas (Al fondo, index 0, para que quede detrás de las barras)
            canvas.getChildren().add(0, lineShape);
        }
    }

    public void cancelConnection() {
        this.isConnecting = false;
        this.connectionSource = null;
        this.currentWaypoints.clear();
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
            System.out.println("Manager: Seleccionada barra -> " + ((Barras) modelData).getNombreBarra());
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
                if (node == seleccionActual)
                    seleccionActual = null;
                return true;
            }
            return false;
        });
    }

    private void removerLineaVisual(Lineas linea) {
        canvas.getChildren().removeIf(node -> {
            boolean isTarget = false;
            if (node instanceof LineShape) {
                if (((LineShape) node).getModel() == linea) {
                    isTarget = true;
                }
            }

            if (isTarget) {
                if (node == seleccionActual) {
                    seleccionActual = null;
                }
                return true;
            }
            return false;
        });
    }

    private NetworkShape<?> buscarShapePorModelo(Object modelo) {
        for (javafx.scene.Node node : canvas.getChildren()) {
            if (node instanceof NetworkShape) {
                Object m = ((NetworkShape<?>) node).getModel();
                // Importante: comparar referencias o equals
                if (m == modelo) {
                    return (NetworkShape<?>) node;
                }
            }
        }
        return null;
    }
}