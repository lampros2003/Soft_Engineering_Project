package com.schedule;

import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PopUp{
    public static void Popup(String fxmlPath) {
        try {
            // 1. Load the FXML
            FXMLLoader fxmlLoader = new FXMLLoader(PopUp.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            // 2. Create a new Stage
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
            popupStage.setTitle("Popup Window");

            // 3. Set the scene
            popupStage.setScene(new Scene(root));

            // 4. Show the window and wait until it is closed
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace(); // Handle it properly in real apps
        }
    }
}


