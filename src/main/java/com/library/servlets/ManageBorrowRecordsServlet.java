package com.library.servlets;

import com.library.dao.BorrowRecordsDAO;
import com.library.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/manageBorrowRecords")
public class ManageBorrowRecordsServlet extends HttpServlet {
    private BorrowRecordsDAO borrowDao;

    @Override
    public void init() throws ServletException {
        super.init();
        borrowDao = new BorrowRecordsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String term = request.getParameter("searchTerm");
        List<Map<String,Object>> recordList;

        try (Connection conn = DBUtil.getConnection()) {
            if (term != null && !term.trim().isEmpty()) {
                recordList = borrowDao.searchByBookOrMember(conn, term.trim());
            } else {
                recordList = borrowDao.getAllBorrowRecordsWithDetails(conn);
            }
        } catch (SQLException e) {
            throw new ServletException("Error loading borrow records", e);
        }

        request.setAttribute("recordList", recordList);
        request.getRequestDispatcher("/views/manageBorrowRecordsView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // allow POST-powered search too
        doGet(req, resp);
    }
}
