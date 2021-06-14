package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MatricesController {

    @FXML
    private Button acceptar;
	
	   @FXML
	    private TextField epsilon;

	    @FXML
	    private TextField aceleracion;

	    @FXML
	    private TextField iteraciones;
	
    @FXML
    private TextField holaperro;
    
    private int numeroIteaciones=50;
    private double factorAcelercion=1.6;
    private double epsilon2=0.01;
    private SPController sp;

	@FXML
	private void aceptarButton() {
		  Stage stage = (Stage) this.acceptar.getScene().getWindow();
		String tepsilon= epsilon.getText();
		String tIteraciones= iteraciones.getText();
		String tFAcele= aceleracion.getText();
		
		boolean ep= verificar(tepsilon);
		boolean ite= verificar(tIteraciones);
		boolean tace= verificar(tFAcele);
		
		if(ep||ite||tace) {
			
			 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico positivos utilizando punto como separador decimal");
			
		}
		
		else {
				
			numeroIteaciones=Integer.parseInt(tIteraciones);
			
			epsilon2=Double.parseDouble(tepsilon);
			
			factorAcelercion=Double.parseDouble(tFAcele);
	
			
			sp.setEpsilon(epsilon2);
			sp.setFACTOR_ACELERACION(factorAcelercion);
			sp.setNUMERO_ITERACIONES(numeroIteaciones);
			sp.setTextInfoPotencia();

		}
		
		stage.close();
	
	}
	
	public void setTexte(String ep) {
		
		epsilon.setText(ep);

	}
	
	public void setTextfac(String fac) {
		aceleracion.setText(fac);
	}
	
	public void setTextIter(String iter) {
		iteraciones.setText(iter);
	}



	public void initialize(SPController sp) {
		this.sp=sp;

	}


	private boolean verificar(String entrada) {
		
		int contador=0;
		
		for(int i=0;i<entrada.length();i++) {
			
			char c= entrada.charAt(i);

			if(!Character.isDigit(c)&& c!='.') {
				
				return true;
			}
			
			else if(c=='.') {
				
				++contador;
				continue;
			}
			
			if (contador>1) {
				
				return true;
			}
		}
		
		return false;
		
		
	}


}
