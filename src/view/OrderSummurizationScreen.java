package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import utilities.SceneChanger;

public class OrderSummurizationScreen extends Screen{

	public OrderSummurizationScreen(Stage prevStage) {
		super(prevStage,"/view/summuryPayment.fxml");
		System.out.println("OrderSummuryCreated!");
	}

}
