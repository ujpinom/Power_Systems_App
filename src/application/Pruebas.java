package application;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;


public class Pruebas extends Application  {

	 @Override 
	   public void start(Stage stage) { 
	      //Creating a Path 
	      Path path = new Path(); 
	       
	      //Moving to the starting point 
	      MoveTo moveTo = new MoveTo(108, 71); 
	        
	      //Creating 1st line 
	      LineTo line1 = new LineTo(321, 161);  
	       
	      //Creating 2nd line 
	      LineTo line2 = new LineTo(126,232);       
	       
	      //Creating 3rd line 
	      LineTo line3 = new LineTo(232,52);  
	       
	      //Creating 4th line 
	      LineTo line4 = new LineTo(269, 250);   
	       
	      //Creating 4th line 
	      LineTo line5 = new LineTo(108, 71);  
	       
	      //Adding all the elements to the path 
	      path.getElements().add(moveTo); 
	      path.getElements().addAll(line1, line2, line3, line4, line5);        
	         
	      //Creating a Group object  
	      Group root = new Group(path); 
	         
	      //Creating a scene object 
	      Scene scene = new Scene(root, 600, 300);  
	      
	      //Setting title to the Stage 
	      stage.setTitle("Drawing an arc through a path");
	      
	      //Adding scene to the stage 
	      stage.setScene(scene);
	      
	      //Displaying the contents of the stage 
	      stage.show();         
	   } 
	   public static void main(String args[]){ 
	      launch(args); 
	   }
	
	
//	public static void main(String[] args) {
//		
//		List<Integer> indices= new ArrayList<>();
//		
//		double [][] a= {{1,3,4,5,7},{5,6,7,8,9},{23,45,32,56,67},{23,34,45,34,23}};
//		
//		double [][] jacobiana= {{1,1,1,1,1,1,1,1 },
//			      {-19.078144,45.44290918, -0.0, -26.364765180000003, -3.8156290000000044, 8.881798779999992 ,
//		       0.0 ,-5.272952220000003 },
//		{-25.847809,-0.0,41.26870756,-15.42089856 ,-5.1695610000000025 ,0.0 ,8.132793899999996, -3.0841791000000027},
//		      {-0.0, -26.364765180000003, -15.42089856, 41.785663740000004, 0.0, -5.272952220000003 ,-3.0841791000000027,
//		       8.691418653599994},
//		{12,12,12,12,12,12,12,12},
//		{3.8156290000000044, -9.088581220000007, -0.0 ,5.272952220000003, -19.078144 ,44.228996820000006,-0.0 ,-26.364765180000003 },
//		{5.1695610000000025,-0.0,-8.253740100000005 ,3.0841791000000027 ,-25.847809 ,-0.0,40.45896844, -15.42089856 },
//		{-0.0 ,5.272952220000003 ,3.0841791000000027,-8.357131320000006, -0.0, -26.364765180000003, -15.42089856 ,43.2438103704}};
//		
//		RealMatrix matrizA= new Array2DRowRealMatrix(jacobiana);
//		
//		
//		SingularValueDecomposition svd = new SingularValueDecomposition(matrizA);
//		DecompositionSolver solver = svd.getSolver();
//		
//		
//		RealMatrix pinv = solver.getInverse();
//		
//		
//		System.out.println(pinv);
//		
//		
//		
//		
//		
//		
////		for(int i=0;i<conversion.length;i++) {
////			for(int j=0;j<conversion[0].length;j++) {
////				
////				System.out.print(conversion[i][j]+" ");
////			}
////			
////			System.out.println();
////		}
//		
//
//	}
//
//	@Override
//	public RealMatrix getInverse() throws SingularMatrixException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isNonSingular() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public RealVector solve(RealVector arg0) throws SingularMatrixException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public RealMatrix solve(RealMatrix arg0) throws SingularMatrixException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
