package grafos;

public class Edges {
	 int u;
	 int v;
	
	public Edges(int u, int v) {
		
		this.u = u;
		this.v = v;
	}
	
	public int getU() {
	return u;
	}
	public int getV() {
		return v;
		}
	
	public void setU(int u) {
		this.u=u;
		
	}
	
	public void setV(int v) {
		this.v=v;
	}

	public boolean equals(Object o) {
		return u == ((Edges)o).u && v == ((Edges)o).v;
	
	}
}
