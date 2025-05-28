package com.invMGMT.controller;

import com.common.Ingredient;
import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;

public class ItemDetailsController extends Screen {
    private InventoryManager manager;
    @FXML public Text itemDetails;
    @FXML public Text itemName;
    Ingredient ing;

    public void init(Ingredient ing){
        this.ing=ing;
        itemName.setText(ing.getName());
        itemDetails.setText(ing.getInfo());
    }
    public void editItem(){
        System.out.println("edit");
        manager.editItem(itemName.getText());
    }
    public void removeItem(ActionEvent event){
        System.out.println("remove: "+itemName.getText());
        manager.removeItem(itemName.getText());
        manager.init((Stage) ((Node) event.getSource()).getScene().getWindow());
    }
    public void setManager(InventoryManager manager){
        this.manager=manager;
    }
}
