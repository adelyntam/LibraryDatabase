package library.servlets;

import java.io.IOException;
import java.sql.Connection;

import library.dao.BorrowRecordsDAO;
import library.util.DBUtil;

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
        String memberIdParam = request.getParameter("memberId");
        // If no memberId is provided, show the history page without error
        if (memberIdParam == null || memberIdParam.trim().isEmpty()) {
            request.setAttribute("historyList", new java.util.ArrayList<>());
            request.getRequestDispatcher("/views/historyView.jsp")
                .forward(request, response);
            return;
        }
        
        try {
            int memberId = Integer.parseInt(memberIdParam);
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                // Use the new detailed method returning joined results
                BorrowRecordsDAO dao = new BorrowRecordsDAO();
                java.util.List<java.util.Map<String, Object>> historyList = dao.getBorrowHistoryDetailed(conn, memberId);
                request.setAttribute("historyList", historyList);
                request.getRequestDispatcher("/views/historyView.jsp")
                       .forward(request, response);
            } finally {
                DBUtil.close(conn);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid MemberID");
            request.getRequestDispatcher("/views/error.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Could not load history: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp")
                   .forward(request, response);
        }
    }
}
