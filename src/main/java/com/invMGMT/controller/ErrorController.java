package com.invMGMT.controller;

import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorController extends Screen {
    @FXML Text errorText;
    public void showErrorMessage(String message){
        errorText.setText(message);
    }
    public void backButton(ActionEvent event){
        ((InventoryManager) this.manager).init((Stage)((Node) event.getSource()).getScene().getWindow());
    }
}
