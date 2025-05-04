package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.library.dao.AuthorsDAO;
import com.library.model.Author;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageAuthorsEdit")
public class ManageAuthorsEditServlet extends HttpServlet {
    private AuthorsDAO authorsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        authorsDAO = new AuthorsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("authorId");
        Author author = new Author();

        if (idParam != null && !idParam.isEmpty()) {
            try (Connection conn = DBUtil.getConnection()) {
                int authorId = Integer.parseInt(idParam);
                Author a = authorsDAO.getAuthorById(conn, authorId);
                if (a != null) {
                    author = a;
                }
            } catch (NumberFormatException | SQLException e) {
                throw new ServletException("Unable to load author for edit", e);
            }
        }

        request.setAttribute("author", author);
        request.getRequestDispatcher("/views/manageAuthorsEditView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam      = request.getParameter("authorId");
        String name         = request.getParameter("name");
        String nationality  = request.getParameter("nationality");

        try (Connection conn = DBUtil.getConnection()) {
            if (idParam != null && !idParam.isEmpty()) {
                // update existing author
                int authorId = Integer.parseInt(idParam);
                authorsDAO.updateAuthor(conn, new Author(authorId, name, nationality));
            } else {
                // create new author
                authorsDAO.addAuthor(conn, new Author(0, name, nationality));
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to save author", e);
        }

        response.sendRedirect(request.getContextPath() + "/manageAuthors");
    }
}
