package com.payBill.util;

public class BankAPI {
    static public boolean askVerification(String name,String cardNum,String cvc){
        if(cardNum.startsWith("178")) {
            return true;
        }
        return false;
    }
}
