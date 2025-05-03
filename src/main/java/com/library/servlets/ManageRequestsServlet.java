package com.library.servlets;

import com.library.dao.OrderRequestsDAO;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/updateRequestStatus")
public class ManageRequestsServlet extends HttpServlet {
    private OrderRequestsDAO requestsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestsDAO = new OrderRequestsDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam    = request.getParameter("requestId");
        String newStatus  = request.getParameter("newStatus");

        if (idParam == null || newStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            int requestId = Integer.parseInt(idParam);
            try (Connection conn = DBUtil.getConnection()) {
                requestsDAO.fulfillRequest(conn, requestId);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid requestId: " + idParam, e);
        } catch (SQLException e) {
            throw new ServletException("Unable to update request status", e);
        }

        // After updating, go back to the manage‐requests list
        response.sendRedirect(request.getContextPath() + "/manageRequests");
    }
}
