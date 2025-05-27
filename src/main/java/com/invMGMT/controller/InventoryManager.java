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
    private Ingredient selectedIngredient;
    public void init(Stage stage){
        this.stage=stage;
        this.loader= SceneSwitching.switchSceneR(stage,"/invMGMT/invMGMT.fxml");
        this.db=new DatabaseManager();
        this.inventory=db.getIngredints();
        InventoryListController listController=loader.getController();
        listController.setManager(this);
        listController.display(this.inventory);
    }
    public void itemDetails(String id){
//        ItemDetailsController idController=new ItemDetailsController();
        this.loader=SceneSwitching.switchSceneR(this.stage,"/invMGMT/itemDetails.fxml");
        Ingredient temp=this.db.getSpecificIngredient(id);
        ItemDetailsController idController=this.loader.getController();
        idController.setManager(this);
        this.selectedIngredient=temp;
        idController.display(temp);
    }
    public void removeItem(String id) {
        db.removeIngredient(id);
    }
    public void editItem(String item){
        this.loader=SceneSwitching.switchSceneR(this.stage,"/invMGMT/itemEdit.fxml");
        editItemController controller=this.loader.getController();
        controller.init(selectedIngredient);
    }
}
