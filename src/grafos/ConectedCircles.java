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

public class ConectedCircles extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage a) throws Exception {
		
		Scene scene = new Scene(new CirclesPane(),800,800);
		a.show();a.setScene(scene);a.setTitle("Conected Circles");
	}

}

class CirclesPane extends Pane{
	private int radio=20;
	public CirclesPane() {
		this.setOnMouseClicked(e->{
			
			if(!insideAnotherCircle(e.getX(),e.getY())) {
				
				this.getChildren().add(new Circle(e.getX(),e.getY(),radio));
				setColor();
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
			
			UnweightedGraph<Node>.SerchTree arbol= lista.bfs(0);
			
			boolean flag= this.getChildren().size()==arbol.getNumberOfVerticesFound();
			for(Node circle:this.getChildren()) {
				
				if(flag) {
					((Circle)circle).setFill(Color.RED);
				}
				
				else {
					((Circle)circle).setFill(Color.WHITE);
					((Circle)circle).setStroke(Color.BLACK);
				}
			}
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
