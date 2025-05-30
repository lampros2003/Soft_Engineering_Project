package com.waiter;

import com.mainpackage.SceneSwitching;
import com.menu.MenuWindow;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class notification_window {
    FXMLLoader loader;

    @FXML
    private Text Title;
    @FXML
    private Pane create_order_button;

    @FXML
    void select_make_order(MouseEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MenuWindow menuWindow = new MenuWindow();
        menuWindow.displayOrderPage_for_waiter(stage);
    }

    public void Display(int tableNumber, Stage stage) {
        System.out.println("Displaying no_order_screen");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/no_order_options_page.fxml",this);
            
            Title = (Text) loader.getNamespace().get("Title");
            if (Title != null) {
                Title.setText("TABLE-" + tableNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}