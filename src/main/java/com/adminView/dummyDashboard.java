package com.adminView;

import com.invMGMT.controller.InventoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;


public class dummyDashboard {

    @FXML
    void redirectToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
    }

    @FXML
    void manageMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/adminView/manageMenu.fxml");
    }
    @FXML
    void analytics(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/analytics/hello-view.fxml");
    }

    @FXML
    void tableManagement(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/com/tables/TablesScreen.fxml");
    }
    @FXML void inventoryManagement(ActionEvent event){
        InventoryManager im=new InventoryManager();
        im.init((Stage) ((Node) event.getSource()).getScene().getWindow());
    }


}
