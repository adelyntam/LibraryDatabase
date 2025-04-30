package com.library.servlets;

import com.library.dao.BooksDAO;
import com.library.model.Book;
import com.library.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/bookListView")
public class AllBooksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection(); // use your DBUtil class

            BooksDAO booksDAO = new BooksDAO();
            List<Book> books = booksDAO.getAllBooks(conn); // ✅ must include ALL books

            request.setAttribute("bookList", books); // ✅ set for use in JSP
            request.getRequestDispatcher("bookListView.jsp").forward(request, response); // ✅ correct JSP

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading books: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            DBUtil.close(conn); // always close connection
        }
    }
}
