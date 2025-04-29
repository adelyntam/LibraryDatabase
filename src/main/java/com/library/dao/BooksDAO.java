package com.library.dao;
// translate objects to queries, vice versa
// handle CRUD

import com.library.model.Book;
import com.library.util.DBUtil;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksDAO {
    // Add new book
    public int addBook(Book book) throws SQLException {
        String sql = "INSERT INTO Books (title, author_id, genre, publish_year, is_available) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishYear());
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
                        rs.getInt("publish_year"),
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
                    rs.getInt("publish_year"),
                    true
                ));
            }
        }
        return books;
    }

    public List<Map<String, Object>> getAvailableBooksWithAuthors() throws SQLException {
        String sql = "SELECT b.*, a.name AS author_name FROM books b " + "JOIN authors a ON b.author_id = a.author_id " + "WHERE b.is_available = true";

        List<Map<String, Object>> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("bookId", rs.getInt("book_id"));
                book.put("title", rs.getString("title"));
                book.put("authorName", rs.getString("author_name"));
                book.put("genre", rs.getString("genre"));
                book.put("publishYear", rs.getInt("publish_year"));
                book.put("available", rs.getBoolean("is_available"));
                books.add(book);
            }
        }
        return books;
    }
}