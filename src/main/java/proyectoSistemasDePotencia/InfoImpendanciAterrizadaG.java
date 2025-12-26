package proyectoSistemasDePotencia;
import javax.swing.JOptionPane;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class InfoImpendanciAterrizadaG extends GridPane{
	
	private Generadores generador;
	private Text infolinea= new Text();
	private TextField z1t= new TextField();
	private Button btncerrar= new Button("Aceptar");
	private double impedanciaAterrizada;
	
	
	
	public  InfoImpendanciAterrizadaG(Generadores generador) {
		this.setStyle("-fx-background-color: #3498DB;");
		btncerrar.setStyle("-fx-background-color: slateblue; -fx-text-fill: white ;");
		infolinea.setStroke(Color.rgb(241, 196, 15));
		
		this.setPadding(new Insets(10));
		this.setVgap(10);
		this.getRowConstraints().add(new RowConstraints(50) );
		
		infolinea.setText("INGRESE EL VALOR DE LA IMPEDANCIA DE ATERRIZAMIENTO ( "+ generador.getNombreGenerador()+" )"+": ");
		
		this.add(infolinea, 0, 0);
		this.add(z1t, 0, 1);
		this.add(btncerrar, 0, 2);
		
		z1t.setText(""+generador.getImpedanciaAterrizamiento()/3);
		
		btncerrar.setOnAction(e->{

		     Stage stage = (Stage) this.btncerrar.getScene().getWindow();
		     
		     boolean vz1t= verificarEntrada(z1t.getText());
		 
		     
		     
		     if(z1t.getText().contains(",") ||vz1t ) {
		    	 
		    	 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico utilizando punto como separador decimal");
		     }
		     else {
		     
		     impedanciaAterrizada=(Double.parseDouble(z1t.getText()));
		   
		    }
		     
		     
		     stage.close();
		});
		
		
	}// fin del costructor
	
	public double getImpedanciaAterrizada() {
		return impedanciaAterrizada;
	}
	
	public boolean verificarEntrada(String entrada) {
		
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
