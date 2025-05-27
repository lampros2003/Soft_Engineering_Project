package com.invMGMT.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.swing.*;

public class ItemDetailsController {
    private InventoryManager manager;
    @FXML public Text itemDetails;
    @FXML public Text itemName;

    public void updateText(String name,String info){
        itemName.setText(name);
        itemDetails.setText(info);
    }
    public void editItem(){
        System.out.println("edit");
    }
    public void removeItem(ActionEvent event){
        System.out.println("remove: "+itemName.getText());
        manager.removeItem(itemName.getText());
        manager.init();
    }
    public void setManager(InventoryManager manager){
        this.manager=manager;
    }
}
