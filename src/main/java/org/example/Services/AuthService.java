package org.example.Services;

import org.example.Models.Session;
import org.example.Models.User;
import org.example.Utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {
    public boolean login(String username, String password) {
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean correctPassword = BCrypt.checkpw(password, rs.getString("password"));
                if (correctPassword) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getDate("created_at")
                    );
                    Session.getInstance().setUser(user);
                    System.out.println("User logged role: " + Session.getInstance().getUser().getRole());
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password, String role) {
        try {
            Connection connection = DBConnection.getConnection();
            String checkSql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?,  ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            insertStmt.setString(3, role);
            int rows = insertStmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
