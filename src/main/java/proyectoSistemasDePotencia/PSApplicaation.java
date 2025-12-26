package proyectoSistemasDePotencia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import grafos.Edges;
import grafos.MyGraph;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import weightedGraphs.WeightEdeges;
import weightedGraphs.WeightedGraph;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class PSApplicaation extends Application {
	private BorderPane bPane= new BorderPane();
	private VBox vBox= new VBox(10); 
	private HBox hbox3=new HBox(5);
	private HBox hBox1= new HBox(5);
	private HBox hBox2= new HBox(5);
	private ToggleGroup group = new ToggleGroup();
	private ToggleGroup group1 = new ToggleGroup();
	private HBox hBox3= new HBox(5);
	private RadioButton linea= new RadioButton("Linea");
	private RadioButton none= new RadioButton("Edición");
	private RadioButton trafo= new RadioButton("Transformador");
	private RadioButton generador= new RadioButton("Generador");
	private RadioButton barra= new RadioButton("Barra");
	private Button instru= new Button("Instrucciones",new ImageView("proyectoSistemasDePotencia/centerAlignment.png"));
	private Button undo= new Button("Limpiar");
	private Button ejecutar= new Button("Ejecutar",new ImageView("proyectoSistemasDePotencia/right.gif"));
	private Label tElementos= new Label("ELEMENTOS");
	private TextArea display= new TextArea();
	private RadioButton trifa= new RadioButton("F.Trifósica");
	private RadioButton mono= new RadioButton("F.Monofósica");
	private RadioButton bifasica= new RadioButton("F.Bifósica");
	private RadioButton bifasicaTierra= new RadioButton("F.Bifósica-Tierra");
	private boolean lselected=false;
	private boolean tselected=false;
	private boolean gselected=false;
	private boolean bselected=false; 
	private boolean noneselected=false;
	private boolean cSelected;
	private boolean bancoSelected;
	private boolean trifasica; 
	private boolean monofasica;
	private boolean lineaALinea;
	private boolean bifasicaATierra;
	private boolean fPotencia;
	private PSApplicaationView paneldibujo= new PSApplicaationView();
	private RadioButton flujoPotencia=new RadioButton("F.Potencia");
	private RadioButton carga=new RadioButton("Carga");
	private RadioButton banco= new RadioButton("Banco");
	private Button undo1=new Button("B",new ImageView("proyectoSistemasDePotencia/44426.png"));
	private Button restablecer= new Button("R",new ImageView("proyectoSistemasDePotencia/reanudar_318-1763.jpg"));
	
	
	public static void main(String[] args) {

		Application.launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage a) throws Exception {
		
		linea.setPadding(new Insets(5, 5, 5, 5));
		linea.setTextFill(Color.GREEN);
		linea.setContentDisplay(ContentDisplay.LEFT);
		trafo.setPadding(new Insets(5, 5, 5, 5));
		linea.setGraphic(new ImageView("proyectoSistemasDePotencia/sfsdfsd.png"));
		trafo.setGraphic(new ImageView("proyectoSistemasDePotencia/trafo1.png"));
		generador.setGraphic(new ImageView("proyectoSistemasDePotencia/Captura.png"));
		barra.setGraphic(new ImageView("proyectoSistemasDePotencia/barra.png"));
		carga.setGraphic(new ImageView("proyectoSistemasDePotencia/carga.png"));
		banco.setGraphic(new ImageView("proyectoSistemasDePotencia/Banco.png"));
		trafo.setTextFill(Color.GREEN);
		trafo.setContentDisplay(ContentDisplay.LEFT);
		carga.setToggleGroup(group);
		carga.setContentDisplay(ContentDisplay.LEFT);
		banco.setToggleGroup(group);
		banco.setContentDisplay(ContentDisplay.LEFT);
		generador.setPadding(new Insets(5, 5, 5, 5));
		generador.setTextFill(Color.GREEN);
		carga.setPadding(new Insets(5, 5, 5, 5));
		banco.setPadding(new Insets(5, 5, 5, 5));
		hbox3.getChildren().addAll(undo1);
		generador.setContentDisplay(ContentDisplay.LEFT);
		barra.setPadding(new Insets(5, 5, 5, 5));
		barra.setTextFill(Color.GREEN);
		carga.setTextFill(Color.GREEN);
		banco.setTextFill(Color.GREEN);
		none.setPadding(new Insets(5, 5, 5, 5));
		none.setTextFill(Color.GREEN);
		none.setContentDisplay(ContentDisplay.LEFT);
		barra.setContentDisplay(ContentDisplay.LEFT);
		linea.setToggleGroup(group);
		trafo.setToggleGroup(group);
		generador.setToggleGroup(group);
		barra.setToggleGroup(group);
		none.setToggleGroup(group);
		trifa.setToggleGroup(group1);
		flujoPotencia.setToggleGroup(group1);
		mono.setToggleGroup(group1);
		bifasica.setToggleGroup(group1);
		bifasicaTierra.setToggleGroup(group1);
		display.setPrefColumnCount(40);display.setPrefRowCount(4);
		GridPane gPane= new GridPane();gPane.setPadding(new Insets(10));
		bPane.setPadding(new Insets(5));
		vBox.setAlignment(Pos.TOP_LEFT);	
		vBox.setPadding(new Insets(10));
		hBox1.setPadding(new Insets(5));
		hBox2.setPadding(new Insets(5));
		hBox3.setPadding(new Insets(5));
		hBox1.setAlignment(Pos.CENTER);
		
		hBox2.setAlignment(Pos.TOP_CENTER);
		hBox3.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(ejecutar,tElementos,linea,trafo,generador,barra,carga,banco,none,hbox3,undo);
		vBox.setStyle("-fx-border-color: black");
		VBox.setMargin(tElementos, new Insets(5));
		hBox1.getChildren().addAll(instru);
		hBox2.getChildren().addAll(display);
		hBox2.setStyle("-fx-border-color: black");
		hBox1.setStyle("-fx-border-color: black");
		hBox3.setStyle("-fx-border-color: black");
		hBox3.getChildren().addAll(trifa,mono,bifasica,bifasicaTierra,flujoPotencia);
		gPane.add(hBox1, 0, 0);	
		gPane.add(hBox2, 1, 0);	
		gPane.add(hBox3, 2, 0);
		bPane.setLeft(vBox);bPane.setTop(gPane);
		
		bPane.setCenter(paneldibujo);
		paneldibujo.setStyle("-fx-background-color: white;");
		undo.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		vBox.setStyle("-fx-background-color: #FAD7A0 ;");
		display.setEditable(false);
		
		Scene scene= new Scene(bPane);
		a.show();
		
		a.setScene(scene);
		a.setResizable(true);
		a.setTitle("Anólisis de Fallas");
		
		Handler handler = new Handler();
		
		linea.setOnAction(handler);
		trafo.setOnAction(handler);
		generador.setOnAction(handler);
		barra.setOnAction(handler);
		none.setOnAction(handler);
		undo.setOnAction(handler);
		carga.setOnAction(handler);
		banco.setOnAction(handler);
		ejecutar.setOnAction(handler);
		trifa.setOnAction(handler);
		mono.setOnAction(handler);
		bifasica.setOnAction(handler);
		bifasicaTierra.setOnAction(handler);
		flujoPotencia.setOnAction(handler);
		undo1.setOnAction(handler);
		restablecer.setOnAction(handler);
		

	}
	
	class Handler implements EventHandler{

		@Override
		public void handle(Event a) {
			
			if(linea.isSelected()) {
				lselected=true;tselected=false; gselected=false;
				bselected=false;noneselected=false;cSelected=false;bancoSelected=false;
				
			}

			if(trafo.isSelected()) {
				lselected=false;tselected=true; gselected=false;
				bselected=false;noneselected=false;cSelected=false;bancoSelected=false;
				
			}
			if(generador.isSelected()) {
				lselected=false;tselected=false; gselected=true;
				bselected=false;noneselected=false;cSelected=false;bancoSelected=false;
				
			}
			if(barra.isSelected()) {
				lselected=false;tselected=false; gselected=false;
				bselected=true;noneselected=false;cSelected=false;bancoSelected=false;
			}
			if(none.isSelected()) {
				lselected=false;tselected=false; gselected=false;
				bselected=false;noneselected=true;cSelected=false;bancoSelected=false;
			}
			if(carga.isSelected()) {
				lselected=false;tselected=false; gselected=false;
				bselected=false;noneselected=false;cSelected=true;bancoSelected=false;
			}
			
			if(banco.isSelected()) {
				lselected=false;tselected=false; gselected=false;
				bselected=false;noneselected=false;cSelected=false;bancoSelected=true;
			}
			
			if(a.getSource()==undo) {
				
				paneldibujo.limpiarArea();
			}
			
			if(a.getSource()==undo1) {
				
				paneldibujo.borrarUltimoElemento();
			}
			
//			if(a.getSource()==restablecer) {
//				paneldibujo.reestablecerElementos();
//			}
			
			if(trifa.isSelected()) {
				
				trifasica=true;monofasica=false;lineaALinea=false;
				bifasicaATierra=false;fPotencia=false;
				
				carga.setDisable(true);
				banco.setDisable(true);
			}
			
			if(mono.isSelected()) {
				
				trifasica=false;monofasica=true;lineaALinea=false;
				bifasicaATierra=false;fPotencia=false;
				
				carga.setDisable(true);
				banco.setDisable(true);

			}
			
			if(bifasica.isSelected()) {
				
				trifasica=false;monofasica=false;lineaALinea=true;
				bifasicaATierra=false;fPotencia=false;
				
				carga.setDisable(true);
				banco.setDisable(true);

			}
			
			if(bifasicaTierra.isSelected()) {
				
				trifasica=false;monofasica=false;lineaALinea=false;
				bifasicaATierra=true;fPotencia=false;
				
				carga.setDisable(true);
				banco.setDisable(true);

			}
			
			if(flujoPotencia.isSelected()) {
				
				trifasica=false;monofasica=false;lineaALinea=false;
				bifasicaATierra=false;fPotencia=true;
				
				carga.setDisable(false);
				banco.setDisable(false);

			}
			if(!flujoPotencia.isSelected()) {
				
				carga.setDisable(true);
				banco.setDisable(true);
			}

			
			
			if(a.getSource()==ejecutar) {
					
				List<List<Integer>> bb= paneldibujo.getGraph().componentesConectados();
				
				if(bb.size()==2) {
					
					paneldibujo.crearGrafos();
				}
				else {
					
					JOptionPane.showMessageDialog(null, "El sistema no es cerrado. Róviselo por favor.");
				}
				
			}
	
		}
		
	}
	
	
	class PSApplicaationView extends AnchorPane{
		
		private ArrayList<Barras> barras= new ArrayList<>();
		private ArrayList<Lineas> conexiones= new ArrayList<>();
		private ArrayList<Transformador> conexiones1= new ArrayList<>();
		private ArrayList<Generadores> conexiongene= new ArrayList<>();
		private ArrayList<Carga> cargas= new ArrayList<>();
		private ArrayList<Bancos> bancos= new ArrayList<>();

		private double endOfLineX, endOfLineY;
		private Barras startB=null;
		private boolean isLineOn = false;
		private int radioCirculo=14;
		
		private String nombreBarra= "B";
		private String nombreLinea="L";private String nombreTrafo="T";
		private String nombreGenerador="G";
		private String nombreCarga="C";
		private String nombreBanco="BA";

		private ArrayList<Double> distanciasLineas= new ArrayList<>();
		private ArrayList<Double> corGenerador= new ArrayList<>();
		private ArrayList<Double> corCarga= new ArrayList<>();
		private ArrayList<Double> corBanco= new ArrayList<>();

		private ObservableList<Node> lista= this.getChildren();
		private ArrayList<Double> posBarra= new ArrayList<>();
		
		private WeightedGraph<Barras> grafo1;
		private WeightedGraph<Barras> grafo2;
		private WeightedGraph<Barras> grafo0;
		
		private double impedanciaDeFalla;
		private String tipoElementoFallado;
		private ArrayList<Double> coorFalla= new ArrayList<>();
		private boolean fallaEnLinea=false;
		private Lineas lineaFallada;
		private Barras barraFallada;
		
		private double xCoorG;
		private double yCoorG;
		private double magCorrientePuntoFallaFaseA;
		private double magCorrientePuntoFallaFaseB;
		private double magCorrientePuntoFallaFaseC;
		private double angCorrientePuntoFallaFaseA;
		private double angCorrientePuntoFallaFaseB;
		private double angCorrientePuntoFallaFaseC;
		private double largoBarra=70;
		private double ancho=10;
		
		private LinkedList<Object> objetosCreados= new LinkedList<>();   //Almacena todos los objectos creados para llevar un record de ellos;
		private LinkedList<Object> restablecerElementos= new LinkedList<>();
		
		public PSApplicaationView() {
			
			
			repaint();
			barras.add(new Barras("Tierra"));
			
			this.setOnMouseClicked(e->{
				
				double x= e.getX();double y= e.getY();
				Node tipoElemento=tipoElemento(x,y);
				
				if(e.getButton()==MouseButton.PRIMARY) {
					
					if(!barraMuycerca(x,y)) {
						
						  if (isLineOn && (lselected||tselected)) {
							  
				                isLineOn = false;
				                repaint();
				                return;
				            }
					}
				
				if((bselected) && !barraMuycerca(x,y)) {
					
					Barras barra= new Barras(x,y,nombreBarra);
					barras.add(barra);
					objetosCreados.add(barra);
					repaint();
					return;
				}
				
				
				Barras b=getContainingVertex(x,y);
				
				if(!isLineOn&&(lselected||tselected) && b!=null) {
					
					startB=b;
					endOfLineX=e.getX();
					isLineOn=true;
				}
				
				if((lselected||tselected)&&isLineOn ) {
					
					if(b!=null) {
						
					if(!sonIguales(b,startB)) {
						
					if(conexiones.isEmpty()&&lselected) {
						
						Lineas l= new Lineas(startB,b,1,1,1);
						conexiones.add(l);
						objetosCreados.add(l);
						distanciasLineas.add(longitudLinea(b,startB));
						
					}
					else if(conexiones.isEmpty()&&tselected) {
						
						Transformador t= new Transformador(startB,b,1,1,1);
						conexiones1.add(t);
						objetosCreados.add(t);
						distanciasLineas.add(longitudLinea(b,startB));
					}
					else {
				
						double pe=longitudLinea(b,startB);
					
						if(!distanciasLineas.contains(pe) && lselected) {
							
							distanciasLineas.add(pe);
							conexiones.add(new Lineas(startB,b,1,1,1));
							objetosCreados.add(new Lineas(startB,b,1,1,1));
							
						}
						
						else if(!distanciasLineas.contains(pe) && tselected) {
							
							distanciasLineas.add(pe);
							conexiones1.add(new Transformador(startB,b,1,1,1));
							objetosCreados.add(new Transformador(startB,b,1,1,1));

						}
					
					}
					
					isLineOn = false;
					repaint();
					
					}
				}
			}
				
			if(gselected&& b!=null) {
					
					
					if(!corGenerador.contains(b.getXbarra())) {
						
						xCoorG=e.getX();yCoorG=e.getY();
						
						b.setxCoorG(xCoorG);b.setyCoorG(yCoorG);
						
						conexiongene.add(new Generadores(nombreGenerador,1,1,1,b));
						objetosCreados.add(new Generadores(nombreGenerador,1,1,1,b));
						
						corGenerador.add(b.getXbarra());
					}
				
					repaint();
					return;
			}
			
			if(cSelected && b!=null) {
				
				if(!corCarga.contains(b.getXbarra())) {
					
					b.setCoordenadasCarga(new Point2D(e.getX(),e.getY()));
					
					cargas.add(new Carga(new Point2D(e.getX(),e.getY()),b,nombreCarga));
					objetosCreados.add(new Carga(new Point2D(e.getX(),e.getY()),b,nombreCarga));
					corCarga.add(b.getXbarra());
					
					repaint();
					return;
				}
			
			}
			
			if(bancoSelected && b!=null) {
				
				if(!corBanco.contains(b.getXbarra())) {
					
					b.setCoordenadasBanco(new Point2D(e.getX(),e.getY()));
					bancos.add(new Bancos(new Point2D(e.getX(),e.getY()),b,nombreBanco));
					objetosCreados.add(new Bancos(new Point2D(e.getX(),e.getY()),b,nombreBanco));
					corBanco.add(b.getXbarra());
					repaint();
					return;
				}
				
			}
			
				if(noneselected) {
					
					 tipoElemento=tipoElemento(x,y);
					
					if((tipoElemento instanceof Rectangle)&&((Rectangle)tipoElemento).getWidth()!=30){
			
						display.setText("Elemento:   "+b.getNombreBarra()+"    Voltaje-Prefalla: "+b.getVoltajePrefalla()+
								" p,u"+"\nCorriente punto de falla (Elemento "+tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
						+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+String.format("%.4f",magCorrientePuntoFallaFaseB)
						+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+String.format("%.4f", magCorrientePuntoFallaFaseC)
						+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+"\nVoltaje/Fase Post-Falla:  "
						+"\nFase A: "+String.format("%.4f", b.getVoltajePosFallaFaseA())+" Ang. "+String.format("%.4f", b.getAnguloVoltajeFaseA())+
						"ó "+"[p,u]         Fase B: "+String.format("%.4f", b.getVoltajePosFallaFaseB())+" Ang. "+String.format("%.4f", b.getAnguloVoltajeFaseB())+"ó "+"[p,u]         "
								+ "Fase C: "+String.format("%.4f", b.getVoltajePosFallaFaseC())+" Ang. "+String.format("%.4f", b.getAnguloVoltajeFaseC())+"ó "+"[p,u]");
						
						if(e.isAltDown()) {
							
							InfoBarras infoBarra= new InfoBarras(b);
							Scene dad= new Scene(infoBarra);
							Stage sta= new Stage();
							
							sta.setScene(dad);
							sta.setTitle("INFORMACIóN DE BARRAS");
							sta.setResizable(false);
							sta.initModality(Modality.APPLICATION_MODAL);
							sta.showAndWait();
							b.setVoltajePrefalla(infoBarra.getvoltajePrefalla());
							
							if(!b.getOrientacion().equals(b.getOrientacionPrimaria())) {
								
								
								if(b.getOrientacion().equals("H")) {
									
								double xCoor=b.getXbarra()-b.getLargo()/2;
								double yCoor=b.getYbarra()+b.getLargo()/2-b.getAncho()/2;
								b.setXbarra(xCoor);b.setYbarra(yCoor);
								b.setAncho(largoBarra);
								b.setLargo(ancho);
								b.setOrientacionPrimaria(b.getOrientacion());
									
									
								}
								else if(b.getOrientacion().equals("V")) {
									
									double xCoor=b.getXbarra()+b.getAncho()/2-ancho/2;
									double yCoor=b.getYbarra()-largoBarra/2+ancho/2;
									b.setXbarra(xCoor);b.setYbarra(yCoor);
									b.setAncho(ancho);
									b.setLargo(largoBarra);
									b.setOrientacionPrimaria(b.getOrientacion());
								
								}
								
								
							}
					
							repaint();
							return;

						}
						
					}
					
					else if(tipoElemento instanceof Line ) {
						
						boolean bandera=false;
						boolean bandera1=false;boolean bandera2=false;
					
						
						for(int i=0;i<conexiones.size();i++) {
							
							Point2D pntmedio= conexiones.get(i).getPuntomedio();
							
							if(((Line)tipoElemento).contains(pntmedio)) {
							
							
//							if((conexiones.get(i).getBarra1().getXbarra()+Barras.ancho/2==inicioX || conexiones.get(i).getBarra2().getXbarra()+Barras.ancho/2==inicioX)
//									&&(conexiones.get(i).getBarra1().getXbarra()+Barras.ancho/2==finalX || conexiones.get(i).getBarra2().getXbarra()+Barras.ancho/2==finalX)) {
								
								if(!fallaEnLinea || conexiones.get(i)!=lineaFallada) {
									
									display.setText("Elemento:   "+conexiones.get(i).getNombreLinea()+"\nCorriente punto de falla (Elemento "+
								tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
								String.format("%.4f", magCorrientePuntoFallaFaseB)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
								String.format("%.4f",magCorrientePuntoFallaFaseC)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nCorriente/Fase Post-Falla:  "+
								"\nFase A: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseA())+"[p,u]"
													+ "         Fase B: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseB())+"[p,u]         "
											+ "Fase C: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseC())+"[p,u]");
									
								}
								else {
									
									display.setText("Elemento:   "+conexiones.get(i).getNombreLinea()+"\nCorriente punto de falla (Elemento 50%-"+
									tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
									String.format("%.4f", magCorrientePuntoFallaFaseB)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
									String.format("%.4f", magCorrientePuntoFallaFaseC)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nContribucción Barra-"+
									conexiones.get(i).getBarra1().getNombreBarra()+" : "+
											"\nFase A: " + String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseA())+
											" Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseA())+"ó "+"[p,u]"+
											 "         Fase B: "+String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseB())+
											 " Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseB())+"ó "+"[p,u]"+
											 "         Fase C: "+String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseC())+
											 " Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseC())+"ó "+"[p,u]"+
											"\nContribucción Barra-"+conexiones.get(i).getBarra2().getNombreBarra()+" : "+"\nFase A: " + 
											 String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseA())+" Ang. "+
											String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseA())+
											"ó "+"[p,u]"+ "         Fase B: "+String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseB())+
											" Ang. "+String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseB())+"ó "+"[p,u]"+
											 "         Fase C: "+String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseC())+" Ang. "+
											String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseC())+"ó "+"[p,u]");
									
								}

								
								bandera=true;
								bandera1=true;
								
									if(e.isAltDown()) {
										
										InfoLineas infolinea= new InfoLineas(conexiones.get(i));
										Scene dad= new Scene(infolinea);
										Stage sta= new Stage();
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN DE LóNEAS");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
			
									}
									
								break;
					
							}
							
						}
						
						if(!bandera) {
							
							
							for(int i=0;i<conexiones1.size();i++) {
								
								Point2D pntmedio= conexiones1.get(i).getPuntoMedio();
								
								if(((Line)tipoElemento).contains(pntmedio)) {
//								
//								if((conexiones1.get(i).getBarra1().getXbarra()+Barras.ancho/2==inicioX || conexiones1.get(i).getBarra2().getXbarra()+Barras.ancho/2==inicioX)
//										&&(conexiones1.get(i).getBarra1().getXbarra()+Barras.ancho/2==finalX || conexiones1.get(i).getBarra2().getXbarra()+Barras.ancho/2==finalX)) {

									display.setText("Elemento:   "+conexiones1.get(i).getNombreLinea()+"   Conexión( "+
									conexiones1.get(i).getConexionPrimaria()+","+conexiones1.get(i).getConexionSecundaria()+" )"+
											"\nCorriente punto de falla (Elemento "+tipoElementoFallado+")"+":"+"\nFase A: "+
									String.format("%.4f", magCorrientePuntoFallaFaseA)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
									String.format("%.4f", magCorrientePuntoFallaFaseB)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
									String.format("%.4f", magCorrientePuntoFallaFaseC)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nCorriente/Fase Post-Falla:  "+
									"\nFase A: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseA())+"[p,u]"
													+ "         Fase B: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseB())+"[p,u]         "
											+ "Fase C: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseC())+"[p,u]");
									
									bandera1=true;
									
									if(e.isAltDown()) {
										
										InfoTrafo infotrafo= new InfoTrafo(conexiones1.get(i));
										Scene dad= new Scene(infotrafo);
										Stage sta= new Stage();
										
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN DE TRANSFORMADORES");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
										
										conexiones1.get(i).setImpedanciaAterrizamientoPrimaria(infotrafo.getImpedanciaAterrizamientoPrimaria());
										conexiones1.get(i).setImpedanciaAterrizamientoSecundaria(infotrafo.getImpedanciaAterrizamientoSecundaria());
									}
									break;

								}
								
							}
						}
						
						if(!bandera && !bandera1) {
							
							for (int i=0;i<cargas.size();i++) {
								
								Point2D pntMedio= cargas.get(i).getPuntoMedio();
								
								if(((Line)tipoElemento).contains(pntMedio)) {
									
									display.setText("Elemento:   "+cargas.get(i).getNombreCarga()+"\nPotencia Activa [MW]: "+
									cargas.get(i).getPotenciaActiva()+"\nPotencia Reactiva [MVars]: "+cargas.get(i).getPotenciaReactiva());
									
									bandera2=true;
									
									if(e.isAltDown()) {
										
										InfoCarga infoCarga= new InfoCarga(cargas.get(i));
										Scene dad= new Scene(infoCarga);
										Stage sta= new Stage();
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN DE CARGAS");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
										repaint();
									}
								}
								
								
							}
							
						}
						
						if(!bandera && !bandera1 && !bandera2) {
							
							for(int i=0;i<bancos.size();i++) {
								
								Point2D pntmedio=bancos.get(i).getPuntoMedio();
								
								if(((Line)tipoElemento).contains(pntmedio)) {
									
									display.setText("Elemento:   "+bancos.get(i).getNombreCarga()+"\nPotencia Reactiva [MVars]: "+bancos.get(i).getPotenciaReactiva());
									
									if(e.isAltDown()) {
										
										infoBanco infoBanco= new infoBanco(bancos.get(i));
										Scene dad= new Scene(infoBanco);
										Stage sta= new Stage();
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN DEl BANCO");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
										repaint();
										
									}
								}
							}
							
							
						}
					}
					
					
					else if(tipoElemento instanceof Circle&& ((Circle)tipoElemento).getRadius()==15) {
						
						for(int i=0;i<conexiones1.size();i++) {
							
							Point2D pntMedio= conexiones1.get(i).getPuntoMedio();
							
							if(((Circle)tipoElemento).contains(pntMedio)) {
								
								display.setText("Elemento:   "+conexiones1.get(i).getNombreLinea()+"   Conexión( "+
								conexiones1.get(i).getConexionPrimaria()+","+conexiones1.get(i).getConexionSecundaria()+" )"+
										"\nCorriente punto de falla (Elemento "+tipoElementoFallado+")"+":"+"\nFase A: "+
								String.format("%.4f", magCorrientePuntoFallaFaseA)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
								String.format("%.4f", magCorrientePuntoFallaFaseB)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
								String.format("%.4f", magCorrientePuntoFallaFaseC)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nCorriente/Fase Post-Falla:  "+
								"\nFase A: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseA())+"[p,u]"
												+ "         Fase B: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseB())+"[p,u]         "
										+ "Fase C: "+String.format("%.4f", conexiones1.get(i).getCorrienteFallaFaseC())+"[p,u]");
								
							
								if(e.isAltDown()) {
									
									InfoTrafo infotrafo= new InfoTrafo(conexiones1.get(i));
									Scene dad= new Scene(infotrafo);
									Stage sta= new Stage();
									
									sta.setScene(dad);
									sta.setTitle("INFORMACIóN DE TRANSFORMADORES");
									sta.setResizable(false);
									sta.initModality(Modality.APPLICATION_MODAL);
									sta.showAndWait();
									
									conexiones1.get(i).setImpedanciaAterrizamientoPrimaria(infotrafo.getImpedanciaAterrizamientoPrimaria());
									conexiones1.get(i).setImpedanciaAterrizamientoSecundaria(infotrafo.getImpedanciaAterrizamientoSecundaria());
								}
								break;
								
							}
							
							
						}
						
					}
					
					else if(tipoElemento instanceof Rectangle&& ((Rectangle)tipoElemento).getWidth()==30) {
						
						for(int i=0;i<conexiones.size();i++) {
							
							Point2D pntMedio= conexiones.get(i).getPuntomedio();
							
							if(((Rectangle)tipoElemento).contains(pntMedio)) {
								
								if(!fallaEnLinea || conexiones.get(i)!=lineaFallada) {
									
									display.setText("Elemento:   "+conexiones.get(i).getNombreLinea()+"\nCorriente punto de falla (Elemento "+
								tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
								String.format("%.4f", magCorrientePuntoFallaFaseB)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
								String.format("%.4f",magCorrientePuntoFallaFaseC)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nCorriente/Fase Post-Falla:  "+
								"\nFase A: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseA())+"[p,u]"
													+ "         Fase B: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseB())+"[p,u]         "
											+ "Fase C: "+String.format("%.4f", conexiones.get(i).getCorrienteFallaFaseC())+"[p,u]");
									
								}
								else {
									
									
									display.setText("Elemento:   "+conexiones.get(i).getNombreLinea()+"\nCorriente punto de falla (Elemento 50%-"+
									tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
									String.format("%.4f", magCorrientePuntoFallaFaseB)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
									String.format("%.4f", magCorrientePuntoFallaFaseC)
									+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+ "\nContribucción Barra-"+
									conexiones.get(i).getBarra1().getNombreBarra()+" : "+
											"\nFase A: " + String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseA())+
											" Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseA())+"ó "+"[p,u]"+
											 "         Fase B: "+String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseB())+
											 " Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseB())+"ó "+"[p,u]"+
											 "         Fase C: "+String.format("%.4f", conexiones.get(i).getBarra1().getContribuccionFallaFaseC())+
											 " Ang. "+String.format("%.4f", conexiones.get(i).getBarra1().getAnguloContribucionFaseC())+"ó "+"[p,u]"+
											"\nContribucción Barra-"+conexiones.get(i).getBarra2().getNombreBarra()+" : "+"\nFase A: " + 
											 String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseA())+" Ang. "+
											String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseA())+
											"ó "+"[p,u]"+ "         Fase B: "+String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseB())+
											" Ang. "+String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseB())+"ó "+"[p,u]"+
											 "         Fase C: "+String.format("%.4f", conexiones.get(i).getBarra2().getContribuccionFallaFaseC())+
											 " Ang. "+String.format("%.4f", conexiones.get(i).getBarra2().getAnguloContribucionFaseC())+"ó "+"[p,u]");
									
								}
								
							
									if(e.isAltDown()) {
										
										InfoLineas infolinea= new InfoLineas(conexiones.get(i));
										Scene dad= new Scene(infolinea);
										Stage sta= new Stage();
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN DE LóNEAS");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
				
									}
								break;
								
								
							}
							
						}
					
						
					}
					
					else if(tipoElemento instanceof Circle&& ((Circle)tipoElemento).getRadius()<=20) {
						
						double xcenter= ((Circle)tipoElemento).getCenterX();
						for(int i=0;i<conexiongene.size();i++) {
							
							if(conexiongene.get(i).getBarra().getxCoorG()-30==xcenter ||
									conexiongene.get(i).getBarra().getxCoorG()+40==xcenter||
									((Circle)tipoElemento).contains(conexiongene.get(i).getXCenter(),conexiongene.get(i).getYCenter()) ) {
								
								
								display.setText("Elemento:   "+conexiongene.get(i).getNombreGenerador()+"\nCorriente punto de falla (Elemento "+
								tipoElementoFallado+")"+":"+"\nFase A: "+String.format("%.4f", magCorrientePuntoFallaFaseA)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseA)+"ó "+"[p,u]         Fase B: "+
								String.format("%.4f",  magCorrientePuntoFallaFaseB)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseB)+"ó "+"[p,u]         Fase C: "+
								String.format("%.4f",  magCorrientePuntoFallaFaseC)
								+" Ang. "+String.format("%.4f", angCorrientePuntoFallaFaseC)+"ó "+"[p,u]"+"\nContribucción de la móquina a la falla: "+
								"\nFase A: "+String.format("%.2f", conexiongene.get(i).getCorrienteFaseA())+" Ang. "+
								String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseA()) +"ó "+"[p,u]"
										+ "         Fase B: "+String.format("%.2f", conexiongene.get(i).getCorrienteFaseB())+
										" Ang. "+String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseB())+"ó "+
										"[p,u]         "
										+ "Fase C: "+String.format("%.2f", conexiongene.get(i).getCorrienteFaseC())+
										" Ang. "+String.format("%.2f", conexiongene.get(i).getAnguloCorrienteFaseC())+"ó "+"[p,u]");
								
								
								if(e.isAltDown()) {
									
									
									InfoGeneradores infog= new InfoGeneradores(conexiongene.get(i));
									Scene dad= new Scene(infog);
									Stage sta= new Stage();
									
									sta.setScene(dad);
									sta.setTitle("INFORMACIóN DE GENERADORES");
									sta.setResizable(false);
									sta.initModality(Modality.APPLICATION_MODAL);
									sta.showAndWait();
									
									conexiongene.get(i).setImpedanciaAterrizamiento(infog.getImpedanciaAterrizamiento());
									repaint();
								}
								
								break;
							}
							
						}
						
					}
					
				}
				

				}
				
				Barras b=getContainingVertex(x,y);
				
				if(e.getButton()==MouseButton.SECONDARY && b!=null) {
					
					barras.remove(b);
					removeGeneradorAdyacente(b);
					removeAdjacentEdges(b);
					removeCargaAdyacente(b);
					removerBancoAdyacente(b);
					repaint();
					
					
					for(int i=0;i<conexiones1.size();i++) {
						
						conexiones1.get(i).setConexionPrimaria("YN-"+conexiones1.get(i).getBarra1().getNombreBarra());
						conexiones1.get(i).setConexionSecundaria("YN-"+conexiones1.get(i).getBarra2().getNombreBarra());
					}
					
					return;
				}
				
				
				if(e.getButton()==MouseButton.MIDDLE) {
					

					 tipoElemento=tipoElemento(x,y);
					 
						if((tipoElemento instanceof Rectangle)&&((Rectangle)tipoElemento).getWidth()!=30){
							
							InfoImpedanciaFalla infolinea= new  InfoImpedanciaFalla(b.getNombreBarra());
							Scene dad= new Scene(infolinea);
							Stage sta= new Stage();
							
							sta.setScene(dad);
							sta.setTitle("INFORMACIóN IMPEDANCIA FALLA");
							sta.setResizable(false);
							sta.initModality(Modality.APPLICATION_MODAL);
							sta.showAndWait();
							
							impedanciaDeFalla=infolinea.getImpedanciaFalla();
							barraFallada=b;
							b.setImpedanciaFalla(impedanciaDeFalla);
							
							tipoElementoFallado=b.getNombreBarra();
							
							double xFalla=b.getXbarra()-5;
							double yFalla=b.getYbarra()+b.getLargo()/2;
							
							coorFalla.add(xFalla);coorFalla.add(yFalla);
							fallaEnLinea=false;
					
							repaint();
						
						}
						
						else if(tipoElemento instanceof Line) {
							
							 boolean bandera=false;
							
							
							for(int i=0;i<conexiones.size();i++) {
								
								Point2D puntmedio=conexiones.get(i).getPuntomedio();
			
									
									if(((Line)tipoElemento).contains(puntmedio)) {
										
										InfoImpedanciaFallaLinea infolinea= new  InfoImpedanciaFallaLinea(conexiones.get(i).getNombreLinea(),conexiones.get(i));
										Scene dad= new Scene(infolinea);
										Stage sta= new Stage();
										
										sta.setScene(dad);
										sta.setTitle("INFORMACIóN IMPEDANCIA FALLA");
										sta.setResizable(false);
										sta.initModality(Modality.APPLICATION_MODAL);
										sta.showAndWait();
										
										impedanciaDeFalla=infolinea.getImpedanciaFalla();
										conexiones.get(i).setImpedanciafalla(impedanciaDeFalla);
										tipoElementoFallado=conexiones.get(i).getNombreLinea();
										lineaFallada=conexiones.get(i);
										
										double xFalla= e.getX()-6;
										double yFalla=e.getY()+8;
										
										bandera=true;
										
										coorFalla.add(xFalla);coorFalla.add(yFalla);
										fallaEnLinea=true;
										
										repaint();
										break;
									}

							}
							
							if(!bandera) {
								
								JOptionPane.showMessageDialog(null, "La falla no se puede ubicar dentro del transformador");
							}
							
							
						}

						else if(tipoElemento instanceof Rectangle && ((Rectangle)tipoElemento).getWidth()==30) {
							
							for(int i=0;i<conexiones.size();i++) {
								
								Point2D pntmedio=conexiones.get(i).getPuntomedio();
								
								if(((Rectangle)tipoElemento).contains(pntmedio)) {
									
									InfoImpedanciaFallaLinea infolinea= new  InfoImpedanciaFallaLinea(conexiones.get(i).getNombreLinea(),conexiones.get(i));
									Scene dad= new Scene(infolinea);
									Stage sta= new Stage();
									
									sta.setScene(dad);
									sta.setTitle("INFORMACIóN IMPEDANCIA FALLA");
									sta.setResizable(false);
									sta.initModality(Modality.APPLICATION_MODAL);
									sta.showAndWait();
									
									impedanciaDeFalla=infolinea.getImpedanciaFalla();
									conexiones.get(i).setImpedanciafalla(impedanciaDeFalla);
									tipoElementoFallado=conexiones.get(i).getNombreLinea();
									lineaFallada=conexiones.get(i);
									
									double xFalla= e.getX()-6;
									double yFalla=e.getY()+8;
									
									coorFalla.add(xFalla);coorFalla.add(yFalla);
									fallaEnLinea=true;
									
									repaint();
									break;
									
								}
								
							}
							
						}
						
						else if(tipoElemento instanceof Circle&& ((Circle)tipoElemento).getRadius()==15) {
							
							JOptionPane.showMessageDialog(null, "La falla no se puede ubicar dentro del transformador");
						}
						
					
				}  // Fin if(e.getButton()==MouseButton.MIDDLE)
				
			});
			
			this.setOnMouseMoved(e->{
				
				double x= e.getX();double y= e.getY();
				Node tipoElemento=tipoElemento(x,y);
				if(e.isControlDown() && noneselected) {
				
					isLineOn=false;
//					
					Circle c= getCirculo(x,y);
					
							
					 if (c!= null && c.getRadius()==14) {
						 
						 
						Barras b= getContainingVertex(c.getCenterX(),c.getCenterY());
						
						 
						 c.setCenterX(x);
						 c.setCenterY(y);
						 
				         b.setXbarra(c.getCenterX()-b.getAncho()/2);
				         b.setYbarra(c.getCenterY()-b.getLargo()/2);
				    
				         b.setxCoorG(b.getXbarra()+b.getAncho()/2);
				         b.setyCoorG(b.getYbarra()+b.getLargo()/2);
				         
				       
				         
				         repaint();
				     } 
					 
					 if(tipoElemento instanceof Circle) {
						 
						 double xcenter=((Circle)tipoElemento).getCenterX();
						 double ycenter=((Circle)tipoElemento).getCenterY();

						 boolean bandera1=false;
						 
//						 moveText=true;
//						System.out.println('s');
//						((Text)tipoElemento).setX(x);
//						((Text)tipoElemento).setY(y);
//						repaint();
						 for (int i=0;i<cargas.size();i++) {
							 
							 double xcarga=cargas.get(i).getBarra().getCoordenadasCarga().getX();
							 double ycarga=cargas.get(i).getBarra().getCoordenadasCarga().getY();
							 
							 if(xcenter==xcarga && ycenter==ycarga) {
								 
								 ((Circle)tipoElemento).setCenterX(x);
								 ((Circle)tipoElemento).setCenterY(y);
								 
								 bandera1=true;
								 
								 Barras b=cargas.get(i).getBarra();
								 
								 b.setCoordenadasCarga(new Point2D(((Circle)tipoElemento).getCenterX(),((Circle)tipoElemento).getCenterY()));
								 
								 repaint();
								 break;
								 
							 }
							 
							 
						 }
	
							 for (int i=0;i<bancos.size();i++) {
								 
								 double xcarga=bancos.get(i).getBarra().getCoordenadasBanco().getX();
								 double ycarga=bancos.get(i).getBarra().getCoordenadasBanco().getY();
								 
								 if(xcenter==xcarga && ycenter==ycarga) {
									 
									 ((Circle)tipoElemento).setCenterX(x);
									 ((Circle)tipoElemento).setCenterY(y);
									 
									 bandera1=true;
									 
									 Barras b=bancos.get(i).getBarra();
									 
									 b.setCoordenadasBanco(new Point2D(((Circle)tipoElemento).getCenterX(),((Circle)tipoElemento).getCenterY()));
									 
									 repaint();
									 break;
									 
								 }
								 
							 }
					 }
					
				}
				
				else if(((tselected||lselected))&& isLineOn) {
					
					endOfLineX=e.getX();
					endOfLineY=e.getY();
					repaint();
					
				}
				
			});
		
		
	}
		
		
		public double longitudLinea(Barras b1,Barras b2) {
			
			return Math.sqrt((b1.getXbarra()-b2.getXbarra())*(b1.getXbarra()-b2.getXbarra())
					+(b1.getYbarra()-b2.getYbarra())*(b1.getYbarra()-b2.getYbarra()));
		}
		
		public double longitudLinea(double x1,double y1,double x2,double y2) {
			
			return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		}
		
		
		public void removeGeneradorAdyacente(Barras b) {
			
			for(int i=0;i<conexiongene.size();i++) {
				
				if(conexiongene.get(i).getBarra()==b) {
					conexiongene.remove(i--);
				}
			}
			
			
		}
		
		
		public void removeCargaAdyacente(Barras b) {
			
			for(int i=0;i<cargas.size();i++) {
				
				if(cargas.get(i).getBarra()==b) {
					cargas.remove(i--);
				}
			}
			
			
		}
		
		
		public void removerBancoAdyacente(Barras b) {
			
			
			for(int i=0;i<bancos.size();i++) {
				
				if(bancos.get(i).getBarra()==b) {
					bancos.remove(i--);
				}
			}
			
		}
		
		public void removeAdjacentEdges(Barras b) {
			
			for(int i=0;i<conexiones.size();i++) {
				
				if(conexiones.get(i).getBarra1()==b || conexiones.get(i).getBarra2()==b) {
					 conexiones.remove(i--);
				}
				
			}
			
			for(int i=0;i<conexiones1.size();i++) {
				
				if(conexiones1.get(i).getBarra1()==b|| conexiones1.get(i).getBarra2()==b) {
					conexiones1.remove(i--);
				}
			}
			
			
		}
		
		public Node tipoElemento(double x,double y) {
			
			for(int i=0;i<lista.size();i++) {
				
				if(lista.get(i).contains(x,y)) {
									
					return lista.get(i);
				}
			
			}
			
		return null;
			
	}
		
		public Circle getCirculo(double x,double y) {
		
			
			for(Node list:lista) {
				
				if(list instanceof Circle) {
					
					if(((Circle) list).contains(x,y)) {
						return (Circle) list;
					}
				}	
				
			}
			
			return null;
		}
		
		public boolean sonIguales(Barras barra1,Barras barra2) {
			
			if(barra1.getXbarra()==barra2.getXbarra()&& barra1.getYbarra()==barra2.getYbarra()) {
				
				return true;
			}
			else {
				
			return false;}
		}
		
		
		public Barras getContainingVertex(double x,double y) {
			
			for(int i=0;i<barras.size();i++) {
				
				if(dentroBarra(barras.get(i),x,y)) {
					
					return barras.get(i);
				}
				
			}
			
			return null;
			
		}
		
		
		public boolean dentroBarra(Barras barra,double x,double y) {
			
			if(barra.getOrientacion().equals("V")) {
				
			if(x>=barra.getXbarra() && x<=barra.getXbarra()+barra.getAncho() && y>=barra.getYbarra()&& y<=barra.getYbarra()+barra.getLargo()) {
				
				return true;
			}
			
			else {
				
				return false;
			}
			
			}
			else {
				
				if(x>=barra.getXbarra() && x<=barra.getXbarra()+barra.getAncho() && y>=barra.getYbarra()&& y<=barra.getYbarra()+barra.getLargo()) {
					
					return true;
				}
				
				else {
					return false;
				}
				
				
			}
			
		}
		
		public void repaint() {
			
			this.getChildren().clear();
			dibujarBarra();
			dibujarLineas();
			dibujarGenerador();
			dibujarFalla();
			dibujarCarga();
			dibujarBanco();
			coorFalla.clear();
			posBarra.clear();
			
			
		}

		public boolean barraMuycerca(double x, double y) {
			
			
			for(int i=1;i<barras.size();i++) {
				
				if(obtenerDistancia(x,y,barras.get(i).getPuntoMedioBarra().getX(),barras.get(i).getPuntoMedioBarra().getY())) {
					return true;
				}
				
			}
			
			return false;
			
		}
		
		public boolean obtenerDistancia(double x1,double y1,double x2,double y2) {
			
			if(Math.abs(x1-x2)< 10+5 && Math.abs(y1-y2)<70+10) {
				
				return true;
				
			}
			else {
				
				return false;
			}
		
		}
		
		public void limpiarArea() {
			
			conexiones.clear();
			conexiones1.clear();
			conexiongene.clear();
			barras.clear();
			cargas.clear();
			bancos.clear();
			distanciasLineas.clear();
			corGenerador.clear();
			corCarga.clear();
			corBanco.clear();
			posBarra.clear();
			objetosCreados.clear();
			restablecerElementos.clear();
			repaint();
			barras.add(new Barras("Tierra"));
			tipoElementoFallado=null;
		}
		
		public void borrarUltimoElemento() {
			
			Object elemento=objetosCreados.pollLast();
			if (elemento!=null)
				
			restablecerElementos.add(elemento);
			
			if(elemento instanceof Barras && elemento!=null) {
				
				Barras b=((Barras)elemento);
				barras.remove(b);
				repaint();
				
			}
			
			else if(elemento instanceof Lineas && !(elemento instanceof Transformador)
					&& elemento!=null&&conexiones.size()>0) {
				
				
				conexiones.remove(conexiones.size()-1);
				distanciasLineas.clear();
				repaint();
				
			}
			else if(elemento instanceof Generadores && elemento!=null&&conexiongene.size()>0) {
				
				conexiongene.remove(conexiongene.size()-1);
				corGenerador.clear();
				repaint();
				
			}
			
			else if((elemento instanceof Transformador) &&(elemento instanceof Lineas) &&
					elemento!=null&&conexiones1.size()>0) {
				
				conexiones1.remove(conexiones1.size()-1);
				distanciasLineas.clear();
				repaint();
			}
			
			else if(elemento instanceof Carga && elemento!=null&&cargas.size()>0) {
				
				cargas.remove(cargas.size()-1);
				corCarga.clear();
				repaint();
			}
			
			else if(elemento instanceof Bancos && elemento!=null&&bancos.size()>0) {
				
				bancos.remove(bancos.size()-1);
				corBanco.clear();
				repaint();
				
			}
			
			
		}
		
