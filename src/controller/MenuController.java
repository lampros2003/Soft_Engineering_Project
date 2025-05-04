package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    private Button testBtn;

    @FXML
    void btnClicked(ActionEvent event) {
        System.out.println("Test button clicked!");
    }

}
