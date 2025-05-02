// Imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// App Class
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScene.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setTitle("SmartRestaurant");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setResizable(false);

    }

    // Main Method
    public static void main(String[] args) {
        launch(args);
    }
}