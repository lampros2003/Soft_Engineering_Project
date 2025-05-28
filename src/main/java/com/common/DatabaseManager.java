package com.common;

import java.util.Objects;

public class DatabaseManager {
    Ingredient[] temp={new Ingredient("patates",1),new Ingredient("ntomates",2)};
    public  Ingredient[] getIngredints(){
        return temp;
    }
    public Ingredient getSpecificIngredient(String name){
        for(int i=0;i<temp.length;i++){
            if(Objects.equals(temp[i].getName(), name)){
                return temp[i];
            }
        }
        return null;
    }
    public void removeIngredient(String id){
        System.out.println(id+ " removed or at least lets pretend it got removed (; -;)");
    }
    public void editIngredient(String id,Ingredient changes){
        System.out.println(id+" changed to"+ changes.getInfo());
    }
    public void addIngredient(Ingredient ing){
        System.out.println(ing.getName()+ing.getInfo()+" added");
    }
    public boolean checkIfNameAlreadyExists(String name){
        return true;
    }
}
