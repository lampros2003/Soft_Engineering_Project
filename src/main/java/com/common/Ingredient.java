package com.common;

public class Ingredient {
    String name;
    int quantity;
    String allergen;
    public Ingredient(String name,int quantity){
        this.name=name;
        this.quantity=quantity;
        this.allergen="";
    }
    public Ingredient(String name,int quantity,String allergen){
        this.name=name;
        this.quantity=quantity;
        this.allergen=allergen;
    }
    public String getName(){
        return this.name;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getInfo(){
        return (allergen==null)? "Quantity: "+ this.quantity + "\n\nAllergen: None" :"Quantity: "+ this.quantity + "allergen:" + this.allergen;
    };
}
