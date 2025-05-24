package com.payBill.controller;


import com.common.Order;
import com.common.OrderItem;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class PaymentController{
	private Stage stage;
	private Scene scene;
	private Parent root;
	public PaymentController() {
		// TODO Auto-generated constructor stub
	}
	//to be deleted in the final version
	public void payBillButton(ActionEvent event) {
		stage=getStage(event);
		PaymentManager pm=new PaymentManager();
		OrderItem[] forMyOrder={new OrderItem("patata",1,2.0f),new OrderItem("pita",2,0.5f)};
		Order myOrder =new Order(forMyOrder);
		pm.init(stage,myOrder);
	}
	public void invMGMTButton(ActionEvent event) {
		stage=getStage(event);
//		InventoryManagementMain invMGMTMain=new InventoryManagementMain(stage);
//		invMGMTMain.changeTitle("Inventory Management Screen");
	}
	//end of to be deleted
	private Stage getStage(ActionEvent event) {
		return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
	}
}
