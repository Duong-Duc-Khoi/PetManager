package org.example.Services;

import org.example.Models.Pet;
import org.example.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetService {
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pet pet = new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getBigDecimal("price"),
                        rs.getString("status"),
                        rs.getInt("supplier_id")
                );
                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    public List<Pet> getAvailablePets() {
        List<Pet> pets = new ArrayList<>();
        String query = "SELECT * FROM pets WHERE status = 'available'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pet pet = new Pet();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setName(rs.getString("name"));
                pet.setSpecies(rs.getString("species"));
                pet.setBreed(rs.getString("breed"));
                pet.setAge(rs.getInt("age"));
                pet.setGender(rs.getString("gender"));
                pet.setPrice(rs.getBigDecimal("price"));
                pet.setStatus(rs.getString("status"));
                pet.setSupplierId(rs.getInt("supplier_id"));

                pets.add(pet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }


    public boolean addPet(Pet pet) {
        String sql = "INSERT INTO pets(name, species, breed, age, gender, price, status, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getGender());
            ps.setBigDecimal(6, pet.getPrice());
            ps.setString(7, pet.getStatus());
            ps.setInt(8, pet.getSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean updatePet(Pet pet) {
        String sql = "UPDATE pets SET name=?, species=?, breed=?, age=?, gender=?, price=?, status=?, supplier_id=? WHERE pet_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getGender());
            ps.setBigDecimal(6, pet.getPrice());
            ps.setString(7, pet.getStatus());
            ps.setInt(8, pet.getSupplierId());
            ps.setInt(9, pet.getPetId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePet(int petId) {
        String sql = "DELETE FROM pets WHERE pet_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, petId);
            int resuilt = ps.executeUpdate();
            return resuilt > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Pet getPetById(int petId) {
        String sql = "SELECT * FROM pets WHERE pet_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getBigDecimal("price"),
                        rs.getString("status"),
                        rs.getInt("supplier_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
