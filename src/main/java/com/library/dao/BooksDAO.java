package com.library.dao;

import com.library.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksDAO {

    // Add new book
    public int addBook(Connection conn, Book book) throws SQLException {
        String sql = "INSERT INTO Books (title, author_id, genre, publish_year, is_available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    // Delete book
    public void deleteBook(Connection conn, int bookId) throws SQLException {
        String sql = "DELETE FROM Books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
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
    public Book getBookById(Connection conn, int bookId) throws SQLException {
        String sql = "SELECT * FROM Books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getInt("book_id"), rs.getString("title"), rs.getInt("author_id"), rs.getString("genre"), rs.getInt("publish_year"), rs.getBoolean("is_available"));
                }
            }
            return null;
        }
    }

    // Get available books
    public List<Book> getAvailableBooks(Connection conn) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE is_available = true";
        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getInt("author_id"), rs.getString("genre"), rs.getInt("publish_year"), true));
            }
        }
        return books;
    }

    // Get available books with author info
    public List<Map<String, Object>> getAvailableBooksWithAuthors(Connection conn) throws SQLException {
        String sql = "SELECT b.*, a.name AS author_name FROM books b " + "JOIN authors a ON b.author_id = a.author_id " + "WHERE b.is_available = true";

        List<Map<String, Object>> books = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
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