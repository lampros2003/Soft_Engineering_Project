package com.invMGMT.controller;

import com.common.Ingredient;
import com.common.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddItemController extends Screen {
    @FXML Text editText;
    @FXML Button okButton;
    @FXML TextField nameIn;
    @FXML TextField quantityIn;
    @FXML TextField allergenIn;
    public void init(){
        editText.setText("Add Ingredient");
        okButton.setText("Add");
    }
    public void confirmChanges(){
        ((InventoryManager) this.manager).passAnswer(nameIn.getText(),quantityIn.getText(), allergenIn.getText(),"add");
    }
    public void cancelChanges(){}
}
