package com.payBill.controller;

import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AfterPaymentController extends Screen {
    @FXML private TextField emailInput;
    @FXML private Text warningText;
    private PaymentManager manager;
    public void sendReceiptButton(ActionEvent event) {
        String emailAddress=emailInput.getText();
        manager.sendEmail(emailAddress,this);
    }
    public void showWarning(){
        warningText.setText("please provide a valid email address");
    }
    public void getManager(PaymentManager manager){
        this.manager=manager;
    }
}
