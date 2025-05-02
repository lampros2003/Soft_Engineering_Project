package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainSceneController {

    @FXML
    private Button orderBtn;

    @FXML
    void btnORDERClicked(ActionEvent event) {
        System.out.println("Order button clicked!");
    }

}
