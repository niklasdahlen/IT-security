package com;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import java.util.Optional;

public class userdao {
        public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void register(String username, String rawPassword) throws SQLException {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'USER')";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.executeUpdate();
        }
    } 


    public Optional<user> login(String username, String rawPassword) throws SQLException {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(rawPassword, storedHash)) {
                        return Optional.of(new user(
                                rs.getInt("id"),
                                rs.getString("username"),
                                storedHash,
                                rs.getString("role")));
                    }
                }
                return Optional.empty();
            }
        }
    }

        public void changePassword(int userId, String newPassword) throws SQLException {
        String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, userId);
            ps.executeUpdate();
             
        }
    }
}

