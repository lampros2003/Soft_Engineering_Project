package com.invMGMT.controller;

 import com.common.Ingredient;
 import javafx.fxml.FXML;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.event.ActionEvent;
 import javafx.scene.control.ListView;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;


public class InventoryListController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML ListView<Text> invList;
    public void addIngredientButton(ActionEvent event){
        System.out.println("add ingredient");
    }
    public void invMGMTButton(ActionEvent event){
        stage=getStage(event);
        InventoryManager im=new InventoryManager();
        im.init(stage);
    }
    private Stage getStage(ActionEvent event) {
        return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
    }

    public void display(Ingredient[] inv) {
        for(int i=0;i<inv.length;i++) {
            invList.getItems().add(new Text(inv[i].getName() +"- Available: "+ inv[i].getQuantity()));
        }
    }
    public void modifySelectedIngredint(){
        System.out.println("modify");
//        invList.getSelectionModel().selected
    }
}
