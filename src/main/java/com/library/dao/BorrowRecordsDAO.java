package com.library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.library.model.BorrowRecord;

public class BorrowRecordsDAO {
    // Create borrow record, return id
    public int borrowBook(Connection conn, int bookId, int memberId) throws SQLException {
        String sql = "INSERT INTO BorrowRecords (book_id, member_id, borrow_date, status) VALUES (?, ?, ?, 'borrowed')";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Update return record
    public void returnBook(Connection conn, int recordId) throws SQLException {
        String sql = "UPDATE BorrowRecords SET return_date = ?, status = 'returned' WHERE record_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, recordId);
            stmt.executeUpdate();
        }
    }

    // Get borrow record by ID
    public BorrowRecord getBorrowRecordById(Connection conn, int recordId) throws SQLException {
        String sql = "SELECT * FROM BorrowRecords WHERE record_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BorrowRecord(rs.getInt("record_id"), rs.getInt("book_id"), rs.getInt("member_id"), rs.getDate("borrow_date").toLocalDate(), rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null, rs.getString("status"));
                }
            }
            return null;
        }
    }

    // Get active borrows for a member
    public List<BorrowRecord> getActiveBorrows(Connection conn, int memberId) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM BorrowRecords WHERE member_id = ? AND status = 'borrowed'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new BorrowRecord(rs.getInt("record_id"), rs.getInt("book_id"), rs.getInt("member_id"), rs.getDate("borrow_date").toLocalDate(), rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null, rs.getString("status")));
                }
            }
        }
        return records;
    }

    // Get active borrow for a book and member, choosing the highest record ID if multiples exist
    public BorrowRecord getActiveBorrow(Connection conn, int bookId, int memberId) throws SQLException {
        String sql = "SELECT * FROM BorrowRecords WHERE book_id = ? AND member_id = ? AND status = 'borrowed' ORDER BY record_id DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public List<Map<String,Object>> getAllBorrowRecordsWithDetails(Connection conn) throws SQLException {
        String sql =
                "SELECT br.record_id, b.title AS book_title, m.name AS borrower_name, " +
                        "       br.borrow_date, br.return_date, br.status " +
                        "FROM BorrowRecords br " +
                        "  JOIN Books b   ON br.book_id   = b.book_id " +
                        "  JOIN Members m ON br.member_id = m.member_id " +
                        "ORDER BY br.borrow_date DESC";

        List<Map<String,Object>> records = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String,Object> rec = new HashMap<>();
                rec.put("recordId",     rs.getInt("record_id"));
                rec.put("bookTitle",    rs.getString("book_title"));
                rec.put("borrowerName", rs.getString("borrower_name"));
                rec.put("borrowDate",   rs.getDate("borrow_date"));
                rec.put("returnDate",   rs.getDate("return_date"));
                rec.put("status",       rs.getString("status"));
                records.add(rec);
            }
        }
        return records;
    }

    public List<Map<String,Object>> searchByBookOrMember(Connection conn, String term) throws SQLException {
        String sql =
                "SELECT br.record_id, b.title AS book_title, m.name AS borrower_name, " +
                        "       br.borrow_date, br.return_date, br.status " +
                        "FROM BorrowRecords br " +
                        "  JOIN Books b   ON br.book_id   = b.book_id " +
                        "  JOIN Members m ON br.member_id = m.member_id " +
                        "WHERE b.title LIKE ? OR m.name LIKE ? " +
                        "ORDER BY br.borrow_date DESC";

        List<Map<String,Object>> records = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + term + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> rec = new HashMap<>();
                    rec.put("recordId",     rs.getInt("record_id"));
                    rec.put("bookTitle",    rs.getString("book_title"));
                    rec.put("borrowerName", rs.getString("borrower_name"));
                    rec.put("borrowDate",   rs.getDate("borrow_date"));
                    rec.put("returnDate",   rs.getDate("return_date"));
                    rec.put("status",       rs.getString("status"));
                    records.add(rec);
                }
            }
        }
        return records;
    }

    public List<BorrowRecord> getBorrowHistory(Connection conn, int memberId) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = 
            "SELECT * " +
            "FROM BorrowRecords " +
            "WHERE member_id = ? " +
            "ORDER BY borrow_date DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date") != null
                            ? rs.getDate("return_date").toLocalDate()
                            : null,
                        rs.getString("status")
                    ));
                }
            }
        }

        return records;
    }

    // New method to retrieve borrow history with book and author details
    public java.util.List<java.util.Map<String, Object>> getBorrowHistoryDetailed(Connection conn, int memberId) throws SQLException {
        String sql = "SELECT br.record_id, b.title AS bookTitle, a.name AS bookAuthor, b.genre AS bookGenre, " +
                     "       br.borrow_date AS dateBorrowed, br.return_date AS dateReturned " +
                     "FROM BorrowRecords br " +
                     "JOIN Books b ON br.book_id = b.book_id " +
                     "JOIN Authors a ON b.author_id = a.author_id " +
                     "WHERE br.member_id = ? " +
                     "ORDER BY br.borrow_date DESC";
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> rec = new java.util.HashMap<>();
                    rec.put("recordId", rs.getInt("record_id"));
                    rec.put("bookTitle", rs.getString("bookTitle"));
                    rec.put("bookAuthor", rs.getString("bookAuthor"));
                    rec.put("bookGenre", rs.getString("bookGenre"));
                    rec.put("dateBorrowed", rs.getDate("dateBorrowed"));
                    rec.put("dateReturned", rs.getDate("dateReturned"));
                    list.add(rec);
                }
            }
        }
        return list;
    }
}
