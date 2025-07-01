package org.example.Services;

import org.example.Models.PetMedicalRecord;
import org.example.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordLogService {

    public List<PetMedicalRecord> getRecordLogService() {
        List<PetMedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM petmedicalrecords";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PetMedicalRecord record = new PetMedicalRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setPetId(rs.getInt("pet_id"));
                record.setType(rs.getString("type"));
                record.setDescription(rs.getString("description"));
                record.setVetName(rs.getString("vet_name"));
                record.setDate(rs.getDate("date"));
                Date nextDate = rs.getDate("next_appointment");
                if (nextDate != null) {
                    record.setNextAppointment(nextDate);
                } else {
                    record.setNextAppointment(null);
                }
                records.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public boolean addRecordLogService(PetMedicalRecord record) {
        String query = "INSERT INTO petmedicalrecords (pet_id, type, description, vet_name, date, next_appointment) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, record.getPetId());
            stmt.setString(2, record.getType());
            stmt.setString(3, record.getDescription());
            stmt.setString(4, record.getVetName());
            if (record.getDate() != null) {
                stmt.setDate(5, new java.sql.Date(record.getDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if (record.getNextAppointment() != null) {
                stmt.setDate(6, new java.sql.Date(record.getNextAppointment().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateRecordLogService(PetMedicalRecord record) {
        String query = "UPDATE petmedicalrecords SET pet_id=?, type=?, description=?, vet_name=?, date=?, next_appointment=? WHERE record_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, record.getPetId());
            stmt.setString(2, record.getType());
            stmt.setString(3, record.getDescription());
            stmt.setString(4, record.getVetName());
            if (record.getDate() != null) {
                stmt.setDate(5, new java.sql.Date(record.getDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if (record.getNextAppointment() != null) {
                stmt.setDate(6, new java.sql.Date(record.getNextAppointment().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setInt(7, record.getRecordId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRecordLogService(PetMedicalRecord record) {
        String query = "DELETE FROM petmedicalrecords WHERE record_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, record.getRecordId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
