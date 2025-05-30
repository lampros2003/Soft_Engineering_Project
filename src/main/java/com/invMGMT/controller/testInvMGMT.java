package com.invMGMT.controller;

public class testInvMGMT {

    public static void main(String[] args){
        System.out.println("Test begins now\n");
        System.out.println("Test add Ingredient");
        int testNumber=0;
        int passNumber=0;
        InventoryManager iv=new InventoryManager();
        System.out.println("Ingredient name has to be a String(space and dash included) ");
        String[] invalidNames={"tomato","t0m4t*","(hello)","/there/","obi_wan","Kenobi$"};
        String[] validNames={"lollipop","fredo Espresso","DO-NOT-PANIC","solid Snake"};
        String[] invalidQuantity={"-1","-5","asdf","007","3omelette","5 5","8-0","0.5"};
        String[] validQuantity={"1312","0","42","10"};
        for(int i=0;i<invalidNames.length;i++){
            System.out.println("add ingredient with invalid name:"+ invalidNames[i]);
            boolean currentTest=iv.check(invalidNames[i],"1",null,null);
            System.out.println("expected output false. Real output:"+ currentTest+"\n");
            testNumber++;
            if(!currentTest)
                passNumber++;
        }
        for(int i=0;i<validNames.length;i++){
            System.out.println("add ingredient with valid name:"+ validNames[i]);
            boolean currentTest=iv.check(validNames[i],"1",null,null);
            System.out.println("expected output true. Real output:"+ currentTest+"\n");
            testNumber++;
            if(currentTest)
                passNumber++;
        }
        for(int i=0;i<invalidQuantity.length;i++){
            System.out.println("add ingredient with valid name:"+ invalidQuantity[i]);
            boolean currentTest=iv.check("test",invalidQuantity[i],null,null);
            System.out.println("expected output false. Real output:"+ currentTest+"\n");
            testNumber++;
            if(!currentTest)
                passNumber++;
        }
        for(int i=0;i<validQuantity.length;i++){
            System.out.println("add ingredient with valid name:"+ validQuantity[i]);
            boolean currentTest=iv.check("test",validQuantity[i],null,null);
            System.out.println("expected output false. Real output:"+ currentTest+"\n");
            testNumber++;
            if(currentTest)
                passNumber++;
        }
        System.out.println("tests: "+testNumber+" "+"passes: "+passNumber + "  " +((float) passNumber/testNumber)*100+"%");
    }
}
