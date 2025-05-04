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

@WebServlet("/bookListView")
public class AllBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTitle = request.getParameter("searchTitle");
        List<Book> books;

        try (Connection conn = DBUtil.getConnection()) {
            BooksDAO booksDAO = new BooksDAO();

            if (searchTitle != null && !searchTitle.trim().isEmpty()) {
                // search by title
                books = booksDAO.searchByTitle(conn, searchTitle.trim());
            } else {
                // show all on initial load or empty search
                books = booksDAO.getAllBooks(conn);
            }

            request.setAttribute("bookList", books);
            request.getRequestDispatcher("/views/bookListView.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading books", e);
        }
    }
}
