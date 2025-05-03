package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/requestBook")
public class RequestBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the "Request a New Book" form
        request.getRequestDispatcher("/views/requestBookView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookTitle  = request.getParameter("bookTitle");
        String authorName = request.getParameter("authorName");

        try (Connection conn = DBUtil.getConnection()) {
            // Insert the new book request into the database
            String sql = "INSERT INTO book_requests (title, author) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookTitle);
                stmt.setString(2, authorName);
                stmt.executeUpdate();
            }

            // Redirect back to the book list (or you could redirect to a confirmation page)
            response.sendRedirect(request.getContextPath() + "/requestBook");
        } catch (SQLException e) {
            throw new ServletException("Error saving book request", e);
        }
    }
}
