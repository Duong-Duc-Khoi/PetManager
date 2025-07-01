package org.example.Models;

import java.math.BigDecimal;
import java.util.Date;

public class Invoice {
    private int invoiceId;
    private int userId;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalAmount;
    private Date createdAt;

    public Invoice(int invoiceId, int userId, String customerName, String customerPhone, BigDecimal totalAmount, Date createdAt) {
        this.invoiceId = invoiceId;
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public Invoice() {
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
