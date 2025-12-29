package proyectoSistemasDePotencia;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InfoCarga extends GridPane {
	private Text infolinea= new Text();
	private GridPane gd= new GridPane();
	private VBox vb= new VBox(5);
	private HBox hb= new HBox();
	private Label z1l= new Label("Potencia Activa [MW]");
	private Label z2l= new Label("Potencia Reactiva [MVars]");
	private TextField z1t= new TextField();
	private TextField z2t= new TextField();
	private Button btncerrar= new Button("Aceptar");
	private HBox hb1= new HBox();
	private Label orientacion= new Label("Orientación");
	private String [] orientaciones= {Carga.LEFT,Carga.RIGHT,Carga.ARRIBA,Carga.ABAJO};
	private ObservableList<String> hor= FXCollections.observableArrayList(orientaciones);
	private ComboBox<String> cbo1 = new ComboBox<>();
	private Carga carga;
	
	public InfoCarga(Carga carga) {
		this.setStyle("-fx-background-color: #3498DB;");
		btncerrar.setStyle("-fx-background-color: slateblue; -fx-text-fill: white ;");
		infolinea.setStroke(Color.rgb(241, 196, 15));
		
		this.carga=carga;
		this.setPadding(new Insets(10));
		this.setVgap(10);
		this.getRowConstraints().add(new RowConstraints(50) );
		hb.getChildren().add(infolinea);
		hb.setAlignment(Pos.CENTER);
		hb1.setAlignment(Pos.CENTER_RIGHT);
		hb1.getChildren().add(btncerrar);
		this.add(hb, 0, 0);
		this.add(hb1, 0, 2);
		cbo1.setMaxWidth(250);
		cbo1.getItems().addAll(orientaciones);
		vb.getChildren().addAll(z1l,z1t,z2l,z2t,orientacion,cbo1);
		this.add(vb,0 , 1);
		infolinea.setText("INFORMACIÓN DE LA CARGA "+ carga.getNombreCarga());
		
		z1t.setText(""+carga.getPotenciaActiva());
		z2t.setText(""+ carga.getPotenciaReactiva());
	
		
		cbo1.setOnAction(e->{
			
			setOrientacion(hor.indexOf(cbo1.getValue()));
			
		});
		
		
		
		btncerrar.setOnAction(e->{
			
		     Stage stage = (Stage) this.btncerrar.getScene().getWindow();
		     
		     boolean vz1t= verificar(z1t.getText());
		     boolean vz2t= verificarEntrada(z2t.getText());
		     
		     
		     if(vz1t||vz2t) {
		    	 
		    	 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico utilizando punto como separador decimal");
		     }
		     else {
		    	 
		    	 carga.setPotenciaActiva(Double.parseDouble(z1t.getText()));
		    	 carga.setPotenciaReactiva(Double.parseDouble(z2t.getText()));
		    	
		    }
		    
		     stage.close();
		});
		
	}
	
	public boolean verificar(String entrada) {
		
		int contador=0;
		
		for(int i=0;i<entrada.length();i++) {
			
			char c= entrada.charAt(i);

			if(!Character.isDigit(c) && c!='.') {
				return true;
			}
			else if(c=='.') {
				++contador;
				continue;
			}
			
			if (contador>1) {
				return true;
			}
			
			contador=0;
		}
		
		return false;
		
	}
	
	public boolean verificarEntrada(String entrada) {
		
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
	
	
	
	

	public void setOrientacion(int index) {
		carga.setOrientacion(orientaciones[index]);
	}
	

}
