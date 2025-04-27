package com.library.dao;
// translate objects to queries, vice versa
// handle CRUD

import com.library.model.Book;
import com.library.util.DBUtil;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {
    // Add new book
    public int addBook(Book book) throws SQLException {
        String sql = "INSERT INTO Books (title, author_id, genre, publish_year, is_available) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishYear().getValue());
            stmt.setBoolean(5, book.isAvailable());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Update book availability
    public void updateAvailability(Connection conn, int bookId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE Books SET is_available = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    // Get book by ID
    public Book getBookById(int bookId) throws SQLException {
        String sql = "SELECT * FROM Books WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("author_id"),
                        rs.getString("genre"),
                        Year.of(rs.getInt("publish_year")),
                        rs.getBoolean("is_available")
                    );
                }
            }
            return null;
        }
    }

    // Get available books (for listing)
    public List<Book> getAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE is_available = true";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getInt("author_id"),
                    rs.getString("genre"),
                    Year.of(rs.getInt("publish_year")),
                    true
                ));
            }
        }
        return books;
    }
}