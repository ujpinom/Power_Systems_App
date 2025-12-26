package grafos;

public class Vertices {
	
	private String nombre;
	private String alcalde;
	private int poblacion;
	public Vertices(String nombre, String alcalde, int poblacion) {
	
		this.nombre = nombre;
		this.alcalde = alcalde;
		this.poblacion = poblacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAlcalde() {
		return alcalde;
	}
	public void setAlcalde(String alcalde) {
		this.alcalde = alcalde;
	}
	public int getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(int poblacion) {
		this.poblacion = poblacion;
	}
	

	
	
	

}
