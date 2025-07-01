package org.example.Services;

import org.example.Models.Supplier;
import org.example.Utils.DBConnection;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierService {
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM suppliers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers(name, phone, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getAddress());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, phone = ?,  address = ?, email = ? WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getAddress());
            stmt.setString(4, supplier.getEmail());
            stmt.setInt(5, supplier.getSupplierId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean deleteSupplier(int id) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}

