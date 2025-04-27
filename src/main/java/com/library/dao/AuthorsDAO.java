package com.library.dao;
// translate objects to queries, vice versa
// handle CRUD

import com.library.model.Author;
import com.library.util.DBUtil;
import java.sql.*;

public class AuthorsDAO {
    // Create author
    public int addAuthor(Author author) throws SQLException {
        String sql = "INSERT INTO Authors (name, nationality) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    // Get author by ID
    public Author getAuthorById(int authorId) throws SQLException {
        String sql = "SELECT * FROM Authors WHERE author_id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(rs.getInt("author_id"), rs.getString("name"), rs.getString("nationality"));
                }
            }
            return null;
        }
    }

    // Find or create author
    public Integer findAuthorIdByName(String name) throws SQLException {
        String sql = "SELECT author_id FROM authors WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
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
