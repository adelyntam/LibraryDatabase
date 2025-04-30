package com.library.servlets;

import com.library.dao.BooksDAO;
import com.library.dao.BorrowRecordsDAO;
import com.library.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/borrowBookView")
public class BorrowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int memberId = Integer.parseInt(request.getParameter("memberId"));

            Connection conn = null;
            try {
                conn = DBUtil.getConnection();

                // 1. Set book as unavailable
                BooksDAO booksDAO = new BooksDAO();
                booksDAO.updateAvailability(conn, bookId, false);

                // 2. Create borrow record
                BorrowRecordsDAO borrowDAO = new BorrowRecordsDAO();
                borrowDAO.borrowBook(conn, bookId, memberId);

                response.sendRedirect("bookListView.jsp");

            } finally {
                DBUtil.close(conn);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Borrow failed: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
