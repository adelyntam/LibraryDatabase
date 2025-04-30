package com.library.servlet;

import com.library.dao.BooksDAO;
import com.library.dao.BorrowRecordsDAO;
import com.library.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/returnBookView")
public class ReturnBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
