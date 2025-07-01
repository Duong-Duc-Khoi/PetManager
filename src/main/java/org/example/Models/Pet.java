package org.example.Models;

import java.math.BigDecimal;

public class Pet {
    private int petId;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String gender;
    private BigDecimal price;
    private String status;
    private int supplierId;

    public Pet(String name, String species, String breed, int age, String gender, BigDecimal price, String status, int supplierId) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.price = price;
        this.status = status;
        this.supplierId = supplierId;
    }

    public Pet(int petId, String name, String species, String breed, int age, String gender, BigDecimal price, String status, int supplierId) {
        this(name, species, breed, age, gender, price, status, supplierId);
        this.petId = petId;
    }



    public Pet() {
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
}