package com.common;

public class DBManager {
    public  Ingredient[] getIngredints(){
        Ingredient[] temp={new Ingredient("patates",1),new Ingredient("ntomates",2)};
        return temp;
    }
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
//        System.out.println("Update menu:");
//        for (MenuItem item : newData.getItems()) {
//            System.out.println(item.getName() + " - " + item.getPrice());
//        }
//        return true;
//    }
}
