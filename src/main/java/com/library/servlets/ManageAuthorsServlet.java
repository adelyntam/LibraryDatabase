package com.library.servlets;

import com.library.dao.AuthorsDAO;
import com.library.model.Author;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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
        String term = request.getParameter("searchTerm");
        List<Author> authorList = Collections.emptyList();

        try (Connection conn = DBUtil.getConnection()) {
            if (term != null && !term.trim().isEmpty()) {
                String t = term.trim();
                try {
                    // if it’s a number, look up by ID
                    int id = Integer.parseInt(t);
                    Author a = authorsDAO.getAuthorById(conn, id);
                    if (a != null) {
                        authorList = List.of(a);
                    }
                } catch (NumberFormatException e) {
                    // otherwise search by name
                    authorList = authorsDAO.searchByName(conn, t);
                }
            } else {
                // no term → list all
                authorList = authorsDAO.getAllAuthors(conn);
            }
        } catch (SQLException e) {
            throw new ServletException("Error loading authors", e);
        }

        request.setAttribute("authorList", authorList);
        request.getRequestDispatcher("/views/manageAuthorsView.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // allow POST for search as well
        doGet(req, resp);
    }
}
