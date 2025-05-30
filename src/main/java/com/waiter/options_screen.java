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
    int table_number;
    FXMLLoader loader;
    @FXML
    private Text Title;

    @FXML
    private Pane Edit_button;

    @FXML
    private Pane cancel_button;

    public void Display(int tableNumber,Stage stage) {
        System.out.println("Displaying options screen");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/options_page.fxml");
            this.loader.setController(this);

            Title = (Text) loader.getNamespace().get("Title");
            if (Title != null) {
                Title.setText("TABLE-" + tableNumber);
                table_number = tableNumber;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Manage_Waiter_Class manager = new Manage_Waiter_Class();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        manager.Get_Order_From_DB(table_number,stage);
    }

}
