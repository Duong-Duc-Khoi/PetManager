package org.example.DTO;

public class SupplierItems {
    private int id;
    private String name;

    public SupplierItems(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }
}
