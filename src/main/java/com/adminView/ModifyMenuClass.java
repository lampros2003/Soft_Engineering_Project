package com.adminView;

import com.common.DBManager;
import com.menu.Menu;
import com.menu.MenuItem;

import java.util.List;

public class ModifyMenuClass {
    private DBManager dbManager;

    public ModifyMenuClass() {
        this.dbManager = new DBManager();
    }

    public Menu getCurrentMenu() {
        List<MenuItem> items = dbManager.loadMenuData();

        return new Menu(items);
    }
}
