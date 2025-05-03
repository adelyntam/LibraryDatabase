package com.library.servlets;

import com.library.dao.BooksDAO;
import com.library.dao.BorrowRecordsDAO;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/returnBook")
public class ReturnBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            // 1. Mark the book as available
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateAvailability(conn, bookId, true);

            // 2. Mark the borrow record as returned
            BorrowRecordsDAO borrowDAO = new BorrowRecordsDAO();
            borrowDAO.returnBook(conn, recordId);

            response.sendRedirect("/views/bookListView.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Return failed: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            DBUtil.close(conn);
        }
    }
}
