package com.waiter;

import javafx.stage.Stage;
import com.common.DBManager;
public class Manage_Waiter_Class {
    public void Search_Tables_Order(int table_number, Stage stage){
        System.out.println("Search order for table "+" "+table_number);
        boolean status = DBManager.Check_if_Order_Exists(table_number);
        if (status) {
            System.out.println("Order exists");
            options_screen op = new options_screen();
            op.Display(stage);


        } else {
            System.out.println("Order does not exist");
        }
    }
}
