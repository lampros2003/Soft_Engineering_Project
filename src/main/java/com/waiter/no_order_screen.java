package com.waiter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class no_order_screen {

    @FXML
    private Pane create_order_button;

    @FXML
    void clicked_create(MouseEvent event) {
        System.out.println("clicked create");
    }

}