package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SceneSwitching;

public class MenuController {

    @FXML
    private Button testBtn;

    @FXML
    private Button backToMainScreen;

    @FXML
    void btnClicked(ActionEvent event) {
        System.out.println("Test button clicked!");
    }

    @FXML
    void nagivateToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/view/MainScreen.fxml");
    }

}
