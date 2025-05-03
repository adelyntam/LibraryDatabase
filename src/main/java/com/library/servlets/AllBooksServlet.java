package com.library.servlets;

import com.library.dao.BooksDAO;
import com.library.model.Book;
import com.library.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
                // show all
                books = booksDAO.getAllBooks(conn);
            }
        } catch (SQLException e) {
            throw new ServletException("Error loading books", e);
        }

        request.setAttribute("bookList", books);
        request.getRequestDispatcher("/views/bookListView.jsp")
                .forward(request, response);
    }
}
