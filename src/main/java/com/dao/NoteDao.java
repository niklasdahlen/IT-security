package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.config.Db;
import com.datamodell.Note;

public class NoteDao {

    public void create(int userId, String title, String content) throws SQLException {
        String sql = "INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
        }
    }

    public List<Note> findByUser(int userId) throws SQLException {
        String sql = "SELECT n.id, n.user_id, u.username, n.title, n.content " +
                     "FROM notes n JOIN users u ON u.id = n.user_id " +
                     "WHERE n.user_id = ? ORDER BY n.id";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

    public List<Note> findAll() throws SQLException {
        String sql = "SELECT n.id, n.user_id, u.username, n.title, n.content " +
                     "FROM notes n JOIN users u ON u.id = n.user_id ORDER BY n.id";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return toList(rs);
        }
    }

    public Optional<Note> findById(int id) throws SQLException {
        String sql = "SELECT n.id, n.user_id, u.username, n.title, n.content " +
                     "FROM notes n JOIN users u ON u.id = n.user_id WHERE n.id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public void update(int id, String title, String content) throws SQLException {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Note> toList(ResultSet rs) throws SQLException {
        List<Note> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    private Note map(ResultSet rs) throws SQLException {
        return new Note(rs.getInt("id"), rs.getInt("user_id"),
                rs.getString("username"), rs.getString("title"), rs.getString("content"));
    }
}