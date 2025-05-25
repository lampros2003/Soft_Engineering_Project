package com.payBill.util;

public class BankAPI {
    static public boolean askVerification(String name,String cardNum,String cvc){
        if(cardNum.length()>3 && cardNum.startsWith("420")) {
            return true;
        }
        return false;
    }
}
