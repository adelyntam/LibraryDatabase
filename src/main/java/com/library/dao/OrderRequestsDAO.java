package com.library.dao;
// translate objects to queries, vice versa
// handle CRUD

import com.library.model.OrderRequest;
import com.library.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRequestsDAO {
    // Create new request
    public int createRequest(OrderRequest request) throws SQLException {
        String sql = "INSERT INTO OrderRequests (member_id, book_title, author_name, request_date, status) " + "VALUES (?, ?, ?, ?, 'pending')";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getMemberId());
            stmt.setString(2, request.getBookTitle());
            stmt.setString(3, request.getAuthorName());
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Fulfill request (mark as fulfilled)
    public void fulfillRequest(int requestId) throws SQLException {
        String sql = "UPDATE OrderRequests SET status = 'fulfilled' WHERE request_id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        }
    }

    // Delete request
    public void deleteRequest(int requestId) throws SQLException {
        String sql = "DELETE FROM OrderRequests WHERE request_id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        }
    }

    // Get request by ID
    public OrderRequest getRequestById(int requestId) throws SQLException {
        String sql = "SELECT * FROM OrderRequests WHERE request_id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderRequest(
                        rs.getInt("request_id"),
                        rs.getInt("member_id"),
                        rs.getString("book_title"),
                        rs.getString("author_name"),
                        rs.getDate("request_date").toLocalDate(),
                        rs.getString("status")
                    );
                }
            }
            return null;
        }
    }

    // Get pending requests
    public List<OrderRequest> getPendingRequests() throws SQLException {
        List<OrderRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM OrderRequests WHERE status = 'pending'";
        try (Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(new OrderRequest(
                        rs.getInt("request_id"),
                        rs.getInt("member_id"),
                        rs.getString("book_title"),
                        rs.getString("author_name"),
                        rs.getDate("request_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        }
        return requests;
    }
}
