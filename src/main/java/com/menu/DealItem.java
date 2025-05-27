package com.menu;

public class DealItem {
    private String dishName;
    private double originalPrice;
    private double discount;

    public DealItem(String dishName, double originalPrice, double discount) {
        this.dishName = dishName;
        this.originalPrice = originalPrice;
        this.discount = discount;
    }

    public String getDishName() {
        return dishName;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return Math.round(originalPrice * (1 - discount) * 100.0) / 100.0;
    }
}
