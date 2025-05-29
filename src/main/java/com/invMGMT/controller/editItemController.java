package com.invMGMT.controller;

import com.common.Ingredient;
import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class editItemController extends Screen {
    @FXML TextField nameIn;
    @FXML TextField quantityIn;
    @FXML TextField allergenIn;
    @FXML Text editText;
    public void init(Ingredient ing){
        editText.setText(ing.getName());
        nameIn.setText(ing.getName());
        quantityIn.setText(String.valueOf(ing.getQuantity()));
        allergenIn.setText(ing.getAllergen()==null? "" :ing.getAllergen());
    }
    public void confirmChanges(){
        InventoryManager temp=(InventoryManager) this.manager;
        temp.passModifications(nameIn.getText(),quantityIn.getText(),allergenIn.getText());
    }
    public void cancelChanges(ActionEvent event){
        InventoryManager temp=(InventoryManager) this.manager;
        temp.init((Stage)((Node) event.getSource()).getScene().getWindow());
    }
}
