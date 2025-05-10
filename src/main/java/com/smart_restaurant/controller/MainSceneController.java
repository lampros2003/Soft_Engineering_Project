package com.smart_restaurant.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.stage.Stage;
import util.SceneSwitching;

public class MainSceneController {

    @FXML
    private Button orderBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private void btnORDERClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/view/MenuScreen.fxml");
    }

    @FXML
    void btnLOGINClicked(ActionEvent event) {
        System.out.println("Login button clicked!");
    }
}
