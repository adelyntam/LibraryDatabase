package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.library.dao.OrderRequestsDAO;
import com.library.model.OrderRequest;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageRequestBook")
public class ManageRequestsServlet extends HttpServlet {
    private OrderRequestsDAO requestsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestsDAO = new OrderRequestsDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = DBUtil.getConnection()) {
            // Retrieve pending requests (adjust if you want all requests)
            List<OrderRequest> requestList = requestsDAO.getAllRequests(conn);
            request.setAttribute("requestList", requestList);
            request.getRequestDispatcher("/views/manageRequestBookView.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load requests", e);
        }
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
        response.sendRedirect(request.getContextPath() + "/manageRequestBook");
    }
}
