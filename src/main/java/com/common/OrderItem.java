package com.common;

public class OrderItem {
    public String name;
    public int quantity;
    Ingredient[] ingredientList;
    public float cost;
    public OrderItem(String name,int quantity,Ingredient[] ingredientList,float cost){
        this.name=name;
        this.quantity=quantity;
        this.cost=cost;
    }
    public OrderItem(String name,int quantity,float cost){
        this.name=name;
        this.quantity=quantity;
        this.cost=cost;
    }
}
