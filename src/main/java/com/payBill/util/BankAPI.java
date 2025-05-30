package com.payBill.util;

public class BankAPI {
    static public boolean askVerification(String name,String cardNum,String cvc){
        if(cardNum.startsWith("4")) {
            return true;
        }
        return false;
    }
}
