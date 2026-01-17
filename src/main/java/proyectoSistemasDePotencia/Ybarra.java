package proyectoSistemasDePotencia;

public class Ybarra {

	private double [][] YBarra;
	private double [][] mAdyacente;
	

	public Ybarra(double [][] mAdyacente) {
		
		this.mAdyacente=mAdyacente;
		
		crearYBarra();
		
	}
	

	public void crearYBarra() {
		
		double resultado [][]=new  double [mAdyacente.length-1][mAdyacente.length-1];
		
		for(int j=0;j<mAdyacente.length;j++) {
			
			if( mAdyacente[0][j]!=0) {
				
				mAdyacente[j][j]=mAdyacente[0][j];
				
			}
	
		}
		
	
		for(int i=1;i<mAdyacente.length;i++) {
			
			for(int j=1;j<mAdyacente.length;j++) {
				
				resultado[i-1][j-1]=mAdyacente[i][j];
				
				if(resultado[i-1][j-1]!=0)
				resultado[i-1][j-1]=1/resultado[i-1][j-1];
			}
	
		}
		
		
		crearYBarra(resultado);

	}
	
	
	public void crearYBarra(double [][] resultado) {
		
		
		
		for(int i=0;i<resultado.length;i++) {
			for(int j=0;j<resultado.length;j++) {
				
				
				if(j!=i && resultado[i][j]!=0) {
					
					resultado[i][i]+= resultado[i][j];

				}
				
			}
			
			resultado[i][i]=(-1)*resultado[i][i];
		}
		
		
		YBarra=resultado;		
	}



	public double[][] getYBarra() {
		return YBarra;
	}



	public void setYBarra(double[][] yBarra) {
		YBarra = yBarra;
	}



	public double[][] getmAdyacente() {
		return mAdyacente;
	}



	public void setmAdyacente(double[][] mAdyacente) {
		this.mAdyacente = mAdyacente;
	}
	
	
	
	public void 	imprimir(double [][] resultado){
		
		System.out.println("YBARRA");
		
		
		for(int i=0;i<resultado.length;i++) {
			for(int j=0;j<resultado.length;j++) {
				
				System.out.print(resultado[i][j]+" ");
			}
			System.out.println();
		}
		
		
	}
	
	
}
