package com.menu;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MenuController {
    
    @FXML
    private void backToMain(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/com.mainUI/MainScreen.fxml");
    }
}
