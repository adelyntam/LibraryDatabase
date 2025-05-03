package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.library.dao.MembersDAO;
import com.library.model.Member;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageMember")
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
        try (Connection conn = DBUtil.getConnection()) {
            String searchTerm = request.getParameter("search");
            
            List<Member> members;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                members = membersDAO.searchByName(conn, searchTerm);
            } else {
                members = membersDAO.getAllMembers(conn);
            }
            
            request.setAttribute("memberList", members);
            request.getRequestDispatcher("/views/manageMemberView.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
