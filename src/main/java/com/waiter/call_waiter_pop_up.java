package com.waiter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class call_waiter_pop_up {

    @FXML
    private Button accept_button;

    @FXML
    private Button decline_button;

    @FXML
    void waiter_accepts(ActionEvent event) {
        System.out.println("waiter_accepts");
    }

    @FXML
    void waiter_declines(ActionEvent event) {
        System.out.println("waiter_declines");
    }

}
