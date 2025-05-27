package com.waiter;

import com.mainpackage.SceneSwitching;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class options_screen {

    @FXML
    private Pane Edit_button;

    @FXML
    private Pane cancel_button;

    public static void Display(Stage stage) {
        System.out.println("Displaying options screen");
        SceneSwitching.switchScene(stage,"/waiter/options_page.fxml");
    }

    @FXML
    void clicked_Cancel(MouseEvent event) {
        System.out.println("Cancel button clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/waiter/TABLES_PAGE.fxml");
    }

    @FXML
    void clicked_edit(MouseEvent event) {
        System.out.println("Edit button clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "waiter/menu_screen_for_phone.fxml");
    }

}
