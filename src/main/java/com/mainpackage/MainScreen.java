package com.mainpackage;

import com.placeOrder.PlaceOrderManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;


public class MainScreen {

    @FXML
    private void btnORDERClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        PlaceOrderManager pm=new PlaceOrderManager();
        pm.init(stage);
    }

    @FXML
    private void btnLOGINClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/login/LoginScreen.fxml");

    }
}
