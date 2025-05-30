package com.invMGMT.controller;

import com.common.DatabaseManager;
import com.common.Ingredient;
import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class InventoryManager {
    Stage stage;
    FXMLLoader loader;
    DatabaseManager db;
    Ingredient[] inventory;
    public void init(Stage stage){
        this.stage=stage;
        this.loader= SceneSwitching.switchSceneR(stage,"/invMGMT/invMGMT.fxml");
        this.db=new DatabaseManager();
        this.inventory=db.getIngredints();
        InventoryListController listController=loader.getController();
        listController.display(this.inventory);
    }
}
