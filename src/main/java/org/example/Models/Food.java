package org.example.Models;

import java.math.BigDecimal;

public class Food {
    private int foodId;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantityInStock;
    private int supplierId;

    public Food() {}

    public Food(String name, String brand, String description, BigDecimal price, int quantityInStock, int supplierId) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.supplierId = supplierId;
    }

    public Food(int foodId, String name, String brand, String description, BigDecimal price, int quantityInStock, int supplierId) {
        this(name, brand, description, price, quantityInStock, supplierId);
        this.foodId = foodId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
}
