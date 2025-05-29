package com.payBill.controller;

import com.common.Order;
import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BankErrorPageController extends Screen {
    @FXML private Text errorText;
    Order order;
    public void backButton(ActionEvent event){
        System.out.println("back pressed");
        PaymentManager pm=new PaymentManager();
        pm.init((Stage)((Node) event.getSource()).getScene().getWindow(),this.order);
    }
    public void ShowErrorMessage(String error){
        errorText.setText(error);
    }
    public void showErrorAndBack(String error, Order order){
        errorText.setText(error);
        this.order=order;
    }
}
