package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import library.model.Author;
import library.model.OrderRequest;

public class OrderRequestsDAO {
    // Create new request, return id
    public int createRequest(Connection conn, OrderRequest request) throws SQLException {
        String sql = "INSERT INTO OrderRequests (member_id, book_title, author_name, status) " + "VALUES ( ?, ?, ?, 'pending')";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getMemberId());
            stmt.setString(2, request.getBookTitle());
            stmt.setString(3, request.getAuthorName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Fulfill request (mark as fulfilled) and insert book into Books table
    public void fulfillRequest(Connection conn, int requestId) throws SQLException {
        // Retrieve the order request details
        OrderRequest request = getRequestById(conn, requestId);
        if (request == null) {
            throw new SQLException("Order request not found with ID: " + requestId);
        }
        // Mark the order as fulfilled
        String sqlUpdate = "UPDATE OrderRequests SET status = 'fulfilled' WHERE request_id = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setInt(1, requestId);
            stmtUpdate.executeUpdate();
        }
        
        // Use AuthorsDAO to check if an author with the same name exists
        AuthorsDAO authorsDao = new AuthorsDAO();
        Integer authorId = authorsDao.findAuthorIdByName(conn, request.getAuthorName());
        if (authorId == null) {
            // Create a new author
            Author newAuthor = new Author(0, request.getAuthorName(), "Unknown");
            authorId = authorsDao.addAuthor(conn, newAuthor);
            if (authorId == -1) {
                throw new SQLException("Failed to create new author");
            }
        }
        
        // Check if the book already exists by title and author_id
        String checkSql = "SELECT book_id FROM Books WHERE title = ? AND author_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, request.getBookTitle());
            checkStmt.setInt(2, authorId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Book already exists – skip insertion 
                    return;
                }
            }
        }
        
        // Insert the new book into the Books table linking the authorID
        String sqlInsert = "INSERT INTO Books (title, author_id) VALUES (?, ?)";
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, request.getBookTitle());
            stmtInsert.setInt(2, authorId);
            stmtInsert.executeUpdate();
        }
    }

    // Delete request
    public void deleteRequest(Connection conn, int requestId) throws SQLException {
        String sql = "DELETE FROM OrderRequests WHERE request_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        }
    }

    // Get request by ID
    public OrderRequest getRequestById(Connection conn, int requestId) throws SQLException {
        String sql = "SELECT * FROM OrderRequests WHERE request_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderRequest(rs.getInt("request_id"), rs.getInt("member_id"), rs.getString("book_title"), rs.getString("author_name"), rs.getDate("request_date").toLocalDate(), rs.getString("status"));
                }
            }
            return null;
        }
    }

    // Get pending requests
    public List<OrderRequest> getPendingRequests(Connection conn) throws SQLException {
        List<OrderRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM OrderRequests WHERE status = 'pending'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(new OrderRequest(rs.getInt("request_id"), rs.getInt("member_id"), rs.getString("book_title"), rs.getString("author_name"), rs.getDate("request_date").toLocalDate(), rs.getString("status")));
            }
        }
        return requests;
    }
    public List<OrderRequest> getAllRequests(Connection conn) throws SQLException {
        List<OrderRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM OrderRequests";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(new OrderRequest(rs.getInt("request_id"), rs.getInt("member_id"), rs.getString("book_title"), rs.getString("author_name"), rs.getDate("request_date").toLocalDate(), rs.getString("status")));
            }
        }
        return requests;
    }
}
