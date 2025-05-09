package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


//myImprorts
import view.OrderSummurizationScreen;
import view.BankPage;
import view.InventoryManagementMain;
import view.OrderCompletedScreen;

public class PaymentController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	private TextField emailInput;
	public PaymentController() {
		// TODO Auto-generated constructor stub
	}
	public void payBillButton(ActionEvent event) throws IOException {
		stage=getStage(event);
		OrderSummurizationScreen summuryOrderScreen=new OrderSummurizationScreen(stage);
		summuryOrderScreen.changeTitle("Order Summury");
	}
	public void invMGMTButton(ActionEvent event) {
		stage=getStage(event);
		InventoryManagementMain invMGMTMain=new InventoryManagementMain(stage);
		invMGMTMain.changeTitle("Inventory Management Screen");
	}
	public void payButton(ActionEvent event) throws IOException {
		System.out.println("pay");
		stage=getStage(event);
		BankPage bankScreen=new BankPage(stage);
		bankScreen.changeTitle("Bank Page");
	}
	public void noMyOrderButton(ActionEvent event) {
		System.out.println("-> to call waiter use case");
	}
	public void bankButton(ActionEvent event) throws IOException {
		stage=getStage(event);
		OrderCompletedScreen orderCompletedScreen=new OrderCompletedScreen(stage);
		orderCompletedScreen.changeTitle("order Completed");
	}
	public void sendReceiptButton(ActionEvent event) {
		String emailAddress=emailInput.getText();
		System.out.println("lets pretend that a receipt was sent to you ^ ^ to "+emailAddress);
	}
	private Stage getStage(ActionEvent event) {
		return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
	}
}
