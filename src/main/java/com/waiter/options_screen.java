package com.waiter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class options_screen {

    @FXML
    private Pane Edit_button;

    @FXML
    private Pane cancel_button;

    @FXML
    void clicked_Cancel(MouseEvent event) {
        System.out.println("Cancel button clicked");
    }

    @FXML
    void clicked_edit(MouseEvent event) {
        System.out.println("Edit button clicked");
    }

}
