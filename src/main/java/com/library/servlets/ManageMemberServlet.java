package com.library.servlets;

import com.library.dao.MembersDAO;
import com.library.model.Member;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/views/manageMember")
public class ManageMemberServlet extends HttpServlet {
    private MembersDAO membersDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        membersDAO = new MembersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String term = searchTerm.trim();
        List<Member> memberList = Collections.emptyList();

        try (Connection conn = DBUtil.getConnection()) {

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                try {
                    // If it parses as an integer, look up by ID
                    int memberId = Integer.parseInt(searchTerm.trim());
                    Member m = membersDAO.getMemberById(conn, memberId);
                    if (m != null) {
                        memberList = List.of(m);
                    }
                } catch (NumberFormatException e) {
                    // Otherwise, search by name
                    memberList = membersDAO.searchByName(conn, term);
                }
            } else {
                // No search term: list all members
                memberList = membersDAO.getAllMembers(conn);
            }
        } catch (SQLException e) {
            throw new ServletException("Error loading member list", e);
        }

        request.setAttribute("memberList", memberList);
        request.getRequestDispatcher("/views/manageMemberView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