//		public void reestablecerElementos() {
//			
//			Object elemento=restablecerElementos.pollLast();
//			if(elemento!=null)
//			objetosCreados.add(elemento);
//			
//			if(elemento instanceof Barras && elemento!=null) {
//				
//				Barras b=((Barras)elemento);
//				barras.add(b);
//				repaint();
//				
//			}
//			
//			else if(elemento instanceof Lineas && !(elemento instanceof Transformador)&& elemento!=null) {
//				Barras b1=((Lineas)elemento).getBarra1();
//				Barras b2=((Lineas)elemento).getBarra2();
//				
//				if(conexiones.size()==0) {
//					Lineas ll=((Lineas)elemento);
//					
//					
//					conexiones.add(ll);
//					distanciasLineas.clear();
//					repaint();
//				}
//				
//				else {
//				
//				for(int i=0;i<conexiones.size();i++) {
//					
//										
//					if(conexiones.get(i).getBarra1()==b1&&conexiones.get(i).getBarra2()==b2) {
//						break;
//					}
//					else {
//						
//						Lineas ll=((Lineas)elemento);
//						
//						
//						conexiones.add(ll);
//						distanciasLineas.clear();
//						repaint();
//					}
//				}
//				
//				}
//				
//			
//				
//			}
//			else if(elemento instanceof Generadores && elemento!=null) {
//				
//				Generadores g=((Generadores)elemento);
//				conexiongene.add(g);
//				corGenerador.clear();
//				repaint();
//				
//			}
//			
//			else if((elemento instanceof Transformador) &&(elemento instanceof Lineas) && elemento!=null) {
//				
//				Transformador t= ((Transformador)elemento);
//				conexiones1.add(t);
//				distanciasLineas.clear();
//				repaint();
//			}
//			
//			else if(elemento instanceof Carga && elemento!=null) {
//				Carga c= ((Carga)elemento);
//				cargas.add(c);
//				corCarga.clear();
//				repaint();
//			}
//			
//			else if(elemento instanceof Bancos && elemento!=null) {
//				Bancos b=((Bancos)elemento);
//				bancos.add(b);
//				corBanco.clear();
//				repaint();
//				
//			}
//			
//			
//			
//			
//			
//		}
		
		public void dibujarBarra() {
			
			
			for(int i=0;i<barras.size();i++) {
				
				
				if(barras.get(i).getOrientacion().equals("H")) {
					
					barras.get(i).setNombreBarra(nombreBarra+(i));
					posBarra.add(barras.get(i).getXbarra());
					
					Collections.sort(posBarra);
					Rectangle barra= new Rectangle();
					
					barra.setX(barras.get(i).getXbarra());
					barra.setY(barras.get(i).getYbarra());
					barra.setWidth(barras.get(i).getAncho());
					barra.setHeight(barras.get(i).getLargo());
					
					double xMedio=barras.get(i).getXbarra()+barras.get(i).getAncho()/2;
					double yMedio=barras.get(i).getYbarra()+barras.get(i).getLargo()/2;
					
					barras.get(i).setPuntoMedioBarra(new Point2D(xMedio,yMedio));
//					barra.setRotate(-90);
					
					Circle circulo= new Circle();
					circulo.setRadius(radioCirculo);
					circulo.setCenterX(barras.get(i).getXbarra()+barras.get(i).getAncho()/2);
					circulo.setCenterY(barras.get(i).getYbarra()+barras.get(i).getLargo()/2);
					circulo.setVisible(false);
					
					Text nbarra= new Text(barras.get(i).getNombreBarra());
					nbarra.setStroke(Color.ORANGE);nbarra.setStrokeWidth(1);
				
					nbarra.setX(barras.get(i).getXbarra()-15);
					nbarra.setY(barras.get(i).getYbarra()+10);
					
				
					this.getChildren().addAll(barra,nbarra,circulo);
				}
				else if(barras.get(i).getOrientacion().equals("V")) {

					
					barras.get(i).setNombreBarra(nombreBarra+(i));
					posBarra.add(barras.get(i).getXbarra());
					Collections.sort(posBarra);
					if(i==0) {
						continue;
					}
					else {
					
					double xMedio=barras.get(i).getXbarra()+barras.get(i).getAncho()/2;
					double yMedio=barras.get(i).getYbarra()+barras.get(i).getLargo()/2;
					
					barras.get(i).setPuntoMedioBarra(new Point2D(xMedio,yMedio));
						
					Rectangle barra= new Rectangle();
					
					barra.setX(barras.get(i).getXbarra());
					barra.setY(barras.get(i).getYbarra());
					barra.setWidth(barras.get(i).getAncho());
					barra.setHeight(barras.get(i).getLargo());
					
					Circle circulo= new Circle();
					circulo.setRadius(radioCirculo);
					circulo.setCenterX(barras.get(i).getXbarra()+barras.get(i).getAncho()/2);
					circulo.setCenterY(barras.get(i).getYbarra()+barras.get(i).getLargo()/2);
					circulo.setVisible(false);
					
					Text nbarra= new Text(barras.get(i).getNombreBarra());
					nbarra.setStroke(Color.ORANGE);nbarra.setStrokeWidth(1);
					
					nbarra.setX(barras.get(i).getXbarra()-5);
					nbarra.setY(barras.get(i).getYbarra()-5);
					
					this.getChildren().addAll(barra,nbarra,circulo);
				}
			}
				
			}
			
		}
		
		public void dibujarFalla() {
			
			if(coorFalla.size()>0) {
				
			Text ubicarFalla= new Text("X");
			ubicarFalla.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 25));
			
			ubicarFalla.setFill(Color.RED);
			ubicarFalla.setX(coorFalla.get(0));
			ubicarFalla.setY(coorFalla.get(1));
			this.getChildren().add(ubicarFalla);
		}
			
		}
		
		
		public void dibujarCarga() {
			
			for(int i=0;i<cargas.size();i++) {
				
				corCarga.add(cargas.get(i).getBarra().getXbarra());
				cargas.get(i).setNombreCarga(nombreCarga+(i+1));
				
				getChildren().addAll(DibujarCarga.dibujarCargaBanco(cargas.get(i),"C"));
				getChildren().add(DibujarCarga.nombreCargaBanco(cargas.get(i),"C"));
				
				
			}
			
			
		}
		
		
		public void dibujarBanco() {
			
			for(int i=0;i<bancos.size();i++) {
				
				corBanco.add(bancos.get(i).getBarra().getXbarra());
				bancos.get(i).setNombreCarga(nombreBanco+(i+1));
				
				getChildren().addAll(DibujarCarga.dibujarCargaBanco(bancos.get(i),"B"));
				getChildren().add(DibujarCarga.nombreCargaBanco(bancos.get(i),"B")); //Modificar el metodos para establecer el nombre
				
				
				
			}
			
			
		}
		
		public void dibujarGenerador() {
			
			for(int i=0;i<conexiongene.size();i++) {
				corGenerador.add(conexiongene.get(i).getBarra().getXbarra());
				
				conexiongene.get(i).setNombreGenerador(nombreGenerador+(i+1));
				if(conexiongene.get(i).getOrientacion().equals(Generadores.RIGHT)) {
					
					Line lineg= new Line(conexiongene.get(i).getBarra().getxCoorG(),
							conexiongene.get(i).getBarra().getyCoorG(),conexiongene.get(i).getBarra().getxCoorG()+20,
							conexiongene.get(i).getBarra().getyCoorG());
					
					lineg.setStrokeWidth(4);
					
					Circle cirg= new Circle();
					cirg.setRadius(20);
					cirg.setCenterX(lineg.getEndX()+20);
					cirg.setCenterY(lineg.getEndY());
					cirg.setFill(Color.WHITE);
					cirg.setStroke(Color.BLACK);
					
					Arc arc1= new Arc(cirg.getCenterX()-9,cirg.getCenterY(),9,9,0,180);
					Arc arc2= new Arc(cirg.getCenterX()+9,cirg.getCenterY(),9,9,180,180);
					Text nombreg= new Text(conexiongene.get(i).getNombreGenerador());
					
					nombreg.setFill(Color.BLUE);
					nombreg.setX(cirg.getCenterX()-3);
					nombreg.setY(cirg.getCenterY()-25);
					
					arc1.setFill(Color.WHITE);
					arc1.setStroke(Color.BLACK);
					arc2.setFill(Color.WHITE);
					arc2.setStroke(Color.BLACK);
					
					this.getChildren().addAll(lineg,cirg,arc1,arc2,nombreg);
				
					
				}
				else if(conexiongene.get(i).getOrientacion().equals(Generadores.LEFT)) {
				
				
				Line lineg= new Line(conexiongene.get(i).getBarra().getxCoorG(),conexiongene.get(i).getBarra().getyCoorG(),conexiongene.get(i).getBarra().getxCoorG()-10,
						conexiongene.get(i).getBarra().getyCoorG());
				
				lineg.setStrokeWidth(4);
				
				Circle cirg= new Circle();
				cirg.setRadius(20);
				cirg.setCenterX(lineg.getEndX()-20);
				cirg.setCenterY(lineg.getEndY());
				cirg.setFill(Color.WHITE);
				cirg.setStroke(Color.BLACK);
				
				Arc arc1= new Arc(cirg.getCenterX()-9,cirg.getCenterY(),9,9,0,180);
				Arc arc2= new Arc(cirg.getCenterX()+9,cirg.getCenterY(),9,9,180,180);
				
				Text nombreg= new Text(conexiongene.get(i).getNombreGenerador());
				nombreg.setFill(Color.BLUE);
				nombreg.setX(cirg.getCenterX()-3);
				nombreg.setY(cirg.getCenterY()-25);
				
				arc1.setFill(Color.WHITE);
				arc1.setStroke(Color.BLACK);
				arc2.setFill(Color.WHITE);
				arc2.setStroke(Color.BLACK);
				
				this.getChildren().addAll(lineg,cirg,arc1,arc2,nombreg);
				}
				
				
				else if(conexiongene.get(i).getOrientacion().equals(Generadores.ARRIBA)) {
					
					Line lineg= new Line(conexiongene.get(i).getBarra().getXbarra()+conexiongene.get(i).getBarra().getAncho()/2,
							conexiongene.get(i).getBarra().getYbarra()+conexiongene.get(i).getBarra().getLargo()/2,
							conexiongene.get(i).getBarra().getXbarra()+conexiongene.get(i).getBarra().getAncho()/2,
							(conexiongene.get(i).getBarra().getYbarra()+conexiongene.get(i).getBarra().getLargo()/2)-10);
					
					lineg.setStrokeWidth(4);
					
					Circle cirg= new Circle();
					cirg.setRadius(20);
					cirg.setCenterX(lineg.getEndX());
					cirg.setCenterY(lineg.getEndY()-20);
					
					conexiongene.get(i).setXCenter(lineg.getEndX());
					conexiongene.get(i).setYCenter(lineg.getEndY()-20);
					
					cirg.setFill(Color.WHITE);
					cirg.setStroke(Color.BLACK);
					
					Arc arc1= new Arc(cirg.getCenterX()-9,cirg.getCenterY(),9,9,0,180);
					Arc arc2= new Arc(cirg.getCenterX()+9,cirg.getCenterY(),9,9,180,180);
					
					Text nombreg= new Text(conexiongene.get(i).getNombreGenerador());
					nombreg.setFill(Color.BLUE);
					nombreg.setX(cirg.getCenterX()-3);
					nombreg.setY(cirg.getCenterY()-25);
					
					arc1.setFill(Color.WHITE);
					arc1.setStroke(Color.BLACK);
					arc2.setFill(Color.WHITE);
					arc2.setStroke(Color.BLACK);
					this.getChildren().addAll(lineg,cirg,arc1,arc2,nombreg);
					
				}
				
				
				else if(conexiongene.get(i).getOrientacion().equals(Generadores.ABAJO)) {
					
					Line lineg= new Line(conexiongene.get(i).getBarra().getXbarra()+conexiongene.get(i).getBarra().getAncho()/2,
							conexiongene.get(i).getBarra().getYbarra()+conexiongene.get(i).getBarra().getLargo()/2,
							conexiongene.get(i).getBarra().getXbarra()+conexiongene.get(i).getBarra().getAncho()/2,
							(conexiongene.get(i).getBarra().getYbarra()+conexiongene.get(i).getBarra().getLargo()/2)+10);
					
					lineg.setStrokeWidth(4);
					Circle cirg= new Circle();
					
					cirg.setRadius(20);
					cirg.setCenterX(lineg.getEndX());
					cirg.setCenterY(lineg.getEndY()+20);
					
					conexiongene.get(i).setXCenter(lineg.getEndX());
					conexiongene.get(i).setYCenter(lineg.getEndY()+20);
					
					cirg.setFill(Color.WHITE);
					cirg.setStroke(Color.BLACK);
					
					Arc arc1= new Arc(cirg.getCenterX()-9,cirg.getCenterY(),9,9,0,180);
					Arc arc2= new Arc(cirg.getCenterX()+9,cirg.getCenterY(),9,9,180,180);
					
					Text nombreg= new Text(conexiongene.get(i).getNombreGenerador());
					
					nombreg.setFill(Color.BLUE);
					nombreg.setX(cirg.getCenterX()-3);
					nombreg.setY(cirg.getCenterY()+30);
					
					arc1.setFill(Color.WHITE);
					arc1.setStroke(Color.BLACK);
					arc2.setFill(Color.WHITE);
					arc2.setStroke(Color.BLACK);
					
					this.getChildren().addAll(lineg,cirg,arc1,arc2,nombreg);
					
					
				}
			}
			
		}
		
		public void dibujarLineas() {
			
			  if (isLineOn && lselected) {
				  
				  Line linea= new Line(startB.getXbarra()+startB.getAncho()/2, startB.getYbarra()+startB.getLargo()/2, endOfLineX, endOfLineY);
				  linea.setStroke(Color.GREEN);
				  linea.setStrokeWidth(2);
				  getChildren().add(linea);
			      
			  
			  }
			  else if(isLineOn && tselected) {
				  
				  Line linea= new Line(startB.getXbarra()+startB.getAncho()/2, startB.getYbarra()+startB.getLargo()/2, endOfLineX, endOfLineY);
				  linea.setStroke(Color.RED);
				  linea.setStrokeWidth(2);
				  getChildren().add(linea);
			  }
			
			 
			  for(int i=0;i<conexiones.size();i++) {
				   
				  
				  Line linea= new Line(conexiones.get(i).getBarra1().getPuntoMedioBarra().getX(),
						  conexiones.get(i).getBarra1().getPuntoMedioBarra().getY(),
						  conexiones.get(i).getBarra2().getPuntoMedioBarra().getX(),
						  conexiones.get(i).getBarra2().getPuntoMedioBarra().getY());
				  
				  linea.setFill(Color.RED);
				  
				  double pe= longitudLinea(linea.getStartX(),linea.getStartY(),linea.getEndX(),linea.getEndY());
				  distanciasLineas.add(pe);
				 
				  conexiones.get(i).setNombreLinea(nombreLinea+(i+1));
				  double puntoMedioX=Math.abs(conexiones.get(i).getBarra1().getPuntoMedioBarra().getX()+ 
						  conexiones.get(i).getBarra2().getPuntoMedioBarra().getX())/2;
				  
				  double puntoMedioY=Math.abs(conexiones.get(i).getBarra1().getPuntoMedioBarra().getY()+
						  conexiones.get(i).getBarra2().getPuntoMedioBarra().getY())/2;
				  
				  conexiones.get(i).setPuntomedio(new Point2D(puntoMedioX,puntoMedioY));
				  
				  Rectangle rec= new Rectangle();
				  rec.setX(puntoMedioX-15);
				  rec.setY(puntoMedioY-15);
				  rec.setWidth(30);
				  rec.setHeight(30);
				  rec.setStroke(Color.RED);
				  rec.setFill(Color.WHITE);
				  
				  linea.setStrokeWidth(4);
				  
				  Text textLinea= new Text(conexiones.get(i).getNombreLinea());
				  textLinea.setStrokeWidth(1);
				  textLinea.setStroke(Color.RED);
				 
				  
				  textLinea.setX(puntoMedioX-3);
				  textLinea.setY(puntoMedioY+2);
	
				  getChildren().addAll(linea,rec,textLinea);
				 
			  }
			
			  
			 
			  for(int i=0;i<conexiones1.size();i++) {
				   
				  

				  Line linea= new Line(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getX(),
						  conexiones1.get(i).getBarra1().getPuntoMedioBarra().getY(),
						  conexiones1.get(i).getBarra2().getPuntoMedioBarra().getX(),
						  conexiones1.get(i).getBarra2().getPuntoMedioBarra().getY());
				  
				  double pe= longitudLinea(linea.getStartX(),linea.getStartY(),linea.getEndX(),linea.getEndY());
				  distanciasLineas.add(pe);
				  
				  
				  conexiones1.get(i).setNombreLinea(nombreTrafo+(i+1));
				  double puntoMedioX=Math.abs(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getX()+ 
						  conexiones1.get(i).getBarra2().getPuntoMedioBarra().getX())/2;
				  
				  double puntoMedioY=Math.abs(conexiones1.get(i).getBarra1().getPuntoMedioBarra().getY()+
						  conexiones1.get(i).getBarra2().getPuntoMedioBarra().getY())/2;
				  
				  conexiones1.get(i).setPuntoMedio(new Point2D(puntoMedioX,puntoMedioY));
				  Circle cc1= new Circle();
				  Circle cc2= new Circle();
				  
				  cc1.setRadius(15);
				  cc1.setCenterX(puntoMedioX-5);cc1.setCenterY(puntoMedioY);
				  cc1.setStroke(Color.GREEN);
				  cc1.setFill(Color.WHITE);
				  cc2.setRadius(15);
				  cc2.setCenterX(puntoMedioX+5);cc2.setCenterY(puntoMedioY);
				  cc2.setStroke(Color.RED);
				  cc2.setFill(Color.WHITE);
				  
				  ;
				  
				  linea.setStroke(Color.BLACK);
				  linea.setStrokeWidth(4);
				  
				  getChildren().add(linea);
				  this.getChildren().addAll(cc1,cc2);
				  
				  Text textLinea= new Text(conexiones1.get(i).getNombreLinea());
				  textLinea.setStroke(Color.GREEN);
				  textLinea.setStrokeWidth(1);
				  
				 
				 textLinea.setX(puntoMedioX);
				 textLinea.setY(puntoMedioY);
				 
				 
			
				  getChildren().addAll(textLinea); 
			  }
		
		}
		
		public void crearGrafos() {  //Crea las matrices de ayancencia y los diferentes tipos de matrices de impedancia de sencuencia
			 
			if(tipoElementoFallado!=null) {
				
		 		if(trifasica) 
		 			fallaTrifasica();
		 		
		 		if(monofasica || bifasicaATierra) 
		 			fallaMonofasica();
		 		
//		 		if(bifasicaATierra);
//		 			fallaBifasicaATierra();
		 		
		 		if(lineaALinea) 
		 			fallaLineaALinea();
		}
			else {
				
				JOptionPane.showMessageDialog(null, "POR FAVOR UBIQUE LA FALLA YA SEA SOBRE UNA BARRA O LóNEA");
			}
			
	    	 	
	} // fin del mótodo crearGrafo()
		
		public void fallaLineaALinea() {
			
			List<WeightEdeges > bordes1= new ArrayList<>();
		 	List<WeightEdeges > bordes2= new ArrayList<>();
			
	
			for(int i=0;i<conexiones.size();i++) {
		 		
		 		int x= barras.indexOf(conexiones.get(i).getBarra1());
		    	int y= barras.indexOf(conexiones.get(i).getBarra2());
		    	double z1=conexiones.get(i).getimpedanciaLineaZ1();
		    	double z2=conexiones.get(i).getimpedanciaLineaZ2();
		    	
		    	
		    	
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		    	bordes2.add(new WeightEdeges(x,y,z2));
		    	bordes2.add(new WeightEdeges(y,x,z2));
		   	
		 		
		 	}
	 	
		for(int i=0;i<conexiones1.size();i++) {
	 		
	 		int x= barras.indexOf(conexiones1.get(i).getBarra1());
	    	int y= barras.indexOf(conexiones1.get(i).getBarra2());
	    	
	    	double z1=conexiones1.get(i).getimpedanciaLineaZ1();
	    	double z2=conexiones1.get(i).getimpedanciaLineaZ2();
	    	

	    	bordes1.add(new WeightEdeges(x,y,z1));
	    	bordes1.add(new WeightEdeges(y,x,z1));
	    	
	    	bordes2.add(new WeightEdeges(x,y,z2));
	    	bordes2.add(new WeightEdeges(y,x,z2));

    
	 	}
		
		for(int i=0;i<conexiongene.size();i++) {
	 		
	 		int x= barras.indexOf(conexiongene.get(i).getBarra());
	 		int y=0;
	 		
	 		double z1= conexiongene.get(i).getImpedanciaZ1();
	 		double z2= conexiongene.get(i).getImpedanciaZ2();
	 		
	 		
	    	bordes1.add(new WeightEdeges(x,y,z1));
	    	bordes1.add(new WeightEdeges(y,x,z1));
	    	
	    	bordes2.add(new WeightEdeges(x,y,z2));
	    	bordes2.add(new WeightEdeges(y,x,z2));
	    	
	    
	 	}
		
		grafo1=new WeightedGraph<>(barras,bordes1);
    	grafo2= new WeightedGraph<>(barras,bordes2);
    
//    	
    	List<List<Edges>> borde1= grafo1.getConexiones();List<List<Edges>> borde2= grafo2.getConexiones();
   
    	double [][] yBarraSecuencia1= obtenerMatrizAdyacencia(borde1);
    	double [][] yBarraSecuencia2=  obtenerMatrizAdyacencia(borde2);
//    	
    	double [][] zBarraSecuencia1= Zbarra.getZbarra(yBarraSecuencia1);
    	double [][] zBarraSecuencia2= Zbarra.getZbarra(yBarraSecuencia2);
    	
    
    	
       	
    	
    	if(tipoElementoFallado.charAt(0)=='B') {
    		
    		FallaLineaALinea fallalinealinea= new FallaLineaALinea(zBarraSecuencia1, zBarraSecuencia2, barras, 
    				conexiones, conexiones1, conexiongene, barraFallada,borde1);
    		
    		angCorrientePuntoFallaFaseA=barraFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB=barraFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC=barraFallada.getAngCorrientePuntoFallaFaseC();
			
			magCorrientePuntoFallaFaseA=barraFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB=barraFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC=barraFallada.getMagcorrientePuntoFallaFaseC();
    
    		
    	}
    	
    	else if(tipoElementoFallado.charAt(0)=='L') {
    		
    		FallaLineaALineaLinea fallaLineaALineaLinea= new FallaLineaALineaLinea(zBarraSecuencia1, zBarraSecuencia2, barras, 
    				conexiones, conexiones1, conexiongene, lineaFallada);
    		
    		angCorrientePuntoFallaFaseA= lineaFallada.getAngCorrientePuntoFallaFaseA();
			angCorrientePuntoFallaFaseB= lineaFallada.getAngCorrientePuntoFallaFaseB();
			angCorrientePuntoFallaFaseC= lineaFallada.getAngCorrientePuntoFallaFaseC();
			
			magCorrientePuntoFallaFaseA= lineaFallada.getMagcorrientePuntoFallaFaseA();
			magCorrientePuntoFallaFaseB= lineaFallada.getMagcorrientePuntoFallaFaseB();
			magCorrientePuntoFallaFaseC= lineaFallada.getMagcorrientePuntoFallaFaseC();
    	}
	
	}
		
		
		public void fallaTrifasica() {
			
		 	List<WeightEdeges > bordes1= new ArrayList<>();

			
		 	for(int i=0;i<conexiones.size();i++) {
		 		
		 		int x= barras.indexOf(conexiones.get(i).getBarra1());
		    	int y= barras.indexOf(conexiones.get(i).getBarra2());
		    	double z1=conexiones.get(i).getimpedanciaLineaZ1();
		   
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		 		
		 	}	
			
			for(int i=0;i<conexiones1.size();i++) {
		 		
		 		int x= barras.indexOf(conexiones1.get(i).getBarra1());
		    	int y= barras.indexOf(conexiones1.get(i).getBarra2());
		    	
		    	double z1=conexiones1.get(i).getimpedanciaLineaZ1();
		    	
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		 	}
			
			for(int i=0;i<conexiongene.size();i++) {
		 		
		 		int x= barras.indexOf(conexiongene.get(i).getBarra());
		 		int y=0;
		 		
		 		double z1= conexiongene.get(i).getImpedanciaZ1();
		 		
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		 	}
			
			grafo1=new WeightedGraph<>(barras,bordes1);
			
			List<List<Edges>> borde1= grafo1.getConexiones();
			

	    	double [][] yBarraSecuencia1= obtenerMatrizAdyacencia(borde1);

    	
	    	double [][] zBarraSecuencia1= Zbarra.getZbarra(yBarraSecuencia1);
	    	

			
			if(tipoElementoFallado.charAt(0)=='B') {
				
				FallaTrifasica calculoFalla= new FallaTrifasica(zBarraSecuencia1, tipoElementoFallado,barras,
						conexiones,conexiones1,conexiongene);
				
				angCorrientePuntoFallaFaseA=barraFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB=barraFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC=barraFallada.getAngCorrientePuntoFallaFaseC();
				
				magCorrientePuntoFallaFaseA=barraFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB=barraFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC=barraFallada.getMagcorrientePuntoFallaFaseC();
			}
			
			else if(tipoElementoFallado.charAt(0)=='L') {
			
				
				FallaTrifasicaLinea fallatrifasicalinea= new FallaTrifasicaLinea(zBarraSecuencia1, lineaFallada, barras, 
						conexiones, conexiones1,conexiongene);
				
				angCorrientePuntoFallaFaseA= lineaFallada.getAngCorrientePuntoFallaFaseA();
				angCorrientePuntoFallaFaseB= lineaFallada.getAngCorrientePuntoFallaFaseB();
				angCorrientePuntoFallaFaseC= lineaFallada.getAngCorrientePuntoFallaFaseC();
				
				magCorrientePuntoFallaFaseA= lineaFallada.getMagcorrientePuntoFallaFaseA();
				magCorrientePuntoFallaFaseB= lineaFallada.getMagcorrientePuntoFallaFaseB();
				magCorrientePuntoFallaFaseC= lineaFallada.getMagcorrientePuntoFallaFaseC();
				
			}
		}
		
		public void fallaMonofasica() {
			
			List<WeightEdeges > bordes1= new ArrayList<>();
		 	List<WeightEdeges > bordes2= new ArrayList<>();
		 	List<WeightEdeges > bordes0= new ArrayList<>();
		 	
 			for(int i=0;i<conexiones.size();i++) {
		 		
		 		int x= barras.indexOf(conexiones.get(i).getBarra1());
		    	int y= barras.indexOf(conexiones.get(i).getBarra2());
		    	
		    	double z1=conexiones.get(i).getimpedanciaLineaZ1();
		    	double z2=conexiones.get(i).getimpedanciaLineaZ2();
		    	double z0=conexiones.get(i).getimpedanciaLineaZ0();
		    	
		    	
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		    	bordes2.add(new WeightEdeges(x,y,z2));
		    	bordes2.add(new WeightEdeges(y,x,z2));
		    	
		    	bordes0.add(new WeightEdeges(x,y,z0));
		    	bordes0.add(new WeightEdeges(y,x,z0));
		    	
		 		
		 	}
		 	
		 	for(int i=0;i<conexiones1.size();i++) {
		 		
		 		int x= barras.indexOf(conexiones1.get(i).getBarra1());
		    	int y= barras.indexOf(conexiones1.get(i).getBarra2());
		    	
		    	double z1=conexiones1.get(i).getimpedanciaLineaZ1();
		    	double z2=conexiones1.get(i).getimpedanciaLineaZ2();
		    	double z0=conexiones1.get(i).getimpedanciaLineaZ0();
		    	
		    	
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		    	bordes2.add(new WeightEdeges(x,y,z2));
		    	bordes2.add(new WeightEdeges(y,x,z2));

		    	if(conexiones1.get(i).getConexionPrimaria().contains("YN")&&
		    			conexiones1.get(i).getConexionSecundaria().contains("YN")) {
		    		
		    		z0+=conexiones1.get(i).getImpedanciaAterrizamientoPrimaria()+
		    				conexiones1.get(i).getImpedanciaAterrizamientoSecundaria();
		    		
		    	}
		    	else if(conexiones1.get(i).getConexionPrimaria().contains("Y-")
		    			&&conexiones1.get(i).getConexionSecundaria().contains("Y-")) {
		    	
		    		z0+=10000000;
		    		
		    	}
		    	
		    	else if((conexiones1.get(i).getConexionPrimaria().contains("Y-")
		    			&&conexiones1.get(i).getConexionSecundaria().contains("YN"))||
		    			(conexiones1.get(i).getConexionPrimaria().contains("YN")
		    					&&conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {
		    		
		    		z0+=10000000;
		    	}
		    	
		    	else if(conexiones1.get(i).getConexionPrimaria().contains("DELTA")
		    			&&conexiones1.get(i).getConexionSecundaria().contains("DELTA")) {
		    		
		    		z0+=10000000;
		    		
		    	}
		    	else if((conexiones1.get(i).getConexionPrimaria().contains("Y-")
		    			&&conexiones1.get(i).getConexionSecundaria().contains("DELTA"))||
		    			(conexiones1.get(i).getConexionPrimaria().contains("DELTA")
		    					&&conexiones1.get(i).getConexionSecundaria().contains("Y-"))) {
		    		
		    		z0+=10000000;
		    	}
		    	
		    	else if((conexiones1.get(i).getConexionPrimaria().contains("YN")
		    			&&conexiones1.get(i).getConexionSecundaria().contains("DELTA"))||
		    			(conexiones1.get(i).getConexionPrimaria().contains("DELTA")
		    					&&conexiones1.get(i).getConexionSecundaria().contains("YN"))) {
		    		

		    		if(conexiones1.get(i).getConexionPrimaria().contains("YN")) {
		    			
		    			z0+=conexiones1.get(i).getImpedanciaAterrizamientoPrimaria();
		    			
		    			String xx=Character.toString(conexiones1.get(i).getConexionPrimaria().charAt(conexiones1.get(i).getConexionPrimaria().length()-1));
		    	
		    			x= Integer.parseInt(xx);
		    			y=0;
		    			
		    		}
		    		else if(conexiones1.get(i).getConexionSecundaria().contains("YN")) {
		    			
		    			String xx=Character.toString(conexiones1.get(i).getConexionSecundaria().charAt(conexiones1.get(i).getConexionSecundaria().length()-1));
		    			z0+=conexiones1.get(i).getImpedanciaAterrizamientoSecundaria();
		    			x= Integer.parseInt(xx);
		    			y=0;
		    			
		    		}
		    		
		    	}
		    	
		    	bordes0.add(new WeightEdeges(x,y,z0));
		    	bordes0.add(new WeightEdeges(y,x,z0));
		    	
	    
		 	}
		 	
		 	
		 	for(int i=0;i<conexiongene.size();i++) {
		 		
		 		int x= barras.indexOf(conexiongene.get(i).getBarra());
		 		int y=0;
		 		
		 		double z1= conexiongene.get(i).getImpedanciaZ1();
		 		double z2= conexiongene.get(i).getImpedanciaZ2();
		 		double z0= conexiongene.get(i).getImpedanciaZ0();
		 		
		 		
		    	bordes1.add(new WeightEdeges(x,y,z1));
		    	bordes1.add(new WeightEdeges(y,x,z1));
		    	
		    	bordes2.add(new WeightEdeges(x,y,z2));
		    	bordes2.add(new WeightEdeges(y,x,z2));
		    	
		    	if(conexiongene.get(i).getAterrizamiento().equals(Generadores.conexion1)) {
		    		
		    		z0+=conexiongene.get(i).getImpedanciaAterrizamiento();
		    	
		    	}
		    	else if(conexiongene.get(i).getAterrizamiento().equals(Generadores.conexion2)){
		    		
		    		z0+=10000000;
		    	}
		    	
		    	bordes0.add(new WeightEdeges(x,y,z0));
		    	bordes0.add(new WeightEdeges(y,x,z0));
		 		
		 	}
		 	
		 	grafo1=new WeightedGraph<>(barras,bordes1);
	    	grafo2= new WeightedGraph<>(barras,bordes2);
	    	grafo0= new WeightedGraph<>(barras,bordes0);
//	    	
	    	List<List<Edges>> borde1= grafo1.getConexiones();
	    	List<List<Edges>> borde2= grafo2.getConexiones();
	    	List<List<Edges>> borde0= grafo0.getConexiones();
	   
	  
	    	double [][] yBarraSecuencia1= obtenerMatrizAdyacencia(borde1);
	    	double [][] yBarraSecuencia2=  obtenerMatrizAdyacencia(borde2);
	    	double [][] yBarraSecuencia0= obtenerMatrizAdyacencia(borde0);
//	    	
	    	double [][] zBarraSecuencia1= Zbarra.getZbarra(yBarraSecuencia1);
	    	double [][] zBarraSecuencia2= Zbarra.getZbarra(yBarraSecuencia2);
	    	double [][] zBarraSecuencia0= Zbarra.getZbarra(yBarraSecuencia0);
	    	
	    	if(monofasica) {
	        	if(tipoElementoFallado.charAt(0)=='B') {
		    		
		    		FallaAsimetricas calculoFallaAsimetrica= new FallaAsimetricas(zBarraSecuencia0,zBarraSecuencia1,zBarraSecuencia2,
		    				barras,conexiones,conexiones1,barraFallada,conexiongene);
		    		
		    		angCorrientePuntoFallaFaseA=barraFallada.getAngCorrientePuntoFallaFaseA();
					angCorrientePuntoFallaFaseB=barraFallada.getAngCorrientePuntoFallaFaseB();
					angCorrientePuntoFallaFaseC=barraFallada.getAngCorrientePuntoFallaFaseC();
					
					magCorrientePuntoFallaFaseA=barraFallada.getMagcorrientePuntoFallaFaseA();
					magCorrientePuntoFallaFaseB=barraFallada.getMagcorrientePuntoFallaFaseB();
					magCorrientePuntoFallaFaseC=barraFallada.getMagcorrientePuntoFallaFaseC();
		    		
		    	}
		    	
		    	else if(tipoElementoFallado.charAt(0)=='L') {
		    		
		    		
		    		FallaAsimetricaLineas calculaFallaAsimetricaLinea= new FallaAsimetricaLineas(zBarraSecuencia0, zBarraSecuencia1, 
		    				zBarraSecuencia2, barras, conexiones, conexiones1, conexiongene, lineaFallada);

					angCorrientePuntoFallaFaseA= lineaFallada.getAngCorrientePuntoFallaFaseA();
					angCorrientePuntoFallaFaseB= lineaFallada.getAngCorrientePuntoFallaFaseB();
					angCorrientePuntoFallaFaseC= lineaFallada.getAngCorrientePuntoFallaFaseC();
					
					magCorrientePuntoFallaFaseA= lineaFallada.getMagcorrientePuntoFallaFaseA();
					magCorrientePuntoFallaFaseB= lineaFallada.getMagcorrientePuntoFallaFaseB();
					magCorrientePuntoFallaFaseC= lineaFallada.getMagcorrientePuntoFallaFaseC();
		    		
		    	}
	    	}
	    	else if(bifasicaATierra) {
	    		
	    		if(tipoElementoFallado.charAt(0)=='B') {
	    			
	    			FallaLineaALineaTierra biaTierra= new FallaLineaALineaTierra(zBarraSecuencia0, zBarraSecuencia1, zBarraSecuencia2,
	    					barras, conexiones, conexiones1, barraFallada, conexiongene);
	    			
	    			angCorrientePuntoFallaFaseA=barraFallada.getAngCorrientePuntoFallaFaseA();
					angCorrientePuntoFallaFaseB=barraFallada.getAngCorrientePuntoFallaFaseB();
					angCorrientePuntoFallaFaseC=barraFallada.getAngCorrientePuntoFallaFaseC();
					
					magCorrientePuntoFallaFaseA=barraFallada.getMagcorrientePuntoFallaFaseA();
					magCorrientePuntoFallaFaseB=barraFallada.getMagcorrientePuntoFallaFaseB();
					magCorrientePuntoFallaFaseC=barraFallada.getMagcorrientePuntoFallaFaseC();
	    			
	    			
	    			
	    		}
	    		else if(tipoElementoFallado.charAt(0)=='L') {
	    			
	    			
	    			FallaLineaALineaTierraEnLinea bifasicaATierraEnLinea= new FallaLineaALineaTierraEnLinea(zBarraSecuencia0, zBarraSecuencia1, 
	    					zBarraSecuencia2, barras, conexiones, conexiones1, conexiongene, lineaFallada);
	    			
	    			angCorrientePuntoFallaFaseA= lineaFallada.getAngCorrientePuntoFallaFaseA();
					angCorrientePuntoFallaFaseB= lineaFallada.getAngCorrientePuntoFallaFaseB();
					angCorrientePuntoFallaFaseC= lineaFallada.getAngCorrientePuntoFallaFaseC();
					
					magCorrientePuntoFallaFaseA= lineaFallada.getMagcorrientePuntoFallaFaseA();
					magCorrientePuntoFallaFaseB= lineaFallada.getMagcorrientePuntoFallaFaseB();
					magCorrientePuntoFallaFaseC= lineaFallada.getMagcorrientePuntoFallaFaseC();
	    			
	    			
	    		}
	    		
	    	}
	
	    	
		}
		
		
		public double [][] obtenerMatrizAdyacencia(List<List<Edges>> borde){
			
			double [] []resultado = new double[borde.size()-1][borde.size()-1];
			
			
			for(int i=1;i<borde.size();i++) {
				for(int j=0;j<borde.get(i).size();j++) {
					
					int u= borde.get(i).get(j).getU();
					int v=borde.get(i).get(j).getV();
					double peso=((WeightEdeges)borde.get(i).get(j)).getWeight();
					
					
					resultado[u-1][u-1]-=1/peso;
					
					if(v!=0) {
						resultado[u-1][v-1]=1/peso;
					}
					
				}
			}
			
		
			return resultado;
			
		}
		
		
		public void imprimirGrafo(List<List<Edges>> bordes,List<Barras> vertices) {
			
		
			for(int i=0;i<vertices.size();i++) {
				System.out.print("Barra "+vertices.get(i).getNombreBarra()+" : ");
				for(int j=0;j<bordes.get(i).size();j++) {
				
					
					System.out.print("(Barra "+bordes.get(i).get(j).getU()+" , "+" Barra "+
					bordes.get(i).get(j).getV()+" , "+((WeightEdeges)bordes.get(i).get(j)).getWeight()+" )");
					
				}
				System.out.println();
			}
			
			
			
		}
		
		public void imprimir(double [][] resultado){
			
			for(int i=0;i<resultado.length;i++) {
				for(int j=0;j<resultado.length;j++) {
					
					System.out.print(resultado[i][j]+" ");
				}
				System.out.println();
			}
			
			
			
		}
		
		
		public MyGraph<Barras> getGraph() {     
			
			List<Edges> edges = new ArrayList<>();
		      
		      for(int i=0;i<conexiones.size();i++) {
		    	  
		    	int x= barras.indexOf(conexiones.get(i).getBarra1());
		    	 int y= barras.indexOf(conexiones.get(i).getBarra2());
		    	 
		    	 edges.add(new Edges(x,y));
		    	 edges.add(new Edges(y,x));
		      }
		      
		      for(int i=0;i<conexiones1.size();i++) {
		    	  
		    	  int x= barras.indexOf(conexiones1.get(i).getBarra1());
		    	 int y= barras.indexOf(conexiones1.get(i).getBarra2());
		    	 
		    	 edges.add(new Edges(x,y));
		    	 edges.add(new Edges(y,x));
		      }
		      
		 
		      MyGraph<Barras> graph = new MyGraph<>(barras, edges);
		      return graph;
		}
		
		
		public void displayConnectedComponents() { 
			  
			  System.out.println("The connected components are " + getGraph().getConnectedComponents());
		      
		}
		public  void imprimir(int [] resultado) {
			
			
			for(int i=0;i<resultado.length;i++) {
				System.out.println(resultado[i]+" ");
			}
		}
		
	}

}


