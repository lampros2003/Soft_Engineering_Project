package com.adminView;

import com.common.DBManager;
import com.common.Ingredient;
import com.menu.Menu;
import com.menu.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ModifyMenuClass {
    private DBManager dbManager;

    public ModifyMenuClass() {
        this.dbManager = new DBManager();
    }
// public void confirmChanges(){};

    public Menu getCurrentMenu() {
        List<MenuItem> items = dbManager.loadMenuData();

        return new Menu(items);
    }

    public List<Ingredient> getRecommendedIngredients() {
        return dbManager.queryRecommendedIngredients();
    }

    public boolean updateMenuItems(List<MenuItem> items) {
        return dbManager.updateMenuData(items);
    }

    public boolean updateDeals(List<MenuItem> items) {
        return dbManager.updateDeals(items);
    }

    public boolean checkIfChangesAreValid(MenuItem item) {
        return item.getName() != null && !item.getName().trim().isEmpty()
               && item.getIngredients() != null && !item.getIngredients().trim().isEmpty()
               && item.getPrice() >= 0;
    }

    public void checkExpiredOffers(MenuModificationScreen screen) {
        DBManager db = new DBManager();
        List<MenuItem> expired = db.queryExpiredOffers();

        if (expired == null || expired.isEmpty()) {
            screen.noExpiredOfferNotification();
        } else {
            screen.displayExpiredOffers(expired);
        }
    }

}

