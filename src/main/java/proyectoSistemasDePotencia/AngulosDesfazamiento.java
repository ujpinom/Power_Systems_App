package proyectoSistemasDePotencia;
import java.util.*;

import grafos.Edges;
public class AngulosDesfazamiento {

	public static void establecerAngulosDeDesfazamiento(ArrayList<Barras> barras,Barras barraFallada,
											String[] conexionesBarras,List<List<Edges>> adyacencias) {
		
		for(int i=0;i<barras.size();i++) {
			
			barras.get(i).setAnguloVoltajeSecuencia1(0);
			barras.get(i).setAnguloVoltajeSecuencia2(0);
			barras.get(i).setAnguloVoltajeSecuencia0(0);
			barras.get(i).setEstablecido(false);
		}
	
		barraFallada.setAnguloVoltajeSecuencia1(0);
		barraFallada.setAnguloVoltajeSecuencia2(0);
		barraFallada.setTipoConexionBarra(conexionesBarras[barras.indexOf(barraFallada)-1]);
		barraFallada.setEstablecido(true);
		
		
		for(int i=barras.indexOf(barraFallada);i>=1;i--) {
			
			for(int j=0;j<adyacencias.get(i).size();j++) {
				
				int u=adyacencias.get(i).get(j).getU();
				int v=adyacencias.get(i).get(j).getV();
				if(!barras.get(u).isEstablecido()) {
					
					if(u!=0 && v!=0) {
					
					if(barras.get(u).getTipoConexionBarra().equals(barras.get(v).getTipoConexionBarra())) {
						barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1());
						barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2());
						barras.get(u).setEstablecido(true);
					}
					else {
						
						if(barras.get(u).getTipoConexionBarra().equals("Y")&&barras.get(v).getTipoConexionBarra().equals("D")) {
							
							barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1()+30);
							barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2()-30);
							barras.get(u).setEstablecido(true);
							
							
						}
						else if(barras.get(u).getTipoConexionBarra().equals("D")&&barras.get(v).getTipoConexionBarra().equals("Y")) {
							
							barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1()-30);
							barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2()+30);
							barras.get(u).setEstablecido(true);
							
						}
						
						
					}
					
					if((barras.get(u).getTipoConexionBarra().equals("O")&&barras.get(v).getTipoConexionBarra().equals("Y"))||
							(barras.get(u).getTipoConexionBarra().equals("O")&&barras.get(v).getTipoConexionBarra().equals("D"))
							||((barras.get(u).getTipoConexionBarra().equals("Y")&&barras.get(v).getTipoConexionBarra().equals("O"))||
							(barras.get(u).getTipoConexionBarra().equals("D")&&barras.get(v).getTipoConexionBarra().equals("O")))) {

						barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1());
						barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2());
						barras.get(u).setEstablecido(true);
						
						
					}
					
					
				}
				else if(!barras.get(v).isEstablecido()) {
					
						
						if(barras.get(v).getTipoConexionBarra().equals(barras.get(u).getTipoConexionBarra())) {
							barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1());
							barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2());
							barras.get(v).setEstablecido(true);
						}
						else {
							
							if(barras.get(v).getTipoConexionBarra().equals("Y")&&barras.get(u).getTipoConexionBarra().equals("D")) {
								
								barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1()+30);
								barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2()-30);
								barras.get(v).setEstablecido(true);
								
								
							}
							else if(barras.get(v).getTipoConexionBarra().equals("D")&&barras.get(u).getTipoConexionBarra().equals("Y")) {
								
								barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1()-30);
								barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2()+30);
								barras.get(v).setEstablecido(true);
								
							}
							
							
						}
						
						if((barras.get(v).getTipoConexionBarra().equals("O")&&barras.get(u).getTipoConexionBarra().equals("Y"))||
								(barras.get(v).getTipoConexionBarra().equals("O")&&barras.get(u).getTipoConexionBarra().equals("D"))
								||((barras.get(v).getTipoConexionBarra().equals("Y")&&barras.get(u).getTipoConexionBarra().equals("O"))||
								(barras.get(v).getTipoConexionBarra().equals("D")&&barras.get(u).getTipoConexionBarra().equals("O")))) {
							
							
							barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1());
							barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2());
							barras.get(v).setEstablecido(true);
							
						}
				}
					
			}
				
			}
			
			
		}
		
		for(int i=barras.indexOf(barraFallada);i<barras.size();i++) {
			
			for(int j=0;j<adyacencias.get(i).size();j++) {
				
				int u=adyacencias.get(i).get(j).getU();
				int v=adyacencias.get(i).get(j).getV();
				
				if(u!=0 && v!=0) {
				if(!barras.get(u).isEstablecido()) {
					
					if(barras.get(u).getTipoConexionBarra().equals(barras.get(v).getTipoConexionBarra())) {
						barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1());
						barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2());
						barras.get(u).setEstablecido(true);
					}
					else {
						
						if(barras.get(u).getTipoConexionBarra().equals("Y")&&barras.get(v).getTipoConexionBarra().equals("D")) {
							
							barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1()+30);
							barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2()-30);
							barras.get(u).setEstablecido(true);
							
							
						}
						else if(barras.get(u).getTipoConexionBarra().equals("D")&&barras.get(v).getTipoConexionBarra().equals("Y")) {
							
							barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1()-30);
							barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2()+30);
							barras.get(u).setEstablecido(true);
							
						}
						
						
					}
					
					if((barras.get(u).getTipoConexionBarra().equals("O")&&barras.get(v).getTipoConexionBarra().equals("Y"))||
							(barras.get(u).getTipoConexionBarra().equals("O")&&barras.get(v).getTipoConexionBarra().equals("D"))
							||((barras.get(u).getTipoConexionBarra().equals("Y")&&barras.get(v).getTipoConexionBarra().equals("O"))||
							(barras.get(u).getTipoConexionBarra().equals("D")&&barras.get(v).getTipoConexionBarra().equals("O")))) {
						
						barras.get(u).setAnguloVoltajeSecuencia1(barras.get(v).getAnguloVoltajeSecuencia1());
						barras.get(u).setAnguloVoltajeSecuencia2(barras.get(v).getAnguloVoltajeSecuencia2());
						barras.get(u).setEstablecido(true);
						
					}
					
					
				}
				else if(!barras.get(v).isEstablecido()) {
					
						
						if(barras.get(v).getTipoConexionBarra().equals(barras.get(u).getTipoConexionBarra())) {
							barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1());
							barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2());
							barras.get(v).setEstablecido(true);
						}
						else {
							
							if(barras.get(v).getTipoConexionBarra().equals("Y")&&barras.get(u).getTipoConexionBarra().equals("D")) {
								
								barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1()+30);
								barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2()-30);
								barras.get(v).setEstablecido(true);
								
								
							}
							else if(barras.get(v).getTipoConexionBarra().equals("D")&&barras.get(u).getTipoConexionBarra().equals("Y")) {
								
								barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1()-30);
								barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2()+30);
								barras.get(v).setEstablecido(true);
								
							}
							
							
						}
						
						if((barras.get(v).getTipoConexionBarra().equals("O")&&barras.get(u).getTipoConexionBarra().equals("Y"))||
								(barras.get(v).getTipoConexionBarra().equals("O")&&barras.get(u).getTipoConexionBarra().equals("D"))
								||((barras.get(v).getTipoConexionBarra().equals("Y")&&barras.get(u).getTipoConexionBarra().equals("O"))||
										(barras.get(v).getTipoConexionBarra().equals("D")&&barras.get(u).getTipoConexionBarra().equals("O")))) 
						{
							
							barras.get(v).setAnguloVoltajeSecuencia1(barras.get(u).getAnguloVoltajeSecuencia1());
							barras.get(v).setAnguloVoltajeSecuencia2(barras.get(u).getAnguloVoltajeSecuencia2());
							barras.get(v).setEstablecido(true);
							
						}
						
				}
				
				}
			}
		}
		
	}

}
