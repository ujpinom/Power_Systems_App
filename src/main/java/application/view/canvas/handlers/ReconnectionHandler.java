package application.view.canvas.handlers;

import application.model.project.NetworkModel;
import application.model.validation.ValidationResult;
import application.service.logging.LogService;
import application.view.canvas.DiagramManager;
import application.view.shapes.NetworkShape;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Connectable;

/** Maneja la lógica de reconexión (cambio de anclaje) para elementos ya existentes. */
public class ReconnectionHandler {
  private final AnchorPane canvas;
  private final NetworkModel model;
  private final DiagramManager mediator;

  private boolean isReconnecting = false;
  private Connectable reconnectModel = null;
  private Barras reconnectBarra1Model = null;
  private Barras reconnectBarra2Model = null;
  private boolean isReconnectStart = false;
  private NetworkShape<?> reconnectTargetShape = null;

  public ReconnectionHandler(AnchorPane canvas, NetworkModel model, DiagramManager mediator) {
    this.canvas = canvas;
    this.model = model;
    this.mediator = mediator;
  }

  public boolean isReconnecting() {
    return isReconnecting;
  }

  public void start(Connectable element, Boolean isStart, NetworkShape<?> targetShape) {
    this.isReconnecting = true;
    this.reconnectModel = element;
    this.isReconnectStart = isStart;

    this.reconnectBarra1Model = isStart ? element.getBarra1() : element.getBarra2();
    this.reconnectBarra2Model = !isStart ? element.getBarra1() : element.getBarra2();
    this.reconnectTargetShape = targetShape;

    if (this.reconnectTargetShape != null) {
      this.reconnectTargetShape.showAnchors(true);
      LogService.getInstance().info("Reconexión iniciada para " + (isStart ? "Inicio" : "Fin"));
      canvas.setCursor(Cursor.CROSSHAIR);
    } else {
      LogService.getInstance().error("No se encontró el shape visual para reconexión.");
      cancel();
    }
  }

  public void handleClick(NetworkShape<?> shape, MouseEvent event) {
    if (!isReconnecting || shape != reconnectTargetShape) return;

    if (event.getTarget() instanceof javafx.scene.shape.Circle) {
      Object userData = ((javafx.scene.shape.Circle) event.getTarget()).getUserData();
      if (userData instanceof application.view.shapes.AnchorPoint) {
        application.view.shapes.AnchorPoint ap = (application.view.shapes.AnchorPoint) userData;
        int newIndex = shape.getAnchorIndex(ap);

        String elementLabel = reconnectModel.toString();
        if (reconnectModel instanceof proyectoSistemasDePotencia.Lineas) {
          elementLabel =
              "Línea " + ((proyectoSistemasDePotencia.Lineas) reconnectModel).getNombreLinea();
        }

        LogService.getInstance()
            .info(
                "Punto de anclaje actualizado para "
                    + (isReconnectStart ? "Inicio" : "Fin")
                    + " de "
                    + elementLabel
                    + " -> Anchor "
                    + newIndex);

        if (isReconnectStart) {
          ValidationResult result =
              model
                  .getValidator()
                  .validateConnection(
                      reconnectBarra1Model,
                      reconnectBarra2Model,
                      newIndex,
                      reconnectModel.getAnchorIndex2());
          if (mediator.showValidationError(result)) {
            cancel();
            return;
          }
          reconnectModel.setAnchorIndex1(newIndex);
        } else {
          ValidationResult result =
              model
                  .getValidator()
                  .validateConnection(
                      reconnectBarra1Model,
                      reconnectBarra2Model,
                      reconnectModel.getAnchorIndex1(),
                      newIndex);
          if (mediator.showValidationError(result)) {
            cancel();
            return;
          }
          reconnectModel.setAnchorIndex2(newIndex);
        }

        cancel();
        return;
      }
    }

    LogService.getInstance().info("Clic en componente (selección).");
  }

  public void cancel() {
    if (reconnectTargetShape != null) {
      reconnectTargetShape.showAnchors(false);
    }
    this.isReconnecting = false;
    this.reconnectModel = null;
    this.reconnectTargetShape = null;
    canvas.setCursor(Cursor.DEFAULT);
  }
}
