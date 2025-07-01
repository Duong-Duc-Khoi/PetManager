package org.example.Services;

import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Utils.DBConnection;

import java.sql.*;
import java.util.List;

public class SaleService {

        public boolean checkout(Invoice invoice, List<InvoiceDetail> details) {
            try (
                    Connection conn = DBConnection.getConnection();
                    PreparedStatement invoiceStmt = conn.prepareStatement(
                            "INSERT INTO invoices (user_id, customer_name, customer_phone, total_amount, created_at) VALUES (?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    PreparedStatement detailStmt = conn.prepareStatement(
                            "INSERT INTO invoicedetails (invoice_id, pet_id, price) VALUES (?, ?, ?)"
                    );
                    PreparedStatement updatePetStmt = conn.prepareStatement(
                            "UPDATE pets SET status = 'sold' WHERE pet_id = ?"
                    )
            ) {
                conn.setAutoCommit(false);

                // Insert invoice
                invoiceStmt.setInt(1, invoice.getUserId());
                invoiceStmt.setString(2, invoice.getCustomerName());
                invoiceStmt.setString(3, invoice.getCustomerPhone());
                invoiceStmt.setBigDecimal(4, invoice.getTotalAmount());
                invoiceStmt.setDate(5, new java.sql.Date(invoice.getCreatedAt().getTime()));
                invoiceStmt.executeUpdate();

                // Lấy invoiceId được sinh tự động
                ResultSet rs = invoiceStmt.getGeneratedKeys();
                if (!rs.next()) throw new SQLException("Không tạo được invoice_id");
                int invoiceId = rs.getInt(1);

                // Insert invoice details & update pet status
                for (InvoiceDetail detail : details) {
                    detailStmt.setInt(1, invoiceId);
                    detailStmt.setInt(2, detail.getPetId());
                    detailStmt.setBigDecimal(3, detail.getPrice());
                    detailStmt.addBatch();

                    updatePetStmt.setInt(1, detail.getPetId());
                    updatePetStmt.addBatch();
                }

                detailStmt.executeBatch();
                updatePetStmt.executeBatch();
                conn.commit();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

