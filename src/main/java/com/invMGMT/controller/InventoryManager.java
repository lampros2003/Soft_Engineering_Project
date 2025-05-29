package com.invMGMT.controller;

import com.common.DBManager;
//import com.common.DatabaseManager;
import com.common.Ingredient;
import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class InventoryManager {
    Stage stage;
    FXMLLoader loader;
//    DatabaseManager db;
    DBManager db;
    Ingredient[] inventory;
    private Ingredient selectedIngredient;
    private Ingredient modifiedIngredient;
    public void init(Stage stage){
        this.stage=stage;
        InventoryListController inventoryList=new InventoryListController();
        inventoryList.display(stage,"/invMGMT/invMGMT.fxml");
//        this.db=new DatabaseManager();
        this.db=new DBManager();
        this.inventory=db.getIngredients();
        inventoryList.setManager(this);
        inventoryList.init(this.inventory);
    }
    public void itemDetails(String id){
        ItemDetailsController idController=new ItemDetailsController();
        idController.display(this.stage,"/invMGMT/itemDetails.fxml");
        Ingredient temp=this.db.getSpecificIngredient(id);
        idController.setManager(this);
        this.selectedIngredient=temp;
        idController.init(temp);
    }
    public void removeItem(String id) {
        db.removeIngredient(id);
    }
    public void editItem(String item){
        editItemController editController=new editItemController();
        editController.display(this.stage,"/invMGMT/itemEdit.fxml");
        editController.getManager(this);
        editController.init(selectedIngredient);
    }
    public void passModifications(String name,String quantity,String allergen){
        confirmController controller=new confirmController();
        controller.display(this.stage,"/invMGMT/confirmChanges.fxml");
        controller.getManager(this);
        controller.init(name,quantity,allergen);
    }
    public void passAnswer(String name,String quantity,String allergen,String mode){
        if(check(name,quantity,allergen)){
            this.modifiedIngredient=new Ingredient(name,Integer.parseInt(quantity),allergen);
            if(mode=="edit")
                this.modifiedIngredient.saveChanges(this.db,this.selectedIngredient.getName());
            else if(mode=="add")
                this.modifiedIngredient.saveChanges(this.db);
            init(this.stage);
        }else{
            ErrorController controller=new ErrorController();
            controller.display(this.stage,"/common/view/errorScreen.fxml");
            controller.showErrorMessage("invalid input!");
            controller.getManager(this);
        }
    }
    public void addItem(){
        AddItemController controller=new AddItemController();
        controller.display(this.stage,"/invMGMT/itemEdit.fxml");
        controller.getManager(this);
        controller.init();
    }
    public boolean check(String name,String quantity,String allergen){
        return quantity.matches("[0-9]+") && name.matches("[A-Za-z0-9]+") && this.db.checkIfNameAlreadyExists(name);
    }
}
