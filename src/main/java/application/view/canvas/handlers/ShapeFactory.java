package application.view.canvas.handlers;

import application.view.canvas.DiagramManager;
import application.view.shapes.BusShape;
import application.view.shapes.CargaShape;
import application.view.shapes.GenShape;
import application.view.shapes.LineShape;
import application.view.shapes.TrafoShape;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Carga;
import proyectoSistemasDePotencia.Generadores;
import proyectoSistemasDePotencia.Lineas;
import proyectoSistemasDePotencia.Transformador;

/**
 * Fábrica responsable de crear los componentes visuales (Shapes) y configurar sus interacciones
 * básicas.
 */
public class ShapeFactory {
  private final DiagramManager mediator;

  public ShapeFactory(DiagramManager mediator) {
    this.mediator = mediator;
  }

  public BusShape createBusShape(Barras barra) {
    BusShape shape = new BusShape(barra);
    shape.setOnMouseClicked(e -> mediator.handleBusClick(shape, e));
    return shape;
  }

  public LineShape createLineShape(Lineas linea, BusShape shape1, BusShape shape2) {
    LineShape lineShape = new LineShape(linea, shape1, shape2);
    lineShape.setOnMouseClicked(e -> mediator.handleNonBusClick(lineShape, e));
    lineShape.setOnReconnectRequest(mediator::startAnchorReselection);
    return lineShape;
  }

  public TrafoShape createTrafoShape(Transformador trafo, BusShape shape1, BusShape shape2) {
    TrafoShape trafoShape = new TrafoShape(trafo, shape1, shape2);
    trafoShape.setOnMouseClicked(e -> mediator.handleNonBusClick(trafoShape, e));
    trafoShape.setOnReconnectRequest(mediator::startAnchorReselection);
    return trafoShape;
  }

  public GenShape createGenShape(Generadores generador, BusShape busShape) {
    GenShape genShape = new GenShape(generador, busShape);
    genShape.setOnMouseClicked(e -> mediator.handleNonBusClick(genShape, e));
    genShape.setOnReconnectRequest(mediator::startAnchorReselection);
    return genShape;
  }

  public CargaShape createCargaShape(Carga carga, BusShape busShape) {
    CargaShape cargaShape = new CargaShape(carga, busShape);
    cargaShape.setOnMouseClicked(e -> mediator.handleNonBusClick(cargaShape, e));
    cargaShape.setOnReconnectRequest(mediator::startAnchorReselection);
    return cargaShape;
  }
}
