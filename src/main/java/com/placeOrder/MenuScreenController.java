package com.placeOrder;

import com.common.Screen;
import com.invMGMT.controller.InventoryManager;
import com.menu.Menu;
import com.menu.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuScreenController extends Screen implements Initializable {
    @FXML ListView<Text> menuList;
    @FXML ListView<Text> orderList;
    public void placeOrder(){
        if(orderList.getItems().size()!=0) {
            System.out.println("placeOrder");
            List<Text> orderItems = new ArrayList<Text>();
            for (int i = 0; i < orderList.getItems().size(); i++) {
                orderItems.add(orderList.getItems().get(i));
            }
            ((PlaceOrderManager) manager).placeOrder(orderItems);
        }
    }
    public void addComments(){
        System.out.println("add Comments");
    }
    public void allergenInfo(){
        System.out.println("add allergen");
    }
    public void callWaiter(){
        System.out.println("call waiter");
    }
    public void redirectToMainScreen(){
        System.out.println("back");
    }
    public void addItem(){
        if(menuList.getSelectionModel().getSelectedItem()!=null){
            Text text=new Text(menuList.getSelectionModel().getSelectedItem().getText());
            orderList.getItems().add(text);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu=((PlaceOrderManager) manager).requestMenu();
        List<MenuItem> menuItems=menu.getItems();
        for(int i=0;i<menuItems.size();i++){
            Text text=new Text(menuItems.get(i).getName()+"\\"+menuItems.get(i).getPrice());
            menuList.getItems().add(text);
        }
    }
}
