package library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import library.dao.AuthorsDAO;
import library.model.Author;
import library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageAuthors")
public class ManageAuthorsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Author> authorList;
        try (Connection conn = DBUtil.getConnection()) {
            AuthorsDAO authorsDAO = new AuthorsDAO();
            // Use "searchTerm" from the JSP
            String searchTerm = request.getParameter("searchTerm");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                authorList = authorsDAO.searchByName(conn, searchTerm.trim());
            } else {
                authorList = authorsDAO.getAllAuthors(conn);
            }
            request.setAttribute("authorList", authorList);
            request.getRequestDispatcher("/views/manageAuthorsView.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // allow POST for search as well
        doGet(req, resp);
    }
}
