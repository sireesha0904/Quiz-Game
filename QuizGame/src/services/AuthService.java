package services;

import db.DBConnection;
import models.User;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    // Register user: returns true if successful, false if username exists
    public boolean registerUser(String username, String password, String fullName, String role) {
        if (isUsernameTaken(username)) {
            return false; // Username already exists
        }

        String salt = PasswordUtil.getSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);
        // Store hashedPassword and salt together separated by ':'
        String passwordStored = salt + ":" + hashedPassword;

        String sql = "INSERT INTO users (username, password_hash, full_name, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordStored);
            stmt.setString(3, fullName);
            stmt.setString(4, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if username exists
    private boolean isUsernameTaken(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if found
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    // Login user: returns User object if success, null otherwise
    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String stored = rs.getString("password_hash"); // salt:hashedPassword
                String[] parts = stored.split(":");
                if (parts.length != 2) return null;
                String salt = parts[0];
                String storedHash = parts[1];

                String hashedInputPassword = PasswordUtil.hashPassword(password, salt);

                if (storedHash.equals(hashedInputPassword)) {
                    // Password matches, return User object
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // login failed
    }
}
