package proyectoSistemasDePotencia;


public class MatricesComplejas implements Cloneable {
	
	
	
	static Complejo[][] sumaMatricesComplejas(Complejo[][] m1,Complejo [][] m2) {
		
		
		Complejo resultado [][] = new Complejo[m1.length][m1[0].length];
		
		if(m1.length==m2.length && m1[0].length==m2[0].length) {
			
			for(int i=0;i<m1.length;i++) {
				for (int j=0;j<m1.length;j++) {
					
					resultado[i][j]=Complejo.suma(m1[i][j], m2[i][j]);
					
				}
			}
			
			return resultado;
		}
		else {
			return null;
		}
		
	}
	
	
	
	static Complejo[][] restaMatricesComplejas(Complejo[][] m1,Complejo [][] m2){
		
		
		Complejo resultado [][] = new Complejo[m1.length][m1[0].length];
		
		if(m1.length==m2.length && m1[0].length==m2[0].length) {
			
			for(int i=0;i<m1.length;i++) {
				for (int j=0;j<m1.length;j++) {
					
					resultado[i][j]=Complejo.resta(m1[i][j], m2[i][j]);
					
				}
			}
			
			return resultado;
		}
		else {
			return null;
		}
		
	}
	
	
	static Complejo[][] multiplicacionMatricesComplejas(Complejo[][] m1,Complejo[][]m2){
		
		
		Complejo resultado[][] = new Complejo[m1.length][m2[0].length];
		Complejo sumaColumnas= new Complejo(0,0);
		
		if(m1[0].length==m2.length) {
			
			
			for(int i=0;i<m1.length;i++) {
				for(int j=0;j<m2[0].length;j++) {
					for(int a=0;a<m1[0].length;a++) {
						
						sumaColumnas=Complejo.suma(sumaColumnas,Complejo.producto(m1[i][a], m2[a][j]) );
						
					}
					
					resultado[i][j]=sumaColumnas;
					sumaColumnas=new Complejo(0,0);
					
				}
			}
			
			
			return  resultado;
		}
		
		else return null;
		
		
	}
	
	
	static Complejo[][] inversaMatrizCompleja(Complejo [][] m1) {
		
		int n= m1.length;
		Complejo [][] A= m1.clone();
		Complejo [][] B= new Complejo [m1.length][m1.length];
		Complejo [][] C= new Complejo [m1.length][m1.length];
		
		  for(int i=0; i<n; i++){
			  for(int j=0;j<n;j++) {
				  if(i==j) {
					  B[i][i]=new Complejo(1,0);
				  }
				  else {
					  B[i][j]=new Complejo(0,0);
				  }
			  }
	           
	        }
		  
			for(int k=0;k<n-1;k++) {
				 for(int i=k+1; i<n; i++) {
					 
					 for(int s=0; s<n; s++) {
						 try {
							B[i][s]=Complejo.resta(B[i][s], Complejo.cociente(Complejo.producto(A[i][k], B[k][s]), A[k][k]));
						} catch (ExcepcionDivideCero e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
								 
						
					 }
					 
					 for(int j=k+1; j<n; j++){
						 	try {
								A[i][j]=Complejo.resta(A[i][j], Complejo.cociente(Complejo.producto(A[i][k], A[k][j]), A[k][k]));
							} catch (ExcepcionDivideCero e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                    
		                }
				 }
			}
			
		      for(int s=0; s<n; s++){

		            try {
						C[n-1][s]=Complejo.cociente(B[n-1][s], A[n-1][n-1]);
					} catch (ExcepcionDivideCero e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            for(int i=n-2; i>=0; i--){
		            	
		            	try {
							C[i][s]=Complejo.cociente(B[i][s], A[i][i]);
						} catch (ExcepcionDivideCero e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                for(int k=n-1; k>i; k--){
		                	try {
								C[i][s]=Complejo.resta(C[i][s],Complejo.cociente(Complejo.producto(A[i][k], C[k][s]), A[i][i]) );
							} catch (ExcepcionDivideCero e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                }
		            }
		        }
		      
		      
//		      for(int i=0;i<C.length;i++) {
//		    	  for(int j=0;j<C.length;j++) {
//		    		  C[i][j]=Complejo.producto(new Complejo(-1,0),C[i][j] );
//		    	  }
//		      }
		      
		      return C;
	}

}
