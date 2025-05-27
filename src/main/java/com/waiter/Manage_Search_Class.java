package com.waiter;

import com.common.DatabaseManager;

public class Manage_Search_Class {
    public void Search_Tables_Order(int table_number){
        System.out.println("Search order for table "+" "+table_number);
        boolean status = DatabaseManager.Check_if_Order_Exists(table_number);
        if (status) {
            System.out.println("Order exists");
            //Stage cstage = null;
            //options_screen.Display(cstage);
            options_screen john = new options_screen();
            john.Display(null);


        } else {
            System.out.println("Order does not exist");
        }
    }
}
