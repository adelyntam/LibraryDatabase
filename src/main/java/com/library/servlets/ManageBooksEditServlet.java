package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.library.dao.BooksDAO;
import com.library.model.Book;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageBooksEdit")
public class ManageBooksEditServlet extends HttpServlet {
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        booksDAO = new BooksDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("bookId");
        Book book = new Book();
        if (idParam != null && !idParam.isEmpty()) {
            try (Connection conn = DBUtil.getConnection()) {
                int bookId = Integer.parseInt(idParam);
                Book b = booksDAO.getBookById(conn, bookId);
                if (b != null) book = b;
            } catch (NumberFormatException | SQLException e) {
                throw new ServletException("Unable to load book", e);
            }
        }
        request.setAttribute("book", book);
        request.getRequestDispatcher("/views/manageBooksEditView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam     = request.getParameter("bookId");
        String title       = request.getParameter("title");
        String authorIdStr = request.getParameter("authorId");
        String genre       = request.getParameter("genre");
        String yearStr     = request.getParameter("publishYear");

        try (Connection conn = DBUtil.getConnection()) {
            int authorId   = Integer.parseInt(authorIdStr);
            int publishYear = Integer.parseInt(yearStr);

            if (idParam != null && !idParam.isEmpty()) {
                // update existing
                int bookId = Integer.parseInt(idParam);
                booksDAO.updateBook(conn,
                        new Book(bookId, title, authorId, genre, publishYear, true));
            } else {
                // create new (available = true by default) default bookID is 0 i guess
                booksDAO.addBook(conn,
                        new Book(0,title, authorId, genre, publishYear, true));
            }

            response.sendRedirect(request.getContextPath() + "/manageBooks");
        } catch (SQLException e) {
            throw new ServletException("Unable to save book", e);
        }
    }
}
