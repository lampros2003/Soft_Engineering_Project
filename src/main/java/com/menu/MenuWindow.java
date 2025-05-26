package com.menu;

import com.adminView.ManageMenuClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import com.mainpackage.SceneSwitching;

import java.io.IOException;


public class MenuWindow {
    public static int tableNumber = 1; // Example table number, replace with actual value
    private final static ManageMenuClass menuManager = new ManageMenuClass(tableNumber);

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
            menuManager.callWaiter();
    }
}
