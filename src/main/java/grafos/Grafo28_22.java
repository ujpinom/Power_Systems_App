package grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Grafo28_22 extends Application {

	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage a) throws Exception {
		
		Scene scene = new Scene(new CirclessPane(),400,400);
		a.show();a.setScene(scene);a.setTitle("Conected Circles");
	}

}

class CirclessPane extends Pane{
	private int radio=20;
	public CirclessPane() {
		this.setOnMouseClicked(e->{
			
			
			if(e.getButton()==MouseButton.PRIMARY) {
			if(!insideAnotherCircle(e.getX(),e.getY())) {
				
				this.getChildren().add(new Circle(e.getX(),e.getY(),radio));
				setColor();
			}
		}
		
			else if(e.getButton()==MouseButton.SECONDARY) {
				if(insideAnotherCircle(e.getX(),e.getY())) {
					
					for(Node circle:this.getChildren()) {
						
						if(circle.contains(e.getX(), e.getY())) {
			
							this.getChildren().remove(circle);
							setColor();
							break;
						}
					}
				}
			}
			
	});
		
		this.setOnMouseDragged(e->{
			
			if(insideAnotherCircle(e.getX(),e.getY())) {
				
				for(Node circle:this.getChildren()) {
					
					if(circle.contains(e.getX(), e.getY())) {
		
						((Circle) circle).setCenterX(e.getX());
						((Circle) circle).setCenterY(e.getY());
						setColor();
					}
				}
			}
		});
	}
	
	public boolean insideAnotherCircle(double x,double y) {
		
		for(Node circle:this.getChildren()) {
			
			if(circle.contains(x, y)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public void setColor() {
		if(this.getChildren().size()==0) {
			return; //No elementos dibujados;
		}
		
		else {
			List<Edges> bordes= new ArrayList<>();
			
			for(int i=0;i<this.getChildren().size();i++) {
				for(int j=i;j<this.getChildren().size();j++) {
					if(j!=i) {
						
						if(isCirclesConectec((Circle)(this.getChildren().get(i)),(Circle)(this.getChildren().get(j)))) {
							bordes.add(new Edges(i,j));bordes.add(new Edges(j,i));
						}
					}
				}
			}
			
			Graph<Node> lista= new UnweightedGraph<>(this.getChildren(),bordes);
			List<List<Integer>> componentesConectados= lista.componentesConectados();
			ObservableList<Node> circulos= this.getChildren();
			
			for(int i=0;i<componentesConectados.size();i++) {
				double r = Math.random();double g = Math.random();double b = Math.random();double a = Math.random();
				for(int j=0;j<componentesConectados.get(i).size();j++) {
					int x= componentesConectados.get(i).get(j);
				
				((Circle)(circulos.get(x))).setFill(new Color(r,g,b,a));
				((Circle)(circulos.get(x))).setStroke(Color.BLACK);;
					
				}
			}
			componentesConectados.clear();
			
		}
			
	}
	
	public boolean isCirclesConectec(Circle c1,Circle c2) {
		
		double x1= c1.getCenterX();double y1=c1.getCenterY();
		double x2=c2.getCenterX();double y2=c2.getCenterY();
		double distancia= Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		
		if(distancia>2*radio) {
			return false;
		}
		else return true;
		
	}

	
	
	
}
