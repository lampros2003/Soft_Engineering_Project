package com.payBill.controller;

import com.payBill.util.SceneSwitching;
import com.common.Order;
import com.common.OrderItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderSummurizationController{
    private Stage stage;
    private PaymentManager manager;
    @FXML private VBox orderList;
    @FXML private Text costText;
    public void payButton(ActionEvent event) throws IOException {
        stage=getStage(event);
        manager.orderApproved();
    }
    public void callWaiterButton(ActionEvent event) {
        System.out.println("-> to call waiter use case");
    }
    private Stage getStage(ActionEvent event) {
        return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
    }
    public void displaySummury(Order order,float totalCost){
        for(int i=0;i<order.items.length;i++){
            OrderItem temp=order.items[i];
            orderList.getChildren().add(new Text(temp.name + " x" + temp.quantity + " : " +temp.quantity* temp.cost + "€"));
        }
        costText.setText("Total Cost: "+totalCost+"€");
    }
    public void getManager(PaymentManager manager){
        this.manager=manager;
    }
}
