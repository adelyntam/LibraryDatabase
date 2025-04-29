package com.library.dao;

import com.library.model.Member;
import java.sql.*;

public class MembersDAO {
    // Add new member, return id
    public int addMember(Connection conn, Member member) throws SQLException {
        String sql = "INSERT INTO Members (name, email, membership_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setDate(3, Date.valueOf(member.getMembershipDate()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // Delete member
    public void deleteMember(Connection conn, int memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.executeUpdate();
        }
    }

    // Get member by ID
    public Member getMemberById(Connection conn, int memberId) throws SQLException {
        String sql = "SELECT * FROM Members WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"), rs.getDate("membership_date").toLocalDate());
                }
            }
            return null;
        }
    }
}
