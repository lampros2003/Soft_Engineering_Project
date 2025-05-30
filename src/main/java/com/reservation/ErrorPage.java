package com.reservation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorPage {

    private static String lastError = "An unknown error occurred.";

    public static void display(String message) {
        lastError = message;
        try {
            FXMLLoader loader = new FXMLLoader(ErrorPage.class.getResource("/reservation/error_page.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load error page: " + e.getMessage());
        }
    }

    public static String getMessage() {
        return lastError;
    }
}

