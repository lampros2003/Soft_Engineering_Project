package com.menu;

import com.common.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.mainpackage.SceneSwitching;

import java.io.IOException;


public class MenuWindow {
    @FXML
    public FXMLLoader loader;

    public static int tableNumber = 1; // Example table number, replace with actual value
    private final static ManageMenuClass menuManager = new ManageMenuClass(tableNumber);

    @FXML
    void redirectToMainScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
    }

    @FXML
    void placeOrder(ActionEvent event) {
        System.out.println("Place Order");
    }

    @FXML
    void addComments(ActionEvent event) {
        System.out.println("Add Comments");
    }

    @FXML
    void allergenInfo(ActionEvent event) {
        System.out.println("Pressed Allergen Information");
    }

    @FXML
    void callWaiter(ActionEvent event) throws IOException {
            menuManager.callWaiter();
    }
    public void display(Stage stage){
        System.out.println("Displaying MenuWindow");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/menu/MenuWindow.fxml");
            this.loader.setController(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayOrderPage_for_waiter(Order order, Stage stage){
        System.out.println("Displaying order_Page_for_waiter");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/menu_screen_for_phone.fxml");
            this.loader.setController(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayOrderPage_for_waiter(Stage stage){
        System.out.println("Displaying order_Page_for_waiter");
        try {
            this.loader = SceneSwitching.switchSceneR(stage, "/waiter/menu_screen_for_phone.fxml");
            this.loader.setController(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
