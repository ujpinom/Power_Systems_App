package proyectoSistemasDePotencia;
import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
public class InfoGeneradores extends GridPane {
	
	private Text infolinea= new Text();
	private GridPane gd= new GridPane();
	private VBox vb= new VBox(5);
	private HBox hb= new HBox();
	private Generadores generador;
	private Label z1l= new Label("Impedancia secuencia positiva [p,u]");
	private Label z2l= new Label("Impedancia secuencia negativa [p,u]");
	private Label z0l= new Label("Impedancia secuencia cero [p,u]");
	private Label tipoconexion= new Label("Tipo de conexión devanados internos");
	private TextField z1t= new TextField();
	private TextField z2t= new TextField();
	private TextField z0t= new TextField();
	private Button btncerrar= new Button("Aceptar");
	private HBox hb1= new HBox();
	private String[] tipoconexiones= {"Y-ATERRIZADO","Y"};
	private ObservableList<String> items= FXCollections.observableArrayList(tipoconexiones);
	private ComboBox<String> cbo = new ComboBox<>();
	private double impedanciaAterrizamiento;
	private Label orientacion= new Label("Orientación");
	private String [] orientaciones= {Generadores.LEFT,Generadores.RIGHT,Generadores.ARRIBA,Generadores.ABAJO};
	private ObservableList<String> hor= FXCollections.observableArrayList(orientaciones);
	private ComboBox<String> cbo1 = new ComboBox<>();
	private Label infoP= new Label("Info.Potencia");
	private VBox infoPotencia= new VBox(3);
	private HBox infoPotenciaSalida= new HBox();
	private HBox infoPotenciaReaLim= new HBox();
	private HBox infoPotenciaReacLim= new HBox();
	private final int SPACING= 2;
	
	private TextField setPotenciaSalida= new TextField();
	private TextField setPotenciaImaSalida= new TextField();
	
	private TextField setPotenciaSalidaMin= new TextField();
	private TextField setPotenciaSalidaMax= new TextField();
	
	private TextField setPotenciaSalidaImMax= new TextField();
	private TextField setPotenciaSalidaImMin= new TextField();
	
