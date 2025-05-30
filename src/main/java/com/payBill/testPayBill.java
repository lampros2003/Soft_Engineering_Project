package com.payBill;

import com.payBill.controller.PaymentManager;

public class testPayBill {
    public static void main(String[] args) {
        int testNumber=0;
        int passNumber=0;
        PaymentManager test=new PaymentManager();
        String[] invalidCardsNums={"11","5000,6466210369","98752","5987255","4242424242542364","test","sda","dsa54564sda","5000 5220 2579 1000","5032-5000-5006-5006"};
        String[] validCardsNums={"17893729974","4539078981951880","5481813415752057","377917782466338","6222024113028426","3016551937738696","6011296371250711","4532251095410047","5275913967106841"};
        String[] invalidName={"inva$lid","asd23","john-doe","Obi_wan","123456"};
        String[] validName={"john doe","Obi Wan Kenobi","Solid Snake","Sephiroth","DoomGuy"};
        String[] invalidCVC={"1569","6513","0","52","cat","0sr","5.10"};
        String[] validCVC={"123","048","655","875"};
        String[] invalidEmail={"invalid","4412sda","hello-there@obi.wan","john@doe"};
        String[] validEmail={"john@doe.com","arthur@dent.com","helloThere@obi.wan","john32@doe.com"};

        for(int i=0;i<invalidCardsNums.length;i++){
            System.out.println("check card for number:"+" "+ invalidCardsNums[i]);
            testNumber++;
            boolean check=test.check("validName",invalidCardsNums[i],"123");
            System.out.println("expected output: false. Real output: "+ check);
            if(!check) {
                passNumber++;
            }
        }
        for(int i=0;i<validCardsNums.length;i++){
            System.out.println("check card for number:"+" "+ validCardsNums[i]);
            testNumber++;
            boolean check=test.check("validName",validCardsNums[i],"123");
            System.out.println("expected output: true. Real output: "+ check);
            if(check) {
                passNumber++;
            }
        }
        for(int i=0;i<invalidName.length;i++){
            System.out.println("check card for name:"+" "+ invalidName[i]);
            testNumber++;
            boolean check=test.check(invalidName[i],validCardsNums[0],"123");
            System.out.println("expected output: false. Real output: "+ check);
            if(!check) {
                passNumber++;
            }
        }
        for(int i=0;i<validName.length;i++){
            System.out.println("check card for name:"+" "+ validName[i]);
            testNumber++;
            boolean check=test.check(validName[i],validCardsNums[0],"123");
            System.out.println("expected output: true. Real output: "+ check);
            if(check) {
                passNumber++;
            }
        }
        for(int i=0;i<invalidCVC.length;i++){
            System.out.println("check card for cvc:"+" "+ invalidCVC[i]);
            testNumber++;
            boolean check=test.check(validName[0],validCardsNums[0],invalidCVC[i]);
            System.out.println("expected output: false. Real output: "+ check);
            if(!check) {
                passNumber++;
            }
        }
        for(int i=0;i<validCVC.length;i++){
            System.out.println("check card for cvc:"+" "+ validCVC[i]);
            testNumber++;
            boolean check=test.check(validName[0],validCardsNums[0],validCVC[i]);
            System.out.println("expected output: true. Real output: "+ check);
            if(check) {
                passNumber++;
            }
        }
        for(int i=0;i<invalidEmail.length;i++){
            System.out.println("check email:"+" "+ invalidEmail[i]);
            testNumber++;
            boolean check=invalidEmail[i].matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+");
            System.out.println("expected output: false. Real output: "+ check);
            if(!check) {
                passNumber++;
            }
        }
        for(int i=0;i<validEmail.length;i++){
            System.out.println("check email:"+" "+ validEmail[i]);
            testNumber++;
            boolean check=validEmail[i].matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+");
            System.out.println("expected output: true. Real output: "+ check);
            if(check) {
                passNumber++;
            }
        }
        System.out.println("tests: "+testNumber+" "+"passes: "+passNumber + "  " +((float) passNumber/testNumber)*100+"%");
    }
}
