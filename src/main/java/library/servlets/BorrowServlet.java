package library.servlets;

import java.io.IOException;
import java.sql.Connection;

import library.dao.BooksDAO;
import library.dao.BorrowRecordsDAO;
import library.model.Book;
import library.util.DBUtil;

// Use jakarta imports to match your web.xml configuration
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/borrowBookView")
public class BorrowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookIdStr = request.getParameter("bookId");
        if (bookIdStr != null) {
            try (Connection conn = DBUtil.getConnection()) {
                int bookId = Integer.parseInt(bookIdStr);
                BooksDAO booksDAO = new BooksDAO();
                // Retrieve book details for display
                Book book = booksDAO.getBookById(conn, bookId);
                request.setAttribute("book", book);
                request.getRequestDispatcher("/views/borrowBookView.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error retrieving book: " + e.getMessage());
                request.getRequestDispatcher("/views/error.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("/views/borrowBookView.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int memberId = Integer.parseInt(request.getParameter("memberId"));

            Connection conn = null;
            try {
                conn = DBUtil.getConnection();

                // Set book as unavailable
                BooksDAO booksDAO = new BooksDAO();
                booksDAO.updateAvailability(conn, bookId, false);

                // Create borrow record
                BorrowRecordsDAO borrowDAO = new BorrowRecordsDAO();
                borrowDAO.borrowBook(conn, bookId, memberId);

                response.sendRedirect(request.getContextPath() + "/bookListView");

            } finally {
                DBUtil.close(conn);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Borrow failed: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp")
                    .forward(request, response);
        }
    }
}
