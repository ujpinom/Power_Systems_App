package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyectoSistemasDePotencia.CompensadorEstatico;
import javafx.scene.control.ComboBox;


public class CompensadorController implements Initializable  {

	private String horientacion="IZQUIERDA";
	private double minVar=-0.5;
	private double maxVar=0.5;
	
	private SPController sp;
    @FXML
    private TextField MaxVar;

    @FXML
    private TextField MinVar;

    @FXML
    private Button acceptar;

    @FXML
    private Label titulo;
    @FXML
    private ComboBox<String> comboBox;
    
    private CompensadorEstatico compensador;
    
    private String [] orientaciones= {"IZQUIERDA", "DERECHA", "ARRIBA","ABAJO"};
	private ObservableList<String> hor= FXCollections.observableArrayList(orientaciones);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.getItems().removeAll(comboBox.getItems());
        comboBox.getItems().addAll("IZQUIERDA", "DERECHA", "ARRIBA","ABAJO");
    
    }
 
    public void initialize(CompensadorEstatico compensador,SPController sp) {
    	
    	this.compensador=compensador;
    	this.sp=sp;
    }
    
    @FXML
	private void aceptarButton() {
		Stage stage = (Stage) this.acceptar.getScene().getWindow();
		String tepsilon= MaxVar.getText();
		String tIteraciones= MinVar.getText();
		
		
		boolean ep= verificar(tepsilon);
		boolean ite= verificar(tIteraciones);
		
		if(ep||ite) {
			
			 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico positivos utilizando punto como separador decimal");
			
		}
		
		else {
				
			minVar=Double.parseDouble(tIteraciones);
			
			maxVar=Double.parseDouble(tepsilon);
	
			compensador.setPotenciaReactivaMax(maxVar);
			compensador.setPotenciaReactivaMin(minVar);

		}
		
		stage.close();
		
		sp.repaint();
	
	}

    
    @FXML
    private void seleccionOrientacion() {
    	horientacion=orientaciones[hor.indexOf(comboBox.getValue())];
    	compensador.setOrientacion(horientacion);
    	
    }
    
 
    public String getHorientacion() {
		return horientacion;
	}

	public void setHorientacion(String horientacion) {
		this.horientacion = horientacion;
	}

	private void setOrientacion(int index) {
		compensador.setOrientacion(orientaciones[index]);
	}
    
	public void setTextMax(String maxVar) {
		MaxVar.setText(maxVar);
	}
	
	public void setTextMin(String minVar) {
		MinVar.setText(minVar);
	}
	
	public void setTitle(String title) {
		
		 titulo.setText("COMPENSADOR ESTÁTICO "+title);
		
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
