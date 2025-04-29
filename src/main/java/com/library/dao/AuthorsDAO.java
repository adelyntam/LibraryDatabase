package com.library.dao;

import com.library.model.Author;
import java.sql.*;

public class AuthorsDAO {
    // Create author, return auto-gen key
    public int addAuthor(Connection conn, Author author) throws SQLException {
        String sql = "INSERT INTO Authors (name, nationality) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getNationality());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Delete author
    public void deleteAuthor(Connection conn, int authorId) throws SQLException {
        String sql = "DELETE FROM Authors WHERE author_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.executeUpdate();
        }
    }

    // Get author by ID
    public Author getAuthorById(Connection conn, int authorId) throws SQLException {
        String sql = "SELECT * FROM Authors WHERE author_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(
                            rs.getInt("author_id"),
                            rs.getString("name"),
                            rs.getString("nationality")
                    );
                }
            }
            return null;
        }
    }

    // Find or create author
    public Integer findAuthorIdByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT author_id FROM authors WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("author_id");
                }
            }
        }
        return null;
    }
}
