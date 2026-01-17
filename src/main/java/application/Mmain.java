package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mmain extends Application {
	
	
	@Override
	public void start(Stage s) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MatricesSeq.fxml"));
        Parent root = fxmlLoader.load();
		

		Scene scene= new Scene(root);
		
		s.setTitle("Paint2_0");
		s.setScene(scene);
		s.show();
		
		MatricesController cont= fxmlLoader.getController();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
