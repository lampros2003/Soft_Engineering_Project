package com.waiter;

import com.common.DBManager;
import com.common.Order;
import com.menu.MenuWindow;
import javafx.stage.Stage;

public class Manage_Waiter_Class {
    DBManager manager = new DBManager();

    public void Search_Tables_Order(int table_number, Stage stage) {
        System.out.println("Search order for table " + " " + table_number);
        boolean status = manager.Check_if_Order_Exists(table_number);
        if (status) {
            System.out.println("Order exists");
            options_screen op = new options_screen();
            op.Display(table_number, stage);
        } else {
            System.out.println("Order does not exist");
            no_order_screen op = new no_order_screen();
            op.Display(table_number, stage);
        }
    }
    public void Get_Order_From_DB(int table_number,Stage stage){
        System.out.println("Get order for table "+" "+table_number+" "+"from the database");

        Order this_tables_order = manager.QueryOrder(table_number) ;
        MenuWindow menuWindow = new MenuWindow();
        menuWindow.displayOrderPage_for_waiter(this_tables_order,stage);
        }

    public void cancelOrder(int table_number) {
        System.out.println("ABout to cancel order for table " + table_number);
        manager.cancelOrder(table_number);
    }
}
