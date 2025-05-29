package com.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.menu.DealItem;
import com.menu.MenuItem;

public class DBManager {

    private static final String URL = "jdbc:sqlite:database.db";



    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public Ingredient[] getIngredients() {
        Ingredient[] temp = {new Ingredient("patates", 1), new Ingredient("ntomates", 2)};
        return temp;
    }

    public List<MenuItem> loadMenuData() {
        List<MenuItem> items = new ArrayList<>();
        String sql = """
                SELECT d.name AS dish_name,
                       d.value AS price,
                       GROUP_CONCAT(i.name, ', ') AS ingredients
                FROM DISH d
                JOIN CONSISTS c ON d.name = c.dishId
                JOIN INGREDIENTS i ON i.name = c.ingredientId
                GROUP BY d.name, d.value;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("dish_name");
                double price = rs.getDouble("price");
                String ingredients = rs.getString("ingredients");

                items.add(new MenuItem(name, price, ingredients));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public List<Ingredient> queryRecommendedIngredients() {
        List<Ingredient> recommended = new ArrayList<>();

        String sql = """
                SELECT name, quantity
                FROM INGREDIENTS
                WHERE quantity >= 10
                ORDER BY quantity DESC;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String quantity = rs.getString("quantity");

                recommended.add(new Ingredient(name, Integer.parseInt(quantity)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommended;
    }

//    public List<DealItem> queryRecommendedDeals() {
//
//    }

    public List<DealItem> loadDeals() {
        List<DealItem> deals = new ArrayList<>();
        String sql = """
                    SELECT d.name, d.value, de.discount
                    FROM DEAL de
                    JOIN DISH d ON de.name = d.name;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("value");
                double discount = rs.getDouble("discount");
                deals.add(new DealItem(name, price, discount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deals;
    }

    public boolean updateMenu(List<MenuItem> items) {
        String sql = "UPDATE DISH SET value = ? WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (MenuItem item : items) {
                stmt.setDouble(1, item.getPrice());
                stmt.setString(2, item.getName());
                stmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


