package com.library.servlets;

import com.library.service.LibService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/bookListView")
public class AllBooksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LibService lib = new LibService();
            List<Map<String, Object>> books = lib.getAvailableBooksWithAuthors();

            System.out.println("DEBUG: Attempting to forward to JSP");
            String jspPath = getServletContext().getRealPath("/views/bookListView.jsp");
            System.out.println("JSP physical path: " + jspPath);
            System.out.println("Book data: " + books);

            request.setAttribute("bookList", books);
            request.getRequestDispatcher("/views/bookListView.jsp").forward(request, response); // ✅ correct JSP

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading books: " + e.getMessage());
            request.setAttribute("javax.servlet.error.exception", e);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
