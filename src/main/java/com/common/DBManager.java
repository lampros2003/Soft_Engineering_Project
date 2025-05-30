package com.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.common.Order;
import com.common.OrderItem;
import com.menu.DealItem;
import com.menu.MenuItem;

public class DBManager {

    private static final String URL = "jdbc:sqlite:database.db";

    public boolean Check_if_Order_Exists(int tableNumber) {
        boolean exists = false;
        System.out.println("Checking if order exists in the database");
        String sql = "SELECT * FROM 'ORDER' WHERE tableNumber ="+tableNumber+" AND state = 'pending'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()){
                exists = true;

            }else{
                exists = false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return exists;
    }

    // Connection method to connect to the SQLite database
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public Ingredient[] getIngredients() {
        List<Ingredient> results=new ArrayList<Ingredient>();
        String sql="SELECT * FROM INGREDIENTS";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ingredient temp=new Ingredient(rs.getString("name"),rs.getInt("quantity"),rs.getString("allergen"));
                results.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results.toArray(Ingredient[]::new);
    }
    public Ingredient getSpecificIngredient(String name){
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM INGREDIENTS WHERE UPPER(name)=UPPER(?)");
             ) {
            stmt.setString(1,name);
            ResultSet rs= stmt.executeQuery();
            rs.next();
            Ingredient temp=new Ingredient(rs.getString("name"),rs.getInt("quantity"),rs.getString("allergen"));
            return temp;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void removeIngredient(String id){
        System.out.println(id+ " removed or at least lets pretend it got removed (; -;)");
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM INGREDIENTS WHERE name=?");
        ) {
            stmt.setString(1,id);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void editIngredient(String id,Ingredient changes){
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE INGREDIENTS SET name=?,quantity=?,allergen=? WHERE name=?");
        ) {
            stmt.setString(1,changes.getName());
            stmt.setInt(2,changes.getQuantity());
            stmt.setString(4,id);
            stmt.setString(3,(changes.getAllergen()=="")? null:changes.getAllergen());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addIngredient(Ingredient ing){
        System.out.println(ing.getName()+" "+ing.getInfo()+" added");
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO INGREDIENTS VALUES(?,?,?)");
        ) {
            stmt.setString(1,ing.getName());
            stmt.setInt(2,ing.getQuantity());
            stmt.setString(3,(ing.getAllergen()=="")? null:ing.getAllergen());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkIfNameAlreadyExists(String name,String oldName){
        System.out.println(oldName);
        return getSpecificIngredient(name).getName()==null || (oldName!=null && oldName.toUpperCase().equals(name.toUpperCase()));
    }

    public void updateOrderStatus(Order order){//DO NOT USE YET
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE ORDER SET state=? WHERE id=?");
        ) {
            stmt.setString(1,order.getStatus());
//            stmt.setInt(2,IDGOESHERE());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Order QueryOrder(int tableNumber) {
        //Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = """
                        SELECT
                            o.id AS orderId,
                            o.tableNumber,
                            d.name AS dishName,
                            d.value AS dishPrice,
                            COUNT(*) AS dishCount,
                            GROUP_CONCAT(h.comment, '; ') AS comments
                        FROM "ORDER" o
                        JOIN "HAS" h ON o.id = h.orderId
                        JOIN "DISH" d ON h.dishId = d.name
                        WHERE o.tableNumber = """+tableNumber+""" 
                          AND o.state = 'pending'
                        GROUP BY o.id, o.tableNumber, d.name, d.value
                        ORDER BY o.id, d.name;""";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()){
                OrderItem newItem = new OrderItem(rs.getString("dishName"), rs.getInt("dishCount"), rs.getFloat("dishPrice"));
                orderItems.add(newItem);

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        OrderItem[] arrayItems = orderItems.toArray(new OrderItem[0]);
        Order order = new Order(arrayItems);
        return order;
    }

    public void cancelOrder(int tableNumber) {
        String sql = "UPDATE 'ORDER' SET state = 'cancelled' WHERE tableNumber = "+tableNumber+" AND state = 'pending'";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}


