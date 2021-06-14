package grafos;
import java.util.ArrayList;
import java.util.List;
import javafx.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class GrafoDinaminco1 extends Application{
	
	private Button btn= new Button("Componentes Conectados");
	private BorderPane p1= new BorderPane();
	HBox hbox= new HBox(4);Text texto= new Text();
	private Vista vista= new Vista();
	
	@Override
	public void start(Stage a) throws Exception {
		
		
		hbox.getChildren().addAll(btn,texto);
		p1.setCenter(vista);
		p1.setBottom(hbox);
		hbox.setAlignment(Pos.CENTER);
		Scene scene= new Scene(p1,600,600);
		a.show();a.setScene(scene);
		
		btn.setOnAction(e->{
			
		});
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}


class Vista extends Pane{
	
	private List<Vertex> vertices= new ArrayList<>();
	private List<Bordes> edges= new ArrayList<>();
	 private Vertex startV = null;
	    private boolean ponerLinea = false;
	    private double finalX, finalY;

	public Vista() {
		//repaint();
		this.setOnMouseClicked(e->{
			
			if(e.getButton()==MouseButton.PRIMARY) {
				double x=e.getX();double y=e.getY();
				if(!dentroOtroCirculo(x,y)) {
					
					if(ponerLinea) {
						ponerLinea=false;
						repaint();
						return;
					}
					
					vertices.add(new Vertex(x,y));
					repaint();
					return;
					
				}
				
				Vertex v= contiene(x,y);
				
				if(!ponerLinea && v!=null) {
				
					startV=v;
					finalX=x;finalY=y;
					ponerLinea=true;
					
				}
				
				if(ponerLinea&&v!=null&&!sonIguales(v,startV)) {
					
					edges.add(new Bordes(startV,v));
					System.out.println(edges.size());
					ponerLinea=false;
					repaint();
					
				}
				
			}
			
			if(e.getButton()==MouseButton.SECONDARY) {
				
				double x=e.getX();double y=e.getY();
				
				Vertex v= contiene(x,y);
				
				
				if(v!=null) {
					
					vertices.remove(v);
					removerBordes(v);
					repaint();
					return;
				}
		
			}
			
		});
		
		
		this.setOnMouseMoved(e->{
			double x=e.getX();
			double y=e.getY();
			Vertex v= contiene(x,y);
			if(e.isControlDown()) {
				ponerLinea=false;
				if(v!=null) {
					v.setX(x);
					v.setY(y);
					repaint();
				}
				
			}
			else if(ponerLinea) {
				finalX=x;finalY=y;
				repaint();
			}
			
		});
	
	}
	
	public void removerBordes(Vertex v) {
		
		for(int i=0;i<edges.size();i++) {
			
			if(edges.get(i).v1==v || edges.get(i).v2==v) {
				
				edges.remove(edges.get(i));
				--i;
			}
			
		}
		
		
	}
	
	public void repaint() {
		this.getChildren().clear();
		dibujarBordes();
		dibujarVertices();
	}
	
	public boolean sonIguales(Vertex v1,Vertex v2) {
		
		return v1.getX()==v2.getX()&&v1.getY()==v2.getY();
		
		
		
	}
	
	
	public void dibujarVertices() {
		
		for(int i=0;i<vertices.size();i++) {
			
			double x=vertices.get(i).getX();double y= vertices.get(i).getY();
			
			Circle circulo= new Circle();
			circulo.setCenterX(x);
			circulo.setCenterY(y);
			circulo.setRadius(Vertex.radio);
			circulo.setFill(Color.WHITE);circulo.setStroke(Color.BLACK);
			this.getChildren().add(circulo);
			
			
			
		}
		
	}
	
	public void dibujarBordes() {
		
		if(ponerLinea) {
			this.getChildren().add(new Line(startV.getX(),startV.getY(),finalX,finalY));
		}
		
			
			for(int i=0;i<edges.size();i++) {
				
				this.getChildren().add(new Line(edges.get(i).v1.getX(),edges.get(i).v1.getY(),edges.get(i).v2.getX(),edges.get(i).v2.getY()));
			}
			
		
	}
	
	public boolean dentroOtroCirculo(double x,double y) {
		
		for(int i=0;i<vertices.size();i++) {
			double xx=vertices.get(i).getX();
			double yy= vertices.get(i).getY();
			
			double distancia=Math.sqrt((x-xx)*(x-xx)+(y-yy)*(y-yy));
			
			if(distancia<=2*Vertex.radio) {
				return true;
				
			}
		}
		
		return false;
		
	}

	public Vertex contiene(double x,double y) {
		
		for(int i=0;i<vertices.size();i++) {
			
			if(vertices.get(i).contienePunto(x, y)) {
				return vertices.get(i);
			}
			
		}
		
		return null;
	
	}
	
}

class Bordes{
	
	Vertex v1;
	Vertex v2;
	
	public Bordes(Vertex v1,Vertex v2) {
		this.v1=v1;this.v2=v2;
	}
}

class Vertex{
	
	 static final int radio=20;
	private double x;
	private double y;
	
	Vertex(double x,double y){
		this.x=x;
		this.y=y;
		
	}
	
	public double getX() {
		return x;
	}
	
	public int getRadio() {
		return radio;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x=x;
				
	}
	
	public void setY(double y) {
		this.y=y;
	}
	
	public boolean contienePunto(double x,double y) {
		return Math.sqrt((getX()-x)*(getX()-x)+(getY()-y)*(getY()-y))<=radio;
	}
	
	
}
