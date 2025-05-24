package com.payBill.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainSceneController {

    @FXML
    private Button orderBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private void btnORDERClicked(ActionEvent event) {
        System.out.println("Order button clicked!");
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        SceneSwitching.switchScene(stage, "/view/MenuScreen.fxml");
    }

    @FXML
    void btnLOGINClicked(ActionEvent event) {
        System.out.println("Login button clicked!");
    }
}
