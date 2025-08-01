package org.example.Models;

public class FoodCategory {
    private int id;
    private String name;
    private String description;

    public FoodCategory() {}

    public FoodCategory(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public FoodCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
