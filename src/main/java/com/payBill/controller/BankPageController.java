package com.payBill.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class BankPageController {
    @FXML private TextField cardsNum;
    @FXML private TextField fullName;
    @FXML private TextField cvc;
    private PaymentManager manager;
    public void bankButton(ActionEvent event) throws IOException {
        manager.passCredentials(fullName.getText(),cardsNum.getText(),cvc.getText());
    }
    public void getManager(PaymentManager manager){
        this.manager=manager;
    }
}
