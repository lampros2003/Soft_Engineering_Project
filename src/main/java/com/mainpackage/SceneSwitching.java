package com.mainpackage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitching {

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitching.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
        }
    }
    public static FXMLLoader switchSceneR(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitching.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            return loader;
        } catch (IOException e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }
    public static FXMLLoader switchSceneR(Stage stage, String fxmlPath,Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitching.class.getResource(fxmlPath));
            loader.setController(controller);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            return loader;
        } catch (IOException e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }
}