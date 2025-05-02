package com.library.servlets;

import com.library.dao.BooksDAO;
import com.library.model.Book;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/manageBooks")
public class ManageBooksServlet extends HttpServlet {
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        booksDAO = new BooksDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String term = request.getParameter("searchTerm");
        List<Book> bookList = Collections.emptyList();

        try (Connection conn = DBUtil.getConnection()) {
            if (term != null && !term.trim().isEmpty()) {
                String t = term.trim();
                try {
                    // if it's a number, search by ID
                    int id = Integer.parseInt(t);
                    Book b = booksDAO.getBookById(conn, id);
                    if (b != null) {
                        bookList = List.of(b);
                    }
                } catch (NumberFormatException e) {
                    // otherwise search by title
                    bookList = booksDAO.searchByTitle(conn, t);
                }
            } else {
                // no term → list all
                bookList = booksDAO.getAllBooks(conn);
            }
        } catch (SQLException e) {
            throw new ServletException("Error loading book list", e);
        }

        request.setAttribute("bookList", bookList);
        request.getRequestDispatcher("/views/manageBooksView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
