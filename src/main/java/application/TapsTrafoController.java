package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import proyectoSistemasDePotencia.Barras;
import proyectoSistemasDePotencia.Transformador;
public class TapsTrafoController implements Initializable{
	
    @FXML
    private RadioButton seleccionB2;

    @FXML
    private RadioButton seleccionB1;

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
    
    public TapsTrafoController(Transformador trafo){
    	
    	this.trafo=trafo;
    	
    	b1=trafo.getBarra1();
    	b2=trafo.getBarra2();

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		seleccionB1.setText("Barra "+ b1.getNombreBarra());
		
		seleccionB2.setText("Barra "+ b2.getNombreBarra());
		
		
	}
    

}
