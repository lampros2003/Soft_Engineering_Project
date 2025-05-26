package com.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


