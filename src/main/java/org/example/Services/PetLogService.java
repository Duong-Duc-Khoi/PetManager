package org.example.Services;

import org.example.Models.PetLog;
import org.example.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetLogService {
    public List<PetLog> filterLogs(Integer petId, Integer userId, String statusFilter) {
        List<PetLog> logs = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM petlogs WHERE 1=1");

        if (petId != null) {
            query.append(" AND pet_id = ?");
        }
        if (userId != null) {
            query.append(" AND user_id = ?");
        }
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("All")) {
            query.append(" AND status = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;

            if (petId != null) {
                stmt.setInt(index++, petId);
            }
            if (userId != null) {
                stmt.setInt(index++, userId);
            }
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("All")) {
                stmt.setString(index++, statusFilter);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PetLog log = new PetLog();
                log.setLogId(rs.getInt("log_id"));
                log.setPetId(rs.getInt("pet_id"));
                log.setStatus(rs.getString("status"));
                log.setNote(rs.getString("note"));
                log.setCreatedAt(rs.getTimestamp("created_at"));
                int uid = rs.getInt("user_id");
                log.setUserId(rs.wasNull() ? null : uid);
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public boolean addLog(PetLog log) {
        String sql = "INSERT INTO petlogs (pet_id, status, note, created_at, user_id) VALUES (?, ?, ?, NOW(), ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getPetId());
            stmt.setString(2, log.getStatus());
            stmt.setString(3, log.getNote());

            if (log.getUserId() != null) {
                stmt.setInt(4, log.getUserId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateLog(PetLog log) {
        String sql = "UPDATE petlogs SET pet_id = ?, status = ?, note = ?, user_id = ? WHERE log_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getPetId());
            stmt.setString(2, log.getStatus());
            stmt.setString(3, log.getNote());

            if (log.getUserId() != null) {
                stmt.setInt(4, log.getUserId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setInt(5, log.getLogId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteLog(int logId) {
        String sql = "DELETE FROM petlogs WHERE log_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, logId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



}
