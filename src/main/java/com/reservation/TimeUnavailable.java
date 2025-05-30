package com.reservation;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class TimeUnavailable {

    public static void show() {
        try {
            Parent root = FXMLLoader.load(TimeUnavailable.class.getResource("time_unavailable.fxml"));
            Stage stage = new Stage();
            stage.setTitle("No Available Times");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error showing TimeUnavailable: " + e.getMessage());
        }
    }
}

