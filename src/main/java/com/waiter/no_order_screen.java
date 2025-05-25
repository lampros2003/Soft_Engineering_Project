package com.waiter;

import com.mainpackage.SceneSwitching;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class no_order_screen {

    @FXML
    private Pane create_order_button;

    @FXML
    void clicked_create(MouseEvent event) {
        System.out.println("clicked create");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/waiter/menu_screen_for_phone.fxml");
    }

}