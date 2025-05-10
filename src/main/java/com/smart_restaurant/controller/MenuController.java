package com.smart_restaurant.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SceneSwitching;

public class MenuController {

    @FXML
    private Button backToMainScreen;

    @FXML
    void navigateToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/view/MainScreen.fxml");
    }

    @FXML
    private Button orderBtn;

    @FXML
    void placeOrder(ActionEvent event) {
        System.out.println("Place Order");
    }

    @FXML
    private Button commentsBtn;

    @FXML
    void addComments(ActionEvent event) {
        System.out.println("Add Comments");
    }

    @FXML
    private Button allergenInfoBtn;

    @FXML
    void allergenInfo(ActionEvent event) {
        System.out.println("Pressed Allergen Information");
    }

    @FXML
    private Button callWaiterBtn;

    @FXML
    void callWaiter(ActionEvent event) {
        System.out.println("Call Waiter");
    }

}