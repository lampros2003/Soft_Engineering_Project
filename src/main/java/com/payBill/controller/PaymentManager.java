package com.payBill.controller;
import com.common.DBManager;
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
    DBManager db;
    public PaymentManager(){}
    public void init(Stage stage,Order order){
        this.stage=stage;
        OrderSummurizationController summuryController=new OrderSummurizationController();
        summuryController.display(this.stage,"/paybill/view/summuryPayment.fxml");
        summuryController.getManager(this);
        this.order=order;
        this.db=new DBManager();
        this.totalCost=this.order.calculateTotalCost();

        summuryController.displaySummury(this.order,this.totalCost);
    }
    public void orderApproved(){
        BankPageController bankController=new BankPageController();
        bankController.display(this.stage,"/paybill/view/payment.fxml");
        bankController.getManager(this);
    }
    public void passCredentials(String name,String cardNum,String cvc){
        System.out.println("xoxoxox you just got doxxed: \n"+cardNum+"\n" + name + "\n" + cvc);
        if()
            System.out.println("ok");
        boolean transactionComplete=BankAPI.askVerification(name,cardNum,cvc);
        if(transactionComplete) {
            this.order.updateStatus();
//            this.db.updateOrderStatus(this.order);
            AfterPaymentController afterController=new AfterPaymentController();
            afterController.display(this.stage,"/paybill/view/paymentSuccessScreen.fxml");
            afterController.getManager(this);
        }else{
            BankErrorPageController errorController=new BankErrorPageController();
            errorController.display(this.stage,"/common/view/errorScreen.fxml");
            errorController.showErrorAndBack("Bank Error!",this.order);
        }

    }
    public void sendEmail(String emailAddress,AfterPaymentController afterController){
        if(emailAddress.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) {//checks if inputs follow the email address pattern
            EmailAPI.sendEmail(emailAddress,"this is a receipt");
        } else {
            afterController.getManager(this);
            afterController.showWarning();
        }


    }
    public boolean check(String name,String cardNum){
        return name.matches("[A-Za-z ]+") && cardNum.matches("[0-9]{8,19}");
    }
    public boolean luhn(String num){
        int sum=0;
        for(int i=0;i<num.length()-1;i++){
            int temp=num.charAt(i)-'0';
            temp*=2;

        }
    }
}
