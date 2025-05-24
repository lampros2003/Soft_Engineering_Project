package com.adminView;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MenuModificationScreen {
    @FXML
    private void onModifyMenu(ActionEvent event) {
        System.out.println("Modify Menu button clicked");
    }

    @FXML
    private void redirectToDashboard(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/adminView/dummyDashboard.fxml");
    }
}
