package library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import library.dao.BooksDAO;
import library.model.Book;
import library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageBooks")
public class ManageBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> bookList;

        try (Connection conn = DBUtil.getConnection()) {
            BooksDAO booksDAO = new BooksDAO();
            String searchTerm = request.getParameter("searchTerm");

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                bookList = booksDAO.searchByTitle(conn, searchTerm.trim());
            } else {
                bookList = booksDAO.getAllBooks(conn);
            }

            request.setAttribute("bookList", bookList);
            request.getRequestDispatcher("/views/manageBooksView.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
