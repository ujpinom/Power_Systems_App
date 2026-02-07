package application.view.canvas.handlers;

import application.enums.ToolType;
import application.model.project.NetworkModel;
import application.service.logging.LogService;
import application.view.canvas.DiagramManager;
import application.view.shapes.AnchorPoint;
import application.view.shapes.NetworkShape;
import javafx.scene.input.MouseEvent;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Generadores;

/**
 * Handler para la creación de elementos de terminal único (Generadores, Cargas, etc.). A diferencia
 * del ConnectionHandler que requiere dos clics (origen y destino), este handler crea el elemento
 * con un solo clic en una barra.
 */
public class SingleTerminalHandler {
  private final NetworkModel model;
  private final DiagramManager mediator;

  public SingleTerminalHandler(NetworkModel model, DiagramManager mediator) {
    this.model = model;
    this.mediator = mediator;
  }

  /**
   * Maneja el clic en una barra cuando se está en modo de creación de elemento de terminal único.
   *
   * @param busShape Shape de la barra donde se hizo clic
   * @param event Evento del mouse
   * @param tool Herramienta activa (GENERADOR, CARGA, etc.)
   */
  public void handleBusClick(NetworkShape<?> busShape, MouseEvent event, ToolType tool) {
    Object busModel = busShape.getModel();
    if (!(busModel instanceof Barras)) {
      LogService.getInstance().warn("SingleTerminalHandler: Se esperaba una Barra.");
      return;
    }

    Barras barra = (Barras) busModel;

    // Detectar el anchor en el que se hizo clic
    int anchorIndex = detectAnchorIndex(busShape, event);

    switch (tool) {
      case GENERADOR:
        crearGenerador(barra, busShape, anchorIndex);
        break;
      case CARGA:
        crearCarga(barra, busShape, anchorIndex);
        break;
      default:
        LogService.getInstance().warn("SingleTerminalHandler: Herramienta no soportada: " + tool);
    }
  }

  /**
   * Detecta el índice del anchor point donde se hizo clic.
   *
   * @return Índice del anchor, o -1 si el clic no fue directamente en un anchor.
   */
  private int detectAnchorIndex(NetworkShape<?> busShape, MouseEvent event) {
    if (event.getTarget() instanceof javafx.scene.shape.Circle) {
      Object userData = ((javafx.scene.shape.Circle) event.getTarget()).getUserData();
      if (userData instanceof AnchorPoint) {
        AnchorPoint ap = (AnchorPoint) userData;
        int index = busShape.getAnchorIndex(ap);
        LogService.getInstance().info("Clic en Anchor " + index);
        return index;
      }
    }
    // Fallback: buscar el anchor más cercano al punto de clic
    return findClosestAnchorIndex(busShape, event.getX(), event.getY());
  }

  /**
   * Encuentra el índice del anchor más cercano al punto dado.
   *
   * @return Índice del anchor más cercano, o 0 si no hay anchors.
   */
  private int findClosestAnchorIndex(NetworkShape<?> shape, double clickX, double clickY) {
    if (shape.getAnchors().isEmpty()) return 0;

    int bestIndex = 0;
    double minDist = Double.MAX_VALUE;

    for (int i = 0; i < shape.getAnchors().size(); i++) {
      AnchorPoint ap = shape.getAnchors().get(i);
      // Calcular distancia usando coordenadas locales del anchor
      double dx = ap.sceneXProperty().get() - (shape.getLayoutX() + clickX);
      double dy = ap.sceneYProperty().get() - (shape.getLayoutY() + clickY);
      double dist = dx * dx + dy * dy;

      if (dist < minDist) {
        minDist = dist;
        bestIndex = i;
      }
    }

    LogService.getInstance().info("Anchor más cercano: " + bestIndex);
    return bestIndex;
  }

  private void crearGenerador(Barras barra, NetworkShape<?> busShape, int anchorIndex) {
    Generadores gen = new Generadores();
    gen.setBarra(barra);
    gen.setAnchorIndex1(anchorIndex);

    double coords[] = calculateInitialPosition(busShape, anchorIndex);
    gen.setXCenter(coords[0]);
    gen.setYCenter(coords[1]);

    model.addGenerador(gen);

    LogService.getInstance()
        .info(
            "Generador "
                + gen.getNombreGenerador()
                + " creado en barra "
                + barra.getNombreBarra()
                + " (Anchor "
                + anchorIndex
                + ")");

    mediator.postStatus(
        "Generador " + gen.getNombreGenerador() + " conectado a " + barra.getNombreBarra());
  }

  private void crearCarga(Barras barra, NetworkShape<?> busShape, int anchorIndex) {
    proyectoSistemasDePotencia.Carga carga = new proyectoSistemasDePotencia.Carga();
    carga.setBarra(barra);
    carga.setAnchorIndex1(anchorIndex);

    double coords[] = calculateInitialPosition(busShape, anchorIndex);
    carga.setXCenter(coords[0]);
    carga.setYCenter(coords[1]);

    model.addCarga(carga);

    LogService.getInstance()
        .info(
            "Carga "
                + carga.getNombreCarga()
                + " creada en barra "
                + barra.getNombreBarra()
                + " (Anchor "
                + anchorIndex
                + ")");

    mediator.postStatus(
        "Carga " + carga.getNombreCarga() + " conectada a " + barra.getNombreBarra());
  }

  private double[] calculateInitialPosition(NetworkShape<?> busShape, int anchorIndex) {
    double genX, genY;
    if (anchorIndex >= 0 && anchorIndex < busShape.getAnchors().size()) {
      AnchorPoint anchor = busShape.getAnchors().get(anchorIndex);
      genX = anchor.sceneXProperty().get();
      genY = anchor.sceneYProperty().get();

      double anchorLocalX = anchor.getRelX();
      if (anchorLocalX <= 0) {
        genX -= 50;
      } else {
        genX += 50;
      }
    } else {
      genX = busShape.getLayoutX() + (busShape.getLayoutX() < 100 ? 100 : -100);
      genY = busShape.getLayoutY() + 50;
    }
    return new double[] {genX, genY};
  }
}
