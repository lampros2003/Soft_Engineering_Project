
package com.reservation;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class DateUnavailable {

    public static void show() {
        try {
            Parent root = FXMLLoader.load(DateUnavailable.class.getResource("date_unavailable.fxml"));
            Stage stage = new Stage();
            stage.setTitle("No Available Dates");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error showing DateUnavailable: " + e.getMessage());
        }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        Navigation.back(
                (Stage) ((javafx.scene.Node) event.getSource())
                        .getScene()
                        .getWindow()
        );
    }

    public void handleOrderToGo(ActionEvent actionEvent) {
    }
}

