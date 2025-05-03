package com.library.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.library.dao.BorrowRecordsDAO;
import com.library.model.BorrowRecord;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/historyView")
public class HistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Parse memberId parameter
            int memberId = Integer.parseInt(request.getParameter("memberId"));

            Connection conn = null;
            try {
                // 2. Open JDBC connection
                conn = DBUtil.getConnection();

                // 3. Load borrow history
                BorrowRecordsDAO dao = new BorrowRecordsDAO();
                List<BorrowRecord> historyList = dao.getActiveBorrows(conn, memberId);

                // 4. Expose to JSP
                request.setAttribute("historyList", historyList);

                // 5. Forward to your Bootstrap‐styled JSP
                request.getRequestDispatcher("/views/historyView.jsp")
                        .forward(request, response);

            } finally {
                DBUtil.close(conn);
            }

        } catch (NumberFormatException e) {
            // invalid memberId
            request.setAttribute("error", "Invalid MemberID");
            request.getRequestDispatcher("/views/error.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            // SQL or other errors
            e.printStackTrace();
            request.setAttribute("error", "Could not load history: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp")
                    .forward(request, response);
        }
    }
}
