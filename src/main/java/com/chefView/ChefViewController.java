package com.chefView;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;


public class ChefViewController {

    @FXML
    private void navigateToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
    }


}
