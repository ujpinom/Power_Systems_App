package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage s) throws IOException {
		
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SP.fxml"));
        Parent root = fxmlLoader.load();
		

		Scene scene= new Scene(root);
		
		s.setTitle("Análisis De Potencia");
		s.setScene(scene);
		s.show();
		s.setResizable(false);
		SPController cont= new SPController();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
	
