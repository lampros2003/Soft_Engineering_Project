package com.waiter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class tables_page {

    @FXML
    private Button table1;

    @FXML
    private Button table2;

    @FXML
    private Button table3;

    @FXML
    private Button table4;

    @FXML
    private Button table5;

    @FXML
    void get_table(ActionEvent event) {
        System.out.println("Button pressed");
    }

}

