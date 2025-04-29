package com.library.dao;

import com.library.model.BorrowRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
