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
		root=FXMLLoader.load(getClass().getResource("/view/summuryPayment.fxml"));
		changeScene(root,event);
	}
	public void payButton(ActionEvent event) throws IOException {
		System.out.println("pay");
		root=FXMLLoader.load(getClass().getResource("/view/payment.fxml"));
		changeScene(root,event);
	}
	public void noMyOrderButton(ActionEvent event) {
		System.out.println("-> to call waiter use case");
	}
	public void bankButton(ActionEvent event) throws IOException {
		root=FXMLLoader.load(getClass().getResource("/view/paymentSuccessScreen.fxml"));
		changeScene(root,event);
	}
	public void sendReceiptButton(ActionEvent event) {
		String emailAddress=emailInput.getText();
		System.out.println("lets pretend that a receipt was sent to you ^ ^ to"+emailAddress);
	}
	private void changeScene(Parent root,ActionEvent event) {
		stage=(Stage)((Node) event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
