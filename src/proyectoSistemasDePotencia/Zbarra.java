package proyectoSistemasDePotencia;

import java.util.Arrays;

public class Zbarra implements Cloneable{
	
	
	public static double [][] getZbarra(double [][] yBarra){
		
		
		int n= yBarra.length;
		double [][] A= yBarra.clone();
		double [][] B= new double [yBarra.length][yBarra.length];
		double [][] C= new double [yBarra.length][yBarra.length];
		
		
		
		  for(int i=0; i<n; i++){
	            B[i][i]=1.0;
	        }
		
		
		for(int k=0;k<n-1;k++) {
			 for(int i=k+1; i<n; i++) {
				 
				 for(int s=0; s<n; s++) {
					 B[i][s]-=A[i][k]*B[k][s]/A[k][k];
				 }
				 
				 for(int j=k+1; j<n; j++){
	                    A[i][j]-=A[i][k]*A[k][j]/A[k][k];
	                }
			 }
		}
		
	      for(int s=0; s<n; s++){
	            C[n-1][s]=B[n-1][s]/A[n-1][n-1];
	            for(int i=n-2; i>=0; i--){
	                C[i][s]=B[i][s]/A[i][i];
	                for(int k=n-1; k>i; k--){
	                    C[i][s]-=A[i][k]*C[k][s]/A[i][i];
	                }
	            }
	        }
	      
	      for(int i=0;i<C.length;i++) {
	    	  for(int j=0;j<C.length;j++) {
	    		  
	    		  C[i][j]=(-1)*C[i][j];
	    	  }
	      }
	      
	      
	      return C;
	}
	
}
