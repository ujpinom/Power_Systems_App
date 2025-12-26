package proyectoSistemasDePotencia;

import javafx.scene.layout.GridPane;
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


public class InfoImpedanciaFallaLinea extends GridPane {
	
	private Text infolinea= new Text();
	private GridPane gd= new GridPane();
	private Label z1l= new Label("Ingrese el valor de la impedancia en el lugar de la falla: ");
	private Label descripcionvoltajePuntoFalla= new Label("Ingrese la tensión en el punto de falla de la línea");
	private TextField voltajePuntoFalla= new TextField();
	private TextField z1t= new TextField();
	private String nombreElemento;
	private Button btncerrar= new Button("Aceptar");
	private double impedanciaFalla;
	private Lineas lineaFallada;
	
	
	public InfoImpedanciaFallaLinea(String nombreElemento,Lineas lineaFallada) {
		this.setStyle("-fx-background-color: #3498DB;");
		btncerrar.setStyle("-fx-background-color: slateblue; -fx-text-fill: white ;");
		infolinea.setStroke(Color.rgb(241, 196, 15));
		this.nombreElemento=nombreElemento;
		this.lineaFallada=lineaFallada;
		this.setPadding(new Insets(10));
		this.setVgap(10);
		this.getRowConstraints().add(new RowConstraints(50) );
		
		infolinea.setText("IMPEDANCIA DE FALLA OCURRIDA SOBRE EL ELEMENTO "+nombreElemento+
				"\nNOTA: Si la falla ocurre sobre una línea, esta se calcula en la mitad de dicha línea. ");
		
		
		this.add(infolinea, 0, 0);
		this.add(z1l, 0, 1);
		this.add(z1t, 0, 2);
		this.add(descripcionvoltajePuntoFalla, 0, 3);
		this.add(voltajePuntoFalla, 0, 4);
		this.add(btncerrar, 0, 5);
		
		voltajePuntoFalla.setText(""+lineaFallada.getTensionLineaPuntoFalla());
		z1t.setText(""+lineaFallada.getImpedanciaFalla());
		
		btncerrar.setOnAction(e->{

		     Stage stage = (Stage) this.btncerrar.getScene().getWindow();
		     
		     boolean vz1t= verificarEntrada(z1t.getText());
		     boolean voltaje= verificarEntrada(voltajePuntoFalla.getText());
		 
		     if(z1t.getText().contains(",") ||vz1t||voltaje ) {
		    	 
		    	 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico utilizando punto como separador decimal");
		     }
		     else {
		     
		     impedanciaFalla=(Double.parseDouble(z1t.getText()));
		     lineaFallada.setTensionLineaPuntoFalla(Double.parseDouble(voltajePuntoFalla.getText()));
		   
		    }
		     
		     stage.close();
		});
	}
	
	public double getImpedanciaFalla() {
		
		return impedanciaFalla;
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

