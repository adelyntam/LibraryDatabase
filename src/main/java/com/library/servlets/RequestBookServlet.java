package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

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
        String member_id = request.getParameter("member_id");
        String book_title  = request.getParameter("book_title");
        String author_name = request.getParameter("author_name");
        LocalDate requestDate = LocalDate.now();

        try (Connection conn = DBUtil.getConnection()) {
            // Insert the new book request into the database
            String sql = "INSERT INTO OrderRequests (member_id,book_title, author_name) VALUES (?,?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(member_id));
                stmt.setString(2, book_title);
                stmt.setString(3, author_name);
                stmt.setDate(4, java.sql.Date.valueOf(requestDate));
                stmt.executeUpdate();
            }

            // Redirect back to the book list (or you could redirect to a confirmation page)
            response.sendRedirect(request.getContextPath() + "/requestBook");
        } catch (SQLException e) {
            throw new ServletException("Error saving book request", e);
        }
    }
}
