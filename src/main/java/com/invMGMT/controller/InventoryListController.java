package com.invMGMT.controller;

 import com.common.Ingredient;
 import com.common.Screen;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.event.ActionEvent;
 import javafx.scene.control.ListView;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;

 import java.net.URL;
 import java.util.ResourceBundle;


public class InventoryListController extends Screen {
    private Parent root;
    @FXML ListView<Text> invList;
    public InventoryManager inventoryManager;

    Ingredient[] inventory;
    public void addIngredientButton(ActionEvent event){
        inventoryManager.addItem();
    }
    public void invMGMTButton(ActionEvent event){
        stage=getStage(event);
        InventoryManager im=new InventoryManager();
        im.init(stage);
    }
    public void init(Ingredient[] inv) {
//        this.inventory=inv;
        for(int i=0;i<inv.length;i++) {
            invList.getItems().add(new Text(inv[i].getName() +"- Available: "+ inv[i].getQuantity()));
        }
    }
    public void modifySelectedIngredint(){
        System.out.println("modify");
        if(invList.getSelectionModel().getSelectedItem()!=null) {
            String modifyId = invList.getSelectionModel().getSelectedItem().getText().split("-")[0];
            System.out.println(modifyId);
            inventoryManager.itemDetails(modifyId);
        }

    }
    public void setManager(InventoryManager manager){
        this.inventoryManager=manager;
    }

}
