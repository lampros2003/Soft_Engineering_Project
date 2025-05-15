package com.mainpackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;


public class MainSceneController {

    @FXML
    private void btnORDERClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/menu/MenuScreen.fxml");
    }

    @FXML
    private void btnLOGINClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/login/LoginScreen.fxml");

    }
}
