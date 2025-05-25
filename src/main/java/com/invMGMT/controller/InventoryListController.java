package com.invMGMT.controller;

 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.event.ActionEvent;
 import javafx.stage.Stage;


public class InventoryListController {
    private Stage stage;
    private Scene scene;
    private Parent root;
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
}
