package com.waiter;

import javafx.scene.text.Text;
import com.mainpackage.SceneSwitching;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class options_screen {
    Manage_Waiter_Class manager = new Manage_Waiter_Class();
    int table_number = 3;
    FXMLLoader loader;
    @FXML
    private Text Title;

    @FXML
    private Pane Edit_button;

    @FXML
    private Pane cancel_button;

    public void Display(int tableNumber,Stage stage) {
        System.out.println("Displaying options screen");
        this.table_number = tableNumber;
        System.out.println(" Table Number:"+this.table_number);
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/options_page.fxml",this);


            Title = (Text) loader.getNamespace().get("Title");
            if (Title != null) {
                Title.setText("TABLE-" + tableNumber);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clicked_Cancel(MouseEvent event) {
        System.out.println("Cancel button clicked "+"Table Number:"+this.table_number);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        manager.cancelOrder(this.table_number);
    }

    @FXML
    void clicked_edit(MouseEvent event) {
        System.out.println("Edit button clicked "+"Table Number:"+this.table_number);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        manager.Get_Order_From_DB(this.table_number,stage);
        tables_page tables_page = new tables_page();
        tables_page.display(stage);
    }

}
