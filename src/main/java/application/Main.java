package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage s) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SP.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        s.setTitle("Power System App - Professional Edition");
        s.setScene(scene);

        s.setMinWidth(1024);
        s.setMinHeight(768);
        s.setResizable(true);

        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
