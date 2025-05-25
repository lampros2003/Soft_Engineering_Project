//package com.mainpackage;
//
//import com.menu.Menu;
//import com.menu.MenuItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DBManager {
//    public Menu loadMenuData() {
//        List<MenuItem> items = new ArrayList<>();
//        items.add(new MenuItem("Pizza", 9.99));
//        items.add(new MenuItem("Burger", 6.49));
//        return new Menu(items);
//    }
//
//    public List<String> queryRecommendedIngredients() {
//        return List.of("Mozzarella", "Basil", "Tomato");
//    }
//
//    public List<String> queryExpiredOffers() {
//        return List.of("2-for-1 Burger", "Free Soda with Pasta");
//    }
//
//    public boolean updateMenuData(Menu newData) {
//        System.out.println("Updated menu:");
//        for (MenuItem item : newData.getItems()) {
//            System.out.println(item.getName() + " - " + item.getPrice());
//        }
//        return true;
//    }
//}