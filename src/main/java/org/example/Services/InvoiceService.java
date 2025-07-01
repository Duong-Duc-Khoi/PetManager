package org.example.Services;

import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class InvoiceService {

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getInt("invoice_id"));
                inv.setUserId(rs.getInt("user_id"));
                inv.setCustomerName(rs.getString("customer_name"));
                inv.setCustomerPhone(rs.getString("customer_phone"));
                inv.setTotalAmount(rs.getBigDecimal("total_amount"));
                inv.setCreatedAt(rs.getDate("created_at"));
                invoices.add(inv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getInt("invoice_id"));
                inv.setUserId(rs.getInt("user_id"));
                inv.setCustomerName(rs.getString("customer_name"));
                inv.setCustomerPhone(rs.getString("customer_phone"));
                inv.setTotalAmount(rs.getBigDecimal("total_amount"));
                inv.setCreatedAt(rs.getDate("created_at"));
                return inv;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InvoiceDetail> getDetailsByInvoiceId(int invoiceId) {
        List<InvoiceDetail> details = new ArrayList<>();

        String sql = """
        SELECT d.*, p.name AS pet_name, p.species
        FROM invoicedetails d
        JOIN pets p ON d.pet_id = p.pet_id
        WHERE d.invoice_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setInvoiceId(rs.getInt("invoice_id"));
                detail.setPetId(rs.getInt("pet_id"));
                detail.setPrice(rs.getBigDecimal("price"));
                detail.setPetName(rs.getString("pet_name"));
                detail.setPetSpecies(rs.getString("species"));

                details.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return details;
    }


    public List<Invoice> searchInvoices(String keyword, Date from, Date to) {
        List<Invoice> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM invoices WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND LOWER(customer_phone) LIKE ?");
        }
        if (from != null) {
            sql.append(" AND created_at >= ?");
        }
        if (to != null) {
            sql.append(" AND created_at <= ?");
        }
        sql.append(" ORDER BY created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(index++, "%" + keyword.toLowerCase() + "%");
            }
            if (from != null) {
                stmt.setDate(index++, new java.sql.Date(from.getTime()));
            }
            if (to != null) {
                stmt.setDate(index++, new java.sql.Date(to.getTime()));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getInt("invoice_id"));
                inv.setUserId(rs.getInt("user_id"));
                inv.setCustomerName(rs.getString("customer_name"));
                inv.setCustomerPhone(rs.getString("customer_phone"));
                inv.setTotalAmount(rs.getBigDecimal("total_amount"));
                inv.setCreatedAt(rs.getDate("created_at"));
                list.add(inv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
