package proyectoSistemasDePotencia;

import javafx.geometry.Point2D;

public class Carga implements Connectable {

  private double potenciaActiva;
  private double potenciaReactiva;
  private Point2D puntoMedio;
  private Point2D inicio;
  private Barras barra;
  private String nombreCarga;
  static final String LEFT = "LEFT";
  static final String RIGHT = "RIGHT";
  static final String ARRIBA = "ARRIBA";
  static final String ABAJO = "ABAJO";
  private String orientacion = "LEFT";
  private int anchorIndex1 = -1;

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
    this.potenciaActiva = potenciaActiva;
  }

  public double getPotenciaReactiva() {
    return potenciaReactiva;
  }

  public void setPotenciaReactiva(double potenciaReactiva) {
    this.potenciaReactiva = potenciaReactiva;
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
    return nombreCarga;
  }

  public void setNombreCarga(String nombreCarga) {
    this.nombreCarga = nombreCarga;
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
    this.anchorIndex1 = index;
    // Carga no parece tener PropertyChangeSupport aún, añadiría si fuera necesario
    // Pero por consistencia del modelo visual bastará por ahora.
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
