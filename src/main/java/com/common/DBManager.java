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

    // Connection method to connect to the SQLite database
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public List<MenuItem> loadMenuData() {
        List<MenuItem> items = new ArrayList<>();
        String sql = """
                SELECT d.name AS dish_name,
                       d.value AS price,
                       GROUP_CONCAT(i.name, ', ') AS ingredients,
                       exp.discount AS discount,
                        exp.expiresOn AS expires_on
                FROM DISH d
                JOIN CONSISTS c ON d.name = c.dishId
                JOIN INGREDIENTS i ON i.name = c.ingredientId
                LEFT JOIN DEAL exp ON exp.name = d.name
                GROUP BY d.name, d.value;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("dish_name");
                String ingredients = rs.getString("ingredients");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                String expires_on = rs.getString("expires_on");

                items.add(new MenuItem(name, price, ingredients, discount, expires_on));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public List<MenuItem> queryExpiredOffers() {
        List<MenuItem> expiredOffers = new ArrayList<>();
        String sql = """
                SELECT d.name AS dish_name,
                       d.value AS price,
                       GROUP_CONCAT(i.name, ', ') AS ingredients,
                       exp.discount AS discount,
                       exp.expiresOn AS expires_on
                FROM DISH d
                JOIN CONSISTS c ON d.name = c.dishId
                JOIN INGREDIENTS i ON i.name = c.ingredientId
                JOIN DEAL exp ON exp.name = d.name
                WHERE exp.expiresOn < DATE('now')
                GROUP BY d.name, d.value;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("dish_name");
                String ingredients = rs.getString("ingredients");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                String expires_on = rs.getString("expires_on");

                expiredOffers.add(new MenuItem(name, price, ingredients, discount, expires_on));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expiredOffers;
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

    public boolean updateMenuData(List<MenuItem> items) {
        String sql = "UPDATE DISH SET value = ? WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (MenuItem item : items) {
                stmt.setDouble(1, item.getPrice());
                stmt.setString(2, item.getName());

                int rows = stmt.executeUpdate();

                if (rows == 0) {
                    System.out.println("Warning: No rows updated for " + item.getName());
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDeals(List<MenuItem> items) {
        String selectSql = "SELECT discount FROM DEAL WHERE name = ?";
        String insertOrReplaceSql = "INSERT OR REPLACE INTO DEAL (name, discount, expiresOn) VALUES (?, ?, DATE('now', '+7 day'))";

        try (Connection conn = connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(insertOrReplaceSql)) {

            for (MenuItem item : items) {
                if (item.getDiscount() > 0.0) {
                    boolean needsUpdate = true;

                    selectStmt.setString(1, item.getName());
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            double existingDiscount = rs.getDouble("discount");
                            if (Double.compare(existingDiscount, item.getDiscount()) == 0) {
                                needsUpdate = false;
                            }
                        }
                    }

                    if (needsUpdate) {
                        updateStmt.setString(1, item.getName());
                        updateStmt.setDouble(2, item.getDiscount());
                        updateStmt.executeUpdate();
                    }
                }
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


