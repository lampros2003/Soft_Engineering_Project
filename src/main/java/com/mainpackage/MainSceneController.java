package com.mainpackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private Button orderBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private void btnORDERClicked(ActionEvent event) {
        System.out.println("Order button clicked!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/com.menu/MenuScreen.fxml");
    }

    @FXML
    void btnLOGINClicked(ActionEvent event) {
        System.out.println("Login button clicked!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/com.login/LoginScreen.fxml");
    }
}
