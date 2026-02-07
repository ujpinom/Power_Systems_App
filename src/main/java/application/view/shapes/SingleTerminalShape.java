package application.view.shapes;

import java.util.function.BiConsumer;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Line;
import proyectoSistemasDePotencia.Connectable;

/**
 * Base para elementos de conexión única (Generador, Carga, etc.) enlazados a una sola barra.
 * Centraliza la resolución de anchor, listeners de movimiento y utilidades de reconexión.
 */
public abstract class SingleTerminalShape<T extends Connectable> extends NetworkShape<T> {

  protected final BusShape busShape;
  protected AnchorPoint connectedAnchor;
  private BiConsumer<Connectable, Boolean> onReconnectHandler;

  private final ChangeListener<Number> positionListener =
      (obs, oldVal, newVal) -> onAnchorOrBusMoved();

  protected SingleTerminalShape(T model, BusShape busShape) {
    super(model);
    this.busShape = busShape;
    enableDrag();
  }

  public void setOnReconnectRequest(BiConsumer<Connectable, Boolean> handler) {
    this.onReconnectHandler = handler;
  }

  protected final void initializeSingleTerminalConnection() {
    resolveAnchor();
    initPositionListeners();
  }

  protected final void refreshConnectionAnchor() {
    removePositionListeners();
    resolveAnchor();
    initPositionListeners();
    onAnchorOrBusMoved();
  }

  protected final void disposeSingleTerminalConnection() {
    removePositionListeners();
  }

  protected final MenuItem createReconnectMenuItem(String text) {
    MenuItem itemReconectar = new MenuItem(text);
    itemReconectar.setOnAction(
        e -> {
          if (onReconnectHandler != null) {
            onReconnectHandler.accept(model, true); // Terminal único: siempre anchor 1
          }
        });
    return itemReconectar;
  }

  protected final double[] getAnchorCoordinates() {
    if (connectedAnchor != null) {
      return new double[] {
        connectedAnchor.sceneXProperty().get(), connectedAnchor.sceneYProperty().get()
      };
    }
    return new double[] {
      busShape.getLayoutX() + ShapeConstants.BUS_HALF_WIDTH,
      busShape.getLayoutY() + ShapeConstants.BUS_DEFAULT_HEIGHT / 2.0
    };
  }

  protected final double[] getDefaultCenterFromAnchor(
      double anchorX, double anchorY, double offset) {
    if (connectedAnchor != null && connectedAnchor.getRelX() <= 0) {
      return new double[] {anchorX - offset, anchorY};
    }
    return new double[] {anchorX + offset, anchorY};
  }

  protected final void updateConnectionLine(
      Line line, double targetX, double targetY, double startOffset) {
    double dist = Math.hypot(targetX, targetY);
    if (dist > 0) {
      double ux = targetX / dist;
      double uy = targetY / dist;
      line.setStartX(ux * startOffset);
      line.setStartY(uy * startOffset);
    } else {
      line.setStartX(0);
      line.setStartY(0);
    }
    line.setEndX(targetX);
    line.setEndY(targetY);
  }

  protected final int getAnchorIndexOrFallbackZero() {
    if (connectedAnchor == null) return 0;
    return busShape.getAnchorIndex(connectedAnchor);
  }

  private void resolveAnchor() {
    int anchorIndex = model.getAnchorIndex1();
    if (anchorIndex >= 0 && anchorIndex < busShape.getAnchors().size()) {
      this.connectedAnchor = busShape.getAnchors().get(anchorIndex);
    } else if (!busShape.getAnchors().isEmpty()) {
      this.connectedAnchor = busShape.getAnchors().get(0);
      model.setAnchorIndex1(0);
    } else {
      this.connectedAnchor = null;
    }
  }

  private void initPositionListeners() {
    if (connectedAnchor != null) {
      connectedAnchor.sceneXProperty().addListener(positionListener);
      connectedAnchor.sceneYProperty().addListener(positionListener);
    } else {
      busShape.layoutXProperty().addListener(positionListener);
      busShape.layoutYProperty().addListener(positionListener);
    }
  }

  private void removePositionListeners() {
    if (connectedAnchor != null) {
      connectedAnchor.sceneXProperty().removeListener(positionListener);
      connectedAnchor.sceneYProperty().removeListener(positionListener);
    } else {
      busShape.layoutXProperty().removeListener(positionListener);
      busShape.layoutYProperty().removeListener(positionListener);
    }
  }

  /** Callback para que la subclase refresque su geometría cuando cambie barra/anchor. */
  protected abstract void onAnchorOrBusMoved();
}
