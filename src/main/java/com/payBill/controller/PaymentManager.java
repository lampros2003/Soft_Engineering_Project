package com.payBill.controller;
import com.common.Order;
import com.payBill.util.BankAPI;
import com.payBill.util.EmailAPI;
import com.mainpackage.SceneSwitching;
//import com.payBill.util.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentManager {
    FXMLLoader loader;
    Order order;
    Stage stage;
    float totalCost;
    public PaymentManager(){}
    public void init(Stage stage,Order order){
        this.stage=stage;
        loader=SceneSwitching.switchSceneR(stage,"/paybill/view/summuryPayment.fxml");
        OrderSummurizationController summuryController=loader.getController();
        summuryController.getManager(this);
        this.order=order;
        this.totalCost=this.order.calculateTotalCost();

        summuryController.displaySummury(this.order,this.totalCost);
    }
    public void orderApproved(){
        loader=SceneSwitching.switchSceneR(this.stage,"/paybill/view/payment.fxml");
        BankPageController bankController=loader.getController();
        bankController.getManager(this);
    }
    public void passCredentials(String name,String cardNum,String cvc){
        System.out.println("xoxoxox you just got doxxed: \n"+cardNum+"\n" + name + "\n" + cvc);
        boolean transactionComplete=BankAPI.askVerification(name,cardNum,cvc);
        if(transactionComplete) {
            this.order.updateStatus();
            loader=SceneSwitching.switchSceneR(this.stage,"/paybill/view/paymentSuccessScreen.fxml");
            AfterPaymentController afterController=loader.getController();
            afterController.getManager(this);
        }else{
            BankErrorPageController errorController=new BankErrorPageController();
            loader=SceneSwitching.switchSceneR(this.stage,"/common/view/errorScreen.fxml",errorController);
            errorController.showErrorAndBack("Bank Error!",this.order);
        }

    }
    public void sendEmail(String emailAddress){
        if(emailAddress.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) {//checks if inputs follow the email address pattern
            EmailAPI.sendEmail(emailAddress,"this is a receipt");
        } else {
            AfterPaymentController afterController = loader.getController();
            afterController.getManager(this);
            afterController.showWarning();
        }


    }
}
