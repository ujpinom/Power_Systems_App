package application.view.shapes;

import javafx.animation.ScaleTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Represents a connection point on a NetworkShape. It maintains its position relative to the owner
 * shape and calculates global scene coordinates for line connections.
 */
public class AnchorPoint {

  private final NetworkShape<?> owner;
  private final Circle visualRepresentation;

  // Position relative to the owner's local coordinate system
  private final DoubleProperty relX = new SimpleDoubleProperty();
  private final DoubleProperty relY = new SimpleDoubleProperty();

  // Position in Scene coordinates (Global) - for Lines to bind to
  private final DoubleProperty sceneX = new SimpleDoubleProperty();
  private final DoubleProperty sceneY = new SimpleDoubleProperty();

  public AnchorPoint(NetworkShape<?> owner, double x, double y) {
    this.owner = owner;
    this.relX.set(x);
    this.relY.set(y);

    // Calculate initial scene coordinates
    updateSceneCoordinates();

    // Visual representation (hidden by default)
    this.visualRepresentation = new Circle(ShapeConstants.ANCHOR_RADIUS, Color.ORANGE);
    this.visualRepresentation.setStroke(Color.BLACK);
    this.visualRepresentation.setStrokeWidth(1);
    this.visualRepresentation.layoutXProperty().bind(relX);
    this.visualRepresentation.layoutYProperty().bind(relY);
    this.visualRepresentation.setVisible(false);
    this.visualRepresentation.setUserData(this); // Store reference to this AnchorPoint

    // Listeners to update scene coordinates
    // 1. When the relative position changes (e.g. resizing the bus)
    this.relX.addListener(obs -> updateSceneCoordinates());
    this.relY.addListener(obs -> updateSceneCoordinates());

    // 2. When the owner moves or rotates in the scene
    // Note: Ideally we bind to localToSceneTransform, but that's complex.
    // For now, we listen to layout and rotate properties of the owner.
    owner.layoutXProperty().addListener(obs -> updateSceneCoordinates());
    owner.layoutYProperty().addListener(obs -> updateSceneCoordinates());
    owner.rotateProperty().addListener(obs -> updateSceneCoordinates());

    // 3. Hover effects (Zoom out/Scale up)
    ScaleTransition hoverScale = new ScaleTransition(Duration.millis(150), visualRepresentation);
    this.visualRepresentation.setOnMouseEntered(
        e -> {
          hoverScale.stop();
          hoverScale.setToX(ShapeConstants.HOVER_SCALE * 2);
          hoverScale.setToY(ShapeConstants.HOVER_SCALE * 2);
          hoverScale.play();
        });

    this.visualRepresentation.setOnMouseExited(
        e -> {
          hoverScale.stop();
          hoverScale.setToX(1.0);
          hoverScale.setToY(1.0);
          hoverScale.play();
        });
  }

  public void updateSceneCoordinates() {
    // Convert local point (relX, relY) to Scene coordinates
    Point2D localPoint = new Point2D(relX.get(), relY.get());
    Point2D scenePoint =
        owner.localToParent(localPoint); // Assuming parent is the canvas/drawing group

    // Note: localToParent considers rotation and translation of the owner
    sceneX.set(scenePoint.getX());
    sceneY.set(scenePoint.getY());
  }

  public void setRelPosition(double x, double y) {
    this.relX.set(x);
    this.relY.set(y);
  }

  public Node getVisual() {
    return visualRepresentation;
  }

  public void setVisible(boolean visible) {
    visualRepresentation.setVisible(visible);
  }

  public ReadOnlyDoubleProperty sceneXProperty() {
    return sceneX;
  }

  public ReadOnlyDoubleProperty sceneYProperty() {
    return sceneY;
  }

  public double getRelX() {
    return relX.get();
  }

  public double getRelY() {
    return relY.get();
  }
}
