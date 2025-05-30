package com.payBill.controller;

import com.common.Order;
import com.common.OrderItem;
import com.common.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderSummurizationController extends Screen {
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
