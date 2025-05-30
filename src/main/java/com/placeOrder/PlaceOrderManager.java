package com.placeOrder;

import com.common.DBManager;
import com.common.Order;
import com.common.OrderItem;
import com.menu.Menu;
import com.menu.MenuItem;
import com.payBill.controller.PaymentManager;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderManager {
    Stage stage;
    DBManager db;
    public PlaceOrderManager(){
        this.db=new DBManager();
    }
    public void init(Stage stage){
        this.stage=stage;
        MenuScreenController menuController=new MenuScreenController();
        menuController.getManager(this);
        menuController.display(this.stage,"/menu/MenuWindow.fxml");
    }
    public Menu requestMenu(){
        List<MenuItem> tempMenuItemList= db.loadMenuData();
        Menu menu=new Menu(tempMenuItemList);
        return menu;
    }

    public void placeOrder(List<Text> menuItems){
        List<OrderItem> orderitems=new ArrayList<OrderItem>();
        for(int i=0;i<menuItems.size();i++){
            System.out.println(menuItems.get(i).getText());
            String[] temp=menuItems.get(i).getText().split("\\\\");
//            System.out.println(temp[0]);
            orderitems.add(new OrderItem(temp[0],1,Float.parseFloat(temp[1])));
        }
        Order newOrder=new Order(orderitems.toArray(OrderItem[]::new));
        PaymentManager pm=new PaymentManager();
        pm.init(this.stage,newOrder);
    };
}
