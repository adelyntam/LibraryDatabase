package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.library.dao.MembersDAO;
import com.library.model.Member;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageMemberEdit")
public class ManageMemberEditServlet extends HttpServlet {
    private MembersDAO membersDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        membersDAO = new MembersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("memberId");
        Member member = new Member();
        // if editing, load from DB
        if (idParam != null && !idParam.isEmpty() && !idParam.equals("0")) {
            try (Connection conn = DBUtil.getConnection()) {
                int memberId = Integer.parseInt(idParam);
                Member m = membersDAO.getMemberById(conn, memberId);
                if (m != null) {
                    member = m;
                }
            } catch (NumberFormatException | SQLException e) {
                throw new ServletException("Unable to load member for edit", e);
            }
        }
        request.setAttribute("member", member);
        request.getRequestDispatcher("/views/manageMemberEditView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("memberId");
        String name  = request.getParameter("name");
        String email = request.getParameter("email");
        String date  = request.getParameter("membershipDate");

        // parse date or default to today
        LocalDate membershipDate;
        try {
            membershipDate = (date != null && !date.isEmpty())
                    ? LocalDate.parse(date)
                    : LocalDate.now();
        } catch (Exception e) {
            membershipDate = LocalDate.now();
        }

        try (Connection conn = DBUtil.getConnection()) {
            if (idParam != null && !idParam.isEmpty() && !idParam.equals("0")) {
                // EDIT existing member
                int memberId = Integer.parseInt(idParam);
                membersDAO.updateMember(conn,
                        new Member(memberId, name, email, membershipDate));
            } else {
                // CREATE new member
                membersDAO.addMember(conn,
                        new Member(name, email, membershipDate));
            }

            // back to list
            response.sendRedirect(request.getContextPath() + "/manageMember");

        } catch (SQLException e) {
            throw new ServletException("Unable to save member", e);
        }
    }
}
