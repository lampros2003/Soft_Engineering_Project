package com.waiter;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        Stage cstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        options_screen op=new options_screen();
        op.Display(cstage);
    }

}

