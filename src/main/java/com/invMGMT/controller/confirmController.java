package com.invMGMT.controller;

import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class confirmController extends Screen {
    @FXML Text changesText;
    private String name;
    private String quantity;
    private String allergen;
    public void init(String name,String quantity,String allergen){
        this.name=name;
        this.quantity=quantity;
        this.allergen=allergen;
        changesText.setText("name: "+name+" \nquantity: "+quantity+"\nallergen:"+allergen);
    }
    public void confirmChanges(){
        InventoryManager temp=(InventoryManager) this.manager;
        temp.passAnswer(name,quantity,allergen,"edit");
    }
    public void cancelChanges(ActionEvent event){
        InventoryManager temp=(InventoryManager) this.manager;
        temp.init((Stage)((Node) event.getSource()).getScene().getWindow());
    }
}
