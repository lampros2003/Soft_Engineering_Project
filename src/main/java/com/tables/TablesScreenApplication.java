package com.tables;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

/**
 * Main application class for the Tables Screen.
 *
 * To run this application without warnings, use the VM argument:
 * --enable-native-access=javafx.graphics
 */
public class TablesScreenApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            URL fxmlUrl = getClass().getResource("/com/tables/TablesScreen.fxml");
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file at: /com/tables/TablesScreen.fxml");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root);
            
            // For CSS, you'll need to add this style file in resources directory
            URL cssUrl = getClass().getResource("/styles/table-view.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Cannot find CSS file at: /styles/table-view.css");
            }
            
            // Set up the stage
            primaryStage.setTitle("Restaurant Table Selection");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Get controller instance for further customization if needed
            TablesScreenController controller = loader.getController();
            System.out.println("Tables Screen loaded successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Tables Screen Application...");
        System.out.println("To avoid warnings, run with VM argument: --enable-native-access=javafx.graphics");

        launch(args);
    }
}
