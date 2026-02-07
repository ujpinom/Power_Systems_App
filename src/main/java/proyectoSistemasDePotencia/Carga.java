package proyectoSistemasDePotencia;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.geometry.Point2D;

public class Carga implements Connectable {

  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.pcs.removePropertyChangeListener(listener);
  }

  private double potenciaActiva;
  private double potenciaReactiva;
  private Point2D puntoMedio;
  private Point2D inicio;
  private Barras barra;
  private String nombreCarga;
  private String nombrePersonalizado;
  static final String LEFT = "LEFT";
  static final String RIGHT = "RIGHT";
  static final String ARRIBA = "ARRIBA";
  static final String ABAJO = "ABAJO";
  private String orientacion = "LEFT";
  private int anchorIndex1 = -1;
  private double XCenter;
  private double YCenter;

  public double getXCenter() {
    return XCenter;
  }

  public void setXCenter(double xCenter) {
    XCenter = xCenter;
  }

  public double getYCenter() {
    return YCenter;
  }

  public void setYCenter(double yCenter) {
    YCenter = yCenter;
  }

  public String getOrientacion() {
    return orientacion;
  }

  public void setOrientacion(String orientacion) {
    this.orientacion = orientacion;
  }

  public Carga() {}

  public Carga(Point2D inicio, Barras barra, String nombreCarga) {
    super();
    this.inicio = inicio;
    this.barra = barra;
    this.nombreCarga = nombreCarga;
  }

  public double getPotenciaActiva() {
    return potenciaActiva;
  }

  public void setPotenciaActiva(double potenciaActiva) {
    double old = this.potenciaActiva;
    this.potenciaActiva = potenciaActiva;
    application.service.logging.LogService.getInstance()
        .propertyChange(getNombreCarga(), "Potencia Activa P", old, potenciaActiva);
  }

  public double getPotenciaReactiva() {
    return potenciaReactiva;
  }

  public void setPotenciaReactiva(double potenciaReactiva) {
    double old = this.potenciaReactiva;
    this.potenciaReactiva = potenciaReactiva;
    application.service.logging.LogService.getInstance()
        .propertyChange(getNombreCarga(), "Potencia Reactiva Q", old, potenciaReactiva);
  }

  public Point2D getPuntoMedio() {
    return puntoMedio;
  }

  public void setPuntoMedio(Point2D puntoMedio) {
    this.puntoMedio = puntoMedio;
  }

  public Point2D getInicio() {
    return inicio;
  }

  public void setInicio(Point2D inicio) {
    this.inicio = inicio;
  }

  public Barras getBarra() {
    return barra;
  }

  public void setBarra(Barras barra) {
    this.barra = barra;
  }

  public String getNombreCarga() {
    return nombrePersonalizado != null ? nombrePersonalizado : nombreCarga;
  }

  public void setNombreCarga(String nombreCarga) {
    String old = this.nombreCarga;
    this.nombreCarga = nombreCarga;
    this.pcs.firePropertyChange("nombreCarga", old, nombreCarga);
  }

  public String getNombreLogico() {
    return nombreCarga;
  }

  public void setNombrePersonalizado(String nombrePersonalizado) {
    String old = this.nombrePersonalizado;
    this.nombrePersonalizado = nombrePersonalizado;
    this.pcs.firePropertyChange("nombrePersonalizado", old, nombrePersonalizado);
    application.service.logging.LogService.getInstance()
        .propertyChange(getNombreCarga(), "Nombre", old, nombrePersonalizado);
  }

  public String getNombrePersonalizado() {
    return nombrePersonalizado;
  }

  // --- Implementation of Connectable ---

  @Override
  public Barras getBarra1() {
    return getBarra();
  }

  @Override
  public void setBarra1(Barras barra) {
    setBarra(barra);
  }

  @Override
  public int getAnchorIndex1() {
    return anchorIndex1;
  }

  @Override
  public void setAnchorIndex1(int index) {
    int old = this.anchorIndex1;
    this.anchorIndex1 = index;
    this.pcs.firePropertyChange("anchorIndex1", old, index);
  }

  @Override
  public Barras getBarra2() {
    return null;
  }

  @Override
  public void setBarra2(Barras barra) {
    // No aplica
  }

  @Override
  public int getAnchorIndex2() {
    return -1;
  }

  @Override
  public void setAnchorIndex2(int index) {
    // No aplica
  }
}