	public InfoGeneradores(Generadores generador) {
		
		this.setStyle("-fx-background-color: #3498DB;");
		btncerrar.setStyle("-fx-background-color: slateblue; -fx-text-fill: white ;");
		infolinea.setStroke(Color.rgb(241, 196, 15));
		
		infoP.setStyle("-fx-font: 12 arial;-fx-text-fill: white ;");
		
		infoPotencia.setAlignment(Pos.CENTER);
		infoPotencia.setStyle("-fx-background-color: #FF5722;");
		infoPotenciaSalida.setAlignment(Pos.CENTER_LEFT);  // Potencias especificadas de salida;
		infoPotenciaReaLim.setAlignment(Pos.CENTER_LEFT);
		infoPotenciaReacLim.setAlignment(Pos.CENTER_LEFT);
		infoPotencia.setMaxWidth(USE_COMPUTED_SIZE);
		infoPotenciaSalida.setMaxWidth(USE_COMPUTED_SIZE);
		infoPotenciaReaLim.setMaxWidth(USE_COMPUTED_SIZE);
		infoPotenciaReacLim.setMaxWidth(USE_COMPUTED_SIZE);
		
		HBox hb11= new HBox();
		hb11.setAlignment(Pos.CENTER_LEFT);
		hb11.setSpacing(SPACING);
		setPotenciaSalida.setPrefColumnCount(4);
		setPotenciaSalida.setText(""+generador.getMWSalida());
		hb11.getChildren().addAll(new Label("MW.Out"),setPotenciaSalida);
		
		HBox hb22= new HBox();
		hb22.setAlignment(Pos.CENTER_LEFT);
		hb22.setSpacing(SPACING);
		setPotenciaImaSalida.setPrefColumnCount(4);
		setPotenciaImaSalida.setText(""+generador.getMVarSalida());
		hb22.getChildren().addAll(new Label("MVar.Out"),setPotenciaImaSalida);
		
		
		
		
		HBox hb33= new HBox();
		hb33.setAlignment(Pos.CENTER_LEFT);
		hb33.setSpacing(SPACING);
		setPotenciaSalidaMin.setPrefColumnCount(4);
		setPotenciaSalidaMin.setText(""+generador.getMWSalidaMin());
		setPotenciaSalidaMin.setMaxWidth(USE_COMPUTED_SIZE);
		
		hb33.getChildren().addAll(new Label("MW.Min"),setPotenciaSalidaMin);
		
		HBox hb44= new HBox();
		hb44.setAlignment(Pos.CENTER_LEFT);
		hb44.setSpacing(SPACING);
		setPotenciaSalidaMax.setPrefColumnCount(4);
		setPotenciaSalidaMax.setMaxWidth(USE_COMPUTED_SIZE);
		setPotenciaSalidaMax.setText(""+generador.getMWSalidaMax());
		hb44.getChildren().addAll(new Label("MW.Max"),setPotenciaSalidaMax);
		
		
		
		
		HBox hb55= new HBox();
		hb55.setAlignment(Pos.CENTER_LEFT);
		hb55.setSpacing(SPACING);
		setPotenciaSalidaImMin.setPrefColumnCount(4);
		setPotenciaSalidaImMin.setText(""+ generador.getMVarSalidaMin());
		setPotenciaSalidaImMin.setMaxWidth(USE_COMPUTED_SIZE);
		hb55.getChildren().addAll(new Label("MVar.Min"),setPotenciaSalidaImMin);
		
		HBox hb66= new HBox();
		hb66.setAlignment(Pos.CENTER_LEFT);
		hb66.setSpacing(SPACING);
		setPotenciaSalidaImMax.setPrefColumnCount(4);
		setPotenciaSalidaImMax.setMaxWidth(USE_COMPUTED_SIZE);
		setPotenciaSalidaImMax.setText(""+generador.getMVarSalidaMax());
		hb66.getChildren().addAll(new Label("MVar.Max"),setPotenciaSalidaImMax);
		
		
		
		
		infoPotenciaSalida.setSpacing(7);
		infoPotenciaSalida.getChildren().addAll(hb11,hb22);
		
		

		infoPotenciaReaLim.setSpacing(7);
		infoPotenciaReaLim.getChildren().addAll(hb33,hb44);
		
		infoPotenciaReacLim.setSpacing(7);
		infoPotenciaReacLim.getChildren().addAll(hb55,hb66);
		
		
		
		infoPotencia.getChildren().addAll(infoP,infoPotenciaSalida,infoPotenciaReaLim,infoPotenciaReacLim);
		
		
		this.generador=generador;
		this.setPadding(new Insets(10));
		this.setVgap(10);
		this.getRowConstraints().add(new RowConstraints(50) );
		hb.getChildren().add(infolinea);
		hb.setAlignment(Pos.CENTER);
		hb1.setAlignment(Pos.CENTER_RIGHT);
		hb1.getChildren().add(btncerrar);
		this.add(hb, 0, 0);
		this.add(hb1, 0, 3);
		cbo.setMaxWidth(250);
		cbo.getItems().addAll(tipoconexiones);
		cbo1.setMaxWidth(250);
		cbo1.getItems().addAll(orientaciones);
		vb.getChildren().addAll(z1l,z1t,z2l,z2t,z0l,z0t,tipoconexion,cbo,orientacion,cbo1);
		this.add(vb,0 , 1);
		this.add(infoPotencia, 0, 2);
		infolinea.setText("INFORMACIÓN DEL GENERADOR "+ generador.getNombreGenerador());
		
		z1t.setText(""+generador.getImpedanciaZ1());
		z2t.setText(""+ generador.getImpedanciaZ2());
		z0t.setText(""+ generador.getImpedanciaZ0());
	
		cbo.setOnAction(e->{
			
			setConexionPrimaria(items.indexOf(cbo.getValue()));
			
			if(cbo.getValue().equals("Y-ATERRIZADO")) {
				
				InfoImpendanciAterrizadaG infoimpedancia = new InfoImpendanciAterrizadaG(generador);
				
				Scene dad= new Scene(infoimpedancia);
				Stage sta= new Stage();
				
				sta.setScene(dad);
				sta.setTitle("INFORMACIÓN IMPEDANCIA ATERRIZAMIENTO");
				sta.setResizable(false);
				sta.initModality(Modality.APPLICATION_MODAL);
				sta.showAndWait();
				
				impedanciaAterrizamiento=infoimpedancia.getImpedanciaAterrizada();
			}
		});
		
		cbo1.setOnAction(e->{
			
			setOrientacion(hor.indexOf(cbo1.getValue()));
			
		});
			
		btncerrar.setOnAction(e->{
			
		     Stage stage = (Stage) this.btncerrar.getScene().getWindow();
		     
		     boolean vz1t= verificarEntrada(z1t.getText());
		     boolean vz2t= verificarEntrada(z2t.getText());
		     boolean vz0t= verificarEntrada(z0t.getText());
		     
		     boolean mwout=verificarEntrada(setPotenciaSalida.getText());
		     boolean mvarout=verificarEntrada(setPotenciaImaSalida.getText());
		     boolean mwmin= verificarEntrada(setPotenciaSalidaMin.getText());
		     boolean mwmax= verificarEntrada(setPotenciaSalidaMax.getText());
		     boolean mvarmin=verificarEntrada(setPotenciaSalidaImMin.getText());
		     boolean mvarmax= verificarEntrada(setPotenciaSalidaImMax.getText());
		     
		     
		     if(z1t.getText().contains(",") ||z2t.getText().contains(",")||z0t.getText().contains(",")||vz1t||vz2t||vz0t
		    		 ||mwout ||mvarout||mwmin||mwmax||mvarmin||mvarmax) {
		    	 
		    	 JOptionPane.showMessageDialog(null, "Ingrese datos de tipo númerico utilizando punto como separador decimal");
		     }
		     else {
		    	 
		    	 generador.setimpedanciaZ0(Double.parseDouble(z0t.getText()));
		    	 generador.setimpedanciaZ1(Double.parseDouble(z1t.getText()));
		    	 generador.setimpedanciaZ2(Double.parseDouble(z2t.getText()));
		    	 
		    	 generador.setMWSalida(Double.parseDouble(setPotenciaSalida.getText()));
		    	 generador.setMVarSalida(Double.parseDouble(setPotenciaImaSalida.getText()));
		    	 generador.setMWSalidaMin(Double.parseDouble(setPotenciaSalidaMin.getText()));
		    	 generador.setMWSalidaMax(Double.parseDouble(setPotenciaSalidaMax.getText()));
		    	 generador.setMVarSalidaMin(Double.parseDouble(setPotenciaSalidaImMin.getText()));
		    	 generador.setMVarSalidaMax(Double.parseDouble(setPotenciaSalidaImMax.getText()));
		    	
		    }
		    
		     stage.close();
		});
		
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
	
	public double getImpedanciaAterrizamiento() {
		return impedanciaAterrizamiento;
	}
	
	public void setConexionPrimaria(int index) {
		generador.setAterrizamiento(tipoconexiones[index]);
		
	}
	
	public void setOrientacion(int index) {
		generador.setOrientacion(orientaciones[index]);
	}
	
}
