package com.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;


public class MenuWindow {
    @FXML
    void redirectToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
    }

    @FXML
    void placeOrder(ActionEvent event) {
        System.out.println("Place Order");
    }

    @FXML
    void addComments(ActionEvent event) {
        System.out.println("Add Comments");
    }

    @FXML
    void allergenInfo(ActionEvent event) {
        System.out.println("Pressed Allergen Information");
    }

    @FXML
    void callWaiter(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/RequestStatusWindow.fxml"));
            Parent root = loader.load();

            RequestStatusWindow controller = loader.getController();
            controller.getStatus();

            Stage stage = new Stage();
            stage.setTitle("Request Status");
            stage.setScene(new Scene(root));
            stage.show();

    }
}