package grafos;

public class City extends Displayable{
	String nombre;
	double x;
	double y;
	
	
	public City(String nombre,double x,double y) {
		this.nombre=nombre;
		this.x=x;
		this.y=y;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
	

}
