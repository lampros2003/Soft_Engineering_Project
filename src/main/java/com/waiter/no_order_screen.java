package com.waiter;

import com.mainpackage.SceneSwitching;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class no_order_screen {
    FXMLLoader loader;

    @FXML
    private Text Title;
    @FXML
    private Pane create_order_button;

    @FXML
    void clicked_create(MouseEvent event) {
        System.out.println("clicked create");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/waiter/menu_screen_for_phone.fxml");
    }

    public void Display(int tableNumber, Stage stage) {
        System.out.println("Displaying no_order_screen");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/no_order_options_page.fxml");
            this.loader.setController(this);
            
            Title = (Text) loader.getNamespace().get("Title");
            if (Title != null) {
                Title.setText("TABLE-" + tableNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}