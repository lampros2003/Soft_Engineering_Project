package com.menu;

public class MenuItem {
    private String name;
    private double price;
    private String ingredients;
    private double discount;
    private String expiresOn;
    public MenuItem(String name, double price, String ingredients, double discount, String expiresOn) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.discount = discount;
        this.expiresOn = expiresOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setDiscount(Double newValue) {
        this.discount = newValue;
//        this.price = this.price * (1 - newValue);
    }

    public double getDiscount() {
        return this.discount;
    }

    public String getExpiresOn() {
        return this.expiresOn;
    }
}

