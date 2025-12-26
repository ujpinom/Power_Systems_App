package proyectoSistemasDePotencia;

import java.awt.Desktop.Action;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
 
public class TapsTrafoController  {

    @FXML
    private Button btnAceptar;

    @FXML
    private TextField magTap;

    @FXML
    private ToggleGroup toggleUbicacionTap;

    @FXML
    private TextField angTap;
    
    private Transformador trafo;
    private Barras b1;
    private Barras b2;

    @FXML
    private void tapB1(ActionEvent e) {
    	
    	trafo.setBarraConTap(b1);
   
    }
    
    @FXML
    private void tapB2(ActionEvent e) {
    	
    	trafo.setBarraConTap(b2);
   
    }
    
    
    @FXML
    private void btnCerrar(ActionEvent e) {
    	Stage stage = (Stage) this.btnAceptar.getScene().getWindow();
    	
    	
   
    	
    	String angT= angTap.getText();
		String magT= magTap.getText();
		
		
		boolean ep= verificar(angT);
		boolean ite= verificar(magT);
    	
		if(ep||ite) {
			
			 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico positivos utilizando punto como separador decimal");
			
		}
		
		else {
				
			trafo.setMagTab(Double.parseDouble(magT));
			trafo.setAngtab(Double.parseDouble(angT));
			
		}
		
		stage.close();

    }


    public void setMagTap(String mag) {
    	magTap.setText(mag);
    }
    
    public void setAngTap(String ang) {
    	angTap.setText(ang);
    }

	
	public void initialize(Transformador trafo) {
		

    	this.trafo=trafo;
    	
    	b1=trafo.getBarra1();
    	b2=trafo.getBarra2();

	}
	
	public boolean verificar(String entrada) {
		
		int contador=0;
		int contador1=0;
		
		for(int i=0;i<entrada.length();i++) {
			
			char c= entrada.charAt(i);
			
			if(c=='-' && i==0) {
				++contador1;
				continue;
			}
			
			else if(c=='-' && i>0) {
				return true;
			}
			

			if(!Character.isDigit(c) && c!='.') {
				
				return true;
			}
			else if(c=='.') {
				
				++contador;
				continue;
			}
			
			else if(c=='-') {
				
			}
			
			if (contador>1 ||contador1>1) {
				
				return true;
			}
			
			contador=0;
			contador1=0;
		}
		
		return false;
		
	}
	
	
}
