package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.library.dao.AuthorsDAO;
import com.library.model.Author;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageAuthors")
public class ManageAuthorsServlet extends HttpServlet {
    private AuthorsDAO authorsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        authorsDAO = new AuthorsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Author> authorList;

        try (Connection conn = DBUtil.getConnection()) {
            String searchName = request.getParameter("searchName");

            if (searchName != null && !searchName.trim().isEmpty()) {
                authorList = authorsDAO.searchByName(conn, searchName.trim());
            } else {
                authorList = authorsDAO.getAllAuthors(conn);
            }

            request.setAttribute("authorList", authorList);
            request.getRequestDispatcher("/views/manageAuthorsView.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // allow POST for search as well
        doGet(req, resp);
    }
}
