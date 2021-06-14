package grafos;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GrapfView extends BorderPane{
	
	private Graph<? extends Displayable> grafo;
	
	private Group grupo= new Group();
	
	
	
	public GrapfView(Graph<? extends Displayable> grafo) {
		this.grafo=grafo;
		this.setCenter(grupo);
		repaintgrafo();
	}
	
	public void repaintgrafo() {
		grupo.getChildren().clear();
		List<? extends Displayable> lista= grafo.getVertices();
		
		for(int i=0;i<lista.size();i++) {
			double x= lista.get(i).getX();
			double y= lista.get(i).getY();
			String nombre= lista.get(i).getNombre();
			
			grupo.getChildren().add(new Circle(x, y, 16));
			grupo.getChildren().add(new Text(x-8, y-18, nombre));
			
			List<Integer> list= grafo.getVecinos(i);
			
			for(int j:list) {
				double x2=grafo.getVertex(j).getX();
				double y2=grafo.getVertex(j).getY();
				String nombre2= grafo.getVertex(j).getNombre();
				grupo.getChildren().add(new Circle(x2, y2, 16));
//				grupo.getChildren().add(new Text(x2-8, y2-18, nombre));
				
				grupo.getChildren().add(new Line(x,y,x2,y2));
			}
			
		}
		
	}
	

}
