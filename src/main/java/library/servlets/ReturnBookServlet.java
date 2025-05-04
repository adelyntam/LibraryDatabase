package library.servlets;

import java.io.IOException;
import java.sql.Connection;

import library.dao.BooksDAO;
import library.dao.BorrowRecordsDAO;
import library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/returnBook")
public class ReturnBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward GET requests to the returnBookView.jsp page.
        request.getRequestDispatcher("/views/returnBookView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            // Mark the book as available
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateAvailability(conn, bookId, true);

            // Mark the borrow record as returned
            BorrowRecordsDAO borrowDAO = new BorrowRecordsDAO();
            borrowDAO.returnBook(conn, recordId);

            // Redirect with context path
            response.sendRedirect(request.getContextPath() + "/bookListView");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Return failed: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            DBUtil.close(conn);
        }
    }
}
