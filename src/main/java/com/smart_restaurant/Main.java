// Software Engineering Project
// SmartRestaurant
// UPatras Spring Semester 2024-2025

package com.smart_restaurant;
// Imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// App Class
public class Main extends Application {
    // Main setup code for GUI
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        // Load UI from fxml file
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        // Construct the main Window
        Scene scene = new Scene(root);

        primaryStage.setTitle("SmartRestaurant");
        primaryStage.setScene(scene);
        // Show the main Window
        primaryStage.show();

        // Set window size unchangeable
        primaryStage.setResizable(false);

    }

    // Main Method
    public static void main(String[] args) {
        launch(args);
    }
}