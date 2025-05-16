package com.smart_restaurant.invMGMT.controller;
 import com.smart_restaurant.invMGMT.view.InvMGMTScreen;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.event.ActionEvent;
 import javafx.stage.Stage;


public class InvMGMTController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void addIngredientButton(ActionEvent event){
        System.out.println("add ingredient");
    }
    public void invMGMTButton(ActionEvent event){
        stage=getStage(event);
        InvMGMTScreen invMGMTScreen=new InvMGMTScreen(stage);
        System.out.println("here");
        invMGMTScreen.changeTitle("Inventory Management");
    }
    private Stage getStage(ActionEvent event) {
        return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
    }
}
