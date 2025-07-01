package org.example.Models;

import java.math.BigDecimal;

public class InvoiceDetail {
    private int detailId;
    private int invoiceId;
    private int petId;
    private BigDecimal price;
    private String petName;
    private String petSpecies;

    public InvoiceDetail(int detailId, int invoiceId, int petId, BigDecimal price) {
        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.petId = petId;
        this.price = price;
    }

    public InvoiceDetail() {
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }
}
