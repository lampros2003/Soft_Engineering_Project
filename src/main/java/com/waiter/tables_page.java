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
    private void Select_Table(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int tableNumber = Integer.parseInt(buttonId.replace("table", ""));


        System.out.println("Button pressed, table number:"+tableNumber);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Manage_Waiter_Class manager = new Manage_Waiter_Class();
        manager.Search_Tables_Order(tableNumber,stage);
    }

    public void display(Stage stage) {
        System.out.println("Displaying tables_page");
        try {
            SceneSwitching.switchSceneR(stage, "/waiter/tables_page.fxml",this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

