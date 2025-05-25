//package com.adminView;
//
//import com.mainpackage.DBManager;
//import com.menu.Menu;
//
//import java.util.List;
//
//public class ManageMenuClass {
//
//    private final DBManager db = new DBManager();
//
//    public Menu displayMenu() {
//        return db.loadMenuData();
//    }
//
//    public List<String> getRecommendedIngredients() {
//        return db.queryRecommendedIngredients();
//    }
//
//    public List<String> getExpiredOffers() {
//        return db.queryExpiredOffers();
//    }
//
//    public boolean checkIfChangesAreValid(Menu data) {
//        return data != null && !data.getItems().isEmpty();
//    }
//
//    public boolean modifyMenu(Menu updatedData) {
//        return db.updateMenuData(updatedData);
//    }
//}