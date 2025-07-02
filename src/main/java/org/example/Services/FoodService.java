package org.example.Services;

import org.example.Models.Food;
import org.example.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class FoodService {

    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Food food = new Food(
                        rs.getInt("food_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("supplier_id")
                );
                foods.add(food);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foods;
    }

    public boolean addFood(Food food) {
        String sql = "INSERT INTO foods(name, brand, description, price, quantity_in_stock, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getName());
            ps.setString(2, food.getBrand());
            ps.setString(3, food.getDescription());
            ps.setBigDecimal(4, food.getPrice());
            ps.setInt(5, food.getQuantityInStock());

            if (food.getSupplierId() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, food.getSupplierId());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateFood(Food food) {
        String sql = "UPDATE foods SET name=?, brand=?, description=?, price=?, quantity_in_stock=?, supplier_id=? WHERE food_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getName());
            ps.setString(2, food.getBrand());
            ps.setString(3, food.getDescription());
            ps.setBigDecimal(4, food.getPrice());
            ps.setInt(5, food.getQuantityInStock());

            if (food.getSupplierId() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, food.getSupplierId());
            }

            ps.setInt(7, food.getFoodId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFood(int foodId) {
        String sql = "DELETE FROM foods WHERE food_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, foodId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Food getFoodById(int foodId) {
        String sql = "SELECT * FROM foods WHERE food_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Food(
                        rs.getInt("food_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("supplier_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
