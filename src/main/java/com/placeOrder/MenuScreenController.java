package com.placeOrder;

import com.common.Screen;
import com.invMGMT.controller.InventoryManager;
import com.mainpackage.SceneSwitching;
import com.menu.ManageMenuClass;
import com.menu.Menu;
import com.menu.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuScreenController extends Screen implements Initializable {
    @FXML ListView<Text> menuList;
    @FXML ListView<Text> orderList;
    @FXML
    public FXMLLoader loader;

    public static int tableNumber = 1; // Example table number, replace with actual value
    private final static ManageMenuClass menuManager = new ManageMenuClass(tableNumber);

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
    @FXML
    void callWaiter(ActionEvent event) throws IOException {
        menuManager.callWaiter();
    }

    @FXML
    void redirectToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
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
