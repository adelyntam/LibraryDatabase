package library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import library.dao.MembersDAO;
import library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageMemberDelete")
public class ManageMemberDeleteServlet extends HttpServlet {
    private MembersDAO membersDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        membersDAO = new MembersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("memberId");
        if (idParam != null && !idParam.isEmpty()) {
            try (Connection conn = DBUtil.getConnection()) {
                int memberId = Integer.parseInt(idParam);
                membersDAO.deleteMember(conn, memberId);
            } catch (SQLException | NumberFormatException e) {
                throw new ServletException("Unable to delete member", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/manageMember");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("memberId");
        if (idParam != null && !idParam.isEmpty()) {
            try (Connection conn = DBUtil.getConnection()) {
                int memberId = Integer.parseInt(idParam);
                membersDAO.deleteMember(conn, memberId);
            } catch (SQLException e) {
                throw new ServletException("Error deleting member " + idParam, e);
            }
        }
        // After deletion, go back to the member list
        response.sendRedirect(request.getContextPath() + "/manageMember");
    }
}
