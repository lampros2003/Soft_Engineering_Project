package com.common;

import java.util.List;

public class Order {
    public OrderItem[] items;
    private String status;

    public Order(OrderItem[] items){
        System.out.println("order Created");
        this.status="pending";
        this.items=items;
    }
    public float calculateTotalCost(){
        float cost=0.0f;
        for(int i=0;i<items.length;i++){
            cost+=items[i].cost*items[i].quantity;
        }
        return cost;
    }
    public String getStatus(){
        return this.status;
    }
    public void updateStatus(){
        this.status="Completed";
    };

}
