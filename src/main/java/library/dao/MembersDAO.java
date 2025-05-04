package library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import library.model.Member;

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

    public List<Member> getAllMembers(Connection conn) throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM Members ORDER BY member_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("membership_date").toLocalDate()
                ));
            }
        }
        return list;
    }

    public void updateMember(Connection conn, Member m) throws SQLException {
        String sql = "UPDATE Members SET name=?, email=?, membership_date=? WHERE member_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getName());
            stmt.setString(2, m.getEmail());
            stmt.setDate(3, Date.valueOf(m.getMembershipDate()));
            stmt.setInt(4, m.getMemberId());
            stmt.executeUpdate();
        }
    }

    // Search members by (partial) name match
    public List<Member> searchByName(Connection conn, String namePattern) throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM Members WHERE name LIKE ? ORDER BY member_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + namePattern + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Member(
                            rs.getInt("member_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getDate("membership_date").toLocalDate()
                    ));
                }
            }
        }
        return list;
    }


}
